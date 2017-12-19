import * as actionTypes from "../constants/actionTypes";

// fetch players success
export const applyFromDateFilter = fromDate => {
  return { type: actionTypes.FROM_DATE_FILTER, fromDate };
};

export const applyToDateFilter = toDate => {
  return { type: actionTypes.TO_DATE_FILTER, toDate };
};

export const applyJobsStatusFilter = completionStatus => {
  return { type: actionTypes.JOBS_STATUS_FILTER, completionStatus };
};
