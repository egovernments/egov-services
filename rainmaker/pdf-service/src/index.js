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
import { fileStoreOnComputer } from "./utils/fileStoreOnComputer";
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
      })
    );
    doc.on("end", () => {
      successCallback("PDF successfully created and stored");
    });
    doc.end();
    //filestore API call to store file on S3    
    var fun=fileStoreAPICall(key,fileNameAppend);    
    
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


  //direct mapping service
  formatconfig=directMapping(req,formatconfig,dataconfig);  

  //external API mapping
  formatconfig=await externalAPIMapping(key,req,formatconfig,dataconfig);
        
  //putting formatconfig in a file to check docdefinition on pdfmake playground online
  var util = require('util');
  fs.writeFileSync('./data.txt', util.inspect(JSON.stringify(formatconfig)) , 'utf-8');
  
  //function to download pdf automatically 
  createPdfBinary(
    JSON.parse(JSON.stringify(formatconfig)),
    response => {
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

export default app;
