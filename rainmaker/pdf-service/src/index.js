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
//create binary
pdfMake.vfs = pdfFonts.pdfMake.vfs;
var pdfMakePrinter = require("pdfmake/src/printer");

let localisationMap={};
let app = express();
app.use(express.static(path.join(__dirname, "public")));
app.use(bodyParser.json({limit: '10mb', extended: true}));
app.use(bodyParser.urlencoded({limit: '10mb', extended: true }));

let maxPagesAllowed=envVariables.MAX_NUMBER_PAGES;
 function createPdfBinary(key,listDocDefinition, successCallback, errorCallback) {
   try {
    var fontDescriptors = {
      Roboto: {
        normal: "fonts/Roboto-Regular.ttf",
        bold: "fonts/Roboto-Medium.ttf",
        italics: "fonts/Roboto-Italic.ttf",
        bolditalics: "fonts/Roboto-MediumItalic.ttf"
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
            errorCallback(err.message);
          }).on("close", () => {
              fileStoreAPICall(filename).then((result)=>{
              // fs.unlink(filename,()=>{});
              listOfFilestoreIds.push(result);
              if(listOfFilestoreIds.length===noOfDefinitions)
                successCallback({message:"PDF successfully created and stored",filestoreId:listOfFilestoreIds});
          }).catch(err=>{
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

app.post("/pdf", asyncHandler(async (req, res)=> { 
  try{
   let key=req.query.key;
      
   var formatconfig=JSON.parse(JSON.stringify(require("./config/format-config/"+key)));
   var dataconfig=require("./config/data-config/"+key);  
 
  let formatObjectArrayObject=[];
  let formatConfigByFile=[];
  for(let propertykey in req.body)
  {
    if(req.body.hasOwnProperty(propertykey))
    {
      for(var i=0, len=req.body[propertykey].length; i < len; i++)
      {
        let moduleObject=req.body[propertykey][i];
        let outerObject={};
        let formatObject=JSON.parse(JSON.stringify(formatconfig));

        // Multipage pdf, each pdf from new page
        if((formatObjectArrayObject.length!=0)&&(formatObject["content"][0]!==undefined))
        {
          formatObject["content"][0]["pageBreak"]= "before";
        }
        outerObject[propertykey]=[];
        outerObject[propertykey].push(moduleObject);
        let variableTovalueMap={};
        //direct mapping service
        await Promise.all([
        directMapping(outerObject,formatObject,dataconfig,variableTovalueMap,localisationMap)
      ,
        //external API mapping
        externalAPIMapping(key,outerObject,formatObject,dataconfig,variableTovalueMap,localisationMap)
         ]);
        formatObject=fillValues(variableTovalueMap,formatObject);
        formatObjectArrayObject.push(formatObject["content"]);
        //putting formatconfig in a file to check docdefinition on pdfmake playground online

        if(((i+1)==maxPagesAllowed)||((i+1)==len))
        {
          let formatconfigCopy=JSON.parse(JSON.stringify(formatconfig));
          formatconfigCopy["content"]=formatObjectArrayObject;
          formatConfigByFile.push(formatconfigCopy);
          formatObjectArrayObject=[];
        }
      }
    }
  } 
  


  // var util = require('util');
  // fs.writeFileSync('./data.txt', util.inspect(JSON.stringify(formatconfig)) , 'utf-8');
  //function to download pdf automatically 
  createPdfBinary(
    key,
    formatConfigByFile,
    response => {
      // doc successfully created
      res.json({
        status: 200,
        data: response.message,
        filestoreId:response.filestoreId
      });
    },
    error => {
      res.status(500);
      // doc creation error
      res.json({
        status: 500,
        data: error.message
      });
    }
  );
}
catch(error)
{
  console.log(error);
  res.status(500);
  res.json({
    status: 500,
    data: error.message
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

const PORT = 5000;
app.listen(PORT, () => {
  console.log(`Server running at http:${PORT}/`);
});

export const fillValues=(variableTovalueMap,formatconfig)=>{
  // let stringfromobject=JSON.stringify(formatconfig);
  // for(let key in variableTovalueMap)
  // {
  //   stringfromobject=stringfromobject.replace("\""+key+"\"",JSON.stringify(variableTovalueMap[key]));
  // // }
  // return stringfromobject;
  let mustache = require('mustache');
  mustache.escape = function(text) {return text;};
  let input=JSON.stringify(formatconfig);
  // // mustache.parse(formatconfig);

  // // console.log(mustache.render(input, variableTovalueMap));
  // // let output=JSON.parse(mustache.render(input, variableTovalueMap));
  // // console.log(variableTovalueMap);
  // console.log(variableTovalueMap);
  // console.log(mustache.render(input, variableTovalueMap).replace(/""/g,"\"").replace(/\\/g,"").replace(/"\[/g,"\[").replace(/\]"/g,"\]").replace(/\]\[/g,"\],\["));
  let output=JSON.parse(mustache.render(input, variableTovalueMap).replace(/""/g,"\"").replace(/\\/g,"").replace(/"\[/g,"\[").replace(/\]"/g,"\]").replace(/\]\[/g,"\],\["));
  return output;
} 

export default app;

