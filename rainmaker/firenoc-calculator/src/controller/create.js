import producer from "../kafka/producer";
import { requestInfoToResponseInfo, upadteForAuditDetails } from "../utils";
import uuid4 from "uuid/v4";
import envVariables from "../envVariables";
import { validateBillingSlabReq } from "../utils/modelValidation";

const create = (req, res, next) => {
  console.log("create");

  let errors = validateBillingSlabReq(req.body);
  if (errors.length > 0) {
    next({
      errorType: "custom",
      errorReponse: {
        ResponseInfo: requestInfoToResponseInfo(req.body.RequestInfo, false),
        Errors: errors
      }
    });
    return;
  }

  let createResponse = {};
  createResponse.ResponseInfo = requestInfoToResponseInfo(
    req.body.RequestInfo,
    true
  );
  createResponse.BillingSlabs = enrichCreateData(req.body);

  const payloads = [
    {
      topic: envVariables.KAFKA_TOPICS_SAVE_SERVICE,
      messages: JSON.stringify(createResponse)
    }
  ];

  producer.send(payloads, function(err, data) {
    if (err) console.log("err", err);
  });
  res.send(createResponse);
};

const enrichCreateData = reqBody => {
  reqBody.BillingSlabs.map(billingSlab => {
    billingSlab.id = uuid4();
    billingSlab.auditDetails = {};
    upadteForAuditDetails(billingSlab.auditDetails, reqBody.RequestInfo);
  });
  return reqBody.BillingSlabs;
};

export default create;
