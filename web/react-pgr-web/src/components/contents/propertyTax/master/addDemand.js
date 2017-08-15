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
import {translate} from '../../../common/common';
import Api from '../../../../api/api';


var flag = 0;
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


class AddDemand extends Component {

  constructor(props) {
    super(props);
    this.state= {
		periods: [],
		demands:  [
			{
				"id": "32",
				"tenantId": "default",
				"consumerCode": "AP-PT-2017/07/27-003395-18",
				"consumerType": "PRIVATE",
				"businessService": "PT",
				"owner": {
					"tenantId": null,
					"id": 279,
					"userName": "9302930251",
					"name": "Tester10",
					"permanentAddress": null,
					"mobileNumber": "9302930251",
					"emailId": null,
					"aadhaarNumber": null
				},
				"taxPeriodFrom": 1490985000000,
				"taxPeriodTo": 1506796199000,
				"demandDetails": [
					{
						"id": "79",
						"demandId": "32",
						"taxHeadMasterCode": "EDU_CESS",
						"taxAmount": 295,
						"collectionAmount": 0,
						"auditDetail": {
							"createdBy": "74",
							"lastModifiedBy": "74",
							"createdTime": 1501135450460,
							"lastModifiedTime": 1501135450460
						},
						"tenantId": "default"
					},
					{
						"id": "80",
						"demandId": "32",
						"taxHeadMasterCode": "PT_TAX",
						"taxAmount": 1055,
						"collectionAmount": 0,
						"auditDetail": {
							"createdBy": "74",
							"lastModifiedBy": "74",
							"createdTime": 1501135450460,
							"lastModifiedTime": 1501135450460
						},
						"tenantId": "default"
					},
					{
						"id": "81",
						"demandId": "32",
						"taxHeadMasterCode": "LIB_CESS",
						"taxAmount": 23,
						"collectionAmount": 0,
						"auditDetail": {
							"createdBy": "74",
							"lastModifiedBy": "74",
							"createdTime": 1501135450460,
							"lastModifiedTime": 1501135450460
						},
						"tenantId": "default"
					}
				],
				"minimumAmountPayable": 1,
				"auditDetail": {
					"createdBy": "74",
					"lastModifiedBy": "74",
					"createdTime": 1501135450460,
					"lastModifiedTime": 1501135450460
				}
			}
		]
    }
  } 


 componentDidMount() {
    //call boundary service fetch wards,location,zone data
    var currentThis = this;
	
	let {toggleSnackbarAndSetText} = this.props;
	
	var taxHeads = [];
		
	this.state.demands.map((demand, index)=>{
		var query = {
			service:'PT',
			code:[]
		}

		Api.commonApiPost('/billing-service/taxheads/_search', query).then((res)=>{
		  console.log('periods', res);
		  currentThis.setState({structureclasses: res.structureClasses})
		}).catch((err)=> {
		  console.log(err)
		})
	})
	
  } 
  
 getTaxHead = (demand, headName) => {
	 demand.demandDetails.map((item, index)=>{
		 if(item.taxHeadMasterCode == headName){
			 var query = {
				service:'PT',
				code:[item.taxHeadMasterCode]
			}
			 Api.commonApiPost('/billing-service/taxheads/_search', query).then((res)=>{
			console.log('periods', res);
			}).catch((err)=> {
		  console.log(err)
			})
		 }
	 })
 } 

  
  render() {

    const renderOption = function(list,listName="") {
        if(list)
        {
            return list.map((item)=>
            {
                return (<MenuItem key={item.id} value={item.code} primaryText={item.name}/>)
            })
        }
    }

    let {
      addDemand,
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
	  addDepandencyFields,
	  removeDepandencyFields
    } = this.props;

    let {search, handleDepartment, getTaxHead} = this;

    let cThis = this;
	
	const showfields = () => {
		return this.state.demands.map((demand, index)=>{
			return(<Card className="uiCard" key={index}>
						<CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>{translate('pt.create.demands.addDemand')}</div>} />
						<CardText style={styles.reducePadding}>
                                  <Grid fluid>
                                      <Row>
                                         <Col xs={12} md={4}>
											<h4>Period</h4>
											<Col xs={12} md={12}>
												<TextField  className="fullWidth"
												  floatingLabelText="Installment"
												  value="2017-2018"
												  onChange={(e) => {handleChange(e,"period", true, '')}}
												  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
												  underlineStyle={styles.underlineStyle}
												  underlineFocusStyle={styles.underlineFocusStyle}
												  maxLength={3}
												  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  disabled={true}/>
											</Col>
										 </Col>
										  <Col xs={12} md={4}>
											<h4>{getTaxHead(demand, 'PT_TAX')}</h4>
											<Col xs={12} md={6}>
												<TextField  className="fullWidth"
												  floatingLabelText="Demand"
												  value="2017-2018"
												  onChange={(e) => {handleChange(e,"period", true, '')}}
												  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
												  underlineStyle={styles.underlineStyle}
												  underlineFocusStyle={styles.underlineFocusStyle}
												  maxLength={3}
												  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												/>
											</Col>
											<Col xs={12} md={6}>
												<TextField  className="fullWidth"
												  floatingLabelText="Collection"
												  errorText={fieldErrors.floor ? (<span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor}</span> :""): ""}
												  value={addDemand.ptCollection ? addDemand.ptCollection : ""}
												  onChange={(e) => {handleChange(e,"ptCollection", true, '')}}
												  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
												  underlineStyle={styles.underlineStyle}
												  underlineFocusStyle={styles.underlineFocusStyle}
												  maxLength={3}
												  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												/>
											</Col>
										 </Col>
                                      </Row>
                                  </Grid>
								</CardText>
						</Card>
					)
		})
	}

    return (<div>{showfields()}</div>)
  }

}

const mapStateToProps = state => ({
  addDemand:state.form.form,
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
          required: ['reasonForCreation', 'propertyType', 'propertySubType', 'extentOfSite' ]
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
  
  addDepandencyFields: (property) => {
		dispatch({
			type: 'ADD_REQUIRED',
			property
		})
	},

	removeDepandencyFields: (property) => {
		dispatch({
			type: 'REMOVE_REQUIRED',
			property
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

export default connect(mapStateToProps, mapDispatchToProps)(AddDemand);


