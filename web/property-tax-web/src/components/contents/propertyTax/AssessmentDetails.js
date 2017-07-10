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
import Api from '../../../api/pTAPIS';


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


class AssessmentDetails extends Component {

  constructor(props) {
    super(props);
    this.state= {
        propertytypes: [],
        apartments:[],
        departments:[],
    }
  } 


  componentDidMount() {
    //call boundary service fetch wards,location,zone data
    var currentThis = this;

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

    let {
      owners,
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
      isAddRoom
    } = this.props;

    let {search} = this;

    let cThis = this;

    return (<Card>
                      <CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Assessment details</div>} />
                      <CardText style={styles.reducePadding}>
                          <Card className="darkShadow">
                              <CardText style={styles.reducePadding}>
                                  <Grid fluid>
                                      <Row>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Reason for Creation"
                                                  errorText={fieldErrors.reasonForCreation ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.reasonForCreation}</span> : ""}
                                                  value={assessmentDetails.reasonForCreation ? assessmentDetails.reasonForCreation : ""}
                                                  onChange={(event, index, value) => {
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
                                                  <MenuItem value={1} primaryText="New Property"/>
                                                  <MenuItem value={2} primaryText="Bifurcation"/>
                                              </SelectField>
                                          </Col>

                                          {(assessmentDetails.reasonForCreation == 2) && <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText="Parent UPIC No."
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
                                                  floatingLabelText="Property Type"
                                                  errorText={fieldErrors.assessmentPropertyType ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.assessmentPropertyType}</span> : ""}
                                                  value={assessmentDetails.assessmentPropertyType ? assessmentDetails.assessmentPropertyType : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "assessmentPropertyType", true, "")}
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
                                                  errorText={fieldErrors.assessmentPropertySubType ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.assessmentPropertySubType}</span> : ""}
                                                  value={assessmentDetails.assessmentPropertySubType ? assessmentDetails.assessmentPropertySubType : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "assessmentPropertySubType", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                  <MenuItem value={1} primaryText="Options"/>
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Assessment Department"
                                                  errorText={fieldErrors.assessmentDepartment ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.assessmentDepartment}</span> : ""}
                                                  value={assessmentDetails.assessmentDepartment ? assessmentDetails.assessmentDepartment : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "assessmentDepartment", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                  <MenuItem value={1} primaryText="Options"/>
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText="Extent of Site (Sq. Mtrs)"
                                                  errorText={fieldErrors.extentOfSite ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.extentOfSite}</span> : ""}
                                                  value={assessmentDetails.extentOfSite ? assessmentDetails.extentOfSite : ""}
                                                  onChange={(e) => {handleChange(e, "extentOfSite", true, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  maxLength={8}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              />
                                          </Col>

                                      </Row>
                                  </Grid>
                              </CardText>
                          </Card>
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

export default connect(mapStateToProps, mapDispatchToProps)(AssessmentDetails);


