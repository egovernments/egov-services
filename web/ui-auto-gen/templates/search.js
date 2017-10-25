var utilities = require('../utilities/utility');
const getType = utilities.getType;
const getTitleCase = utilities.getTitleCase;
const setLabels = utilities.setLabels;
const getQuery = utilities.getQuery;

let localeFields = {};
let errors = {};
let searchTemplate = function (module, numCols, path, config, definition, basePath, uiInfoDef) {
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
			localeFields[module + ".create." + paramKey] = getTitleCase(paramKey);
			fields[paramKey] = {
				"name": paramKey,
				"jsonPath": paramKey,
				"label": module + ".create." + paramKey,
				"pattern": definition[paramKey].pattern,
				"type": definition[paramKey].enum ? "singleValueList" : definition[paramKey].format && ["number", "integer", "double", "long", "float"].indexOf(definition[paramKey].type) == -1 ? getType(definition[paramKey].format) : getType(definition[paramKey].type),
				"isRequired": definition[paramKey].required,
				"isDisabled": definition[paramKey].readOnly ? true : false,
				"defaultValue": definition[paramKey].default,
				"maxLength": definition[paramKey].maxLength,
				"minLength": definition[paramKey].minLength,
				"patternErrorMsg": module + ".create.field.message." + paramKey
			};

			if(fields[paramKey].type == "text" && definition[paramKey].maxLength && definition[paramKey].maxLength > 256)
				fields[paramKey].type = "textarea";
		} else {
			let paramKey = parameterConfig[i].name;
			localeFields[module + ".create." + paramKey] = getTitleCase(paramKey);
			fields[paramKey] = {
				"name": paramKey,
				"jsonPath": paramKey,
				"label": module + ".create." + paramKey,
				"pattern": parameterConfig[i].pattern,
				"type": parameterConfig[i].enum ? "singleValueList" : parameterConfig[i].format && ["number", "integer", "double", "long", "float"].indexOf(parameterConfig[i].type) == -1 ? getType(parameterConfig[i].format) : getType(parameterConfig[i].type),
				"isRequired": parameterConfig[i].required,
				"isDisabled": parameterConfig[i].readOnly ? true : false,
				"defaultValue": parameterConfig[i].default,
				"maxLength": parameterConfig[i].maxLength,
				"minLength": parameterConfig[i].minLength,
				"patternErrorMsg": module + ".create.field.message." + paramKey
			};
		} 
	}

	if (uiInfoDef.externalData && typeof uiInfoDef.externalData == "object" && uiInfoDef.externalData.length) {
        for(var i=0; i<uiInfoDef.externalData.length; i++) {
        	var splitArr = uiInfoDef.externalData[i].fieldName.split(".");
        	splitArr.shift();
        	var paramKey = splitArr.join(".");
            if(fields[paramKey]) {
                if(fields[paramKey].type == "autoCompelete")
                    fields[paramKey].autoCompleteUrl = uiInfoDef.externalData[i].url + getQuery(uiInfoDef.externalData[i].url, uiInfoDef.externalData[i].keyPath, uiInfoDef.externalData[i].valPath);
                else
                    fields[paramKey].url = uiInfoDef.externalData[i].url + getQuery(uiInfoDef.externalData[i].url, uiInfoDef.externalData[i].keyPath, uiInfoDef.externalData[i].valPath);
            } else {
                //errors[paramKey] = "Field exists in x-ui-info externalData section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;
            }
        }
    }

	if (uiInfoDef.dependents && uiInfoDef.dependents.length) {
        for (let i = 0; i < uiInfoDef.dependents.length; i++) {
        	var splitArr = uiInfoDef.dependents[i].onChangeField.split(".");
        	splitArr.shift();
        	var paramKey = splitArr.join(".");
            if (fields[paramKey]) {
                fields[paramKey].depedants = [];
                for (let key in uiInfoDef.dependents[i].affectedFields) {
                    fields[paramKey].depedants.push({
                        "jsonPath": key,
                        "type": uiInfoDef.dependents[i].affectedFields[key].type,
                        "pattern": uiInfoDef.dependents[i].affectedFields[key].pattern
                    })
                }
            }
        }
    }

    if(uiInfoDef.checkboxes && uiInfoDef.checkboxes.length) {
        for(var i=0; i<uiInfoDef.checkboxes.length; i++) {
        	var splitArr = uiInfoDef.checkboxes[i].split(".");
        	splitArr.shift();
        	var paramKey = splitArr.join(".");
            if(fields[paramKey]) {
                fields[paramKey].type = "checkbox";
            } else {
                errors[paramKey] = "Field exists in x-ui-info checkboxes section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;   
            }
        }
    }

    if(uiInfoDef.radios && uiInfoDef.radios.length) {
        for(var i=0; i<uiInfoDef.radios.length; i++) {
        	var splitArr = uiInfoDef.radios[i].jsonPath.split(".");
        	splitArr.shift();
        	var paramKey = splitArr.join(".");
            if(fields[paramKey]) {
            	localeFields[module + ".create." + uiInfoDef.radios[i].trueLabel] = getTitleCase(uiInfoDef.radios[i].trueLabel);
                localeFields[module + ".create." + uiInfoDef.radios[i].falseLabel] = getTitleCase(uiInfoDef.radios[i].falseLabel);
                fields[paramKey].type = "radio";
                fields[paramKey].values = [{
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
			var tmp = {
				label: module + ".search.result." + uiInfoDef.searchResult.columns[i],
			};

			if(fields[uiInfoDef.searchResult.columns[i]] && fields[uiInfoDef.searchResult.columns[i]].type == "datePicker")
				tmp.isDate = true;
			specifications.result.header.push(tmp);
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