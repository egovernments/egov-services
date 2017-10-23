const fs = require('fs');
const mkdirp = require('mkdirp');

function setLabels(json, path) {
    mkdirp.sync(path);
    try {
        var file = fs.readFileSync(path + "/default.json");
    } catch (e) {
        var file = "";
    }

    if (file) {
        json = Object.assign({}, JSON.parse(file), json);
    }

    fs.writeFileSync(path + "/default.json", JSON.stringify(json));
}

function getType(type) {
    switch (type) {
        case 'integer':
        case 'float':
        case 'double':
        case 'long':
        case 'number':
            return 'number';
        case 'string':
            return 'text';
        case 'boolean':
            return 'radio';
        case 'date':
            'datePicker';
        default:
            return '';
    }
}

function getTitleCase(field) {
    if (field) {
        var newField = field[0].toUpperCase();
        for (let i = 1; i < field.length; i++) {
            if (field[i - 1] != " " && field[i] != " ") {
                newField += field.charAt(i).toLowerCase();
            } else {
                newField += field[i]
            }
        }
        return newField;
    } else {
        return "";
    }
}

exports.getTitleCase = getTitleCase;
exports.getType = getType;
exports.setLabels = setLabels;