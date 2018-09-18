import {Action} from '../../../../src/js/entities/actions/action';

describe('Action', () => {
    const type = 'someType';

    beforeEach(angular.mock.module('ALEX'));

    it('should correctly create a default action and have a default toString method', () => {
        const action = new Action(type, {});
        const expectedAction = {
            type: type,
        };

        expect(angular.toJson(action)).toEqual(angular.toJson(expectedAction));
        expect(action.toString().search(type) > -1).toBe(true);
        expect(angular.isFunction(action.toString)).toBe(true);
    });

    it('should correctly create an action from given data', () => {
        const action = new Action(type, {
        });
        const expectedAction = {
            type: type,
        };

        expect(angular.toJson(action)).toEqual(angular.toJson(expectedAction));
    })
});
