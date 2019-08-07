"use strict";
import http from "http";
import request from "request";
import express from "express";
import path from "path";
import fs, { exists } from "fs";
import cors from "cors";
import morgan from "morgan";
import bodyParser from "body-parser";
import asyncHandler from 'express-async-handler';
import * as pdfmake from "pdfmake/build/pdfmake";
import * as pdfFonts from "pdfmake/build/vfs_fonts";
import get from "lodash/get";
import set from "lodash/set";
import { strict } from "assert";
import { Recoverable } from "repl";
import { fileStoreAPICall } from "./utils/fileStoreAPICall";
import { directMapping } from "./utils/directMapping";
import { externalAPIMapping } from "./utils/externalAPIMapping";
import envVariables from "./EnvironmentVariables";

import {checkifNullAndSetValue} from "./utils/commons";
// import {getFileStoreIds,insertStoreIds} from "./queries";
var jp = require('jsonpath');
//create binary
pdfMake.vfs = pdfFonts.pdfMake.vfs;
var pdfMakePrinter = require("pdfmake/src/printer");

let localisationMap={};
let app = express();
app.use(express.static(path.join(__dirname, "public")));
app.use(bodyParser.json({limit: '10mb', extended: true}));
app.use(bodyParser.urlencoded({limit: '10mb', extended: true }));

let maxPagesAllowed=envVariables.MAX_NUMBER_PAGES;
let serverport=envVariables.SERVER_PORT;
 function createPdfBinary(key,listDocDefinition, successCallback, errorCallback,tenantId) {
   try {
    var fontDescriptors = {
      Roboto: {
        normal: "src/fonts/Roboto-Regular.ttf",
        bold: "src/fonts/Roboto-Medium.ttf",
        italics: "src/fonts/Roboto-Italic.ttf",
        bolditalics: "src/fonts/Roboto-MediumItalic.ttf"
      }
    };
    
    const printer =  new pdfMakePrinter(fontDescriptors);
    let noOfDefinitions=listDocDefinition.length;
    let listOfFilestoreIds=[];
    listDocDefinition.forEach(docDefinition =>
    {
        const doc = printer.createPdfKitDocument(docDefinition);
        let fileNameAppend="-"+new Date().getTime();
        let filename="src/pdfs/"+key+" "+fileNameAppend+".pdf"
        //reference link
        //https://medium.com/@kainikhil/nodejs-how-to-generate-and-properly-serve-pdf-6835737d118e#d8e5
    
        //storing file on local computer/server
        doc.pipe(
          fs.createWriteStream(filename).on("error", err => {
            errorCallback({message:"error occurred while writing pdf: "+err.message});
          }).on("close", () => {
              fileStoreAPICall(filename,tenantId).then((result)=>{
              fs.unlink(filename,()=>{});
              listOfFilestoreIds.push(result);
              if(listOfFilestoreIds.length===noOfDefinitions)
              {
                // insertStoreIds("",);
                successCallback({message:"PDF successfully created and stored",filestoreId:listOfFilestoreIds});
              }
          }).catch(err=>{
            fs.unlink(filename,()=>{});
            console.log(err);
            errorCallback({message:"error occurred while uploading pdf: "+err.message});
          });
        }
        ));
      //   doc.on("end", () => {
      //     //filestore API call to store file on S3    
  //     //filestore API call to store file on S3    
      //     //filestore API call to store file on S3    
      //     fileStoreAPICall(key,fileNameAppend,function(result) {
      //     successCallback({message:"PDF successfully created and stored",filestoreId:result});
      //  });
          
      //   });
        doc.end();
    });
  } catch (err) {
    console.log(err);
    errorCallback({message:"error occured while creating pdf: "+err.message});
  }
}

