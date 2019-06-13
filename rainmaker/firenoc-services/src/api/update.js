import { Router } from "express";
import producer from "../kafka/producer";
import envVariables from "../envVariables";
const asyncHandler = require("express-async-handler");
import mdmsData from "../utils/mdmsData";
import { addUUIDAndAuditDetails } from "../utils/create";
import { requestInfoToResponseInfo, createWorkFlow } from "../utils";
import { calculate } from "../services/firenocCalculatorService";

export default ({ config, db }) => {
  let api = Router();
  api.post(
    "/_update",
    asyncHandler(async ({ body }, res) => {
      let payloads = [];
      let mdms = await mdmsData(body.RequestInfo);
      body = await addUUIDAndAuditDetails(body);
      let workflowResponse = await createWorkFlow(body);
      //calculate call
      // let { FireNOCs, RequestInfo } = body;
      // for (var i = 0; i < FireNOCs.length; i++) {
      //   let firenocResponse = await calculate(FireNOCs[i], RequestInfo);
      // }

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
