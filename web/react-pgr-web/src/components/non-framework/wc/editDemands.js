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
    demandDetailBeans:[]
    }
  }


 componentDidMount() {
    //call boundary service fetch wards,location,zone data
    var currentThis = this;

	let {toggleSnackbarAndSetText, initForm, setLoadingStatus} = this.props;
	setLoadingStatus('loading');
	initForm();

	var getDemands = {
		consumerNumber: this.props.match.params.upicNumber
	}

	Api.commonApiPost('wcms-connection/connection/_leacydemand', getDemands, {}, false, true).then((res)=>{

		console.log('search',res);

		 currentThis.setState({
			 demandDetailBeans: res.demandDetailBeans
		 })

		res.demandDetailBeans.map((demand, index)=>{
			demand.demandDetails.map((item, i)=>{
				var query = {
					service:'PT',
					code:item.taxHeadMasterCode
				}

				Api.commonApiPost('/billing-service/taxheads/_search', query, {}, false, true).then((res)=>{
						setLoadingStatus('hide');
					 currentThis.setState({
						 taxHeads:[
							...currentThis.state.taxHeads,
							res.TaxHeadMasters[0]
						 ]
					 })
				}).catch((err)=> {
					console.log(err)
				})
			})
		})

	}).catch((err)=> {
		console.log(err)
	})
  }

  submitDemand = () => {

	  var data = this.state.demands;

	  let { addDemand } = this.props;

	  data.map((demand, index)=> {
		  demand.businessService = 'PT'
		  demand.demandDetails.map((item, i)=> {
			  item.taxAmount = (addDemand['demands'+index] ? addDemand['demands'+index]['demand'+i] :  item.taxAmount)|| item.taxAmount;
			  item.collectionAmount = (addDemand['collections'+index] ? addDemand['collections'+index]['collection'+i] :  item.collectionAmount)|| item.collectionAmount;
		  })
	  })

	  console.log(data);

	  var body = {
		  Demands : data
	  }


	Api.commonApiPost('billing-service/demand/_update', {}, body, false, true).then((res)=>{

	}).catch((err)=> {
		console.log(err)
	})

  }


validateCollection = (index) => {

	var current = this;

	var demands = [];
	var collections = [];


	setTimeout(() => {

		let {addDemand} = current.props;

		//console.log(addDemand);

		for(var key in addDemand) {
			if(addDemand.hasOwnProperty(key)){
				if(key.match('collections')) {
					collections.push(addDemand[key])
				} else {
					demands.push(addDemand[key])
				}
			}
		}

		console.log(collections, demands);

		for(var i=0; i<collections.length;i++){
			var count = 0;
			for (var key in collections[i]){
				if(collections[i][key] && demands[i]["demand" + count] && Number(collections[i][key]) > Number(demands[i]["demand" + count])){
					console.log(collections[i][key]);
					console.log(demands[i]["demand" + count]);
					current.setState({
						hasError: true
					})
					return false;
				} else {
					current.setState({
						hasError: false
					})
				}
				count++;
			}
		}


	}, 100)
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
    let { demandDetailBeans } = this.state;

    let cThis = this;

    const handleChangeSrchRslt = function(e, name, ind){
    var _emps = Object.assign([], this.state.demandDetailBeans);
    _emps[ind][name] = e.target.value;
    cThis.setState({
        ...cThis.state,
        demandDetailBeans: _emps
    })
  }

	const showfields = () => {
    if(demandDetailBeans.length>0) {
      return demandDetailBeans.map((item, index)=> {
            return (<tr key={index}>
                    <td data-label="taxPeriod">{item.taxPeriod}</td>
                    <td data-label="taxHeadMasterCode">{item.taxPeriod}</td>
                    <td data-label="taxAmount">
                    <input type="number" id={item.id} name="taxAmount"  value={item.taxAmount}
                      onChange={(e)=>{handleChangeSrchRslt(e, "noOfDays", index)}} />
                    </td>
                    <td data-label="collectionAmount">
                    <input type="number" id={item.id} name="taxAmount"  value={item.collectionAmount}
                      onChange={(e)=>{handleChangeSrchRslt(e, "noOfDays", index)}} />
                    </td>
                </tr>
            );
      })
  }

	}


    return (<div><Card className="uiCard">
				<CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>{translate('pt.create.demands.addDemand')}</div>} />
				<CardText style={styles.reducePadding}>
					<Grid fluid>
						<Row>
							 <Col xs={12}>
								<h5>Assessment Number : <span style={{fontWeight:400}}>{this.props.match.params.upicNumber}</span></h5>
								<br/>
								<Table style={{color:"black",fontWeight: "normal", marginBottom:0, minWidth:'100%', width:'auto'}}  bordered responsive>
									<thead>
										<tr>
											<th style={{textAlign:'center'}}>Installment</th>
											<th  style={{textAlign:'center'}}>Tax</th>
											<th style={{textAlign:'center'}}>Demand</th>
                      <th style={{textAlign:'center'}}>Collection</th>
										</tr>
									</thead>
									<tbody>

										{showfields()}
									</tbody>
								</Table>
							 </Col>
						</Row>
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
   toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
     dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
   }

});

export default connect(mapStateToProps, mapDispatchToProps)(AddDemand);
