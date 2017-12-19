import * as actionTypes from "../constants/actionTypes";

const initialState = {
  isFetching: false,
  error: false,
  fileStoreId: null,
  inputFile: null
};
const fileUpload = (state = initialState, action) => {
  switch (action.type) {
    case actionTypes.INITIATE_FILE_UPLOAD:
      return { ...state, isFetching: true, error: false };
    case actionTypes.FILE_UPLOAD_SUCCESS:
      return {
        ...state,
        fileStoreId: action.fileStoreId,
        isFetching: false,
        error: false
      };
    case actionTypes.FILE_UPLOAD_FAILURE:
      return { ...state, error: true, isFetching: false };
    case actionTypes.FILE_SELECTED:
      return { ...state, inputFile: action.file };

    default:
      return state;
  }
};
export default fileUpload;
