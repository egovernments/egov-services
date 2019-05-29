"use strict";
import http from "http";
import request from "request";
import express from "express";
import path from "path";
import fs from "fs";
import cors from "cors";
import morgan from "morgan";
import bodyParser from "body-parser";
import initializeDb from "./db";
import middleware from "./middleware";
import api from "./api";
import config from "./config.json";
import { PROPERTY } from "./endpoint";
import { httpRequest } from "./api/api";
import receipt_data from './receipt_data.json';
import dataconfig from "./pdfgenerator.json";

import * as pdfmake from "pdfmake/build/pdfmake";
import * as pdfFonts from "pdfmake/build/vfs_fonts";
import get from "lodash/get";
import set from "lodash/set";
import { strict } from "assert";
import { Recoverable } from "repl";
//create binary 

pdfMake.vfs = pdfFonts.pdfMake.vfs;
var pdfMakePrinter = require("pdfmake/src/printer");

let app = express();
app.use(express.static(path.join(__dirname, "public")));
 app.use(bodyParser.json());
 app.use(bodyParser.urlencoded({ extended: true }));

function createPdfBinary(docDefinition, successCallback, errorCallback) {
  try {
    var fontDescriptors = {
      Roboto: {
        normal: "fonts/Roboto-Regular.ttf",
        bold: "fonts/Roboto-Medium.ttf",
        italics: "fonts/Roboto-Italic.ttf",
        bolditalics: "fonts/Roboto-MediumItalic.ttf"
      }
    };
    const printer = new pdfMakePrinter(fontDescriptors);
    const doc = printer.createPdfKitDocument(docDefinition);

    //reference link
    //https://medium.com/@kainikhil/nodejs-how-to-generate-and-properly-serve-pdf-6835737d118e#d8e5

    //storing file on local computer/server
    doc.pipe(
      fs.createWriteStream("src/pdfs/filename.pdf").on("error", err => {
        errorCallback(err.message);
      })
    );

    doc.on("end", () => {
      successCallback("PDF successfully created and stored");
    });

    //filestore API call to store file on S3
    var url =
      "https://egov-micro-dev.egovernments.org/filestore/v1/files?tenantId=default&module=pgr&tag=00040-2017-QR";
    var req = request.post(url, function(err, _resp, body) {
      if (err) {
        console.log("Error!");
      } else {
        console.log("URL: " + body);
      }
    });
    //attaching file with API
    var form = req.form();
    form.append("file", fs.createReadStream("src/pdfs/filename.pdf"));

    doc.end();
  } catch (err) {
    throw err;
  }
}

