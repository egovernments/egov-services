// import _ from 'lodash';

const defaultState = {
  showTable: false,
  metaData: {},
  mockData: {},
  reportResult: {},
  flag: 0,
  moduleName: '',
  actionName: '',
  dropDownData: {},
  dropDownOringalData: {},
};

export default (state = defaultState, action) => {
  switch (action.type) {
    case 'SET_META_DATA':
      return {
        ...state,
        metaData: action.metaData,
      };
    case 'SET_MOCK_DATA':
      return {
        ...state,
        mockData: action.mockData,
      };
    case 'SET_DROPDWON_DATA':
      return {
        ...state,
        dropDownData: {
          ...state.dropDownData,
          [action.fieldName]: action.dropDownData,
        },
      };
    case 'SET_ORIGINAL_DROPDWON_DATA':
      return {
        ...state,
        dropDownOringalData: {
          ...state.dropDownData,
          [action.fieldName]: action.dropDownData,
        },
      };

    case 'RESET_ORIGNINAL_DROPDOWN_DATA':
      return {
        ...state,
        dropDownOringalData: {},
      };

    case 'RESET_DROPDOWN_DATA':
      return {
        ...state,
        dropDownData: {},
      };

    case 'SET_MODULE_NAME':
      return {
        ...state,
        moduleName: action.moduleName,
      };

    case 'SET_ACTION_NAME':
      return {
        ...state,
        actionName: action.actionName,
      };

    case 'SET_REPORT_RESULT':
      return {
        ...state,
        reportResult: action.reportResult,
      };

    case 'SHOW_TABLE':
      return {
        ...state,
        showTable: action.state,
      };

    case 'SET_FLAG':
      return {
        ...state,
        flag: action.flag,
      };

    default:
      return state;
  }
};
