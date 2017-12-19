import * as actionTypes from "../constants/actionTypes";

const initialState = {
  completionStatus: null,
  fromDate: null,
  toDate: null
};
const filter = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.JOBS_STATUS_FILTER:
      return {
        ...state,
        completionStatus: action.completionStatus
      };
    case actionTypes.FROM_DATE_FILTER:
      return {
        ...state,
        fromDate: action.fromDate
      };
    case actionTypes.TO_DATE_FILTER:
      return {
        ...state,
        toDate: action.toDate
      };
    default:
      return state;
  }
};

export default filter;
