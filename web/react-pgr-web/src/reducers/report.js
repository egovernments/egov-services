import _ from 'lodash';

const defaultState = {
  showTable: false,
  metaData:{}
};

export default(state = defaultState, action) => {
  switch (action.type) {
    case "SET_META_DATA":
      return {
        ...state,
        metaData:action.metaData
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
