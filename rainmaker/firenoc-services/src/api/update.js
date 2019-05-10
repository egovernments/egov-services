import { Router } from "express";
import producer from "../kafka/producer";

export default ({ config, db }) => {
  let api = Router();
  api.post("/_update", function({ body }, res) {
    let payloads=[];
    payloads.push({
      topic: process.env.KAFKA_TOPICS_FIRENOC_UPDATE,
      messages:JSON.stringify(body)
    })
    console.log("before",payloads);
    producer.send(payloads, function(err, data) {
      console.log(err);
      console.log(data);
      res.json(data);
    });
  });
  return api;
};
