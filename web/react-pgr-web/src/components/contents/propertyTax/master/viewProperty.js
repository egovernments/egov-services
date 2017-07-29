import React, {Component} from 'react';
import {connect} from 'react-redux';

import {Grid, Row, Col, Table, DropdownButton, ListGroup, ListGroupItem} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import {List, ListItem} from 'material-ui/List';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import Api from '../../../../api/api';

const $ = require('jquery');

var flag = 0;
const styles = {
  errorStyle: {
    color: red500
  },
  underlineStyle: {
    borderColor: brown500
  },
  underlineFocusStyle: {
    borderColor: brown500
  },
  floatingLabelStyle: {
    color: brown500
  },
  floatingLabelFocusStyle: {
    color: brown500
  },
  customWidth: {
    width:100
  },
  bold: {
	  fontWeight:500
  }
};


const getNameById = function(object, id, property = "") {
  if (id == "" || id == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property == "") {
            if (object[i].id == id) {
                return object[i].name;
            }
        } else {
            if (object[i].hasOwnProperty(property)) {
                if (object[i].id == id) {
                    return object[i][property];
                }
            } else {
                return "";
            }
        }
    }
    return "";
}

const getNameByCode = function(object, code, property = "") {
	
	console.log(object, code);
	
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

class ViewProperty extends Component {
  constructor(props) {
       super(props);
       this.state = {
			resultList:[],
			unitType:[{code:"FLAT", name:'Flat'},{code:"ROOM", name:'Room'}],
			floorNumber:[{code:1, name:'Basement-3'},{code:2, name:'Basement-2'},{code:3, name:'Basement-1'},{code:4, name:'Ground Floor'}],
			gaurdianRelation: [{code:'FATHER', name:'Father'}, {code:'HUSBAND', name:'Husband'}, {code:'MOTHER', name:'Mother'}, {code:'OTHERS', name:'Others'} ],
		    gender:[{code:'MALE', name:'Male'}, {code:'FEMALE', name:'Female'}, {code:'OTHERS', name:'Others'}],
			ownerType:[{code:'Ex_Service_man', name:'Ex-Service man'}, {code:'Freedom_Fighter', name:'Freedom Fighter'}, {code:'Freedom_fighers_wife', name:"Freedom figher's wife"}],
			propertytypes: [],
			apartments:[],
			departments:[],
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
			creationReason:[{code:'NEWPROPERTY', name:'New Property'}, {code:'SUBDIVISION', name:'Bifurcation'}]
       }
      
   }

  componentWillMount()
  {

 
  }

  componentDidMount() {
	  
	var currentThis = this;
		
	 let {showTable,changeButtonText, propertyTaxSearch, setLoadingStatus, toggleSnackbarAndSetText }=this.props;
      	  
	  setLoadingStatus('loading');
	  
	   var query;
	  
	  if(this.props.match.params.type){
		  query = {
			  propertyId: this.props.match.params.searchParam
		  };
	  } else {
		   query = {
			  upicNumber: this.props.match.params.searchParam
		  };
	  }

	  
      Api.commonApiPost('pt-property/properties/_search', query,{}, false, true).then((res)=>{   
		setLoadingStatus('hide');
		if(res.hasOwnProperty('Errors')){
			toggleSnackbarAndSetText(true, "Server returned unexpected error. Please contact system administrator.")
		} else {
			  var units = [];
  
			  var floors = res.properties[0].propertyDetail.floors;
			  
			  for(var i = 0; i<floors.length; i++){
				  for(var j = 0; j<floors[i].units.length;j++){
					  floors[i].units[j].floorNo = floors[i].floorNo;
					  units.push(floors[i].units[j])
				  }
			  }
			  
			  res.properties[0].propertyDetail.floors = units;
			  
			  this.setState({
				  resultList: res.properties,
			  })
		}
	
      }).catch((err)=> {
			setLoadingStatus('hide');
			toggleSnackbarAndSetText(true, err.message)
      })	
		
		
	Api.commonApiPost('pt-property/property/propertytypes/_search',{}, {},false, true).then((res)=>{
        currentThis.setState({propertytypes:res.propertyTypes})
    }).catch((err)=> {
        currentThis.setState({
          propertytypes:[]
        })
		toggleSnackbarAndSetText(true, err.message);
    }) 

	Api.commonApiPost('pt-property/property/departments/_search',{}, {},false, true).then((res)=>{
	  currentThis.setState({
		departments:res.departments
	  })
	}).catch((err)=> {
	  console.log(err)
		toggleSnackbarAndSetText(true, err.message);
	})
	
	Api.commonApiPost('pt-property/property/floortypes/_search',{}, {},false, true).then((res)=>{
      console.log(res);
	  res.floorTypes.unshift({code:-1, name:'None'})
      currentThis.setState({floortypes:res.floorTypes})
    }).catch((err)=> {
      currentThis.setState({
        floortypes:[]
      })
      console.log(err)
    })

    Api.commonApiPost('pt-property/property/rooftypes/_search',{}, {},false, true).then((res)=>{
      console.log(res);
      currentThis.setState({rooftypes: res.roofTypes})
    }).catch((err)=> {
      currentThis.setState({
        rooftypes: []
      })
      console.log(err)
    })

    Api.commonApiPost('pt-property/property/walltypes/_search',{}, {},false, true).then((res)=>{
      console.log(res);
      currentThis.setState({walltypes: res.wallTypes})
    }).catch((err)=> {
      currentThis.setState({
        walltypes:[]
      })
      console.log(err)
    })

    Api.commonApiPost('pt-property/property/woodtypes/_search',{}, {},false, true).then((res)=>{
      console.log(res);
      currentThis.setState({woodtypes: res.woodTypes})
    }).catch((err)=> {
      currentThis.setState({
        woodtypes:[]
      })
    })
	
	
	Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"LOCALITY", hierarchyTypeName:"LOCATION"}).then((res)=>{
          console.log(res);
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
          currentThis.setState({zone : res.Boundary})
        }).catch((err)=> {
           currentThis.setState({
            zone : []
          })
          console.log(err)
        })

          Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"WARD", hierarchyTypeName:"REVENUE"}).then((res)=>{
          console.log(res);
          currentThis.setState({ward : res.Boundary})
        }).catch((err)=> {
          currentThis.setState({
            ward : []
          })
          console.log(err)
        })

         Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"BLOCK", hierarchyTypeName:"REVENUE"}).then((res)=>{
          console.log(res);
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
          currentThis.setState({revanue : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })

        Api.commonApiPost('egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName', {boundaryTypeName:"WARD", hierarchyTypeName:"ADMINISTRATION"}).then((res)=>{
          console.log(res);
          currentThis.setState({election : res.Boundary})
        }).catch((err)=> {
          console.log(err)
        })



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
				code:i,
				name:(i-4)+label+" Floor"
			}
			temp.push(commonFloors);
			
		}
		
		  
		  this.setState({
			  floorNumber: temp
		  })

		
   
  
}

  componentWillUnmount(){
  
  }


  componentWillUpdate() {

  }

  componentDidUpdate(prevProps, prevState) {
    
  }

  render() {
	  
	 let { resultList } = this.state;
	 
	 let currentThis = this;
	 	  
	 return(
		<div className="viewProperty">
		{resultList.length != 0 && resultList.map((item, index)=>{
			
		return (
                    <Grid key={index}>
                          <br/>
                          <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Property Details</div>} />
                              <CardText>
								
									<Col md={6} xs={12}>
										<ListGroup>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Old Assessment Number
											  </Col>
											  <Col xs={8} md={6}>
												  {item.oldUpicNumber || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Assessment Number
											  </Col>
											  <Col xs={8} md={6}>
												  {item.upicNumber || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Annual Rental Value
											  </Col>
											  <Col xs={8} md={6}>
												  
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Property Type
											  </Col>
											  <Col xs={8} md={6}>
												 {getNameByCode(this.state.propertytypes ,item.propertyDetail.propertyType) || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Extent of Site (Sq.Mtrs)
											  </Col>
											  <Col xs={8} md={6}>
												  {item.propertyDetail.sitalArea || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Registration Doc No
											  </Col>
											  <Col xs={8} md={6}>
												  {item.propertyDetail.regdDocNo || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Reason for Creation
											  </Col>
											  <Col xs={8} md={6}>
												   {getNameByCode(this.state.creationReason, item.creationReason) || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
									  </ListGroup>
									</Col>
									<Col md={6} xs={12}>
										<ListGroup>
								
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Assessment number of parent property
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Exemption Category
											  </Col>
											  <Col xs={8} md={6}>
												{item.propertyDetail.exemptionReason || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Effective Date
											  </Col>
											  <Col xs={8} md={6}>
												  {item.occupancyDate ? new Date(item.occupancyDate).getDate()+'/'+(new Date(item.occupancyDate).getMonth()+1)+'/'+new Date(item.occupancyDate).getFullYear() : 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Apartment/Complex Name
											  </Col>
											  <Col xs={8} md={6}>
												  {item.propertyDetail.apartment || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Property Department
											  </Col>
											  <Col xs={8} md={6}>
												  {item.propertyDetail.department || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Registration Doc Date
											  </Col>
											  <Col xs={8} md={6}>
												{item.propertyDetail.regdDocDate ? new Date(item.propertyDetail.regdDocDate).getDate()+'/'+(new Date(item.propertyDetail.regdDocDate).getMonth()+1)+'/'+new Date(item.propertyDetail.regdDocDate).getFullYear() : 'NA'}
 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Assessment Date
											  </Col>
											  <Col xs={8} md={6}>
												  {item.assessmentDate ? new Date(item.propertyDetail.regdDocDate).getDate()+'/'+new Date(item.propertyDetail.regdDocDate).getMonth()+'/'+new Date(item.propertyDetail.regdDocDate).getFullYear() : 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
									  </ListGroup>
									</Col>
									<div className="clearfix"></div>
                              </CardText>
                          </Card>
				
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Address Details</div>} />
                              <CardText>
								
									<Col md={6} xs={12}>
										<ListGroup>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Door No
											  </Col>
											  <Col xs={8} md={6}>
												  {item.address.addressNumber}
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Property Address
											  </Col>
											  <Col xs={8} md={6}>
												 {item.address.addressNumber ? item.address.addressNumber+', ' : 'NA' }
												 {item.address.addressLine1 ? item.address.addressLine1+', ' : 'NA' }
												 {item.address.addressLine2 ? item.address.addressLine2+', ':'NA'}
												 {item.address.landmark ? item.address.landmark+', ' : 'NA'}
												 {item.address.city ? item.address.city+', ' : 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Zone
											  </Col>
											  <Col xs={8} md={6}>
												 {item.boundary.revenueBoundary.name || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Block
											  </Col>
											  <Col xs={8} md={6}>
													
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Election Ward
											  </Col>
											  <Col xs={8} md={6}>
												  {item.boundary.adminBoundary.name || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>										  
									  </ListGroup>
									</Col>
									<Col md={6} xs={12}>
										<ListGroup>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Correspondence Address
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Ward
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Locality
											  </Col>
											  <Col xs={8} md={6}>
												  {item.address.addressLine1 || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  EB Block
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
									  </ListGroup>
									</Col>
									<div className="clearfix"></div>
                              </CardText>
                          </Card>
				
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Owner Details</div>} />
								  {item.owners.length !=0 && item.owners.map((owner, index)=> {
									  return(
												<CardText key={index}>
													<Col md={6} xs={12}>
														<ListGroup>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Aadhaar No
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.aadhaarNumber ? owner.aadhaarNumber : 'NA'}
															  </Col>
															</Row>
														  </ListGroupItem>
														<ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Mobile Number (without +91)
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.mobileNumber ? owner.mobileNumber : 'NA'}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Owner Name
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.name ? owner.name : 'NA'}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																  Gender
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.gender ? getNameByCode(currentThis.state.gender, owner.gender) : 'NA'}
															  </Col>
															</Row>
														  </ListGroupItem>								  
													  </ListGroup>
													</Col>
													<Col md={6} xs={12}>
														<ListGroup>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Email Address
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.emailId ? owner.emailId : 'NA'}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Guardian Relation
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.gaurdianRelation ? getNameByCode(currentThis.state.gaurdianRelation, owner.gaurdianRelation) : 'NA'}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Guardian
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.fatherOrHusbandName ? owner.fatherOrHusbandName : 'NA'}
															  </Col>
															</Row>
														  </ListGroupItem>  
													  </ListGroup>
													</Col>
													<div className="clearfix"></div>
											  </CardText>
									  )
								  })}
                              
                          </Card>
					
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Amenities</div>} />
                              <CardText>
									<Col md={6} xs={12}>
										<ListGroup>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Lift
											  </Col>
											  <Col xs={8} md={6}>
												  {item.lift || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Toilets
											  </Col>
											  <Col xs={8} md={6}>
												  {item.toilet || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Water Tap
											  </Col>
											  <Col xs={8} md={6}>
												  {item.waterTap || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Electricity
											  </Col>
											  <Col xs={8} md={6}>
												  {item.electricity || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>								  
									  </ListGroup>
									</Col>
									<Col md={6} xs={12}>
										<ListGroup>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Attached Bathroom
											  </Col>
											  <Col xs={8} md={6}>
												  {item.attachedBathroom || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Water Harvesting
											  </Col>
											  <Col xs={8} md={6}>
												  {item.waterHarvesting || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Cable Connection
											  </Col>
											  <Col xs={8} md={6}>
												  {item.cableConnection || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>  
									  </ListGroup>
									</Col>
									<div className="clearfix"></div>
                              </CardText>
                          </Card>
				
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Construction Type</div>} />
                              <CardText>
									<Col md={6} xs={12}>
										<ListGroup>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Floor Type
											  </Col>
											  <Col xs={8} md={6}>
												  {getNameByCode(this.state.floortypes ,item.propertyDetail.floorType) || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Wall Type
											  </Col>
											  <Col xs={8} md={6}>
												  {getNameByCode(this.state.walltypes ,item.propertyDetail.wallType) || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>							  
									  </ListGroup>
									</Col>
									<Col md={6} xs={12}>
										<ListGroup>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Roof Type
											  </Col>
											  <Col xs={8} md={6}>
												{getNameByCode(this.state.rooftypes ,item.propertyDetail.roofType) || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Wood Type
											  </Col>
											  <Col xs={8} md={6}>
											  
												{getNameByCode(this.state.floortypes ,item.propertyDetail.woodType) || 'NA'}
											  </Col>
											</Row>
										  </ListGroupItem>
									  </ListGroup>
									</Col>
									<div className="clearfix"></div>
                              </CardText>
                          </Card>
				
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Floor Details</div>} />
								<CardText>
									<Col xs={12} md={12}>
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
                                            </tr>
                                          </thead>
                                          <tbody>
                                            {item.propertyDetail.floors.length !=0  && item.propertyDetail.floors.map(function(i, index){
                                              if(i){
												  console.log(i)
                                                return (<tr key={index}>
                                                    <td>{index}</td>
                                                    <td>{getNameByCode(currentThis.state.floorNumber, (parseInt(i.floorNo)+1)) || 'NA'}</td>
													<td>{getNameByCode(currentThis.state.unitType, i.unitType) || 'NA'}</td>
													<td>{i.flatNo ? i.flatNo : ''}</td>
                                                    <td>{i.unitNo || 'NA'}</td>
                                                    <td>{getNameByCode(currentThis.state.structureclasses, i.structure) || 'NA'}</td>
                                                    <td>{getNameByCode(currentThis.state.usages ,i.usage) || 'NA'}</td>
                                                    <td>{getNameByCode(currentThis.state.usages, i.usageSubType) || 'NA'}</td>
                                                    <td>{i.firmName || 'NA'}</td>
                                                    <td>{getNameByCode(currentThis.state.occupancies,i.occupancyType) || 'NA'}</td>
                                                    <td>{i.occupierName || 'NA'}</td>
                                                    <td>{i.annualRent || 'NA'}</td>
                                                    <td>{parseFloat(i.manualArv) || 'NA'}</td>
                                                    <td>{i.constCompletionDate ? new Date(i.constCompletionDate).getDate()+'/'+(new Date(i.constCompletionDate).getMonth()+1)+'/'+new Date(i.constCompletionDate).getFullYear() : 'NA' }</td>
                                                    <td>{i.occupancyDate ? new Date(i.occupancyDate).getDate()+'/'+(new Date(i.occupancyDate).getMonth()+1)+'/'+new Date(i.occupancyDate).getFullYear() : 'NA' }</td>
                                                    <td>{(i.isStructured == true ? 'Yes' : i.isStructured)|| 'NA'}</td>
                                                    <td>{parseFloat(i.length) || 'NA'}</td>
                                                    <td>{parseFloat(i.width) || 'NA'}</td>
                                                    <td>{i.builtupArea || 'NA'}</td>
                                                    <td>{i.occupancyCertiNumber || 'NA'}</td>
                                                    <td>{i.bpaNo || 'NA'}</td>
                                                    <td>{i.bpaDate ? new Date(i.bpaDate).getDate()+'/'+(new Date(i.bpaDate).getMonth()+1)+'/'+new Date(i.bpaDate).getFullYear() : 'NA' }</td>
                                                    <td>{i.bpaBuiltupArea || 'NA'}</td>
                                                  </tr>)
												  
                                              }

                                            })}
                                          </tbody>
                                          </Table>
										  </Col>
										  <div className="clearfix"></div>
                              </CardText>
                          </Card>
						    <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Tax Details</div>} />
                              <CardText>
									<Col  xs={4} md={12}>
										<Table id="TaxCalculationTable" style={{color:"black",fontWeight: "normal", marginBottom:0}} bordered responsive>
											<thead>
												<tr>
												  <th>#</th>
												  <th>Property Tax</th>
												  <th>Education Cess</th>
												  <th>Library Cess</th>
												  <th>Unauthorized Penalty</th>
												  <th>Total Tax</th>
												  <th>Total Tax Due</th>
												</tr>
											</thead>
											<tbody>
											</tbody>
										</Table>
										<div className="clearfix"></div>
									</Col>
									<div className="clearfix"></div>
							  </CardText>
							</Card>
							<div style={{textAlign:'center', paddingTop:10}}> 
								
									<RaisedButton type="button" primary={true} label="View DCB" style={{margin:'0 5px'}} />
									<RaisedButton type="button" primary={true} label="Search Property" style={{margin:'0 5px'}} />
									<RaisedButton type="button" primary={true} label="Print" style={{margin:'0 5px'}} />																							
								
							</div>
		</Grid>)
					  })}
           
        </div>)
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => ({
	
	setLoadingStatus: (loadingStatus) => {
     dispatch({type: "SET_LOADING_STATUS", loadingStatus});
   },
   toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
     dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
   }
  
 });

export default connect(mapStateToProps, mapDispatchToProps)(ViewProperty);
