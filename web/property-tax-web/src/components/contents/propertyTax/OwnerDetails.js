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


class OwnerDetails extends Component { 


  render() {


    let {
      owners,
      ownerDetails,
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

    return (
      <Card>
        <CardHeader title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Owner Details</div>} style={styles.reducePadding} />
        <CardText style={styles.reducePadding}>
          <Card className="darkShadow">
            <CardText style={styles.reducePadding}>
              <Grid fluid>
                <Row>
                  <Col xs={12} md={12}>
                    <Row>
                      <Row>
                        <Col xs={12} md={3} sm={6}>
                          <TextField
                            hintText="eg- 434345456545"
                            floatingLabelText="Aadhar No"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.aadhaarNumber ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.aadhaarNumber}</span>: "") : ""}
                            value={ownerDetails.owner ? ownerDetails.owner.aadhaarNumber:""}
                            onChange={(e) => {
                                handleChangeNextOne(e,"owner", "aadhaarNumber", true, /^\d{12}$/g);
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            className="fullWidth"
                            maxLength={12}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                          />
                        </Col>
                        <Col xs={12} md={3} sm={6}>
                          <TextField  className="fullWidth"
                            hintText="eg- 9999888877"
                            floatingLabelText="Mobile No"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.mobileNumber ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.mobileNumber}</span>: ""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.mobileNumber:""}
                            onChange={(e) =>{
                                handleChangeNextOne(e, "owner","mobileNumber", true, /^\d{10}$/g)
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            maxLength={10}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                          />
                        </Col>
                        <Col xs={12} md={3} sm={6}>
                          <TextField  className="fullWidth"
                            hintText="eg- Joe Doe"
                            floatingLabelText="Owner Name"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.name ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.name}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.name:""}
                            onChange={(e) => {
                                handleChangeNextOne(e,"owner" ,"name", true, "");
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            maxLength={32}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                          />
                        </Col>
                        <Col xs={12} md={3} sm={6}>
                          <SelectField  className="fullWidth selectOption"
                            floatingLabelText="Gender"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.gender? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.gender}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.gender:""}
                            onChange={(event, index, value) => {
                                var e = {
                                  target: {
                                    value: value
                                   }
                                  };
                                handleChangeNextOne(e, "owner" ,"gender", true, "")
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                            >
                            <MenuItem value={1} primaryText="Male"/>
                            <MenuItem value={2} primaryText="Female"/>
                            <MenuItem value={3} primaryText="Others"/>  
                          </SelectField>
                        </Col>
                        <Col xs={12} md={3} sm={6}>
                          <TextField  className="fullWidth"
                            hintText="eg- example@example.com"
                            floatingLabelText="Email"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.emailId? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.emailId}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.emailId:""}
                            onChange={(e) => {
                                handleChangeNextOne(e, "owner", "emailId", false, /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/);
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            maxLength={32}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                          />
                        </Col>
                        <Col xs={12} md={3} sm={6}>
                          <SelectField  className="fullWidth selectOption"
                            hintText="eg- Father"
                            floatingLabelText="Guardian Relation"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.gaurdianRelation? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.gaurdianRelation}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.gaurdianRelation:""}
                            onChange={(event, index, value) => {
                                var e = {
                                  target: {
                                    value: value
                                  }
                                };
                                handleChangeNextOne(e, "owner", "gaurdianRelation", true, "")
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                            >
                            <MenuItem value={1} primaryText="Father"/>
                            <MenuItem value={2} primaryText="Husband"/>
                            <MenuItem value={3} primaryText="Mother"/>
                            <MenuItem value={4} primaryText="Others"/>
                          </SelectField>
                        </Col>
                        <Col xs={12} md={3} sm={6}>
                          <TextField  className="fullWidth"
                            hintText="eg- Guardian name"
                            floatingLabelText="Guardian"
                            errorText={fieldErrors.owner ?(fieldErrors.owner.gaurdian? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.gaurdian}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.gaurdian:""}
                            onChange={(e) => handleChangeNextOne(e,  "owner",  "gaurdian", false, "")}
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            maxLength={32}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                          />
                        </Col>
                        <Col xs={12} md={3} sm={6}>
                          <SelectField  className="fullWidth selectOption"
                            floatingLabelText="Owner type"
                            errorText={fieldErrors.owner ?(fieldErrors.owner.ownerType? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.ownerType}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.ownerType:""}
                            onChange={(event, index, value) => {
                                var e = {
                                  target: {
                                    value: value
                                  }
                                };
                                handleChangeNextOne(e, "owner" ,"ownerType", false, "");
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                            >
                            <MenuItem value={1} primaryText="Ex-Service man"/>
                            <MenuItem value={2} primaryText="Freedom Fighter"/>
                            <MenuItem value={3} primaryText="Freedom figher's wife"/>
                          </SelectField>
                        </Col>

                        <Col xs={12} md={3} sm={6}>
                          <TextField  className="fullWidth"
                            hintText="eg- 100"
                            floatingLabelText="Percentage of ownership"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.ownerShipPercentage ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.ownerShipPercentage}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.ownerShipPercentage:""}
                            onChange={(e) => handleChangeNextOne(e,"owner", "ownerShipPercentage", false, /^\d{3}$/g)}
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            maxLength={3}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                          />
                        </Col>
                        <Col xs={12} md={6} sm={6}>
                          <br/>
                          <RadioButtonGroup
                            name="ownerRadio"
                            valueSelected={ownerDetails.owner ? ownerDetails.owner.ownerTypeRadio: ''}
                            onChange={(e, v) =>{ alert('Boom');
                            var e = {
                            target: {
                            value: v
                            }
                            }
                            handleChangeNextOne(e,"owner", "ownerTypeRadio", true,'')
                            }}
                          >
                            <RadioButton
                              value="PrimaryOwner"
                              label="Primary owner"
                              style={styles.radioButton}
                              className="col-md-6"
                            />
                            <RadioButton
                              value="SecondaryOwner"
                              label="Secondary owner"
                              style={styles.radioButton}
                              className="col-md-6"
                            />
                        </RadioButtonGroup>
                        </Col>
                        <Col xs={12} md={3} sm={3} style={styles.textRight}>
                          <br/>
                          { (editIndex == -1 || editIndex == undefined ) &&
                            <RaisedButton type="button" label="Add" backgroundColor="#0b272e" labelColor={white} onClick={()=> {
                                this.props.addNestedFormData("owners","owner");
                                this.props.resetObject("owner");
                              }
                            }/>
                          }
                          { (editIndex > -1) &&
                            <RaisedButton type="button" label="Save"  backgroundColor="#0b272e" labelColor={white} onClick={()=> {
                                this.props.updateObject("owners","owner",  editIndex);
                                this.props.resetObject("owner");
                                isEditIndex(-1);
                              }
                            }/>
                          }
                        </Col>
                      </Row>
                      {ownerDetails.owners &&
                        <div>  <br/>
                          <Table id="createPropertyTable" style={{color:"black",fontWeight: "normal", marginBottom:0}} bordered responsive>
                            <thead style={{backgroundColor:"#607b84",color:"white"}}>
                              <tr>
                                <th>#</th>
                                <th>Adhar Number</th>
                                <th>Mobile Number</th>
                                <th>Owner Name</th>
                                <th>Gender</th>
                                <th>Email</th>
                                <th>Gaurdian Relation</th>
                                <th>Owner Type Radio</th>
                                <th>Gaurdian</th>
                                <th>Owner Type</th>
                                <th>Percentage of Ownership</th>
                                <th></th>               
                              </tr>
                            </thead>
                            <tbody>
                              {ownerDetails.owners && ownerDetails.owners.map(function(i, index){
                                if(i){
                                  return (<tr key={index}>
                                    <td>{index}</td>
                                    <td>{i.aadhaarNumber}</td>
                                    <td>{i.mobileNumber}</td>
                                    <td>{i.name}</td>
                                    <td>{i.gender}</td>
                                    <td>{i.emailId}</td>
                                    <td>{i.gaurdianRelation}</td>
                                    <td>{i.ownerTypeRadio}</td>
                                    <td>{i.gaurdian}</td>
                                    <td>{i.ownerType}</td>
                                    <td>{i.ownerShipPercentage}</td>
                                    <td>
										<i className="material-icons" style={styles.iconFont} onClick={ () => {
											editObject("owner",i);
											isEditIndex(index);
										 }}>mode_edit</i>
										<i className="material-icons" style={styles.iconFont} onClick={ () => {
											  deleteObject("owners", index);
										}}>delete</i>
                                    </td>
                                  </tr> )
                                }
                              })}
                            </tbody>
                          </Table>
                        </div>
                      }
                    </Row>
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
  ownerDetails:state.form.form,
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

export default connect(mapStateToProps, mapDispatchToProps)(OwnerDetails);


