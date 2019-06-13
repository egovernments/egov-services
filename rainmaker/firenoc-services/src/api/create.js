import { Router } from "express";
import producer from "../kafka/producer";
import { requestInfoToResponseInfo, createWorkFlow } from "../utils";
import envVariables from "../envVariables";
import mdmsData from "../utils/mdmsData";
import { addUUIDAndAuditDetails } from "../utils/create";
const asyncHandler = require("express-async-handler");
// var ajv = require("ajv")({ removeAdditional: true });
// var swagger=require("../swagger.json");
// var Validator = require('swagger-model-validator');
// var validator = new Validator(swagger);

export default ({ config, db }) => {
  let api = Router();
  api.post(
    "/_create",
    asyncHandler(async ({ body }, res, next) => {
      let payloads = [];
      // var isValid = ajv.validate(
      //   { $ref: "swagger.json#/definitions/FireNOCs" },
      //   body
      // );
      // var validation = swagger.validateModel("FireNOCs", body);
      // console.log("test",JSON.stringify(validation));
      let mdms = await mdmsData(body.RequestInfo);
      body = await addUUIDAndAuditDetails(body);
      let workflowResponse = await createWorkFlow(body);
      //need to implement notification
      payloads.push({
        topic: envVariables.KAFKA_TOPICS_FIRENOC_CREATE,
        messages: JSON.stringify(body)
      });
      producer.send(payloads, function(err, data) {
        let response = {
          ResponseInfo: requestInfoToResponseInfo(body.RequestInfo, true),
          FireNOCs: body.FireNOCs
        };
        res.json(response);
      });
    })
  );
  return api;
};
