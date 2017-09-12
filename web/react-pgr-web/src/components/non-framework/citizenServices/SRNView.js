import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {translate} from '../../common/common';
import Api from '../../../api/api';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import {getFullDate} from '../../framework/utility/utility';

let nameMap = {
	  "CREATED": "Created",
	  "WATER_NEWCONN": "New Water Connection",
	  "CANCELLED": "Request Cancelled",
	  "REJECTED": "Rejected",
	  "BPA_FIRE_NOC": "Fire NOC",
	  "INPROGRESS": "In Progress",
	  "APPROVED": "Approved",
	  "PT_EXTRACT": "Property Extract",
	  "WC_PAYTAX": "Water Charge Tax Payment",
	  "PT_PAYTAX": "Property Tax Payment",
	  "ESTIMATIONNOTICEGENERATED": "Estimation Notice Generated",
	  "VERIFIED": "Verified",
	  "ESTIMATIONAMOUNTCOLLECTED": "Estimation Amount Collected",
	  "WORKORDERGENERATED": "Work Order Generated",
	  "SANCTIONED": "Sanctioned",
	  "TL_NEWCONN": "New Trade License"
};

class CertificateView extends Component {
	constructor(props) {
		super(props);
		this.state = {
			open: false,
			ServiceRequest: {},
			statuses: ["APPROVED", "SANCTIONED"],
			serviceCodes: ["TL_NEWCONN", "WATER_NEWCONN", "BPA_FIRE_NOC"]
		};
	}

	componentDidMount() {
		let srn = decodeURIComponent(this.props.match.params.srn);
		let self = this;
		self.props.setLoadingStatus('loading'); 
		Api.commonApiPost("/citizen-services/v1/requests/anonymous/_search", {serviceRequestId: srn}, {}, false, true).then(function(res) {
			self.props.setLoadingStatus('hide');
			if(res && res.serviceReq && res.serviceReq.length && self.state.serviceCodes.indexOf(res.serviceReq[0].serviceCode) > -1) {
				self.setState({
					ServiceRequest: res.serviceReq[0]
				})
			} else {
				self.setState({
					open: true
				})
			}
		}, function(err) {
			self.props.setLoadingStatus('hide');
			self.setState({
				open: true
			})
		})
	}

	handleClose = () => {
		this.props.setRoute("/" + (localStorage.getItem("tenantId") || "default"));
	}

	render () {
		let self = this;
		let { open, ServiceRequest } = this.state;
		let { handleClose } = this;

		return (
			<div>
				<Card className="uiCard">
                    <CardHeader title={"Application Info - " + decodeURIComponent(self.props.match.params.srn)}/>
                    <CardText>
                    	<Grid>
                    		<Row>
                    			<Col xs={12} sm={4} md={3}>
                    				<Col style={{textAlign:"left"}} xs={12}>
						              <label><span style={{"fontWeight":500, "fontSize": "13px"}}>SRN Date</span></label>
						            </Col>
						            <Col style={{textAlign:"left"}} xs={12}>{ServiceRequest.auditDetails && ServiceRequest.auditDetails.createdDate ? getFullDate(ServiceRequest.auditDetails.createdDate) : "-"}</Col>
                    			</Col>
                    			<Col xs={12} sm={4} md={3}>
                    				<Col style={{textAlign:"left"}} xs={12}>
						              <label><span style={{"fontWeight":500, "fontSize": "13px"}}>Application Type</span></label>
						            </Col>
						            <Col style={{textAlign:"left"}} xs={12}>{nameMap[ServiceRequest.serviceCode] || ServiceRequest.serviceCode || "-"}</Col>
                    			</Col>
                    			<Col xs={12} sm={4} md={3}>
                    				<Col style={{textAlign:"left"}} xs={12}>
						              <label><span style={{"fontWeight":500, "fontSize": "13px"}}>Applicant Name</span></label>
						            </Col>
						            <Col style={{textAlign:"left"}} xs={12}>{ServiceRequest.firstName || "-"}</Col>
                    			</Col>
                    			<Col xs={12} sm={4} md={3}>
                    				<Col style={{textAlign:"left"}} xs={12}>
						              <label><span style={{"fontWeight":500, "fontSize": "13px"}}>Application Status</span></label>
						            </Col>
						            <Col style={{textAlign:"left"}} xs={12}>{nameMap[ServiceRequest.status] || ServiceRequest.status || "-"}</Col>
                    			</Col>
                    		</Row>
                    	</Grid>
                    </CardText>	
				</Card>
				{
					self.state.statuses.indexOf(ServiceRequest.status) > -1 ?

					<Card className="uiCard">
	                    <CardHeader title={"Application Info - " + decodeURIComponent(self.props.match.params.srn)}/>
	                    <CardText>
	                    	<Table responsive style={{fontSize:"bold"}} bordered condensed>
	                          <thead>
	                            <tr>
	                              <th>By</th>
	                              <th>Date</th>
	                              <th>File Name</th>
	                              <th>Action</th>
	                            </tr>
	                          </thead>
	                          <tbody>
	                            {
	                              ServiceRequest && 
	                              ServiceRequest.documents && 
	                              ServiceRequest.documents.length ? 
	                              ServiceRequest.documents.map(function(v, i) {
	                              	return (
		                                  <tr key={i}>
		                                    <td>{v.from}</td>
		                                    <td>{getFullDate(v.timeStamp)}</td>
		                                    <td>{v.name}</td>
		                                    <td><a target="_blank" href={"/filestore/v1/files/id?tenantId=" + localStorage.getItem("tenantId") + "&fileStoreId=" + v.filePath}>Download</a></td>
		                                  </tr>
		                                )
	                              }) : <tr><td style={{"textAlign": "center"}} colSpan={self.props.showRemarks ? 5 : 4}>No documents uploaded!</td></tr>
	                            }
	                          </tbody>
	                      </Table>
	                    </CardText>	
					</Card> : ""
				}
				<Dialog
		            title="Not Found"
		            modal={false}
		            open={open}
		            actions={<FlatButton
						        label="Ok"
						        primary={true}
						        onClick={this.handleClose}
						      />}
		            onRequestClose={handleClose}>
		            <div>
		            	Application not found.
		            </div>
		        </Dialog>
			</div>
		);
	}
}

const mapStateToProps = state => ({
  formData:state.frameworkForm.form
});

const mapDispatchToProps = dispatch => ({
  setMockData: (mockData) => {
    dispatch({type: "SET_MOCK_DATA", mockData});
  },
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route})
});

export default connect(mapStateToProps, mapDispatchToProps)(CertificateView);
