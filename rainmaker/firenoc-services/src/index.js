//loading env property
require("dotenv").config();
import http from "http";
import express from "express";
import cors from "cors";
import morgan from "morgan";
import bodyParser from "body-parser";
// import util from "util";
import initializeDb from "./db";
import middleware from "./middleware";
import api from "./api";
import config from "./config.json";
import tracer from "./middleware/tracer"
var swaggerUi = require("swagger-ui-express"),
  swaggerDocument = require("./swagger.json");
// const validator = require('swagger-express-validator');

// const opts = {
//   schema:swaggerDocument, // Swagger schema
//   preserveResponseContentType: false, // Do not override responses for validation errors to always be JSON, default is true
//   returnRequestErrors: true, // Include list of request validation errors with response, default is false
//   returnResponseErrors: true, // Include list of response validation errors with response, default is false
//   validateRequest: true,
//   // validateResponse: true,
//   requestValidationFn: (req, data, errors) => {
//     console.log(`failed request validation: ${req.method} ${req.originalUrl}\n ${util.inspect(errors)}`)
//   },
//   // responseValidationFn: (req, data, errors) => {
//   //   console.log(`failed response validation: ${req.method} ${req.originalUrl}\n ${util.inspect(errors)}`)
//   // },
// };

let app = express();
app.server = http.createServer(app);

// logger
app.use(morgan("dev"));

// 3rd party middleware
app.use(
  cors({
    exposedHeaders: config.corsHeaders
  })
);

app.use(
  bodyParser.json({
    limit: config.bodyLimit
  })
);

app.use(tracer())


app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerDocument));

// connect to db
initializeDb(db => {
  // internal middleware
  app.use(middleware({ config, db }));

  // app.use(validator(opts));

  // api router
  app.use("/", api({ config, db }));

  app.server.listen(process.env.PORT || config.port, () => {
    console.log(`Started on port ${app.server.address().port}`);
  });
});

export default app;
