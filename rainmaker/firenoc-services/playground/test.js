const Ajv = require("Ajv");
const ajv = new Ajv({ allErrors: true });

const schema = require("../src/swagger.json");

const data;

var valid = ajv.validate(schema, data);
if (!valid) console.log(ajv.errors);
