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


class VacantLand extends Component {

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
      vacantLand,
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
    } = this.props;

    let {search} = this;

    let cThis = this;

    return (<div>
			<Card>
			  <CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Vacant Land Details</div>} />
			  <CardText style={styles.reducePadding}>
				  <Card className="darkShadow">
					  <CardText style={styles.reducePadding}>
						  <Grid fluid>
							  <Row>
								  <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="Survey Number *"
											errorText={fieldErrors.survayNumber ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.survayNumber}</span> : ""}
											value={vacantLand.survayNumber ? vacantLand.survayNumber:""}
											onChange={(e) => {
												handleChange(e,"survayNumber", true, /^([0-9]|[a-z])+([0-9a-z]+){3}$/i);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={64}
										
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								  <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="Patta Number *"
											errorText={fieldErrors.pattaNumber ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.pattaNumber}</span> : ""}
											value={vacantLand.pattaNumber ? vacantLand.pattaNumber:""}
											onChange={(e) => {
												handleChange(e,"pattaNumber", true,  /^([0-9]|[a-z])+([0-9a-z]+){3}$/i);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={64}
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								  <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="Vacant Land Area(in Sq. Mtrs) *"
											errorText={fieldErrors.vacantLandArea ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.vacantLandArea}</span> : ""}
											value={vacantLand.vacantLandArea ? vacantLand.vacantLandArea:""}
											onChange={(e) => {
												handleChange(e,"vacantLandArea", true, /^\d{3,64}$/g);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={64}
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								   <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="Market Value *"
											errorText={fieldErrors.marketValue ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.marketValue}</span> : ""}
											value={vacantLand.marketValue ? vacantLand.marketValue:""}
											onChange={(e) => {
												handleChange(e,"marketValue", true, /^([0-9]|[a-z])+([0-9a-z]+){3}$/i);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={64}
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								  <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="Capital Value *"
											errorText={fieldErrors.capitalValue ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.capitalValue}</span> : ""}
											value={vacantLand.capitalValue ? vacantLand.capitalValue:""}
											onChange={(e) => {
												handleChange(e,"capitalValue", true, /^([0-9]|[a-z])+([0-9a-z]+){3}$/i);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={64}
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								  <Col xs={12} md={3} sm={6}>
									<DatePicker  className="fullWidth datepicker"
									  floatingLabelText="Effective Date *"
									  errorText={fieldErrors.effectiveDate ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.effectiveDate}</span> : ""}
									  defaultDate={ vacantLand.effectiveDate ? vacantLand.effectiveDate :  new Date()}
									  onChange={(event,date) => {
										  var e = {
											target:{
												value: date
											}
										  }
										  handleChange(e,"effectiveDate", true, "")}}
									  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
									  underlineStyle={styles.underlineStyle}
									  underlineFocusStyle={styles.underlineFocusStyle}
									  textFieldStyle={{width: '100%'}}
									  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
									/>
								</Col>
								 <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="Vacant Land Plot Area *"
											errorText={fieldErrors.vacantLandPlotArea ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.vacantLandPlotArea}</span> : ""}
											value={vacantLand.vacantLandPlotArea ? vacantLand.vacantLandPlotArea : ""}
											onChange={(e) => {
												handleChange(e,"vacantLandPlotArea", true, '');
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								   <Col xs={12} md={3} sm={6}>
									    <SelectField  className="fullWidth selectOption"
											hintText=""
											floatingLabelText="Layout Approval Authority *"
											errorText={fieldErrors.layoutApprovalAuthority ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.layoutApprovalAuthority}</span> : ""}
											value={vacantLand.layoutApprovalAuthority ? vacantLand.layoutApprovalAuthority : ""}
											onChange={(e, value) => {
												var e = {
													target: {
														value: value
													}
												}
												handleChange(e,"layoutApprovalAuthority", true, '');
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
											>
												<MenuItem value="1" primaryText="Options"/>
										  </SelectField>
								  </Col>
								  <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="Layout Permit Number *"
											errorText={fieldErrors.layoutPermitNumber ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.layoutPermitNumber}</span> : ""}
											value={vacantLand.layoutPermitNumber ? vacantLand.layoutPermitNumber : ""}
											onChange={(e) => {
												handleChange(e,"layoutPermitNumber", true, /^([0-9]|[a-z])+([0-9a-z]+){3}$/i);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={64}
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								
								<Col xs={12} md={3} sm={6}>
									<DatePicker  className="fullWidth datepicker"
									  floatingLabelText="Layout Permit Date *"
									  errorText={fieldErrors.layoutPermitDate ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.layoutPermitDate}</span> : ""}
									  defaultDate={ vacantLand.layoutPermitDate ? vacantLand.layoutPermitDate :  new Date()}
									  onChange={(event,date) => {
										  var e = {
											target:{
												value: date
											}
										  }
										  handleChange(e,"layoutPermitDate", true, "")}}
									  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
									  underlineStyle={styles.underlineStyle}
									  underlineFocusStyle={styles.underlineFocusStyle}
									  textFieldStyle={{width: '100%'}}
									  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
									/>
								</Col>
							  </Row>
						  </Grid>
					  </CardText>
				  </Card>
			  </CardText>
		  </Card>
		  <Card>
			  <CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Details of surrounding Boundaries of the property</div>} />
			  <CardText style={styles.reducePadding}>
				  <Card className="darkShadow">
					  <CardText style={styles.reducePadding}>
						  <Grid fluid>
							  <Row>
								  <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="North"
											errorText={fieldErrors.north ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.north}</span> : ""}
											value={vacantLand.north ? vacantLand.north:""}
											onChange={(e) => {
												handleChange(e,"north", true, /^([0-9]|[a-z])+([0-9a-z]+)$/i);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={256}
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								  <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="South"
											errorText={fieldErrors.south ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.south}</span> : ""}
											value={vacantLand.south ? vacantLand.south:""}
											onChange={(e) => {
												handleChange(e,"south", true, /^([0-9]|[a-z])+([0-9a-z]+)$/i);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={256}
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								   <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="East"
											errorText={fieldErrors.east ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.east}</span> : ""}
											value={vacantLand.east ? vacantLand.east:""}
											onChange={(e) => {
												handleChange(e,"east", true, /^([0-9]|[a-z])+([0-9a-z]+)$/i);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={256}
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>
								   <Col xs={12} md={3} sm={6}>
									   <TextField
											hintText=""
											floatingLabelText="West"
											errorText={fieldErrors.west ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.west}</span> : ""}
											value={vacantLand.west ? vacantLand.west:""}
											onChange={(e) => {
												handleChange(e,"west", true, /^([0-9]|[a-z])+([0-9a-z]+)$/i);
											  }
											}
											floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
											underlineStyle={styles.underlineStyle}
											underlineFocusStyle={styles.underlineFocusStyle}
											className="fullWidth"
											maxLength={256}
											floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										  />
								  </Col>	  
							  </Row>
						  </Grid>
					  </CardText>
				  </Card>
			  </CardText>
		  </Card>
		  </div>)
  }

}

const mapStateToProps = state => ({
  vacantLand:state.form.form,
  fieldErrors: state.form.fieldErrors,
  editIndex: state.form.editIndex,
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

export default connect(mapStateToProps, mapDispatchToProps)(VacantLand);