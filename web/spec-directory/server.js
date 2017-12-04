var express = require('express')
const path = require('path');
const http = require('http');
const bodyParser = require('body-parser');
const config = require('./config');
const fs = require('fs');
const SwaggerParser = require('swagger-parser');
var $RefParser = require('json-schema-ref-parser');
const app = express();
var request = require('request');

app.use(express.static(__dirname + '/'))
app.use(bodyParser.json());


function getType(type) {
    switch (type) {
        case 'integer':
            return 'number';
        case 'float':
            return 'number';
        case 'double':
            return 'number';
        case 'long':
            return 'number';
        case 'number':
            return 'number';
        case 'string':
            return 'text';
        case 'boolean':
            return 'radio';
        case 'date':
            return 'datePicker';
        case 'email':
            return 'email';
        case 'pan':
            return 'pan';
        case 'pinCode':
            return 'pinCode';
        case 'mobileNumber':
            return 'mobileNumber';
        case 'autoComplete':
            return 'autoCompelete';
        case 'aadhar':
            return 'aadhar';
        case 'checkbox':
            return 'checkbox';
        case 'singleValueList':
            return 'singleValueList';
        case 'multiValueList':
            return 'multiValueList';
        default:
            return '';
    }
}

var getFieldsFromInnerObject = function(properties, module, jPath, isArray, required) {
    var fields = [];
    var header = [];
    for (let key in properties) {
        if (["id", "tenantId", "auditDetails", "assigner"].indexOf(key) > -1) continue;
        if(properties[key].properties) {
            if(jPath.search("." + key) < 2)
                getFieldsFromInnerObject(properties[key].properties, module, (isArray ? (jPath + "[0]") : jPath) + "." + key, false, (properties[key].properties.required || []));
        } else if(properties[key].items && properties[key].items.properties) {
            if(jPath.search("." + key) < 2)
                getFieldsFromInnerObject(properties[key].items.properties, module, (isArray ? (jPath + "[0]") : jPath) + "." + key, true, (properties[key].items.properties.required || []));
        } else {
            fields.push({
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
            });
            

        }

        for(var i = 0; i < fields.length; i++){
        	header[i] = {};
        	header[i].label = fields[i].jsonPath;
        }
    }
    return {
        fields,
        header
    };
}

var mainObj = {};
var completed_requests = 0;
var finalSpecs = {};

for(let i = 0; i < config.data.length; i++){

	SwaggerParser.dereference(config.data[i])
        .then(function(yamlJSON) {
        	// console.log(yamlJSON)
        	let basePath = [];
        	basePath = yamlJSON.basePath.split("-")[0].split("");
        	let index = basePath.indexOf("/");
        	if(index > -1){
        		basePath.splice(index, 1);
        	}

        	mainObj[basePath.join("")] = yamlJSON.definitions;
        	
        	completed_requests++;

        	if (completed_requests == config.data.length) {
	            // All downloads done, process responses array
	            // console.log(mainObj);

	            for(key in mainObj){
	            	finalSpecs[key.toLowerCase()] = {};
	            	for(property in mainObj[key.toLowerCase()]){
	            		// console.log(property);
	            		if(!finalSpecs[key].masters) finalSpecs[key].masters = {};

	            		if(!finalSpecs[key.toLowerCase()].masters[property.toLowerCase()]) finalSpecs[key.toLowerCase()].masters[property.toLowerCase()] = {};
	            		if(!finalSpecs[key.toLowerCase()].masters[property.toLowerCase()]) finalSpecs[key.toLowerCase()].masters[property.toLowerCase()] = {};
	            		

	            		finalSpecs[key.toLowerCase()].masters[property.toLowerCase()].name = "";
	            		finalSpecs[key.toLowerCase()].masters[property.toLowerCase()].label = "";
	            		finalSpecs[key.toLowerCase()].masters[property.toLowerCase()].type = "multiFieldAddToTable";
	            		finalSpecs[key.toLowerCase()].masters[property.toLowerCase()].jsonPath = "";
	            		finalSpecs[key.toLowerCase()].masters[property.toLowerCase()].header = (getFieldsFromInnerObject(mainObj[key][property].properties, key,'MdmsMetadata.masterData', true, mainObj[key][property].required || [])).header;
	            		finalSpecs[key.toLowerCase()].masters[property.toLowerCase()].values = (getFieldsFromInnerObject(mainObj[key][property].properties, key,'MdmsMetadata.masterData', true, mainObj[key][property].required || [])).fields;
	            		// console.log("Break-------------------------------------------");
	            		// console.log(finalSpecs.swm.masters.CollectionPoint);
	            	}
	            	
	            }

	            console.log(finalSpecs);
	        
	        }
		})
		.catch(function(err) {
            console.log(err);
        });
}


app.post('/:module/:master', function(req, res, next) {
	for (var key in req.params){ 
		req.params[key] = req.params[key].toLowerCase();
	}
	console.log(req.params);
	console.log(req.params.master);
	var master = req.params.master;
	var module = req.params.module;
	if(finalSpecs[module] && finalSpecs[module].masters[master]){
		res.status(200).json(finalSpecs[module].masters[master]);
	}
	else{
		res.status(400).json({
            message: "Invalid parameters"
        })
	}
	

	next();
});

app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'index.html'));
});



const port = process.env.PORT || '4022';
app.listen(port, function() {
    console.log('Parser listening on port: ' + port);
})