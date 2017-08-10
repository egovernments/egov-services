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
import Api from '../../../../../api/api';


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


class AssessmentDetails extends Component {

  constructor(props) {
    super(props);
    this.state= {
        propertytypes: [],
        reasonForCreation:[],
        departments:[],
		usages:[]
    }
  } 


 componentDidMount() {
    //call boundary service fetch wards,location,zone data
    var currentThis = this;
	
	let {toggleSnackbarAndSetText} = this.props;

      Api.commonApiPost('pt-property/property/propertytypes/_search',{}, {},false, true).then((res)=>{
		  res.propertyTypes.unshift({id:-1, name:'None'});
        console.log(res);
        currentThis.setState({propertytypes:res.propertyTypes})
      }).catch((err)=> {
        currentThis.setState({
          propertytypes:[]
        })
		toggleSnackbarAndSetText(true, err.message);
        console.log(err)
      })
	  
	  
        Api.commonApiPost('pt-property/property/usages/_search').then((res)=>{
          console.log(res);
          currentThis.setState({usages : res.usageMasters})
        }).catch((err)=> {
          console.log(err)
        })
		
  } 

handleDepartment = (e) => {
	
	let {toggleSnackbarAndSetText, setLoadingStatus} = this.props;
		
		setLoadingStatus('loading');
	
	var currentThis = this;
	
	 currentThis.setState({
            departments:[]
     })
	 
	 this.props.assessmentDetails.department = '';
	
	let query = {
		category : e.target.value
	}
	
	  Api.commonApiPost('pt-property/property/departments/_search',query, {},false, true).then((res)=>{
		   res.departments.unshift({id:-1, name:'None'});
		  console.log(res);
		  currentThis.setState({
			departments:res.departments
		  })
		setLoadingStatus('hide');
		}).catch((err)=> {
		  console.log(err)
		  	toggleSnackbarAndSetText(true, err.message);
			setLoadingStatus('hide');
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
      assessmentDetails,
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

    let {search, handleDepartment} = this;

    let cThis = this;

    return (
				<Card className="uiCard">
                      <CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Assessment details</div>} />
                      <CardText style={styles.reducePadding}>
                                  <Grid fluid>
                                      <Row>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Reason for Creation *"
                                                  errorText={fieldErrors.reasonForCreation ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.reasonForCreation}</span> : ""}
                                                  value={assessmentDetails.reasonForCreation ? assessmentDetails.reasonForCreation : ""}
                                                  onChange={(event, index, value) => {
													    (value == -1) ? value = '' : '';
														if(value == 'SUBDIVISION') {
															addDepandencyFields('parentUpicNo');
														} else {
															removeDepandencyFields('parentUpicNo');
														}
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "reasonForCreation", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  id="creationReason"
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
												  <MenuItem value={-1} primaryText="None"/>
                                                  <MenuItem value="NEWPROPERTY" primaryText="New Property"/>
                                                  <MenuItem value="SUBDIVISION" primaryText="Bifurcation"/>
                                              </SelectField>
                                          </Col>

                                          {(assessmentDetails.reasonForCreation == 'SUBDIVISION') && <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText="Parent UPIC No. *"
                                                  errorText={fieldErrors.parentUpicNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.parentUpicNo}</span> : ""}
                                                  value={assessmentDetails.parentUpicNo ? assessmentDetails.parentUpicNo : ""}
                                                  onChange={(e) => {handleChange(e, "parentUpicNo", true, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  id="upicNumber"
                                                  maxLength={15}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              />
                                          </Col>}
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Property Type *"
                                                  errorText={fieldErrors.propertyType ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.propertyType}</span> : ""}
                                                  value={assessmentDetails.propertyType ? assessmentDetails.propertyType : ""}
                                                  onChange={(event, index, value) => {
													    (value == -1) ? value = '' : '';
														if(value == 'VACANT_LAND') {
															addDepandencyFields('survayNumber');
															addDepandencyFields('pattaNumber');
															addDepandencyFields('vacantLandArea');
															addDepandencyFields('marketValue');
															addDepandencyFields('capitalValue');
															addDepandencyFields('effectiveDate');
															addDepandencyFields('vacantLandPlotArea');
															addDepandencyFields('layoutApprovalAuthority');
															addDepandencyFields('layoutPermitNumber');
															addDepandencyFields('layoutPermitDate');
														} else {
															removeDepandencyFields('survayNumber');
															removeDepandencyFields('pattaNumber');
															removeDepandencyFields('vacantLandArea');
															removeDepandencyFields('marketValue');
															removeDepandencyFields('capitalValue');
															removeDepandencyFields('effectiveDate');
															removeDepandencyFields('vacantLandPlotArea');
															removeDepandencyFields('layoutApprovalAuthority');
															removeDepandencyFields('layoutPermitNumber');
															removeDepandencyFields('layoutPermitDate');
															
														}
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
													  handleDepartment(e);
                                                      handleChange(e, "propertyType", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                  {renderOption(this.state.propertytypes)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Property Sub-type"
                                                  errorText={fieldErrors.propertySubType ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.propertySubType}</span> : ""}
                                                  value={assessmentDetails.propertySubType ? assessmentDetails.propertySubType : ""}
                                                  onChange={(event, index, value) => {
													    (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "propertySubType", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >   
											      <MenuItem value={-1} primaryText="None"/>
                                                  <MenuItem value={1} primaryText="Options"/>
                                              </SelectField>
                                          </Col>
										  <Col xs={12} md={3} sm={6}>
														<SelectField  className="fullWidth selectOption"
														  floatingLabelText="Usage type *"
														  errorText={fieldErrors.usage ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.usage}</span> : ""}
														  value={assessmentDetails.usage ? assessmentDetails.usage : ""}
														  onChange={(event, index, value) => {
															  (value == -1) ?  value = '' : '';
															  var e = {
																target: {
																  value: value
																}
															  };
															  handleChange(e,"usage", true, "")}
														  }
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														><MenuItem value={-1} primaryText="None"/>
															  {renderOption(this.state.usages)}

														</SelectField>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<SelectField  className="fullWidth selectOption"
														  floatingLabelText="Usage sub type"
														  errorText={fieldErrors.usageSubType ?<span style={{position:"absolute", bottom:-41}}>{fieldErrors.usageSubType}</span> : ""}
														  value={assessmentDetails.usageSubType ? assessmentDetails.usageSubType : ""}
														  onChange={(event, index, value) => {
															  (value == -1) ?  value = '' : '';
															  var e = {
																target: {
																  value: value
																}
															  };
															  handleChange(e,"usageSubType", false, "")}
														  }
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														><MenuItem value={-1} primaryText="None"/>
															{renderOption(this.state.usages)}
														</SelectField>
													</Col>
										  {(getNameByCode(this.state.propertytypes ,assessmentDetails.propertyType).match('Central Government') ||
											getNameByCode(this.state.propertytypes ,assessmentDetails.propertyType).match('State Government')) 
											&& <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Department"
                                                  errorText={fieldErrors.department ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.department}</span> : ""}
                                                  value={assessmentDetails.department ? assessmentDetails.department : ""}
                                                  onChange={(event, index, value) => {
													    (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "department", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                  {renderOption(this.state.departments)}
                                              </SelectField>
                                          </Col>
                                          }
                                          <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText="Total Area *"
												  hintText="14"
                                                  errorText={fieldErrors.extentOfSite ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.extentOfSite}</span> : ""}
                                                  value={assessmentDetails.extentOfSite ? assessmentDetails.extentOfSite : ""}
                                                  onChange={(e) => {handleChange(e, "extentOfSite", true, /^\d+$/g)}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  maxLength={8}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              />
                                          </Col>
										   <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText="Sequence No. *"
												  hintText="14"
                                                  errorText={fieldErrors.sequenceNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.sequenceNo}</span> : ""}
                                                  value={assessmentDetails.sequenceNo ? assessmentDetails.sequenceNo : ""}
                                                  onChange={(e) => {handleChange(e, "sequenceNo", true, /^\d+$/g)}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  maxLength={4}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              />
                                          </Col>
										  <Col xs={12} md={3} sm={6}>
                                              <Checkbox
                                                label="Is Authorized?"
                                                style={styles.checkbox}
                                                defaultChecked ={true}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "isAuthorized", false, '')
                                                }}

                                              />
                                          </Col>
                                      </Row>
                                  </Grid>
                      </CardText>
                  </Card>)
  }

}

const mapStateToProps = state => ({
  assessmentDetails:state.form.form,
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

export default connect(mapStateToProps, mapDispatchToProps)(AssessmentDetails);


