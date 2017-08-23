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
import {translate} from '../../../common/common';
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
	  margin:'15px 0'
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
			creationReason:[{code:'NEWPROPERTY', name:'New Property'}, {code:'SUBDIVISION', name:'Bifurcation'}],
			demands: [],
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
			  
			  
			  	var tQuery = {
					businessService :'PT',
					consumerCode: res.properties[0].upicNumber || res.properties[0].propertyDetail.applicationNo
				}		
		
		
				Api.commonApiPost('billing-service/demand/_search', tQuery, {}).then((res)=>{
				  console.log('demands',res);
				  currentThis.setState({demands : res.Demands})
				}).catch((err)=> {
					currentThis.setState({demands : []})
				  console.log(err)
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
	 
	 var totalAmount=0;
	 var taxCollected = 0;
	 
	 let currentThis = this;
	 	  
	 return(
		<div className="viewProperty">
		{resultList.length != 0 && resultList.map((item, index)=>{
			
		return (
                    <Grid fluid key={index}>
                          <br/>
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>{translate('pt.create.groups.ownerDetails')}</div>} />
								  {item.owners.length !=0 && item.owners.map((owner, index)=> {
									  return(
												<CardText key={index}>
													<Col md={12} xs={12}>
														
															<Row>
															  <Col xs={4} md={3} style={styles.bold}>
																  <div style={{fontWeight:500}}>{translate('pt.create.groups.ownerDetails.fields.aadhaarNumber')}</div>
																  {owner.aadhaarNumber ? owner.aadhaarNumber : translate('pt.search.searchProperty.fields.na')}
															  </Col>	
															  <Col xs={4} md={3} style={styles.bold}>
																   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.mobileNumber')}</div>
																   {owner.mobileNumber ? owner.mobileNumber : translate('pt.search.searchProperty.fields.na')}
															  </Col>
															  <Col xs={4} md={3} style={styles.bold}>
																  <div style={{fontWeight:500}}>{translate('pt.create.groups.ownerDetails.fields.ownerName')}</div>
																   {owner.name ? owner.name : translate('pt.search.searchProperty.fields.na')}
															  </Col>
															  <Col xs={4} md={3} style={styles.bold}>
																  <div style={{fontWeight:500}}>{translate('pt.create.groups.ownerDetails.fields.gender')}</div>
																  {owner.gender ? getNameByCode(currentThis.state.gender, owner.gender) : translate('pt.search.searchProperty.fields.na')}
															  </Col>
															</Row>
															<Row>
															  <Col xs={4} md={3} style={styles.bold}>
																   <div style={{fontWeight:500}}>{translate('pt.create.groups.ownerDetails.fields.email')}</div>
																   {owner.emailId ? owner.emailId : translate('pt.search.searchProperty.fields.na')}
															  </Col>	
															  <Col xs={4} md={3} style={styles.bold}>
																   <div style={{fontWeight:500}}>{translate('pt.create.groups.ownerDetails.fields.guardianRelation')}</div>
																   {owner.gaurdianRelation ? getNameByCode(currentThis.state.gaurdianRelation, owner.gaurdianRelation) : translate('pt.search.searchProperty.fields.na')}
															  </Col>				
															  <Col xs={4} md={3} style={styles.bold}>
																   <div style={{fontWeight:500}}>{translate('pt.create.groups.ownerDetails.fields.guardian')}</div>
																   {owner.fatherOrHusbandName ? owner.fatherOrHusbandName : translate('pt.search.searchProperty.fields.na')}
															  </Col>
															  <Col xs={4} md={3} style={styles.bold}>
																   <div style={{fontWeight:500}}>{translate('pt.create.groups.ownerDetails.fields.primaryOwner')}</div>
																   {owner.isPrimaryOwner ? 'True' : 'False'}
															  </Col>
															</Row>
															<Row>
															  <Col xs={4} md={3} style={styles.bold}>
																   <div style={{fontWeight:500}}>{translate('pt.create.groups.ownerDetails.fields.percentageOfOwnerShip')}</div>
																   {owner.ownerShipPercentage ? owner.ownerShipPercentage : translate('pt.search.searchProperty.fields.na')}
															  </Col>
															</Row>
														
													</Col>
													<div className="clearfix"></div>
											  </CardText>
									  )
								  })}
                              
                          </Card>
                          <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>{translate('pt.create.groups.propertyDetails')}</div>} />
                              <CardText>
									<Col md={12} xs={12}>
											<Row>
											  <Col xs={12} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.oldPropertyNo')}</div>
												   {item.oldUpicNumber || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											
											
											  <Col xs={12} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyNo')}</div>
												   {item.upicNumber || translate('pt.search.searchProperty.fields.na')}
											  </Col>
										
											
											  <Col xs={12} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.annualRentalValue')}</div>
												   NA
											  </Col>
											
											  <Col xs={12} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.propertyType')}</div>
												   {getNameByCode(this.state.propertytypes ,item.propertyDetail.propertyType) || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											</Row>
										 
										
											<Row>
											  <Col xs={4} md={3} style={styles.bold}>
												 <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.extentOfSite')}</div>
												  {item.propertyDetail.sitalArea || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.registrationDocNo')}</div>
												  {item.propertyDetail.regdDocNo || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.creationReason')}</div>
												  {getNameByCode(this.state.creationReason, item.creationReason) || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.AssessmentNumberOfParentProperty')}</div>
												   NA
											  </Col>
											</Row> 
											<Row>											
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.exemptionCategory')}</div>
												   {item.propertyDetail.exemptionReason || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												    <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.effectiveDate')}</div>
												   {item.occupancyDate ? item.occupancyDate.split(' ')[0] : translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.appartment')}</div>
												  {item.propertyDetail.apartment || translate('pt.search.searchProperty.fields.na')}
											  </Col>
										
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.propertyDepartment')}</div>
												  {item.propertyDetail.department || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											</Row>
										 
										 
											<Row>
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.registrationDocDate')}</div>
												  {item.propertyDetail.regdDocDate ? item.propertyDetail.regdDocDate.split(' ')[0] : translate('pt.search.searchProperty.fields.na')}

											  </Col>
											
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.assessmentDate')}</div>
												  {item.assessmentDate ? item.assessmentDate.split(' ')[0] : translate('pt.search.searchProperty.fields.na')}
											  </Col>
											</Row>
										
									</Col>
									<div className="clearfix"></div>
                              </CardText>
                          </Card>
				
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>{translate('pt.create.groups.propertyAddress.fields.addressDetails')}</div>} />
                              <CardText>
								
									<Col md={12} xs={12}>
										
											<Row>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.doorNo')}</div>
												   {item.address.addressNumber}
											  </Col>
										
											
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.propertyAddress')}</div>
												    {item.address.addressNumber ? item.address.addressNumber+', ' : '' }
													{item.address.addressLine1 ? getNameById(this.state.locality,item.address.addressLine1)+', ' : '' }
													{item.address.addressLine2 ? item.address.addressLine2+', ':''}
													{item.address.landmark ? item.address.landmark+', ' : ''}
													{item.address.city ? item.address.city : ''}
											  </Col>
											
											
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.zoneNo')}</div>
												   {getNameById(this.state.zone, item.boundary.revenueBoundary.id) || translate('pt.search.searchProperty.fields.na')}
											  </Col>
										
											
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.blockNo')}</div>
												   NA
											  </Col>
											 
											</Row>
											<Row>
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.electionWard')}</div>
												  {getNameById(this.state.election,item.boundary.adminBoundary.id) || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('employee.Employee.fields.correspondenceAddress')}</div>
												   NA
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.wardNo')}</div>
												   NA
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.locality')}</div>
												    {getNameById(this.state.locality,item.address.addressLine1) || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											</Row>
									
											<Row>
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.ebBlock')}</div>
												  NA
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												  <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.pin')}</div>
													  {item.address.pincode}
											  </Col>
											</Row>
									</Col>
									<div className="clearfix"></div>
                              </CardText>
                          </Card>
				
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>{translate('pt.create.groups.assessmentDetails')}</div>} />
                              <CardText>
									<Col md={12} xs={12}>
											<Row>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.creationReason')}</div>
												   {getNameByCode(this.state.creationReason, item.creationReason)  || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.propertyType')}</div>
												   {getNameByCode(this.state.propertytypes ,item.propertyDetail.propertyType) || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.propertySubType')}</div>
												   {getNameByCode(this.state.propertytypes ,item.propertyDetail.category) || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.usageType')}</div>
												   {translate('pt.search.searchProperty.fields.na')}
											  </Col>
											   <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.usageSubType')}</div>
												    {translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.extentOfSite')}</div>
													{item.propertyDetail.sitalArea || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.assessmentDetails.fields.sequenceNo')}</div>
												   {item.sequenceNo || translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.floorDetails.fields.buildingPermissionNumber')}</div>
													   {translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.floorDetails.fields.buildingPermissionDate')}</div>
													   {translate('pt.search.searchProperty.fields.na')}
											  </Col>
											</Row>
										 
									</Col>
									<div className="clearfix"></div>
                              </CardText>
                          </Card>
					
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>{translate('pt.create.groups.propertyFactors')}</div>} />
                              <CardText>
									<Col md={12} xs={12}>
										
											<Row>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyFactors.fields.totalFactor')}</div>
													{item.propertyDetail.hasOwnProperty('factors') ? (item.propertyDetail.factors !=null && item.propertyDetail.factors.length !=0 ? (item.propertyDetail.factors[0].value ||  translate('pt.search.searchProperty.fields.na') ) : translate('pt.search.searchProperty.fields.na') ) : translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyFactors.fields.roadFactor')}</div>
													{item.propertyDetail.hasOwnProperty('factors') ? (item.propertyDetail.factors !=null && item.propertyDetail.factors.length !=0 ? (item.propertyDetail.factors[1].value ||  translate('pt.search.searchProperty.fields.na')) : translate('pt.search.searchProperty.fields.na') ) : translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyFactors.fields.liftFactor')}</div>
													{item.propertyDetail.hasOwnProperty('factors') ? (item.propertyDetail.factors !=null && item.propertyDetail.factors.length !=0 ? (item.propertyDetail.factors[2].value ||  translate('pt.search.searchProperty.fields.na')) : translate('pt.search.searchProperty.fields.na') ) : translate('pt.search.searchProperty.fields.na')}
											  </Col>
											  <Col xs={4} md={3} style={styles.bold}>
												   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyFactors.fields.parkingFactor')}</div>
													{item.propertyDetail.hasOwnProperty('factors') ? (item.propertyDetail.factors !=null && item.propertyDetail.factors.length !=0 ? (item.propertyDetail.factors[3].value ||  translate('pt.search.searchProperty.fields.na')): translate('pt.search.searchProperty.fields.na') ) : translate('pt.search.searchProperty.fields.na')}
											  </Col>
											</Row>
										 
									</Col>
									<div className="clearfix"></div>
                              </CardText>
                          </Card>
				
						  <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>{translate('pt.create.groups.floorDetails')}</div>} />
								<CardText>
									<Col xs={12} md={12}>
									 <Table id="floorDetailsTable" style={{color:"black",fontWeight: "normal", marginBottom:0}} bordered responsive>
                                          <thead style={{backgroundColor:"#607b84",color:"white"}}>
                                            <tr>
                                              <th>#</th>
											  <th>{translate('pt.create.groups.floorDetails.fields.floorNumber')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.unitType')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.unitNumber')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.constructionClass')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.usageType')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.usageSubType')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.firmName')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.occupancy')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.occupantName')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.annualRent')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.manualArv')}</th>
											  <th>{translate('pt.create.groups.floorDetails.fields.constructionStartDate')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.constructionEndDate')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.effectiveFromDate')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.unstructuredLand')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.length')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.breadth')}</th>
                                              <th>{translate('pt.create.groups.floorDetails.fields.plinthArea')}</th>
											  <th>{translate('pt.create.groups.propertyAddress.fields.carpetArea')}</th>
											  <th>{translate('pt.create.groups.propertyAddress.fields.exemptedArea')}</th>
											  <th>{translate('pt.create.groups.floorDetails.fields.occupancyCertificateNumber')}</th>
											  <th>{translate('pt.create.groups.propertyAddress.fields.buildingCost')}</th>
											  <th>{translate('pt.create.groups.propertyAddress.fields.landCost')}</th>
                                              <th>{translate('pt.create.groups.assessmentDetails.fields.isLegal')}</th>
                                            </tr>
                                          </thead>
                                          <tbody>
                                            {item.propertyDetail.floors.length !=0  && item.propertyDetail.floors.map(function(i, index){
                                              if(i){
                                                return (<tr key={index}>
                                                    <td>{index+1}</td>
                                                    <td>{getNameByCode(currentThis.state.floorNumber, (parseInt(i.floorNo)+1)) || translate('pt.search.searchProperty.fields.na')}</td>
													<td>{getNameByCode(currentThis.state.unitType, i.unitType) || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{i.unitNo || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{getNameByCode(currentThis.state.structureclasses, i.structure) || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{getNameByCode(currentThis.state.usages ,i.usage) || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{getNameByCode(currentThis.state.usages, i.usageSubType) || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{i.firmName || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{getNameByCode(currentThis.state.occupancies,i.occupancyType) || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{i.occupierName || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{i.annualRent || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{parseFloat(i.manualArv) || translate('pt.search.searchProperty.fields.na')}</td>
													<td>{i.constStartDate ?  i.constStartDate.split(' ')[0] : translate('pt.search.searchProperty.fields.na') }</td>
                                                    <td>{i.constCompletionDate ? i.constCompletionDate.split(' ')[0] : translate('pt.search.searchProperty.fields.na') }</td>
                                                    <td>{i.occupancyDate ? i.occupancyDate.split(' ')[0]  : translate('pt.search.searchProperty.fields.na') }</td>
                                                    <td>{(i.isStructured == true ? 'Yes' : i.isStructured)|| translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{parseFloat(i.length) || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{parseFloat(i.width) || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{i.builtupArea || translate('pt.search.searchProperty.fields.na')}</td>
													<td>{i.carpetArea || translate('pt.search.searchProperty.fields.na')}</td>
													<td>{i.exemptedArea || translate('pt.search.searchProperty.fields.na')}</td>
													<td>{i.occupancyCertiNumber || translate('pt.search.searchProperty.fields.na')}</td>
													<td>{i.buildingCost || translate('pt.search.searchProperty.fields.na')}</td>
													<td>{i.landCost || translate('pt.search.searchProperty.fields.na')}</td>
                                                    <td>{i.bpaNo || translate('pt.search.searchProperty.fields.na')}</td>  
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
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>{translate('pt.create.groups.constructionDetails')}</div>} />
                              <CardText>
									<Col md={12} xs={12}>
										<Row>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.currentAssessmentDate')}</div>
												{translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.firstAssessmentDate')}</div>
												{translate('pt.search.searchProperty.fields.na')}										  
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.revisedAssessmentDate')}</div>
											   {translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.propertyAddress.fields.lastAssessmentDate')}</div>
											   {translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.constructionDetails.fields.orderDate')}</div>
											   {translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.constructionDetails.fields.certificateNumber')}</div>
											   {item.certificateNumber || translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.constructionDetails.fields.certificateCompletionDate')}</div>
											   {item.certificateCompletionDate || translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.constructionDetails.fields.certificateReceivedDate')}</div>
											   {item.certificateReceivedDate || translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.constructionDetails.fields.agencyName')}</div>
											   {item.agencyName || translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.constructionDetails.fields.licenseType')}</div>
											   {item.licenseType || translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  <Col xs={4} md={3} style={styles.bold}>
											   <div style={{fontWeight:500}}>{translate('pt.create.groups.constructionDetails.fields.licenseNumber')}</div>
											   {item.licenseNumber || translate('pt.search.searchProperty.fields.na')}
										  </Col>
										  
										</Row> 
									</Col>
									<div className="clearfix"></div>
                              </CardText>
                          </Card>
						    <Card className="uiCard">
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>{translate('pt.create.groups.propertyAddress.taxDetails')}</div>} />
                              <CardText>
									<Col  xs={4} md={12}>
										<Table id="TaxCalculationTable" style={{color:"black",fontWeight: "normal", marginBottom:0}} bordered responsive>
											<thead>
												<tr>
												  <th>#</th>
												  <th>{translate('pt.create.groups.propertyAddress.propertyTax')}</th>
												  <th>{translate('pt.create.groups.propertyAddress.educationCess')}</th>
												  <th>{translate('pt.create.groups.propertyAddress.libraryCess')}</th>
												  <th>{translate('pt.create.groups.propertyAddress.totalTax')}</th>
												  <th>{translate('pt.create.groups.propertyAddress.totalTaxDue')}</th>
												</tr>
											</thead>
											<tbody>
											{currentThis.state.demands.length !=0 && currentThis.state.demands.map((item, index)=>{
												
												return(
													<tr key={index}>
														<td>{index +1}</td>
														<td>
														
														{(item.hasOwnProperty('demandDetails') && item.demandDetails.length !=0 ) && item.demandDetails.map((i,index)=>{
															if(i.taxHeadMasterCode == "PT_TAX") {
																return(
																<span key={index}>{i.taxAmount}</span>
																)
															}
														})}
														</td>
														<td>
														{(item.hasOwnProperty('demandDetails') && item.demandDetails.length !=0 ) && item.demandDetails.map((i,index)=>{
															if(i.taxHeadMasterCode == "EDU_CESS") {
																return(
																<span key={index}>{i.taxAmount}</span>
																)
															}
														})}
														</td>
														<td>
														{(item.hasOwnProperty('demandDetails') && item.demandDetails.length !=0 ) && item.demandDetails.map((i,index)=>{
															if(i.taxHeadMasterCode == "LIB_CESS") {
																return(
																<span key={index}>{i.taxAmount}</span>
																)
															}
														})}
														</td>
													
														<td>
														{(item.hasOwnProperty('demandDetails') && item.demandDetails.length !=0 ) && item.demandDetails.map((i,index)=>{
															
															totalAmount += parseFloat(i.taxAmount);
															taxCollected += parseFloat(i.collectionAmount);
														})}
														{totalAmount}
														</td>
														<td>
														{totalAmount - taxCollected}
														</td>
													</tr>
												)
											})}
											</tbody>
										</Table>
										<div className="clearfix"></div>
									</Col>
									<div className="clearfix"></div>
							  </CardText>
							</Card>
							<div style={{textAlign:'center', paddingTop:10}}> 
								
									<RaisedButton type="button" primary={true} label={translate('pt.search.searchProperty')} style={{margin:'0 5px'}}  onClick={()=>{
										this.props.history.push('/propertyTax/search/')
									}}/>
								
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
