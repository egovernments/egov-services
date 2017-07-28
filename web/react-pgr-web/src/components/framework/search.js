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
import UiTable from './components/UiTable';

var specifications={};
try {
  var hash = window.location.hash.split("/");
  if(hash.length == 4) {
    specifications = require(`./specs/${hash[2]}/${hash[2]}`).default;
  } else {
    specifications = require(`./specs/${hash[2]}/master/${hash[3]}`).default;
  }
} catch(e) {}
let reqRequired = [];
class Report extends Component {
  constructor(props) {
    super(props);
    this.state = {
      showResult: false,
      resultList : {
        resultHeader: [],
        resultValues: []
      },
      values: []
    }
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
    e.preventDefault();
    let self = this;
    self.props.setLoadingStatus('loading');
    var formData = {...this.props.formData};
    for(var key in formData) {
      if(!formData[key])
        delete formData[key];
    }

    Api.commonApiPost(self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].url, formData, {}, null, self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].useTimestamp).then(function(res){
      self.props.setLoadingStatus('hide');
      var resultList = {
        resultHeader: [{label: "#"}, ...self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.header],
        resultValues: []
      };
      var specsValuesList = self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.values;
      var values = _.get(res, self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.resultPath);
      if(values && values.length) {
        for(var i=0; i<values.length; i++) {
          var tmp = [i+1];
          for(var j=0; j<specsValuesList.length; j++) {
            tmp.push(_.get(values[i], specsValuesList[j]));
          }
          resultList.resultValues.push(tmp);
        }
      }
      self.setState({
        resultList,
        values,
        showResult: true
      });
    }, function(err) {
      self.props.toggleSnackbarAndSetText(true, err.message, false, true);
      self.props.setLoadingStatus('hide');
    })
  }

  getVal = (path) => {
    return _.get(this.props.formData, path) || "";
  }

  handleChange=(e, property, isRequired, pattern, requiredErrMsg="Required",patternErrMsg="Pattern Missmatch") => {
      let {handleChange}=this.props;
      handleChange(e,property, isRequired, pattern, requiredErrMsg, patternErrMsg);
  }

  rowClickHandler = (index) => {
    var value = this.state.values[index];
    var _url = window.location.hash.split("/").indexOf("update") > -1 ? this.props.metaData[`${this.props.moduleName}.${this.props.actionName}`].result.rowClickUrlUpdate : this.props.metaData[`${this.props.moduleName}.${this.props.actionName}`].result.rowClickUrlView;
    _url = _url.replace("{id}", value.id);
    this.props.setRoute(_url);
  }

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors} = this.props;
    let {search, handleChange, getVal, addNewCard, removeCard, rowClickHandler} = this;
    let {showResult, resultList} = this.state;

    return (
      <div className="SearchResult">
        <form onSubmit={(e) => {
          search(e)
        }}>
        {!_.isEmpty(mockData) && <ShowFields groups={mockData[`${moduleName}.${actionName}`].groups} noCols={mockData[`${moduleName}.${actionName}`].numCols} ui="google" handler={handleChange} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false} addNewCard={""} removeCard={""}/>}
          <div style={{"textAlign": "center"}}>
            <br/>
            <UiButton item={{"label": "Search", "uiType":"submit"}} ui="google"/>
            <br/>
            {showResult && <UiTable resultList={resultList} rowClickHandler={rowClickHandler}/>}
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
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route})
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);
