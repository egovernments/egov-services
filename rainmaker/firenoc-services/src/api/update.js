import { Router } from "express";
import producer from "../kafka/producer";
import envVariables from "../envVariables";
const asyncHandler = require("express-async-handler");
import mdmsData from "../utils/mdmsData";
import { addUUIDAndAuditDetails } from "../utils/create";
import { requestInfoToResponseInfo, createWorkFlow } from "../utils";

export default ({ config, db }) => {
  let api = Router();
  api.post(
    "/_update",
    asyncHandler(async ({ body }, res) => {
      let payloads = [];
      let mdms = await mdmsData(body.RequestInfo);
      body = await addUUIDAndAuditDetails(body);
      let workflowResponse = await createWorkFlow(body);
      payloads.push({
        topic: envVariables.KAFKA_TOPICS_FIRENOC_UPDATE,
        messages: JSON.stringify(body)
      });
      // console.log(JSON.stringify(body));
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
