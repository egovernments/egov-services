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


class FloorDetails extends Component {

  constructor(props) {
    super(props);
    this.state= {
        propertytypes: [],
        apartments:[],
        departments:[],
        rooms: [],
        floortypes:[],
      rooftypes:[],
      walltypes:[],
      woodtypes:[],
      structureclasses:[],
      occupancies:[],
      ward:[],
      locality:[],
      zone:[],
      block:[],
      street:[],
      revanue:[],
      election:[],
      usages:[],
    }
  } 


  componentDidMount() {
    //call boundary service fetch wards,location,zone data
    var currentThis = this;

        Api.commonApiPost('property/structureclasses/_search').then((res)=>{
          console.log(res);
          currentThis.setState({structureclasses: res.structureClasses})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/occupancies/_search').then((res)=>{
          console.log(res);
          currentThis.setState({occupancies : res.occupancies})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('property/usages/_search').then((res)=>{
          console.log(res);
          currentThis.setState({usages : res.usageMasters})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"WARD", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({ward : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"LOCALITY", hierarchyTypeName:"LOCATION"}).then((res)=>{
          console.log(res);
          currentThis.setState({locality : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"ZONE", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({zone : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"BLOCK", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({block : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"STREET", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({street : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"REVANUE", hierarchyTypeName:"REVANUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({street : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"ELECTION", hierarchyTypeName:"ADMINISTRATION"}).then((res)=>{
          console.log(res);
          currentThis.setState({election : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })
		
		this.setRoomsInArray();
  }  

  setRoomsInArray = () => {

     var currentThis = this;

    setTimeout(function() {
         var temArray = [];

    let {floorDetails} = currentThis.props;

    if(floorDetails.floors){  
      for(let i = 0; i<floorDetails.floors.length;i++){
		 floorDetails.floors[i].uniquePosition = i;
        if(floorDetails.floors[i].unitType == 1){
			
          if(floorDetails.floors[i].units){
			
             for(let j=0; j< floorDetails.floors[i].units.length; j++){
				floorDetails.floors[i].units[j].uniquePosition = i;
				floorDetails.floors[i].units[j].uniqueSubPosition = j;
               temArray.push(floorDetails.floors[i].units[j]);
            }
          } else {
            temArray.push(floorDetails.floors[i]);
          }
        } else {
			temArray.push(floorDetails.floors[i]);
        }
      }

	     
      currentThis.setState({
        rooms : temArray
      })
    }

    }, 300)

    
  }

handleDelete = (position, subPosition, index) => {
	var temp = this.state.rooms;
	temp.splice(index, 1)
	this.setState({
		rooms: temp
	})
	if(subPosition != undefined) {
		this.props.deleteNestedObject('floors', position ,'units', subPosition);
	} else {
		this.props.deleteObject('floors', position);
	}
}  

handleEdit = (item, index) => {
	var object = this.state.rooms[index];
	if(item.uniqueSubPosition) {
		this.setState({
			addRoom:true
		});
		this.props.editObject('unit', item);
	} else {
		this.props.editObject('floor', item);
	} 
}

handleUpdate = (object) => {
		var position = object.uniquePosition;
		var subPosition = object.uniqueSubPosition;
		
		this.props.updateNestedObject("floors", position,"unit", subPosition);
}


  render() {
	  
	  console.log("Floor Details", this.state.rooms);

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
      isAddRoom
    } = this.props;

    let {search, handleDelete, handleEdit} = this;

    let cThis = this;

      const roomForm = () => (
        <div>
              <Col xs={12} md={3} sm={6}>
                  <SelectField  className="fullWidth selectOption"
                    floatingLabelText="Floor Number"
                    errorText={fieldErrors.unit ? (fieldErrors.unit.floorNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.floorNo}</span>:"") : ""}
                    value={floorDetails.unit ? floorDetails.unit.floorNo : ""}
                    onChange={(event, index, value) => {
                        var e = {
                          target: {
                            value: value
                          }
                        };
                        handleChangeNextOne(e, "unit","floorNo", true, "")}
                    }
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  >
                        <MenuItem value={1} primaryText="Ground floor" />
                        <MenuItem value={2} primaryText=" Basement-2" />
                        <MenuItem value={3} primaryText=" Basement-1" />
                  </SelectField>
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <TextField  className="fullWidth"
                    floatingLabelText="Unit Number"
                    errorText={fieldErrors.unit ? (fieldErrors.unit.unitNo ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.unitNo}</span> :""): ""}
                    value={floorDetails.unit ? floorDetails.unit.unitNo : ""}
                    onChange={(e) => {handleChangeNextOne(e,"unit" ,"unitNo", true, /^\d{3}$/g)}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    maxLength={3}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <SelectField  className="fullWidth selectOption"
                    floatingLabelText="Construction type"
                    errorText={fieldErrors.unit ?(fieldErrors.unit.constructionType? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.constructionType}</span>:"" ): ""}
                    value={floorDetails.unit ? floorDetails.unit.constructionType : ""}
                    onChange={(event, index, value) => {
                        var e = {
                          target: {
                            value: value
                          }
                        };
                        handleChangeNextOne(e,"unit" ,"constructionType", true, "")}
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
                    floatingLabelText="Usage type"
                    errorText={fieldErrors.unit ?(fieldErrors.unit.usage? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.usage}</span>:"" ): ""}
                    value={floorDetails.unit ? floorDetails.unit.usage : ""}
                    onChange={(event, index, value) => {
                        var e = {
                          target: {
                            value: value
                          }
                        };
                        handleChangeNextOne(e,"unit" ,"usage", true, "")}
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
                    floatingLabelText="Usage sub type"
                    errorText={fieldErrors.unit ?(fieldErrors.unit.usageSubType ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.usageSubType}</span> :""): ""}
                    value={floorDetails.unit ? floorDetails.unit.usageSubType : ""}
                    onChange={(event, index, value) => {
                        var e = {
                          target: {
                            value: value
                          }
                        };
                        handleChangeNextOne(e,"unit" ,"usageSubType", true, "")}
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
                    errorText={fieldErrors.unit ?(fieldErrors.unit.firmName? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.firmName}</span>:"") : ""}
                    value={floorDetails.unit ? floorDetails.unit.firmName : ""}
                    onChange={(e) => {handleChangeNextOne(e,"unit" ,"firmName", false, "")}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <SelectField  className="fullWidth selectOption"
                    floatingLabelText="Occupancy"
                    errorText={fieldErrors.unit ? (fieldErrors.unit.occupancyType?<span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.occupancyType}</span>:"") : ""}
                    value={floorDetails.unit ? floorDetails.unit.occupancyType : ""}
                    onChange={(event, index, value) => {
                        var e = {
                          target: {
                            value: value
                          }
                        };
                        handleChangeNextOne(e,"unit" ,"occupancyType", true, "")}
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
                    errorText={fieldErrors.unit ? (fieldErrors.unit.occupierName? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.occupierName}</span> :""): ""}
                    value={floorDetails.unit ? floorDetails.unit.occupierName : ""}
                    onChange={(e) => {handleChangeNextOne(e,"unit" , "occupierName", false, "")}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <div className="clearfix"></div>
              <Col xs={12} md={3} sm={6}>
                  <TextField  className="fullWidth"
                    floatingLabelText="Annual Rent"
                    errorText={fieldErrors.unit ? (fieldErrors.unit.annualRent ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.annualRent}</span>:""): ""}
                    value={floorDetails.unit ? floorDetails.unit.annualRent : ""}
                    onChange={(e) => {handleChangeNextOne(e,"unit" , "annualRent", false, /^\d{9}$/g)}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <TextField  className="fullWidth"
                    floatingLabelText="Manual ARV"
                    errorText={fieldErrors.unit ? (fieldErrors.unit.manualArv?<span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.manualArv}</span>:"") : ""}
                    value={floorDetails.unit ? floorDetails.unit.manualArv : ""}
                    onChange={(e) => {handleChangeNextOne(e,"unit" , "manualArv", false, /^\d{9}$/g)}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <DatePicker  className="fullWidth datepicker"
                    floatingLabelText="Construction Date"
                    errorText={fieldErrors.unit ? (fieldErrors.unit.constructionDate ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.constructionDate}</span> :""): ""}
                    onChange={(event,date) => {
                        var e = {
                          target:{
                              value: date
                          }
                        }
                      handleChangeNextOne(e,"unit" ,"constructionDate", true, "")}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    textFieldStyle={{width: '100%'}}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <DatePicker  className="fullWidth datepicker"
                    floatingLabelText="Effective From Date"
                    errorText={fieldErrors.unit ? (fieldErrors.unit.effectiveDate ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.effectiveDate}</span> : "") : ""}
                    onChange={(event,date) => {
                        var e = {
                          target:{
                              value: date
                          }
                        }
                        handleChangeNextOne(e,"unit" ,"effectiveDate", true, "")}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    textFieldStyle={{width: '100%'}}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <SelectField  className="fullWidth selectOption"
                    floatingLabelText="Unstructured land"
                    errorText={fieldErrors.unit ? ( fieldErrors.unit.unstructuredLand?<span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.unstructuredLand}</span>:"") : ""}
                    value={floorDetails.unit ? floorDetails.unit.unstructuredLand : ""}
                    onChange={(event, index, value) => {
                        var e = {
                          target: {
                            value: value
                          }
                        };
                        handleChangeNextOne(e, "unit" ,"unstructuredLand", true, "")}
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
                    errorText={fieldErrors.unit ? (fieldErrors.unit.length ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.length}</span>:"") : ""}
                    value={floorDetails.unit ? floorDetails.unit.length : ""}
                    onChange={(e) => {handleChangeNextOne(e,"unit" ,"length", false, "")}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <TextField  className="fullWidth"
                    floatingLabelText="Breadth"
                    errorText={fieldErrors.unit ?(fieldErrors.unit.width ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.width}</span> :""): ""}
                    value={floorDetails.unit ? floorDetails.unit.width : ""}
                    onChange={(e) => {handleChangeNextOne(e,"unit" ,"width", false, "")}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <TextField  className="fullWidth"
                    floatingLabelText="Plinth Area"
                    errorText={fieldErrors.unit ?(fieldErrors.unit.plinthArea? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.plinthArea}</span>:"" ): ""}
                    value={floorDetails.unit ? floorDetails.unit.plinthArea : ""}
                    onChange={(e) => {handleChangeNextOne(e, "unit","plinthArea", true, "")}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <div className="clearfix"></div>
              <Col xs={12} md={3} sm={6}>
                  <TextField  className="fullWidth"
                    floatingLabelText="Occupancy Certificate Number"
                    errorText={fieldErrors.unit ?(fieldErrors.unit.occupancyCertiNumber? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.occupancyCertiNumber}</span>:"" ): ""}
                    value={floorDetails.unit ? floorDetails.unit.occupancyCertiNumber : ""}
                    onChange={(e) => {handleChangeNextOne(e,"unit" ,"occupancyCertiNumber", false, /^\d[a-zA-Z0-9]{9}$/g)}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <TextField  className="fullWidth"
                    floatingLabelText="Building Permission number"
                    errorText={fieldErrors.unit ? (fieldErrors.unit.bpaNo? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.bpaNo}</span>:"" ): ""}
                    value={floorDetails.unit ? floorDetails.unit.bpaNo : ""}
                    onChange={(e) => {handleChangeNextOne(e,"unit" ,"bpaNo", false, /^\d[a-zA-Z0-9]{14}$/g)}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <Col xs={12} md={3} sm={6}>
                  <DatePicker  className="fullWidth datepicker"
                    floatingLabelText="Building Permission Date"
                    errorText={fieldErrors.unit ?(fieldErrors.unit.bpaDate? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.bpaDate}</span>:"") : ""}
                    onChange={(event,date) => {
                        var e = {
                          target:{
                              value: date
                          }
                        }
                        handleChangeNextOne(e,"unit" ,"bpaDate", false, "")}}
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
                    errorText={fieldErrors.unit ? (fieldErrors.unit.plinthAreaBuildingPlan? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.unit.plinthAreaBuildingPlan}</span>:"") : ""}
                    value={floorDetails.unit ? floorDetails.unit.plinthAreaBuildingPlan : ""}
                    onChange={(e) => {handleChangeNextOne(e, "unit","plinthAreaBuildingPlan", false,  /^\d{6}$/g)}}
                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                    underlineStyle={styles.underlineStyle}
                    underlineFocusStyle={styles.underlineFocusStyle}
                    floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                  />
              </Col>
              <div className="clearfix"></div>
              <br/>

              <Col xs={12}  style={{textAlign:'right'}}>

                {((editIndex == -1 || editIndex == undefined) && true) &&
                  <RaisedButton  type="button" label="Add Room Details"  backgroundColor="#0b272e" labelColor={white} onClick={()=> {
					  
                  this.props.addNestedFormDataTwo("floor","units",'unit');
                  this.props.resetObject("unit");
                }}/>}
                { ((editIndex > -1) && true) &&
                  <RaisedButton type="button" label="Save Room Details"  backgroundColor="#0b272e" labelColor={white} onClick={()=> {
						this.handleUpdate(floorDetails.unit);
                      this.props.resetObject("unit");
                      isEditIndex(-1);
                  }}/>}
              </Col>
          </div>
      )

      let todayDate = new Date();

      //console.log(bpaDate, constructionDate, effectiveDate );

    const flatForm = () => {

    return(
      <div>
            <Col xs={12} md={3} sm={6}>
                <SelectField  className="fullWidth selectOption"
                  floatingLabelText="Floor Number"
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
                      <MenuItem value={1} primaryText="Ground floor" />
                      <MenuItem value={2} primaryText=" Basement-2" />
                      <MenuItem value={3} primaryText=" Basement-1" />
                </SelectField>
            </Col>
            <Col xs={12} md={3} sm={6}>
                <TextField  className="fullWidth"
                  floatingLabelText="Unit Number"
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
                  floatingLabelText="Construction type"
                  errorText={fieldErrors.floor ?(fieldErrors.floor.constructionType? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.constructionType}</span>:"" ): ""}
                  value={floorDetails.floor ? floorDetails.floor.constructionType : ""}
                  onChange={(event, index, value) => {
                      var e = {
                        target: {
                          value: value
                        }
                      };
                      handleChangeNextOne(e,"floor" ,"constructionType", true, "")}
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
                  floatingLabelText="Usage type"
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
                  floatingLabelText="Usage sub type"
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
                  floatingLabelText="Occupancy"
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
            <div className="clearfix"></div>
            <Col xs={12} md={3} sm={6}>
                <TextField  className="fullWidth"
                  floatingLabelText="Annual Rent"
                  errorText={fieldErrors.floor ? (fieldErrors.floor.annualRent ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.annualRent}</span>:""): ""}
                  value={floorDetails.floor ? floorDetails.floor.annualRent : ""}
                  onChange={(e) => {handleChangeNextOne(e,"floor" , "annualRent", false, /^\d{9}$/g)}}
                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                  underlineStyle={styles.underlineStyle}
                  underlineFocusStyle={styles.underlineFocusStyle}
                  maxLength={9}
                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                />
            </Col>
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
                  floatingLabelText="Construction Date"
                  errorText={fieldErrors.floor ? (fieldErrors.floor.constructionDate ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.constructionDate}</span> :""): ""}
                  defaultDate={ floorDetails.floor ? (floorDetails.floor.constructionDate ? new Date(floorDetails.floor.constructionDate) : new Date() ):  new Date()}
                  onChange={(event,date) => {
                      var e = {
                        target:{
                            value: date
                        }
                      }
                    handleChangeNextOne(e,"floor" ,"constructionDate", true, "")}}
                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                  underlineStyle={styles.underlineStyle}
                  underlineFocusStyle={styles.underlineFocusStyle}
                  textFieldStyle={{width: '100%'}}
                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                />
            </Col>
            <Col xs={12} md={3} sm={6}>
                <DatePicker  className="fullWidth datepicker"
                  floatingLabelText="Effective From Date"
                  errorText={fieldErrors.floor ? (fieldErrors.floor.effectiveDate ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.effectiveDate}</span> : "") : ""}
                  defaultDate={ floorDetails.floor ? (floorDetails.floor.effectiveDate ? new Date(floorDetails.floor.effectiveDate) : new Date() ):  new Date()}
                  onChange={(event,date) => {
                      var e = {
                        target:{
                            value: date
                        }
                      }
                      handleChangeNextOne(e,"floor" ,"effectiveDate", true, "")}}
                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                  underlineStyle={styles.underlineStyle}
                  underlineFocusStyle={styles.underlineFocusStyle}
                  textFieldStyle={{width: '100%'}}
                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                />
            </Col>
            <Col xs={12} md={3} sm={6}>
                <SelectField  className="fullWidth selectOption"
                  floatingLabelText="Unstructured land"
                  errorText={fieldErrors.floor ? ( fieldErrors.floor.unstructuredLand?<span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.unstructuredLand}</span>:"") : ""}
                  value={floorDetails.floor ? floorDetails.floor.unstructuredLand : ""}
                  onChange={(event, index, value) => {
                      var e = {
                        target: {
                          value: value
                        }
                      };
                      handleChangeNextOne(e, "floor" ,"unstructuredLand", true, "")}
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
                  errorText={fieldErrors.floor ? (fieldErrors.floor.length ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.length}</span>:"") : ""}
                  value={floorDetails.floor ? floorDetails.floor.length : ""}
                  onChange={(e) => {handleChangeNextOne(e,"floor" ,"length", false, "")}}
                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                  underlineStyle={styles.underlineStyle}
                  underlineFocusStyle={styles.underlineFocusStyle}
                  maxLength={6}
                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                />
            </Col>
            <Col xs={12} md={3} sm={6}>
                <TextField  className="fullWidth"
                  floatingLabelText="Breadth"
                  errorText={fieldErrors.floor ?(fieldErrors.floor.width ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.width}</span> :""): ""}
                  value={floorDetails.floor ? floorDetails.floor.width : ""}
                  onChange={(e) => {handleChangeNextOne(e,"floor" ,"width", false, "")}}
                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                  underlineStyle={styles.underlineStyle}
                  underlineFocusStyle={styles.underlineFocusStyle}
                  maxLength={6}
                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                />
            </Col>
            <Col xs={12} md={3} sm={6}>
                <TextField  className="fullWidth"
                  floatingLabelText="Plinth Area"
                  errorText={fieldErrors.floor ?(fieldErrors.floor.plinthArea? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.plinthArea}</span>:"" ): ""}
                  value={floorDetails.floor ? floorDetails.floor.plinthArea : ""}
                  onChange={(e) => {handleChangeNextOne(e, "floor","plinthArea", true, "")}}
                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                  underlineStyle={styles.underlineStyle}
                  underlineFocusStyle={styles.underlineFocusStyle}
                  maxLength={6}
                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                />
            </Col>
            <div className="clearfix"></div>
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
                  floatingLabelText="Building Permission Date"
                  errorText={fieldErrors.floor ?(fieldErrors.floor.bpaDate? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.bpaDate}</span>:"") : ""}
                    defaultDate={ floorDetails.floor ? (floorDetails.floor.bpaDate ? new Date(floorDetails.floor.bpaDate) : new Date() ):  new Date()}
                  onChange={(event,date) => {
                      var e = {
                        target:{
                            value: date
                        }
                      }
                      handleChangeNextOne(e,"floor" ,"bpaDate", false, "")}}
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
                  errorText={fieldErrors.floor ? (fieldErrors.floor.plinthAreaBuildingPlan? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.plinthAreaBuildingPlan}</span>:"") : ""}
                  value={floorDetails.floor ? floorDetails.floor.plinthAreaBuildingPlan : ""}
                  onChange={(e) => {handleChangeNextOne(e, "floor","plinthAreaBuildingPlan", false,  /^\d{6}$/g)}}
                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                  underlineStyle={styles.underlineStyle}
                  underlineFocusStyle={styles.underlineFocusStyle}
                  maxLength={6}
                  floatingLabelStyle={{color:"rgba(0,0,0,0.5)"}}
                />
            </Col>
        </div>
    )}


    return ( 
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
                                                    floatingLabelText="Unit Type"
                                                    errorText={fieldErrors.floor ? (fieldErrors.floor.unitType ? <span style={{position:"absolute", bottom:-13}}>{fieldErrors.floor.unitType}</span>:"" ): ""}
                                                    value={floorDetails.floor ? floorDetails.floor.unitType : ""}
                                                    onChange={(event, index, value) => {
                                                        var e = {
                                                          target: {
                                                            value: value
                                                          }
                                                        };
                                                        handleChangeNextOne(e,"floor" ,"unitType", true, "");
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
                                                  <MenuItem value={1} primaryText="Flat" />
                                                  <MenuItem value={2} primaryText="Room" />
                                                  </SelectField>
                                              </Col>
                                              {(floorDetails.floor ? (floorDetails.floor.unitType == 1 ? true : false) : false) &&
                                                <Col xs={12} md={3} sm={6}>
                                                  <RaisedButton type="button" label="Add Room" style={{marginTop:21}}  backgroundColor="#0b272e" labelColor={white} onClick={
                                                    () => {
                                                      this.setState({addRoom:true});

                                                    }
                                                  }/>
                                              </Col>}
                                              <div className="clearfix"></div>

                                              {this.state.addRoom && roomForm()}
                                              {!this.state.addRoom && flatForm()}
                                                <br/>
                                                <Col xs={12} style={{textAlign:"right"}}>
                                                    <br/>
                                                    { (editIndex == -1 || editIndex == undefined) &&
                                                      <RaisedButton type="button" label="Add Floor"  backgroundColor="#0b272e" labelColor={white} onClick={()=> {
                                                          //  this.props.addNestedFormData("floor","units");
                                                            this.props.addNestedFormData("floors","floor");
                                                            this.props.resetObject("floor"); 
                                                            this.setState({addRoom:false});
															this.setRoomsInArray();
                                                          }
                                                      }/>
                                                    }
                                                      { (editIndex > -1) &&
                                                        <RaisedButton type="button" label="Save Floor"  backgroundColor="#0b272e" labelColor={white} onClick={()=> {
                                                              this.props.updateObject("floors","floor",  editIndex);
                                                              this.props.resetObject("floor");
                                                              isEditIndex(-1);
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
                                              <th>Unit Type</th>
                                              <th>Floor No.</th>
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
                                              <th></th>
                                            </tr>
                                          </thead>
                                          <tbody>
                                            {cThis.state.rooms && cThis.state.rooms.map(function(i, index){
                                              if(i){
                                                return (<tr key={index}>
                                                    <td>{index}</td>
                                                    <td>{i.unitType}</td>
                                                    <td>{i.floorNo}</td>
                                                    <td>{i.unitNo}</td>
                                                    <td>{i.constructionType}</td>
                                                    <td>{i.usageType}</td>
                                                    <td>{i.usageSubType}</td>
                                                    <td>{i.firmName}</td>
                                                    <td>{i.occupancy}</td>
                                                    <td>{i.occupantName}</td>
                                                    <td>{i.annualRent}</td>
                                                    <td>{i.manualArv}</td>
                                                    <td>{i.constructionDate}</td>
                                                    <td>{i.effectiveDate}</td>
                                                    <td>{i.unstructuredLand}</td>
                                                    <td>{i.length}</td>
                                                    <td>{i.breadth}</td>
                                                    <td>{i.plinthArea}</td>
                                                    <td>{i.occupancyCertiNumber}</td>
                                                    <td>{i.buildingPermissionNo}</td>
                                                    <td>{i.buildingPermissionDate}</td>
                                                    <td>{i.plinthAreaBuildingPlan}</td>
                                                    <td>
														<i className="material-icons" style={styles.iconFont} onClick={ () => {
															handleEdit(i, index);
															isEditIndex(index);
                                                         }}>mode_edit</i>
                                                         <i className="material-icons" style={styles.iconFont} onClick={ () => {
															handleDelete(i.uniquePosition, i.uniqueSubPosition, index);
															isEditIndex(-1);
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

  isAddRoom: (room) => {
    dispatch({
      type: "ADD_ROOM",
      room
    })
  },

});

export default connect(mapStateToProps, mapDispatchToProps)(FloorDetails);


