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

class ViewProperty extends Component {
  constructor(props) {
       super(props);
       this.state = {
		 resultList:[]
       }
      
   }

  componentWillMount()
  {

 
  }

  componentDidMount() {
  
    let properties = [
    {
      "id": 1,
      "tenantId": "default",
      "upicNumber": "upicnumber",
      "oldUpicNumber": "",
      "vltUpicNumber": "",
      "creationReason": "NEWPROPERTY",
      "address": {
        "id": 1,
        "tenantId": "default",
        "latitude": null,
        "longitude": null,
        "addressId": null,
        "addressNumber": "40/179-7-1",
        "addressLine1": "Muttarasupalli",
        "addressLine2": "Revenue Ward No 40",
        "landmark": null,
        "city": "Kadapa",
        "pincode": "516001",
        "detail": null,
        "auditDetails": {
          "createdBy": "74",
          "lastModifiedBy": "74",
          "createdTime": 1501140313055,
          "lastModifiedTime": 1501140313055
        }
      },
      "owners": [
        {
          "id": 282,
          "userName": "9302930208",
          "password": null,
          "salutation": null,
          "name": "Tester12",
          "gender": "FEMALE",
          "mobileNumber": "9302930208",
          "emailId": null,
          "altContactNumber": null,
          "pan": null,
          "aadhaarNumber": null,
          "permanentAddress": null,
          "permanentCity": null,
          "permanentPincode": null,
          "correspondenceCity": null,
          "correspondencePincode": null,
          "correspondenceAddress": null,
          "active": true,
          "dob": null,
          "pwdExpiryDate": "25-10-2017 12:55:11",
          "locale": "en_IN",
          "type": "CITIZEN",
          "signature": null,
          "accountLocked": false,
          "roles": [
            {
              "id": 1,
              "name": "Citizen",
              "code": "CITIZEN",
              "description": null,
              "auditDetails": null,
              "tenantId": null
            }
          ],
          "fatherOrHusbandName": null,
          "bloodGroup": null,
          "identificationMark": null,
          "photo": null,
          "auditDetails": null,
          "otpReference": null,
          "tenantId": null,
          "isPrimaryOwner": null,
          "isSecondaryOwner": null,
          "ownerShipPercentage": null,
          "ownerType": null
        }
      ],
      "propertyDetail": {
        "id": 1,
        "source": "MUNICIPAL_RECORDS",
        "regdDocNo": null,
        "regdDocDate": null,
        "reason": "CREATE",
        "status": "ACTIVE",
        "isVerified": false,
        "verificationDate": null,
        "isExempted": false,
        "exemptionReason": null,
        "propertyType": "PRIVATE",
        "category": "RESIDENTIAl",
        "usage": null,
        "department": null,
        "apartment": null,
        "siteLength": 0.0,
        "siteBreadth": 0.0,
        "sitalArea": 0.0,
        "totalBuiltupArea": 0.0,
        "undividedShare": 0.0,
        "noOfFloors": 0,
        "isSuperStructure": false,
        "landOwner": null,
        "floorType": null,
        "woodType": null,
        "roofType": null,
        "wallType": null,
        "floors": [
          {
            "id": 1,
            "floorNo": "0",
            "units": [
              {
                "id": 1,
                "unitNo": 0,
                "units": null,
                "unitType": "FLAT",
                "length": 0.0,
                "width": 0.0,
                "builtupArea": 73.0,
                "assessableArea": 73.0,
                "bpaBuiltupArea": 0.0,
                "bpaNo": null,
                "bpaDate": null,
                "usage": "RESD",
                "occupancyType": null,
                "occupierName": null,
                "firmName": null,
                "rentCollected": 0.0,
                "structure": "RCCPOSHBUILDING",
                "age": "0TO25",
                "exemptionReason": null,
                "isStructured": true,
                "occupancyDate": "2017-05-21 00:00:00",
                "constCompletionDate": null,
                "manualArv": 0.0,
                "arv": 0.0,
                "electricMeterNo": null,
                "waterMeterNo": null,
                "parentid": 0,
                "isAuthorised": true,
                "auditDetails": {
                  "createdBy": "74",
                  "lastModifiedBy": "74",
                  "createdTime": 1501140313055,
                  "lastModifiedTime": 1501140313055
                }
              },
              {
                "id": 2,
                "unitNo": 0,
                "units": null,
                "unitType": "FLAT",
                "length": 0.0,
                "width": 0.0,
                "builtupArea": 53.0,
                "assessableArea": 53.0,
                "bpaBuiltupArea": 0.0,
                "bpaNo": null,
                "bpaDate": null,
                "usage": "RESD",
                "occupancyType": null,
                "occupierName": null,
                "firmName": null,
                "rentCollected": 0.0,
                "structure": "RCCPOSHBUILDING",
                "age": "0TO25",
                "exemptionReason": null,
                "isStructured": true,
                "occupancyDate": "2017-05-21 00:00:00",
                "constCompletionDate": null,
                "manualArv": 0.0,
                "arv": 0.0,
                "electricMeterNo": null,
                "waterMeterNo": null,
                "parentid": 0,
                "isAuthorised": true,
                "auditDetails": {
                  "createdBy": "74",
                  "lastModifiedBy": "74",
                  "createdTime": 1501140313055,
                  "lastModifiedTime": 1501140313055
                }
              }
            ],
            "auditDetails": {
              "createdBy": "74",
              "lastModifiedBy": "74",
              "createdTime": 1501140313055,
              "lastModifiedTime": 1501140313055
            }
          },
          {
            "id": 2,
            "floorNo": "0",
            "units": [
              {
                "id": 1,
                "unitNo": 0,
                "units": null,
                "unitType": "FLAT",
                "length": 0.0,
                "width": 0.0,
                "builtupArea": 73.0,
                "assessableArea": 73.0,
                "bpaBuiltupArea": 0.0,
                "bpaNo": null,
                "bpaDate": null,
                "usage": "RESD",
                "occupancyType": null,
                "occupierName": null,
                "firmName": null,
                "rentCollected": 0.0,
                "structure": "RCCPOSHBUILDING",
                "age": "0TO25",
                "exemptionReason": null,
                "isStructured": true,
                "occupancyDate": "2017-05-21 00:00:00",
                "constCompletionDate": null,
                "manualArv": 0.0,
                "arv": 0.0,
                "electricMeterNo": null,
                "waterMeterNo": null,
                "parentid": 0,
                "isAuthorised": true,
                "auditDetails": {
                  "createdBy": "74",
                  "lastModifiedBy": "74",
                  "createdTime": 1501140313055,
                  "lastModifiedTime": 1501140313055
                }
              },
              {
                "id": 2,
                "unitNo": 0,
                "units": null,
                "unitType": "FLAT",
                "length": 0.0,
                "width": 0.0,
                "builtupArea": 53.0,
                "assessableArea": 53.0,
                "bpaBuiltupArea": 0.0,
                "bpaNo": null,
                "bpaDate": null,
                "usage": "RESD",
                "occupancyType": null,
                "occupierName": null,
                "firmName": null,
                "rentCollected": 0.0,
                "structure": "RCCPOSHBUILDING",
                "age": "0TO25",
                "exemptionReason": null,
                "isStructured": true,
                "occupancyDate": "2017-05-21 00:00:00",
                "constCompletionDate": null,
                "manualArv": 0.0,
                "arv": 0.0,
                "electricMeterNo": null,
                "waterMeterNo": null,
                "parentid": 0,
                "isAuthorised": true,
                "auditDetails": {
                  "createdBy": "74",
                  "lastModifiedBy": "74",
                  "createdTime": 1501140313055,
                  "lastModifiedTime": 1501140313055
                }
              }
            ],
            "auditDetails": {
              "createdBy": "74",
              "lastModifiedBy": "74",
              "createdTime": 1501140313055,
              "lastModifiedTime": 1501140313055
            }
          }
        ],
        "documents": [
          
        ],
        "stateId": "232",
        "applicationNo": "AP-PT-2017/07/27-003398-01",
        "taxCalculations": "[{\"toDate\": \"30/09/2017 23:59:59\", \"fromDate\": \"01/04/2017 00:00:00\", \"unitTaxes\": [{\"unitNo\": 0, \"unitTaxes\": {\"totalTax\": 295.0, \"manualARV\": null, \"rebateValue\": null, \"depreciation\": 3.0, \"calculatedARV\": 1445.9, \"effectiveDate\": \"01/04/2017 00:00:00\", \"headWiseTaxes\": [{\"taxDays\": 183, \"taxName\": \"EDU_CESS\", \"taxValue\": 58.0}, {\"taxDays\": 183, \"taxName\": \"PT_TAX\", \"taxValue\": 203.0}, {\"taxDays\": 183, \"taxName\": \"LIB_CESS\", \"taxValue\": 5.0}], \"occupancyDate\": \"21/05/2017\", \"residentialARV\": null, \"nonResidentialARV\": null}, \"floorNumber\": \"0\", \"usageFactor\": 1.0, \"assessableArea\": 73.0, \"subUsageFactor\": null, \"structureFactor\": 1.5}, {\"unitNo\": 0, \"unitTaxes\": {\"totalTax\": 1226.0, \"manualARV\": null, \"rebateValue\": null, \"depreciation\": 9.0, \"calculatedARV\": 4732.45, \"effectiveDate\": \"01/04/2017 00:00:00\", \"headWiseTaxes\": [{\"taxDays\": 183, \"taxName\": \"EDU_CESS\", \"taxValue\": 237.0}, {\"taxDays\": 183, \"taxName\": \"PT_TAX\", \"taxValue\": 852.0}, {\"taxDays\": 183, \"taxName\": \"LIB_CESS\", \"taxValue\": 18.0}], \"occupancyDate\": \"21/05/2017\", \"residentialARV\": null, \"nonResidentialARV\": null}, \"floorNumber\": \"0\", \"usageFactor\": 1.0, \"assessableArea\": 53.0, \"subUsageFactor\": null, \"structureFactor\": 1.5}], \"effectiveDate\": \"01/04/2017 00:00:00\", \"propertyTaxes\": {\"totalTax\": 1373.0, \"manualARV\": 0.0, \"rebateValue\": null, \"depreciation\": 12.0, \"calculatedARV\": 6179.0, \"effectiveDate\": \"01/04/2017 00:00:00\", \"headWiseTaxes\": [{\"taxDays\": 183, \"taxName\": \"EDU_CESS\", \"taxValue\": 295.0}, {\"taxDays\": 183, \"taxName\": \"PT_TAX\", \"taxValue\": 1055.0}, {\"taxDays\": 183, \"taxName\": \"LIB_CESS\", \"taxValue\": 23.0}], \"occupancyDate\": \"21/05/2017\", \"residentialARV\": null, \"nonResidentialARV\": null}}]",
        "workFlowDetails": null,
        "auditDetails": {
          "createdBy": "74",
          "lastModifiedBy": "74",
          "createdTime": 1501140313055,
          "lastModifiedTime": 1501140313055
        }
      },
      "vacantLand": null,
      "assessmentDate": "",
      "occupancyDate": "2017-04-01 00:00:00.0",
      "gisRefNo": "",
      "isAuthorised": false,
      "isUnderWorkflow": false,
      "boundary": {
        "id": 1,
        "revenueBoundary": {
          "id": 2,
          "name": "Zone-1"
        },
        "locationBoundary": {
          "id": 21,
          "name": null
        },
        "adminBoundary": {
          "id": 178,
          "name": "Election-Ward"
        },
        "northBoundedBy": "",
        "eastBoundedBy": "",
        "westBoundedBy": "",
        "southBoundedBy": "southboundedby",
        "auditDetails": {
          "createdBy": "74",
          "lastModifiedBy": "74",
          "createdTime": 1501140313055,
          "lastModifiedTime": 1501140313055
        }
      },
      "active": true,
      "channel": "SYSTEM",
      "auditDetails": {
        "createdBy": "74",
        "lastModifiedBy": "74",
        "createdTime": 1501140313055,
        "lastModifiedTime": 1501140313055
      },
      "demands": null
    }
  ]
  
  this.setState({
	  resultList: properties
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
												  {item.oldUpicNumber}
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Assessment Number
											  </Col>
											  <Col xs={8} md={6}>
												  {item.upicNumber}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Category of Ownership
											  </Col>
											  <Col xs={8} md={6}>
												  {item.propertyDetail.propertyType}
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
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Extent of Site (Sq.Mtrs)
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Registration Doc No
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Reason for Creation
											  </Col>
											  <Col xs={8} md={6}>
												 
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
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Effective Date
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Apartment/Complex Name
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Property Department
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Registration Doc Date
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Assessment Date
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
												 {item.address.addressNumber ? item.address.addressNumber+', ' : '' }
												 {item.address.addressLine1 ? item.address.addressLine1+', ' : '' }
												 {item.address.addressLine2 ? item.address.addressLine2+', ':''}
												 {item.address.landmark ? item.address.landmark+', ' : ''}
												 {item.address.city ? item.address.city+', ' : ''}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Zone
											  </Col>
											  <Col xs={8} md={6}>
												 {item.boundary.revenueBoundary.name}
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
												  {item.boundary.adminBoundary.name}
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
												  {item.address.addressLine1 || ''}
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
																  {owner.aadhaarNumber ? owner.aadhaarNumber : ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														<ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Mobile Number (without +91)
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.mobileNumber ? owner.mobileNumber : ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Owner Name
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.name ? owner.name : ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																  Gender
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.gender ? owner.gender : ''}
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
																  {owner.emailId ? owner.emailId : ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Guardian Relation
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.gaurdianRelation ? owner.gaurdianRelation : ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Guardian
															  </Col>
															  <Col xs={8} md={6}>
																  {owner.gaurdian ? owner.gaurdian : ''}
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
												  {item.lift || ''}
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Toilets
											  </Col>
											  <Col xs={8} md={6}>
												  {item.toilet || ''}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Water Tap
											  </Col>
											  <Col xs={8} md={6}>
												  {item.waterTap || ''}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Electricity
											  </Col>
											  <Col xs={8} md={6}>
												  {item.electricity || ''}
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
												  {item.attachedBathroom || ''}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Water Harvesting
											  </Col>
											  <Col xs={8} md={6}>
												  {item.waterHarvesting || ''}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Cable Connection
											  </Col>
											  <Col xs={8} md={6}>
												  {item.cableConnection || ''}
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
												  {item.propertyDetail.floorType}
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Wall Type
											  </Col>
											  <Col xs={8} md={6}>
												  {item.propertyDetail.wallType}
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
												{item.propertyDetail.roofType}
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Wood Type
											  </Col>
											  <Col xs={8} md={6}>
												{item.propertyDetail.woodType}
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
								  {item.propertyDetail.floors.length !=0 && item.propertyDetail.floors.map((floor, index)=>(
									<div key={index}> <h5 style={{marginLeft:'30px'}}> Floor Number {floor.floorNo || ''}</h5>
										{floor.units.length !=0 && floor.units.map((unit, index)=>{
											return(
													<CardText key={index}>
													<Col md={6} xs={12}>
														<ListGroup>
														
														<ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Classification of Building
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.structure || ''}
															  </Col>
															</Row>
														  </ListGroupItem>							  
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Nature of Usage
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.usage || ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Firm Name
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.firmName || ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Occupancy
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.occupancyType || ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Occupant Name
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.occupierName || ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Construction Date	
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.constCompletionDate || ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Effective From Date	
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.occupancyDate || ''}
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
																   Unstructured land
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.isStructured || ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Length
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.length || ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Breadth
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.width || ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Plinth area (Sq.Mtrs)
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.builtupArea || ''}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Building Permission no
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.bpaNo}
															  </Col>
															</Row>
														  </ListGroupItem>
														  <ListGroupItem>
															<Row>
															  <Col xs={4} md={6} style={styles.bold}>
																   Building Permission Date
															  </Col>
															  <Col xs={8} md={6}>
																  {unit.bpaDate}
															  </Col>
															</Row>
														  </ListGroupItem>
													  </ListGroup>
													</Col>
													<div className="clearfix"></div>
											  </CardText>
											)
										})}
										<hr/>
									  </div>
								  ))}
                              
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
  
 });

export default connect(mapStateToProps, mapDispatchToProps)(ViewProperty);
