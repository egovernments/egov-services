import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "./showFields";

import {translate} from '../common/common';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import Chip from 'material-ui/Chip';
import FontIcon from 'material-ui/FontIcon';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import Upload from 'material-ui-upload/Upload';
import FlatButton from 'material-ui/FlatButton';
import TextField from 'material-ui/TextField';
import {blue800, red500,white} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import Checkbox from 'material-ui/Checkbox';
import Api from '../../api/api';
import jp from "jsonpath";
import UiButton from './components/UiButton';
import {fileUpload} from './utility/utility';
import UiTable from './components/UiTable';
import Workflow from './specs/pt/Workflow';

var specifications={};

let reqRequired = [];

const styles = {
  errorStyle: {
    color: red500
  },
  underlineStyle: {
  
  },
  underlineFocusStyle: {
   
  },
  floatingLabelStyle: {
    color: "#354f57"
  },
  floatingLabelFocusStyle: {
    color:"#354f57"
  },
  customWidth: {
    width:100
  },
  checkbox: {
    marginBottom: 0,
    marginTop:15
  },
  uploadButton: {
   verticalAlign: 'middle',
 },
 uploadInput: {
   cursor: 'pointer',
   position: 'absolute',
   top: 0,
   bottom: 0,
   right: 0,
   left: 0,
   width: '100%',
   opacity: 0,
 },
 floatButtonMargin: {
   marginLeft: 20,
   fontSize:12,
   width:30,
   height:30
 },
 iconFont: {
   fontSize:17,
   cursor:'pointer'
 },
 radioButton: {
    marginBottom:0,
  },
actionWidth: {
  width:160
},
reducePadding: {
  paddingTop:4,
  paddingBottom:0
},
noPadding: {
  paddingBottom:0,
  paddingTop:0
},
noMargin: {
  marginBottom: 0
},
textRight: {
  textAlign:'right'
},
chip: {
  marginTop:4
}
};


function getPosition(objArray, id){
	
	if(id == '' || id == null) {
		return false;
	}

	for(var i = 0; i<objArray.length;i++){
		if(objArray[i].id == id){
			return objArray[i].assignments[0].position;
		}
	}
}

class Inbox extends Component {
  constructor(props) {
    super(props);
	this.state = {
		searchResult : [],
		buttons : [],
		employee : [],
		designation:[],
		workflowDepartment: [],
		process: []
		
	}
  }
  
  componentWillMount() {
	  localStorage.setItem('propertyId',  this.props.match.params.searchParam)
  }
  
   setLabelAndReturnRequired(configObject) {
    if(configObject && configObject.groups) {
      for(var i=0;configObject && i<configObject.groups.length; i++) {
        configObject.groups[i].label = translate(configObject.groups[i].label);
        for (var j = 0; j < configObject.groups[i].fields.length; j++) {
              configObject.groups[i].fields[j].label = translate(configObject.groups[i].fields[j].label);
        }

        if(configObject.groups[i].children && configObject.groups[i].children.length) {
          for(var k=0; k<configObject.groups[i].children.length; k++) {
            this.setLabelAndReturnRequired(configObject.groups[i].children[k]);
          }
        }
      }
    }
  }
  
