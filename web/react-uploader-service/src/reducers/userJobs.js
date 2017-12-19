import * as actionTypes from "../constants/actionTypes";

const initialState = {
  isFetching: false,
  error: false,
  items: []
};

const transformUserJobs = userJobs => {
  return userJobs.map(userJob => {
    const id = userJob.code;
    const status = userJob.status;
    const moduleName = userJob.moduleName;
    const moduleDefiniton = userJob.defName;
    const download = { label: "Download" };

    if (status === "completed") {
      // construct the download url
      download.href = "http://google.com";
    }

    return {
      id,
      moduleName,
      moduleDefiniton,
      status,
      download
    };
  });
};

const userJobs = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.INITIATE_USER_JOBS_FETCH:
      return { ...state, isFetching: true, error: false };
    case actionTypes.FETCH_USER_JOBS_SUCCESS:
      return {
        ...state,
        items: transformUserJobs(action.userJobs),
        isFetching: false,
        error: false
      };
    case actionTypes.FETCH_USER_JOBS_FAILURE:
      return { ...state, error: true, isFetching: false };
    default:
      return state;
  }
};
export default userJobs;
