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
import {translate} from '../../../../common/common';
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


class PropertyAddress extends Component {

  constructor(props) {
    super(props);
    this.state= {
          locality:[],
           apartments:[],
           zone:[],
           ward:[],
           block:[],
           street:[],
           revanue:[],
           election:[]
    }
  } 


  componentDidMount() {
    //call boundary service fetch wards,location,zone data
    var currentThis = this;

       Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"LOCALITY", hierarchyTypeName:"LOCATION"}).then((res)=>{
          console.log(res);
		  res.Boundary.unshift({id:-1, name:'None'})
          currentThis.setState({locality : res.Boundary})
        }).catch((err)=> {
           currentThis.setState({
            locality : []
          })
          console.log(err)
        })

         Api.commonApiPost('pt-property/property/apartments/_search',{}, {},false, true).then((res)=>{
          console.log(res);
          currentThis.setState({apartments:res.apartments})
        }).catch((err)=> {
           currentThis.setState({
            apartments:[]
          })
          console.log(err)
        }) 

       Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"ZONE", hierarchyTypeName:"REVENUE"}).then((res)=>{
          console.log(res);
		  res.Boundary.unshift({id:-1, name:'None'})
          currentThis.setState({zone : res.Boundary})
        }).catch((err)=> {
           currentThis.setState({
            zone : []
          })
          console.log(err)
        })

          Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"WARD", hierarchyTypeName:"REVENUE"}).then((res)=>{
          console.log(res);
		  res.Boundary.unshift({id:-1, name:'None'})
          currentThis.setState({ward : res.Boundary})
        }).catch((err)=> {
          currentThis.setState({
            ward : []
          })
          console.log(err)
        })

         Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"BLOCK", hierarchyTypeName:"REVENUE"}).then((res)=>{
          console.log(res);
		  res.Boundary.unshift({id:-1, name:'None'})
          currentThis.setState({block : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"STREET", hierarchyTypeName:"LOCATION"}).then((res)=>{
          console.log(res);
          currentThis.setState({street : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"REVENUE", hierarchyTypeName:"REVENUE"}).then((res)=>{
          console.log(res);
		  res.Boundary.unshift({id:-1, name:'None'})
          currentThis.setState({revanue : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"WARD", hierarchyTypeName:"ADMINISTRATION"}).then((res)=>{
          console.log(res);
		  res.Boundary.unshift({id:-1, name:'None'})
          currentThis.setState({election : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })
		
		this.props.initForm();

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
      isAddRoom,
	  addDepandencyFields,
	  removeDepandencyFields,
	  addFloors
    } = this.props;

    let {search} = this;

    let cThis = this;

    return (	
				<Card className="uiCard">
                      <CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Property Address</div>} />
                      <CardText style={styles.reducePadding}>
                                  <Grid fluid>
                                      <Row>
                                          <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText={translate('pt.create.groups.propertyAddress.fields.referancePropertyNumber')}
												                          hintText="000001111122222"
                                                  errorText={fieldErrors.refPropertyNumber ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.refPropertyNumber}</span> : ""}
                                                  value={propertyAddress.refPropertyNumber ? propertyAddress.refPropertyNumber : ""}
                                                  onChange={(e) => handleChange(e, "refPropertyNumber", false, /^[a-zA-Z0-9]*$/g)}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  maxLength={15}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              />
                                          </Col>
										  <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText={translate('pt.create.groups.propertyAddress.fields.appartment')}
                                                  errorText={fieldErrors.appComplexName ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.appComplexName}</span>: ""}
                                                  value={propertyAddress.appComplexName ? propertyAddress.appComplexName:""}
                                                  onChange={(event, index, value) => {
													                               (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "appComplexName", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												                          dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                              ><MenuItem value={-1} primaryText="None"/>
                                                    {renderOption(this.state.apartments)}
                                              </SelectField>
                                          </Col>
										  <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText={<span>{translate('pt.create.groups.propertyAddress.fields.doorNo')}<span style={{"color": "#FF0000"}}> *</span></span>}
												  hintText="301"
                                                  errorText={fieldErrors.doorNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.doorNo}</span> : ""}
                                                  value={propertyAddress.doorNo ? propertyAddress.doorNo : ""}
                                                  onChange={(e) => handleChange(e, "doorNo", true, /^[^-\s]+$/g)}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  maxLength={12}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                              />
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText={translate('pt.create.groups.propertyAddress.fields.locality')}
                                                  errorText={fieldErrors.locality ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.locality}</span>: ""}
                                                  value={propertyAddress.locality ? propertyAddress.locality:""}
                                                  onChange={(event, index, value) => {
													  (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "locality", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                              >
                                                      {renderOption(this.state.locality)}
                                              </SelectField>
                                          </Col>
										   <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText={translate('pt.create.groups.propertyAddress.fields.electionWard')}
                                                  errorText={fieldErrors.electionWard ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.electionWard}</span> : ""}
                                                  value={propertyAddress.electionWard ? propertyAddress.electionWard : ""}
                                                  onChange={(event, index, value) => {
													  (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "electionWard", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                              >
                                                    {renderOption(this.state.election)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText={<span>{translate('pt.create.groups.propertyAddress.fields.zoneNo')}<span style={{"color": "#FF0000"}}> *</span></span>}
                                                  errorText={fieldErrors.zoneNo ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.zoneNo}</span>: ""}
                                                  value={propertyAddress.zoneNo ? propertyAddress.zoneNo:""}
                                                  onChange={(event, index, value) => {
													  (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "zoneNo", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                              >
                                                  {renderOption(this.state.zone)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText={<span>{translate('pt.create.groups.propertyAddress.fields.wardNo')}<span style={{"color": "#FF0000"}}> *</span></span>}
                                                  errorText={fieldErrors.wardNo ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.wardNo}</span> : ""}
                                                  value={propertyAddress.wardNo ? propertyAddress.wardNo : ""}
                                                  onChange={(event, index, value) => {
													  (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "wardNo", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                              >
                                                    {renderOption(this.state.ward)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText={translate('pt.create.groups.propertyAddress.fields.blockNo')}
                                                  errorText={fieldErrors.blockNo ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.blockNo}</span> : ""}
                                                  value={propertyAddress.blockNo ? propertyAddress.blockNo : ""}
                                                  onChange={(event, index, value) => {
													  (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "blockNo", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                              >
                                                      {renderOption(this.state.block)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText={translate('pt.create.groups.propertyAddress.fields.street')}
                                                  errorText={fieldErrors.street ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.street}</span> : ""}
                                                  value={propertyAddress.street ? propertyAddress.street : ""}
                                                  onChange={(event, index, value) => {
													  (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "street", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                              >
                                                    {renderOption(this.state.street)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <SelectField  className="fullWidth selectOption"
                                                  floatingLabelText={translate('pt.create.groups.propertyAddress.fields.revenueCircle')}
                                                  errorText={fieldErrors.revenueCircle ? <span style={{position:"absolute", bottom:-41}}>{fieldErrors.revenueCircle}</span> : ""}
                                                  value={propertyAddress.revenueCircle ? propertyAddress.revenueCircle : ""}
                                                  onChange={(event, index, value) => {
													  (value == -1) ? value = '' : '';
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "revenueCircle", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                                              >
                                                    {renderOption(this.state.revanue)}
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText={<span>{translate('pt.create.groups.propertyAddress.fields.pin')}<span style={{"color": "#FF0000"}}> *</span></span>}
												  hintText="400050"
                                                  errorText={fieldErrors.pin ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.pin}</span> : ""}
                                                  value={propertyAddress.pin ? propertyAddress.pin : ""}
                                                  onChange={(e) => handleChange(e, "pin", true, /^\d{6}$/g)}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  maxLength={6}
                                              />
                                          </Col>
										  <Col xs={12} md={3} sm={6}>
                                              <TextField  className="fullWidth"
                                                  floatingLabelText={<span>{translate('pt.create.groups.propertyAddress.fields.totalFloors')}<span style={{"color": "#FF0000"}}> *</span></span>}
                                                  errorText={fieldErrors.totalFloors ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.totalFloors}</span> : ""}
                                                  value={propertyAddress.totalFloors ? propertyAddress.totalFloors : ""}
                                                  onChange={(e, value) => {
													  addFloors(value);
                                                      handleChange(e, "totalFloors", true, /^\d+$/g)}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
												  maxLength={2}
												/>
										
                                             
                                          </Col>
                                          <Col xs={12} md={12}>
                                              <Checkbox
                                                label={translate('pt.create.groups.propertyAddress.fields.isCorrespondanceAddressDifferentFromAddress')}
                                                style={styles.checkbox}
                                                defaultChecked ={propertyAddress.cAddressDiffPAddress}
                                                onCheck = {(e, i, v) => {
													if(i) {
														addDepandencyFields('cDoorno');
														addDepandencyFields('addressTwo');
													} else {	
														removeDepandencyFields('cDoorno');
														removeDepandencyFields('addressTwo');
													}
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
                                                        floatingLabelText={<span>{translate('pt.create.groups.propertyAddress.fields.doorNo')}<span style={{"color": "#FF0000"}}> *</span></span>}
														                            hintText="302"
                                                        errorText={fieldErrors.cDoorno ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.cDoorno}</span> : ""}
                                                        value={propertyAddress.cDoorno ? propertyAddress.cDoorno : ""}
                                                        onChange={(e) => handleChange(e, "cDoorno", true, '')}
                                            
      
                                                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                        underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                        underlineFocusStyle={styles.underlineFocusStyle}
                                                        maxLength={12}
                                                        floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														
                                                    />
                                                </Col>
                                                <Col xs={12} md={3} sm={6}>
                                                    <TextField  className="fullWidth"
                                                        floatingLabelText={<span>{translate('pt.create.groups.propertyAddress.fields.address1')}<span style={{"color": "#FF0000"}}> *</span></span>}
                                                        errorText={fieldErrors.addressTwo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.addressTwo}</span> : ""}
                                                        value={propertyAddress.addressTwo ? propertyAddress.addressTwo : ""}
                                                        onChange={(e) => handleChange(e, "addressTwo", true, '')}
                                                        hintText="Address"
                                                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                        underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
                                                        underlineFocusStyle={styles.underlineFocusStyle}
                                                        maxLength={128}
                                                        floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                                                    />
                                                </Col>
                                                <Col xs={12} md={3} sm={6}>
                                                    <TextField  className="fullWidth"
                                                        floatingLabelText={translate('pt.create.groups.propertyAddress.fields.pin')}
														hintText="400050"
                                                        errorText={fieldErrors.pinTwo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.pinTwo}</span> : ""}
                                                        value={propertyAddress.pinTwo ? propertyAddress.pinTwo : ""}
                                                        onChange={(e) => handleChange(e, "pinTwo", false, /^\d{6}$/g)}
                                                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                        underlineStyle={styles.underlineStyle} floatingLabelFixed={true}
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
	
initForm : () => {
	dispatch({
		type: "SET_FLOOR_NUMBER",
		noOfFloors: 0
	})
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
	
	addFloors: (noOfFloors) => {
		dispatch({
			type: 'FLOOR_NUMBERS',
			noOfFloors
		})
	}

});

export default connect(mapStateToProps, mapDispatchToProps)(PropertyAddress);