   handleWorkFlowChange = (e, type) => {

    let currentThis = this;

    let query = {};

    let hasData = false;

    if(type == 'department' && e.target.value != '' && this.props.workflow.workflowDesignation) {
		  console.log(type);
		  query = {
			  departmentId: e.target.value,
			  designationId: this.props.workflow.workflowDesignation
		  }
		  hasData = true;

    } else if(type == 'designation' && e.target.value != '' && this.props.workflow.workflowDepartment) {
		  console.log(type);
		  query = {
			  departmentId: this.props.workflow.workflowDepartment,
			  designationId: e.target.value
		  }
          hasData = true;
    } else {
         hasData = false;
    }

    if(hasData){
        Api.commonApiPost( '/hr-employee/employees/_search', query).then((res)=>{
          currentThis.setState({approver: res.Employee})
      }).catch((err)=> {
        currentThis.setState({
          approver:[]
        })
        console.log(err);
      })
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
          var regex = new RegExp(children[i].groups[j].jsonPath.replace("[", "\[").replace("]", "\]") + "\\[\\d{1}\\]", 'g');
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

  hideField(specs, moduleName, actionName, hideObject) {
    if(hideObject.isField) {
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<specs[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(hideObject.name == specs[moduleName + "." + actionName].groups[i].fields[j].name) {
            specs[moduleName + "." + actionName].groups[i].fields[j].hide = true;
            break;
          }
        }
      }
    } else {
      let flag = 0;
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        if(hideObject.name == specs[moduleName + "." + actionName].groups[i].name) {
          flag = 1;
          specs[moduleName + "." + actionName].groups[i].hide = true;
          break;
        }
      }

      if(flag == 0) {
        for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
          if(specs[moduleName + "." + actionName].groups[i].children && specs[moduleName + "." + actionName].groups[i].children.length) {
            for(let j=0; j<specs[moduleName + "." + actionName].groups[i].children.length; j++) {
              for(let k=0; k<specs[moduleName + "." + actionName].groups[i].children[j].groups.length; k++) {
                if(hideObject.name == specs[moduleName + "." + actionName].groups[i].children[j].groups[k].name) {
                  specs[moduleName + "." + actionName].groups[i].children[j].groups[k].hide = true;
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  showField(specs, moduleName, actionName, showObject) {
    if(showObject.isField) {
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<specs[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(showObject.name == specs[moduleName + "." + actionName].groups[i].fields[j].name) {
            specs[moduleName + "." + actionName].groups[i].fields[j].hide = false;
            break;
          }
        }
      }
    } else {
      let flag = 0;
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        if(showObject.name == specs[moduleName + "." + actionName].groups[i].name) {
          flag = 1;
          specs[moduleName + "." + actionName].groups[i].hide = false;
          break;
        }
      }

      if(flag == 0) {
        for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
          if(specs[moduleName + "." + actionName].groups[i].children && specs[moduleName + "." + actionName].groups[i].children.length) {
            for(let j=0; j<specs[moduleName + "." + actionName].groups[i].children.length; j++) {
              for(let k=0; k<specs[moduleName + "." + actionName].groups[i].children[j].groups.length; k++) {
                if(showObject.name == specs[moduleName + "." + actionName].groups[i].children[j].groups[k].name) {
                  specs[moduleName + "." + actionName].groups[i].children[j].groups[k].hide = false;
                  break;
                }
              }
            }
          }
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
          specs[moduleName + "." + actionName].groups[ind+1].index = ind+1;
        }
      }

      for(var j=0; j<specs[moduleName + "." + actionName].groups[i].fields.length; j++) {
        if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields && specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields.length) {
          for(var k=0; k<specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields.length; k++) {
            if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].ifValue == _.get(form, specs[moduleName + "." + actionName].groups[i].fields[j].jsonPath)) {
              if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide && specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide.length) {
                for(var a=0; a<specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide.length; a++) {
                  this.hideField(specs, moduleName, actionName, specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide[a]);
                }
              }

              if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show && specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show.length) {
                for(var a=0; a<specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show.length; a++) {
                  this.showField(specs, moduleName, actionName, specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show[a]);
                }
              }
            }
          }
        }
      }

      if(specs[moduleName + "." + actionName].groups[ind || i].children && specs[moduleName + "." + actionName].groups[ind || i].children.length) {
        this.setInitialUpdateChildData(form, specs[moduleName + "." + actionName].groups[ind || i].children);
      }
    }

