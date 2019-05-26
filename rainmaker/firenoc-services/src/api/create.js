import { Router } from "express";
import producer from "../kafka/producer";
import {
  requestInfoToResponseInfo,
  addUUIDAndAuditDetails,
  createWorkFlow
} from "../utils";
import envVariables from "../envVariables";
import mdmsData from "../utils/mdmsData";
const asyncHandler = require("express-async-handler");

export default ({ config, db }) => {
  let api = Router();
  api.post(
    "/_create",
    asyncHandler(async ({ body }, res, next) => {
      let payloads = [];
      let mdms = await mdmsData(body.RequestInfo);
      body = await addUUIDAndAuditDetails(body);
      // await createWorkFlow(body);
      // console.log(body);
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
