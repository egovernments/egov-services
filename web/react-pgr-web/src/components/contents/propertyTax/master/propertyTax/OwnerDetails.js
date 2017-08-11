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


class OwnerDetails extends Component { 

constructor(props) {
	super(props);
	this.state = {
		gaurdianRelation: [{code:-1, name:'None'}, {code:'FATHER', name:'Father'}, {code:'HUSBAND', name:'Husband'}, {code:'MOTHER', name:'Mother'}, {code:'OTHERS', name:'Others'} ],
		gender:[{code:-1, name:'None'},{code:'MALE', name:'Male'}, {code:'FEMALE', name:'Female'}, {code:'OTHERS', name:'Others'}],
		ownerType:[{code:-1, name:'None'},{code:'PRIVATE', name:'Private'}, {code:'PUBLIC', name:'Public'}, {code:'COMPANY', name:"Company"},{code:'ORGANIZATION', name:"Organization"}]
	}
}

	componentDidMount() {
		
	}


  render() {
	  
	  const renderOption = function(list,listName="") {
			if(list)
			{
				return list.map((item)=>
				{
					return (<MenuItem key={item.code} value={item.code} primaryText={item.name}/>)
				})
			}
		}


    let {
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
      isAddRoom,
	  isOwnerValid,
	  handleChangeOwner
    } = this.props;

    let {search} = this;

    let cThis = this;

    return (
      <Card className="uiCard">
        <CardHeader title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Owner Details</div>} style={styles.reducePadding} />
        <CardText >
       
              <Grid fluid>
                <Row>
                  <Col xs={12} md={12}>
                    <Row>
                      <Row>
                        <Col xs={12} md={3} sm={6}>
                          <TextField
                            hintText="434345456545"
                            floatingLabelText="Aadhaar No "
                            errorText={fieldErrors.owner ? (fieldErrors.owner.aadhaarNumber ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.aadhaarNumber}</span>: "") : ""}
                            value={ownerDetails.owner ? ownerDetails.owner.aadhaarNumber:""}
                            onChange={(e) => {
								handleChangeOwner(e,"owner", "aadhaarNumber", false, /^\d{12}$/g);
                               // handleChangeNextOne(e,"owner", "aadhaarNumber", false, /^\d{12}$/g);
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
                            hintText="9999888877"
                            floatingLabelText="Mobile No *"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.mobileNumber ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.mobileNumber}</span>: ""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.mobileNumber:""}
                            onChange={(e) =>{
								handleChangeOwner(e, "owner","mobileNumber", true, /^\d{10}$/g)
                               // handleChangeNextOne(e, "owner","mobileNumber", false, /^\d{10}$/g)
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
                            hintText="Joe Doe"
                            floatingLabelText="Owner Name *"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.name ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.name}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.name:""}
                            onChange={(e) => {
								handleChangeOwner(e,"owner" ,"name", true, "");
                                //handleChangeNextOne(e,"owner" ,"name", false, "");
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
                            floatingLabelText="Gender *"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.gender? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.owner.gender}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.gender:""}
                            onChange={(event, index, value) => {
								(value == -1) ? value = '' : '';
                                var e = {
                                  target: {
                                    value: value
                                   }
                                  };
								  handleChangeOwner(e, "owner" ,"gender", true, "")
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                            >
							{renderOption(this.state.gender)}
                          </SelectField>
                        </Col>
                        <Col xs={12} md={3} sm={6}>
                          <TextField  className="fullWidth"
                            hintText="example@example.com"
                            floatingLabelText="Email"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.emailId? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.emailId}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.emailId:""}
                            onChange={(e) => {
                                handleChangeNextOne(e, "owner", "emailId", false, /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
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
                          <TextField  className="fullWidth"
                            hintText="BTKPM5492G"
                            floatingLabelText="Pan Number"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.pan? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.pan}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.pan:""}
                            onChange={(e) => {
                                handleChangeNextOne(e, "owner", "pan", false, /^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$/);
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
                          <SelectField  className="fullWidth selectOption"
                            floatingLabelText="Guardian Relation *"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.gaurdianRelation? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.owner.gaurdianRelation}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.gaurdianRelation:""}
                            onChange={(event, index, value) => {
								(value == -1) ? value = '' : '';
                                var e = {
                                  target: {
                                    value: value
                                  }
                                };
								handleChangeOwner(e, "owner", "gaurdianRelation", true, "")
								//handleChangeNextOne(e, "owner", "gaurdianRelation", false, "")

                                
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                            >
							{renderOption(this.state.gaurdianRelation)}
                          </SelectField>
                        </Col>
                        <Col xs={12} md={3} sm={6}>
                          <TextField  className="fullWidth"
                            hintText="Guardian name"
                            floatingLabelText="Guardian *"
                            errorText={fieldErrors.owner ?(fieldErrors.owner.fatherOrHusbandName? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.fatherOrHusbandName}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.fatherOrHusbandName:""}
                            onChange={(e) => {
								handleChangeOwner(e,  "owner",  "fatherOrHusbandName", true, "")
								//handleChangeNextOne(e,  "owner",  "gaurdian", false, "")
							}}
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            maxLength={32}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                          />
                        </Col>
                        {false && <Col xs={12} md={3} sm={6}>
                          <SelectField  className="fullWidth selectOption"
                            floatingLabelText="Owner type "
                            errorText={fieldErrors.owner ?(fieldErrors.owner.ownerType? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.owner.ownerType}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.ownerType:""}
                            onChange={(event, index, value) => {
								(value == -1) ? value = '' : '';
                                var e = {
                                  target: {
                                    value: value
                                  }
                                };
                                handleChangeOwner(e, "owner" ,"ownerType", false, "");
                              }
                            }
                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                            underlineStyle={styles.underlineStyle}
                            underlineFocusStyle={styles.underlineFocusStyle}
                            floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                            >
							{renderOption(this.state.ownerType)}
                          </SelectField>
                        </Col>}

                        <Col xs={12} md={3} sm={6}>
                          <TextField  className="fullWidth"
                            hintText="100"
                            floatingLabelText="Percentage of ownership"
                            errorText={fieldErrors.owner ? (fieldErrors.owner.ownerShipPercentage ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.owner.ownerShipPercentage}</span>:""): ""}
                            value={ownerDetails.owner ? ownerDetails.owner.ownerShipPercentage:""}
                            onChange={(e) => handleChangeNextOne(e,"owner", "ownerShipPercentage", false, /^[1-9][0-9]?$|^100$/g)}
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
                            valueSelected={ownerDetails.owner ? ownerDetails.owner.isPrimaryOwner: ''}
                            onChange={(e, v) =>{ 
                            var e = {
                            target: {
                            value: v
                            }
                            }
							handleChangeOwner(e,"owner", "isPrimaryOwner", false,'')
                           // handleChangeNextOne(e,"owner", "isPrimaryOwner", false,'')
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
                              label="Secondary owner "
                              style={styles.radioButton}
                              className="col-md-6"
                            />
                        </RadioButtonGroup>
                        </Col>
						<div className="clearfix"></div>
                        <Col xs={12} md={3} sm={3} style={styles.textRight} className="pull-right">
                          <br/>
                          { (editIndex == -1 || editIndex == undefined ) &&
                            <RaisedButton type="button" label="Add" disabled={!isOwnerValid} primary={true} onClick={()=> {
                                this.props.addNestedFormData("owners","owner");
                                this.props.resetObject("owner", false);
                              }
                            }/>
                          }
                          { (editIndex > -1) &&
                            <RaisedButton type="button" label="Save" disabled={!isOwnerValid} primary={true} onClick={()=> {
                                this.props.updateObject("owners","owner",  editIndex);
                                this.props.resetObject("owner", false);
                                isEditIndex(-1);
                              }
                            }/>
                          }
                        </Col>
						<div className="clearfix"></div>
                      </Row>
                      {ownerDetails.owners &&
                        <div className="col-md-12 col-xs-12">  <br/>
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
                                <th>Is Primary Owner</th>
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
                                    <td>{index+1}</td>
                                    <td>{i.aadhaarNumber || 'NA'}</td>
                                    <td>{i.mobileNumber || 'NA'}</td>
                                    <td>{i.name || 'NA'}</td>
                                    <td>{i.gender || 'NA'}</td>
                                    <td>{i.emailId || 'NA'}</td>
                                    <td>{i.gaurdianRelation || 'NA'}</td>
                                    <td>{(i.isPrimaryOwner == 'PrimaryOwner' ? "True" : "False") || 'NA'}</td>
                                    <td>{i.fatherOrHusbandName || 'NA'}</td>
                                    <td>{i.ownerType || 'NA'}</td>
                                    <td>{i.ownerShipPercentage || 'NA'}</td>
                                    <td>
										<i className="material-icons" style={styles.iconFont} onClick={ () => {
											editObject("owner",i, true);
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
      </Card>)
  }

}

const mapStateToProps = state => ({
  ownerDetails:state.form.form,
  fieldErrors: state.form.fieldErrors,
  editIndex: state.form.editIndex,
  addRoom : state.form.addRoom,
  isOwnerValid : state.form.isOwnerValid
});

const mapDispatchToProps = dispatch => ({

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

  editObject: (objectName, object, isSectionValid) => {
    dispatch({
      type: "EDIT_OBJECT",
      objectName,
      object,
      isSectionValid
    })
  },

  resetObject: (object, isSectionValid) => {
    dispatch({
      type: "RESET_OBJECT",
      object,
	  isSectionValid,
	    validatePropertyOwner: {
        required: {
          current: [],
          required: ['mobileNumber', 'name', 'gaurdianRelation', 'gaurdian', 'gender' ]
        },
        pattern: {
          current: [],
          required: []
        }
      },
	   validatePropertyFloor: {
        required: {
          current: [],
          required: ['floorNo', 'unitType','unitNo', 'structure', 'usage', 'occupancyType', 'constCompletionDate', 'occupancyDate', 'isStructured', 'builtupArea','carpetArea', 'buildingCost', 'landCost']
        },
        pattern: {
          current: [],
          required: []
        }
      }
	  
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


