import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import _ from "lodash";
import ShowFields from "../../framework/showFields";
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import {translate} from '../../common/common';
import Api from '../../../api/api';
import jp from "jsonpath";
import UiButton from '../../framework/components/UiButton';
import {fileUpload, getInitiatorPosition} from '../../framework/utility/utility';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import {Card, CardHeader, CardText, CardTitle} from 'material-ui/Card';
import $ from "jquery";
import jsPDF from 'jspdf';
import "jspdf-autotable";

var CONST_API_GET_FILE = "/filestore/v1/files/id";
var specifications={};
let reqRequired = [];
let baseUrl="https://raw.githubusercontent.com/abhiegov/test/master/specs/";

const defaultMat = {
	"name":"",
	"quantity":"",
	"size":"",
	"amountDetails":""
};

const generateEstNotice = function(connection, tenantInfo) {
	var doc = new jsPDF();

	doc.setFont("courier");
	doc.setFontType("bold");
	doc.setFontSize(20);
	doc.text(105, 10, (tenantInfo && tenantInfo.city && tenantInfo.city.name ? tenantInfo.city.name : "Roha Municipal Council") , null, null, 'center');
	doc.setFontSize(15);
	doc.setFontType("normal");
	doc.text(105, 20, 'Water Department', null, null, 'center');
	doc.text(105, 30, 'Letter Of Intimation', null, null, 'center');
	doc.setFontType("bold");
	doc.text(200, 40, 'Date: ________', null, null, 'right');
	doc.text(200, 50, 'No.: ________', null, null, 'right');
	doc.setFontType("bold");
	doc.setFontSize(20);
	doc.text(10, 60, "To,");
	doc.text(10, 70, "Applicant");
	doc.setFont("times");
	doc.setFontType("normal");
	doc.setFontSize(15);
	doc.text(10, 82, "Subject: Letter of Intimation for New Water Connection");
	doc.text(10, 90, "Reference: Application No: " + connection.consumerNumber + " and Application Date " + (connection.executionDate ? new Date(connection.executionDate) : ""));
	doc.text(10, 98, "Sir/Madam");
	doc.setFontType("bold");
	doc.text(35, 98, connection.property.nameOfApplicant + "  has applied for New Water Connection for Water No. ")
	doc.text(10, 106, "Water No. " + connection.consumerNumber + ". Requested to New Water Connection has been approved. Kindly pay the");
	doc.text(10, 114, "charges which are mentioned below within __ days.")
	doc.text(10, 122, "If not paid Application will be rejected or Penalty will be levied.");
	doc.text(10, 140, "Road Cutting Charges:    " + connection.estimationCharge[0].roadCutCharges);
	doc.text(10, 148, "Security Charges:             " + connection.estimationCharge[0].specialSecurityCharges);
	doc.text(10, 156, "Supervision Charges:       " + connection.estimationCharge[0].supervisionCharges);
	doc.setFontType("normal");
	doc.text(200, 190, 'Signing Authority', null, null, 'right');
	//doc.text(182, 198, 'अधिक्', null, null, 'right');
	doc.setFontType("bold");
	doc.setFontSize(20);
	doc.text(163, 210, (tenantInfo && tenantInfo.city && tenantInfo.city.name ? tenantInfo.city.name : "Roha Municipal Council"), null, null, 'center');
	doc.save("SN" + connection.consumerNumber + ".pdf");
}

const generateWO = function(connection, tenantInfo) {
	var doc = new jsPDF();

	doc.setFont("courier");
	doc.setFontType("bold");
	doc.setFontSize(20);
	doc.text(105, 10, (tenantInfo && tenantInfo.city && tenantInfo.city.name ? tenantInfo.city.name : "Roha Municipal Council"), null, null, 'center');
	doc.setFontSize(15);
	doc.setFontType("normal");
	doc.text(105, 20, 'Water Department', null, null, 'center');
	doc.text(105, 30, 'Letter Of Intimation', null, null, 'center');
	doc.setFontType("bold");
	doc.text(200, 40, 'Date: ________', null, null, 'right');
	doc.text(200, 50, 'No.: ________', null, null, 'right');
	doc.setFontType("bold");
	doc.setFontSize(20);
	doc.text(10, 60, "To,");
	doc.text(10, 70, "Applicant");
	doc.setFont("times");
	doc.setFontType("normal");
	doc.setFontSize(15);
	doc.text(10, 82, "Subject: Approval Order");
	doc.text(10, 90, "Reference: Application No: " + connection.consumerNumber + " and Application Date " + (connection.executionDate ? new Date(connection.executionDate) : ""));
	doc.text(10, 98, "Sir/Madam");
	doc.setFontType("bold");
	doc.text(35, 98, connection.property.nameOfApplicant + " has applied for New <Service name> has been approved.")
	doc.text(10, 106, connection.plumberName + " assigned for the work.");
	doc.text(10, 114, "Allotted Water Connection No. " + connection.consumerNumber)

	doc.setFontType("normal");
	doc.text(200, 190, 'Signing Authority', null, null, 'right');
	doc.setFontType("bold");
	doc.setFontSize(20);
	doc.text(163, 210, ' Roha Muncipal Council', null, null, 'center');
	doc.save("SN" + connection.consumerNumber + ".pdf");
}

class Report extends Component {
  state={
    pathname:""
  }
  constructor(props) {
    super(props);
    this.state = {
    	workflow: [],
    	buttons: [],
    	departments: [],
    	designations: [],
    	employees: [],
    	initiatorPosition: "",
    	hide: false,
    	disable: false
    };
  }

