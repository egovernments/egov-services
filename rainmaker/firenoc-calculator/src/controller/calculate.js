import { requestInfoToResponseInfo, upadteForAuditDetails } from "../utils";
import { calculateService } from "../services/calculateService";
import { validateCalculationReq } from "../utils/modelValidation";

const calculalte = async (req, res, pool, next) => {
  console.log("calculalte");
  let errors = validateCalculationReq(req.body);
  console.log(errors);
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

  let calculalteResponse = {};
  calculalteResponse = await calculateService(req, pool);
  res.send(calculalteResponse);
};

export default calculalte;
