var utilities = require('./utility');
const getTitleCase = utilities.getTitleCase;
const getType = utilities.getType;
const getQuery = utilities.getQuery;

let getFieldsFromInnerObject = function(fields, properties, module, jPath, isArray, required, localeFields) {
    let errors = {};
    localeFields = localeFields || {};

    for (let key in properties) {
        if (["id", "tenantId", "auditDetails", "assigner"].indexOf(key) > -1) continue;
        if(properties[key].properties) {
            if(jPath.search("." + key) < 2)
                getFieldsFromInnerObject(fields, properties[key].properties, module, (isArray ? (jPath + "[0]") : jPath) + "." + key, false, (properties[key].properties.required || []), localeFields);
        } else if(properties[key].items && properties[key].items.properties) {
            if(jPath.search("." + key) < 2)
                getFieldsFromInnerObject(fields, properties[key].items.properties, module, (isArray ? (jPath + "[0]") : jPath) + "." + key, true, (properties[key].items.properties.required || []), localeFields);
        } else {
            fields[(isArray ? (jPath + "[0]") : jPath) + "." + key] = {
                "name": key,
                "jsonPath": (isArray ? (jPath + "[0]") : jPath) + "." + key,
                "label": module + ".create." + key,
                "pattern": properties[key].pattern || "",
                "type": properties[key].enum ? "singleValueList" : properties[key].format && ["number", "integer", "double", "long", "float"].indexOf(properties[key].type) == -1 ? getType(properties[key].format) : getType(properties[key].type),
                "isRequired": (properties[key].required || (required && required.constructor == Array && required.indexOf(key) > -1) ? true : false),
                "isDisabled": properties[key].readOnly ? true : false,
                "defaultValue": properties[key].default || "",
                "maxLength": properties[key].maxLength,
                "minLength": properties[key].minLength,
                "patternErrorMsg": properties[key].pattern ? (module + ".create.field.message." + key) : ""
            };

            let keyNameSplit = fields[(isArray ? (jPath + "[0]") : jPath) + "." + key].jsonPath.split(".");
            let len = keyNameSplit.length;
            localeFields[module + ".create." + (keyNameSplit.length > 2 ? (keyNameSplit[len-2] + "." + keyNameSplit[len-1]) : key)] = getTitleCase(key);
            fields[(isArray ? (jPath + "[0]") : jPath) + "." + key].label = module + ".create." + (keyNameSplit.length > 2 ? (keyNameSplit[len-2] + "." + keyNameSplit[len-1]) : key);
            if (fields[(isArray ? (jPath + "[0]") : jPath) + "." + key].type == "text" && properties[key].maxLength && properties[key].maxLength > 256)
                fields[(isArray ? (jPath + "[0]") : jPath) + "." + key].type = "textarea";

        }
    }
    return {
        fields,
        errors,
        localeFields
    };
}

let addUrls = function(fields, uiInfoDef) {
    let errors = {};
    for (var i = 0; i < uiInfoDef.externalData.length; i++) {
        if (fields[uiInfoDef.externalData[i].fieldName]) {
            if (fields[uiInfoDef.externalData[i].fieldName].type == "autoCompelete")
                fields[uiInfoDef.externalData[i].fieldName].autoCompleteUrl = uiInfoDef.externalData[i].url + getQuery(uiInfoDef.externalData[i].url, uiInfoDef.externalData[i].keyPath, uiInfoDef.externalData[i].valPath);
            else
                fields[uiInfoDef.externalData[i].fieldName].url = uiInfoDef.externalData[i].url + getQuery(uiInfoDef.externalData[i].url, uiInfoDef.externalData[i].keyPath, uiInfoDef.externalData[i].valPath);
        } else {
            errors[uiInfoDef.externalData[i].fieldName] = "Field exists in x-ui-info externalData section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;
        }
    }

    return {
        fields,
        errors
    }
}

