
import request from "request";
import fs from "fs";
import get from "lodash/get";
import axios, { post } from 'axios';
var FormData = require('form-data')
export const fileStoreAPICall=async function(filename){
    var url =
      "https://egov-micro-dev.egovernments.org/filestore/v1/files?tenantId=default&module=pdfgen&tag=00040-2017-QR";
  
      var form = new FormData();
      form.append("file", fs.createReadStream(filename));
      let response=await axios.post(url, form,  {headers: {
        ...form.getHeaders(),
      }});
      return get(response.data,"files[0].fileStoreId");


      // var res = await request.post(url, function(err, _resp, body) {
    //   if (err) {
    //     console.log("Error!"+err);
    //   } else {
    //     console.log("URL: " + body); 
    //     let filestoreid=get(JSON.parse(body),"files[0].fileStoreId","NA"); 
    //     callback(filestoreid);  
    //   } 
    // });
    // //attaching file with API
    // var form = res.form();
    // form.append("file", fs.createReadStream("src/pdfs/"+key+" "+fileNameAppend+".pdf"));
}