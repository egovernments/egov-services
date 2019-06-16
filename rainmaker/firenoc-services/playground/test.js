const Ajv = require("Ajv");
const ajv = new Ajv({ loadSchema: loadSchema });
const schema = require("../src/swagger.json");
// const axios = require("axios");

const data = require("./fireNOCRequest.json");

ajv.addSchema(schema, "swagger.json");

// ajv
//   .compileAsync({ $ref: "swagger.json#/definitions/FireNOCRequest" })
//   .then(function(validate) {
//     var valid = validate(data);
//     if (!valid) console.log(validate.errors);
//   });
//
// function loadSchema(uri) {
//   console.log("tst");
//   return axios
//     .get(uri)
//     .then(function(res) {
//       return res.body;
//     })
//     .catch(function(error) {
//       throw new Error("Loading error: " + error.statusCode);
//     });
// }

let validate=ajv.compile({ $ref: 'swagger.json#/definitions/FireNOCRequest' });

if (validate(data)) console.log(validate.errors);
