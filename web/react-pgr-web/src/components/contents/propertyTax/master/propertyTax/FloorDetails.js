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

const  getNameById = (object, code, property = "") => {
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

class FloorDetails extends Component {
	
  constructor(props) {
    super(props);
    this.state= {
		unitType:[{code:"FLAT", name:'Flat'}, {code:"ROOM", name:'Room'}],
		floorNumber:[{code:1, name:'Basement-3'},{code:2, name:'Basement-2'},{code:3, name:'Basement-1'},{code:4, name:'Ground Floor'}],
		rooms: [],
		structureclasses:[],
		occupancies:[],
		usages:[],
		hasLengthWidth: false,
		roomInFlat:[{code:1, name:'Yes'}, {code:2, name:'No'}]
    }
  }

  componentDidMount() {
    //call boundary service fetch wards,location,zone data
    var currentThis = this;

        Api.commonApiPost('pt-property/property/structureclasses/_search').then((res)=>{
          console.log(res);
          currentThis.setState({structureclasses: res.structureClasses})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('pt-property/property/occuapancies/_search').then((res)=>{
          console.log(res);
          currentThis.setState({occupancies : res.occuapancyMasters})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('pt-property/property/usages/_search').then((res)=>{
          console.log(res);
          currentThis.setState({usages : res.usageMasters})
        }).catch((err)=> {
          console.log(err)
        })
		
		var temp = this.state.floorNumber;
		
		for(var i=5;i<=34;i++){
			var label = 'th';
			if((i-4)==1){
				label = 'st';
			} else if ((i-4)==2){
				label = 'nd';
			} else if ((i-4)==3){
				label = 'rd';
			}
			var commonFloors = {
				id:i,
				name:(i-4)+label+" Floor"
			}
			temp.push(commonFloors);
			
		}
		
		this.setState({
				floorNumber:temp
			});
	
		this.createFloorObject();
  }
  
  createFloorObject = () => {
	  
		let {floorDetails} = this.props;
		
		floorDetails.floorsArr = [];
		
		var allFloors = this.state.floorNumber;
	  
	    var allRooms = floorDetails.floors;
		
		if(!allRooms){
			return false;
		}
		
		var rooms = allRooms.filter(function(item, index, array){
			if(!item.hasOwnProperty('flatNo')){
				return item;
			}
		});
		
		rooms.map((item, index)=>{
			var floor = {
				  floorNo:'',
				  units:[]
				}
			  floor.floorNo = item.floorNo;
			  floor.units.push(item);
			  floorDetails.floorsArr.push(floor);
		})
		
		var flats = allRooms.filter(function(item, index, array){
			if(item.hasOwnProperty('flatNo')){
				return item;
			}
		});		
		
		var flatNos = flats.map(function(item, index, array){
			return item.flatNo;
		});
		
		flatNos = flatNos.filter(function(item, index, array){
			return index == array.indexOf(item);
		});
		
		var floorNos = flats.map(function(item, index, array){
			return item.floorNo;
		});
		
		floorNos = floorNos.filter(function(item, index, array){
			return index == array.indexOf(item);
		});
		
		
		var flatsArray = [];
		
		
		for(var i =0;i<flatNos.length;i++){
			var local = {
				units : []
			}
			for(var j = 0;j<flats.length;j++){
				if(flats[j].flatNo == flatNos[i]) {
					local.units.push(flats[j])
				}
			}
			flatsArray.push(local);
		}
		
		
	var finalFloors = [];
		for(let j=0;j<floorNos.length;j++){
			
			var local = {
				flats : []
			}
			for(let k =0;k<flatsArray.length;k++){
				for(let l=0;l<flatsArray[k].units.length;l++){
					if(flatsArray[k].units[l].floorNo == floorNos[j]){
						for(let m = 0 ; m<flatNos.length;m++){
							if(flatNos[m] == flatsArray[k].units[l].flatNo){
								local.flats.push(flatsArray[k].units[l]);
							}
						}
							
					}
				}
			}
			finalFloors.push(local);
		}
		
		var finalFlats = [];
		
		for(let x = 0;x<finalFloors.length;x++){
			var temp = finalFloors[x].flats;
			for(var i =0;i<finalFloors[x].flats.length;i++){
				var local= {
					units:[]
				};
				for(var j = 0;j<temp.length;j++){
					if(temp[j].flatNo == finalFloors[x].flats[i].flatNo) {
						local.units.push(temp[j])
					}
				}
			finalFlats.push(local);
			}
			
		}
		
		finalFlats = finalFlats.map((item, index)=>{
			return JSON.stringify(item);
		})
		
		finalFlats = finalFlats.filter((item, index, array)=>{
			return index == array.indexOf(item);
		})
		
		finalFlats = finalFlats.map((item, index)=>{
			return JSON.parse(item);
		})
		
		finalFlats.map((item, index)=>{
			let floor = {
				  floorNo:'',
				  units:[]
				}
			  floor.floorNo = item.units[0].floorNo;
			  floor.units.push(item);
			  floorDetails.floorsArr.push(floor);
		})
		
  }
  
	formatDate(date){
		return date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
	}
	
componentDidUpdate(){

}

handleAge = (year) => {
/*	var query = {
		fromYear : year,
		toYear: year
	}
	var currentThis = this;

	Api.commonApiPost('/property/depreciations/_search',query).then((res)=>{
	  console.log(res);
	  currentThis.setState({structureclasses: res.structureClasses})
	}).catch((err)=> {
	  console.log(err)
	})*/
}	
	
calcArea = (e, type) => {
		let {floorDetails, handleChangeNextOne} = this.props;
		
		let f = {
			target:{
				value: ''
			}
		}
		
		let hasLW = false
		
		if(floorDetails.hasOwnProperty('floor')) {
			if(type == 'length' && floorDetails.floor.hasOwnProperty('width')){
			
				f.target.value = parseInt(e.target.value) * parseInt(floorDetails.floor.width)
		
				if(!f.target.value){ 
					hasLW = false;
				} else {
					hasLW = true;
				}
				
				handleChangeNextOne(f, "floor","assessableArea", false, "");
				handleChangeNextOne(f, "floor","builtupArea", true, "");
				
			} else if(type == 'width' 	&& floorDetails.floor.hasOwnProperty('length')){
			
				f.target.value = parseInt(floorDetails.floor.length) * parseInt(e.target.value)
				
				if(!f.target.value){ 
					hasLW = false;
				} else {
					hasLW = true;
				}
				
				handleChangeNextOne(f, "floor","assessableArea", false, "");
				handleChangeNextOne(f, "floor","builtupArea", true, "");
				
			} 			
		}

		this.setState({
			hasLengthWidth: hasLW
		});
}	
 
  
   render(){
	  
		var _this = this;   
		   
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
		  owners,
		  floorDetails,
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
		  toggleDailogAndSetText,
		  setLoadingStatus,
		  toggleSnackbarAndSetText
				} = this.props;

				let {handleAge} = this;
		let cThis = this;
		
		console.log(floorDetails);
	   return(
			<Card>
                <CardHeader style={styles.reducePadding}  title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Floor Details</div>} />
				<CardText style={styles.reducePadding}>
					<Card className="darkShadow">
							<CardText style={styles.reducePadding}>
								<Grid fluid>
									<Row>
										 <Col xs={12} md={12}>
											<Row>
												<Row>
													<Col xs={12} md={3} sm={6}>
														<SelectField  className="fullWidth selectOption"
														  floatingLabelText="Floor Number *"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.floorNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.floorNo}</span>:"") : ""}
														  value={floorDetails.floor ? floorDetails.floor.floorNo : ""}
														  onChange={(event, index, value) => {
															  var e = {
																target: {
																  value: value
																}
															  };
																
															  handleChangeNextOne(e, "floor","floorNo", true, "")}
														  }
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														>
														{renderOption(_this.state.floorNumber)}
														</SelectField>
													</Col>
													<Col xs={12} md={3} sm={6}>
														 <SelectField  className="fullWidth selectOption"
															floatingLabelText="Unit Type"
															errorText={fieldErrors.floor ? (fieldErrors.floor.unitType ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.unitType}</span>:"" ): ""}
															value={floorDetails.floor ? floorDetails.floor.unitType : ""}
															onChange={(event, index, value) => {
																var e = {
																  target: {
																	value: value
																  }
																};
																
																floorDetails.floor.electricMeterNo = null;
																floorDetails.floor.waterMeterNo = null;
																floorDetails.floor.exemptionReason = null;
																floorDetails.floor.rentCollected = null;
																floorDetails.floor.age = '0TO25';

																handleChangeNextOne(e,"floor" ,"unitType", true, "");
																
															  }
															}
															floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
															underlineStyle={styles.underlineStyle}
															underlineFocusStyle={styles.underlineFocusStyle}
															floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														  >
														  {renderOption(_this.state.unitType)}
														</SelectField>
													</Col>
													{(floorDetails.floor ? (floorDetails.floor.unitType == 'FLAT' ? true: false ): false) &&
														<span>
															<Col xs={12} md={3} sm={6}>
																 <SelectField  className="fullWidth selectOption"
																	floatingLabelText="Is Room in Flat?"
																	errorText={fieldErrors.roomInFlat ? (fieldErrors.floor.roomInFlat ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.roomInFlat}</span>:"" ): ""}
																	value={floorDetails.floor ? floorDetails.floor.roomInFlat : ""}
																	onChange={(event, index, value) => {
																		var e = {
																		  target: {
																			value: value
																		  }
																		};
																		handleChangeNextOne(e,"floor" ,"roomInFlat", true, "");
																		if(value == 2) {
																		  this.setState({addRoom:false});
																		}
																	  }
																	}
																	floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
																	underlineStyle={styles.underlineStyle}
																	underlineFocusStyle={styles.underlineFocusStyle}
																	floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
																  >
																  {renderOption(_this.state.roomInFlat)}																								
																</SelectField>
															</Col>	
															{(floorDetails.floor ? (floorDetails.floor.roomInFlat == '1' ? true: false ): false) && 
																<Col xs={12} md={3} sm={6}>			
																	<TextField  className="fullWidth"
																	  hintText="201"
																	  floatingLabelText="Flat Number *"
																	  errorText={fieldErrors.floor ? (fieldErrors.floor.flatNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.flatNo}</span> :""): ""}
																	  value={floorDetails.floor ? floorDetails.floor.flatNo : ""}
																	  onChange={(e) => {handleChangeNextOne(e,"floor" ,"flatNo", true, /^\d+$/g)}}
																	  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
																	  underlineStyle={styles.underlineStyle}
																	  underlineFocusStyle={styles.underlineFocusStyle}
																	  maxLength={3}
																	  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}/>
																</Col>
															}
															
														</span>	  
														
													}
													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Unit Number *"
														  hintText="102"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.unitNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.unitNo}</span> :""): ""}
														  value={floorDetails.floor ? floorDetails.floor.unitNo : ""}
														  onChange={(e) => {handleChangeNextOne(e,"floor" ,"unitNo", true, /^\d{3}$/g)}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={3}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>		
													<Col xs={12} md={3} sm={6}>
														<SelectField  className="fullWidth selectOption"
														  floatingLabelText="Construction type *"
														  errorText={fieldErrors.floor ?(fieldErrors.floor.structure? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.structure}</span>:"" ): ""}
														  value={floorDetails.floor ? floorDetails.floor.structure : ""}
														  onChange={(event, index, value) => {
															  var e = {
																target: {
																  value: value
																}
															  };
															  handleChangeNextOne(e,"floor" ,"structure", true, "")}
														  }
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														>
															  {renderOption(this.state.structureclasses)}
														</SelectField>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<SelectField  className="fullWidth selectOption"
														  floatingLabelText="Usage type *"
														  errorText={fieldErrors.floor ?(fieldErrors.floor.usage? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.usage}</span>:"" ): ""}
														  value={floorDetails.floor ? floorDetails.floor.usage : ""}
														  onChange={(event, index, value) => {
															  var e = {
																target: {
																  value: value
																}
															  };
															  handleChangeNextOne(e,"floor" ,"usage", true, "")}
														  }
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														>
															  {renderOption(this.state.usages)}

														</SelectField>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<SelectField  className="fullWidth selectOption"
														  floatingLabelText="Usage sub type *"
														  errorText={fieldErrors.floor ?(fieldErrors.floor.usageSubType ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.usageSubType}</span> :""): ""}
														  value={floorDetails.floor ? floorDetails.floor.usageSubType : ""}
														  onChange={(event, index, value) => {
															  var e = {
																target: {
																  value: value
																}
															  };
															  handleChangeNextOne(e,"floor" ,"usageSubType", true, "")}
														  }
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														>
															{renderOption(this.state.usages)}
														</SelectField>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Firm Name"
														  errorText={fieldErrors.floor ?(fieldErrors.floor.firmName? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.firmName}</span>:"") : ""}
														  value={floorDetails.floor ? floorDetails.floor.firmName : ""}
														  onChange={(e) => {handleChangeNextOne(e,"floor" ,"firmName", false, "")}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={20}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<SelectField  className="fullWidth selectOption"
														  floatingLabelText="Occupancy *"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.occupancyType?<span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.occupancyType}</span>:"") : ""}
														  value={floorDetails.floor ? floorDetails.floor.occupancyType : ""}
														  onChange={(event, index, value) => {
															  var e = {
																target: {
																  value: value
																}
															  };
															  handleChangeNextOne(e,"floor" ,"occupancyType", true, "")}
														  }
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														>
															  {renderOption(this.state.occupancies)}
														</SelectField>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Occupant Name"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.occupierName? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.occupierName}</span> :""): ""}
														  value={floorDetails.floor ? floorDetails.floor.occupierName : ""}
														  onChange={(e) => {handleChangeNextOne(e,"floor" , "occupierName", false, "")}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={32}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
													
													{(floorDetails.floor? !getNameById(this.state.occupancies,floorDetails.floor.occupancyType).match('Owner') : false) 
													
													&& <Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Annual Rent"
														  hintText="15000"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.annualRent ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.annualRent}</span>:""): ""}
														  value={floorDetails.floor ? floorDetails.floor.annualRent : ""}
														  onChange={(e) => {handleChangeNextOne(e,"floor" , "annualRent", false, /^\d{9}$/g)}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={9}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>}
													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Manual ARV"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.manualArv?<span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.manualArv}</span>:"") : ""}
														  value={floorDetails.floor ? floorDetails.floor.manualArv : ""}
														  onChange={(e) => {handleChangeNextOne(e,"floor" , "manualArv", false, /^\d{9}$/g)}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={9}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<DatePicker  className="fullWidth datepicker"
														  formatDate={(date)=> this.formatDate(date)}
														  floatingLabelText="Construction Date *"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.constCompletionDate ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.constCompletionDate}</span> :""): ""}
														  defaultDate={ floorDetails.floor ? (floorDetails.floor.constCompletionDate ? new Date(floorDetails.floor.constCompletionDate) : new Date() ):  new Date()}
														  onChange={(event,date) => {
															  var e = {
																target:{
																	value: date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear()
																}
															  }
															handleAge(date.getFullYear());
															handleChangeNextOne(e,"floor" ,"constCompletionDate", true, "")}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  textFieldStyle={{width: '100%'}}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<DatePicker  className="fullWidth datepicker"
														formatDate={(date)=> this.formatDate(date)}
														  floatingLabelText="Effective From Date *"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.occupancyDate ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.occupancyDate}</span> : "") : ""}
														  defaultDate={ floorDetails.floor ? (floorDetails.floor.occupancyDate ? new Date(floorDetails.floor.occupancyDate) : new Date() ):  new Date()}
														  onChange={(event,date) => {
															  var e = {
																target:{
																	value: date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear()
																}
															  }
															  handleChangeNextOne(e,"floor" ,"occupancyDate", true, "")}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  textFieldStyle={{width: '100%'}}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<SelectField  className="fullWidth selectOption"
														  floatingLabelText="Unstructured land *"
														  errorText={fieldErrors.floor ? ( fieldErrors.floor.isStructured?<span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.isStructured}</span>:"") : ""}
														  value={floorDetails.floor ? floorDetails.floor.isStructured : ""}
														  onChange={(event, index, value) => {
															  var e = {
																target: {
																  value: value
																}
															  };
															  handleChangeNextOne(e, "floor" ,"isStructured", true, "")}
														  }
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														>
															  <MenuItem value={1} primaryText="Yes" />
															  <MenuItem value={2} primaryText="No" />
														</SelectField>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Length"
														  hintText="12.50"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.length ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.length}</span>:"") : ""}
														  value={floorDetails.floor ? floorDetails.floor.length : ""}
														  onChange={(e) => {
															  handleChangeNextOne(e,"floor" ,"length", false, /^[0-9.]+$/);
															  cThis.calcArea(e, 'length');
														  }}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={6}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  hintText="15.25"
														  floatingLabelText="Breadth"
														  errorText={fieldErrors.floor ?(fieldErrors.floor.width ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.width}</span> :""): ""}
														  value={floorDetails.floor ? floorDetails.floor.width : ""}
														  onChange={(e) => {
															  handleChangeNextOne(e,"floor" ,"width", false, /^[0-9.]+$/);
															  cThis.calcArea(e, 'width');
															}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={6}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
													
													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Plinth Area *"
														  hintText="27.75"
														  errorText={fieldErrors.floor ?(fieldErrors.floor.builtupArea? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.builtupArea}</span>:"" ): ""}
														  value={floorDetails.floor ? floorDetails.floor.builtupArea : ""}
														  onChange={(e) => {
															  handleChangeNextOne(e, "floor","assessableArea", false, "")
															  handleChangeNextOne(e, "floor","builtupArea", true, "")
															}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={6}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														  disabled={this.state.hasLengthWidth}
														/>
													</Col>

													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Occupancy Certificate Number"
														  errorText={fieldErrors.floor ?(fieldErrors.floor.occupancyCertiNumber? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.occupancyCertiNumber}</span>:"" ): ""}
														  value={floorDetails.floor ? floorDetails.floor.occupancyCertiNumber : ""}
														  onChange={(e) => {handleChangeNextOne(e,"floor" ,"occupancyCertiNumber", false, /^\d[a-zA-Z0-9]{9}$/g)}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={10}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
												
													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Building Permission number"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.bpaNo? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.bpaNo}</span>:"" ): ""}
														  value={floorDetails.floor ? floorDetails.floor.bpaNo : ""}
														  onChange={(e) => {handleChangeNextOne(e,"floor" ,"bpaNo", false, /^\d[a-zA-Z0-9]{14}$/g)}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={15}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<DatePicker  className="fullWidth datepicker"
														formatDate={(date)=> this.formatDate(date)}
														  floatingLabelText="Building Permission Date"
														  errorText={fieldErrors.floor ?(fieldErrors.floor.bpaDate? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.bpaDate}</span>:"") : ""}
															defaultDate={ floorDetails.floor ? (floorDetails.floor.bpaDate ? new Date(floorDetails.floor.bpaDate) : new Date() ):  new Date()}
														  onChange={(event,date) => {
															  var e = {
																target:{
																	value: date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear()
																}
															  }
															  handleChangeNextOne(e,"floor" ,"buildingPermissionDate", false, "")}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  textFieldStyle={{width: '100%'}}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>
													<Col xs={12} md={3} sm={6}>
														<TextField  className="fullWidth"
														  floatingLabelText="Plinth area in Building plan"
														  errorText={fieldErrors.floor ? (fieldErrors.floor.bpaBuiltupArea? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.bpaBuiltupArea}</span>:"") : ""}
														  value={floorDetails.floor ? floorDetails.floor.bpaBuiltupArea : ""}
														  onChange={(e) => {handleChangeNextOne(e, "floor","bpaBuiltupArea", false,  /^\d{6}$/g)}}
														  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
														  underlineStyle={styles.underlineStyle}
														  underlineFocusStyle={styles.underlineFocusStyle}
														  maxLength={6}
														  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
														/>
													</Col>

													<Col xs={12} style={{textAlign:"right"}}>
														<br/>
														{(editIndex == -1 || editIndex == undefined) && <RaisedButton type="button" label="Add Room"  backgroundColor="#0b272e" labelColor={white} onClick={()=>{
															 this.props.addNestedFormData("floors","floor");
															 this.props.resetObject("floor");
															 setTimeout(()=>{
																	_this.createFloorObject();
																}, 300);
															}	
														}/>}
														{ (editIndex > -1) &&
															<RaisedButton type="button" label="Update Room"  backgroundColor="#0b272e" labelColor={white} onClick={()=> {
																  this.props.updateObject("floors","floor",  editIndex);
																  this.props.resetObject("floor");
																  isEditIndex(-1);
																  setTimeout(()=>{
																	_this.createFloorObject();
																	}, 300);
																}
															}/>
														}
													</Col>
													
												</Row>
												{ floorDetails.floors &&
                                            <div> <br/>
                                          <Table id="floorDetailsTable" style={{color:"black",fontWeight: "normal", marginBottom:0}} bordered responsive>
                                          <thead style={{backgroundColor:"#607b84",color:"white"}}>
                                            <tr>
                                              <th>#</th>
											  <th>Floor No.</th>
                                              <th>Unit Type</th>
											  <th>Flat No.</th>
                                              <th>Unit No.</th>
                                              <th>Construction Type</th>
                                              <th>Usage Type</th>
                                              <th>Usage Sub Type</th>
                                              <th>Firm Name</th>
                                              <th>Occupancy</th>
                                              <th>Occupant Name</th>
                                              <th>Annual Rent</th>
                                              <th>Manual ARV</th>
                                              <th>Construction Date</th>
                                              <th>Effective From Date</th>
                                              <th>Unstructured land</th>
                                              <th>Length</th>
                                              <th>Breadth</th>
                                              <th>Plinth Area</th>
                                              <th>Occupancy Certificate Number</th>
                                              <th>Building Permission Number</th>
                                              <th>Building Permission Date</th>
                                              <th>Plinth Area In Building Plan</th>
                                              <th style={{minWidth:70}}></th>
                                            </tr>
                                          </thead>
                                          <tbody>
                                            {floorDetails.floors && floorDetails.floors.map(function(i, index){
                                              if(i){
                                                return (<tr key={index}>
                                                    <td>{index}</td>
                                                    <td>{getNameById(_this.state.floorNumber ,i.floorNo)}</td>
													<td>{getNameById(_this.state.unitType ,i.unitType)}</td>
													<td>{i.flatNo ? i.flatNo : ''}</td>
                                                    <td>{i.unitNo}</td>
                                                    <td>{i.structure}</td>
                                                    <td>{i.usageType}</td>
                                                    <td>{i.usageSubType}</td>
                                                    <td>{i.firmName}</td>
                                                    <td>{i.occupancyType}</td>
                                                    <td>{i.occupierName}</td>
                                                    <td>{i.annualRent}</td>
                                                    <td>{i.manualArv}</td>
                                                    <td>{i.constCompletionDate}</td>
                                                    <td>{i.occupancyDate}</td>
                                                    <td>{i.isStructured}</td>
                                                    <td>{i.length}</td>
                                                    <td>{i.width}</td>
                                                    <td>{i.builtupArea}</td>
                                                    <td>{i.occupancyCertiNumber}</td>
                                                    <td>{i.bpaNo}</td>
                                                    <td>{i.bpaDate}</td>
                                                    <td>{i.bpaBuiltupArea}</td>
                                                    <td>
														<i className="material-icons" style={styles.iconFont} onClick={ () => {
															editObject("floor",i);
															toggleSnackbarAndSetText(true, 'Edit room details and update.')
															isEditIndex(index);
                                                         }}>mode_edit</i>
                                                         <i className="material-icons" style={styles.iconFont} onClick={ () => {
															  deleteObject("floors", index);
																isEditIndex(-1);
																setTimeout(()=>{
																	_this.createFloorObject();
																}, 300);
                                                         }}>delete</i>
                                                    </td>
                                                  </tr>)
                                              }

                                            })}
                                          </tbody>
                                          </Table>


                                                </div>}

											</Row>
										</Col>
									</Row>
								</Grid>
							</CardText>
					</Card>
				</CardText>
			</Card>
	   )
   } 

}

const mapStateToProps = state => ({
  floorDetails:state.form.form,
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

  deleteNestedObject: (property,position, propertyOne, subPosition) => {
    dispatch({
      type: "DELETE_NESTED_OBJECT",
      property,
	  position,
      propertyOne,
      subPosition
    })
  },

  editObject: (objectName, object) => {
    dispatch({
      type: "EDIT_OBJECT",
      objectName,
      object
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

  updateNestedObject:  (property,position, propertyOne, subPosition) => {
    dispatch({
      type: "UPDATE_NESTED_OBJECT",
      property,
	  position,
      propertyOne,
	  subPosition
    })
  },

  isEditIndex: (index) => {
    dispatch({
      type: "EDIT_INDEX",
      index
    })
  },
  
	setLoadingStatus: (loadingStatus) => {
	  dispatch({type: "SET_LOADING_STATUS", loadingStatus});
	},
	toggleDailogAndSetText: (dailogState,msg) => {
	  dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState, msg});
	},
	toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
	  dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
	},

});

export default connect(mapStateToProps, mapDispatchToProps)(FloorDetails);