import { combineReducers } from "redux";
import userJobs from "./userJobs";
import auth from "./auth";
import uploadDefinitions from "./uploadDefinitions";
import jobCreate from "./jobCreate";

const rootReducer = combineReducers({
  auth,
  uploadDefinitions,
  userJobs,
  jobCreate
});

export default rootReducer;