app.post("/pdf", function(req, res) {
  //direct mapping service
  var directArr=[];         
  var obj={
     jPath:"",
     val:"",
      type:"",
      format:""
      
  };
  var o={};
  o=get(dataconfig,'DataConfigs.mappings[0].mappings[0].direct',[]);
  
  directArr=o.map((item)=>{   
    return {
      jPath:item.variable,
      val:get(req.body,item.value.path,''),
      valJsonPath:item.value.path,
      type:item.type,
      format:item.format      
    }
  });
  
  
  for(var i=0;i<directArr.length;i++)
  {
    //for array type direct mapping
    if(directArr[i].type=="array")
    {
      var arrayOfItems=[];
       var ownerObject={};
       var arrayOfOwnerObject=[];
       ownerObject=get(receipt_data,directArr[i].jPath+"[0]",[]);      
     
      //  console.log(get(receipt_data,directArr[i].jPath,[]));     
      let {format={},val=[],variable}=directArr[i];
      let {scema=[]}=format;
      
      //taking values about owner from req body
      for(var j=0;j<val.length;j++)
      {
        var x=1; 
        for(var k=0;k<scema.length;k++)
        {           
          set(ownerObject[x],"text",get(val[j],scema[k],""));
          x+=2;
        }
        arrayOfOwnerObject.push(ownerObject);        
      }
      set(receipt_data,directArr[i].jPath,arrayOfOwnerObject);          
    }
    else  //setting value in pdf for no type direct mapping
    {
      set(receipt_data,directArr[i].jPath,directArr[i].val);
    }
  }

  // var util = require('util');
  // fs.writeFileSync('./data.txt', util.inspect(JSON.stringify(receipt_data)) , 'utf-8');
  // console.log(JSON.stringify(receipt_data));
   
  //external API mapping
  var externalAPIArray=[];
  var oEA={};
  oEA=get(dataconfig,'DataConfigs.mappings[0].mappings[2].externalAPI',[]);
  externalAPIArray=oEA.map((item)=>{   
    return {
      uri:item.path,
      queryParams:item.queryParam,
      jPath:item.responseMapping,
      body:item.apiRequest,
      variable:"",
      val:""      
    }
  });
  
  for(var i=0;i<externalAPIArray.length;i++)
  {
    for(var j=0;j<externalAPIArray[i].jPath.length;j++)
    {
      externalAPIArray[i].variable=externalAPIArray[i].jPath[j].variable;
      externalAPIArray[i].val=externalAPIArray[i].jPath[j].value;
    }
  }
  
  for(var i=0;i<externalAPIArray.length;i++)
  { 
    var point=0;
    var temp1="";
    var temp2="";
    var flag=0;
    
    for(var j=0;j<externalAPIArray[i].queryParams.length;j++)
    {      
      if(externalAPIArray[i].queryParams[j]=="$")
      {
        flag=1;  
      }      
      if(externalAPIArray[i].queryParams[j]==",")
      {
        if(flag==1)
        {
          temp2=temp1;
          temp1=temp1.replace("$.","");          
          var temp3=get(req.body,temp1,'vikas');          
          externalAPIArray[i].queryParams=externalAPIArray[i].queryParams.replace(temp2,temp3);
          
          j=0;
          flag=0;
          temp1="";
          temp2="";            
        }                   
      }
      if(flag==1)
      {
        temp1+=externalAPIArray[i].queryParams[j];        
      }
      if(j==externalAPIArray[i].queryParams.length-1 && flag==1)
      {           
          temp2=temp1;
          temp1=temp1.replace("$.","");   
          var temp3=get(req.body,temp1,'vikas');
          
          externalAPIArray[i].queryParams=externalAPIArray[i].queryParams.replace(temp2,temp3);
          
          flag=0;
          temp1="";
          temp2="";  
      }
    }
    externalAPIArray[i].queryParams=externalAPIArray[i].queryParams.replace(/,/g,"&");        
    
    var req = request.post({          
      url:externalAPIArray[i].uri+"?"+externalAPIArray[i].queryParams,      
      body:JSON.stringify(externalAPIArray[i].body)}, function(err, _resp, body) {
      if (err) {
        console.log("Error!"+err);
      } else {
        console.log("Response: " + _resp.body);
      }
      res.end();      
    });
  }
  
 
  //function to download pdf automatically
  // console.log(req.body);
  createPdfBinary(
    receipt_data,(response) => {
      // doc successfully created
      res.json({
        status: 200,
        data: response
      });
    },
    error => {
      // doc creation error
      res.json({
        status: 400,
        data: error
      });
    }
  );
  


  // function to open PDF
  //  createPdfBinary(receipt_data, (response) => {
  //  	res.setHeader('Content-Type', 'application/pdf');
  //  	console.log(req.body);
  //  	res.send(response).download(); // Buffer data
  //  	},function(error) {
  //  		res.send('ERROR:' + error);
  //  	}
  //  	);

  /*function to sent binary pdf data to s3
	createPdfBinary(dd, function(binary) {
	  res.contentType('application/pdf');
	  console.log(req.body);
	  res.send(binary);
	}, function(error) {
	  res.send('ERROR:' + error);
	});*/
});

app.post("/create-receipt", (req, res) => {
  console.log(req.body);
  //const data=req.body;
  res.send(httpRequest(PROPERTY.GET.URL));
});

const PORT = 5000;

app.listen(PORT, () => {
  console.log(`Server running at http:${PORT}/`);
});

export default app;
