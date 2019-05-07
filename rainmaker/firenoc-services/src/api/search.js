import { Router } from "express";

export default ({ config, db }) => {
  let api = Router();
  api.post("/_search", function(request, apiRes) {
    let response = {
      ResponseInfo: {
        apiId: "string",
        ver: "string",
        ts: 0,
        resMsgId: "string",
        msgId: "string",
        status: "SUCCESSFUL"
      },
      FireNOCs: []
    };
    // "FireNOCs": [
    const text =
      "SELECT * FROM eg_fn_firenoc FN JOIN eg_fn_firenocdetail FD ON (FN.uuid = FD.firenocuuid) JOIN eg_fn_owner FO ON (FD.uuid = FO.firenocdetailsuuid) where";
    const queryObj = request.query;
    const queryKeys = Object.keys(queryObj);
    let sqlQuery = text;
    if (queryKeys) {
      queryKeys.forEach(item => {
        if (item != "fromDate" && item != "toDate")
          sqlQuery = `${sqlQuery} ${item}='${queryObj[item]}' AND`;
      });
    }
    if (
      queryObj.hasOwnProperty("fromDate") &&
      queryObj.hasOwnProperty("toDate")
    ) {
      sqlQuery = `${sqlQuery} FN.createdtime >= ${
        queryObj.fromDate
      } AND FN.lastmodifiedtime <= ${queryObj.toDate}`;
    } else if (
      queryObj.hasOwnProperty("fromDate") &&
      !queryObj.hasOwnProperty("toDate")
    ) {
      sqlQuery = `${sqlQuery} FN.createdtime >= ${queryObj.fromDate}`;
    } else {
      console.log("Select From Date");
      sqlQuery = sqlQuery.substring(0, sqlQuery.length - 3);
    }
    db.query(sqlQuery, (err, res) => {
      if (err) {
        console.log(err.stack);
      } else {
        console.log(res.rows[0]);
        response.FireNOCs = res.rows;
        apiRes.json(response);
      }
    });
  });
  return api;
};
