import { Router } from "express";
import producer from "../kafka/producer";
import { requestInfoToResponseInfo, createWorkFlow} from "../utils";
import {validateFireNOCModel} from "../model/fireNOC";
import envVariables from "../envVariables";
import mdmsData from "../utils/mdmsData";
import { addUUIDAndAuditDetails } from "../utils/create";
import { calculate } from "../services/firenocCalculatorService";
const asyncHandler = require("express-async-handler");


export default ({ config, db }) => {
  let api = Router();
  api.post(
    "/_create",
    asyncHandler(async ({ body }, res, next) => {
      let payloads = [];
      //model validator
      // let errors=validateFireNOCModel(body.FireNOCs);
      let mdms = await mdmsData(body.RequestInfo);
      body = await addUUIDAndAuditDetails(body);
      let workflowResponse = await createWorkFlow(body);

      //need to implement notification
      //calculate call
      let { FireNOCs, RequestInfo } = body;
      for (var i = 0; i < FireNOCs.length; i++) {
        let firenocResponse = await calculate(FireNOCs[i], RequestInfo);
      }
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