    setMockData(specs);
  }

  initData() {
	  
	  var current = this;
	  
    try {
      var hash = window.location.hash.split("/");
      if(hash.length == 4) {
        specifications = require(`./specs/${hash[2]}/${hash[2]}`).default;
      } else {
        specifications = require(`./specs/${hash[2]}/master/${hash[3]}`).default;
      }
    } catch(e) {
      
    }

    let { setMetaData, setModuleName, setActionName, setMockData, workflow} = this.props;
    let hashLocation = window.location.hash;
    let self = this;
    let obj = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
	self.setLabelAndReturnRequired(obj);
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
		current.setState({
			searchResult: res.properties
		})
		var workflowDetails = res.properties[0].propertyDetail.workFlowDetails;
		if(workflowDetails) {
			workflow.workflowDepartment = workflowDetails.department || null;
			workflow.workflowDesignation = workflowDetails.designation || null;
			workflow.approver =workflowDetails.assignee || null;
		}
		
		var query = {
			id : res.properties[0].propertyDetail.stateId
		}
		
		Api.commonApiPost('egov-common-workflows/process/_search', query,{}, false, true).then((res)=>{
			console.log(res);
			
						current.setState({
							process: res.processInstance
						})														
			
			 Api.commonApiPost( 'egov-common-workflows/designations/_search?businessKey=Create Property&departmentRule=&currentStatus='+res.processInstance.status+'&amountRule=&additionalRule=&pendingAction=&approvalDepartmentName=&designation&',{}, {},false, false).then((res)=>{
			    
				for(var i=0; i<res.length;i++){
					Api.commonApiPost('hr-masters/designations/_search', {name:res[i].name}).then((response)=>{
						console.log(response)
						response.Designation.unshift({id:-1, name:'None'});
						current.setState({
								...current.state,
								designation: [
									...current.state.designation,
									...response.Designation
								]
						})
					}).catch((err)=> {
						current.setState({designation: []})
						console.log(err)
					})
				}
								
					}).catch((err)=> {
					  current.setState({
						designation:[]
					  })
					  console.log(err)
					})
			current.setState({
				buttons: res.processInstance
			});
		}).catch((err)=> {
			console.log(res);
			current.setState({
				buttons: []
			});
		})
		
      self.props.setFormData(res);
      self.setInitialUpdateData(res, JSON.parse(JSON.stringify(specifications)), hashLocation.split("/")[2], hashLocation.split("/")[1], specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].objectName);
    }, function(err){

    })
  }

  componentDidMount() {
      this.initData();
	  this.props.initForm();
	  
	  var current = this;
	  
	   Api.commonApiPost( 'egov-common-masters/departments/_search').then((res)=>{
		  console.log(res);
		  res.Department.unshift({id:-1, name:'None'});
		  current.setState({workflowDepartment: res.Department})
		}).catch((err)=> {
		  current.setState({
			workflowDepartment:[]
		  })
		  console.log(err)
		})
  }
  
  updateInbox = (actionName, status) => {
	  	  
	  var currentThis = this;
	  
	  let {workflow, setLoadingStatus, toggleSnackbarAndSetText} = this.props;
	 
	  var data = this.state.searchResult;
	  
	  	var workFlowDetails = {
				"department": workflow.workflowDepartment || null,
				"designation":workflow.workflowDesignation || null,
				"assignee": null,
				"action": actionName,
				"status": status
			  }
			  
	  if(actionName == 'Forward') {
		 
			workFlowDetails.assignee = getPosition(this.state.approver, workflow.approver) || null;
		    localStorage.setItem('inboxStatus', 'Forwarded')
		  
	  } else if(actionName == 'Approve') {
		  
		  workFlowDetails.assignee = this.state.process.initiatorPosition || null
		  localStorage.setItem('inboxStatus', 'Approved')
		  
	  } else if(actionName == 'Reject') {
		  
		  workFlowDetails.assignee = this.state.process.initiatorPosition || null
		  localStorage.setItem('inboxStatus', 'Rejected') 
	  }
	  
		data[0].owners[0].tenantId = "default";
		data[0].vltUpicNumber = null;
		data[0].gisRefNo = null;
		data[0].oldUpicNumber = null;

				  
	  data[0].propertyDetail.workFlowDetails = workFlowDetails;
	  
	   setLoadingStatus('loading');
	   
	   var body = {
		   "properties": data
	   } 
	  
	     Api.commonApiPost('pt-property/properties/_update', {},body, false, true).then((res)=>{
			setLoadingStatus('hide');
			currentThis.props.history.push('/propertyTax/inbox-acknowledgement')
		  }).catch((err)=> {
			console.log(err)
			setLoadingStatus('hide');
			toggleSnackbarAndSetText(true, err.message);
		  })
  }

  getVal = (path) => {
    var val = _.get(this.props.formData, path);
    return  typeof val != "undefined" && (typeof val == "string" || typeof val == "number" || typeof val == "boolean") ? (val + "") : "";
  }

  printer = () => {
    window.print();
  }
  
  render() {
	  
	const renderOption = function(list,listName="") {
        if(list)
        {
            return list.map((item)=>
            {
                return (<MenuItem key={item.id} value={item.id} primaryText={item.name}/>)
            })
        }
    }
	  
	var current = this;
	
    let {mockData, moduleName, actionName, formData, fieldErrors, workflow, handleChange} = this.props;
    let { getVal, addNewCard, removeCard, printer, handleWorkFlowChange} = this;
	
	

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
      <div className="Inbox">
        <form id="printable">
        {!_.isEmpty(mockData) && <ShowFields groups={mockData[`${moduleName}.${actionName}`].groups} noCols={mockData[`${moduleName}.${actionName}`].numCols} ui="google" handler={""} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false} addNewCard={""} removeCard={""} screen="view"/>}
          <br/>
          {renderTable()}
			  {(this.state.buttons.hasOwnProperty('attributes') && (this.state.buttons.attributes.validActions.values.length > 0) && (this.state.buttons.attributes.validActions.values.map((item)=>{
				  if(item.name != 'Approve') {
					  return true;
				  } else {
					  false
				  }
			  })) ) &&	<Card className="uiCard">
                    <CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Workflow</div>} />
                    <CardText style={styles.reducePadding}>
                                <Grid fluid>
                                    <Row>
                                        <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Department Name *"
                                                  errorText={fieldErrors.workflowDepartment ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.workflowDepartment}</span>: ""}
                                                  value={workflow.workflowDepartment ? workflow.workflowDepartment :""}
                                                  onChange={(event, index, value) => {
													(value == -1) ? value = '' : '';  
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleWorkFlowChange(e, 'department');
                                                    handleChange(e, "workflowDepartment", true, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                                  >
                                                    {renderOption(this.state.workflowDepartment)}
                                              </SelectField>
                                        </Col>
                                        <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Designation Name *"
                                                  errorText={fieldErrors.workflowDesignation ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.workflowDesignation}</span>: ""}
                                                  value={workflow.workflowDesignation ? workflow.workflowDesignation :""}
                                                  onChange={(event, index, value) => {
													  (value == -1) ? value = '' : '';
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleWorkFlowChange(e, 'designation');
                                                    handleChange(e, "workflowDesignation", true, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                                  >
                                                   {renderOption(this.state.designation)}
                                              </SelectField>
                                        </Col>
                                        <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Approver Name *"
                                                  errorText={fieldErrors.approver ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.approver}</span>: ""}
                                                  value={workflow.approver ? workflow.approver : ""}
                                                  onChange={(event, index, value) => {
													  (value == -1) ? value = '' : '';
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "approver", true, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                                  >
                                                    {renderOption(this.state.approver)}
                                              </SelectField>
                                        </Col>
										<Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText="Comments"
                                                  errorText={fieldErrors.comments ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.comments}</span>: ""}
                                                  value={workflow.comments ? workflow.comments : ""}
                                                  onChange={(e) => {
                                                    handleChange(e, "comments", false, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                                  />
                                                   
                                        </Col>
                                    </Row>
                                </Grid>
                    </CardText>
			  </Card> }
		  <br/>
        </form>
        <div style={{"textAlign": "center"}}>
	
			{(this.state.buttons.hasOwnProperty('attributes') && this.state.buttons.attributes.validActions.values.length > 0) && this.state.buttons.attributes.validActions.values.map((item,index)=> {
				return(
					<RaisedButton key={index} type="button" primary={true} label={item.name} style={{margin:'0 5px'}} onClick={()=> {
						this.updateInbox(item.name, current.state.buttons.status);
					}}/>
				)
			})}
			
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  workflow:state.form.form,
  metaData:state.framework.metaData,
  mockData: state.framework.mockData,
  moduleName:state.framework.moduleName,
  actionName:state.framework.actionName,
  formData:state.frameworkForm.form,
  fieldErrors: state.frameworkForm.fieldErrors
});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ['approver', 'workflowDesignation', 'workflowDepartment']
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
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
export default connect(mapStateToProps, mapDispatchToProps)(Inbox);
