import { combineReducers } from 'redux';
import form from './reducers/form_reducer';
import auth from './reducers/auth';
import common from './reducers/common';
import report from './reducers/report';
import framework from './reducers/framework';


export default combineReducers({
  form,auth,common,report,framework
});
