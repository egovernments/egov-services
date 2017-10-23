const getType = require('../utilities/utility').getType;
const getTitleCase = require('../utilities/utility').getTitleCase;
const setLabels = require('../utilities/utility').setLabels;

let localeFields = {};
let errors = {};
let searchTemplate = function (module, numCols, path, config, definition) {
	localeFields[module + ".search.title"] = getTitleCase("search");
	let specifications = {
		numCols: numCols,
		useTimestamp: true,
		objectName: '',
		groups: [{
			"name": "search",
			"label": module + ".search.title",
			"fields": []
		}],
		result: {
			header: [],
			values: [],
			resultPath: ""
		}
	};
	let fields = {};
	let parameterConfig = config["post"].parameters;
	for(let i=0; i<parameterConfig.length; i++) {
		if(parameterConfig[i].$ref && !/requestInfo|tenantId|pageSize|pageNumber|sortResult/.test(parameterConfig[i].$ref)) {
			let splitArr = parameterConfig[i].$ref.split("/");
			let paramKey = splitArr[splitArr.length-1];
			localeFields[module + ".create" + paramKey] = getTitleCase(paramKey);
			specifications.groups[0].fields.push({
				"name": paramKey,
				"jsonPath": paramKey,
				"label": module + ".create" + paramKey,
				"pattern": definition[paramKey].pattern,
				"type": definition[paramKey].enum ? "singleValueList" : definition[paramKey].format == "date" ? "datePicker" : getType(definition[paramKey].type),
				"isRequired": definition[paramKey].required,
				"isDisabled": definition[paramKey].readOnly ? true : false,
				"defaultValue": definition[paramKey].default,
				"maxLength": definition[paramKey].maxLength,
				"minLength": definition[paramKey].minLength,
				"patternErrorMsg": module + ".create.field.message." + paramKey
			});
		}
	}

	setLabels(localeFields, "./output/" + module);
	return {specifications: specifications};
}

module.exports = searchTemplate;