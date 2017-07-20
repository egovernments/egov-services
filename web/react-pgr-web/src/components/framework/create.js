import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "./showFields";
import wcSpecs from './specs/wc/wc';

import {translate} from '../common/common';
import Api from '../../api/api';
import jp from "jsonpath";
import UiButton from './components/UiButton';

class Report extends Component {

  initData()
  {
    let {setMetaData,setModuleName,setAtionName,initForm}=this.props;
    let reqRequired = [], patRequired = [];
    let hashLocation=window.location.hash;
    let obj=wcSpecs[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
    for (var i = 0; i <obj.groups.length; i++) {
      obj.groups[i].label=translate(obj.groups[i].label);
      for (var j = 0; j < obj.groups[i].fields.length; j++) {
        obj.groups[i].fields[j].label=translate(obj.groups[i].fields[j].label);
        if(obj.groups[i].fields[j].isRequired)
          reqRequired.push(obj.groups[i].fields[j].jsonPath);
      }
    }
    
    initForm(reqRequired, patRequired);
    setMetaData(wcSpecs);
    setModuleName(hashLocation.split("/")[2]);
    setAtionName(hashLocation.split("/")[1]);
  }

  componentDidMount() {
      this.initData();
  }

  create=(e) => {
    let self = this;
    e.preventDefault();
    self.props.setLoadingStatus('loading');
    var formData = Object.assign(this.props.formData);
    if(self.props.moduleName && self.props.actionName && self.props.metaData && self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].tenantIdRequired) {
      if(!formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName])
        formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName] = {};

      formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName]["tenantId"] = localStorage.getItem("tenantId") || "default";
    }

    Api.commonApiPost(self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].url, "", formData, "", true).then(function(response){
      self.props.setLoadingStatus('hide');
      self.props.toggleSnackbarAndSetText(true, "Success!");
      self.props.initData();
    }, function(err) {
      self.props.setLoadingStatus('hide');
      self.props.toggleSnackbarAndSetText(true, err.message);
    })
  }

  getVal = (path) => {
    return _.get(this.props.formData, path);
  }

  // componentDidUpdate()
  // {
  //     // this.initData();
  // }

  handleChange=(e, property, isRequired, pattern, requiredErrMsg="Required",patternErrMsg="Pattern Missmatch")=>
  {
      let {handleChange}=this.props;
      // console.log(e + " "+ property + " "+ isRequired +" "+pattern);
      handleChange(e,property, isRequired, pattern, requiredErrMsg, patternErrMsg);
  }





  render() {
    let {metaData, moduleName, actionName, formData, fieldErrors}=this.props;
    let {create, handleChange, getVal}=this;
    return (
      <div className="Report">
        <form onSubmit={(e) => {
          create(e)
        }}>
        {!_.isEmpty(metaData) && <ShowFields groups={metaData[`${moduleName}.${actionName}`].groups} noCols={metaData[`${moduleName}.${actionName}`].numCols} ui="google" handler={handleChange} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={metaData[`${moduleName}.${actionName}`].useTimestamp || false}/>}
          <div style={{"textAlign": "center"}}>
            <br/>
            <UiButton item={{"label": "Create", "uiType":"submit"}} ui="google"/>
            <br/>
          </div>
        </form>
      </div>
    );
  }
}

const mapStateToProps = state => ({metaData:state.framework.metaData,moduleName:state.framework.moduleName,actionName:state.framework.actionName,formData:state.frameworkForm.form, fieldErrors: state.frameworkForm.fieldErrors});

const mapDispatchToProps = dispatch => ({
  initForm: (reqRequired, patRequired) => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: reqRequired
        },
        pattern: {
          current: [],
          required: patRequired
        }
      }
    });
  },
  setMetaData:(metaData)=>{
    dispatch({type:"SET_META_DATA",metaData})
  },
  setModuleName:(moduleName)=>{
    dispatch({type:"SET_MODULE_NAME",moduleName})
  },
  setAtionName:(actionName)=>{
    dispatch({type:"SET_ACTION_NAME",actionName})
  },
  handleChange:(e, property, isRequired, pattern, requiredErrMsg, patternErrMsg)=>{
    dispatch({type:"HANDLE_CHANGE_VERSION_TWO",property,value: e.target.value, isRequired, pattern, requiredErrMsg, patternErrMsg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState,toastMsg});
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);
