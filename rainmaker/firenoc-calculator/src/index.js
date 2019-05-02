import http from "http";
import express from "express";
import cors from "cors";
import morgan from "morgan";
import config from "./config.json";
import swaggerTools from "swagger-tools";
//loading env property
require("dotenv").config();

const serverPort = 8080;

const options = {
  controllers: "./src/controllers",
  useStubs: process.env.NODE_ENV === "development" ? true : false // Conditionally turn on stubs (mock mode)
};

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

let swaggerDoc = require("../config/docs/contract/swagger.json");

swaggerTools.initializeMiddleware(swaggerDoc, middleware => {
  app.use(middleware.swaggerMetadata());

  // Validate Swagger requests
  app.use(middleware.swaggerValidator());

  // Route validated requests to appropriate controller
  app.use(middleware.swaggerRouter(options));

  // Serve the Swagger documents and Swagger UI
  app.use(middleware.swaggerUi());

  app.server.listen(serverPort, () => {
    console.log("port is ", serverPort);
  });
});

export default app;
