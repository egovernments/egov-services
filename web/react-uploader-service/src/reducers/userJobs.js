import * as actionTypes from "../constants/actionTypes";
import { getFileDownloadLink } from "../utils";

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
    const startTime = userJob.startTime;
    const endTime = userJob.endTime;
    const download = { label: "Download" };

    if (status === "completed") {
      const tenantId = userJob.tenantId;
      const responseFilePath = userJob.responseFilePath;
      download.href = getFileDownloadLink(tenantId, responseFilePath);
    }

    return {
      id,
      moduleName,
      status,
      download,
      startTime,
      endTime,
      softDelete: false
    };
  });
};

// TODO : build a generic filter which is agnostic of the keys and value
const filterUserJobs = (filter, userJobs) => {
  return userJobs.map(userJob => {
    let shouldInclude = true;
    // apply the filters
    Object.keys(filter).forEach(filterKey => {
      let filterValue = filter[filterKey];

      if (
        Array.isArray(filterValue) &&
        filterValue.filter(value => value.trim().length > 0).length < 1
      ) {
        filterValue = null;
      }

      if (filterValue) {
        switch (filterKey) {
          case "startDate":
            shouldInclude &=
              new Date(userJob.startTime).getTime() >= filterValue;
            break;
          case "endDate":
            shouldInclude &= new Date(userJob.endTime).getTime() <= filterValue;
            break;
          case "statuses":
            shouldInclude &= filterValue.indexOf(userJob.status) !== -1;
            break;
          case "codes":
            shouldInclude &= filterValue.indexOf(userJob.id) !== -1;
            break;
          default:
            break;
        }
      }
    });
    return { ...userJob, softDelete: !shouldInclude };
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

    case actionTypes.FILTER_USER_JOBS:
      return { ...state, items: filterUserJobs(action.filter, state.items) };
    default:
      return state;
  }
};
export default userJobs;
