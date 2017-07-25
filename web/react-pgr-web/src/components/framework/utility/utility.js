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