var express = require("express");
const path = require("path");
const http = require("http");
const bodyParser = require("body-parser");
const config = require("./config");
const fs = require("fs");
const SwaggerParser = require("swagger-parser");
var $RefParser = require("json-schema-ref-parser");
const app = express();
var request = require("request");

app.use(express.static(__dirname + "/"));
app.use(bodyParser.json());

// app.use(function(req, res, next) {
//   res.header('Access-Control-Allow-Origin', '*');
//   res.header('Access-Control-Allow-Methods', 'GET,HEAD,OPTIONS,POST,PUT');
//   res.header(
//     'Access-Control-Allow-Headers',
//     'Origin, X-Requested-With, Content-Type, Accept, Authorization, Access-Control-Allow-Origin'
//   );
//   next();
// });

function getType(type) {
  switch (type) {
    case "integer":
      return "number";
    case "float":
      return "number";
    case "double":
      return "number";
    case "long":
      return "number";
    case "number":
      return "number";
    case "string":
      return "text";
    case "boolean":
      return "checkbox";
    case "date":
      return "datePicker";
    case "email":
      return "email";
    case "pan":
      return "pan";
    case "pinCode":
      return "pinCode";
    case "mobileNumber":
      return "mobileNumber";
    case "autoComplete":
      return "autoCompelete";
    case "aadhar":
      return "aadhar";
    case "checkbox":
      return "checkbox";
    case "singleValueList":
      return "singleValueList";
    case "multiValueList":
      return "multiValueList";
    default:
      return "";
  }
}

getPropertyStatus = (module, master, property) => {
  var scopeObj = uniqueKeyObj;

  if (scopeObj[module.toLowerCase()][master.toLowerCase()]) {
    if (
      scopeObj[module.toLowerCase()][master.toLowerCase()].indexOf(property) >
      -1
    ) {
      return true;
    } else {
      return false;
    }
  } else {
    // console.log(module, master);
    return false;
  }
};

getHeaderStatus = (module, master, property) => {
  var scopeObj = specificHeaderObj;
  if (
    specificHeaderObj[module.toLowerCase()].isSpecificHeader &&
    scopeObj[module.toLowerCase()][master.toLowerCase()]
  ) {
    if (
      scopeObj[module.toLowerCase()][master.toLowerCase()].indexOf(property) >
      -1
    ) {
      return true;
    } else {
      return false;
    }
  } else {
    return true;
  }
};

