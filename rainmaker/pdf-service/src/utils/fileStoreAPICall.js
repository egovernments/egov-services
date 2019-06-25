
import request from "request";
import fs from "fs";
export const fileStoreAPICall=async function(key,fileNameAppend){
    var url =
      "https://egov-micro-dev.egovernments.org/filestore/v1/files?tenantId=default&module=pgr&tag=00040-2017-QR";
    var res = await request.post(url, function(err, _resp, body) {
      if (err) {
        console.log("Error!"+err);
      } else {
        console.log("URL: " + body);                
      }
    });
    //attaching file with API
    var form = res.form();
    form.append("file", fs.createReadStream("src/pdfs/"+key+" "+fileNameAppend+".pdf"));
}