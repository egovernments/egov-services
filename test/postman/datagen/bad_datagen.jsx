const SwaggerParser = require("swagger-parser");
const yamljs = require("yamljs");

const fs = require("fs")
// const log = require("winston-color")
// log.level = 'debug';

const winston = require('winston');
const wcf = require('winston-console-formatter');

const log = new winston.Logger({
    level: 'debug',
});

const {formatter, timestamp} = wcf();

const JSONPath = function () {
    this.performOperations = (data, operations) => {
        for (let op in operations) {
            let json_path = operations[op]["json_path"]
            let value = operations[op]["value"];
            console.log("Performing - " + json_path + " - " + value)

            if (value == "##delete##")
                this.remove(data, json_path)
            else
                this.set(data, json_path, value)

        }
    }

    this.pathToArray = path => path
        .split(/[.\[\]]/)
        .map(f => {
            let v = f;
            if (v !== '' && !isNaN(v) && isFinite(v))
                v = parseFloat(v);
            else
                v = f.replace(/^['"]|['"]$/g, '')
            return v
        })
        .filter(f => f !== '');

    this.get = (obj, path) => {
        let path2 = this.pathToArray(path)
        let data = path2.reduce((a, b) => {
            if (a == "$")
                a = obj;
            return a[b];
        })

        return data;
    }

    this.set = (obj, path, value) => {
        path = this.pathToArray(path)
        let lastObject = obj;
        if (path.length > 2) {
            lastObject = path.slice(0, -1).reduce((a, b) => {
                if (a === "$")
                    a = obj;
                return a[b];
            })
        }

        let lastIndex = path[path.length - 1];
        if (lastObject instanceof Array && !isNaN(lastIndex) && lastIndex + 1 > lastObject.length) {
            // The index we are trying to set, actually doesn't exists. So we need to create it
            // there are few possibilities here, the index is for the new element in the array at the end
            // or it for an existing element or it is for an element way beyond the current length
            if (lastIndex == lastObject.length)
                lastObject.push(value)
            else if (lastIndex > lastObject.length)
                throw new Error("The index you are trying to set for the array is invalid - " + lastIndex + ". The array should be large enough or you should set value of lastIndex + 1")
        }
        lastObject[lastIndex] = value
        return value;
    }

    this.remove = (obj, path) => {
        path = this.pathToArray(path)
        let lastObject = path.slice(0, -1).reduce((a, b) => {
            if (a === "$")
                a = obj;
            return a[b];
        })

        delete lastObject[path[path.length - 1]]
    }
}

let jp = new JSONPath()

log.add(winston.transports.Console, {
    formatter,
    timestamp,
});

const path = require('path');
// const appRoot = path.resolve(__dirname, '..') + '/';

// const readline = require('readline');
//
// const rl = readline.createInterface({
//     input: process.stdin,
//     output: process.stdout
// });

if (process.argv.length <= 2) {
    console.log("Usage: " + __filename + " <spec.yaml path>");
    process.exit(-1);
}

var yamlpath = process.argv[2];

// rl.question('Enter Data Gen YAML URL/PATH: ', (yamlpath) => {

log.info("Provided Spec YAML " + yamlpath)
let spec = yamljs.load(yamlpath)
log.info("Spec YAML loaded")

let defaults = {
    enabled: true,
    ignorefields: [],
    data: {}
}

let output_folder = path.resolve(spec["output_folder"])
if (!fs.existsSync(output_folder)) {
    fs.mkdirSync(output_folder);
}

let contract_url = spec["contract"]

function isParamCircular(param_name, parameters) {
    let properties = parameters["properties"]
    let param = properties[param_name]

    return (param.properties
        && param.properties[param_name]
        && param.properties[param_name] == properties[param_name])
        ||
        (properties[param_name].items
            && properties[param_name].items.properties == parameters.properties)
}

log.info("Loading contract YAML - " + contract_url)
SwaggerParser.dereference(contract_url)
    .then(function (contract_yaml) {
        //log.debug("contract loaded")

        for (const api_path in spec["apis"]) {
            // var yamlResolved = yamljs.stringify(contract_yaml , 8, 2);
            let api = Object.assign({}, defaults, spec["defaults"], spec["apis"][api_path])
            api["ignorefields"] = api["ignorefields"].concat(spec["defaults"]["ignorefields"])

            if (!api.enabled) {
                log.silly("API has been disabled, please enable to generate bad data spec for " + api_path)
                continue
            } else {
                log.info("Processing API", api_path)
            }


            let referenceFields = api["reference"] || []
            var generatedFields = api["generated"] || []

            let api_ignored_fields = api["ignorefields"] || []

            var ignoredFields = [
                ...api_ignored_fields,
                ...Object.keys(referenceFields),
                ...Object.keys(generatedFields)
            ]

            const apiCall = contract_yaml["paths"][api_path]["post"]

            const parameters = apiCall["parameters"]
            let data = {}

            let body_params = parameters.filter(elem => elem.in == "body")

            let data2 = get_params_data(body_params[0]["schema"], data, "$")

            function generate_test(param, param_name, path, type) {
                var tests = [];
                let minimum = param.minimum;
                let maximum = param.maximum;

                if (param.exclusiveMinimum)
                    minimum += 1
                if (param.exclusiveMaximum)
                    maximum -= 1

                var common = {
                    api_path: api_path,
                    field_name: param_name,
                    field_path: path,
                    type: type,
                    subtype: "",
                    minimum: "",
                    maximum: ""
                };

                switch (type) {
                    case "required":
                        // tests.push(Object.assign({}, common, {
                        //     subtype: "null",
                        //     value: null
                        // }));
                        tests.push(Object.assign({}, common, {
                            subtype: "missing",
                            operations: [{json_path: path, value: "##delete##"}],
                        }));
                        break;
                    case "array":
                        // TODO: Evaluate if a object instead of array test is required?
                        // tests.push(Object.assign({}, common, {
                        //     subtype: "object_instead_array",
                        //     value: {}
                        // }));
                        // TODO: Evaluate if a blank array test is required?
                        // tests.push(Object.assign({}, common, {
                        //     subtype: "blank_array",
                        //     value: []
                        // }));
                        break;
                    case "maxLength":
                        tests.push(Object.assign({}, common, {
                            type: "length",
                            subtype: "maximum",
                            minimum: param.minLength,
                            maximum: param.maxLength,
                            operations: [{
                                json_path: path,
                                value: "{{$randomString(" + (param.maxLength + 1) + ")}}"
                            }],
                            // value: randomstring.generate(param.maxLength + 1)
                        }));
                        break;
                    case "minLength":
                        tests.push(Object.assign({}, common, {
                            type: "length",
                            subtype: "minimum",
                            minimum: param.minLength,
                            maximum: param.maxLength,
                            operations: [{
                                json_path: path,
                                value: "{{$randomString(" + (param.minLength - 1) + ")}}"
                            }],
                            // value: randomstring.generate(param.minLength - 1)
                        }));
                        break;
                    case "number":
                        // tests.push(Object.assign({}, common, {
                        //     subtype: "string",
                        //     value: "abc"
                        // }));
                        // tests.push(Object.assign({}, common, {
                        //     subtype: "null",
                        //     value: null
                        // }));
                        break;
                    case "maximum":

                        tests.push(Object.assign({}, common, {
                            type: "intvalue",
                            subtype: "maximum",
                            minimum: minimum,
                            maximum: maximum,
                            // expected_error_message: "Value of " + param_name + " shall be between " + param.minimum + " and " + param.maximum,
                            operations: [{json_path: path, value: maximum + 1}],
                            // value: param.maximum + 1
                        }));
                        break;
                    case "minimum":
                        tests.push(Object.assign({}, common, {
                            type: "intvalue",
                            subtype: "minimum",
                            minimum: minimum,
                            maximum: maximum,
                            // expected_error_message: "Value of " + param_name + " shall be between " + minimum + " and " + maximum,
                            operations: [{json_path: path, value: minimum - 1}],
                            // value: param.minimum - 1
                        }));
                        break;

                }

                return tests;
            }

            function get_params_data(parameters, data, path) {
                path = path || "$.";

                if (!data) {
                    data = {};
                }

                data.tests = data.tests || [];
                data.sample_json = data.sample_json || {};
                let sample_json = data.sample_json;

                const properties = parameters["properties"];
                for (var param_name in properties) {
                    let current_path = path + "." + param_name;
                    var param = properties[param_name];

                    if (param_name === "code" || param_name === "fileStoreId") {
                        log.warn(`You are probably processing a reference or generated field - "${path}.${param_name}". Please review your spec file`)
                    }

                    //log.debug(`Processing ${path} ${param_name}`)


                    // if (param_name === "parent") {
                    //     log.info("Encountered field parent, might lead to recursion, ignoring the field");
                    //     continue;
                    // }


                    if (!param.readOnly
                        && !(param_name in generatedFields)
                        && param_name !== 'id'
                    ) {
                        let sample_values = {
                            "string": "",
                            "number": 0,
                            "integer": 0,
                            "object": {},
                            "boolean": true,
                            "null": null,
                            "array": []
                        };

                        let isParamArray = param.type === "array"
                        let isParamObject = param.type === "object"

                        jp.set(sample_json, path + "." + param_name, sample_values[param.type])

                        if (param_name in referenceFields) {
                            // If it is a reference field then we need to set a sample object inside the same
                            // for array it would be in form of [{}]
                            let props = isParamArray ? param.items.properties : param.properties

                            let inner_data;
                            if (isParamObject || (isParamArray && param.items.type === "object")) {
                                inner_data = {}
                                let refFields = referenceFields[param_name] || {}

                                for (let [k, _] of Object.entries(refFields)) {
                                    inner_data[k] = sample_values[props[k].type]
                                }
                            } else if (!isParamArray) {
                                inner_data =  sample_values[param.type]
                            }

                            jp.set(sample_json, path +"." + param_name, isParamArray? [inner_data]: inner_data)
                        } else if (isParamArray) {
                            jp.set(sample_json, path + "." + param_name + "[0]", sample_values[param.items.type])
                        }
                    }

                    if (isParamCircular(param_name, parameters)) {
                        log.info("Circular reference detected for - " + path
                            + "." + param_name + ". Skipping the field")
                        continue
                    }

                    if (ignoredFields.indexOf(param_name) >= 0) {
                        if (param_name in spec["defaults"]["data"]) {
                            jp.set(sample_json, path + "." + param_name, spec["defaults"]["data"][param_name])
                        }
                        continue;
                    }

                    // if (param.in !== "body")
                    //     continue;

                    if (parameters.required && parameters["required"].indexOf(param_name) > 0) {
                        var tests = generate_test(param, param_name, current_path, "required");
                        if (tests)
                            data.tests.push(...tests);
                    }

                    if (param.type == "object") {
                        if (param.schema)
                            data = get_params_data(param["schema"], data, current_path);
                        else if (param.properties)
                            data = get_params_data(param, data, current_path);
                    }
                    else if (param.type == "array") {
                        var tests = generate_test(param, param_name, current_path, "array");
                        data.tests.push(...tests);
                        data = get_params_data(param["items"], data, current_path + "[0]");
                    }
                    else {
                        // console.log("Let's do something about ", param);

                        if (param.type == "string") {
                            if (param.maxLength) {
                                var tests = generate_test(param, param_name, current_path, "maxLength");
                                data.tests.push(...tests);

                            }
                            if (param.minLength) {
                                if (param.minLength <= 1) {
                                    // we need not do anything about this case because when we use length 0 and
                                    // if field is required then it will be automatically be covered by required field missing/blank test case
                                    // if it is not required then length 0 is still a valid value
                                }
                                else {
                                    var tests = generate_test(param, param_name, current_path, "minLength");
                                    data.tests.push(...tests);
                                }
                            }
                        }

                        if (param.type == "number" || param.type == "integer") {
                            var tests = generate_test(param, param_name, current_path, "number");
                            data.tests.push(...tests);

                            if (param.maximum) {
                                tests = generate_test(param, param_name, current_path, "maximum");

                                data.tests.push(...tests);
                            }

                            if (param.minimum) {
                                tests = generate_test(param, param_name, current_path, "minimum");
                                // data.tests.push(util.format("%s.%s field, send value lower than minimum %s", path, param_name, param.minimum));
                                data.tests.push(...tests);
                            }
                        }

                    }

                }

                return data;

            }

            fs.writeFileSync(path.join(output_folder, api_path.replace(/\//g, "") + ".json"), JSON.stringify(data.tests, null, 2));
            fs.writeFileSync(path.join(output_folder, api_path.replace(/\//g, "") + ".sample.json"), JSON.stringify(data.sample_json, null, 2));
            log.info(`Test data generated for ${api_path} - Total ${data.tests.length} tests`)
        }
    }).catch((err) => {
    log.error("Error occurred" + err.message);
    process.exit(10);
})


// })
