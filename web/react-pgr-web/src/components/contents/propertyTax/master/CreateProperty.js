import React, {Component} from 'react';
import {connect} from 'react-redux';

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
import RaisedButton from 'material-ui/RaisedButton';
import Api from '../../../../api/api';

import OwnerDetails from './propertyTax/OwnerDetails';
import CreateNewProperty from './propertyTax/CreateNewProperty';
import PropertyAddress from './propertyTax/PropertyAddress';
import Amenities from './propertyTax/Amenities';
import AssessmentDetails from './propertyTax/AssessmentDetails';
import ConstructionTypes from './propertyTax/ConstructionTypes';
import FloorDetails from './propertyTax/FloorDetails';
import DocumentUpload from './propertyTax/DocumentUpload';
import Workflow from './propertyTax/Workflow';
import VacantLand from './propertyTax/vacantLand';



var flag = 0;
const styles = {
  errorStyle: {
    color: red500
  },
  underlineStyle: {
    borderColor: "#354f57"
  },
  underlineFocusStyle: {
    borderColor: "#354f57"
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


const getNameById = function(object, id, property = "") {
  if (id == "" || id == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property == "") {
            if (object[i].id == id) {
                return object[i].name;
            }
        } else {
            if (object[i].hasOwnProperty(property)) {
                if (object[i].id == id) {
                    return object[i][property];
                }
            } else {
                return "";
            }
        }
    }
    return "";
}

const getNameByCode = function(object, code, property = "") {
  if (code == "" || code == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property == "") {
            if (object[i].code == code) {
                return object[i].name;
            }
        } else {
            if (object[i].hasOwnProperty(property)) {
                if (object[i].code == code) {
                    return object[i][property];
                }
            } else {
                return "";
            }
        }
    }
    return "";
}

//Create Class for Create and update property
class CreateProperty extends Component {
  constructor(props) {
    super(props)
    this.state = {
      addButton: false,
      addOwner: true,
      addFloor: false,
      addRoom: false,
      files:[],
      propertytypes: [],
      apartments:[],
      departments:[],
      floortypes:[],
      rooftypes:[],
      walltypes:[],
      woodtypes:[],
      structureclasses:[],
      occupancies:[],
      ward:[],
      locality:[],
      zone:[],
      block:[],
      street:[],
      revanue:[],
      election:[],
      usages:[],
	  ack:''
    }
 }
  



  componentWillMount() {


  }

  componentDidMount() {
      let {initForm}=this.props;
      initForm();
	  
	  var currentThis = this;

		  Api.commonApiPost('pt-property/property/propertytypes/_search',{}, {},false, true).then((res)=>{
			currentThis.setState({propertytypes:res.propertyTypes})
		  }).catch((err)=> {
			currentThis.setState({
			  propertytypes:[]
			})
		  })
	  
	    Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"WARD", hierarchyTypeName:"ADMINISTRATION"}).then((res)=>{
          console.log(res);
          currentThis.setState({election : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })
		
		Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"STREET", hierarchyTypeName:"LOCATION"}).then((res)=>{
          console.log(res);
          currentThis.setState({street : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })
		
		Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"BLOCK", hierarchyTypeName:"REVENUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({block : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })
		
		Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"LOCALITY", hierarchyTypeName:"LOCATION"}).then((res)=>{
          console.log(res);
          currentThis.setState({locality : res.Boundary})
        }).catch((err)=> {
           currentThis.setState({
            locality : []
          })
          console.log(err)
        })
		
		 Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"ZONE", hierarchyTypeName:"REVENUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({zone : res.Boundary})
        }).catch((err)=> {
           currentThis.setState({
            zone : []
          })
          console.log(err)
        })
  }

  componentWillUnmount() {

  }


  componentWillUpdate() {


  }

  componentDidUpdate(prevProps, prevState) {

  }

  search = (e) => {

  }

  
