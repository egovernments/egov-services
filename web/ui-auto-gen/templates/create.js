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
const addShowHideFields = uiUtilities.addShowHideFields;
const addGroups = uiUtilities.addGroups;
const addTables = uiUtilities.addTables;

let localeFields = {};
let errors = {};

let createTemplate = function(module, numCols, path, config, basePath, uiInfoDef) {
    let specifications = {
        numCols: numCols,
        useTimestamp: true,
        objectName: '',
        groups: [],
        url: path,
        tenantIdRequired: true
    };
    let fields = {};
    let isArr = false;
    let ind = 0;
    for (var i = 0; i < config["post"].parameters.length; i++) {
        if (config["post"].parameters[i].schema) {
            ind = i;
            break;
        }
    }

    let properties = config["post"].parameters[ind].schema.properties;
    let innerDef;
    let reqfields = [];

    for (let key in properties) {
        if (key != "requestInfo" && key != "RequestInfo") {//Added case insensitive check
            //IF ARRAY
            if (properties[key].type == "array") {
                isArr = true;
                innerDef = properties[key].items.properties;
                reqfields = properties[key].items.required || [];
            } else {
                innerDef = properties[key].properties;
                reqfields = properties[key].required || [];
            }
            specifications.objectName = key;
            break;
        }
    }

    let fieldsData = getFieldsFromInnerObject(fields, innerDef, module, isArr ? (specifications.objectName + "[0]") : specifications.objectName, false, reqfields);
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
        fields = fieldsData.fields;                                           
        errors = Object.assign({}, errors, fieldsData.errors);
        localeFields = Object.assign({}, localeFields, fieldsData.localeFields);
    }

    fieldsData = addGroups(specifications, fields, uiInfoDef, module);
    specifications = fieldsData.specifications;
    errors = Object.assign({}, errors, fieldsData.errors);
    localeFields = Object.assign({}, localeFields, fieldsData.localeFields);
    
    if(uiInfoDef.showHideFields && Object.keys(uiInfoDef.showHideFields).length) {
        fieldsData = addShowHideFields(specifications, fields, uiInfoDef);
        specifications = fieldsData.specifications;
        errors = Object.assign({}, errors, fieldsData.errors);
    }

    if(uiInfoDef.tables && Object.keys(uiInfoDef.tables).length) {
        fieldsData = addTables(specifications, fields, uiInfoDef, module);
        specifications = fieldsData.specifications;
        errors = Object.assign({}, errors, fieldsData.errors);   
        localeFields = Object.assign({}, localeFields, fieldsData.localeFields);
    }

    setLabels(localeFields, "./output/" + module);
    if(Object.keys(errors).length) {
        return {specifications: specifications, errors: errors};
    }

    //==================================================================>>
    return {specifications: specifications};
}

module.exports = createTemplate;