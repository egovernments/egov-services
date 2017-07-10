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


class PropertyAddress extends Component {

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
      propertyAddress,
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
                      <CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Property Address</div>} />
                      <CardText style={styles.reducePadding}>
                          <Card className="darkShadow">
                              <CardText style={styles.reducePadding}>
                                  <Grid fluid>
                                      <Row>
                                          <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText="Reference property number"
                                                  errorText={fieldErrors.refPropertyNumber ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.refPropertyNumber}</span> : ""}
                                                  value={propertyAddress.refPropertyNumber ? propertyAddress.refPropertyNumber : ""}
                                                  onChange={(e) => handleChange(e, "refPropertyNumber", false, /^\d{15}$/g)}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  maxLength={15}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              />
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Locality"
                                                  errorText={fieldErrors.locality ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.locality}</span>: ""}
                                                  value={propertyAddress.locality ? propertyAddress.locality:""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "locality", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                      {renderOption(this.state.locality)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Appartment/Complex name"
                                                  errorText={fieldErrors.locality ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.appComplexName}</span>: ""}
                                                  value={propertyAddress.locality ? propertyAddress.appComplexName:""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "appComplexName", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                    {renderOption(this.state.apartments)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Zone No."
                                                  errorText={fieldErrors.zoneNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.zoneNo}</span>: ""}
                                                  value={propertyAddress.zoneNo ? propertyAddress.zoneNo:""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "zoneNo", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                  {renderOption(this.state.zone)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Ward No."
                                                  errorText={fieldErrors.wardNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.wardNo}</span> : ""}
                                                  value={propertyAddress.wardNo ? propertyAddress.wardNo : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "wardNo", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                    {renderOption(this.state.ward)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Block No."
                                                  errorText={fieldErrors.blockNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.blockNo}</span> : ""}
                                                  value={propertyAddress.blockNo ? propertyAddress.blockNo : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "blockNo", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                      {renderOption(this.state.block)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Street"
                                                  errorText={fieldErrors.street ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.street}</span> : ""}
                                                  value={propertyAddress.street ? propertyAddress.street : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "street", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                    {renderOption(this.state.street)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Revenue circle"
                                                  errorText={fieldErrors.revenueCircle ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.revenueCircle}</span> : ""}
                                                  value={propertyAddress.revenueCircle ? propertyAddress.revenueCircle : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "revenueCircle", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                    {renderOption(this.state.revanue)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText="Election ward"
                                                  errorText={fieldErrors.electionCard ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.electionCard}</span> : ""}
                                                  value={propertyAddress.electionCard ? propertyAddress.electionCard : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "electionWard", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              >
                                                    {renderOption(this.state.election)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText="Door No."
                                                  errorText={fieldErrors.doorNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.doorNo}</span> : ""}
                                                  value={propertyAddress.doorNo ? propertyAddress.doorNo : ""}
                                                  onChange={(e) => handleChange(e, "doorNo", true, '')}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  maxLength={12}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              />
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText="Pin"
                                                  errorText={fieldErrors.pin ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.pin}</span> : ""}
                                                  value={propertyAddress.pin ? propertyAddress.pin : ""}
                                                  onChange={(e) => handleChange(e, "pin", false, '')}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              />
                                          </Col>
                                          <Col xs={12} md={12}>
                                              <Checkbox
                                                label="Is correspondence address different from property address?"
                                                style={styles.checkbox}
                                                defaultChecked ={propertyAddress.cAddressDiffPAddress}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "cAddressDiffPAddress", false, '')
                                                }}

                                              />
                                          </Col>
                                          {propertyAddress.cAddressDiffPAddress &&
                                            <div className="addMoreAddress">
                                                <Col xs={12} md={3} sm={6}>
                                                    <TextField  className="fullWidth"
                                                        floatingLabelText="Door No."
                                                        errorText={fieldErrors.cDoorno ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.cDoorno}</span> : ""}
                                                        value={propertyAddress.cDoorno ? propertyAddress.cDoorno : ""}
                                                        onChange={(e) => handleChange(e, "cDoorno", true, '')}
                                                        multiLine={true}
                                                         rows={2}
                                                         rowsMax={4}
                                                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                        underlineStyle={styles.underlineStyle}
                                                        underlineFocusStyle={styles.underlineFocusStyle}
                                                        maxLength={12}
                                                        floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                            maxLength={6}
                                                    />
                                                </Col>
                                                <Col xs={12} md={3} sm={6}>
                                                    <TextField  className="fullWidth"
                                                        floatingLabelText="Address 1"
                                                        errorText={fieldErrors.addressTwo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.addressTwo}</span> : ""}
                                                        value={propertyAddress.addressTwo ? propertyAddress.addressTwo : ""}
                                                        onChange={(e) => handleChange(e, "addressTwo", true, '')}
                                                        multiLine={true}
                                                         rows={2}
                                                         rowsMax={4}
                                                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                        underlineStyle={styles.underlineStyle}
                                                        underlineFocusStyle={styles.underlineFocusStyle}
                                                        maxLength={128}
                                                        floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                                    />
                                                </Col>
                                                <Col xs={12} md={3} sm={6}>
                                                    <TextField  className="fullWidth"
                                                        floatingLabelText="Pin"
                                                        errorText={fieldErrors.pinTwo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.pinTwo}</span> : ""}
                                                        value={propertyAddress.pinTwo ? propertyAddress.pinTwo : ""}
                                                        onChange={(e) => handleChange(e, "pinTwo", true, '')}
                                                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                        underlineStyle={styles.underlineStyle}
                                                        underlineFocusStyle={styles.underlineFocusStyle}
                                                        maxLength={6}
                                                        floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                                    />
                                                </Col>
                                            </div>
                                          }
                                      </Row>
                                  </Grid>
                              </CardText>
                          </Card>
                      </CardText>
                  </Card>)
  }

}

const mapStateToProps = state => ({
  propertyAddress:state.form.form,
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

export default connect(mapStateToProps, mapDispatchToProps)(PropertyAddress);


