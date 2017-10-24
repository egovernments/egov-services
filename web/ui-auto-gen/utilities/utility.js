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
            return 'number';
        case 'float':
            return 'number';
        case 'double':
            return 'number';
        case 'long':
            return 'number';
        case 'number':
            return 'number';
        case 'string':
            return 'text';
        case 'boolean':
            return 'radio';
        case 'date':
            return 'datePicker';
        case 'email':
            return 'email';
        case 'pan':
            return 'pan';
        case 'pinCode':
            return 'pinCode';
        case 'mobileNumber':
            return 'mobileNumber';
        case 'autoComplete':
            return 'autoCompelete';
        case 'aadhar':
            return 'aadhar';
        case 'checkbox':
            return 'checkbox';
        case 'singleValueList':
            return 'singleValueList';
        case 'multiValueList':
            return 'multiValueList';
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


function getQuery(url, key, val) {
    var query = "";
    if(url.indexOf("?") == -1) {
        query = "?";
    }

    return (query + "|" + key + "|" + val);
}

exports.getTitleCase = getTitleCase;
exports.getType = getType;
exports.setLabels = setLabels;
exports.getQuery = getQuery;