let addDependents = function(fields, uiInfoDef) {
    let errors = {};
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

    return {
        fields,
        errors
    };
}

let addAutoFills = function(fields, uiInfoDef) {
    let errors = {};
    for (var i = 0; i < uiInfoDef.autoFills.length; i++) {
        if (fields[uiInfoDef.autoFills[i].onChangeField]) {
            fields[uiInfoDef.autoFills[i].onChangeField].type = "textSearch";
            fields[uiInfoDef.autoFills[i].onChangeField].autoCompleteDependancy = {
                autoCompleteUrl: uiInfoDef.autoFills[i].url,
                autoFillFields: {}
            };

            for (var j = 0; j < uiInfoDef.autoFills[i].affectedFields.length; j++) {
                fields[uiInfoDef.autoFills[i].onChangeField].autoCompleteDependancy.autoFillFields[uiInfoDef.autoFills[i].affectedFields[j]] = uiInfoDef.autoFills[i].affectJSONPath[j];
            }
        } else {
            errors[uiInfoDef.autoFills[i].onChangeField] = "Field exists in x-ui-info AutoFills section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;
        }
    }

    return {
        fields,
        errors
    };
}

let addRadios = function(fields, uiInfoDef, module) {
    let errors = {};
    let localeFields = {};
    for (var i = 0; i < uiInfoDef.radios.length; i++) {
        if (fields[uiInfoDef.radios[i].jsonPath]) {
            fields[uiInfoDef.radios[i].jsonPath].type = "radio";
            localeFields[module + ".create." + uiInfoDef.radios[i].trueLabel] = getTitleCase(uiInfoDef.radios[i].trueLabel);
            localeFields[module + ".create." + uiInfoDef.radios[i].falseLabel] = getTitleCase(uiInfoDef.radios[i].falseLabel);
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

    return {
        fields,
        errors,
        localeFields
    };
}

let addGroups = function(specifications, fields, uiInfoDef, module) {
    let errors = {};
    let localeFields = {};
    for (var key in uiInfoDef.groups) {
        localeFields[module + ".create.group.title." + key] = getTitleCase(key);
        let group = {
            name: key,
            label: module + ".create.group.title." + key,
            fields: []
        };
        for (var i = 0; i < uiInfoDef.groups[key].fields.length; i++) {
            if (fields[uiInfoDef.groups[key].fields[i]])
                group.fields.push(fields[uiInfoDef.groups[key].fields[i]]);
            else
                errors[uiInfoDef.groups[key].fields[i]] = "Field exists in x-ui-info groups section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;
        }
        specifications.groups.push(group);
    }

    return {
        specifications,
        errors,
        localeFields
    }
}

let addShowHideFields = function(specifications, fields, uiInfoDef) {
    let errors = {};
    for(let k=0; k<specifications.groups.length; k++) {
        for(let m=0; m<specifications.groups[k].fields.length; m++) {
            let key = specifications.groups[k].fields[m].jsonPath;
            if(uiInfoDef.showHideFields[key]) {
                specifications.groups[k].fields[m].showHideFields = [];
                for(let i=0; i<uiInfoDef.showHideFields[key].length; i++) {
                    let tmp = {};
                    tmp.ifValue = uiInfoDef.showHideFields[key][i].ifValue;
                    tmp.hide = [];
                    tmp.show = [];
                    if(uiInfoDef.showHideFields[key][i].showFields) {
                        for(let j=0; j<uiInfoDef.showHideFields[key][i].showFields.length; j++) {
                            let _f = uiInfoDef.showHideFields[key][i].showFields[j];
                            if(fields[_f]) {
                                tmp.show.push({
                                    "name": fields[_f].name,
                                    "isField": true,
                                    "isGroup": false
                                });
                                for(let a=0; a<specifications.groups.length; a++) {
                                    let flag = 0;
                                    for(let b=0; b<specifications.groups[a].fields.length; b++) {
                                        if(_f == specifications.groups[a].fields[b].jsonPath) {
                                            specifications.groups[a].fields[b].hide = true;
                                            flag = 1;
                                            break;
                                        }
                                    }
                                    if(flag == 1) break;
                                }
                            }
                        }
                    }

                    if(uiInfoDef.showHideFields[key][i].hideFields) {
                        for(let j=0; j<uiInfoDef.showHideFields[key][i].hideFields.length; j++) {
                            let _f = uiInfoDef.showHideFields[key][i].hideFields[j];
                            if(fields[_f]) 
                                tmp.hide.push({
                                    "name": fields[_f].name,
                                    "isField": true,
                                    "isGroup": false
                                });
                        }
                    }

                    if(uiInfoDef.showHideFields[key][i].showGroups) {
                        for(let j=0; j<uiInfoDef.showHideFields[key][i].showGroups.length; j++) {
                            tmp.show.push({
                                "name": uiInfoDef.showHideFields[key][i].showGroups[j],
                                "isField": false,
                                "isGroup": true
                            });
                            for(let n=0; n<specifications.groups.length; n++) {
                                if(specifications.groups[n].name == uiInfoDef.showHideFields[key][i].showGroups[j]) {
                                    specifications.groups[n].hide = true;
                                    break;
                                }
                            }
                        }
                    }

                    if(uiInfoDef.showHideFields[key][i].hideGroups) {
                        for(let j=0; j<uiInfoDef.showHideFields[key][i].hideGroups.length; j++) {
                            tmp.hide.push({
                                "name": uiInfoDef.showHideFields[key][i].hideGroups[j],
                                "isField": false,
                                "isGroup": true
                            });
                        }
                    }
                    specifications.groups[k].fields[m].showHideFields.push(tmp);
                }
            }
        }
    }

    return {
        errors,
        specifications
    }
}

let addTables = function(specifications, fields, uiInfoDef, module) {
    let errors = {};
    let localeFields = {};
    let inGroups = {};
    
    for(let key in uiInfoDef.tables) {
        let _f = {
            "type": "tableList",
            "jsonPath": key,
            "tableList": {
                "header": [],
                "values": []
            }
        };
        for(let i=0; i<uiInfoDef.tables[key].columns.length; i++) {
            _f.tableList.header.push({
                "label": module + ".create." + uiInfoDef.tables[key].columns[i]
            });
            localeFields[module + ".create." + uiInfoDef.tables[key].columns[i]] = getTitleCase(uiInfoDef.tables[key].columns[i]);
        }
        for(let i=0; i<uiInfoDef.tables[key].values.length; i++) {
            if(fields[uiInfoDef.tables[key].values[i]]) {
                _f.tableList.values.push(fields[uiInfoDef.tables[key].values[i]]);
            } else {
                errors[uiInfoDef.tables[key].values[i]] = "Field exists in x-ui-info tables section but not present in API specifications. REFERENCE PATH: " + uiInfoDef.referencePath;
            }
        }
        
        if(!inGroups[uiInfoDef.tables[key].group]) inGroups[uiInfoDef.tables[key].group] = [];
        inGroups[uiInfoDef.tables[key].group].push(_f);
    }
    
    for(let i=0; i<specifications.groups.length; i++) {
        if(inGroups[specifications.groups[i].name]) {
            specifications.groups[i].fields = specifications.groups[i].fields.concat(inGroups[specifications.groups[i].name]);
        }
    }

    return {
        errors,
        localeFields,
        specifications
    }
}

exports.getFieldsFromInnerObject = getFieldsFromInnerObject;
exports.addUrls = addUrls;
exports.addDependents = addDependents;
exports.addAutoFills = addAutoFills;
exports.addRadios = addRadios;
exports.addShowHideFields = addShowHideFields;
exports.addTables = addTables;
exports.addGroups = addGroups;