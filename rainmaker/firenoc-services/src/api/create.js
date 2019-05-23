import { Router } from "express";
import producer from "../kafka/producer";
import { requestInfoToResponseInfo ,addUUIDAndAuditDetails} from "../utils";
import {KAFKA_TOPICS_FIRENOC_CREATE} from '../envVariables'
const asyncHandler = require('express-async-handler');
// var Validator = require('swagger-model-validator');
// var swaggerDocument = new Validator("../swagger.json");


export default ({ config, db }) => {
  let api = Router();
  api.post("/_create", asyncHandler(async ({ body }, res,next)=> {
    let payloads=[];
    body=await addUUIDAndAuditDetails(body);
    console.log(body);
    payloads.push({
      topic: KAFKA_TOPICS_FIRENOC_CREATE,
      messages:JSON.stringify(body)
    })
    // console.log("before",payloads);
    // console.log(body);
    producer.send(payloads, function(err, data) {
      let response = {
        ResponseInfo: requestInfoToResponseInfo(body.RequestInfo, true),
        FireNOCs: body.FireNOCs
      };
      res.json(response);
    });
  }));
  return api;
};
