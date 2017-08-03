import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "./showFields";

import {translate} from '../common/common';
import Api from '../../api/api';
import jp from "jsonpath";
import UiButton from './components/UiButton';
import {fileUpload, getInitiatorPosition} from './utility/utility';
import $ from "jquery";

var specifications={};
let reqRequired = [];
let baseUrl="https://raw.githubusercontent.com/abhiegov/test/master/specs/";
class Report extends Component {
  state={
    pathname:""
  }
  constructor(props) {
    super(props);
  }

  setLabelAndReturnRequired(configObject) {
    if(configObject && configObject.groups) {
      for(var i=0;configObject && i<configObject.groups.length; i++) {
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

  setDefaultValues (groups, dat) {
    for(var i=0; i<groups.length; i++) {
      for(var j=0; j<groups[i].fields.length; j++) {
        if(typeof groups[i].fields[j].defaultValue != 'undefined') {
          _.set(dat, groups[i].fields[j].jsonPath, groups[i].fields[j].defaultValue);
        }

        if(groups[i].fields[j].children && groups[i].fields[j].children.length) {
          for(var k=0; k<groups[i].fields[j].children.length; k++) {
            this.setDefaultValues(groups[i].fields[j].children[k].groups);
          }
        }
      }
    }
  }

  displayUI(results)
  {
    let { setMetaData, setModuleName, setActionName, initForm, setMockData, setFormData } = this.props;
    let hashLocation = window.location.hash;
    let self = this;

    specifications =typeof(results)=="string"?JSON.parse(results):results;
    let obj = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
    self.setLabelAndReturnRequired(obj);
    initForm(reqRequired);
    setMetaData(specifications);
    setMockData(JSON.parse(JSON.stringify(specifications)));
    setModuleName(hashLocation.split("/")[2]);
    setActionName(hashLocation.split("/")[1]);

    if(hashLocation.split("/").indexOf("update") > -1) {
      var url = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].searchUrl.split("?")[0];
      var id = self.props.match.params.id || self.props.match.params.master;
      var query = {
        [specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].searchUrl.split("?")[1].split("=")[0]]: id
      };
      Api.commonApiPost(url, query, {}, false, specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].useTimestamp).then(function(res){
          if(specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].isResponseArray) {
            var obj = {};
            _.set(obj, specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].objectName, jp.query(res, "$..[0]")[0]);
            self.props.setFormData(obj);
          } else {
            self.props.setFormData(res);
          }
      }, function(err){

      })
    } else {
      var formData = {};
      if(obj && obj.groups && obj.groups.length) self.setDefaultValues(obj.groups, formData);
      setFormData(formData);
    }

    this.setState({
      pathname:this.props.history.location.pathname
    })
  }

  initData() {
    var hash = window.location.hash.split("/");
    let endPoint="";
    let self = this;

    // if (hash[2]=="wc") {
    //     if(hash.length == 3 || (hash.length == 4 && hash.indexOf("update") > -1)) {
    //       endPoint = `${hash[2]}/${hash[2]}.json`;
    //     } else {
    //       endPoint = `${hash[2]}/master/${hash[3]}.json`;
    //     }
    //   $.ajax({
    //   url: baseUrl+endPoint+"?timestamp="+new Date().getTime(),
    //   // dataType: 'application/javascript',
    //   success: function(results)
    //   {
    //     self.displayUI(results);
    //   },
    //   error: function (results) {
    //     try {
    //       if(hash.length == 3 || (hash.length == 4 && hash.indexOf("update") > -1)) {
    //         specifications = require(`./specs/${hash[2]}/${hash[2]}`).default;
    //       } else {
    //         specifications = require(`./specs/${hash[2]}/master/${hash[3]}`).default;
    //       }
    //     } catch(e) {
    //
    //     }
    //     self.displayUI(specifications);
    //
    //
    //   }})
    // }
    //
    // else {
      try {
        if(hash.length == 3 || (hash.length == 4 && hash.indexOf("update") > -1)) {
          specifications = require(`./specs/${hash[2]}/${hash[2]}`).default;
        } else {
          specifications = require(`./specs/${hash[2]}/master/${hash[3]}`).default;
        }
      } catch(e) {

      }

      self.displayUI(specifications);

    // }

  }

  componentDidMount() {
      this.initData();
  }

  componentWillReceiveProps(nextProps)
  {
    if (this.state.pathname!=nextProps.history.location.pathname) {
      this.initData();
    }
  }

  // componentDidUpdate(nextProps,prevProps)
  // {
  //   if (this.props.history.location.pathname!=nextProps.history.location.pathname) {
  //     this.initData();
  //   }
  // }

  autoComHandler = (autoObject, path) => {
    let self = this;
    var value = this.getVal(path);
    if(!value) return;
    var url = autoObject.autoCompleteUrl.split("?")[0];
    var hashLocation = window.location.hash;
    var query = {
        [autoObject.autoCompleteUrl.split("?")[1].split("=")[0]]: value
    };
    Api.commonApiPost(url, query, {}, false, specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].useTimestamp).then(function(res){
        var formData = {...self.props.formData};
        for(var key in autoObject.autoFillFields) {
          _.set(formData, key, _.get(res, autoObject.autoFillFields[key]));
        }
        self.props.setFormData(formData);
    }, function(err){
      console.log(err);
    })
  }

  makeAjaxCall = (formData, url) => {
    let self = this;
    delete formData.ResponseInfo;
    Api.commonApiPost((url || self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].url), "", formData, "", true).then(function(response){
      self.props.setLoadingStatus('hide');
      self.initData();
      self.props.toggleSnackbarAndSetText(true, translate(self.props.actionName == "create" ? "wc.create.message.success" : "wc.update.message.success"), true);
      setTimeout(function() {
        if(self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].idJsonPath) {
          if(self.props.actionName == "update") {
            var hash = window.location.hash.replace(/(\#\/create\/|\#\/update\/)/, "/view/");
          } else {
            var hash = window.location.hash.replace(/(\#\/create\/|\#\/update\/)/, "/view/") + "/" + _.get(response, self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].idJsonPath);
          }
          self.props.setRoute(hash);
        }
      }, 1500);
    }, function(err) {
      self.props.setLoadingStatus('hide');
      self.props.toggleSnackbarAndSetText(true, err.message);
    })
  }

  //Needs to be changed later for more customfields
  checkCustomFields = (formData, cb) => {
    var self = this;
    if(self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].customFields && self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].customFields.initiatorPosition) {
      var jPath = self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].customFields.initiatorPosition;
      getInitiatorPosition(function(err, pos) {
        if(err) {
          self.toggleSnackbarAndSetText(true, err.message);
        } else {
          _.set(formData, jPath, pos);
          cb(formData);
        }
      })
    } else {
      cb(formData);
    }
  }

  create=(e) => {
    let self = this, _url;
    e.preventDefault();
    self.props.setLoadingStatus('loading');
    var formData = {...this.props.formData};
    if(self.props.moduleName && self.props.actionName && self.props.metaData && self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].tenantIdRequired) {
      if(!formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName])
        formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName] = {};

      if(formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName].constructor == Array) {
        for(var i=0; i< formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName].length; i++) {
          formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName][i]["tenantId"] = localStorage.getItem("tenantId") || "default";
        }
      } else
        formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName]["tenantId"] = localStorage.getItem("tenantId") || "default";
    }

    if(/\{.*\}/.test(self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].url)) {
      _url = self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].url;
      var match = _url.match(/\{.*\}/)[0];
      var jPath = match.replace(/\{|}/g,"");
      _url = _url.replace(match, _.get(formData, jPath));
    }

    //Check if documents, upload and get fileStoreId
    if(formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName]["documents"] && formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName]["documents"].length) {
      let documents = [...formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName]["documents"]];
      let _docs = [];
      let counter = documents.length, breakOut = 0;
      for(let i=0; i<documents.length; i++) {
        fileUpload(documents[i].fileStoreId, self.props.moduleName, function(err, res) {
          if(breakOut == 1) return;
          if(err) {
            breakOut = 1;
            self.props.setLoadingStatus('hide');
            self.props.toggleSnackbarAndSetText(true, err, false, true);
          } else {
            _docs.push({
              ...documents[i],
              fileStoreId: res.files[0].fileStoreId
            })
            counter--;
            if(counter == 0 && breakOut == 0) {
              formData[self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].objectName]["documents"] = _docs;
              self.makeAjaxCall(formData, _url);
            }
          }
        })
      }
    } else {
      self.makeAjaxCall(formData, _url);
    }

  }

  getVal = (path) => {
    return _.get(this.props.formData, path) || "";
  }

  hideField = (_mockData, hideObject, reset) => {
    let {moduleName, actionName} = this.props;
    if(hideObject.isField) {
      for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(hideObject.name == _mockData[moduleName + "." + actionName].groups[i].fields[j].name) {
            _mockData[moduleName + "." + actionName].groups[i].fields[j].hide = reset ? false : true;
          }
        }
      }
    } else {
      for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
        if(hideObject.name == _mockData[moduleName + "." + actionName].groups[i].name) {
          _mockData[moduleName + "." + actionName].groups[i].hide = reset ? false : true;
        }
      }
    }

    return _mockData;
  }

  showField = (_mockData, showObject, reset) => {
    let {moduleName, actionName} = this.props;
    if(showObject.isField) {
      for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(showObject.name == _mockData[moduleName + "." + actionName].groups[i].fields[j].name) {
            _mockData[moduleName + "." + actionName].groups[i].fields[j].hide = reset ? true : false;
          }
        }
      }
    } else {
      for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
        if(showObject.name == _mockData[moduleName + "." + actionName].groups[i].name) {
          _mockData[moduleName + "." + actionName].groups[i].hide = reset ? true : false;
        }
      }
    }

    return _mockData;
  }

  checkIfHasShowHideFields = (jsonPath, val) => {
    let _mockData = {...this.props.mockData};
    let {moduleName, actionName, setMockData} = this.props;
    for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
      for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
        if(jsonPath == _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath && _mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields && _mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields.length) {
          for(let k=0; k<_mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields.length; k++) {
            if(val == _mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].ifValue) {
              for(let y=0; y<_mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide.length; y++) {
                _mockData = this.hideField(_mockData, _mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide[y]);
              }

              for(let z=0; z<_mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show.length; z++) {
                _mockData = this.showField(_mockData, _mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show[z]);
              }
            } else {
              for(let y=0; y<_mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide.length; y++) {
                _mockData = this.hideField(_mockData, _mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide[y], true);
              }

              for(let z=0; z<_mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show.length; z++) {
                _mockData = this.showField(_mockData, _mockData[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show[z], true);
              }
            }
          }
        }
      }
    }

    setMockData(_mockData);
  }

  handleChange=(e, property, isRequired, pattern, requiredErrMsg="Required",patternErrMsg="Pattern Missmatch") => {
      let {getVal}=this;
      let {handleChange,mockData,setDropDownData}=this.props;
      let hashLocation = window.location.hash;
      let obj = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
      // console.log(obj);
      let depedants=jp.query(obj,`$.groups..fields[?(@.jsonPath=="${property}")].depedants.*`);
      this.checkIfHasShowHideFields(property, e.target.value);
      handleChange(e,property, isRequired, pattern, requiredErrMsg, patternErrMsg);

      _.forEach(depedants, function(value,key) {
            if (value.type=="dropDown") {
                let splitArray=value.pattern.split("?");
                let context="";
          			let id={};
          			// id[splitArray[1].split("&")[1].split("=")[0]]=e.target.value;
          			for (var j = 0; j < splitArray[0].split("/").length; j++) {
          				context+=splitArray[0].split("/")[j]+"/";
          			}

          			let queryStringObject=splitArray[1].split("|")[0].split("&");
          			for (var i = 0; i < queryStringObject.length; i++) {
          				if (i) {
                    if (queryStringObject[i].split("=")[1].search("{")>-1) {
                      if (queryStringObject[i].split("=")[1].split("{")[1].split("}")[0]==property) {
                        id[queryStringObject[i].split("=")[0]]=e.target.value || "";
                      } else {
                        id[queryStringObject[i].split("=")[0]]=getVal(queryStringObject[i].split("=")[1].split("{")[1].split("}")[0]);
                      }
                    } else {
                      id[queryStringObject[i].split("=")[0]]=queryStringObject[i].split("=")[1];
                    }
          				}
          			}

                Api.commonApiPost(context,id).then(function(response)
                {
                  let keys=jp.query(response,splitArray[1].split("|")[1]);
                  let values=jp.query(response,splitArray[1].split("|")[2]);
                  let dropDownData=[];
                  for (var k = 0; k < keys.length; k++) {
                      let obj={};
                      obj["key"]=keys[k];
                      obj["value"]=values[k];
                      dropDownData.push(obj);
                  }
                  setDropDownData(value.jsonPath,dropDownData);
                },function(err) {
                    console.log(err);
                });
                // console.log(id);
                // console.log(context);
            }

            else if (value.type=="textField") {
              let object={
                target:{
                  value:eval(eval(value.pattern))
                }
              }
              handleChange(object,value.jsonPath,value.isRequired,value.rg,value.requiredErrMsg,value.patternErrMsg);
            }
      });

  }

  incrementIndexValue = (group, jsonPath) => {
    let {formData} = this.props;
    var length = _.get(formData, jsonPath) ? _.get(formData, jsonPath).length : 0;
    var _group = JSON.stringify(group);
    var regexp = new RegExp(jsonPath + "\\[\\d{1}\\]", "g");
    _group = _group.replace(regexp, jsonPath + "[" + (length+1) + "]");
    return JSON.parse(_group);
  }

  getNewSpecs = (group, updatedSpecs, path) => {
    let {moduleName, actionName} = this.props;
    let groupsArray = _.get(updatedSpecs[moduleName + "." + actionName], path);
    groupsArray.push(group);
    _.set(updatedSpecs[moduleName + "." + actionName], path, groupsArray);
    return updatedSpecs;
  }

  getPath = (value) => {
    let {mockData, moduleName, actionName} = this.props;
    const getFromGroup = function(groups) {
      for(var i=0; i<groups.length; i++) {
        for(var j=0; j<groups[i].children.length; i++) {
          if(groups[i].children[j].jsonPath == value) {
            return "groups[" + i + "].children[" + j + "].groups";
          } else {
            return "groups[" + i + "].children[" + j + "][" + getFromGroup(groups[i].children[j].groups) + "]";
          }
        }
      }
    }

    return getFromGroup(mockData[moduleName + "." + actionName].groups);
  }

  addNewCard = (group, jsonPath) => {
    let self = this;
    group = JSON.parse(JSON.stringify(group));
    let {setMockData, mockData, metaData, moduleName, actionName} = this.props;
    //Increment the values of indexes
    var grp = _.get(metaData[moduleName + "." + actionName], self.getPath(jsonPath)+ '[0]');
    group = this.incrementIndexValue(grp, jsonPath);
    //Push to the path
    var updatedSpecs = this.getNewSpecs(group, JSON.parse(JSON.stringify(mockData)), self.getPath(jsonPath));
    //Create new mock data
    setMockData(updatedSpecs);
  }

  removeCard = (jsonPath, index) => {
    //Remove at that index and update upper array values
    let {mockData, setMockData, formData} = this.props;

  }

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors, isFormValid} = this.props;
    let {create, handleChange, getVal, addNewCard, removeCard, autoComHandler} = this;
    return (
      <div className="Report">
        <form onSubmit={(e) => {
          create(e)
        }}>
        {!_.isEmpty(mockData) && moduleName && actionName && <ShowFields
                                    groups={mockData[`${moduleName}.${actionName}`].groups}
                                    noCols={mockData[`${moduleName}.${actionName}`].numCols}
                                    ui="google"
                                    handler={handleChange}
                                    getVal={getVal}
                                    fieldErrors={fieldErrors}
                                    useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false}
                                    addNewCard={addNewCard}
                                    removeCard={removeCard}
                                    autoComHandler={autoComHandler}/>}
          <div style={{"textAlign": "center"}}>
            <br/>
            {actionName == "create" && <UiButton item={{"label": "Create", "uiType":"submit", "isDisabled": isFormValid ? false : true}} ui="google"/>}
            {actionName == "update" && <UiButton item={{"label": "Update", "uiType":"submit", "isDisabled": isFormValid ? false : true}} ui="google"/>}
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
  fieldErrors: state.frameworkForm.fieldErrors,
  isFormValid: state.frameworkForm.isFormValid
});

const mapDispatchToProps = dispatch => ({
  initForm: (requiredFields) => {
    dispatch({
      type: "SET_REQUIRED_FIELDS",
      requiredFields
    });
  },
  setMetaData: (metaData) => {
    dispatch({type:"SET_META_DATA", metaData})
  },
  setMockData: (mockData) => {
    dispatch({type: "SET_MOCK_DATA", mockData});
  },
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
  },
  setModuleName: (moduleName) => {
    dispatch({type:"SET_MODULE_NAME", moduleName})
  },
  setActionName: (actionName) => {
    dispatch({type:"SET_ACTION_NAME", actionName})
  },
  handleChange: (e, property, isRequired, pattern, requiredErrMsg, patternErrMsg)=>{
    dispatch({type:"HANDLE_CHANGE_FRAMEWORK", property,value: e.target.value, isRequired, pattern, requiredErrMsg, patternErrMsg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    console.log(toastMsg);
    console.log(isSuccess);
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  },
  setDropDownData:(fieldName,dropDownData)=>{
    dispatch({type:"SET_DROPDWON_DATA",fieldName,dropDownData})
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route})
});

export default connect(mapStateToProps, mapDispatchToProps)(Report);
