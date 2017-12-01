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




app.use(express.static(__dirname + '/public'))
app.use(bodyParser.json());

 // SwaggerParser.dereference('https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yaml')
 //        .then(function(yamlJSON) {
 //        	console.log(yamlJSON);
 //        })
 //        .catch(function(err) {
 //            console.log(err);
 //        });

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



	var urls = [];
	var masters = [];
	var moduleName = [];
	var urlData = [];
	var urlDataFull = [];
	var completed_requests = 0;
	var definitions = [];
	var filteredDefinitions = [];
	var filteredSpecs = [];
	var finalSpecObj = {};
	var finalModuleData = {};
	var finalSpecs = {};
	
	for(module in config.data){
		// console.log(module);
		urls.push(config.data[module].yamlURL);
		masters.push(config.data[module].masters);
		moduleName.push(module);
	}

	// console.log(masters);

	for (i in urls) {

		SwaggerParser.dereference(urls[i])
        .then(function(yamlJSON) {
        	// console.log(yamlJSON.definitions)
        	urlData.push(yamlJSON.definitions);
        	completed_requests++;

        	if (completed_requests == urls.length) {
	            // All download done, process responses array
	            for(var j = 0; j < urlData.length; j++){
	            	for(definition in urlData[j]){
		            	definitions.push(definition);
		            }
	            }

	            // console.log(definitions);


	            


	            for(var k = 0; k < urlData.length; k++){
	            	for(var l = 0; l < masters[k].length; l++){
	            		for(var m = 0; m < definitions.length; m++){
	            			if(masters[k][l] === definitions[m]){

	            				filteredDefinitions.push(definitions[m]);
	            				
	            			}
	            		}

	            	}
	            }

	            // console.log(filteredDefinitions);
	            for(var j = 0; j < urlData.length; j++){
	            	for(key in urlData[j]){
		            	console.log(key);
		            	for(var k = 0; k < filteredDefinitions.length; k++){
		            		if(key == filteredDefinitions[k]){
		            			filteredSpecs.push(urlData[j][key]);
		            		}
		            	}
		            }
		        }

	            // console.log(filteredSpecs.length);
	            // console.log(filteredSpecs);

	            for(var i = 0; i < filteredDefinitions.length; i++){
	            	finalSpecObj[filteredDefinitions[i]] = filteredSpecs[i];
	            }

	            // console.log(finalSpecObj);


	            // console.log(filteredDefinitions);
	            for(var i = 0; i < urlData.length; i++){
	            	finalModuleData[moduleName[i]] = {};
	            	// finalModuleData[i].moduleName = moduleName[i];
	            	for(var j = 0; j < masters[i].length; j++){
	            		for(key in finalSpecObj){
	            			if(key === masters[i][j]){
	            				if(!finalModuleData[moduleName[i]].masters) finalModuleData[moduleName[i]].masters = {};
	            				finalModuleData[moduleName[i]].masters[key] = finalSpecObj[key];
	            			}
		            	}
	            	}
		            	
	            }

	            console.log(finalModuleData);
	            // console.log(getFieldsFromInnerObject(finalModuleData.swm.masters.Vendor.properties, "swm",'MdmsMetadata.masterData', true, finalModuleData.swm.masters.Vendor.required || []))
	            for(key in finalModuleData){
	            	// console.log(key);
	            	finalSpecs[key] = {};
	            	for(property in finalModuleData[key].masters){
	            		// console.log(property);
	            		if(!finalSpecs[key].masters) finalSpecs[key].masters = {};

	            		if(!finalSpecs[key].masters[property]) finalSpecs[key].masters[property] = {};
	            		if(!finalSpecs[key].masters[property]) finalSpecs[key].masters[property] = {};
	            		

	            		finalSpecs[key].masters[property].name = "";
	            		finalSpecs[key].masters[property].label = "";
	            		finalSpecs[key].masters[property].type = "multiFieldAddToTable";
	            		finalSpecs[key].masters[property].jsonPath = "";
	            		finalSpecs[key].masters[property].header = (getFieldsFromInnerObject(finalModuleData[key].masters[property].properties, key,'MdmsMetadata.masterData', true, finalModuleData[key].masters[property].required || [])).header;
	            		finalSpecs[key].masters[property].values = (getFieldsFromInnerObject(finalModuleData[key].masters[property].properties, key,'MdmsMetadata.masterData', true, finalModuleData[key].masters[property].required || [])).fields;
	            		// console.log("Break-------------------------------------------");
	            		// console.log(finalSpecs.swm.masters.CollectionPoint);
	            	}
	            	
	            }

	            
	        }        	
        })
        .catch(function(err) {
            console.log(err);
        });

	}


app.post('/:module/:master', function(req, res, next) {

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



app.on('listening',function(){
    console.log('ok, server is running');
});


// request.get('https://raw.githubusercontent.com/egovernments/egov-services/master/docs/swm/contract/v1-0-0.yaml', function (error, response, body) {
//     if (!error && response.statusCode == 200) {
//         var csv = body;
//         // console.log(csv);
//         // Continue with your processing here.
//     }
// });


const port = process.env.PORT || '4022';
app.listen(port, function() {
    console.log('Parser listening on port: ' + port);
})