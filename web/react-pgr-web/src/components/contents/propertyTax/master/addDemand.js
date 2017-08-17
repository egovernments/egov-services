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
import {translate} from '../../../common/common';
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

const getHeadByCode = function(object, code) {
	object.map((item, index)=>{
		if(item.code == code){
			console.log(item.name);
			return item.name;
		}
	})
}

class AddDemand extends Component {

  constructor(props) {
    super(props);
    this.state= {
		taxHeads: [],
		demands: [],
		hasError: false,
		errorMsg: 'Invalid',
    }
  }


 componentDidMount() {
    //call boundary service fetch wards,location,zone data
    var currentThis = this;

	let {toggleSnackbarAndSetText, initForm, setLoadingStatus} = this.props;
	
	initForm();

	var getDemands = {
		upicNumber: this.props.match.params.upicNumber
	}

	setLoadingStatus('loading');
	
	Api.commonApiPost('pt-property/properties/_preparedcb', getDemands, {}, false, true).then((res)=>{

		console.log('search',res);

		 currentThis.setState({
			 demands: res.Demands
		 })

		res.Demands.map((demand, index)=>{
			
			demand.demandDetails.map((item, i)=>{
				setLoadingStatus('loading');
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

    let {search, handleDepartment, getTaxHead, validateCollection} = this;

    let cThis = this;

	const showfields = () => {

		if(this.state.demands.length !=0){

		return this.state.demands.map((demand, index)=> {

			return(
				<tr key={index}>
					<td style={{width:100}} className="lastTdBorder">{new Date(demand.taxPeriodFrom).getFullYear()} - {new Date(demand.taxPeriodTo).getFullYear()}</td>
						{demand.demandDetails.map((detail, i)=>{
							
							if(!addDemand.hasOwnProperty('demands'+index)){
								var e = {
									target: {
										value : detail.taxAmount
									}
								}
								handleChangeNextOne(e ,"demands"+index,"demand"+i, false, '')
							}
							
							if((demand.demandDetails.length-1) == i){
								return (
									<td key={i} className="lastTdBorder">
										<TextField  className="fullWidth"
										  floatingLabelText={<span style={{fontSize:'14px'}}>Demand</span>}
										  type="number"
										  value={(addDemand['demands'+index] ? addDemand['demands'+index]['demand'+i] : detail.taxAmount) || (detail.taxAmount ? detail.taxAmount : '')}
										  onChange={(e) => {
											  if(addDemand.hasOwnProperty('collections'+index) && addDemand['collections'+index].hasOwnProperty('collection'+i) && addDemand['collections'+index]['collection'+i]) {
												  validateCollection(i)
											  } else {
												  validateCollection(i);
											  }
											  handleChangeNextOne(e,"demands"+index,"demand"+i, false, '')}}
										  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
										  underlineStyle={styles.underlineStyle}
										  underlineFocusStyle={styles.underlineFocusStyle}
										  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										/>
									</td>)
							} else {
								return (
									<td key={i}>
										<TextField  className="fullWidth"
										  floatingLabelText={<span style={{fontSize:'14px'}}>Demand</span>}
										  type="number"
										  value={(addDemand['demands'+index] ? addDemand['demands'+index]['demand'+i] : detail.taxAmount) || (detail.taxAmount ? detail.taxAmount : '')}
										  onChange={(e) => {
											  if(addDemand.hasOwnProperty('collections'+index) && addDemand['collections'+index].hasOwnProperty('collection'+i) && addDemand['collections'+index]['collection'+i]) {
												  validateCollection(i)
											  } else {
												  validateCollection(i);
											  }
											  handleChangeNextOne(e,"demands"+index,"demand"+i, false, '')
										  }}
										  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
										  underlineStyle={styles.underlineStyle}
										  underlineFocusStyle={styles.underlineFocusStyle}
										  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
										/>
									</td>)
							}

						})}
						{demand.demandDetails.map((detail, i)=>{
							if(!addDemand.hasOwnProperty('collections'+index)){
								var e = {
									target: {
										value : detail.collectionAmount
									}
								}
								handleChangeNextOne(e ,"collections"+index,"collection"+i, false, '')
							}
							return (
							<td key={i} >
								<TextField  className="fullWidth"
								  floatingLabelText={<span style={{fontSize:'14px'}}>Collection</span>}
								  value={addDemand['collections'+index] ? addDemand['collections'+index]['collection'+i] : ''}
								  type="number"
								  onChange={(e) => {
									  if(addDemand.hasOwnProperty('demands'+index) && addDemand['demands'+index].hasOwnProperty('demand'+i) && addDemand['demands'+index]['demand'+i]) {
										  validateCollection(i)
									  } else {
										  validateCollection(i);
									  }

									  handleChangeNextOne(e,"collections"+index,"collection"+i, false, '')
								  }}
								  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
								  underlineStyle={styles.underlineStyle}
								  underlineFocusStyle={styles.underlineFocusStyle}
								  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
								/>
							</td>)
						})}
				</tr>
			)

		})
		}
	}

	const showSubHeading = () => {
		if(this.state.demands.length !=0){
			return this.state.demands[0].demandDetails.map((detail, index)=>{
			if((this.state.demands[0].demandDetails.length-1) == index){
				return (
				<td key={index} className="lastTdBorder">{(cThis.state.taxHeads.length != 0) && cThis.state.taxHeads.map((e,i)=>{
					if(e.code == detail.taxHeadMasterCode){
						return(<span key={i} style={{fontWeight:500}}>{e.name ? e.name : 'NA'}</span>);
					}
				})}</td>)
			} else {
				return (<td key={index}>{(cThis.state.taxHeads.length != 0) && cThis.state.taxHeads.map((e,i)=>{
					if(e.code == detail.taxHeadMasterCode){
						return(<span key={i} style={{fontWeight:500}}>{e.name ? e.name : 'NA'}</span>);
					}
				})}</td>)
			}

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
											<th style={{textAlign:'center'}}>Period</th>
											<th colSpan={this.state.demands.length !=0 && this.state.demands[0].demandDetails.length} style={{textAlign:'center'}}>Demand</th>
											<th colSpan={this.state.demands.length !=0 && this.state.demands[0].demandDetails.length} style={{textAlign:'center'}}>Collection</th>
										</tr>
									</thead>
									<tbody>
										<tr>
										  <td className="lastTdBorder"></td>
										{showSubHeading()}
										{showSubHeading()}
										</tr>
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
