var utilities = require('../utilities/utility');
const getType = utilities.getType;
const getTitleCase = utilities.getTitleCase;
const setLabels = utilities.setLabels;
const getQuery = utilities.getQuery;

let localeFields = {};
let errors = {};
let searchTemplate = function (module, numCols, path, config, definition, uiInfoDef) {
	localeFields[module + ".search.title"] = getTitleCase("search");
	let specifications = {
		numCols: numCols,
		useTimestamp: true,
		objectName: '',
		url: path,
		groups: [{
			"name": "search",
			"label": module + ".search.title",
			"fields": []
		}],
		result: {
			header: [],
			values: [],
			resultPath: "",
			rowClickUrlUpdate: "",
			rowClickUrlView: ""
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

	if(uiInfoDef.SearchResult && Object.keys(uiInfoDef.SearchResult).length) {
		specifications.result.resultPath = uiInfoDef.SearchResult.resultObjectName;
		specifications.result.rowClickUrlView = uiInfoDef.SearchResult.rowClickUrlView;
		specifications.result.rowClickUrlUpdate = uiInfoDef.SearchResult.rowClickUrlUpdate;
		for (var i=0; i< uiInfoDef.SearchResult.columns.length; i++) {
			specifications.result.values.push(uiInfoDef.SearchResult.values[i]);
			localeFields[module + ".search.result." + uiInfoDef.SearchResult.columns[i]] = getTitleCase(uiInfoDef.SearchResult.columns[i]);
			specifications.result.header.push({
				label: module + ".search.result." + uiInfoDef.SearchResult.columns[i]
			});
		}
	} else {
		errors["search-results"] = "SearchResult not present in x-ui-info. REFERENCE PATH: " + uiInfoDef.referencePath
	}

	setLabels(localeFields, "./output/" + module);

	if(Object.keys(errors).length) {
		return {specifications: specifications, errors: errors};	
	}

	return {specifications: specifications};
}

module.exports = searchTemplate;