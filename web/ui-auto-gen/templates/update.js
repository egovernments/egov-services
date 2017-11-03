var utilities = require('../utilities/utility');
var uiUtilities = require('../utilities/uiUtility');
const getType = utilities.getType;
const getTitleCase = utilities.getTitleCase;
const setLabels = utilities.setLabels;
const getQuery = utilities.getQuery;
const getFieldsFromInnerObject = uiUtilities.getFieldsFromInnerObject;
const addUrls = uiUtilities.addUrls;
const addDependents = uiUtilities.addDependents;
const addAutoFills = uiUtilities.addAutoFills;
const addRadios = uiUtilities.addRadios;
const addGroups = uiUtilities.addGroups;

let localeFields = {};
let errors = {};

let updateTemplate = function(module, numCols, path, config, definition, basePath, uiInfoDef) {
    let specifications = {
        numCols: numCols,
        useTimestamp: true,
        objectName: '',
        groups: [],
        url: path,
        tenantIdRequired: true,
        searchUrl: basePath + uiInfoDef.searchUrl
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
    let reference;
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

    let fieldsData = getFieldsFromInnerObject(reference, fields, definition, module, isArr ? (specifications.objectName + "[0]") : specifications.objectName);
    fields = fieldsData.fields;
    errors = Object.assign({}, errors, fieldsData.errors);
    localeFields = Object.assign({}, localeFields, fieldsData.localeFields);

    //=======================CUSTOM FILE LOGIC==========================>>
    if (uiInfoDef.externalData && typeof uiInfoDef.externalData == "object" && uiInfoDef.externalData.length) {
        fieldsData = addUrls(fields, uiInfoDef);
        fields = fieldsData.fields;
        errors = Object.assign({}, errors, fieldsData.errors);
    }

    if (uiInfoDef.dependents && uiInfoDef.dependents.length) {
        fieldsData = addDependents(fields, uiInfoDef);
        fields = fieldsData.fields;
        errors = Object.assign({}, errors, fieldsData.errors);
    }

    if(uiInfoDef.autoFills && uiInfoDef.autoFills.length) {
        fieldsData = addAutoFills(fields, uiInfoDef);
        fields = fieldsData.fields;
        errors = Object.assign({}, errors, fieldsData.errors);
    }

    if(uiInfoDef.radios && uiInfoDef.radios.length) {
        fieldsData = addRadios(fields, uiInfoDef, module);
        errors = Object.assign({}, errors, fieldsData.errors);
        localeFields = Object.assign({}, localeFields, fieldsData.localeFields);
    }

    fieldsData = addGroups(specifications, fields, uiInfoDef, module);
    specifications = fieldsData.specifications;
    errors = Object.assign({}, errors, fieldsData.errors);
    localeFields = Object.assign({}, localeFields, fieldsData.localeFields);

    setLabels(localeFields, "./output/" + module);
    if(Object.keys(errors).length) {
        return {specifications: specifications, errors: errors};
    }
    
    //==================================================================>>
    return {specifications: specifications};
}

module.exports = updateTemplate;