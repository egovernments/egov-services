import { version } from "../../package.json";
import { Router } from "express";
import update from "./update";
import create from "./create";
import search from "./search";

export default pool => {
  let api = Router();

  api.post("/billingslab/_create", (req, res) => create(req, res));
  api.post("/billingslab/_search", (req, res) => search(req, res, pool));
  api.post("/billingslab/_update", (req, res) => update(req, res));
  // perhaps expose some API metadata at the root
  api.get("/", (req, res) => {
    res.json({ version });
  });
  return api;
};
