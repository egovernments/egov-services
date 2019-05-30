import { generateDemandSearchURL } from "../utils";
import axios from "../config/httpClient";
import { httpRequest } from "../utils/api";

export const generateDemand = async (
  requestInfo,
  tenantId,
  calculations,
  config
) => {
  let consumercodeList = calculations.map(calculation => {
    return calculation.applicationNumber;
  });

  let createcalculations = [];
  let updatecalculations = [];
  let demandsSearch = await searchDemand(
    requestInfo,
    tenantId,
    consumercodeList
  );
  let foundConsumerCode = demandsSearch.Demands.map(demand => {
    return demand.consumerCode;
  });
  calculations.map(calculation => {
    if (foundConsumerCode.includes(calculation.applicationNumber))
      updatecalculations.push(calculation);
    else createcalculations.push(calculation);
  });
  if (createcalculations > 0) createDemand(requestInfo, calculations, config);
  return "uri";
};

const createDemand = async (requestInfo, calculations, config) => {
  let demands = [];
  calculations.map(calculation => {
    let tenantId = calculation.tenantId;
    let consumerCode = calculation.applicationNumber;
    let fireNOC = calculation.fireNOC;
    let demand = {
      tenantId,
      consumerCode,
      consumerType: "FIRENOC",
      businessService: process.env.BUSINESSSERVICE,
      demandDetails: []
    };
    calculation.taxHeadEstimates.map(taxHeadEstimate => {
      let demandDetail = {
        taxHeadMasterCode: taxHeadEstimate.taxHeadCode,
        taxAmount: taxHeadEstimate.estimateAmount,
        collectionAmount: 0,
        tenantId
      };
      demand.demandDetails.push(demandDetail);
    });
    demands.push(demand);
  });

  var uri = ``;
  var demandsSearch = await httpRequest({
    hostURL: process.env.EGOV_BILLINGSERVICE_HOST,
    endPoint: uri,
    requestBody
  });
};

const searchDemand = async (requestInfo, tenantId, consumercodeList) => {
  let uri = generateDemandSearchURL();
  uri = uri.replace("{1}", tenantId);
  uri = uri.replace("{2", process.env.BUSINESSSERVICE);
  uri = uri.replace("{3}", consumercodeList.join());
  // console.log(uri);
  // var mdmsResponse = await httpRequest({
  //   hostURL: "https://swapi.co",
  //   endPoint: "/api/starships/9"
  // });
  let requestBody = { RequestInfo: requestInfo };
  var demandsSearch = await httpRequest({
    hostURL: process.env.EGOV_BILLINGSERVICE_HOST,
    endPoint: uri,
    requestBody
  });

  // console.log("error from api utils:", errorReponse);
  console.log(demandsSearch);
  return demandsSearch;
};
