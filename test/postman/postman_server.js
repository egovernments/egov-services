const express = require("express");
const fs = require("fs")
const app = express()
const watch = require('node-watch');
const walk = require('fs-walk');
const path = require('path')
const compression = require('compression')

app.use(compression())

let folders = [
    "scripts",
    "data_templates"
]
for (let folder of folders) {
    app.use("/" + folder, express.static(folder))
}

let files_cache = {}

function updateFile(filename) {

    let file_data = fs.readFileSync(filename, "utf8");

    if (/\.json$/i.test(filename)) {
        try {
            JSON.parse(file_data)
        } catch (ex) {
            console.log("File has errors, not loading it", filename, ex.message)
            return
        }
    }

    // console.log("Loaded " + filename)
    files_cache[filename] = file_data
}

const directoryWalker = (basedir, filename, stat) => {
    let file_path = path.join(basedir, filename);
    if (stat.isDirectory()) {
        walk.walkSync(file_path, directoryWalker);
    } else if (stat.isFile()) {
        updateFile(file_path)
    }
}

folders.forEach((dir) => walk.walkSync(dir, directoryWalker))

watch(folders, {recursive: true}, function (event, filename) {
    if (event == "update") {
        console.log(event, filename)
        updateFile(filename);
    }
})

app.get('/', (req, res) => res.send('Hello World!'))
app.get('/get/all', (req, res) => {
        let format = req.query.format || "json";
        let only_filenames = typeof req.query.only_filenames === "undefined" ? true: req.query.only_filenames;

        if (format == "json") {
            res.json(files_cache)
        } else if (format === "environment" || format === "globals") {
            let environment_file = {}
            const uuid = require("uuid").v1;
            environment_file["id"] = uuid()
            environment_file["name"] = "Postman " + format + " Variables"
            environment_file["_postman_variable_scope"] = format
            environment_file["_postman_exported_using"] = "PostmanServer"

            // environment_file["_postman_exported_at"=: \"2018-01-30T06:21:02.944Z\",\n"]
            let values = []

            for (const [key, value] of Object.entries(files_cache)) {
                values.push({
                    "type": "text",
                    "enabled": true,
                    "key": only_filenames ? path.basename(key) : key,
                    "value": value
                })
            }

            environment_file["values"] = values

            res.json(environment_file)
        }
    }
)
app.listen(8787, () => console.log('Example app listening on port 8787!'))

