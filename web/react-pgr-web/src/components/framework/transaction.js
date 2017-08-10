import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "./showFields";

import {translate} from '../common/common';
import Api from '../../api/api';
import UiButton from './components/UiButton';

import {fileUpload} from './utility/utility';
import UiTable from './components/UiTable';
import UiDynamicTable from './components/UiDynamicTable';

import jp from "jsonpath";
import $ from "jquery";


var specifications={};

let reqRequired = [];
class Transaction extends Component {
  state={
    pathname:""
  }
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
    return typeof _.get(this.props.formData, path) != "undefined" ? _.get(this.props.formData, path) : "";
  }

  initData() {

    let hashLocation = window.location.hash;
    try {
      var hash = window.location.hash.split("/");
      if(hash.length == 4 && hashLocation.split("/")[1]!="transaction") {
        specifications = require(`./specs/${hash[2]}/${hash[2]}`).default;
      } else if(hashLocation.split("/")[1]!="transaction"){
        specifications = require(`./specs/${hash[2]}/master/${hash[3]}`).default;
      } else {
        specifications = require(`./specs/${hash[2]}/transaction/${hash[3]}`).default;
      }
    } catch(e) {}
    let { setMetaData, setModuleName, setActionName, initForm, setMockData } = this.props;
    let obj = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
    reqRequired = [];
    this.setLabelAndReturnRequired(obj);
    initForm(reqRequired);
    setMetaData(specifications);
    setMockData(JSON.parse(JSON.stringify(specifications)));
    setModuleName(hashLocation.split("/")[2]);
    setActionName(hashLocation.split("/")[1]);

    this.setState({
      pathname:this.props.history.location.pathname
    })
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
      self.props.handleChange({target:{value:res.Bill}},"Receipt[0].Bill",false,false);
      self.props.handleChange({target:{value:localStorage.getItem("tenantId")}},"Receipt[0].tenantId",false,false);
      self.props.handleChange({target:{value:localStorage.getItem("tenantId")}},"Receipt[0].instrument.instrumentType.tenantId",false,false);
      self.props.handleChange({target:{value:localStorage.getItem("tenantId")}},"Receipt[0].instrument.bankAccount.tenantId",false,false);
      self.props.handleChange({target:{value:localStorage.getItem("tenantId")}},"Receipt[0].instrument.bank.tenantId",false,false);


      var resultList = {
        resultHeader: self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.header,
        resultValues: []
      };

      let objectPath=self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.resultPath;
      let tableObjectPath=self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.tableResultPath;
      var values = _.get(res,objectPath);
      // var specsValuesList = self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.values;
      let headers=Object.assign([],resultList.resultHeader);

      if(values && values.length) {
        for(var i=0; i<values.length; i++) {
          var tmp = [];
          for(var j=0; j<headers.length; j++) {
              // tmp.push({jsonPath:objectPath+"["+i+"]."+headers[j].jsonPath,...headers[j]})
            tmp.push(Object.assign({},headers[j],{jsonPath:tableObjectPath+"["+i+"]."+headers[j].jsonPath}));
            // tmp.push(_.get(values[i], specsValuesList[j]));
          }
          resultList.resultValues.push(tmp);
        }
      }


      console.log(resultList);

      // var specsValuesList = self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.values;
      // var values = _.get(res, self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.resultPath);
      // if(values && values.length) {
      //   for(var i=0; i<values.length; i++) {
      //     var tmp = [i+1];
      //     for(var j=0; j<specsValuesList.length; j++) {
      //       tmp.push(_.get(values[i], specsValuesList[j]));
      //     }
      //     resultList.resultValues.push(tmp);
      //   }
      // }
      // self.setState({
      //   resultList,
      //   values,
      //   showResult: true
      // });

      self.setState({
        resultList,
        showResult: true
      });

      self.props.setFlag(1);
    }, function(err) {
      self.props.toggleSnackbarAndSetText(true, err.message, false, true);
      self.props.setLoadingStatus('hide');
    })
  }

  getVal = (path) => {
    return _.get(this.props.formData, path) || "";
  }


    hideField = (_mockData, hideObject, reset) => {
      let {moduleName, actionName, setFormData} = this.props;
      let _formData = {...this.props.formData};
      if(hideObject.isField) {
        for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
          for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
            if(hideObject.name == _mockData[moduleName + "." + actionName].groups[i].fields[j].name) {
              _mockData[moduleName + "." + actionName].groups[i].fields[j].hide = reset ? false : true;
              if(!reset) {
                _.set(_formData, _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath, '');
                setFormData(_formData);
              }

              break;
            }
          }
        }
      } else {
        for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
          if(hideObject.name == _mockData[moduleName + "." + actionName].groups[i].name) {
            _mockData[moduleName + "." + actionName].groups[i].hide = reset ? false : true;
            break;
          }
        }
      }

      return _mockData;
    }

    showField = (_mockData, showObject, reset) => {
      let {moduleName, actionName, setFormData} = this.props;
      let _formData = {...this.props.formData};
      if(showObject.isField) {
        for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
          for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
            if(showObject.name == _mockData[moduleName + "." + actionName].groups[i].fields[j].name) {
              _mockData[moduleName + "." + actionName].groups[i].fields[j].hide = reset ? true : false;
              if(!reset) {
                _.set(_formData, _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath, '');
                setFormData(_formData);
              }
              break;
            }
          }
        }
      } else {
        for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
          if(showObject.name == _mockData[moduleName + "." + actionName].groups[i].name) {
            _mockData[moduleName + "." + actionName].groups[i].hide = reset ? true : false;
            break;
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

  rowClickHandler = (index) => {
    var value = this.state.values[index];
    var _url = window.location.hash.split("/").indexOf("update") > -1 ? this.props.metaData[`${this.props.moduleName}.${this.props.actionName}`].result.rowClickUrlUpdate : this.props.metaData[`${this.props.moduleName}.${this.props.actionName}`].result.rowClickUrlView;
    var key = _url.split("{")[1].split("}")[0];
    _url = _url.replace("{" + key + "}", _.get(value, key));
    this.props.setRoute(_url);
  }



  create=(e) => {
    let self=this;
    e.preventDefault();
    self.props.setLoadingStatus('loading');
    var formData = {...this.props.formData.Receipt};
    Api.commonApiPost("/collection-services/receipts/v1/_create", "", formData, "", true).then(function(response){
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

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors,isFormValid} = this.props;
    let {search, handleChange, getVal, addNewCard, removeCard, rowClickHandler,create} = this;
    let {showResult, resultList} = this.state;
    // showResult=true;

    console.log(isFormValid);


    console.log(formData);

    return (
      <div className="SearchResult">
        <form onSubmit={(e) => {
          search(e)
        }}>
        {!_.isEmpty(mockData) && <ShowFields groups={mockData[`${moduleName}.${actionName}`].groups} noCols={mockData[`${moduleName}.${actionName}`].numCols} ui="google" handler={handleChange} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false} addNewCard={""} removeCard={""}/>}
          <div style={{"textAlign": "center"}}>
            <br/>
            <UiButton item={{"label": "Search", "uiType":"submit","isDisabled": isFormValid ? false : true}} ui="google"/>
            <br/>
          </div>
          {/*showResult && <UiTable resultList={resultList} rowClickHandler={rowClickHandler}/>*/}

        </form>

                  {showResult && <UiDynamicTable resultList={resultList} ui="google"  handler={handleChange} getVal={getVal} fieldErrors={fieldErrors}/>}

                  <br/>
                  {showResult && !_.isEmpty(mockData) && <ShowFields groups={mockData[`${moduleName}.${actionName}`].transaction} noCols={mockData[`${moduleName}.${actionName}`].numCols} ui="google" handler={handleChange} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false} addNewCard={""} removeCard={""}/>}
                  <div style={{"textAlign": "center"}}>
                    <br/>
                  {showResult &&  <UiButton handler={create} item={{"label": "Pay", "uiType":"button"}} ui="google"/>}
                    <br/>
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
  fieldErrors: state.frameworkForm.fieldErrors,
  flag: state.report.flag,
  isFormValid: state.frameworkForm.isFormValid
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
    dispatch({type:"HANDLE_CHANGE_FRAMEWORK", property,value: e.target.value, isRequired, pattern, requiredErrMsg, patternErrMsg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route}),
  setFlag: (flag) => {
    dispatch({type:"SET_FLAG", flag})
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Transaction);


/*let res=
{"ResposneInfo":null,"Bill":[{"id":"3","payeeName":"M Sambasivudu","payeeAddress":null,"payeeEmail":null,"isActive":true,"isCancelled":false,"billDetails":[{"id":"3","tenantId":"default","bill":"3","businessService":"Property Tax","billNumber":"3","billDate":1502090587494,"consumerCode":"1016000001","consumerType":"PRIVATE","billDescription":"Property Tax Consumer Code: 1016000001","displayMessage":"Property Tax Consumer Code: 1016000001","minimumAmount":4.00,"totalAmount":3649.00,"collectionModesNotAllowed":[""],"callBackForApportioning":false,"partPaymentAllowed":true,"billAccountDetails":[{"id":"25","tenantId":"default","billDetail":"3","glcode":"3503002","order":1,"accountDescription":"EDU_CESS-1443637800000-1459448999000","crAmountToBePaid":288.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"ADVANCE_AMOUNT"},{"id":"26","tenantId":"default","billDetail":"3","glcode":"1100101","order":1,"accountDescription":"PT_TAX-1443637800000-1459448999000","crAmountToBePaid":439.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"ADVANCE_AMOUNT"},{"id":"27","tenantId":"default","billDetail":"3","glcode":"3503002","order":1,"accountDescription":"LIB_CESS-1443637800000-1459448999000","crAmountToBePaid":10.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"ADVANCE_AMOUNT"},{"id":"28","tenantId":"default","billDetail":"3","glcode":"3503002","order":1,"accountDescription":"EDU_CESS-1459449000000-1475260199000","crAmountToBePaid":288.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"ADVANCE_AMOUNT"},{"id":"29","tenantId":"default","billDetail":"3","glcode":"1100101","order":1,"accountDescription":"PT_TAX-1459449000000-1475260199000","crAmountToBePaid":439.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"ADVANCE_AMOUNT"},{"id":"30","tenantId":"default","billDetail":"3","glcode":"3503002","order":1,"accountDescription":"LIB_CESS-1459449000000-1475260199000","crAmountToBePaid":10.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"ADVANCE_AMOUNT"},{"id":"31","tenantId":"default","billDetail":"3","glcode":"3503002","order":1,"accountDescription":"EDU_CESS-1475260200000-1490984999000","crAmountToBePaid":288.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"ADVANCE_AMOUNT"},{"id":"32","tenantId":"default","billDetail":"3","glcode":"1100101","order":1,"accountDescription":"PT_TAX-1475260200000-1490984999000","crAmountToBePaid":439.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"ADVANCE_AMOUNT"},{"id":"33","tenantId":"default","billDetail":"3","glcode":"3503002","order":1,"accountDescription":"LIB_CESS-1475260200000-1490984999000","crAmountToBePaid":10.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"ADVANCE_AMOUNT"},{"id":"34","tenantId":"default","billDetail":"3","glcode":"3503002","order":1,"accountDescription":"EDU_CESS-1490985000000-1506796199000","crAmountToBePaid":288.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"CURRENT_AMOUNT"},{"id":"35","tenantId":"default","billDetail":"3","glcode":"1100101","order":1,"accountDescription":"PT_TAX-1490985000000-1506796199000","crAmountToBePaid":1126.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"CURRENT_AMOUNT"},{"id":"36","tenantId":"default","billDetail":"3","glcode":"3503002","order":1,"accountDescription":"LIB_CESS-1490985000000-1506796199000","crAmountToBePaid":24.00,"creditAmount":null,"debitAmount":null,"isActualDemand":true,"purpose":"CURRENT_AMOUNT"}]}],"tenantId":"default","auditDetail":null}]}*/
