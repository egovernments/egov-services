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
        console.log(res);
        currentThis.setState({propertytypes:res.propertyTypes})
      }).catch((err)=> {
        currentThis.setState({
          propertytypes:[]
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
	
	var currentThis = this;
      var body = {
			"properties": [{
				"tenantId": "default",
				"oldUpicNumber": "",
				"vltUpicNumber": "",
				"creationReason": "NEWPROPERTY",
				"address": {
					"tenantId": "default",
					"addressNumber": "Door number",
					"addressLine1": "Locality Name",
					"landmark": null,
					"city": "After login I will get",
					"pincode": "500082",
					"detail": null,
					"auditDetails": {
						"createdBy": "egovernments",
						"lastModifiedBy": "egovernments",
						"createdTime": 0,
						"lastModifiedTime": 0
					}
				},
				"owners": this.props.createProperty.owners,
				"propertyDetail": {
					"propertyType": "OwnerShip type",
					"category": "Property Sub Type",
					"usage": null,
					"department": "Department",
					"apartment":null,
					"siteLength": 12,
					"siteBreadth": 15,
					"sitalArea": "Extent of Site",
					"totalBuiltupArea": "Sum of plinth area",
					"undividedShare": null,
					"noOfFloors": "Number of floors added",
					"isSuperStructure": null,
					"landOwner": null,
					"floorType": "normal",
					"woodType": "modern",
					"roofType": "new one",
					"wallType": "painting",
					"floors":this.props.createProperty.floorsArr,
					"documents": [{
						"documentType": {
							
							"name": "Test application for anil",
							"application": "CREATE",
							"auditDetails": {
								"createdBy": "egovernments",
								"lastModifiedBy": "egovernments",
								"createdTime": 0,
								"lastModifiedTime": 0
							}
						},
						"fileStore": "testing",
						"auditDetails": {
							"createdBy": "egovernments",
							"lastModifiedBy": "egovernments",
							"createdTime": 0,
							"lastModifiedTime": 0
						}
					}],
					"stateId": null,
					"workFlowDetails": {
						"department": "incometax",
						"designation": "manager",
						"assignee": 14,
						"action": "no",
						"status": null
					},
					"auditDetails": {
						"createdBy": "egovernments",
						"lastModifiedBy": "egovernments",
						"createdTime": 0,
						"lastModifiedTime": 0
					}
				},
				"vacantLand": {
					"surveyNumber": "surveynumber2",
					"pattaNumber": "pn2",
					"marketValue": 10748,
					"capitalValue": 452200,
					"layoutApprovedAuth": "laa2",
					"layoutPermissionNo": "lpn2",
					"layoutPermissionDate": "10/05/2017",
					"resdPlotArea": 475,
					"nonResdPlotArea": 658,
					"auditDetails": {
						"createdBy": "egovernments",
						"lastModifiedBy": "egovernments",
						"createdTime": 0,
						"lastModifiedTime": 0
					}
				},

				"gisRefNo": null,
				"isAuthorised": null,
				"boundary": {
					"revenueBoundary": { // block No
						"id": 13,
						"name": "rbname2"
					},
					"locationBoundary": { // Street (if not selected than Location) 
						"id": 19,
						"name": "lbname2"
					},
					"adminBoundary": { // election ward
						"id": 173,
						"name": "abname2"
					},
					"northBoundedBy": "nbb test1",
					"eastBoundedBy": "ebb1",
					"westBoundedBy": "wbb1",
					"southBoundedBy": "sbb1",
					"auditDetails": {
						"createdBy": "egovernments",
						"lastModifiedBy": "egovernments",
						"createdTime": 0,
						"lastModifiedTime": 0
					}
				},
				"channel": "SYSTEM",
				"auditDetails": {
					"createdBy": "egovernments",
					"lastModifiedBy": "egovernments",
					"createdTime": 0,
					"lastModifiedTime": 0
				}
			}]
      }

     Api.commonApiPost('pt-property/properties/_create', {},body).then((res)=>{
		 
		 
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
									cThis.setState({
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