app.post("/pdf/v1/_create", asyncHandler(async (req, res)=> { 
  try{
   let key=req.query.key;
   let tenantId=req.query.tenantId;
   let requestInfo=get(req.body,"RequestInfo");
   let errorMessage="";
   if((key===undefined)||(key.trim()==="")){
    errorMessage+=" key is missing,";
   }
   if((tenantId===undefined)||(tenantId.trim()==="")){
    errorMessage+=" tenantId is missing,";
   }
   if(requestInfo===undefined){
    errorMessage+=" requestInfo is missing,";
   }
   if(errorMessage!==""){
    res.status(400);
    res.json({
      status: 400,
      data: errorMessage
    });
   }
   else
   {
    var formatconfig=JSON.parse(JSON.stringify(require("./config/format-config/"+key)));
    var dataconfig=require("./config/data-config/"+key);  
    var baseKeyPath=get(dataconfig,"DataConfigs.baseKeyPath");
    let formatObjectArrayObject=[];
    let formatConfigByFile=[];
    let countOfObjectsInCurrentFile=0;
    let moduleObjectsArray=checkifNullAndSetValue(jp.query(req.body,baseKeyPath),[]);
      if(moduleObjectsArray!==[])
      {
        for(var i=0, len=moduleObjectsArray.length; i < len; i++)
        {
          let moduleObject=moduleObjectsArray[i];
          let formatObject=JSON.parse(JSON.stringify(formatconfig));
  
          // Multipage pdf, each pdf from new page
          if((formatObjectArrayObject.length!=0)&&(formatObject["content"][0]!==undefined))
          {
            formatObject["content"][0]["pageBreak"]= "before";
          }
          let variableTovalueMap={};
          //direct mapping service
          await Promise.all([
          directMapping(moduleObject,formatObject,dataconfig,variableTovalueMap,localisationMap,requestInfo)
        ,
          //external API mapping
          externalAPIMapping(key,moduleObject,formatObject,dataconfig,variableTovalueMap,localisationMap,requestInfo)
            ]);
          formatObject=fillValues(variableTovalueMap,formatObject);
          formatObjectArrayObject.push(formatObject["content"]);
          //putting formatconfig in a file to check docdefinition on pdfmake playground online
          countOfObjectsInCurrentFile++;
          if((countOfObjectsInCurrentFile===maxPagesAllowed)||((i+1)==len))
          {
            let formatconfigCopy=JSON.parse(JSON.stringify(formatconfig));
            formatconfigCopy["content"]=formatObjectArrayObject;
            formatConfigByFile.push(formatconfigCopy);
            formatObjectArrayObject=[];
            countOfObjectsInCurrentFile=0;
          }
        }
      }
      else{
        throw {message:`could not find propery in request body with name ${baseKeyPath}`}; 
      }
    
    
  
  
    // var util = require('util');
    // fs.writeFileSync('./data.txt', util.inspect(JSON.stringify(formatconfig)) , 'utf-8');
    //function to download pdf automatically 
    createPdfBinary(
      key,
      formatConfigByFile,
      response => {
        // doc successfully created
        res.status(201);
        res.json({
          status: 201,
          data: response.message,
          filestoreId:response.filestoreId
        });
      },
      error => {
        res.status(500);
        // doc creation error
        res.json({
          status: 500,
          data: "error in createPdfBinary"+error.message
        });
      },tenantId
    );
   }
}
catch(error)
{
  console.log(error);
  res.status(500);
  res.json({
    status: 500,
    data: "some unknown error: "+error.message
  });
}
  // function to open PDF
  //  createPdfBinary(formatconfig, (response) => {
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
  
}));


// app.post("/pdf/v1/_search", asyncHandler(async (req, res)=> { 
//   let jobid=req.query.jobid;
//   let tenantId=req.query.tenantId;
//   getFileStoreIds(jobid,tenantId,res);
// }));


app.listen(serverport, () => {
  console.log(`Server running at http:${serverport}/`);
});

export const fillValues=(variableTovalueMap,formatconfig)=>{
  let mustache = require('mustache');
  mustache.escape = function(text) {return text;};
  let input=JSON.stringify(formatconfig);
  let output=JSON.parse(mustache.render(input, variableTovalueMap).replace(/""/g,"\"").replace(/\\/g,"").replace(/"\[/g,"\[").replace(/\]"/g,"\]").replace(/\]\[/g,"\],\["));
  return output;
} 

export default app;

