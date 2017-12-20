import * as actionTypes from "../constants/actionTypes";

const initialState = {
  isFetching: false,
  error: false,
  selectedModule: null,
  selectedModuleDefinition: null,
  items: {}
};

const transformUploadDefinitions = uploadDefinitions => {
  return uploadDefinitions.reduce((transformedDefinition, moduleDefinition) => {
    transformedDefinition[
      moduleDefinition.name
    ] = moduleDefinition.Definitions.map(definition => definition.name);
    return transformedDefinition;
  }, {});
};

const uploadDefinitions = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.INITIATE_UPLOAD_DEFINITIONS_FETCH:
      return { ...state, isFetching: true, error: false };

    case actionTypes.UPLOAD_DEFINTIONS_RECEIVED:
      const uploadDefinitions = transformUploadDefinitions(
        action.uploadDefinitions
      );
      const selectedModule = Object.keys(uploadDefinitions)[0];
      const selectedModuleDefinition = uploadDefinitions[selectedModule][0];
      return {
        ...state,
        items: uploadDefinitions,
        selectedModule,
        selectedModuleDefinition,
        isFetching: false,
        error: false
      };

    case actionTypes.FETCH_UPLOAD_DEFINITIONS_FAILURE:
      return { ...state, error: true, isFetching: false };

    case actionTypes.MODULE_SELECTED:
      return {
        ...state,
        selectedModule: action.moduleName
      };

    case actionTypes.MOUDULE_DEFINITION_SELECTED:
      return {
        ...state,
        selectedModuleDefinition: action.moduleDefinition
      };

    default:
      return state;
  }
};
export default uploadDefinitions;
