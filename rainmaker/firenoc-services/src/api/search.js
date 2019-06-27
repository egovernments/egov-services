import { Router } from "express";
import { requestInfoToResponseInfo } from "../utils";
import { mergeSearchResults, searchByMobileNumber } from "../utils/search";
import isEmpty from "lodash/isEmpty";
import get from "lodash/get";
import some from "lodash/some";
import { actions } from "../utils/search";
import { validateFireNOCSearchModel } from "../utils/modelValidation";
import envVariables from "../envVariables";
const asyncHandler = require("express-async-handler");

export default ({ config, db }) => {
  let api = Router();
  api.post(
    "/_search",
    asyncHandler(async (request, res, next) => {
      let response = {
        ResponseInfo: requestInfoToResponseInfo(request.body.RequestInfo, true),
        FireNOCs: []
      };

      const queryObj = JSON.parse(JSON.stringify(request.query));
      // console.log(queryObj);
      let errors = validateFireNOCSearchModel(queryObj);
      if (errors.length > 0) {
        next({
          errorType: "custom",
          errorReponse: {
            ResponseInfo: requestInfoToResponseInfo(
              request.body.RequestInfo,
              true
            ),
            Errors: errors
          }
        });
        return;
      }
      // console.log(queryObj);


      let text =
        "SELECT FN.uuid as FID,FN.tenantid,FN.fireNOCNumber,FN.provisionfirenocnumber,FN.oldfirenocnumber,FN.dateofapplied,FN.createdBy,FN.createdTime,FN.lastModifiedBy,FN.lastModifiedTime,FD.uuid as firenocdetailsid,FD.action,FD.applicationnumber,FD.fireNOCType,FD.applicationdate,FD.financialYear,FD.firestationid,FD.issuedDate,FD.validFrom,FD.validTo,FD.action,FD.channel,FD.propertyid,FD.noofbuildings,FD.additionaldetail,FBA.uuid as puuid,FBA.doorno as pdoorno,FBA.latitude as platitude,FBA.longitude as plongitude,FBA.buildingName as pbuildingname,FBA.addressnumber as paddressnumber,FBA.pincode as ppincode,FBA.locality as plocality,FBA.city as pcity,FBA.street as pstreet,FB.uuid as buildingid ,FB.name as buildingname,FB.usagetype,FO.uuid as ownerid,FO.ownertype,FO.useruuid,FO.relationship,FUOM.uuid as uomuuid,FUOM.code,FUOM.value,FUOM.activeuom,FBD.uuid as documentuuid,FUOM.active,FBD.documentType,FBD.filestoreid,FBD.documentuid,FBD.createdby as documentCreatedBy,FBD.lastmodifiedby as documentLastModifiedBy,FBD.createdtime as documentCreatedTime,FBD.lastmodifiedtime as documentLastModifiedTime FROM eg_fn_firenoc FN JOIN eg_fn_firenocdetail FD ON (FN.uuid = FD.firenocuuid) JOIN eg_fn_address FBA ON (FD.uuid = FBA.firenocdetailsuuid) JOIN eg_fn_owner FO ON (FD.uuid = FO.firenocdetailsuuid) JOIN eg_fn_buidlings FB ON (FD.uuid = FB.firenocdetailsuuid) JOIN eg_fn_buildinguoms FUOM ON (FB.uuid = FUOM.buildinguuid) LEFT OUTER JOIN eg_fn_buildingdocuments FBD on(FB.uuid = FBD.buildinguuid)";
      // FBD.active=true AND FO.active=true AND FUOM.active=true AND";
      if (!isEmpty(queryObj)) {
        text=text+" where "
      }

      if (queryObj.status) {
        queryObj.action = actions[queryObj.status];
      }

      //if citizen
      const roles = get(request.body, "RequestInfo.userInfo.roles");
      const userUUID = get(request.body, "RequestInfo.userInfo.uuid");
      if (some(roles, { code: "CITIZEN" }) && userUUID) {
        const mobileNumber = get(request.body, "RequestInfo.userInfo.mobileNumber");
        const tenantId=get(request.body, "RequestInfo.userInfo.tenantId");
        console.log(mobileNumber);
        console.log(tenantId);
        text = `${text} where FN.createdby = '${userUUID}' OR`;
        queryObj.mobileNumber=queryObj.mobileNumber?queryObj.mobileNumber:mobileNumber;
        queryObj.tenantId=queryObj.tenantId?queryObj.tenantId:tenantId;
      } else {
        if (queryObj.tenantId) {
          text = `${text} FN.tenantid = '${queryObj.tenantId}' AND`;
        }
      }

      const queryKeys = Object.keys(queryObj);
      let sqlQuery = text;

      if (queryObj.hasOwnProperty("ids")) {
        // console.log(queryObj.ids.split(","));
        let ids=queryObj.ids.split(",");
        for (var i = 0; i < ids.length; i++) {
          sqlQuery = `${sqlQuery} FN.uuid = '${ids[i]}' OR`;
        }
      }

      if (queryObj.hasOwnProperty("mobileNumber")) {
        // console.log("mobile number");
        let userSearchResponse = await searchByMobileNumber(
          queryObj.mobileNumber,
          queryObj.tenantId ||envVariables.EGOV_DEFAULT_STATE_ID
        );
        // console.log(userSearchResponse);
        let searchUserUUID = get(userSearchResponse, "user.0.uuid");
        // if (searchUserUUID) {
        //   // console.log(searchUserUUID);
          sqlQuery = `${sqlQuery} FO.useruuid='${searchUserUUID || queryObj.mobileNumber}' AND`;
        // }
      }

      if (queryKeys) {
        queryKeys.forEach(item => {
          if (queryObj[item]) {
            if (
              item != "fromDate" &&
              item != "toDate" &&
              item != "tenantId" &&
              item != "status" &&
              item != "ids" &&
              item != "mobileNumber"
            ) {
              sqlQuery = `${sqlQuery} ${item}='${queryObj[item]}' AND`;
            }
          }
        });
      }
      if (
        queryObj.hasOwnProperty("fromDate") &&
        queryObj.hasOwnProperty("toDate")
      ) {
        sqlQuery = `${sqlQuery} FN.createdtime >= ${
          queryObj.fromDate
        } AND FN.createdtime <= ${queryObj.toDate} ORDER BY FN.uuid`;
      } else if (
        queryObj.hasOwnProperty("fromDate") &&
        !queryObj.hasOwnProperty("toDate")
      ) {
        sqlQuery = `${sqlQuery} FN.createdtime >= ${
          queryObj.fromDate
        } ORDER BY FN.uuid`;
      } else if(!isEmpty(queryObj)) {
        sqlQuery = `${sqlQuery.substring(
          0,
          sqlQuery.length - 3
        )} ORDER BY FN.uuid`;
      }


      console.log(sqlQuery);



      db.query(sqlQuery, async (err, dbRes) => {
        if (err) {
          console.log(err.stack);
        } else {
          // console.log(JSON.stringify(res.rows));
          response.FireNOCs =
            dbRes.rows && !isEmpty(dbRes.rows)
              ? await mergeSearchResults(
                  dbRes.rows,
                  request.query,
                  request.body.RequestInfo
                )
              : [];
          res.json(response);
        }
      });
    })
  );
  return api;
};
