import { combineReducers } from "redux";
import userJobs from "./userJobs";
import filter from "./filter";
import auth from "./auth";
import uploadDefinitions from "./uploadDefinitions";
import jobCreate from "./jobCreate";

const rootReducer = combineReducers({
  auth,
  filter,
  userJobs,
  uploadDefinitions,
  jobCreate
});

export default rootReducer;