createPropertyTax = () => {
	
	let {createProperty, setLoadingStatus, toggleSnackbarAndSetText} = this.props;
	
	setLoadingStatus('loading');
	
	var userRequest = JSON.parse(localStorage.getItem("userRequest"));
	
	var numberOfFloors='';
	var builtupArea = 0;
	if(createProperty && createProperty.hasOwnProperty('floorsArr') && createProperty.hasOwnProperty('floors')){
		numberOfFloors = createProperty.floorsArr.length;
		for(let i=0;i<createProperty.floors.length;i++){
			
			builtupArea += createProperty.floors[i].builtupArea;
			
		}
	}
	
	if(createProperty && createProperty.hasOwnProperty('owners')) {		
		for(var i=0;i<createProperty.owners.length;i++){
			createProperty.owners[i].locale = userRequest.locale;
			createProperty.owners[i].type = userRequest.type;
			createProperty.owners[i].active = true;
			createProperty.owners[i].tenantId = 'default';
			if(createProperty.owners[i].isPrimaryOwner == "PrimaryOwner") {
				createProperty.owners[i].isPrimaryOwner = true;
			} else {
				createProperty.owners[i].isPrimaryOwner = false;
			}
		}
	}
	
	var date = new Date().getTime();
	
	var currentThis = this;
      var body = {
			"properties": [{
				"occupancyDate":"02/12/2016",
				"tenantId": "default",
				"oldUpicNumber": null,
				"vltUpicNumber": null,
				"creationReason": createProperty.reasonForCreation || null,
				"address": {
					"tenantId": "default",
					 "latitude": null,
					"longitude": null,
					"addressNumber": createProperty.doorNo || null,
					"addressLine1": createProperty.locality || null,
					"addressLine2": null,
					"landmark": null,
					"city": "secundrabad",
					"pincode": createProperty.pin || null,
					"detail": null,
					"auditDetails": {
						"createdBy": userRequest.userName,
						"lastModifiedBy":userRequest.userName,
						"createdTime": date,
						"lastModifiedTime": date
					}
				},
				"owners": createProperty.owners || null,
				"propertyDetail": {
					"source": "MUNICIPAL_RECORDS",
					"regdDocNo": "rdn2",
					"regdDocDate": "15/02/2017",
					"reason": "CREATE",
					"status": "ACTIVE",
					"isVerified": true,
					"verificationDate": "25/05/2017",
					"isExempted": false,
					"propertyType": createProperty.propertyType || null,
					"category": createProperty.propertySubType || null,
					"usage": null,
					"department": createProperty.department || null,
					"apartment":null,
					"siteLength": 12,
					"siteBreadth": 15,
					"sitalArea": createProperty.extentOfSite || null,
					"totalBuiltupArea": builtupArea, 
					"undividedShare": null,
					"noOfFloors": numberOfFloors, 
					"isSuperStructure": null,
					"landOwner": null,
					"floorType":createProperty.floorType || null,
					"woodType": createProperty.woodType || null,
					"roofType": createProperty.roofType || null,
					"wallType": createProperty.wallType || null,
					"floors":createProperty.floorsArr || null,
					"documents": [],
					"stateId": null,
					"workFlowDetails": {
						"department": createProperty.workflowDepartment || null,
						"designation":createProperty.workflowDesignation || null,
						"assignee": createProperty.approver || null,
						"action": "no",
						"status": null
					},
					"auditDetails": {
						"createdBy": userRequest.userName,
						"lastModifiedBy":userRequest.userName,
						"createdTime": date,
						"lastModifiedTime": date
					}
				},
				"vacantLand": null ,/*{
					"surveyNumber": createProperty.survayNumber || ,
					"pattaNumber": createProperty.pattaNumber || null,
					"marketValue": createProperty.marketValue || null,
					"capitalValue": createProperty.capitalValue || null,
					"layoutApprovedAuth": createProperty.layoutApprovalAuthority || null,
					"layoutPermissionNo": createProperty.layoutPermitNumber || null,
					"layoutPermissionDate":createProperty.layoutPermitDate || null,
					"resdPlotArea": null,
					"nonResdPlotArea": null,
					"auditDetails": {
						"createdBy": userRequest.userName,
						"lastModifiedBy":userRequest.userName,
						"createdTime": date,
						"lastModifiedTime": date
					}
				},*/

				"gisRefNo": null,
				"isAuthorised": null,
				"boundary": {
					"revenueBoundary": { 
						"id": createProperty.zoneNo || null,
						"name": getNameById(currentThis.state.zone, createProperty.zoneNo)  || null
					},
					"locationBoundary": {
						"id": createProperty.street || createProperty.locality || null ,
						"name": getNameById(currentThis.state.street, createProperty.street)  || getNameById(currentThis.state.locality, createProperty.locality) || null
					},
					"adminBoundary": { 
						"id": createProperty.electionWard || null,
						"name": getNameById(currentThis.state.election, createProperty.electionWard)  || null
					},
					"northBoundedBy": createProperty.north || null,
					"eastBoundedBy": createProperty.east || null,
					"westBoundedBy": createProperty.west || null,
					"southBoundedBy": createProperty.south || null,
					"auditDetails": {
						"createdBy": userRequest.userName,
						"lastModifiedBy":userRequest.userName,
						"createdTime": date,
						"lastModifiedTime": date
					}
				},
				"channel": "SYSTEM",
				"auditDetails": {
					"createdBy": userRequest.userName,
					"lastModifiedBy":userRequest.userName,
					"createdTime": date,
					"lastModifiedTime": date
				}
			}]
      }
	  
	  var fileStoreArray = [];
	  			
	   if(currentThis.props.files.length !=0){
			if(currentThis.props.files[0].length === 0){
				console.log('No file uploads');
			}else{
				
				console.log('still file upload pending', currentThis.props.files[0].length);
				
			  for(let i=0;i<currentThis.props.files[0].length;i++){
				  
				let formData = new FormData();
				formData.append("tenantId", localStorage.getItem('tenantId'));
				formData.append("module", "PT");
				formData.append("file", currentThis.props.files[0][i]);
				Api.commonApiPost("/filestore/v1/files",{},formData).then(function(response){
					var documentArray = {
						"documentType": {						
							"code": ""
						},
						"fileStore": "",
						"auditDetails": {
							"createdBy": userRequest.userName,
							"lastModifiedBy":userRequest.userName,
							"createdTime": date,
							"lastModifiedTime": date
						}
					}
					
					fileStoreArray.push(response.files[0]);
					console.log('All files succesfully uploaded');
					
					documentArray.documentType.name = "Photo of Assessment "+[i]
					documentArray.fileStore = response.files[0].fileStoreId;
					body.properties[0].propertyDetail.documents.push(documentArray);
					console.log(body);
				},function(err) {
				  console.log(err);
				});
			  }
			}
		  }
		  
     Api.commonApiPost('pt-property/properties/_create', {},body, false, true).then((res)=>{
		currentThis.setState({
			ack: res.properties.applicationNo
		});
		localStorage.setItem('ack', res.properties[0].propertyDetail.applicationNo);
		this.props.history.push('acknowledgement');
setLoadingStatus('hide');
      }).catch((err)=> {
        console.log(err)
		setLoadingStatus('hide');
 toggleSnackbarAndSetText(true, err.message);
      })
    }
	