  setLabelAndReturnRequired(configObject) {
    if(configObject && configObject.groups) {
      for(var i=0;configObject && i<configObject.groups.length; i++) {
        configObject.groups[i].label = translate(configObject.groups[i].label);
        for (var j = 0; j < configObject.groups[i].fields.length; j++) {
              configObject.groups[i].fields[j].label = translate(configObject.groups[i].fields[j].label);
              if (configObject.groups[i].fields[j].isRequired && !configObject.groups[i].fields[j].hide && !configObject.groups[i].hide)
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
        if(typeof groups[i].fields[j].defaultValue == 'string' || typeof groups[i].fields[j].defaultValue == 'number' || typeof groups[i].fields[j].defaultValue == 'boolean') {
          //console.log(groups[i].fields[j].name + "--" + groups[i].fields[j].defaultValue);
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

  setInitialUpdateChildData(form, children) {
    let _form = JSON.parse(JSON.stringify(form));
    for(var i=0; i<children.length; i++) {
      for(var j=0; j<children[i].groups.length; j++) {
        if(children[i].groups[j].multiple) {
          var arr = _.get(_form, children[i].groups[j].jsonPath);
          var ind = j;
          var _stringifiedGroup = JSON.stringify(children[i].groups[j]);
          var regex = new RegExp(children[i].groups[j].jsonPath.replace(/\[/g, "\\[").replace(/\]/g, "\\]") + "\\[\\d{1}\\]", 'g');
          for(var k=1; k < arr.length; k++) {
            j++;
            children[i].groups[j].groups.splice(ind+1, 0, JSON.parse(_stringifiedGroup.replace(regex, children[i].groups[ind].jsonPath + "[" + k + "]")));
            children[i].groups[j].groups[ind+1].index = ind+1;
          }
        }

        if(children[i].groups[j].children && children[i].groups[j].children.length) {
          this.setInitialUpdateChildData(form, children[i].groups[j].children);
        }
      }
    }
  }

  setInitialUpdateData(form, specs, moduleName, actionName, objectName) {
    let {setMockData} = this.props;
    let _form = JSON.parse(JSON.stringify(form));
    var ind;
    for(var i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
      if(specs[moduleName + "." + actionName].groups[i].multiple) {
        var arr = _.get(_form, specs[moduleName + "." + actionName].groups[i].jsonPath);
        ind = i;
        var _stringifiedGroup = JSON.stringify(specs[moduleName + "." + actionName].groups[i]);
        var regex = new RegExp(specs[moduleName + "." + actionName].groups[i].jsonPath.replace(/\[/g, "\\[").replace(/\]/g, "\\]") + "\\[\\d{1}\\]", 'g');
        for(var j=1; j < arr.length; j++) {
          i++;
          specs[moduleName + "." + actionName].groups.splice(ind+1, 0, JSON.parse(_stringifiedGroup.replace(regex, specs[moduleName + "." + actionName].groups[ind].jsonPath + "[" + j + "]")));
          specs[moduleName + "." + actionName].groups[ind+1].index = j;
        }
      }

      if(specs[moduleName + "." + actionName].groups[ind || i].children && specs[moduleName + "." + actionName].groups[ind || i].children.length) {
        this.setInitialUpdateChildData(form, specs[moduleName + "." + actionName].groups[ind || i].children);
      }
    }

    setMockData(specs);
  }

  displayUI(results) {
    let { setMetaData, setModuleName, setActionName, initForm, setMockData, setFormData } = this.props;
    let hashLocation = window.location.hash;
    let self = this;

    specifications =typeof(results)=="string" ? JSON.parse(results) : results;
    let obj = specifications["wc.create"];
    reqRequired = [];
    self.setLabelAndReturnRequired(obj);
    initForm(reqRequired);
    setMetaData(specifications);
    setMockData(JSON.parse(JSON.stringify(specifications)));
    setModuleName("wc");
    setActionName("create");

    this.setState({
      pathname:this.props.history.location.pathname
    })

    //Get connection and set form data
    Api.commonApiPost("/wcms-connection/connection/_search", {"stateId": self.props.match.params.stateId}, {}, null, true).then(function(res){
    	if(res.Connection[0].property.propertyTypeId) {
    		Api.commonApiPost("/pt-property/property/propertytypes/_search", {"id": res.Connection[0].property.propertyTypeId}, {}, null, true).then(function(res4){
    			if(res4 && res4.propertyTypes && res4.propertyTypes.length) {
    				for(var i=0; i<res4.propertyTypes.length; i++) {
    					if(res4.propertyTypes[i].id == res.Connection[0].property.propertyTypeId) {
    						res.Connection[0].property.propertyType = res4.propertyTypes[i].name;
    						//Fetch category type
				    		Api.commonApiPost("/wcms/masters/propertytype-categorytype/_search", {propertyTypeName: res.Connection[0].property.propertyType}, {}, null, true).then(function(res){
				    			  let keys=jp.query(res, "$..categoryTypeName");
				                  let values=jp.query(res, "$..categoryTypeName");
				                  let dropDownData=[];
				                  for (var k = 0; k < keys.length; k++) {
				                      let obj={};
				                      obj["key"]=keys[k];
				                      obj["value"]=values[k];
				                      dropDownData.push(obj);
				                  }
				                  self.props.setDropDownData("Connection[0].categoryType", dropDownData);
				    		}, function(err) {

				    		})

				    		Api.commonApiPost("/wcms/masters/propertytype-usagetype/_search", {propertyTypeName: res.Connection[0].property.propertyType}, {}, null, true).then(function(res){
				    			  let keys=jp.query(res, "$..usageCode");
				                  let values=jp.query(res, "$..usageType");
				                  let dropDownData=[];
				                  for (var k = 0; k < keys.length; k++) {
				                      let obj={};
				                      obj["key"]=keys[k];
				                      obj["value"]=values[k];
				                      dropDownData.push(obj);
				                  }
				                  self.props.setDropDownData("Connection[0].property.usageType", dropDownData);
				    		}, function(err) {

				    		})

				    		Api.commonApiPost("/wcms/masters/propertytype-pipesize/_search", {propertyTypeName: res.Connection[0].property.propertyType}, {}, null, true).then(function(res){
				    			let keys=jp.query(res, "$..pipeSize");
				                  let values=jp.query(res, "$..pipeSize");
				                  let dropDownData=[];
				                  for (var k = 0; k < keys.length; k++) {
				                      let obj={};
				                      obj["key"]=keys[k];
				                      obj["value"]=values[k];
				                      dropDownData.push(obj);
				                  }
				                  self.props.setDropDownData("Connection[0].hscPipeSizeType", dropDownData);
				    		}, function(err) {

				    		})
    						break;
    					}
    				}
    			}
    		}, function(err) {

    		})
    	}


    	if(res.Connection[0].property.usageTypeId) {
    		Api.commonApiPost("/wcms/masters/propertytype-usagetype/_search", {id: res.Connection[0].property.usageTypeId}, {}, null, true).then(function(res5){
    			if(res5 && res5.propertyTypeUsageTypes && res5.propertyTypeUsageTypes.length) {
    				for(var i=0; i<res5.propertyTypeUsageTypes.length; i++) {
    					if(res5.propertyTypeUsageTypes[i].id == res.Connection[0].property.usageTypeId) {
    						res.Connection[0].property.usageType = res5.propertyTypeUsageTypes[i].usageType;
    						Api.commonApiPost("/pt-property/property/usages/_search", {parent: res.Connection[0].property.usageType}, {}, null, true).then(function(res){
				    			let keys=jp.query(res, "$..code");
				                  let values=jp.query(res, "$..name");
				                  let dropDownData=[];
				                  for (var k = 0; k < keys.length; k++) {
				                      let obj={};
				                      obj["key"]=keys[k];
				                      obj["value"]=values[k];
				                      dropDownData.push(obj);
				                  }
				                  self.props.setDropDownData("Connection[0].subUsageType", dropDownData);
				    		}, function(err) {

				    		})
    						break;9
    					}
    				}
    			}
    		}, function(err) {

    		})
    	}

    	res.Connection[0].estimationCharge = [{
	    		"estimationCharges": 0,
		        "supervisionCharges": 0,
		        "roadCutCharges": 0,
		        "specialSecurityCharges": 0,
		        "existingDistributionPipeline": 0,
		        "pipelineToHomeDistance": 0,
		        "materials":[{...defaultMat}]
    		}
    	];
    	self.props.setFormData(res);
    }, function(err) {

    })

    //Fetch workflow
    Api.commonApiPost("egov-common-workflows/history", {workflowId: self.props.match.params.stateId}, {}, null, true).then(function(res) {
    	self.setState({
    		workflow: res.tasks
    	})
    }, function(err) {

    })

    //Fetch buttons
    Api.commonApiPost("egov-common-workflows/process/_search", {id: self.props.match.params.stateId}, {}, null, false).then(function(res) {
    	if(res && res.processInstance && res.processInstance.attributes && res.processInstance.attributes.validActions && res.processInstance.attributes.validActions.values && res.processInstance.attributes.validActions.values.length) {
    		var flg = 0;
    		for(var j=0; j<res.processInstance.attributes.validActions.values.length; j++) {
    			if(res.processInstance.attributes.validActions.values[j].key.toLowerCase() == "forward" || res.processInstance.attributes.validActions.values[j].key.toLowerCase() == "submit") {
    				flg = 1;
    			}
    		}

    		self.setState({
    			buttons: res.processInstance.attributes.validActions.values,
    			hide: flg == 1 ? false : true,
    			disable: flg == 1 ? false : true
    		})

    		if(flg == 0) {
    			for(var i=0; i<specifications["wc.create"].groups.length; i++) {
    				for(var j=0; j<specifications["wc.create"].groups[i].fields.length; j++) {
    					specifications["wc.create"].groups[i].fields[j].isDisabled = true;
    				}
    			}

    			setMockData(specifications);
    		}
    	} else {

    	}

    	if(res && res.processInstance) {
    		Api.commonApiPost("/egov-common-workflows/designations/_search", {
    			businessKey: "WaterConnection",
    			approvalDepartmentName: "",
    			departmentRule: "",
    			currentStatus: res.processInstance.status,
    			additionalRule: "",
    			pendingAction: "",
    			designation: "",
    			amountRule: ""
    		}, {}, null, false).then(function(res3){
    			if(res3 && res3.length) {
    				var count = res3.length;
    				for(let i=0; i<res3.length; i++) {
    					Api.commonApiPost("/hr-masters/designations/_search", {
		    				name: res3[i].name
		    			}, {}, null, false).then(function(res2){
		    				res3[i].id = res2.Designation && res2.Designation[0] ? res2.Designation[0].id : "-"
		    				count--;
		    				if(count == 0) {
		    					self.setState({
				    				designations: res3,
				    				initiatorPosition: res.processInstance.initiatorPosition,
				    				status: res.processInstance.status
				    			})
		    				}
		    			}, function(err) {

		    			})
    				}
    			}
		    }, function(err) {

		    })
    	}

    }, function(err) {

    })

    Api.commonApiPost("egov-common-masters/departments/_search", {}, {}, null, false).then(function(res){
    	self.setState({
    		departments: res.Department
    	})
    }, function(err) {

    })
  }

  getEmployee = () => {
  	let self = this;
  	if(this.props.formData.Connection[0].workflowDetails.department && this.props.formData.Connection[0].workflowDetails.designation) {
  		Api.commonApiPost("hr-employee/employees/_search", {
  			departmentId: this.props.formData.Connection[0].workflowDetails.department,
  			designationId: this.props.formData.Connection[0].workflowDetails.designation
  		}, {}, null, false).then(function(res) {
  			self.setState({
  				Employee: res.Employee
  			})
  		}, function(err) {

  		})
  	}
  }

  initData() {
    var hash = window.location.hash.split("/");
    let endPoint="";
    let self = this;

    specifications = require("../../framework/specs/wc/others/workflow").default;
	self.displayUI(specifications);

  }

  componentDidMount() {
      this.initData();
  }

  componentWillReceiveProps(nextProps) {
    if (this.state.pathname!=nextProps.history.location.pathname) {
      this.initData();
    }
  }

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
    //return console.log(formData);
    Api.commonApiPost((url || self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].url), "", formData, "", true).then(function(response){
      self.props.setLoadingStatus('hide');
      self.initData();
      self.props.toggleSnackbarAndSetText(true, translate(self.props.actionName == "create" ? "wc.create.message.success" : "wc.update.message.success"), true);
      setTimeout(function() {
        if(self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].idJsonPath) {
          if (self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].ackUrl) {
              var hash = self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].ackUrl + "/" + encodeURIComponent(_.get(response, self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].idJsonPath));
          } else {
            if(self.props.actionName == "update") {
              var hash = window.location.hash.replace(/(\#\/create\/|\#\/update\/)/, "/view/");
            } else {
              var hash = window.location.hash.replace(/(\#\/create\/|\#\/update\/)/, "/view/") + "/" + _.get(response, self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].idJsonPath);
            }
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

  getVal = (path, dateBool) => {
    var _val = _.get(this.props.formData, path);
    if(dateBool && typeof _val == 'string' && _val && _val.indexOf("-") > -1) {
      var _date = _val.split("-");
      return new Date(_date[0], (Number(_date[1])-1), _date[2]);
    }

    return typeof _val != "undefined" ? _val : "";
  }

  hideField = (_mockData, hideObject, reset) => {
    let {moduleName, actionName, setFormData, delRequiredFields, removeFieldErrors, addRequiredFields} = this.props;
    let _formData = {...this.props.formData};
    if(hideObject.isField) {
      for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(hideObject.name == _mockData[moduleName + "." + actionName].groups[i].fields[j].name) {
            _mockData[moduleName + "." + actionName].groups[i].fields[j].hide = reset ? false : true;
            if(!reset) {
              _.set(_formData, _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath, '');
              setFormData(_formData);
              //Check if required is true, if yes remove from required fields
              if(_mockData[moduleName + "." + actionName].groups[i].fields[j].isRequired) {
                delRequiredFields([_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath]);
                removeFieldErrors(_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath);
              }
            } else if(_mockData[moduleName + "." + actionName].groups[i].fields[j].isRequired) {
              addRequiredFields([_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath]);
            }

            break;
          }
        }
      }
    } else {
      let flag = 0;
      for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
        if(hideObject.name == _mockData[moduleName + "." + actionName].groups[i].name) {
          flag = 1;
          _mockData[moduleName + "." + actionName].groups[i].hide = reset ? false : true;
          if(!reset) {
            var _rReq = [];
            for(var j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
              _.set(_formData, _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath, '');
              if(_mockData[moduleName + "." + actionName].groups[i].fields[j].isRequired) {
                _rReq.push(_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath);
                removeFieldErrors(_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath);
              }
            }
            delRequiredFields(_rReq);
            setFormData(_formData);
          } else {
            var _rReq = [];
            for(var j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
              if(_mockData[moduleName + "." + actionName].groups[i].fields[j].isRequired)
                _rReq.push(_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath);
            }
            addRequiredFields(_rReq);
          }
          break;
        }
      }

      if(flag == 0) {
        for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
          if(_mockData[moduleName + "." + actionName].groups[i].children && _mockData[moduleName + "." + actionName].groups[i].children.length) {
            for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].children.length; j++) {
              for(let k=0; k<_mockData[moduleName + "." + actionName].groups[i].children[j].groups.length; k++) {
                if(hideObject.name == _mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].name) {
                  _mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].hide = reset ? false : true;
                  if(!reset) {
                    var _rReq = [];
                    for(let a=0; a<_mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].fields.length; a++) {
                      _.set(_formData, _mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].fields[a].jsonPath, '');
                      if(_mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].fields[a].isRequired) {
                        _rReq.push(_mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].fields[a].jsonPath);
                        removeFieldErrors(_mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].fields[a].jsonPath);
                      }
                    }
                    delRequiredFields(_rReq);
                    setFormData(_formData);
                  } else {
                    var _rReq = [];
                    for(let a=0; a<_mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].fields.length; a++) {
                      if(_mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].fields[a].isRequired)
                        _rReq.push(_mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].fields[a].jsonPath);
                    }
                    addRequiredFields(_rReq);
                  }
                }
              }
            }
          }
        }
      }
    }

    return _mockData;
  }

  showField = (_mockData, showObject, reset) => {
    let {moduleName, actionName, setFormData, delRequiredFields, removeFieldErrors, addRequiredFields} = this.props;
    let _formData = {...this.props.formData};
    if(showObject.isField) {
      for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(showObject.name == _mockData[moduleName + "." + actionName].groups[i].fields[j].name) {
            _mockData[moduleName + "." + actionName].groups[i].fields[j].hide = reset ? true : false;
            if(!reset) {
              _.set(_formData, _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath, '');
              setFormData(_formData);
              if(_mockData[moduleName + "." + actionName].groups[i].fields[j].isRequired) {
                addRequiredFields([_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath]);
              }
            } else if(_mockData[moduleName + "." + actionName].groups[i].fields[j].isRequired) {
              delRequiredFields([_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath]);
              removeFieldErrors(_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath);
            }
            break;
          }
        }
      }
    } else {
      let flag = 0;
      for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
        if(showObject.name == _mockData[moduleName + "." + actionName].groups[i].name) {
          flag = 1;
          _mockData[moduleName + "." + actionName].groups[i].hide = reset ? true : false;
          if(!reset) {
            var _rReq = [];
            for(var j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
              _.set(_formData, _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath, '');
              if(_mockData[moduleName + "." + actionName].groups[i].fields[j].isRequired)
                _rReq.push(_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath);
            }

            addRequiredFields(_rReq);
            setFormData(_formData);
          } else {
            var _rReq = [];
            for(var j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
              if(_mockData[moduleName + "." + actionName].groups[i].fields[j].isRequired) {
                _rReq.push(_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath);
                removeFieldErrors(_mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath);
              }
            }
            delRequiredFields(_rReq);
          }
          break;
        }
      }

      if(flag == 0) {
        for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
          if(_mockData[moduleName + "." + actionName].groups[i].children && _mockData[moduleName + "." + actionName].groups[i].children.length) {
            for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].children.length; j++) {
              for(let k=0; k<_mockData[moduleName + "." + actionName].groups[i].children[j].groups.length; k++) {
                if(showObject.name == _mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].name) {
                  _mockData[moduleName + "." + actionName].groups[i].children[j].groups[k].hide = reset ? true : false;
                  /*if(!reset) {

                  } else {

                  }*/
                }
              }
            }
          }
        }
      }
    }

    return _mockData;
  }

  enField = (_mockData, enableStr, reset) => {
    let {moduleName, actionName, setFormData} = this.props;
    let _formData = {...this.props.formData};
    for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
      for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
        if(enableStr == _mockData[moduleName + "." + actionName].groups[i].fields[j].name) {
          _mockData[moduleName + "." + actionName].groups[i].fields[j].isDisabled = reset ? true : false;
          if(!reset) {
            _.set(_formData, _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath, '');
            setFormData(_formData);
          }
          break;
        }
      }
    }

    return _mockData;
  }

  disField = (_mockData, disableStr, reset) => {
    let {moduleName, actionName, setFormData} = this.props;
    let _formData = {...this.props.formData};
    for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
      for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
        if(disableStr == _mockData[moduleName + "." + actionName].groups[i].fields[j].name) {
          _mockData[moduleName + "." + actionName].groups[i].fields[j].isDisabled = reset ? false : true;
          if(!reset) {
            _.set(_formData, _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath, '');
            setFormData(_formData);
          }

          break;
        }
      }
    }

    return _mockData;
  }

  checkIfHasEnDisFields = (jsonPath, val) => {
    let _mockData = {...this.props.mockData};
    let {moduleName, actionName, setMockData} = this.props;
    for(let i=0; i<_mockData[moduleName + "." + actionName].groups.length; i++) {
      for(let j=0; j<_mockData[moduleName + "." + actionName].groups[i].fields.length; j++) {
        if(jsonPath == _mockData[moduleName + "." + actionName].groups[i].fields[j].jsonPath && _mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields && _mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields.length) {
          for(let k=0; k<_mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields.length; k++) {
            if(val == _mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields[k].ifValue) {
              for(let y=0; y<_mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields[k].disable.length; y++) {
                _mockData = this.disField(_mockData, _mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields[k].disable[y]);
              }

              for(let z=0; z<_mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields[k].enable.length; z++) {
                _mockData = this.enField(_mockData, _mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields[k].enable[z]);
              }
            } else {
              for(let y=0; y<_mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields[k].disable.length; y++) {
                _mockData = this.disField(_mockData, _mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields[k].disable[y], true);
              }

              for(let z=0; z<_mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields[k].enable.length; z++) {
                _mockData = this.enField(_mockData, _mockData[moduleName + "." + actionName].groups[i].fields[j].enableDisableFields[k].enable[z], true);
              }
            }
          }
        }
      }
    }

    setMockData(_mockData);
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

  handleChange = (e, property, isRequired, pattern, requiredErrMsg="Required", patternErrMsg="Pattern Missmatch") => {
      let {getVal} = this;
      let {handleChange,mockData,setDropDownData} = this.props;
      let hashLocation = window.location.hash;
      let obj = specifications["wc.create"];
      // console.log(obj);
      let depedants=jp.query(obj,`$.groups..fields[?(@.jsonPath=="${property}")].depedants.*`);
      this.checkIfHasShowHideFields(property, e.target.value);
      this.checkIfHasEnDisFields(property, e.target.value);
      handleChange(e,property, isRequired, pattern, requiredErrMsg, patternErrMsg);

      if(property == "Connection[0].workflowDetails.department" || property == "Connection[0].workflowDetails.designation") {
      	this.getEmployee();
      }
      _.forEach(depedants, function(value, key) {
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
        if(groups[i].children) {
          for(var j=0; j<groups[i].children.length; i++) {
            if(groups[i].children[j].jsonPath == value) {
              return "groups[" + i + "].children[" + j + "].groups";
            } else {
              return "groups[" + i + "].children[" + j + "][" + getFromGroup(groups[i].children[j].groups) + "]";
            }
          }
        }
      }
    }

    return getFromGroup(mockData[moduleName + "." + actionName].groups);
  }

  addNewCard = (group, jsonPath, groupName) => {
    let self = this;
    let {setMockData, metaData, moduleName, actionName, setFormData, formData} = this.props;
    let mockData = {...this.props.mockData};
    if(!jsonPath) {
      for(var i=0; i<metaData[moduleName + "." + actionName].groups.length; i++) {
        if(groupName == metaData[moduleName + "." + actionName].groups[i].name) {
          var _groupToBeInserted = {...metaData[moduleName + "." + actionName].groups[i]};
          for(var j=(mockData[moduleName + "." + actionName].groups.length-1); j>=0; j--) {
            if(groupName == mockData[moduleName + "." + actionName].groups[j].name) {
              var regexp = new RegExp(mockData[moduleName + "." + actionName].groups[j].jsonPath.replace(/\[/g, "\\[").replace(/\]/g, "\\]") + "\\[\\d{1}\\]", "g");
              var stringified = JSON.stringify(_groupToBeInserted);
              var ind = mockData[moduleName + "." + actionName].groups[j].index || 0;
              //console.log(ind);
              _groupToBeInserted = JSON.parse(stringified.replace(regexp, mockData[moduleName + "." + actionName].groups[i].jsonPath + "[" + (ind+1) + "]"));
              _groupToBeInserted.index = ind+1;
              mockData[moduleName + "." + actionName].groups.splice(j+1, 0, _groupToBeInserted);
              //console.log(mockData[moduleName + "." + actionName].groups);
              setMockData(mockData);
              var temp = {...formData};
              self.setDefaultValues(mockData[moduleName + "." + actionName].groups, temp);
              setFormData(temp);
              break;
            }
          }
          break;
        }
      }
    } else {
      group = JSON.parse(JSON.stringify(group));
      //Increment the values of indexes
      var grp = _.get(metaData[moduleName + "." + actionName], self.getPath(jsonPath)+ '[0]');
      group = this.incrementIndexValue(grp, jsonPath);
      //Push to the path
      var updatedSpecs = this.getNewSpecs(group, JSON.parse(JSON.stringify(mockData)), self.getPath(jsonPath));
      //Create new mock data
      setMockData(updatedSpecs);
    }
  }

  removeCard = (jsonPath, index, groupName) => {
    //Remove at that index and update upper array values
    let {setMockData, moduleName, actionName, setFormData} = this.props;
    let _formData = {...this.props.formData};
    let self = this;
    let mockData = {...this.props.mockData};

    if(!jsonPath) {
      var ind = 0;
      for(let i=0; i<mockData[moduleName + "." + actionName].groups.length; i++) {
        if(index == i && groupName == mockData[moduleName + "." + actionName].groups[i].name) {
          mockData[moduleName + "." + actionName].groups.splice(i, 1);
          ind = i;
          break;
        }
      }

      for(let i=ind; i<mockData[moduleName + "." + actionName].groups.length; i++) {
        if(mockData[moduleName + "." + actionName].groups[i].name == groupName) {
          var regexp = new RegExp(mockData[moduleName + "." + actionName].groups[i].jsonPath.replace(/\[/g, "\\[").replace(/\]/g, "\\]") + "\\[\\d{1}\\]", "g");
          //console.log(regexp);
          //console.log(mockData[moduleName + "." + actionName].groups[i].index);
          //console.log(mockData[moduleName + "." + actionName].groups[i].index);
          var stringified = JSON.stringify(mockData[moduleName + "." + actionName].groups[i]);
          mockData[moduleName + "." + actionName].groups[i] = JSON.parse(stringified.replace(regexp, mockData[moduleName + "." + actionName].groups[i].jsonPath + "[" + (mockData[moduleName + "." + actionName].groups[i].index-1) + "]"));

          if(_.get(_formData, mockData[moduleName + "." + actionName].groups[i].jsonPath)) {
            var grps = [..._.get(_formData, mockData[moduleName + "." + actionName].groups[i].jsonPath)];
            //console.log(mockData[moduleName + "." + actionName].groups[i].index-1);
            grps.splice((mockData[moduleName + "." + actionName].groups[i].index-1), 1);
            //console.log(grps);
            _.set(_formData, mockData[moduleName + "." + actionName].groups[i].jsonPath, grps);
            //console.log(_formData);
            setFormData(_formData);
          }
        }
      }
      //console.log(mockData[moduleName + "." + actionName].groups);
      setMockData(mockData);
    } else {
      var _groups = _.get(mockData[moduleName + "." + actionName], self.getPath(jsonPath));
      _groups.splice(index, 1);
      var regexp = new RegExp("\\[\\d{1}\\]", "g");
      for(var i=index; i<_groups.length; i++) {
        var stringified = JSON.stringify(_groups[i]);
        _groups[i] = JSON.parse(stringified.replace(regexp, "[" + i + "]"));
      }

      _.set(mockData, self.getPath(jsonPath), _groups);
      setMockData(mockData);
      }
  }

  addMaterial = (e) => {
  	e.preventDefault();
  	var formData = {...this.props.formData};
  	formData.Connection[0].estimationCharge[0].materials.push({...defaultMat});
  	this.props.setFormData(formData);
  }

  removeMaterial = (e, ind) => {
  	e.preventDefault();
  	var formData = {...this.props.formData};
  	formData.Connection[0].estimationCharge[0].materials.splice(ind, 1);
  	this.props.setFormData(formData);
  }

  getPosition = (id) => {
  	var tempEmploye = {};
    for (var i = 0; i < this.state.employees.length; i++) {
        if (this.state.employees[i].id == id) {
            tempEmploye = this.state.employees[i];
        }
    }

    if(tempEmploye && tempEmploye.assignments) {
        return tempEmploye.assignments[0].position;
    } else {
        return "";
    }
  }

  initiateWF = (action) => {
  	let self = this;
  	var formData = {...this.props.formData};
    if(!self.state.disable && (!formData.Connection[0].estimationCharge[0].materials[0].name || !formData.Connection[0].estimationCharge[0].roadCutCharges || !formData.Connection[0].estimationCharges[0].specialSecurityCharges)) {
      return self.props.toggleSnackbarAndSetText(true, translate("Please enter all mandatory fields."));
    }
    
  	if(action.key.toLowerCase() == "reject" && !formData.Connection[0].workflowDetails.comments) {
  		return self.props.toggleSnackbarAndSetText(true, translate("wc.create.workflow.comment"));
  	}

  	formData.Connection[0].workflowDetails.assignee = this.getPosition(formData.Connection[0].workflowDetails.assignee);
  	formData.Connection[0].workflowDetails.action = action.key;
  	formData.Connection[0].workflowDetails.status = this.state.status;

  	self.props.setLoadingStatus('loading');
  	Api.commonApiPost("/wcms-connection/connection/_update", {}, formData, null, true).then(function(res){
  		self.props.setLoadingStatus('hide');
  		if(action.key.toLowerCase() == "generate estimation notice") {
  			generateEstNotice(res.Connection[0], self.props.tenantInfo ? self.props.tenantInfo[0] : "");
  		} else if(action.key.toLowerCase() == "generate work order") {
  			generateWO(res.Connection[0], self.props.tenantInfo ? self.props.tenantInfo[0] : "");
  		}

  		setTimeout(function(){
  			self.props.setRoute("/view/wc/" + res.Connection[0].acknowledgementnumber);
  		}, 5000);

  	}, function(err){
  		self.props.setLoadingStatus('hide');
  		self.props.toggleSnackbarAndSetText(true, err.message, false, true);
  	})
  }

  calcAmt = (i) => {
  	if(this.props.formData.Connection[0].estimationCharge[0].materials[i].quantity && this.props.formData.Connection[0].estimationCharge[0].materials[i].size && this.props.formData.Connection[0].estimationCharge[0].materials[i].rate) {
  		var val = Number(this.props.formData.Connection[0].estimationCharge[0].materials[i].quantity) * Number(this.props.formData.Connection[0].estimationCharge[0].materials[i].size) * Number(this.props.formData.Connection[0].estimationCharge[0].materials[i].rate);
  		this.handleChange({target:{value: val}}, "Connection[0].estimationCharge[0].materials[" + i + "].amountDetails", false, "");
  		var sum = 0;
  		for(var j=0; j<this.props.formData.Connection[0].estimationCharge[0].materials.length; j++) {
  			if(this.props.formData.Connection[0].estimationCharge[0].materials[j].amountDetails)
  				sum += Number(this.props.formData.Connection[0].estimationCharge[0].materials[j].amountDetails);
  		}

  		this.handleChange({target: {value: sum}}, "Connection[0].estimationCharge[0].estimationCharges", false, "");
  		this.handleChange({target:{value: parseInt(sum*(15/100))}}, "Connection[0].estimationCharge[0].supervisionCharges", false, "");
  	}
  }

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors, isFormValid} = this.props;
    let {create, handleChange, getVal, addNewCard, removeCard, autoComHandler} = this;
    let self = this;
    const renderEstimateBody = function() {
    	{return formData && formData.Connection && formData.Connection[0] && formData.Connection[0].estimationCharge && formData.Connection[0].estimationCharge[0].materials && formData.Connection[0].estimationCharge[0].materials.map(function(v, i){
    		return (
    			<tr>
    				<td>
    					{i+1}
    				</td>
    				<td>
    					<TextField
    						disabled = {self.state.disable}
    						value={formData.Connection[0].estimationCharge[0].materials[i].name}
    						onChange={(e) => {
    							handleChange(e, "Connection[0].estimationCharge[0].materials[" + i + "].name", false, "")
    						}}/>
    				</td>
    				<td>
    					<TextField
    						disabled = {self.state.disable}
    						value={formData.Connection[0].estimationCharge[0].materials[i].quantity}
    						onChange={(e) => {
    							handleChange(e, "Connection[0].estimationCharge[0].materials[" + i + "].quantity", false, "")
    							self.calcAmt(i);
    						}}/>
    				</td>
    				<td>
    					<TextField
    						disabled = {self.state.disable}
    						value={formData.Connection[0].estimationCharge[0].materials[i].size}
    						onChange={(e) => {
    							handleChange(e, "Connection[0].estimationCharge[0].materials[" + i + "].size", false, "")
    							self.calcAmt(i);
    						}}/>
    				</td>
    				<td>
    					<TextField
    						disabled = {self.state.disable}
    						value={formData.Connection[0].estimationCharge[0].materials[i].rate}
    						onChange={(e) => {
    							handleChange(e, "Connection[0].estimationCharge[0].materials[" + i + "].rate", false, "")
    							self.calcAmt(i);
    						}}/>
    				</td>
    				<td>
    					<TextField
    						disabled = {self.state.disable}
    						value={formData.Connection[0].estimationCharge[0].materials[i].amountDetails}
    						disabled={true}
    						onChange={(e) => {
    							handleChange(e, "Connection[0].estimationCharge[0].materials[" + i + "].amountDetails", false, "")
    						}}/>
    				</td>
    				<td>
    					{(i == formData.Connection[0].estimationCharge[0].materials.length-1) && <span onClick={(e) => {self.addMaterial(e)}} className="glyphicon glyphicon-plus"></span>}
    					{(i < formData.Connection[0].estimationCharge[0].materials.length-1) && <span onClick={(e) => {self.removeMaterial(e)}} className="glyphicon glyphicon-trash"></span>}
    				</td>
    			</tr>
    		)
    	})}
    }

    const renderWorkflowHistory = function() {
    	{return self.state.workflow && self.state.workflow.map(function(v, i) {
    		return (
    			<tr key={i}>
    				<td>{v.createdDate}</td>
    				<td>{v.senderName}</td>
    				<td>{v.status}</td>
    				<td>{v.owner.name || "-"}</td>
    				<td>{v.comments}</td>
    			</tr>
    		)
    	})}
    }

    const renderFiles = function() {
    	{return formData && formData.Connection && formData.Connection[0] && formData.Connection[0].documents && formData.Connection[0].documents.length && formData.Connection[0].documents.map(function(v, i) {
    		return (
    			<tr key={i}>
    				<td>{i+1}</td>
    				<td>{v.name}</td>
    				<td><a href={window.location.origin + "/" + CONST_API_GET_FILE + "?tenantId=" + localStorage.tenantId + "&fileStoreId=" + v.fileStoreId} target="_blank">Download</a></td>
    			</tr>
    		)
    	})}
    }

    return (
      <div className="Report">
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
         <Card className="uiCard">
         	<CardHeader title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>{translate("employee.Employee.fields.documents")}</div>}/>
              <CardText>
              	<Table bordered responsive className="table-striped">
              		<thead>
              			<tr>
              				<th>#</th>
              				<th>{translate("employee.Employee.fields.User.name")}</th>
              				<th>{translate("employee.Assignment.fields.action")}</th>
              			</tr>
              		</thead>
              		<tbody>
              			{renderFiles()}
              		</tbody>
              	</Table>
              </CardText>
         </Card>

         <Card className="uiCard">
         	<CardHeader title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Field Inspection Details(Estimate)</div>}/>
         	<CardText>
         		<Table bordered responsive className="table-striped">
              		<thead>
              			<tr>
              				<th>#</th>
              				<th>{translate("wc.create.workflow.material")+"*"} </th>
              				<th>{translate("wc.create.workflow.quantity")}</th>
              				<th>{translate("tl.create.groups.feematrixtype.unitofmeasurement")}</th>
              				<th>{translate("wc.create.workflow.rate")}</th>
              				<th>{translate("tl.create.license.table.amount")}</th>
              				<th>{translate("employee.Assignment.fields.action")}</th>
              			</tr>
              		</thead>
              		<tbody>
              			{renderEstimateBody()}
              		</tbody>
              	</Table>
              	<br/>
              	<Grid>
              		<Row>
              			<Col xs="12" md="3">
              				<TextField
              					floatingLabelText={translate("wc.create.workflow.distributionPipeline") +"*"}
              					disabled = {self.state.disable}
              					type="number"
              					value={formData.Connection && formData.Connection[0] && formData.Connection[0].estimationCharge && formData.Connection[0].estimationCharge[0] ? formData.Connection[0].estimationCharge[0].existingDistributionPipeline : ""}
              					onChange={(e) => {

			                	}}/>
              			</Col>
              			<Col xs="12" md="3">
              				<TextField
              					floatingLabelText={translate("wc.create.workflow.homeDistance")+"*"}
              					disabled = {self.state.disable}
              					type="number"
              					value={formData.Connection && formData.Connection[0] && formData.Connection[0].estimationCharge && formData.Connection[0].estimationCharge[0] ? formData.Connection[0].estimationCharge[0].pipelineToHomeDistance : ""}
              					onChange={(e) => {

			                	}}/>
              			</Col>
              			<Col xs="12" md="3">
              				<TextField
              					floatingLabelText={translate("wc.create.workflow.supervisionCharge")}
              					disabled = {true}
              					type="number"
              					value={formData.Connection && formData.Connection[0] && formData.Connection[0].estimationCharge && formData.Connection[0].estimationCharge[0] ? formData.Connection[0].estimationCharge[0].supervisionCharges : ""}
              					onChange={(e) => {

			                	}}/>
              			</Col>
              			<Col xs="12" md="3">
              				<TextField
              					floatingLabelText={translate("wc.create.donation.subtitle")}
              					disabled = {self.state.disable}
              					type="number"
              					value={formData.Connection && formData.Connection[0] && formData.Connection[0].estimationCharge && formData.Connection[0].estimationCharge[0] ? formData.Connection[0].estimationCharge[0].specialSecurityCharges : ""}
              					onChange={(e) => {

			                	}}/>
              			</Col>
              		</Row>
              		<Row>
              			<Col xs="12" md="3">
              				<TextField
              					type="number"
              					floatingLabelText={translate("wc.create.workflow.roadCutCharges")}
              					disabled = {self.state.disable}
              					value={formData.Connection && formData.Connection[0] && formData.Connection[0].estimationCharge && formData.Connection[0].estimationCharge[0] ? formData.Connection[0].estimationCharge[0].roadCutCharges : ""}
              					onChange={(e) => {

			                	}}/>
              			</Col>
              		</Row>
              	</Grid>
         	</CardText>
         </Card>
         <Card className="uiCard">
         	<CardHeader title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>{translate("wc.create.workflow.applicationHistory")}</div>}/>
         	<CardText>
         		<Table bordered responsive className="table-striped">
              		<thead>
                		<tr>
                			<th>{translate("employee.ServiceHistory.fields.date")}</th>
                			<th>{translate("wc.create.workflow.UpdatedBy")}</th>
                			<th>{translate("collection.create.status")}</th>
                			<th>{translate("wc.create.workflow.currentOwner")}</th>
                			<th>{translate("reports.common.comments")}</th>
                		</tr>
               		</thead>
               		<tbody>
               			{renderWorkflowHistory()}
               		</tbody>
               </Table>
         	</CardText>
         </Card>
         <Card className="uiCard">
         	<CardHeader title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Workflow Details</div>}/>
         	<CardText>
         		<Row>
         			<Col xs={12} md={3}>
         				<SelectField dropDownMenuProps={{animated: true, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
		                  floatingLabelText={translate("employee.Assignment.fields.department")+" *"}
		                  value={formData.Connection && formData.Connection[0] && formData.Connection[0].workflowDetails ? formData.Connection[0].workflowDetails.department : ""}
		                  onChange={(event, key, value) => {
		                  		handleChange({target: {value}}, "Connection[0].workflowDetails.department", true, "")
		                  }}>
		                    {
		                      self.state.departments && self.state.departments.map(function(v, i){
		                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
		                      })
		                  }
		                </SelectField>
         			</Col>
         			<Col xs={12} md={3}>
         				<SelectField dropDownMenuProps={{animated: true, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
		                  floatingLabelText={translate("employee.Assignment.fields.designation")+"*"}
		                  value={formData.Connection && formData.Connection[0] && formData.Connection[0].workflowDetails ? formData.Connection[0].workflowDetails.designation : ""}
		                  onChange={(event, key, value) => {
		                  	handleChange({target: {value}}, "Connection[0].workflowDetails.designation", true, "")
		                  }}>
		                    {
		                      self.state.designations && self.state.designations.map(function(v, i){
		                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
		                      })
		                  	}
		                </SelectField>
         			</Col>
         			<Col xs={12} md={3}>
         				<SelectField dropDownMenuProps={{animated: true, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
		                  floatingLabelText={translate("wc.create.groups.approvalDetails.fields.approver")+"*"}
		                  value={formData.Connection && formData.Connection[0] && formData.Connection[0].workflowDetails ? formData.Connection[0].workflowDetails.assignee : ""}
		                  onChange={(event, key, value) => {
		                  	handleChange({target: {value}}, "Connection[0].workflowDetails.assignee", true, "")
		                  }}>
		                    {
		                      self.state.employees && self.state.employees.map(function(v, i){
		                        return (<MenuItem value={v.id} key={i} primaryText={v.name}/>)
		                      })
		                  	}
		                </SelectField>
         			</Col>
         		</Row>
         		<Row>
         			<Col xs={12} md={12}>
         				<TextField
          					type="text"
          					multiple={true}
          					fullWidth={true}
          					rows={3}
          					floatingLabelText={translate("wc.create.groups.approvalDetails.fields.comments")}
          					value={formData.Connection && formData.Connection[0] && formData.Connection[0].workflowDetails ? formData.Connection[0].workflowDetails.comments : ""}
          					onChange={(e) => {

		                	}}/>
         			</Col>
         		</Row>
         	</CardText>
         </Card>
         <br/>

         <div style={{"textAlign": "center"}}>
         	{ self.state.buttons && self.state.buttons.map(function(v, i){
         		return (<span><RaisedButton onClick={(e) => {self.initiateWF(v)}} label={v.name} primary={true}/>&nbsp;&nbsp;</span>)
         	})}
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
  isFormValid: state.frameworkForm.isFormValid,
  requiredFields: state.frameworkForm.requiredFields,
  tenantInfo: state.common.tenantInfo
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
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  },
  setDropDownData:(fieldName,dropDownData)=>{
    dispatch({type:"SET_DROPDWON_DATA",fieldName,dropDownData})
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route}),
  delRequiredFields: (requiredFields) => {
    dispatch({type: "DEL_REQUIRED_FIELDS", requiredFields})
  },
  addRequiredFields: (requiredFields) => {
    dispatch({type: "ADD_REQUIRED_FIELDS", requiredFields})
  },
  removeFieldErrors: (key) => {
    dispatch({type: "REMOVE_FROM_FIELD_ERRORS", key})
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(Report);
