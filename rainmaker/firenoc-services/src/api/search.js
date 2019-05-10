import { Router } from "express";
import { mergeSearchResults, requestInfoToResponseInfo } from "../utils";

export default ({ config, db }) => {
  let api = Router();
  api.post("/_search", function(request, apiRes) {
    let response = {
      ResponseInfo: requestInfoToResponseInfo(request.body.RequestInfo, true),
      FireNOCs: []
    };
    const actions = {
      INITIATED: "INITIATE",
      APPROVED: "APPROVE"
    };

    const queryObj = JSON.parse(JSON.stringify(request.query));
    queryObj.action = actions[queryObj.action];

    const text = `SELECT FN.uuid as FID,FN.tenantid,FN.fireNOCNumber,FN.dateOfApplied,FD.uuid as firenocdetailsid,FD.applicationnumber,FD.fireNOCType,FD.applicationdate,FD.financialYear,FD.issuedDate,FD.validFrom,FD.validTo,FD.action,FD.channel,FB.uuid as buildingid ,FB.name,FB.noOfFloors,FB.noOfBasements,FO.uuid as ownerid,FO.ownertype,FO.relationship FROM eg_fn_firenoc FN JOIN eg_fn_firenocdetail FD ON (FN.uuid = FD.firenocuuid) JOIN eg_fn_owner FO ON (FD.uuid = FO.firenocdetailsuuid) JOIN eg_fn_buidlings FB ON (FD.uuid = FB.firenocdetailsuuid) where FN.tenantid = '${
      queryObj.tenantId
    }' AND`;

    const queryKeys = Object.keys(queryObj);
    let sqlQuery = text;

    if (queryKeys) {
      queryKeys.forEach(item => {
        if (item != "fromDate" && item != "toDate" && item != "tenantId") {
          sqlQuery = `${sqlQuery} ${item}='${queryObj[item]}' AND`;
        }
      });
    }
    if (
      queryObj.hasOwnProperty("fromDate") &&
      queryObj.hasOwnProperty("toDate")
    ) {
      sqlQuery = `${sqlQuery} FN.createdtime >= ${
        queryObj.fromDate
      } AND FN.lastmodifiedtime <= ${queryObj.toDate} ORDER BY FN.uuid`;
    } else if (
      queryObj.hasOwnProperty("fromDate") &&
      !queryObj.hasOwnProperty("toDate")
    ) {
      sqlQuery = `${sqlQuery} FN.createdtime >= ${
        queryObj.fromDate
      } ORDER BY FN.uuid`;
    } else {
      console.log("Select From Date");
      sqlQuery = `${sqlQuery.substring(
        0,
        sqlQuery.length - 3
      )} ORDER BY FN.uuid`;
    }
    db.query(sqlQuery, (err, res) => {
      if (err) {
        console.log(err.stack);
      } else {
        response.FireNOCs = mergeSearchResults(res.rows, request.query);
        apiRes.json(response);
      }
    });
  });
  return api;
};
