const utils = require('../migration-utils');

const config = utils.readInput();
const data = config.data;
if (data.type === 'symbols') {
	migrateSymbols(data.symbols);
} else if (data.type === 'symbolGroups') {
    migrateGroups(data.symbolGroups);
} else {
	console.error('The input file does not seem to contain any symbols.');
	return 0;
}

function migrateSymbols(symbols) {
	symbols.forEach(symbol => {
		symbol.steps = symbol.actions.map(action => {
			const disabled = action.disabled;
			const negated = action.negated;
			const ignoreFailure = action.ignoreFailure;
			delete action.disabled;
			delete action.negated;
			delete action.ignoreFailure;
			return {
				type: "action",
				disabled: disabled,
				action: action,
                negated: negated,
                ignoreFailure: ignoreFailure
			}
		});
		delete symbol.actions;
	});
}

function migrateGroups(groups) {
	groups.forEach(group => {
        migrateSymbols(group.symbols);
        migrateGroups(group.groups);
	})
}

utils.writeOutput(config, '1.6.0');
