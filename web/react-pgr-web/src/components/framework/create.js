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

let reqRequired = []; 
class Report extends Component {
  constructor(props) {
    super(props);
  }

  setLabelAndReturnRequired(configObject) {
    for(var i=0; i<configObject.groups.length; i++) {
      configObject.groups[i].label = translate(configObject.groups[i].label);
      for (var j = 0; j < configObject.groups[i].fields.length; j++) {
            configObject.groups[i].fields[j].label = translate(configObject.groups[i].fields[j].label);
            if (configObject.groups[i].fields[j].isRequired)
                reqRequired.push(configObject.groups[i].fields[j].jsonPath);
      }

      if(configObject.groups[i].children && configObject.groups[i].children.length) {
        for(var k=0; k<configObject.groups[i].children.length; k++) {
          this.setLabelAndReturnRequired(configObject.groups[i].children[k]);
        }
      }
    }
  }

  initData() {
    let { setMetaData, setModuleName, setActionName, initForm, setMockData } = this.props;
    let hashLocation = window.location.hash;
    let obj = wcSpecs[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
    this.setLabelAndReturnRequired(obj);
    initForm(reqRequired, []);
    setMetaData(wcSpecs);
    setMockData(JSON.parse(JSON.stringify(wcSpecs)));
    setModuleName(hashLocation.split("/")[2]);
    setActionName(hashLocation.split("/")[1]);
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
      self.initData();
    }, function(err) {
      self.props.setLoadingStatus('hide');
      self.props.toggleSnackbarAndSetText(true, err.message);
    })
  }

  getVal = (path) => {
    return _.get(this.props.formData, path) || "";
  }

  // componentDidUpdate()
  // {
  //     // this.initData();
  // }

  handleChange=(e, property, isRequired, pattern, requiredErrMsg="Required",patternErrMsg="Pattern Missmatch") => {
      let {handleChange}=this.props;
      // console.log(e + " "+ property + " "+ isRequired +" "+pattern);
      handleChange(e,property, isRequired, pattern, requiredErrMsg, patternErrMsg);
  }

  incrementIndexValue = (group) => {

  }

  getNewSpecs = (group) => {

  }

  addNewCard = (group, jsonPath) => {
    let {setMockData} = this.props;
    //Increment the values of indexes
    let {updatedJsonPath, updatedSpecs} = this.incrementIndexValue(group, jsonPath);
    //Push to the path
    var updatedSpecs = this.getNewSpecs(group, updatedSpecs, updatedJsonPath);
    //Create new mock data
    setMockData(updatedSpecs);

  }

  removeCard = (jsonPath, index) => {

  }

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors} = this.props;
    let {create, handleChange, getVal, addNewCard} = this;
    return (
      <div className="Report">
        <form onSubmit={(e) => {
          create(e)
        }}>
        {!_.isEmpty(mockData) && <ShowFields groups={mockData[`${moduleName}.${actionName}`].groups} noCols={mockData[`${moduleName}.${actionName}`].numCols} ui="google" handler={handleChange} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false} addNewCard={addNewCard}/>}
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

const mapStateToProps = state => ({
  metaData:state.framework.metaData, 
  mockData: state.framework.mockData,
  moduleName:state.framework.moduleName,
  actionName:state.framework.actionName,
  formData:state.frameworkForm.form, 
  fieldErrors: state.frameworkForm.fieldErrors
});

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
  setMetaData: (metaData) => {
    dispatch({type:"SET_META_DATA", metaData})
  },
  setMockData: (mockData) => {
    dispatch({type: "SET_MOCK_DATA", mockData});
  },
  setModuleName: (moduleName) => {
    dispatch({type:"SET_MODULE_NAME", moduleName})
  },
  setActionName: (actionName) => {
    dispatch({type:"SET_ACTION_NAME", actionName})
  },
  handleChange: (e, property, isRequired, pattern, requiredErrMsg, patternErrMsg)=>{
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
