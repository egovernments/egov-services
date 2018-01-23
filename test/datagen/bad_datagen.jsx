const SwaggerParser = require("swagger-parser");
const yamljs = require("yamljs");

const fs = require("fs")
const log = require('winston')
log.level = 'debug';

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

log.info("Provided Spec YAML", yamlpath)
let spec = yamljs.load(yamlpath)
log.info("Spec YAML loaded")

let output_folder = path.resolve(spec["output_folder"])
if (!fs.existsSync(output_folder)) {
    fs.mkdirSync(output_folder);
}

let contract_url = spec["contract"]
let ignore_fields_base = spec["ignorefields"]
log.info("Loading contract YAML", contract_url)
SwaggerParser.dereference(contract_url)
    .then(function (contract_yaml) {
        log.debug("contract loaded")

        for (const api_path in spec["apis"]) {
            log.info("Processing API", api_path)
            // var yamlResolved = yamljs.stringify(contract_yaml , 8, 2);
            let api = spec["apis"][api_path]
            let referenceFields = api["reference"] || []
            var generatedFields = api["generated"] || []

            let api_ignored_fields = api["ignorefields"] || []

            var ignoredFields = [
                ...api_ignored_fields,
                ...ignore_fields_base,
                ...Object.keys(referenceFields),
                ...Object.keys(generatedFields)
            ]

            const apiCall = contract_yaml["paths"][api_path]["post"]

            const parameters = apiCall["parameters"]
            let data = {}
            let data2 = get_params_data(parameters[1]["schema"], data, "$")

            function generate_test(param, param_name, path, type) {
                var tests = [];
                var common = {
                    field_name: param_name,
                    type: type
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
                            subtype: "maximum(" + param.maxLength + ")",
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
                            subtype: "minimum(" + param.minLength + ")",
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
                            subtype: "maximum(" + param.maximum + ")",
                            expected_error_message: "Value of " + param_name + " shall be between " + param.minimum + " and " + param.maximum,
                            operations: [{json_path: path, value: param.maximum + 1}],
                            // value: param.maximum + 1
                        }));
                        break;
                    case "minimum":
                        tests.push(Object.assign({}, common, {
                            type: "intvalue",
                            subtype: "minimum(" + param.minimum + ")",
                            expected_error_message: "Value of " + param_name + " shall be between " + param.minimum + " and " + param.maximum,
                            operations: [{json_path: path, value: param.maximum - 1}],
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

                const properties = parameters["properties"];
                for (var param_name in properties) {
                    var param = properties[param_name];
                    let current_path = path + "." + param_name;

                    // if (param.in !== "body")
                    //     continue;

                    if (parameters.required && parameters["required"].indexOf(param_name) > 0) {
                        var tests = generate_test(param, param_name, current_path, "required");
                        if (tests)
                            data.tests.push(...tests);
                    }


                    if (
                        ignoredFields.indexOf(param_name) >= 0
                    // referenceFields.indexOf(param_name) >= 0 ||
                    // generated.indexOf(param_name) >= 0
                    )
                        continue;

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
            log.debug("test data generated is ", data)
        }
    }).then(() => {
}).catch((err) => {
    log.error("Error occurred", err);
    process.exit(10);
})


// })
