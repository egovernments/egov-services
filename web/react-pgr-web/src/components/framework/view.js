import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "./showFields";

import {translate} from '../common/common';
import Api from '../../api/api';
import jp from "jsonpath";
import UiButton from './components/UiButton';
import {fileUpload} from './utility/utility';
import UiTable from './components/UiTable';

var specifications={};

let reqRequired = [];
class Report extends Component {
  constructor(props) {
    super(props);
  }

  setLabelAndReturnRequired(configObject) {
    if(configObject && configObject.groups) {
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
  }

  initData() {
    try {
      var hash = window.location.hash.split("/");
      if(hash.length == 4) {
        specifications = require(`./specs/${hash[2]}/${hash[2]}`).default;
      } else {
        specifications = require(`./specs/${hash[2]}/master/${hash[3]}`).default;
      }
    } catch(e) {
      
    }

    let { setMetaData, setModuleName, setActionName, initForm, setMockData } = this.props;
    let hashLocation = window.location.hash;
    let self = this;
    let obj = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
    this.setLabelAndReturnRequired(obj);
    initForm(reqRequired, []);
    setMetaData(specifications);
    setMockData(JSON.parse(JSON.stringify(specifications)));
    setModuleName(hashLocation.split("/")[2]);
    setActionName(hashLocation.split("/")[1]);
    //Get view form data
    var url = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].url.split("?")[0];
    var hash = window.location.hash.split("/");
    var value = (hash.length == 4) ? hash[3] : hash[4];
    var query = {
      [specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].url.split("?")[1].split("=")[0]]: value
    };

    Api.commonApiPost(url, query, {}, false, specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].useTimestamp).then(function(res){
      self.props.setFormData(res);
    }, function(err){

    })
  }

  componentDidMount() {
      this.initData();
  }

  getVal = (path) => {
    var val = _.get(this.props.formData, path);
    return  val && (typeof val == "string" || typeof val == "number" || typeof val == "boolean") ? (val + "") : "";
  }

  printer = () => {
    window.print();
  }

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors} = this.props;
    let {handleChange, getVal, addNewCard, removeCard, printer} = this;

    const renderTable = function() {
      if(moduleName && actionName && formData && formData[objectName]) {
        var objectName = mockData[`${moduleName}.${actionName}`].objectName;
        if(formData[objectName].documents && formData[objectName].documents.length) {
          var dataList = {
            resultHeader: ["#", "Name", "File"],
            resultValues: []
          };

          for(var i=0; i<formData[objectName].documents.length; i++) {
            dataList.resultValues.push([i+1, formData[objectName].documents[i].name || "File", "<a href=/filestore/v1/files/id?tenantId=" + localStorage.getItem("tenantId") + "&fileStoreId=" + formData[objectName].documents[i].fileStoreId + ">Download</a>"]);
          }

          return (
            <UiTable resultList={dataList}/>
          );
        }
      }
    }

    return (
      <div className="Report">
        <form id="printable">
        {!_.isEmpty(mockData) && <ShowFields groups={mockData[`${moduleName}.${actionName}`].groups} noCols={mockData[`${moduleName}.${actionName}`].numCols} ui="google" handler={""} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false} addNewCard={""} removeCard={""} screen="view"/>}
          <br/>
          {renderTable()}
          <br/>
        </form>
        <div style={{"textAlign": "center"}}>
            <UiButton item={{"label": "Print", "uiType":"view"}} ui="google" handler={printer}/>
        </div>
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
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
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
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);