createActivate = () => {
	
	let {isFormValid, createProperty} = this.props;
	
	console.log(createProperty)
	
	let notValidated = true;
	
	if(createProperty.hasOwnProperty('propertyType') && createProperty.propertyType == "VACANT_LAND") {
		if(isFormValid && (createProperty.owners ? (createProperty.owners.length == 0 ? false : true) : false )){
			notValidated = false;
		} else {
			notValidated = true;
		}
	} else {
		if(isFormValid && (createProperty.floors ? (createProperty.floors.length == 0 ? false : true) : false ) && (createProperty.owners ? (createProperty.owners.length == 0 ? false : true) : false )){
			notValidated = false;
		} else {
			notValidated = true;
		}
	}
	
	return notValidated;
	
}	
  
  render() {
	  	  
    let {
      createProperty,
      fieldErrors,
      isFormValid,
      handleChange,
      handleChangeNextOne,
      handleChangeNextTwo,
      deleteObject,
      deleteNestedObject,
      editObject,
      editIndex,
      isEditIndex,
      isAddRoom,
	  files,
	  handleChangeOwner
    } = this.props;

    let {search, createPropertyTax, cThis} = this;
	
	if(this.props.files.length != 0){
		console.log(this.props.files[0].length);
	}

	console.log(isFormValid);
    

    const renderOption = function(list,listName="") {
        if(list)
        {
			return list.map((item)=>
			{
				return (<MenuItem key={item.id} value={item.id} primaryText={item.name}/>)
			})
        }
    }

	  return(
		  <div className="createProperty">
				<h3 style={{padding:15}}>Create New Property</h3>
			  <form onSubmit={(e) => {search(e)}}>
				  <OwnerDetails />
				  <PropertyAddress/>  
				  <AssessmentDetails />				  
				  <Amenities />                  
				  <ConstructionTypes/>
				  {(getNameByCode(this.state.propertytypes, createProperty.propertyType) == "Vacant Land") ? <VacantLand/> : 
					 <div> {!this.state.addFloor && <Card>
						<CardText>
							 <RaisedButton type="button" className="pull-right" label="Add Floor" style={{marginTop:21}}  backgroundColor="#0b272e" labelColor={white} 
								  onClick={()=>{
									this.setState({
									  addFloor: true
									});
								  }}
							  />
							  <div className="clearfix"></div>                    
						</CardText>
					  </Card>}
					  </div>
				  }
				  {this.state.addFloor && <FloorDetails/>}
				  <DocumentUpload />
				  <Workflow />
				  
									
			   
				  <Card>
					<CardText style={{textAlign:'center'}}>
						<br/>
						<RaisedButton type="button" label="Create Property" disabled={(this.createActivate())} backgroundColor="#0b272e" labelColor={white} onClick={()=> {
							createPropertyTax();
							}
						}/>
						<div className="clearfix"></div>
					</CardText>
				  </Card>
			  </form>
		  </div>
      )
  }
}

