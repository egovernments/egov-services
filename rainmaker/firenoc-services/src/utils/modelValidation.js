const Ajv = require("ajv");
const ajv = new Ajv({ allErrors: true });
const schema = require("../model/fireNOC.js");

let validate = ajv.compile(schema);

export const validateModel = data => {
  var valid = validate(data);
  let errors = [];
  if (!valid) {
    errors = validate.errors;
  }
  return errors;
};
