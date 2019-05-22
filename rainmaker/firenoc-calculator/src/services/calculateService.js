import config from "../config/config";
import { requestInfoToResponseInfo } from "../utils";
import { searchService } from "../controller/search";

export const calculateService = async (req, pool) => {
  let calculalteResponse = {};
  calculalteResponse.ResponseInfo = requestInfoToResponseInfo(
    req.body.RequestInfo,
    true
  );
  calculalteResponse.Calculation = await getCalculation(req, pool, config);
  return calculalteResponse;
};

const getCalculation = async (req, pool, config) => {
  let calculations = [];
  for (let i = 0; i < req.body.CalulationCriteria.length; i++) {
    //TODO: if calculation criteria doesnt content fireNOC obj

    let calculation = await calculateForSingleReq(
      req.body.CalulationCriteria[i],
      config,
      pool
    );

    console.log("calculation", calculation);
    calculations.push(calculation);
  }

  return calculations;
};

const calculateForSingleReq = async (calculateCriteria, config, pool) => {
  let searchReqParam = {};
  searchReqParam.tenantId = calculateCriteria.fireNOC.tenantId;
  searchReqParam.fireNOCType =
    calculateCriteria.fireNOC.fireNOCDetails.fireNOCType;
  searchReqParam.calculationType = config.CALCULATON_TYPE;

  let nocfee = 0;

  for (
    let i = 0;
    i < calculateCriteria.fireNOC.fireNOCDetails.buildings.length;
    i++
  ) {
    let buidingnocfee = 0;
    searchReqParam.buildingUsageType =
      calculateCriteria.fireNOC.fireNOCDetails.buildings[i].usageType;
    let uoms = calculateCriteria.fireNOC.fireNOCDetails.buildings[
      i
    ].uoms.filter(uom => {
      return uom.isActiveUom;
    });
    for (let uomindex = 0; uomindex < uoms.length; uomindex++) {
      searchReqParam.uom = uoms[uomindex].code;
      if (config.CALCULATON_TYPE !== "FLAT")
        searchReqParam.uomValue = uoms[uomindex].value;
      const billingslabs = await searchService(searchReqParam, {}, pool);
      if (billingslabs.length > 1) {
        console.log("More than 1 billingslabs");
        throw new Error("More than 1 billingslabs");
      }
      if (billingslabs.length < 1) {
        console.log("No Billing slabs found");
        throw new Error("No billing slab found");
      }
      console.log("billingslabs", billingslabs);
      if (config.CALCULATON_TYPE === "FLAT") {
        buidingnocfee += billingslabs[0].rate;
      } else {
        buidingnocfee += billingslabs[0].rate * searchReqParam.value;
      }
    }
    if (config.CALCULATON_TYPE !== "FLAT") {
      const minimumFee =
        searchReqParam.fireNOCType === "NEW"
          ? config.MIN_NEW
          : config.MIN_PROVISIONAL;
      buidingnocfee = max(buidingnocfee, minimumFee);
    }
    nocfee += buidingnocfee;
  }
  const feeEstimate = {
    category: "TAX",
    taxHeadCode: "FIRENOC_FEES",
    estimateAmount: nocfee
  };
  let calculation = {
    applicationNumber: null,
    fireNoc: null,
    tenantId: searchReqParam.tenantId,
    taxHeadEstimates: []
  };
  calculation.taxHeadEstimates.push(feeEstimate);
  return calculation;
};
