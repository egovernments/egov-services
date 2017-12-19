import * as actionTypes from "../constants/actionTypes";
import { Api } from "../api";

export const initiateFileUpload = () => {
  return { type: actionTypes.INITIATE_FILE_UPLOAD };
};

export const fileUploaded = fileStoreId => {
  return { type: actionTypes.FILE_UPLOAD_SUCCESS, fileStoreId };
};

export const fileUploadFailed = error => {
  return { type: actionTypes.FILE_UPLOAD_FAILURE, error };
};

export const setInputFile = file => {
  return { type: actionTypes.FILE_SELECTED, file };
};
export const uploadFile = (moduleName, file) => {
  return async (dispatch, getState) => {
    dispatch(initiateFileUpload());
    try {
      const fileStoreId = await Api().uploadFileMock(moduleName, file);
      dispatch(fileUploaded(fileStoreId));
    } catch (error) {
      //handle the error
      console.log(error);
    }
  };
};
