import { Router } from "express";
import producer from "../kafka/producer";

export default ({ config, db }) => {
  let api = Router();
  api.post("/_search", function({ body }, res) {
    let payloads=[];
    res.json(payloads);
  });
  return api;
};
