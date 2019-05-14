import { Router } from "express";
import producer from "../kafka/producer";
import { requestInfoToResponseInfo ,addUUIDAndAuditDetails} from "../utils";

export default ({ config, db }) => {
  let api = Router();
  api.post("/_create", function({ body }, res) {
    let payloads=[];
    // console.log(body);
    body=addUUIDAndAuditDetails(body);
    payloads.push({
      topic: process.env.KAFKA_TOPICS_FIRENOC_CREATE,
      messages:JSON.stringify(body)
    })
    // console.log("before",payloads);
    // console.log(body);
    producer.send(payloads, function(err, data) {
      // console.log(err);
      // console.log(data);
      let response = {
        ResponseInfo: requestInfoToResponseInfo(body.RequestInfo, true),
        FireNOCs: body.FireNOCs
      };
      res.json(response);
    });
    // res.json("");
  });
  return api;
};