var getFieldsFromInnerObject = function(
  fields,
  header,
  properties,
  module,
  master,
  jPath,
  isArray,
  required
) {
  // console.log("Iner object called with - " + jPath);
  for (let key in properties) {
    var isUnique = getPropertyStatus(module, master, "$." + key);
    var isSpecificHeader = getPropertyStatus(module, master, "$." + key);
    // console.log(module, master, key, getPropertyStatus(module, master, key));
    //Adding tenantId and id as per discussion on Friday (08-12-2017)
    if ([/*"id", "tenantId",*/ "auditDetails", "assigner"].indexOf(key) > -1)
      continue;
    if (properties[key].properties) {
      //its an inner object - should be another MDMS object - make UI paint a singleValueList with appropriate URL
      //Alert !!!! Hardcoding....

      //Adding custom specs for Address Block as per discussion on Friday (08-12-2017)
      if (key == "address" || key == "wasteType") {
        // console.log(properties[key].properties);
        getFieldsFromInnerObject(
          fields,
          header,
          properties[key].properties,
          module,
          master,
          (isArray ? jPath + "[0]" : jPath) + "." + key,
          false,
          properties[key].properties.required || []
        );
      } else {
        fields.push({
          name: key,
          jsonPath: (isArray ? jPath + "[0]" : jPath) + "." + key,
          label: "MdmsMetadata.masterData." + module + "." + master + "." + key, //Changes (08-12-2017)
          type: "singleValueList",
          isRequired:
            properties[key].required ||
            (required &&
              required.constructor == Array &&
              required.indexOf(key) > -1)
              ? true
              : false,
          defaultValue: properties[key].default || "",
          url:
            "/egov-mdms-service/v1/_get?&moduleName=" +
            module +
            "&masterName=" +
            master +
            "|$.MdmsRes." +
            module +
            "." +
            key +
            ".*.id|$.MdmsRes." +
            module +
            "." +
            key +
            ".*.name",
          isStateLevel: false,
          apiKey: jPath + "." + key,
          isUnique: isUnique
        });
        if (getHeaderStatus(module, master, key)) {
          header.push({
            label:
              "MdmsMetadata.masterData." + module + "." + master + "." + key
          });
        }
      }
    } else if (properties[key].items && properties[key].items.properties) {
      if (jPath == "WasteSubType") console.log(jPath + " is an array");
      if (jPath.search("." + key) < 2)
        //What is this for??
        getFieldsFromInnerObject(
          fields,
          header,
          properties[key].items.properties,
          module,
          master,
          (isArray ? jPath + "[0]" : jPath) + "." + key,
          true,
          properties[key].items.properties.required || []
        );
    } else {
      fields.push({
        name: key,
        jsonPath: (isArray ? jPath + "[0]" : jPath) + "." + key,
        label: "MdmsMetadata.masterData." + module + "." + master + "." + key, //Changes (08-12-2017)
        pattern: properties[key].pattern || "",
        type: properties[key].enum
          ? "singleValueList"
          : properties[key].format &&
            ["number", "integer", "double", "long", "float"].indexOf(
              properties[key].type
            ) == -1
            ? getType(properties[key].format)
            : getType(properties[key].type),
        isRequired:
          properties[key].required ||
          (required &&
            required.constructor == Array &&
            required.indexOf(key) > -1)
            ? true
            : false,
        isDisabled: properties[key].readOnly ? true : false,
        defaultValue: properties[key].default || "",
        maxLength: properties[key].maxLength,
        minLength: properties[key].minLength,
        patternErrorMsg: properties[key].pattern
          ? module + ".create.field.message." + key
          : "",
        isUnique: isUnique
      });
      if (getHeaderStatus(module, master, key)) {
        header.push({
          label: "MdmsMetadata.masterData." + module + "." + master + "." + key
        });
      }
    }
  }
  return {
    fields,
    header
  };
};

var configData = config.data;
var mainObj = {};
var completed_requests = 0;
var finalSpecs = {};
var finalSpecsRaw = {};
var urls = [];
var modules = [];
var uniqueKeyObj = {};
var specificHeaderObj = {};

for (module in configData) {
  urls.push(configData[module].url);
  modules.push(module);

  if (!specificHeaderObj[module.toLowerCase()])
    specificHeaderObj[module.toLowerCase()] = {};
  specificHeaderObj[module.toLowerCase()].isSpecificHeader =
    configData[module].isSpecificHeader;
  if (!uniqueKeyObj[module.toLowerCase()])
    uniqueKeyObj[module.toLowerCase()] = {};
  for (var i = 0; i < configData[module].masters.length; i++) {
    if (
      configData[module].isSpecificHeader &&
      configData[module].masters[i].hasOwnProperty("specificHeaders")
    ) {
      if (
        !specificHeaderObj[module.toLowerCase()][
          configData[module].masters[i].masterName.toLowerCase()
        ]
      )
        specificHeaderObj[module.toLowerCase()][
          configData[module].masters[i].masterName.toLowerCase()
        ] = {};
      specificHeaderObj[module.toLowerCase()][
        configData[module].masters[i].masterName.toLowerCase()
      ] =
        configData[module].masters[i].specificHeaders;
    }
  }
}

// console.log(specificHeaderObj);

request.get(
  "http://raw.githubusercontent.com/egovernments/egov-services/master/core/egov-mdms-create/src/main/resources/master-config.json",
  function(error, response, body) {
    if (!error && response.statusCode == 200) {
      var allModuleData = JSON.parse(body);
      // console.log(allModuleData);
      console.log(modules);
      for (allModule in allModuleData) {
        if (modules.indexOf(allModule.toLowerCase()) > -1) {
          console.log(allModule);

          for (allMaster in allModuleData[allModule]) {
            if (!uniqueKeyObj[allModule.toLowerCase()][allMaster.toLowerCase()])
              uniqueKeyObj[allModule.toLowerCase()][
                allMaster.toLowerCase()
              ] = {};
            uniqueKeyObj[allModule.toLowerCase()][allMaster.toLowerCase()] =
              allModuleData[allModule][allMaster].uniqueKeys;
          }
        }
      }
      // console.log(uniqueKeyObj);
    }
  }
);

