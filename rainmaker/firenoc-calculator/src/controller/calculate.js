import { requestInfoToResponseInfo, upadteForAuditDetails } from "../utils";
import { calculateService } from "../services/calculateService";

const calculalte = async (req, res, pool) => {
  console.log("calculalte");
  let calculalteResponse = {};
  calculalteResponse = await calculateService(req, pool);
  res.send(calculalteResponse);
};

export default calculalte;
