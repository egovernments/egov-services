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
import { strict } from "assert";

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
        //getting data from json
        // for (var attributename in data) {
        //   console.log(attributename, ": ", data[attributename]);
        // }
        //printing array of objects
      
        // var arr = [];
        // var len;
        // for (var attributename in data) {
        //   var obj = {
        //     key: ""
        //   };
        //   obj.key = attributename;
        //   obj.value = data[attributename];

        //   if (attributename == "propertyDetails") {
        //     arr.push(data[attributename].units);
        //     len = data[attributename].units.length;
        //   }
        // }

        // for (var i = 0; i < len; i++) {
        //   console.log(arr[0][i]);
        // }

        var directArr=[];
        var obj={
        	key:"",
        	value:""
        };

         var ob="";
         var i=0;
         var key=[];
         var value=[];
          key.push(get(dataconfig,'DataConfigs.mappings[0].mappings[0].direct',''));
          // value.push(get(dataconfig,'DataConfigs,mappings[0].mappings[0].direct',''));
          //directArr.push(key);
         
         
         
        
        console.log(key);
        // for(var i=0;i<directArr.length;i++)
        // {	console.log(directArr[i]);	}
        // for(var att in dataconfig)
        // {
        // 		get()
        // }

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
  var reqbody =
   {
    text:req.body
   };

  //function to download pdf automatically
  createPdfBinary(
    receipt_data,(response) => {
      // doc successfully created
      // res.send(reqbody.text);
      //console.log(reqbody.text);
      res.json({
        status: 200,
        data: reqbody.text
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

  /*function to sent binary data to s3
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
  console.log(`Server running at http://localhost:${PORT}/`);
});

/*

app.server = http.createServer(app);

// logger
app.use(morgan('dev'));

// 3rd party middleware
app.use(cors({
	exposedHeaders: config.corsHeaders
}));

app.use(bodyParser.json({
	limit : config.bodyLimit
}));

// connect to db
initializeDb( db => {

	// internal middleware
	app.use(middleware({ config, db }));

	// api router
	app.use('/api', api({ config, db }));

	app.server.listen(process.env.PORT || config.port, () => {
		console.log(`Started on port ${app.server.address().port}`);
	});
});
*/
export default app;
