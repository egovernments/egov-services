const express = require('express');
const path = require('path');
const http = require('http');
const bodyParser = require('body-parser');
const fs = require('fs');
const SwaggerParser = require('swagger-parser');
const app = express();
const zip = require('express-zip');
const mkdirp = require('mkdirp');

const searchTemplate = require('./templates/search');
const createTemplate = require('./templates/create');
const viewTemplate = require('./templates/view');
const updateTemplate = require('./templates/update');

//===================PARSER===================//
let parse = function(yamlPath, module, cb) {
    mkdirp.sync("./output/" + module);

    SwaggerParser.bundle(yamlPath)
        .then(function(yamlJSON) {

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
                    if (specifications[xPath + ".search"].errors) {
                        errors = Object.assign({}, errors, specifications[xPath + ".search"].errors);
                    } else {
                        specifications[xPath + ".search"] = specifications[xPath + ".search"].specifications;
                    }
                } else if (/_create/.test(key)) {
                    specifications[xPath + ".create"] = createTemplate((module || "specs"), 4, basePath + key, yamlJSON.paths[key], yamlJSON.definitions, allUiInfo[xPath]);
                    specifications[xPath + ".view"] = viewTemplate((module || "specs"), 4, basePath + key, yamlJSON.paths[key], yamlJSON.definitions, allUiInfo[xPath]);
                    if (specifications[xPath + ".create"].errors) {
                        errors = Object.assign({}, errors, specifications[xPath + ".create"].errors);
                    } else {
                        specifications[xPath + ".create"] = specifications[xPath + ".create"].specifications;
                    }
                    if (specifications[xPath + ".view"].errors) {
                        errors = Object.assign({}, errors, specifications[xPath + ".view"].errors);
                    } else {
                        specifications[xPath + ".view"] = specifications[xPath + ".view"].specifications;
                    }
                } else if (/_update/.test(key)) {
                    specifications[xPath + ".update"] = updateTemplate((module || "specs"), 4, basePath + key, yamlJSON.paths[key], yamlJSON.definitions, allUiInfo[xPath]);
                    if (specifications[xPath + ".update"].errors) {
                        errors = Object.assign({}, errors, specifications[xPath + ".update"].errors);
                    } else {
                        specifications[xPath + ".update"] = specifications[xPath + ".update"].specifications;
                    }
                }
            }

            if (Object.keys(errors).length) {
                return cb(errors);
            }

            let specsObj = {};
            for (var key in specifications) {
                if (!specsObj[key.split(".")[0]]) specsObj[key.split(".")[0]] = {};
                var _partkey = (module || "specs") + "." + key.split(".")[1];
                specsObj[key.split(".")[0]][_partkey] = specifications[key];
            }
            let fileNames = [];
            for (var key in specsObj) {
                let filePath = key.replace(/\//g, "_");
                fileNames.push({
                    path: "./output/" + module + "/" + filePath + ".js",
                    name: filePath + ".js"
                });
                fs.writeFileSync("./output/" + module + "/" + filePath + ".js", "var dat = " + JSON.stringify(specsObj[key]) + "\n export default dat;");
                setTimeout(function() { fs.unlinkSync("./output/" + module + "/" + filePath + ".js") }, 1000 * 10);
            }

            fileNames.push({
                path: "./output/" + module + "/default.json",
                name: "default.json"
            });

            cb(null, fileNames);

        })
        .catch(function(err) {
            console.error(err);
            rl.close();
            process.exit();
        });
}
//============================================//

app.use(express.static(path.join(__dirname, 'app')));
app.use(bodyParser.json());
// Catch all other routes and return the index file
app.get('*', (req, res) => {
    res.sendFile(path.join(__dirname, 'app/index.html'));
});

app.post('/yaml/create', function(req, res) {
    if (!req.body.url || !req.body.module) {
        res.status(400).json({
            message: "Invalid parameters"
        })
    } else {
        parse(req.body.url, req.body.module, function(errors, fileNames) {
            if (errors) {
                res.status(400).json({
                    errors: errors
                })
            } else {
                res.zip(fileNames);
            }
        });
    }
})

const port = process.env.PORT || '8081';
app.listen(port, function() {
    console.log('Parser listening on port 8081!')
})