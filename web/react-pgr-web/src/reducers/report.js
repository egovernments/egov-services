import _ from 'lodash';

const defaultState = {
  showTable: false,
  metaData:{},
  reportResult:{}
};

export default(state = defaultState, action) => {
  switch (action.type) {
    case "SET_META_DATA":
      return {
        ...state,
        metaData:action.metaData
      }

  case "SET_REPORT_RESULT":
        return {
          ...state,
          reportResult:action.reportResult
        }

    case "SHOW_TABLE":
      return {
        ...state,
        showTable: action.state
      }

    default:
      return state;
  }
}
