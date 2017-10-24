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
			fields[paramKey] = {
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
			};
		}
	}

	if (uiInfoDef.dependents && uiInfoDef.dependents.length) {
        for (let i = 0; i < uiInfoDef.dependents.length; i++) {
            if (fields[uiInfoDef.dependents[i].onChangeField]) {
                fields[uiInfoDef.dependents[i].onChangeField].depedants = [];
                for (let key in uiInfoDef.dependents[i].affectedFields) {
                    fields[uiInfoDef.dependents[i].onChangeField].depedants.push({
                        "jsonPath": key,
                        "type": uiInfoDef.dependents[i].affectedFields[key].type,
                        "pattern": uiInfoDef.dependents[i].affectedFields[key].pattern
                    })
                }
            }
        }
    }

	if(uiInfoDef.multiValueList && uiInfoDef.multiValueList.length) {
        for(var i=0; i<uiInfoDef.multiValueList.length; i++) {
            if(fields[uiInfoDef.multiValueList[i]]) {
                fields[uiInfoDef.multiValueList[i]].type = "multiValueList";
            } else {
                errors[uiInfoDef.multiValueList[i]] = "Field exists in x-ui-info multiValueList section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;   
            }
        }
    }

    if(uiInfoDef.checkboxes && uiInfoDef.checkboxes.length) {
        for(var i=0; i<uiInfoDef.checkboxes.length; i++) {
            if(fields[uiInfoDef.checkboxes[i]]) {
                fields[uiInfoDef.checkboxes[i]].type = "checkbox";
            } else {
                errors[uiInfoDef.checkboxes[i]] = "Field exists in x-ui-info checkboxes section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;   
            }
        }
    }

    if(uiInfoDef.radios && uiInfoDef.radios.length) {
        for(var i=0; i<uiInfoDef.radios.length; i++) {
            if(fields[uiInfoDef.radios[i].jsonPath]) {
                fields[uiInfoDef.radios[i].jsonPath].type = "radio";
                fields[uiInfoDef.radios[i].jsonPath].values = [{
                    label: module + ".create." + uiInfoDef.radios[i].trueLabel,
                    value: true
                }, {
                    label: module + ".create." + uiInfoDef.radios[i].falseLabel,
                    value: false
                }];
            } else {
                errors[uiInfoDef.radios[i].jsonPath] = "Field exists in x-ui-info radios section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;   
            }
        }
    }

	if(uiInfoDef.searchResult && Object.keys(uiInfoDef.searchResult).length) {
		specifications.result.resultPath = uiInfoDef.searchResult.resultObjectName;
		specifications.result.rowClickUrlView = uiInfoDef.searchResult.rowClickUrlView;
		specifications.result.rowClickUrlUpdate = uiInfoDef.searchResult.rowClickUrlUpdate;
		for (var i=0; i< uiInfoDef.searchResult.columns.length; i++) {
			specifications.result.values.push(uiInfoDef.searchResult.values[i]);
			localeFields[module + ".search.result." + uiInfoDef.searchResult.columns[i]] = getTitleCase(uiInfoDef.searchResult.columns[i]);
			specifications.result.header.push({
				label: module + ".search.result." + uiInfoDef.searchResult.columns[i]
			});
		}
	} else {
		errors["search-results"] = "SearchResult not present in x-ui-info. REFERENCE PATH: " + uiInfoDef.referencePath
	}

	for(var key in fields) {
		specifications.groups[0].fields.push(fields[key]);
	};

	setLabels(localeFields, "./output/" + module);

	if(Object.keys(errors).length) {
		return {specifications: specifications, errors: errors};	
	}

	return {specifications: specifications};
}

module.exports = searchTemplate;