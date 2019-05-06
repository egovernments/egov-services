import { Router } from "express";

export default ({ config, db }) => {
  let api = Router();
  api.post("/_search", function(req, res) {
    let payloads=[];
    res.json(payloads);
  });
  return api;
};
