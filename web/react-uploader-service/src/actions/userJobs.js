import * as actionTypes from "../constants/actionTypes";
import { Api } from "../api";

export const initiateUserJobsFetch = () => {
  return { type: actionTypes.INITIATE_USER_JOBS_FETCH };
};

// fetch players success
export const fetchUserJobsSuccess = (userJobs = []) => {
  return { type: actionTypes.FETCH_USER_JOBS_SUCCESS, userJobs };
};

// fetch players failure
export const fetchUserJobsFailure = error => {
  return { type: actionTypes.FETCH_USER_JOBS_FAILURE, error };
};

export const fetchUserJobs = (codes, statuses, startDate, endDate) => {
  return async (dispatch, getState) => {
    dispatch(initiateUserJobsFetch());
    try {
      const userJobs = await Api().fetchUserJobs(
        codes,
        statuses,
        startDate,
        endDate
      );
      dispatch(fetchUserJobsSuccess(userJobs));
    } catch (error) {
      dispatch(fetchUserJobsFailure(error));
    }
  };
};
