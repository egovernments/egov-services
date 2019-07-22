"use strict";
import http from "http";
import request from "request";
import express from "express";
import path from "path";
import fs, { exists } from "fs";
import cors from "cors";
import morgan from "morgan";
import bodyParser from "body-parser";
import initializeDb from "./db";
import middleware from "./middleware";
import api from "./api";
import config from "./config.json";
import { PROPERTY } from "./endpoint";
import { httpRequest } from "./api/api";
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

//create binary
pdfMake.vfs = pdfFonts.pdfMake.vfs;
var pdfMakePrinter = require("pdfmake/src/printer");

let app = express();
app.use(express.static(path.join(__dirname, "public")));
app.use(bodyParser.json({limit: '10mb', extended: true}));
app.use(bodyParser.urlencoded({limit: '10mb', extended: true }));

var key;
var fileNameAppend="randomNumber";
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
    
    const printer =  new pdfMakePrinter(fontDescriptors);
    const doc = printer.createPdfKitDocument(docDefinition);
    //reference link
    //https://medium.com/@kainikhil/nodejs-how-to-generate-and-properly-serve-pdf-6835737d118e#d8e5

    //storing file on local computer/server
    doc.pipe(
       fs.createWriteStream("src/pdfs/"+key+" "+fileNameAppend+".pdf").on("error", err => {
        errorCallback(err.message);
      }).on("close", () => {
          fileStoreAPICall(key,fileNameAppend,function(result) {
          successCallback({message:"PDF successfully created and stored",filestoreId:result});
       });
      })
    );
  //   doc.on("end", () => {
  //     //filestore API call to store file on S3    
  //     fileStoreAPICall(key,fileNameAppend,function(result) {
  //     successCallback({message:"PDF successfully created and stored",filestoreId:result});
  //  });
      
  //   });
    doc.end();
  } catch (err) {
    throw err;
  }
}

app.post("/pdf", asyncHandler(async (req, res)=> { 
   key=req.query.key;
      
   var formatconfig=require("./config/format-config/"+key);
   var dataconfig=require("./config/data-config/"+key);  
   
   if(key=="tl-receipt"){          
          fileNameAppend=req.body.Licenses[0].applicationNumber;          
    }
      else if(key=="firenoc-receipt"){        
          fileNameAppend=req.body.FireNOCs[0].fireNOCDetails.applicationNumber;        
    }
      else if(key=="pt-receipt"){        
        fileNameAppend=req.body.Properties[0].propertyId+":"+req.body.Properties[0].propertyDetails[0].assessmentNumber;        
    }
    else{
      
    }

  let formatObjectArrayObject={};
  formatObjectArrayObject["content"]=[];

  for(let propertykey in req.body)
  {
    
    if(req.body.hasOwnProperty(propertykey))
    {
      for(var i=0, len=req.body[propertykey].length; i < len; i++)
      {
        let moduleObject=req.body[propertykey][i];
        let outerObject={};
        let formatObject=JSON.parse(JSON.stringify(formatconfig));
        if((i!=0)&&(formatObject["content"][0]!==undefined))
        {
          formatObject["content"][0]["pageBreak"]= "before";
        }
        outerObject[propertykey]=[];
        outerObject[propertykey].push(moduleObject);
        let variableTovalueMap={};
        //direct mapping service
        directMapping(outerObject,formatObject,dataconfig,variableTovalueMap);  
      
        //external API mapping
        await externalAPIMapping(key,outerObject,formatObject,dataconfig,variableTovalueMap);
        formatObject=fillValues(variableTovalueMap,formatObject);
        formatObjectArrayObject["content"].push(formatObject["content"]);
        //putting formatconfig in a file to check docdefinition on pdfmake playground online
      }
    }
  } 




  var util = require('util');
  fs.writeFileSync('./data.txt', util.inspect(JSON.stringify(formatObjectArrayObject)) , 'utf-8');
  //function to download pdf automatically 
  createPdfBinary(
    JSON.parse(JSON.stringify(formatObjectArrayObject)),
    response => {
      // doc successfully created
      res.json({
        status: 200,
        data: response.message,
        filestoreId:response.filestoreId
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

