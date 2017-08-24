import Api from '../../../api/api';

export function fileUpload(file, module, cb) {
  if(file.constructor == File) {
    let formData = new FormData();
    formData.append("tenantId", localStorage.getItem('tenantId'));
    formData.append("module", module);
    formData.append("file", file);
    Api.commonApiPost("/filestore/v1/files",{}, formData).then(function(response) {
      cb(null, response);
    }, function(err) {
      cb(err.message);
    })
  } else {
    cb(null, {files: [{
      fileStoreId: file
    }]});
  }
}

export function getInitiatorPosition(cb) {
  if(localStorage.userRequest) {
    var employeeId = JSON.parse(localStorage.userRequest).id;
    Api.commonApiPost("/hr-employee/employees/_search", { id: employeeId }, {}).then(function(res) {
      if(res && res.Employee && res.Employee[0] && res.Employee[0].assignments && res.Employee[0].assignments[0] && res.Employee[0].assignments[0].position) {
        cb(null, res.Employee[0].assignments[0].position);
      } else {
        cb(null, "");
      }
    }, function(err) {
      cb(err);
    })
  } else {
    cb(null, "");
  }
}

export function getTitleCase(field) {
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
