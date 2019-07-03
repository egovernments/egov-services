import { Router } from "express";
import producer from "../kafka/producer";
import { requestInfoToResponseInfo, createWorkFlow } from "../utils";
import envVariables from "../envVariables";
import mdmsData from "../utils/mdmsData";
import { addUUIDAndAuditDetails } from "../utils/create";
import { calculate } from "../services/firenocCalculatorService";
import { validateFireNOCModel } from "../utils/modelValidation";
const asyncHandler = require("express-async-handler");

export default ({ config, db }) => {
  let api = Router();
  api.post(
    "/_create",
    asyncHandler(async ({ body }, res, next) => {
      let payloads = [];
      //getting mdms data
      let mdms = await mdmsData(body.RequestInfo, body.FireNOCs[0].tenantId);
      //model validator
      let errors = validateFireNOCModel(body, mdms);
      if (errors.length > 0) {
        next({
          errorType: "custom",
          errorReponse: {
            ResponseInfo: requestInfoToResponseInfo(body.RequestInfo, true),
            Errors: errors
          }
        });
        return;
      }

      // console.log(JSON.stringify(mdms));
      body = await addUUIDAndAuditDetails(body);
      let workflowResponse = await createWorkFlow(body);
      // console.log(JSON.stringify(workflowResponse));

      //need to implement notification
      //calculate call
      let { FireNOCs, RequestInfo } = body;
      for (var i = 0; i < FireNOCs.length; i++) {
        let firenocResponse = await calculate(FireNOCs[i], RequestInfo);
      }

      FireNOCs = updateStatus(FireNOCs, workflowResponse);

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

const updateStatus = (FireNOCs, workflowResponse) => {
  let workflowStatus = {};
  for (let i = 0; i < workflowResponse.ProcessInstances.length; i++) {
    workflowStatus = {
      ...workflowStatus,
      [workflowResponse.ProcessInstances[i].businessId]:
        workflowResponse.ProcessInstances[i].state.state
    };
  }
  FireNOCs = FireNOCs.map(firenoc => {
    firenoc.fireNOCDetails.status =
      workflowStatus[firenoc.fireNOCDetails.applicationNumber];
    return firenoc;
  });
  return FireNOCs;
};
