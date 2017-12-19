import { combineReducers } from "redux";
import userJobs from "./userJobs";
import filter from "./filter";
import auth from "./auth";
import uploadDefinitions from "./uploadDefinitions";
import fileUpload from "./fileUpload";
import jobCreate from "./jobCreate";

const rootReducer = combineReducers({
  auth,
  filter,
  userJobs,
  fileUpload,
  uploadDefinitions,
  jobCreate
});

export default rootReducer;