const mapStateToProps = state => ({
  createProperty:state.form.form,
  fieldErrors: state.form.fieldErrors,
  editIndex: state.form.editIndex,
  addRoom : state.form.addRoom,
  files: state.form.files,
  isFormValid: state.form.isFormValid
});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ['reasonForCreation', 'propertyType', 'propertySubType', 'extentOfSite','doorNo', 'locality', 'electionWard', 'zoneNo', 'wardNo', 'floorType', 'roofType', 'workflowDepartment', 'workflowDesignation']
        },
        pattern: {
          current: [],
          required: []
        }
      },
	   validatePropertyOwner: {
        required: {
          current: [],
          required: ['aadhaarNumber', 'mobileNumber', 'name', 'gaurdianRelation', 'gaurdian', 'gender' ]
        },
        pattern: {
          current: [],
          required: []
        }
      },
	   validatePropertyFloor: {
        required: {
          current: [],
          required: ['floorNo', 'unitType','unitNo', 'structure', 'usage', 'usageSubType', 'occupancyType', 'constCompletionDate', 'occupancyDate', 'isStructured', 'builtupArea' ]
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

  handleChangeNextOne: (e, property, propertyOne, isRequired, pattern) => {
    dispatch({
      type: "HANDLE_CHANGE_NEXT_ONE",
      property,
      propertyOne,
      value: e.target.value,
      isRequired,
      pattern
    })
  },
  addNestedFormData: (formArray, formData) => {
    dispatch({
      type: "PUSH_ONE",
      formArray,
      formData
    })
  },

  addNestedFormDataTwo: (formObject, formArray, formData) => {
    dispatch({
      type: "PUSH_ONE_ARRAY",
      formObject,
      formArray,
      formData
    })
  },

  deleteObject: (property, index) => {
    dispatch({
      type: "DELETE_OBJECT",
      property,
      index
    })
  },

  deleteNestedObject: (property,propertyOne, index) => {
    dispatch({
      type: "DELETE_NESTED_OBJECT",
      property,
      propertyOne,
      index
    })
  },

  editObject: (objectName, object, isEditable) => {
    dispatch({
      type: "EDIT_OBJECT",
      objectName,
      object,
      isEditable
    })
  },

  resetObject: (object) => {
    dispatch({
      type: "RESET_OBJECT",
      object
    })
  },

  updateObject: (objectName, object) => {
    dispatch({
      type: "UPDATE_OBJECT",
      objectName,
      object
    })
  },

  updateNestedObject:  (objectName, objectArray, object) => {
    dispatch({
      type: "UPDATE_NESTED_OBJECT",
      objectName,
      objectArray,
      object
    })
  },
  
  handleChangeOwner: (e, property, propertyOne, isRequired, pattern) => {
    dispatch({
      type: "HANDLE_CHANGE_OWNER",
      property,
      propertyOne,
      value: e.target.value,
      isRequired,
      pattern
    })
  },
  
  handleChangeFloor: (e, property, propertyOne, isRequired, pattern) => {
    dispatch({
      type: "HANDLE_CHANGE_FLOOR",
      property,
      propertyOne,
      value: e.target.value,
      isRequired,
      pattern
    })
  },

  isEditIndex: (index) => {
    dispatch({
      type: "EDIT_INDEX",
      index
    })
  },

  isAddRoom: (room) => {
    dispatch({
      type: "ADD_ROOM",
      room
    })
  },
  
    setLoadingStatus: (loadingStatus) => {
     dispatch({type: "SET_LOADING_STATUS", loadingStatus});
   },
   toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
     dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
   }

});

export default connect(mapStateToProps, mapDispatchToProps)(CreateProperty);
