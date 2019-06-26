import { Router } from "express";
import producer from "../kafka/producer";
import envVariables from "../envVariables";
const asyncHandler = require("express-async-handler");
import mdmsData from "../utils/mdmsData";
import { addUUIDAndAuditDetails } from "../utils/create";
import { getApprovedList } from "../utils/update";
import { requestInfoToResponseInfo, createWorkFlow } from "../utils";
import { calculate } from "../services/firenocCalculatorService";
// import cloneDeep from "lodash/cloneDeep";
import filter from "lodash/filter";
import {validateFireNOCModel} from "../utils/modelValidation";


export default ({ config, db }) => {
  let api = Router();
  api.post(
    "/_update",
    asyncHandler(async ({ body }, res,next) => {
      let payloads = [];
      let mdms = await mdmsData(body.RequestInfo,body.FireNOCs[0].tenantId);
      //model validator
      let errors=validateFireNOCModel(body,mdms);
      if (errors.length>0) {
        next({errorType:"custom",errorReponse:{
          ResponseInfo: requestInfoToResponseInfo(body.RequestInfo, true),
          Errors: errors
        }})
        return;
      }

      body = await addUUIDAndAuditDetails(body);

      //Check records for approved
      // let approvedList=await getApprovedList(cloneDeep(body));

      //applay workflow
      let workflowResponse = await createWorkFlow(body);

      //calculate call
      let { FireNOCs, RequestInfo } = body;
      for (var i = 0; i < FireNOCs.length; i++) {
        let firenocResponse = await calculate(FireNOCs[i], RequestInfo);
      }



      payloads.push({
        topic: envVariables.KAFKA_TOPICS_FIRENOC_UPDATE,
        messages: JSON.stringify(body)
      });

      //check approved list
      const approvedList=filter(body.FireNOCs,function(fireNoc) { return fireNoc.fireNOCNumber; });

      // console.log("list length",approvedList.length);
      if (approvedList.length>0) {
        payloads.push({
          topic: envVariables.KAFKA_TOPICS_FIRENOC_WORKFLOW,
          messages: JSON.stringify({RequestInfo,FireNOCs:approvedList})
        });
      }
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
