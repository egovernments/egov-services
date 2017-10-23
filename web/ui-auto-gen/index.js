var SwaggerParser = require('swagger-parser');
var fs = require("fs");
const readline = require('readline');

const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});

const searchTemplate = require('./templates/search');
const createTemplate = require('./templates/create');
const viewTemplate = require('./templates/view');
const updateTemplate = require('./templates/update');

rl.question('Enter YAML URL/PATH: ', (yamlpath) => {
    rl.question('Enter Template Parser Path (Leave empty if you want to use default): ', (templateParser) => {
        rl.question('Enter module name (Defaults to "specs"): ', (module) => {
            rl.question("Enter path to output folder(Defaults to current folder, please provide absolute paths): ", (outputFolderPath) => {
                SwaggerParser.bundle(yamlpath)
                    .then(function(yamlJSON) {
                        //return fs.writeFileSync(outputFileName || "specs.js", JSON.stringify(yamlJSON));
                        if (templateParser) {
                            require(templateParser)(yamlJSON, function(specifications) {
                                output("specs.js", specifications);
                            })
                        } else {
                            let basePath = yamlJSON.basePath;
                            let specifications = {};
                            let allUiInfo = {};
                            let errors = {};
                            for (var i = 0; i < yamlJSON["x-ui-info"].UIInfo.length; i++) {
                                allUiInfo[yamlJSON["x-ui-info"].UIInfo[i].referencePath] = yamlJSON["x-ui-info"].UIInfo[i];
                            }


                            for (let key in yamlJSON.paths) {
                                let arr = key.split("/");
                                arr.splice((arr.length - 1), 1);
                                let xPath = arr.join("/");
                                if (!allUiInfo[xPath]) continue;
                                if (/_search/.test(key)) {
                                    specifications[xPath + ".search"] = searchTemplate((module || "specs"), 4, basePath + key, yamlJSON.paths[key], yamlJSON.parameters, allUiInfo[xPath]);
                                    if(specifications[xPath + ".search"].errors) {
                                        errors = Object.assign({}, errors, specifications[xPath + ".search"].errors);
                                    } else {
                                        specifications[xPath + ".search"] = specifications[xPath + ".search"].specifications;
                                    }
                                } else if (/_create/.test(key)) {
                                    specifications[xPath + ".create"] = createTemplate((module || "specs"), 4, basePath + key, yamlJSON.paths[key], yamlJSON.definitions, allUiInfo[xPath]);
                                    specifications[xPath + ".view"] = viewTemplate((module || "specs"), 4, basePath + key, yamlJSON.paths[key], yamlJSON.definitions, allUiInfo[xPath]);
                                    if(specifications[xPath + ".create"].errors) {
                                        errors = Object.assign({}, errors, specifications[xPath + ".create"].errors);
                                    } else {
                                        specifications[xPath + ".create"] = specifications[xPath + ".create"].specifications;
                                    }
                                    if(specifications[xPath + ".view"].errors) {
                                        errors = Object.assign({}, errors, specifications[xPath + ".view"].errors);
                                    } else {
                                        specifications[xPath + ".view"] = specifications[xPath + ".view"].specifications;
                                    }
                                } else if (/_update/.test(key)) {
                                    specifications[xPath + ".update"] = updateTemplate((module || "specs"), 4, basePath + key, yamlJSON.paths[key], yamlJSON.definitions, allUiInfo[xPath]);
                                    if(specifications[xPath + ".update"].errors) {
                                        errors = Object.assign({}, errors, specifications[xPath + ".update"].errors);
                                    } else {
                                        specifications[xPath + ".update"] = specifications[xPath + ".update"].specifications;
                                    }
                                }
                            }

                            if(Object.keys(errors).length) {
                                console.log("\x1b[31mERROR OCCURED! CANNOT PROCESS YAML.");
                                for(let key in errors) {
                                    console.log("ERROR::" + key + "-" + errors[key]);
                                }
                                console.log("\x1b[37m");
                                process.exit();
                            }

                            let specsObj = {};
                            for (var key in specifications) {
                                if (!specsObj[key.split(".")[0]]) specsObj[key.split(".")[0]] = {};
                                var _partkey = (module || "specs") + "." + key.split(".")[1];
                                specsObj[key.split(".")[0]][_partkey] = specifications[key];
                            }

                            for (var key in specsObj) {
                                let filePath = key.replace(/\//g, "_");
                                fs.writeFileSync(outputFolderPath ? (outputFolderPath.replace(/\/$/, "") + "/" + filePath + ".js") : (filePath + ".js"), "var dat = " + JSON.stringify(specsObj[key]) + "\n export default dat;");
                            }

                            console.log("SUCCESSFULLY CREATED.");
                            rl.close();
                            process.exit();
                        }
                    })
                    .catch(function(err) {
                        console.error(err);
                        rl.close();
                        process.exit();
                    });
            })
        })
    })
})