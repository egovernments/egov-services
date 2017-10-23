const getType = require('../utilities/utility').getType;
const getTitleCase = require('../utilities/utility').getTitleCase;
const setLabels = require('../utilities/utility').setLabels;

let localeFields = {};
let errors = {};
let getFieldsFromInnerObject = function(reference, fields, definition, module, jPath, isArray) {
    if (definition[reference])
        for (let key in definition[reference].properties) {
            if (["id", "tenantId", "auditDetails", "assigner"].indexOf(key) > -1) continue;

            if (definition[reference].properties[key].type == "array") {
                let refSplitArr = definition[reference].properties[key].items.$ref.split("/");
                getFieldsFromInnerObject(refSplitArr[refSplitArr.length - 1], fields, definition, module, (isArray ? (jPath + "[0]") : jPath) + "." + key, true);
            } else if (definition[reference].properties[key].$ref) {
                let refSplitArr = definition[reference].properties[key].$ref.split("/");
                getFieldsFromInnerObject(refSplitArr[refSplitArr.length - 1], fields, definition, module, (isArray ? (jPath + "[0]") : jPath) + "." + key);
            } else {
                localeFields[module + ".create." + key] = getTitleCase(key);
                fields[(isArray ? (jPath + "[0]") : jPath) + "." + key] = {
                    "name": key,
                    "jsonPath": (isArray ? (jPath + "[0]") : jPath) + "." + key,
                    "label": module + ".create." + key,
                    "pattern": definition[reference].properties[key].pattern,
                    "type": definition[reference].properties[key].enum ? "singleValueList" : definition[reference].properties[key].format == "date" ? 'date' :  getType(definition[reference].properties[key].type),
                    "isRequired": (definition[reference].properties[key].required || (definition[reference].required && definition[reference].required.constructor == Array && definition[reference].required.indexOf(key) > -1) ? true : false),
                    "isDisabled": definition[reference].properties[key].readOnly ? true : false,
                    "defaultValue": definition[reference].properties[key].default,
                    "maxLength": definition[reference].properties[key].maxLength,
                    "minLength": definition[reference].properties[key].minLength,
                    "patternErrorMsg": definition[reference].properties[key].pattern ? (module + ".create.field.message." + key) : ""
                };
            }
        }
}

let viewTemplate = function(module, numCols, path, config, definition, uiInfoDef) {
    let specifications = {
        numCols: numCols,
        useTimestamp: true,
        objectName: '',
        groups: []
    };
    let fields = {};
    let ind = 0;
    for(var i=0; i<config["post"].parameters.length; i++) {
        if(config["post"].parameters[i].schema) {
            ind = i;
            break;
        }
    }
    let splitArr = config["post"].parameters[ind].schema.$ref.split("/");
    let properties = definition[splitArr[splitArr.length - 1]].properties;
    for (let key in properties) {
        if (key != "requestInfo") {
            //IF ARRAY
            if(properties[key].type == "array") {
                let propertiesArr = properties[key].items.$ref.split("/");
                specifications.objectName = key;
                reference = propertiesArr[propertiesArr.length - 1];
                isArr = true;
            } else {
                let propertiesArr = properties[key].$ref.split("/");
                specifications.objectName = key;
                reference = propertiesArr[propertiesArr.length - 1];
            }
            break;
        }
    }

    getFieldsFromInnerObject(reference, fields, definition, module, isArr ? (specifications.objectName + "[0]") : specifications.objectName);

    //=======================CUSTOM FILE LOGIC==========================>>
    if(uiInfoDef.ExternalData && typeof uiInfoDef.ExternalData == "object" && Object.keys(uiInfoDef.ExternalData).length) {
    	for(var key in uiInfoDef.ExternalData) {
    		if (fields[key]) {
                fields[key].url = uiInfoDef.ExternalData[key];
                fields[key].type = "singleValueList";
            } else
                errors[key] = "Field exists in x-ui-info ExternalData section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;
    	}
    }

    if(uiInfoDef.dependents && uiInfoDef.dependents.length) {
    	for(let i=0; i<uiInfoDef.dependents.length; i++) {
    		if(fields[uiInfoDef.dependents[i].onChangeField]) {
    			fields[uiInfoDef.dependents[i].onChangeField].depedants = [];
    			for(let key in uiInfoDef.dependents[i].affectedFields) {
    				fields[uiInfoDef.dependents[i].onChangeField].depedants.push({
    					"jsonPath": key,
    					"type": uiInfoDef.dependents[i].affectedFields[key].type,
    					"pattern": uiInfoDef.dependents[i].affectedFields[key].pattern
    				})
    			}
    		}
    	}
    }
    
    for(var key in uiInfoDef.groups) {
        localeFields[module + ".create.group.title." + key] = getTitleCase(key);
    	let group = {
    		name: key,
    		label: module + ".create.group.title." + key,
    		fields: []
    	};
    	for(var i=0; i<uiInfoDef.groups[key].fields.length; i++) {
    		if (fields[uiInfoDef.groups[key].fields[i]])
                group.fields.push(fields[uiInfoDef.groups[key].fields[i]]);
            else
                errors[uiInfoDef.groups[key].fields[i]] = "Field exists in x-ui-info groups section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;
    	}
    	specifications.groups.push(group);
    }

    setLabels(localeFields, "./output/" + module);
    if(Object.keys(errors).length) {

        /*console.log("\x1b[31mERROR OCCURED! CANNOT PROCESS YAML.");
        console.log(errors);
        console.log("\x1b[37m");
        process.exit();*/
        return {specifications: specifications, errors: errors};
    }
    //==================================================================>>
    return {specifications: specifications};
}

module.exports = viewTemplate;