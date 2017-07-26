import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "./showFields";

import {translate} from '../common/common';
import Api from '../../api/api';
import UiButton from './components/UiButton';
import UiDynamicTable from './components/UiDynamicTable';
import {fileUpload} from './utility/utility';
var specifications={};
try {
  var hash = window.location.hash.split("/");
  if(hash.length == 3) {
    specifications = require(`./specs/${hash[2]}/${hash[2]}`).default;
  } else {
    specifications = require(`./specs/${hash[2]}/master/${hash[3]}`).default;
  }
} catch(e) {}
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

  getVal = (path) => {
    return _.get(this.props.formData, path) || "";
  }

  initData() {
    let { setMetaData, setModuleName, setActionName, initForm, setMockData } = this.props;
    let hashLocation = window.location.hash;
    let obj = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
    this.setLabelAndReturnRequired(obj);
    initForm(reqRequired, []);
    setMetaData(specifications);
    setMockData(JSON.parse(JSON.stringify(specifications)));
    setModuleName(hashLocation.split("/")[2]);
    setActionName(hashLocation.split("/")[1]);
  }

  componentDidMount() {
      this.initData();
  }

  search = (e) => {


  }

  getVal = (path) => {
    return _.get(this.props.formData, path) || "";
  }

  handleChange=(e, property, isRequired, pattern, requiredErrMsg="Required",patternErrMsg="Pattern Missmatch") => {
      let {handleChange}=this.props;
      // console.log(e + " "+ property + " "+ isRequired +" "+pattern);
      handleChange(e,property, isRequired, pattern, requiredErrMsg, patternErrMsg);
  }

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors} = this.props;
    let {search, handleChange, getVal, addNewCard, removeCard} = this;
    return (
      <div className="Report">
        <form onSubmit={(e) => {
          search(e)
        }}>
        {!_.isEmpty(mockData) && <ShowFields groups={mockData[`${moduleName}.${actionName}`].groups} noCols={mockData[`${moduleName}.${actionName}`].numCols} ui="google" handler={handleChange} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false} addNewCard={""} removeCard={""}/>}
          <div style={{"textAlign": "center"}}>
            <br/>
            <UiButton item={{"label": "Search", "uiType":"submit"}} ui="google"/>
            <UiDynamicTable getVal={getVal} fieldErrors={fieldErrors} handler={handleChange} item={{
							"name": "consumerCode",
							"jsonPath": "search.consumerCode",
							"label": "Consumer Code",
							"pattern": "",
							"type": "dynamictable",
							"header": [{
									"label": "wc.table.view"
							}],
							"values":[{

		  						cols:[{
                    "name": "billerService",
  		  						"jsonPath": "search.billerService",
  		  						"label": "Billing service name",
  		  						"pattern": "",
  		  						"type": "singleValueList",
  		  						"isRequired": false,
  		  						"isDisabled": false,
  		  						"url": "/egov-common-masters/departments/_search?tenantId=default|$..id|$..name",
  		  						"requiredErrMsg": "",
  		  						"patternErrMsg": ""
                  }]

							}],
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}}/>
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
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);
