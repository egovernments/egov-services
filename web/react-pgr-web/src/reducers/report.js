// import _ from 'lodash';

const defaultState = {
  showTable: false,
  metaData:{},
  reportResult:{},
  flag:0,
  searchParams:[]
};

export default(state = defaultState, action) => {
  switch (action.type) {
    case "SET_SEARCH_PARAMS":
      return {
        ...state,
        searchParams:action.searchParams
      }

    case "SET_META_DATA":
      return {
        ...state,
        "metaData":{
          ...state.metaData,
          ...action.metaData
        }
        // Object.assign(state.metaData,action.metaData)
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

      case "SET_FLAG":
        return {
          ...state,
          flag: action.flag
        }

    default:
      return state;
  }
}
