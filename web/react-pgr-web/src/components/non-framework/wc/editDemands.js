import React, {Component} from 'react';
import {connect} from 'react-redux';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText,CardTitle} from 'material-ui/Card';
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
import {translate} from '../../common/common';
import Api from '../../../api/api';


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

class AddDemand extends Component {

  constructor(props) {
    super(props);
    this.state= {
		taxHeads: [],
		demands: [],
		hasError: false,
		errorMsg: 'Invalid',
    DemandDetailBeans:[],
    searchData:[]
    }
  }


 componentDidMount() {
    //call boundary service fetch wards,location,zone data
    var currentThis = this;

	let {toggleSnackbarAndSetText, initForm, setLoadingStatus} = this.props;
	setLoadingStatus('loading');
	initForm();

	var getDemands = {
		consumerNumber: decodeURIComponent(this.props.match.params.upicNumber)
	}

    Api.commonApiPost('wcms-connection/connection/_search',getDemands,{},false,true).then((res)=>{
        console.log(res.Connection[0].acknowledgementNumber);
      currentThis.setState({
        searchData: res
      })
    }).catch((err)=> {
      console.log(err)
    })

	 Api.commonApiPost('wcms-connection/connection/getLegacyDemandDetailBeanListByExecutionDate', getDemands, {}, false, true).then((res)=>{
  		 currentThis.setState({
  			 DemandDetailBeans: res.DemandDetailBeans
  		 })
    	}).catch((err)=> {
    		console.log(err)
    	})
  }

  submitDemand = () => {
    let self = this;
	  var body = {
      DemandDetailBeans: Object.assign([], this.state.DemandDetailBeans)
    };

    self.props.setLoadingStatus('loading');
	  Api.commonApiPost('wcms-connection/connection/_leacydemand', {consumerNumber: decodeURIComponent(self.props.match.params.upicNumber), executionDate: self.state.searchData && self.state.searchData.Connection && self.state.searchData.Connection[0] && self.state.searchData.Connection[0].executionDate}, body, false, true).then((res)=>{
      self.props.setLoadingStatus('hide');
       self.props.toggleSnackbarAndSetText(true,translate("wc.update.message.success"), true, false);
      self.props.setRoute("/searchconnection/wc");
	  }).catch((err)=> {
      self.props.setLoadingStatus('hide');
		  self.props.toggleSnackbarAndSetText(true, err.message, false, true);
	  })

  }


