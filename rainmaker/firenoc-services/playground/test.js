const Ajv = require("Ajv");
const ajv = new Ajv({ allErrors:true });
const schema = require("../src/model/fireNOC.js");
// console.log(schema);
// const axios = require("axios");

const data = require("./fireNOCRequest.json");
// console.log(data);

// ajv.addSchema(schema, "swagger.json");

// ajv
//   .compileAsync({ $ref: "swagger.json#/definitions/FireNOCRequest" })
//   .then(function(validate) {
//     var valid = validate(data);
//     if (!valid) console.log(validate.errors);
//   });
//
// function loadSchema(uri) {
//   return axios
//     .get(uri)
//     .then(function(res) {
//       return res.body;
//     })
//     .catch(function(error) {
//       throw new Error("Loading error: " + error.statusCode);
//     });
// }
//

let validate=ajv.compile(schema);

var valid = validate(data);

if (!valid) console.log(validate.errors);


// console.log('test');
