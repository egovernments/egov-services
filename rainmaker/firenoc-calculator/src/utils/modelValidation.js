const Ajv = require("ajv");
const ajv = new Ajv({ allErrors: true });
import { BillingSlabReq, CalculationReq } from "../model/validationReq.js";
// const fireNOCSchema = require("../model/validateReq.js");
// const fireNOCSearchSchema = require("../model/fireNOCSearch.js");

export const validateBillingSlabReq = data => {
  let validate = ajv.compile(BillingSlabReq);
  var valid = validate(data);
  let errors = [];
  if (!valid) {
    errors = validate.errors;
  }
  return errors;
};

export const validateCalculationReq = data => {
  let validate = ajv.compile(CalculationReq);
  var valid = validate(data);
  let errors = [];
  if (!valid) {
    errors = validate.errors;
  }
  return errors;
};

// export const validateFireNOCSearchModel = data => {
//   let validate = ajv.compile(fireNOCSearchSchema);
//   var valid = validate(data);
//   let errors = [];
//   if (!valid) {
//     errors = validate.errors;
//   }
//   return errors;
// };
