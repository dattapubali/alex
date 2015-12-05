describe('SymbolResource', () => {
    let $http, $httpBackend;
    let SymbolResource, Symbol,SymbolFormModel;
    let project;

    beforeEach(module('ALEX'));
    beforeEach(inject(($injector) => {
        $http = $injector.get('$http');
        $httpBackend = $injector.get('$httpBackend');
        SymbolResource = $injector.get('SymbolResource');
        Symbol = $injector.get('Symbol');
        SymbolFormModel = $injector.get('SymbolFormModel');

        project = ENTITIES.projects[0];
    }));

    it('should correctly initialize the resource', () => {
        expect(SymbolResource.$http).toEqual($http);
    });

    it('should get a single symbol and return an instance of a symbol', () => {
        spyOn($http, 'get').and.callThrough();

        const symbol = ENTITIES.symbols[0];
        const uri = `/rest/projects/${project.id}/symbols/${symbol.id}`;
        $httpBackend.whenGET(uri).respond(200, ENTITIES.symbols[0]);
        const promise = SymbolResource.get(project.id, symbol.id);
        $httpBackend.flush();

        expect($http.get).toHaveBeenCalledWith(uri);
        promise.then((s) => {
            expect(s instanceof Symbol).toBe(true)
        })
    });

    it('should get all symbols and create instances of them', () => {
        spyOn($http, 'get').and.callThrough();

        const uri = `/rest/projects/${project.id}/symbols`;

        $httpBackend.whenGET(uri).respond(200, ENTITIES.symbols);
        const promise = SymbolResource.getAll(project.id);
        $httpBackend.flush();

        expect($http.get).toHaveBeenCalledWith(uri);
        promise.then((symbols) => {
            symbols.forEach(s => expect(s instanceof Symbol).toBe(true));
        })
    });

    it('should get a list of symbols by given id revision pairs', () => {

    });

    it('should get all revisions of a symbol', () => {

    });

    it('should create a new symbol', () => {
        spyOn($http, 'post').and.callThrough();

        const uri = `/rest/projects/${project.id}/symbols`;
        const symbol = new SymbolFormModel();
        $httpBackend.whenPOST(uri).respond(201, ENTITIES.symbols[0]);
        const promise = SymbolResource.create(project.id, symbol);
        $httpBackend.flush();

        expect($http.post).toHaveBeenCalledWith(uri, symbol);
        promise.then((symbol) => {
            expect(symbol instanceof Symbol).toBe(true);
        })
    });

    it('should create multiple symbols at once', () => {
        spyOn($http, 'post').and.callThrough();

        const uri = `/rest/projects/${project.id}/symbols/batch`;
        const symbols = [new SymbolFormModel(), new SymbolFormModel(), new SymbolFormModel()];
        $httpBackend.whenPOST(uri).respond(201, ENTITIES.symbols);
        const promise = SymbolResource.createMany(project.id, symbols);
        $httpBackend.flush();

        expect($http.post).toHaveBeenCalledWith(uri, symbols);
        promise.then((symbols) => {
            symbols.forEach(s => expect(s instanceof Symbol).toBe(true));
        })
    });

    it('should move many symbols to another group', () => {

    });

    it('should update a single symbol', () => {

    });

    it('should remove a single symbol', () => {
        spyOn($http, 'post').and.callThrough();

        const symbol = new Symbol(ENTITIES.symbols[0]);
        const uri = `/rest/projects/${project.id}/symbols/${symbol.id}/hide`;

        $httpBackend.whenPOST(uri).respond(200, {});
        const promise = SymbolResource.remove(symbol);
        $httpBackend.flush();

        expect($http.post).toHaveBeenCalledWith(uri, {});
        expect(promise.then).toBeDefined();
    });

    it('should remove many symbols', () => {
        spyOn($http, 'post').and.callThrough();

        const symbols = ENTITIES.symbols.map(s => new Symbol(s));
        const ids = symbols.map(s => s.id).join(',');
        const uri = `/rest/projects/${project.id}/symbols/batch/${ids}/hide`;

        $httpBackend.whenPOST(uri).respond(200, {});
        const promise = SymbolResource.removeMany(symbols);
        $httpBackend.flush();

        expect($http.post).toHaveBeenCalledWith(uri, {});
        expect(promise.then).toBeDefined();
    });

    it('should recover a single symbols', () => {
        spyOn($http, 'post').and.callThrough();

        const symbol = new Symbol(ENTITIES.symbols[0]);
        const uri = `/rest/projects/${project.id}/symbols/${symbol.id}/show`;

        $httpBackend.whenPOST(uri).respond(200, {});
        const promise = SymbolResource.recover(symbol);
        $httpBackend.flush();

        expect($http.post).toHaveBeenCalledWith(uri, {});
        expect(promise.then).toBeDefined();
    });

    it('should recover many symbols', () => {
        spyOn($http, 'post').and.callThrough();

        const symbols = ENTITIES.symbols.map(s => new Symbol(s));
        const ids = symbols.map(s => s.id).join(',');
        const uri = `/rest/projects/${project.id}/symbols/batch/${ids}/show`;

        $httpBackend.whenPOST(uri).respond(200, {});
        const promise = SymbolResource.recoverMany(symbols);
        $httpBackend.flush();

        expect($http.post).toHaveBeenCalledWith(uri, {});
        expect(promise.then).toBeDefined();
    });
});