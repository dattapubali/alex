import {Project} from '../../../../app/modules/entities/Project';
import {SymbolGroupFormModel} from '../../../../app/modules/entities/SymbolGroup';
import {events} from '../../../../app/modules/constants';
import {SymbolGroupCreateModalController} from '../../../../app/modules/directives/modals/symbolGroupCreateModalHandle';

describe('SymbolGroupCreateModalController', () => {
    let SessionService;
    let SymbolGroupResource;
    let $controller;
    let EventBus;
    let ToastService;
    let scope;

    let controller;
    let project;
    let modalInstance;
    let deferred;

    beforeEach(angular.mock.module('ALEX'));
    beforeEach(angular.mock.inject((_$controller_, $rootScope, _SessionService_, _SymbolGroupResource_, _EventBus_,
                       _ToastService_, _$q_) => {

        SessionService = _SessionService_;
        scope = $rootScope.$new();
        SymbolGroupResource = _SymbolGroupResource_;
        $controller = _$controller_;
        EventBus = _EventBus_;
        ToastService = _ToastService_;

        modalInstance = {
            close: jasmine.createSpy('modalInstance.close'),
            dismiss: jasmine.createSpy('modalInstance.dismiss'),
            result: {
                then: jasmine.createSpy('modalInstance.result.then')
            }
        };

        project = new Project(ENTITIES.projects[0]);
        SessionService.saveProject(project);
        deferred = _$q_.defer();
    }));

    afterEach(() => {
        sessionStorage.removeItem('project');
    });

    function createController() {
        controller = $controller(SymbolGroupCreateModalController, {
            $modalInstance: modalInstance,
            SessionService: SessionService,
            SymbolGroupResource: SymbolGroupResource,
            ToastService: ToastService,
            EventBus: EventBus
        });
    }

    it('should initialize the controller correctly', () => {
        createController();
        expect(controller.project).toEqual(project);
        expect(controller.group).toEqual(new SymbolGroupFormModel());
        expect(controller.errorMsg).toBeNull();
    });

    it('should dismiss the modal window on close', () => {
        createController();
        controller.close();
        expect(modalInstance.dismiss).toHaveBeenCalled();
    });

    it('should correctly create a new group and close the modal', () => {
        createController();
        spyOn(SymbolGroupResource, 'create').and.returnValue(deferred.promise);
        spyOn(EventBus, 'emit');
        spyOn(ToastService, 'success');

        const group = new SymbolGroupFormModel(ENTITIES.groups[0]);
        deferred.resolve(ENTITIES.groups[0]);

        controller.group = group;
        controller.createGroup();
        scope.$digest();

        expect(SymbolGroupResource.create).toHaveBeenCalledWith(project.id, group);
        expect(ToastService.success).toHaveBeenCalled();
        expect(EventBus.emit).toHaveBeenCalledWith(events.GROUP_CREATED, {group: ENTITIES.groups[0]});
        expect(modalInstance.dismiss).toHaveBeenCalled();
        expect(controller.errorMsg).toBeNull();
    });

    it('should fail to create a new group and display an error message', () => {
        createController();
        spyOn(SymbolGroupResource, 'create').and.returnValue(deferred.promise);

        const message = 'failed';
        const group = new SymbolGroupFormModel(ENTITIES.groups[0]);
        deferred.reject({data: {message: message}});

        controller.group = group;
        controller.createGroup();
        scope.$digest();

        expect(SymbolGroupResource.create).toHaveBeenCalledWith(project.id, group);
        expect(controller.errorMsg).toEqual(message);
    });
});