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
	  
	   Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"ELECTION", hierarchyTypeName:"ADMINISTRATION"}).then((res)=>{
          currentThis.setState({election : res.Boundary})
        }).catch((err)=> {
			currentThis.setState({
				election : []
			})
        })
		
		 Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"STREET", hierarchyTypeName:"REVANUE"}).then((res)=>{
          currentThis.setState({street : res.Boundary})
        }).catch((err)=> {
			currentThis.setState({
				street : []
			})
        })
		
		Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"BLOCK", hierarchyTypeName:"REVANUE"}).then((res)=>{
          currentThis.setState({block : res.Boundary})
        }).catch((err)=> {
			currentThis.setState({
				block :[]
				})
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
	
	let {createProperty} = this.props;
	
	var numberOfFloors='';
	var plinthArea = 0;
	if(createProperty && createProperty.hasOwnProperty('floorsArr')){
		numberOfFloors = createProperty.floorsArr.length;
		for(let i=0;i<createProperty.floors.length;i++){
			
			plinthArea += createProperty.floors[i].plinthArea;
			
		}
	}
	
	
	
	

	
	var userRequest = JSON.parse(localStorage.getItem("userRequest"));
	
	var date = new Date().getTime();
	
	
	var currentThis = this;
      var body = {
			"properties": [{
				"tenantId": "default",
				"oldUpicNumber": "",
				"vltUpicNumber": "",
				"creationReason": createProperty.reasonForCreation || '',
				"address": {
					"tenantId": "default",
					"addressNumber": createProperty.doorNo || '',
					"addressLine1": createProperty.locality || '',
					"landmark": null,
					"city": "Bangalore",
					"pincode": createProperty.pin || '',
					"detail": null,
					"auditDetails": {
						"createdBy": userRequest.userName,
						"lastModifiedBy":userRequest.userName,
						"createdTime": date,
						"lastModifiedTime": date
					}
				},
				"owners": createProperty.owners || '',
				"propertyDetail": {
					"propertyType": createProperty.ownerShip || '',
					"category": createProperty.assessmentPropertySubType || '',
					"usage": null,
					"department": createProperty.assessmentDepartment || '',
					"apartment":null,
					"siteLength": 12,
					"siteBreadth": 15,
					"sitalArea": createProperty.extentOfSite || '',
					"totalBuiltupArea": plinthArea, 
					"undividedShare": null,
					"noOfFloors": numberOfFloors, 
					"isSuperStructure": null,
					"landOwner": null,
					"floorType":createProperty.floorType || '',
					"woodType": createProperty.woodType || '',
					"roofType": createProperty.roofType || '',
					"wallType": createProperty.wallType || '',
					"floors":createProperty.floorsArr || '',
					"documents": [{
						"documentType": {						
							"name": "Photo of Assessment",
							"application": "CREATE",
							"auditDetails": {
								"createdBy": userRequest.userName,
								"lastModifiedBy":userRequest.userName,
								"createdTime": date,
								"lastModifiedTime": date
							}
						},
						"fileStore": "testing",
						"auditDetails": {
							"createdBy": userRequest.userName,
							"lastModifiedBy":userRequest.userName,
							"createdTime": date,
							"lastModifiedTime": date
						}
					}],
					"stateId": null,
					"workFlowDetails": {
						"department": createProperty.workflowDepartment,
						"designation":createProperty.workflowDesignation,
						"assignee": createProperty.approver,
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
				"vacantLand": {
					"surveyNumber": createProperty.survayNumber || '',
					"pattaNumber": createProperty.pattaNumber || '',
					"marketValue": createProperty.marketValue || '',
					"capitalValue": createProperty.capitalValue || '',
					"layoutApprovedAuth": createProperty.layoutApprovalAuthority || '',
					"layoutPermissionNo": createProperty.layoutPermitNumber || '',
					"layoutPermissionDate":createProperty.layoutPermitDate || '',
					"resdPlotArea": null,
					"nonResdPlotArea": null,
					"auditDetails": {
						"createdBy": userRequest.userName,
						"lastModifiedBy":userRequest.userName,
						"createdTime": date,
						"lastModifiedTime": date
					}
				},

				"gisRefNo": null,
				"isAuthorised": null,
				"boundary": {
					"revenueBoundary": { 
						"id": createProperty.blockNo,
						"name": getNameById(currentThis.state.block, createProperty.blockNo)  || ''
					},
					"locationBoundary": {
						"id": createProperty.street ,
						"name": getNameById(currentThis.state.street, createProperty.street)  || ''
					},
					"adminBoundary": { 
						"id": createProperty.electionCard || '',
						"name": getNameById(currentThis.state.election, createProperty.electionCard)  || ''
					},
					"northBoundedBy": createProperty.north || '',
					"eastBoundedBy": createProperty.east || '',
					"westBoundedBy": createProperty.west || '',
					"southBoundedBy": createProperty.south || '',
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

     Api.commonApiPost('pt-property/properties/_create', {},body, false, true).then((res)=>{
		 
		 
		  if(currentThis.props.files){
			if(currentThis.props.files.length === 0){
			  //currentThis.setState({loadingstatus:'hide'});
				console.log('create succesfully done. No file uploads');
			}else{
				
				console.log('create succesfully done. still file upload pending');
				
			  for(let i=0;i<currentThis.props.files.length;i++){
				//this.props.files.length[i]
				let formData = new FormData();
				formData.append("tenantId", localStorage.getItem('tenantId'));
				formData.append("module", "PT");
				formData.append("file", currentThis.props.files[i]);
				Api.commonApiPost("/filestore/v1/files",{},formData).then(function(response){
				  if(i === (currentThis.props.files.length - 1)){
					console.log('All files succesfully uploaded');
					console.log(response);
				  }
				  
				},function(err) {
				  console.log(err);
				});
			  }
			}
		  }
		  
        console.log(res);
      }).catch((err)=> {
        console.log(err)
      })
    }
  
  render() {
	  	  
    let {
      owners,
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
	  files
    } = this.props;

    let {search, createPropertyTax, cThis} = this;

    console.log(createProperty);

    const renderOption = function(list,listName="") {
        if(list)
        {
			return list.map((item)=>
			{
				return (<MenuItem key={item.id} value={item.id} primaryText={item.name}/>)
			})
        }
    }

    const fileNames = () => {
      this.state.files.map(function(e,i){
        {i} {e}
      })
    }

	  return(
		  <div className="createProperty">
				<h3 style={{padding:15}}>Create New Property</h3>
			  <form onSubmit={(e) => {search(e)}}>
					
				  <OwnerDetails />
				  <CreateNewProperty />              
				  <PropertyAddress/>                      
				  <Amenities />                  
				  <AssessmentDetails />
				  <ConstructionTypes/>
				  {(getNameById(this.state.propertytypes, createProperty.assessmentPropertyType) == "Vacant Land") ? <VacantLand/> : 
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
					<CardText style={styles.reducePadding}>
						<br/>
						<RaisedButton type="button" label="Create Property" className="pull-right" backgroundColor="#0b272e" labelColor={white} onClick={()=> {
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
  files: state.form.files
});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: []
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

});

export default connect(mapStateToProps, mapDispatchToProps)(CreateProperty);
