import producer from "../kafka/producer";
import { requestInfoToResponseInfo, upadteForAuditDetails } from "../utils";
import envVariables from "../envVariables";
import { validateBillingSlabReq } from "../utils/modelValidation";

const update = (req, res, next) => {
  console.log("update");
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

  let updateResponse = {};
  updateResponse.ResponseInfo = requestInfoToResponseInfo(
    req.body.RequestInfo,
    true
  );
  updateResponse.BillingSlabs = enrichUpdateData(req.body);

  const payloads = [
    {
      topic: envVariables.KAFKA_TOPICS_UPDATE_SERVICE,
      messages: JSON.stringify(updateResponse)
    }
  ];
  producer.send(payloads, function(err, data) {});
  res.send(updateResponse);
};

const enrichUpdateData = reqBody => {
  reqBody.BillingSlabs.map(billingSlab => {
    upadteForAuditDetails(billingSlab.auditDetails, reqBody.RequestInfo, true);
  });
  return reqBody.BillingSlabs;
};

export default update;