// console.log(specificHeaderObj);

// for(module in modules){
for (var i = 0; i < urls.length; i++) {
  SwaggerParser.dereference(urls[i])
    .then(function(yamlJSON) {
      // console.log(yamlJSON)
      let module = yamlJSON["x-module"];
      if (module) {
        mainObj[module] = yamlJSON.definitions;
      } else {
        let basePath = [];
        basePath = yamlJSON.basePath.split("-")[0].split(""); // "/asset-services" type pattern should be in basepath
        let index = basePath.indexOf("/");
        if (index > -1) {
          basePath.splice(index, 1);
        }

        mainObj[basePath.join("")] = yamlJSON.definitions;
      }

      completed_requests++;

      if (completed_requests == urls.length) {
        // All downloads done, process responses array
        // console.log(mainObj);
        // console.log(mainObj);
        for (moduleName in mainObj) {
          // 1. get unique keys from config for each master
          // 2. find unique property in mainObj
          // 3. if exists set flag as true

          console.log("module name is - " + moduleName);
          finalSpecs[moduleName.toLowerCase()] = {};
          finalSpecsRaw[moduleName.toLowerCase()] = {};
          for (master in mainObj[moduleName]) {
            finalSpecsRaw[moduleName.toLowerCase()][master.toLowerCase()] =
              mainObj[moduleName][master];
            if (!finalSpecs[moduleName.toLowerCase()].masters)
              finalSpecs[moduleName.toLowerCase()].masters = {};

            if (
              !finalSpecs[moduleName.toLowerCase()].masters[
                master.toLowerCase()
              ]
            )
              finalSpecs[moduleName.toLowerCase()].masters[
                master.toLowerCase()
              ] = {};
            if (
              !finalSpecs[moduleName.toLowerCase()].masters[
                master.toLowerCase()
              ]
            )
              finalSpecs[moduleName.toLowerCase()].masters[
                master.toLowerCase()
              ] = {};

            finalSpecs[moduleName.toLowerCase()].masters[
              master.toLowerCase()
            ].name =
              "";
            finalSpecs[moduleName.toLowerCase()].masters[
              master.toLowerCase()
            ].label =
              "";
            finalSpecs[moduleName.toLowerCase()].masters[
              master.toLowerCase()
            ].type =
              "multiFieldAddToTable";
            finalSpecs[moduleName.toLowerCase()].masters[
              master.toLowerCase()
            ].jsonPath =
              "";

            var header = [];
            var fields = [];
            var spec = getFieldsFromInnerObject(
              fields,
              header,
              mainObj[moduleName][master].properties,
              moduleName,
              master,
              "MdmsMetadata.masterData",
              true,
              mainObj[moduleName][master].required || []
            );

            finalSpecs[moduleName.toLowerCase()].masters[
              master.toLowerCase()
            ].header =
              spec.header;
            finalSpecs[moduleName.toLowerCase()].masters[
              master.toLowerCase()
            ].values =
              spec.fields;
            // console.log("Break-------------------------------------------");
            // console.log(finalSpecs.swm.masters.CollectionPoint);
          }
        }

        // console.log(finalSpecs);
        console.log(finalSpecs.propertytax.masters.propertytype);
      }
    })
    .catch(function(err) {
      console.log(err);
    });
}

// }

app.post("/spec-directory/:module/:master", function(req, res, next) {
  for (var key in req.params) {
    req.params[key] = req.params[key].toLowerCase();
  }

  console.log(req.params);
  console.log(req.params.master);
  var master = req.params.master;
  var module = req.params.module;
  console.log(getPropertyStatus(module, master, "code"));
  if (finalSpecs[module] && finalSpecs[module].masters[master]) {
    res.status(200).json(finalSpecs[module].masters[master]);
  } else {
    res.status(400).json({
      message: "Invalid parameters"
    });
  }

  next();
});

app.get("/spec-directory", (req, res) => {
  res.sendFile(path.join(__dirname, "index.html"));
});

const port = process.env.PORT || "4022";
app.listen(port, function() {
  console.log("Parser listening on port: " + port);
});
