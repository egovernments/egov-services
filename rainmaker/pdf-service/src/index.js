"use strict";
import http from "http";
import request from "request";
import express from "express";
import logger from "./config/logger";
import path from "path";
import fs, { exists } from "fs";
import axios from 'axios';
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
import {getFileStoreIds,insertStoreIds} from "./queries";
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

let dataConfigUrls=envVariables.DATA_CONFIG_URLS;
let formatConfigUrls=envVariables.FORMAT_CONFIG_URLS;

let dataConfigMap={};
let formatConfigMap={};



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
 function createPdfBinary(key,listDocDefinition, successCallback, errorCallback,tenantId,starttime) {
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
    if(noOfDefinitions==0)
    {
      logger.error("no file generated for pdf");
      errorCallback({message:" error: no file generated for pdf"});
    }
    else
    {
      listDocDefinition.forEach(docDefinition =>
        {
            const doc = printer.createPdfKitDocument(docDefinition);
            let fileNameAppend="-"+new Date().getTime();
            // let filename="src/pdfs/"+key+" "+fileNameAppend+".pdf"
            let filename=key+""+fileNameAppend+".pdf"
            //reference link
            //https://medium.com/@kainikhil/nodejs-how-to-generate-and-properly-serve-pdf-6835737d118e#d8e5
        
            //storing file on local computer/server

            var chunks=[];
            doc.on('data', function (chunk) {
              chunks.push(chunk);
              });
              doc.on('end', function () {
                // console.log("enddddd "+cr++);
              var data = Buffer.concat(chunks);
              fileStoreAPICall(filename,tenantId,data).then((result)=>{
                listOfFilestoreIds.push(result);
                if(listOfFilestoreIds.length===noOfDefinitions)
                {
                  // insertStoreIds("",);
                  var jobid=`${key}${fileNameAppend}`;

                  logger.info("PDF uploaded to filestore");
                  insertStoreIds(jobid,listOfFilestoreIds,tenantId,starttime,successCallback,errorCallback);
                }
                }).catch(err=>{
                    logger.error(err.stack);
                    errorCallback({message:"error occurred while uploading pdf: "+((typeof err)==='string')?err:err.message});
                  });
              });
            // doc.pipe(
            //   fs.createWriteStream(filename).on("error", err => {
            //     errorCallback({message:"error occurred while writing pdf: "+((typeof err)==='string')?err:err.message});
            //   }).on("close", () => {
            //       fileStoreAPICall(filename,tenantId).then((result)=>{
            //       fs.unlink(filename,()=>{});
            //       listOfFilestoreIds.push(result);
            //       if(listOfFilestoreIds.length===noOfDefinitions)
            //       {
            //         // insertStoreIds("",);
            //         logger.info(`PDF successfully created and stored filestoreIds: ${listOfFilestoreIds}`);
            //         successCallback({message:"PDF successfully created and stored",filestoreId:listOfFilestoreIds});
            //       }
            //   }).catch(err=>{
            //     fs.unlink(filename,()=>{});
            //     logger.error(err.stack);
            //     errorCallback({message:"error occurred while uploading pdf: "+((typeof err)==='string')?err:err.message});
            //   });
            // }
            // ));
            doc.end();
        });
    }
  } catch (err) {
    logger.error(err.stack);
    errorCallback({message:" error occured while creating pdf: "+((typeof err)==='string')?err:err.message});
  }
}

app.post("/pdf/v1/_create", asyncHandler(async (req, res)=> { 
  let requestInfo;
  try{
   var starttime=new Date().getTime();
   let key=req.query.key;
   let tenantId=req.query.tenantId;
   requestInfo=get(req.body,"RequestInfo");
   let errorMessage="";
   if((key==undefined)||(key.trim()==="")){
    errorMessage+=" key is missing,";
   }
   if((tenantId==undefined)||(tenantId.trim()==="")){
    errorMessage+=" tenantId is missing,";
   }
   if(requestInfo==undefined){
    errorMessage+=" requestInfo is missing,";
   }
   if((formatConfigMap[key]==undefined)||(dataConfigMap[key]==undefined)){
    errorMessage+=` no config found for key ${key}`;
   }
   if(errorMessage!==""){
    res.status(400);
    res.json({
      message: errorMessage,
      ResponseInfo:requestInfo
    });
   }
   else
   {

    var formatconfig = formatConfigMap[key];
    var dataconfig = dataConfigMap[key];  
    
    var baseKeyPath = get(dataconfig,"DataConfigs.baseKeyPath");
    if(baseKeyPath == null)
    {
      logger.error("baseKeyPath is absent in config");
      throw {message:`baseKeyPath is absent in config`};
    }
    let isCommonTableBorderRequired=get(dataconfig,"DataConfigs.isCommonTableBorderRequired");
    let formatObjectArrayObject=[];
    let formatConfigByFile=[];
    let countOfObjectsInCurrentFile=0;
    let moduleObjectsArray=checkifNullAndSetValue(jp.query(req.body,baseKeyPath),[],baseKeyPath);
      if(Array.isArray(moduleObjectsArray) && (moduleObjectsArray.length>0))
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
          
          await generateQRCodes(moduleObject,dataconfig,variableTovalueMap);
          handleDerivedMapping(dataconfig,variableTovalueMap);
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
        logger.error(`could not find property of type array in request body with name ${baseKeyPath}`);
        throw {message:`could not find property of type array in request body with name ${baseKeyPath}`}; 
      }
    
    
    logger.info(`Applied templating engine on ${moduleObjectsArray.length} objects output will be in ${formatConfigByFile.length} files`);
  
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
          ResponseInfo:requestInfo,
          message: response.message,
          filestoreIds:response.filestoreIds,
          jobid:response.jobid,
          starttime:response.starttime,
          endtime:response.endtime,
          tenantid:response.tenantid
        });
      },
      error => {
        res.status(500);
        // doc creation error
        res.json({
          ResponseInfo:requestInfo,
          message: "error in createPdfBinary "+error.message
        });
      },tenantId,starttime
    );
   }
}
catch(error)
{
  logger.error(error.stack);
  res.status(500);
  res.json({
    ResponseInfo:requestInfo,
    message: "some unknown error while creating: "+error.message
  });
}
  
}));


