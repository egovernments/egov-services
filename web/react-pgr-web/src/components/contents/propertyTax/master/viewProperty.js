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

  componentDidMount()
  {
    
   
  }

  componentWillUnmount(){
  
  }


  componentWillUpdate() {

  }

  componentDidUpdate(prevProps, prevState) {
    
  }

  render() {
	 return(
		<div className="viewProperty">
            <Card style={styles.marginStyle}>
                <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Property details</div>} />
                <CardText style={{padding:0}}>
                    <Grid>
                          <br/>
                          <Card>
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
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Assessment Number
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Category of Ownership
											  </Col>
											  <Col xs={8} md={6}>
												 
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
						  <hr/>
						  <Card>
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
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Property Address
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Zone
											  </Col>
											  <Col xs={8} md={6}>
												 
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
						  <hr/>
						  <Card>
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Owner Details</div>} />
                              <CardText>
									<Col md={6} xs={12}>
										<ListGroup>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Aadhaar No
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Mobile Number (without +91)
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Owner Name
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Gender
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
												   Email Address
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Guardian Relation
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Guardian
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
						  <hr/>
						  <Card>
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
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Toilets
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Water Tap
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Electricity
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
												   Attached Bathroom
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Water Harvesting
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												  Cable Connection
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
						  <hr/>
						  <Card>
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
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Wall Type
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
												   Roof Type
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Wood Type
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
						  <hr/>
						  <Card>
							  <CardHeader style={{paddingBottom:0}}  title={<div style={styles.headerStyle}>Floor Details</div>} />
                              <CardText>
									<Col md={6} xs={12}>
										<ListGroup>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Floor Number
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>
										<ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Classification of Building
											  </Col>
											  <Col xs={8} md={6}>
												 
											  </Col>
											</Row>
										  </ListGroupItem>							  
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Nature of Usage
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Firm Name
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Occupancy
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Occupant Name
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Construction Date	
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Effective From Date	
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
												   Unstructured land
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Length
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Breadth
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Plinth area (Sq.Mtrs)
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Building Permission no
											  </Col>
											  <Col xs={8} md={6}>
												
											  </Col>
											</Row>
										  </ListGroupItem>
										  <ListGroupItem>
											<Row>
											  <Col xs={4} md={6} style={styles.bold}>
												   Building Permission Date
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
                      </Grid>
                </CardText>
            </Card>
        </div>)
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => ({
  
 });

export default connect(mapStateToProps, mapDispatchToProps)(ViewProperty);
