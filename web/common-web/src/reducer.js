import { combineReducers } from 'redux';
import form from './reducers/form_reducer';
import auth from './reducers/auth';
import common from './reducers/common';
import labels from './reducers/labels';


export default combineReducers({
  form,auth,common,labels
});