app.post("/pdf/v1/_search", asyncHandler(async (req, res)=> { 
  let requestInfo;
  try{

    let tenantid=req.query.tenantid;
    let jobid=req.query.jobid;
    requestInfo=get(req.body,"RequestInfo");
    if((jobid==undefined)||(jobid.trim()=="")){
      res.status(400);
      res.json({
        ResponseInfo:requestInfo,
        message: "job id is missing"
      });
    }
    else
    {
      if(jobid.includes(","))
      {
        jobid=jobid.split(',');
      }
      else
      {
        jobid=[jobid];
      }
        getFileStoreIds(jobid,tenantid,responseBody => {
        // doc successfully created
        res.status(responseBody.status);
        delete responseBody.status;
        res.json({ResponseInfo:requestInfo,...responseBody});
      });
    }
  }
  catch(error)
  {
    logger.error(error.stack);
    res.status(500);
    res.json({
      ResponseInfo:requestInfo,
      message: "some unknown error while searching: "+error.message
    });
  }
}));


dataConfigUrls && dataConfigUrls.split(",").map(
  item =>{
    item=item.trim();
    if(item.includes("file://"))
    {
      item=item.replace("file://","");
      fs.readFile(item,'utf8', function (err, data) {
        if (err) {
          logger.error(err.stack);
        }
        else
        {
          data=JSON.parse(data);
          dataConfigMap[data.key]=data;
          logger.info("loaded dataconfig: file:///"+item);
        }
      });
    }
    else
    {
      axios.get(item)
      .then((response) => {
          dataConfigMap[response.data.key]=response.data;
          logger.info("loaded dataconfig: "+item);
        
      })
      .catch((error) => {
        logger.error(error.stack);
      });
    }

  }
);

formatConfigUrls && formatConfigUrls.split(",").map(
  item =>{
    item=item.trim();
    if(item.includes("file://"))
    {
      item=item.replace("file://","");
      fs.readFile(item,'utf8', function (err, data) {
        if (err) {
          logger.error(err.stack);
        }
        else
        {
          data=JSON.parse(data);
          formatConfigMap[data.key]=data.config;
          logger.info("loaded formatconfig: file:///"+item);
        }
      });
    }
    else
    {
      axios.get(item)
      .then((response) => {
         formatConfigMap[response.data.key]=response.data.config;
         logger.info("loaded formatconfig: "+item);
      })
      .catch((error) => {
        logger.error(error.stack);
      });
    }

  }
);

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
  // console.log(mustache.render(input, variableTovalueMap).replace(/""/g,"\"").replace(/\\/g,"").replace(/"\[/g,"\[").replace(/\]"/g,"\]").replace(/\]\[/g,"\],\[").replace(/"\{/g,"\{").replace(/\}"/g,"\}"));
  let output=JSON.parse(mustache.render(input, variableTovalueMap).replace(/""/g,"\"").replace(/\\/g,"").replace(/"\[/g,"\[").replace(/\]"/g,"\]").replace(/\]\[/g,"\],\[").replace(/"\{/g,"\{").replace(/\}"/g,"\}"));
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

const handleDerivedMapping=(dataconfig,variableTovalueMap)=>
{
  let derivedMappings=checkifNullAndSetValue(jp.query(dataconfig, "$.DataConfigs.mappings.*.mappings.*.derived.*"),[],"$.DataConfigs.mappings.*.mappings.*.derived.*");

  for(var i=0, len=derivedMappings.length; i < len; i++) 
  {
      let mapping=derivedMappings[i];
      let expression=mustache.render(mapping.formula.replace(/-/g," - ").replace(/\+/g," + "), variableTovalueMap).replace(/NA/g,"0");
      variableTovalueMap[mapping.variable]=eval(expression);     
  }
}


export default app;

