const Ajv = require("ajv");
const ajv = new Ajv({ allErrors: true });
const fireNOCSchema = require("../model/fireNOC.js");
const fireNOCSearchSchema = require("../model/fireNOCSearch.js");




export const validateFireNOCModel = data => {
  let validate = ajv.compile(fireNOCSchema);
  var valid = validate(data);
  let errors = [];
  if (!valid) {
    errors = validate.errors;
  }
  return errors;
};


export const validateFireNOCSearchModel = data => {
  let validate = ajv.compile(fireNOCSearchSchema);
  var valid = validate(data);
  let errors = [];
  if (!valid) {
    errors = validate.errors;
  }
  return errors;
};
