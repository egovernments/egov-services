"use strict";
import http from "http";
import request from "request";
import express from "express";
import logger from "./config/logger";
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
import QRCode from "qrcode";
import {checkifNullAndSetValue} from "./utils/commons";
// import {getFileStoreIds,insertStoreIds} from "./queries";
var jp = require('jsonpath');
//create binary
pdfMake.vfs = pdfFonts.pdfMake.vfs;
var pdfMakePrinter = require("pdfmake/src/printer");

let localisationMap={};
let localisationModuleList=[];
let app = express();
app.use(express.static(path.join(__dirname, "public")));
app.use(bodyParser.json({limit: '10mb', extended: true}));
app.use(bodyParser.urlencoded({limit: '10mb', extended: true }));

let maxPagesAllowed=envVariables.MAX_NUMBER_PAGES;
let serverport=envVariables.SERVER_PORT;
let mustache = require('mustache');
mustache.escape = function(text) {return text;};
let borderLayout = {
  hLineColor: function(i, node) {
    return "#979797";
  },
  vLineColor: function(i, node) {
    return "#979797";
  },
  hLineWidth: function(i, node) {
    return 0.5;
  },
  vLineWidth: function(i, node) {
    return 0.5;
  }
};

/**
 * 
 * @param {*} key - name of the key used to identify module configs. Provided request URL
 * @param {*} listDocDefinition - doc definitions as per pdfmake and formatconfig, each for each file
 * @param {*} successCallback - callaback when success
 * @param {*} errorCallback - callback when error
 * @param {*} tenantId - tenantID
 */
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
            errorCallback({message:"error occurred while writing pdf: "+((typeof err)==='string')?err:err.message});
          }).on("close", () => {
              fileStoreAPICall(filename,tenantId).then((result)=>{
              fs.unlink(filename,()=>{});
              listOfFilestoreIds.push(result);
              if(listOfFilestoreIds.length===noOfDefinitions)
              {
                // insertStoreIds("",);
                logger.info(`PDF successfully created and stored filestoreIds: ${listOfFilestoreIds}`);
                successCallback({message:"PDF successfully created and stored",filestoreId:listOfFilestoreIds});
              }
          }).catch(err=>{
            fs.unlink(filename,()=>{});
            logger.error(err.stack);
            errorCallback({message:"error occurred while uploading pdf: "+((typeof err)==='string')?err:err.message});
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
    logger.error(err.stack);
    errorCallback({message:" error occured while creating pdf: "+((typeof err)==='string')?err:err.message});
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
    let isCommonTableBorderRequired=get(dataconfig,"DataConfigs.isCommonTableBorderRequired");
    let formatObjectArrayObject=[];
    let formatConfigByFile=[];
    let countOfObjectsInCurrentFile=0;
    let moduleObjectsArray=checkifNullAndSetValue(jp.query(req.body,baseKeyPath),[],baseKeyPath);
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
          directMapping(moduleObject,dataconfig,variableTovalueMap,localisationMap,requestInfo,localisationModuleList)
        ,
          //external API mapping
          externalAPIMapping(key,moduleObject,dataconfig,variableTovalueMap,localisationMap,requestInfo,localisationModuleList),
            ]);
          
          await generateQRCodes(moduleObject,dataconfig,variableTovalueMap)
          formatObject=fillValues(variableTovalueMap,formatObject);
          if(isCommonTableBorderRequired===true)
            formatObject=updateBorderlayout(formatObject);
          formatObjectArrayObject.push(formatObject["content"]);
          //putting formatconfig in a file to check docdefinition on pdfmake playground online
          countOfObjectsInCurrentFile++;
          if((countOfObjectsInCurrentFile==maxPagesAllowed)||((i+1)==len))
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
    
    
    logger.info(`Success:Applied templating engine on ${moduleObjectsArray.length} objects output will be in ${formatConfigByFile.length} files`);
  
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
  logger.error(error.stack);
  res.status(500);
  res.json({
    status: 500,
    data: "some unknown error: "+error.message
  });
}
  
}));


// app.post("/pdf/v1/_search", asyncHandler(async (req, res)=> { 
//   let jobid=req.query.jobid;
//   let tenantId=req.query.tenantId;
//   getFileStoreIds(jobid,tenantId,res);
// }));


app.listen(serverport, () => {
  logger.info(`Server running at http:${serverport}/`);
});

/**
 * 
 * @param {*} formatconfig - format config read from formatconfig file
 */
const updateBorderlayout=(formatconfig)=>{
  formatconfig.content=formatconfig.content.map(item=>{
    if(item.hasOwnProperty('layout')&&((typeof item.layout)==='object')&&(Object.keys(item.layout).length===0))
    {
      item.layout=borderLayout;
    }
    return item;
  });
  return formatconfig;
}

/**
 * 
 * @param {*} variableTovalueMap - key, value map. Keys are variable defined in data config
 * and value is their corresponding values. Map will be used by Moustache template engine
 * @param {*} formatconfig -format config read from formatconfig file
 */
export const fillValues=(variableTovalueMap,formatconfig)=>{
  let input=JSON.stringify(formatconfig);
  // console.log(mustache.render(input, variableTovalueMap).replace(/""/g,"\"").replace(/\\/g,"").replace(/"\[/g,"\[").replace(/\]"/g,"\]").replace(/\]\[/g,"\],\["));
  let output=JSON.parse(mustache.render(input, variableTovalueMap).replace(/""/g,"\"").replace(/\\/g,"").replace(/"\[/g,"\[").replace(/\]"/g,"\]").replace(/\]\[/g,"\],\["));
  return output;
} 


/** 
 * generateQRCodes-function to geneerate qrcodes
 * moduleObject-current module object from request body
 * dataconfig- data config read from dataconfig of module 
*/
const generateQRCodes=async(moduleObject,dataconfig,variableTovalueMap)=>{

    let qrcodeMappings=checkifNullAndSetValue(jp.query(dataconfig, "$.DataConfigs.mappings.*.mappings.*.qrcodeConfig.*"),[],"$.DataConfigs.mappings.*.mappings.*.qrcodeConfig.*");

    for(var i=0, len=qrcodeMappings.length; i < len; i++) 
    {
        let qrmapping=qrcodeMappings[i];
        let varname=qrmapping.variable;
        let qrtext=mustache.render(qrmapping.value, variableTovalueMap);
        
        let qrCodeImage = await QRCode.toDataURL(qrtext);
        variableTovalueMap[varname]=qrCodeImage;
    }
}
export default app;