  render() {

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

    let {search, handleDepartment, getTaxHead, validateCollection} = this;
    let { DemandDetailBeans, searchData } = this.state;


    let cThis = this;

    const handleChangeSrchRslt = function(e, name, ind){
      var _emps = Object.assign([], DemandDetailBeans);
      if(name == "collectionAmount" && _emps[ind]["taxAmount"] < e.target.value) {
        return cThis.props.toggleSnackbarAndSetText(true, "Collection Amount cannot be greater than Tax Amount.", false, true);
      }

      _emps[ind][name] = e.target.value;
      cThis.setState({
          ...cThis.state,
          DemandDetailBeans: _emps
      })
    }

	const showfields = () => {
    if(DemandDetailBeans.length>0) {
      return DemandDetailBeans.map((item, index)=> {
            return (<tr key={index}>
                    <td data-label={translate("wc.create.demands.taxPeriod")}>{item.taxPeriod}</td>
                    <td data-label={translate("wc.create.demands.taxHeadMasterCode")}>{item.taxHeadMasterCode}</td>
                    <td data-label={translate("wc.create.demands.taxAmount")}>
                    <TextField type="number" id={item.id} name="taxAmount"  value={item.taxAmount}
                      onChange={(e)=>{handleChangeSrchRslt(e, "taxAmount", index)}} />
                    </td>
                    <td data-label={translate("wc.create.demands.collectionAmount")}>
                    <TextField type="number" id={item.id} name="collectionAmount"  value={item.collectionAmount}
                      onChange={(e)=>{handleChangeSrchRslt(e, "collectionAmount", index)}} />
                    </td>
                </tr>
            );
      })
  }

	}

    const getValue = function(jPath) {
        if(cThis.state.searchData && cThis.state.searchData.Connection && cThis.state.searchData.Connection[0]) {
          return cThis.state.searchData.Connection[0][(cThis.state.searchData.Connection[0].withProperty ? "property" : "connectionOwner")][jPath];
        }
    }

    return (<div><Card className="uiCard">
				<CardTitle style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>{translate("wc.create.demand.applicantParticular")}</div>} subtitle={<div style={{color:"#354f57", fontSize:15,margin:'8px 0'}}>{translate("wc.create.demand.basicDetails")}</div>} />
        <br/>
        <CardText style={styles.reducePadding}>
					<Grid fluid>
						<Row>
            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.consumerNumber")}</span></label><br/>
            <label>{this.state.searchData && this.state.searchData.Connection && this.state.searchData.Connection[0] && this.state.searchData.Connection[0].consumerNumber}</label></span>
            </Col>

            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.nameOfApplicant")}</span></label><br/>
            <label>{getValue("nameOfApplicant")}</label></span>
            </Col>

            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.address")}</span></label><br/>
            <label>{getValue("address")}</label></span>
            </Col>

            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.mobileNumber")}</span></label><br/>
            <label>{getValue("mobileNumber")}</label></span>
            </Col>
            </Row>
            <br/>
            <Row>
            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.email")}</span></label><br/>
            <label>{getValue("email")}</label></span>
            </Col>

            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.locality")}</span></label><br/>
            <label>{getValue("locality")}</label></span>
            </Col>

            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.zone")}</span></label><br/>
            <label>{getValue("ward")}</label></span>
            </Col>
            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.adharNumber")}</span></label><br/>
            <label>{getValue("adharNumber")}</label></span>
            </Col>
            </Row>
              <br/>
            <Row>

            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.noOfFloors")}</span></label><br/>
            <label>{getValue("noOfFlats")}</label></span>
            </Col>
            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.connectionDetails.connectionType")}</span></label><br/>
            <label>{getValue("connectionType")}</label></span>
            </Col>
            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.connectionDetails.usageType")}</span></label><br/>
            <label>{getValue("usageType")}</label></span>
            </Col>
            <Col xs={12} sm={4} md={3} lg={3}>
            <span><label><span style={{"fontWeight":"500"}}>{translate("wc.create.groups.applicantDetails.propertyTaxDue")}</span></label><br/>
            <label>{getValue("propertyTaxDue")}</label></span>
            </Col>
            </Row>
              <br/>
								<br/>
								<Table style={{color:"black",fontWeight: "normal", marginBottom:0, minWidth:'100%', width:'auto'}}  bordered responsive>
									<thead>
										<tr>
											<th style={{textAlign:'center'}}>{translate("pt.create.groups.addDemand.fields.installment")}</th>
											<th  style={{textAlign:'center'}}>{translate("wc.create.demand.tax")}</th>
											<th style={{textAlign:'center'}}>{translate("wc.create.demands")}</th>
                      <th style={{textAlign:'center'}}>{translate("pt.create.groups.addDemand.fields.collection")}</th>
										</tr>
									</thead>
									<tbody>

										{showfields()}
									</tbody>
								</Table>

					</Grid>
				</CardText>

			</Card>
			<div style={{textAlign:'center'}}>
					<br/>
					{this.state.hasError && <p style={{color:'Red',textAlign:'center'}}>Collection entered should be equal to or less than the Demand<br/></p>}

					<RaisedButton type="button" label="Update" disabled={this.state.hasError}  primary={true} onClick={()=> {
								this.submitDemand();
								}
					}/>
				</div>
				</div>)
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
   toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
     dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
   },
   setRoute: (route) => dispatch({type: "SET_ROUTE", route})

});

export default connect(mapStateToProps, mapDispatchToProps)(AddDemand);
