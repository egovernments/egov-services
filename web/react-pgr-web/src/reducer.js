import { combineReducers } from 'redux';
import form from './reducers/form_reducer';
import auth from './reducers/auth';
import common from './reducers/common';

export default combineReducers({
  form,auth,common
});
