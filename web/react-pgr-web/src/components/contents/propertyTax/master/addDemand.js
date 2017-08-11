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
	 
	 this.props.addDemand.department = '';
	
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

    let {search, handleDepartment} = this;

    let cThis = this;

    return (
				<Card className="uiCard">
                      <CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Add Demand</div>} />
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
											<h4>Property tax</h4>
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
                  </Card>)
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


