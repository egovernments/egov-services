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
import DataTable from '../common/Table';
import Api from '../../api/pTAPIS';

import OwnerDetails from './propertyTax/OwnerDetails';
import CreateNewProperty from './propertyTax/CreateNewProperty';
import PropertyAddress from './propertyTax/PropertyAddress';
import Amenities from './propertyTax/Amenities';
import AssessmentDetails from './propertyTax/AssessmentDetails';
import ConstructionTypes from './propertyTax/ConstructionTypes';
import FloorDetails from './propertyTax/FloorDetails';
import DocumentUpload from './propertyTax/DocumentUpload';
import Workflow from './propertyTax/Workflow';



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
    //call boundary service fetch wards,location,zone data
    //var dropDownSearch = ['propertytypes','apartments', 'departments', 'floortypes', 'rooftypes', 'walltypes', 'woodtypes', 'structureclasses', 'occupancies'];
    var currentThis = this;

        Api.commonApiPost('property/propertytypes/_search').then((res)=>{
          console.log(res);
          currentThis.setState({propertytypes:res.propertyTypes})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/apartments/_search').then((res)=>{
          console.log(res);
          currentThis.setState({apartments:res.apartments})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/departments/_search').then((res)=>{
          console.log(res);
          currentThis.setState({departments:res.departments})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/floortypes/_search').then((res)=>{
          console.log(res);
          currentThis.setState({floortypes:res.floorTypes})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/rooftypes/_search').then((res)=>{
          console.log(res);
          currentThis.setState({rooftypes: res.roofTypes})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/walltypes/_search').then((res)=>{
          console.log(res);
          currentThis.setState({walltypes: res.walltypes})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/woodtypes/_search').then((res)=>{
          console.log(res);
          currentThis.setState({woodtypes: res.woodTypes})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/structureclasses/_search').then((res)=>{
          console.log(res);
          currentThis.setState({structureclasses: res.structureClasses})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/occupancies/_search').then((res)=>{
          console.log(res);
          currentThis.setState({occupancies : res.occupancies})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/usages/_search').then((res)=>{
          console.log(res);
          currentThis.setState({usages : res.usageMasters})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"WARD", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({ward : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"LOCALITY", hierarchyTypeName:"LOCATION"}).then((res)=>{
          console.log(res);
          currentThis.setState({locality : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"ZONE", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({zone : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"BLOCK", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({block : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"STREET", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({street : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"REVANUE", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({street : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"ELECTION", hierarchyTypeName:"ADMINISTRATION"}).then((res)=>{
          console.log(res);
          currentThis.setState({election : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })



  }

  componentDidMount() {
      let {initForm}=this.props;
      initForm();
  }

  componentWillUnmount() {

  }


  componentWillUpdate() {


  }

  componentDidUpdate(prevProps, prevState) {

  }

  search = (e) => {

  }



  handleCheckBoxChange = (prevState) => {
      this.setState((prevState) => {prevState.cAddressDiffPAddress.checked = !prevState.cAddressDiffPAddress.checked})
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
      isAddRoom
    } = this.props;

    let {search} = this;

    let cThis = this;

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

    const createPropertyTax = () => {
      var body = {
          {
    "properties": [{
        "tenantId": "default",
        "oldUpicNumber": "",
        "vltUpicNumber": "",
        "creationReason": "NEWPROPERTY",
        "address": {
            "tenantId": "default",
            "latitude": 11,
            "longitude": 20,
            "addressNumber": "test",
            "addressLine1": "ameerpet",
            "addressLine2": "mitrivanam",
            "landmark": "test travels",
            "city": "secundrabad",
            "pincode": "500082",
            "detail": "testing",
            "auditDetails": {
                "createdBy": "egovernments",
                "lastModifiedBy": "egovernments",
                "createdTime": 0,
                "lastModifiedTime": 0
            }
        },
        "owners": [ {
            "userName": "ramki786",
            "password":"123",
            "salutation": "Mrs",
            "name": "Ramakrishna",
            "gender": "MALE",
            "mobileNumber": "9923948234",
            "emailId": "ramakrishna@egovernments.org",
            "altContactNumber": null,
            "pan": null,
            "aadhaarNumber": null,
            "permanentAddress": null,
            "permanentCity": null,
            "permanentPinCode": null,
            "correspondenceAddress": null,
            "correspondenceCity": null,
            "correspondencePinCode": null,
            "active": true,
            "locale": "en_IN",
            "type": "CITIZEN",
            "accountLocked": false,
            "fatherOrHusbandName": null,
            "signature": null,
            "bloodGroup": null,
            "photo": null,
            "identificationMark": null,
            "createdBy": null,
            "otpReference": null,
            "lastModifiedBy": null,
            "tenantId": "default",
             "auditDetails": {
                    "createdBy": "egovernments",
                    "lastModifiedBy": "egovernments",
                    "createdTime": 0,
                    "lastModifiedTime": 0
                },
            "roles": [
                {
                    "code": "CITIZEN",
                    "name": "Citizen"
                }
            ]
        }],
        "propertyDetail": {
           
            "source": "MUNICIPAL_RECORDS",
            "regdDocNo": "rdn2",
            "regdDocDate": "15/02/2017",
            "reason": "trying to purchase",
            "status": "ACTIVE",
            "isVerified": true,
            "verificationDate": "25/05/2017",
            "isExempted": false,
            "exemptionReason": "",
            "propertyType": "house",
            "category": "land",
            "usage": "no",
            "department": "incometax",
            "apartment": "no",
            "siteLength": 12,
            "siteBreadth": 15,
            "sitalArea": 14,
            "totalBuiltupArea": 12,
            "undividedShare": 17,
            "noOfFloors": 1,
            "isSuperStructure": false,
            "landOwner": "kumar",
            "floorType": "normal",
            "woodType": "modern",
            "roofType": "new one",
            "wallType": "painting",
            "floors": [{
                
                "floorNo": "fn3",
                "units": [{
                    "unitNo":"2",
                    "unitType": "FLAT",
                    "units":[{
                    "unitNo":"3",
                    "unitType": "ROOM",
                    "length": 11,
                    "width": 15,
                    "builtupArea": 120,
                    "assessableArea": 105,
                    "bpaBuiltupArea": 195,
                    "bpaNo": "bpn2",
                    "bpaDate": "15/02/2017",
                    "usage": "tset",
                    "occupancyType": "asf",
                    "occupierName": "kumar",
                    "firmName": "wtc technologies",
                    "rentCollected": 17,
                    "structure": "rec14",
                    "age": "28",
                    "exemptionReason": "testcase",
                    "isStructured": false,
                    "occupancyDate": "15/02/2017",
                    "constCompletionDate": "15/02/2017",
                    "electricMeterNo": "emn2",
                    "waterMeterNo": "wmn2",
                    "auditDetails": {
                        "createdBy": "egovernments",
                        "lastModifiedBy": "egovernments",
                        "createdTime": 0,
                        "lastModifiedTime": 0
                    }
                }],
                    "length": 114,
                    "width": 175,
                    "builtupArea": 120,
                    "assessableArea": 105,
                    "bpaBuiltupArea": 195,
                    "bpaNo": "bpn2",
                    "bpaDate": "15/02/2017",
                    "usage": "tset",
                    "occupancyType": "asf",
                    "occupierName": "kumar",
                    "firmName": "wtc technologies",
                    "rentCollected": 17,
                    "structure": "rec14",
                    "age": "28",
                    "exemptionReason": "testcase",
                    "isStructured": false,
                    "occupancyDate": "15/02/2017",
                    "constCompletionDate": "15/02/2017",
                    "electricMeterNo": "emn2",
                    "waterMeterNo": "wmn2",
                    "auditDetails": {
                        "createdBy": "egovernments",
                        "lastModifiedBy": "egovernments",
                        "createdTime": 0,
                        "lastModifiedTime": 0
                    }
                }],
                "auditDetails": {
                    "createdBy": "egovernments",
                    "lastModifiedBy": "egovernments",
                    "createdTime": 0,
                    "lastModifiedTime": 0
                }
            }],
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
            "stateId": "si2",
            "workFlowDetails": {
                "department": "incometax",
                "designation": "manager",
                "assignee": 14,
                "action": "no",
                "status": "processing"
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
        "assessmentDate": "10/04/2017",
        "occupancyDate": "10/04/2017",
        "gisRefNo": "gfn2",
        "isAuthorised": false,
        "isUnderWorkflow": false,
        "boundary": {
            "revenueBoundary": {
                "id": 13,
                "name": "rbname2"
            },
            "locationBoundary": {
                "id": 19,
                "name": "lbname2"
            },
            "adminBoundary": {
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
        "active": false,
        "channel": "SYSTEM",
        "auditDetails": {
            "createdBy": "egovernments",
            "lastModifiedBy": "egovernments",
            "createdTime": 0,
            "lastModifiedTime": 0
        }
    }]
}
      }

     Api.commonApiPost('pt-property/properties/_create', {},body).then((res)=>{
        console.log(res);
      }).catch((err)=> {
        console.log(err)
      })
    }

   

      return(
          <div className="createProperty">
              <form onSubmit={(e) => {search(e)}}>
                  <OwnerDetails />
                  <CreateNewProperty />              
                  <PropertyAddress/>                      
                  <Amenities />                  
                  <AssessmentDetails />
                  <ConstructionTypes/>
                  {!this.state.addFloor && <Card>
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
  addRoom : state.form.addRoom
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
