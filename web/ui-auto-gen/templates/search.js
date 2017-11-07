var utilities = require('../utilities/utility');
const getType = utilities.getType;
const getTitleCase = utilities.getTitleCase;
const setLabels = utilities.setLabels;
const getQuery = utilities.getQuery;

let localeFields = {};
let errors = {};
let searchTemplate = function (module, numCols, path, config, basePath, uiInfoDef) {
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
		if(!/requestInfo|RequestInfo|sortProperty|tenantId|pageSize|pageNumber|sortResult/.test(parameterConfig[i].name)) {
			localeFields[module + ".create." + parameterConfig[i].name] = getTitleCase(parameterConfig[i].name);
			fields[parameterConfig[i].name] = {
				"name": parameterConfig[i].name,
				"jsonPath": parameterConfig[i].name,
				"label": module + ".create." + parameterConfig[i].name,
				"pattern": parameterConfig[i].pattern,
				"type": parameterConfig[i].enum ? "singleValueList" : parameterConfig[i].format && ["number", "integer", "double", "long", "float"].indexOf(parameterConfig[i].type) == -1 ? getType(parameterConfig[i].format) : getType(parameterConfig[i].type),
				"isRequired": parameterConfig[i].required,
				"isDisabled": parameterConfig[i].readOnly ? true : false,
				"defaultValue": parameterConfig[i].default,
				"maxLength": parameterConfig[i].maxLength,
				"minLength": parameterConfig[i].minLength,
				"patternErrorMsg": module + ".create.field.message." + parameterConfig[i].name
			};

			if(fields[parameterConfig[i].name].type == "text" && parameterConfig[i].maxLength && parameterConfig[i].maxLength > 256)
				fields[parameterConfig[i].name].type = "textarea";
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