import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {Grid, Row, Col} from 'react-bootstrap';
import {List, ListItem} from 'material-ui/List';
import TextField from 'material-ui/TextField';
import FlatButton from 'material-ui/FlatButton';
import RaisedButton from 'material-ui/RaisedButton';
import Dialog from 'material-ui/Dialog';
import {Table,TableBody,TableHeader,TableHeaderColumn,TableRow,TableRowColumn} from 'material-ui/Table';
import jsPDF from 'jspdf';
import WorkFlow from '../workflow/WorkFlow';
import {translate, epochToDate} from '../../../common/common';
import Api from '../../../../api/api';
import styles from '../../../../styles/material-ui';

var self;

class viewLicense extends Component{
  constructor(props){
    super(props);
    this.state={
      open: false,
      fieldInspection : false
    };
  }
  componentDidMount(){
    this.initData(this.props.match.params.id);
  }
  componentWillReceiveProps(nextProps){
    if(this.props.match.params.id !== nextProps.match.params.id){
      this.initData(nextProps.match.params.id);
    }
  }
  initData = (id) => {
    let {setForm, setLoadingStatus} = this.props;
    setLoadingStatus('loading');
    Api.commonApiPost("/tl-services/license/v1/_search",{ids : id}, {}, false, true).then(function(response)
    {
      if(response.licenses.length > 0){
        self.setState({license : response.licenses[0]});
        setForm(response.licenses[0]);
        if(!response.licenses[0].isLegacy){
          self.getEmployees();
          self.history();
          console.log(response.licenses[0].applications[0].statusName);
          if(response.licenses[0].applications[0].statusName == 'Scrutiny Completed')
            self.setState({fieldInspection : true});
          else{
            self.setState({fieldInspection : false});
          }
        }
        setLoadingStatus('hide');
      }else{
        self.handleError('License does not exist');
      }
    },function(err) {
      setLoadingStatus('hide');
      self.handleError(err.message);
    });
  }
  handleError = (msg) => {
    let {toggleDailogAndSetText, setLoadingStatus}=this.props;
    setLoadingStatus('hide');
    toggleDailogAndSetText(true, msg);
  }
  renderFeeDetails = () => {
    let {viewLicense} = this.props;
    if(viewLicense.applications && viewLicense.applications[0].feeDetails && viewLicense.applications[0].feeDetails.length > 0){
      return(
        <Card style={styles.cardSpacing}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
             Fee Details
           < /div>}/>
         <CardText style={{padding:'8px 16px 0'}}>
           <Table>
             <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
               <TableRow>
                 <TableHeaderColumn>Financial Year</TableHeaderColumn>
                 <TableHeaderColumn>Amount</TableHeaderColumn>
                 <TableHeaderColumn>Paid</TableHeaderColumn>
               </TableRow>
             </TableHeader>
             <TableBody displayRowCheckbox={false}>
               {viewLicense.applications[0].feeDetails.map((fee, index)=>{
                  return(
                    <TableRow selectable={false} key={index}>
                      <TableRowColumn>{fee.financialYear}</TableRowColumn>
                      <TableRowColumn style={{textAlign:'right'}}>{fee.amount}</TableRowColumn>
                      <TableRowColumn>{fee.paid ? 'Yes' : 'No'}</TableRowColumn>
                    </TableRow>
                  )
               })}
             </TableBody>
           </Table>
         </CardText>
        </Card>
      )
    }
  }
  supportDocuments = () => {
    let {viewLicense} = this.props;
    if(viewLicense.applications && viewLicense.applications[0].supportDocuments && viewLicense.applications[0].supportDocuments.length > 0){
      return(
        <Card style={styles.cardSpacing}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
             Supporting Documents
           < /div>}/>
         <CardText style={{padding:'8px 16px 0'}}>
           <Table>
             <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
               <TableRow>
                 <TableHeaderColumn>S.no</TableHeaderColumn>
                 <TableHeaderColumn>Document Name</TableHeaderColumn>
                 <TableHeaderColumn>comments</TableHeaderColumn>
                 <TableHeaderColumn>Download</TableHeaderColumn>
               </TableRow>
             </TableHeader>
             <TableBody displayRowCheckbox={false}>
               {viewLicense.applications[0].supportDocuments.map((docs, index)=>{
                  return(
                    <TableRow selectable={false} key={index}>
                      <TableRowColumn>{index+1}</TableRowColumn>
                      <TableRowColumn>{docs.documentTypeName}</TableRowColumn>
                      <TableRowColumn>{docs.comments}</TableRowColumn>
                      <TableRowColumn><a href={'/filestore/v1/files/id?fileStoreId='+docs.fileStoreId+'&tenantId='+localStorage.getItem('tenantId')} download>Download</a></TableRowColumn>
                    </TableRow>
                  )
               })}
             </TableBody>
           </Table>
         </CardText>
        </Card>
      )
    }
  }
  getEmployees = () => {
    Api.commonApiPost('hr-employee/employees/_search').then((response)=>{
      self.setState({employees : response.Employee});
    },function(err) {
      self.handleError(err.message);
    });
  }
  history = () => {
    let {viewLicense} = this.props;
    if(Object.keys(viewLicense).length && viewLicense.applications[0].state_id){
      Api.commonApiPost('egov-common-workflows/history', {workflowId:viewLicense.applications[0].state_id},{}, false, true).then((response)=>{
        // console.log(response);
        self.setState({tasks : response.tasks});
      },function(err) {
        self.handleError(err.message);
      });
    }
  }
  showHistory = () => {
    if(this.state.tasks && this.state.employees && this.state.tasks.length > 0){
      return(
        <Card style={styles.cardSpacing}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
             History
           < /div>}/>
         <CardText style={{padding:'8px 16px 0'}}>
           <Table>
             <TableHeader displaySelectAll={false} adjustForCheckbox={false}>
               <TableRow>
                 <TableHeaderColumn style={styles.customColumnStyle}>{translate('core.lbl.date')}</TableHeaderColumn>
                 <TableHeaderColumn style={styles.customColumnStyle} className="hidden-xs">{translate('pgr.lbl.updatedby')}</TableHeaderColumn>
                 <TableHeaderColumn style={styles.customColumnStyle}>{translate('core.lbl.status')}</TableHeaderColumn>
                 <TableHeaderColumn style={styles.customColumnStyle}>{translate('pgr.lbl.currentowner')}</TableHeaderColumn>
                 <TableHeaderColumn style={styles.customColumnStyle}>{translate('core.lbl.comments')}</TableHeaderColumn>
               </TableRow>
             </TableHeader>
             <TableBody displayRowCheckbox={false}>
               {this.state.tasks.map((task, index)=>{
                 let userObj = this.state.employees.find(x => x.id === task.owner.id);
                  return(
                    <TableRow selectable={false} key={index}>
                      <TableRowColumn style={styles.customColumnStyle}>{task.createdDate}</TableRowColumn>
                      <TableRowColumn style={styles.customColumnStyle} className="hidden-xs">{task.senderName}</TableRowColumn>
                      <TableRowColumn style={styles.customColumnStyle}>{task.status}</TableRowColumn>
                      <TableRowColumn style={styles.customColumnStyle}>{userObj ? userObj.name : ''}</TableRowColumn>
                      <TableRowColumn style={styles.customColumnStyle}>{task.comments}</TableRowColumn>
                    </TableRow>
                  )
               })}
             </TableBody>
           </Table>
         </CardText>
       </Card>
       )
    }
  }
  fieldInspection = () => {
    let {viewLicense} = this.props;
    // console.log('field inspection:', this.state.fieldInspection);
    if(viewLicense.applications && this.state.fieldInspection){
      return(
        <Card style={styles.cardSpacing}>
          <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
             Field Inspection
           < /div>}/>
         <CardText style={styles.cardTextPadding}>
             <Row>
               <Col xs={12} sm={6} md={4} lg={3}>
                 <TextField fullWidth={true} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('Trade value for the UOM')+' *'}
                    value={this.props.viewLicense.quantity?this.props.viewLicense.quantity:""}
                    errorText={this.props.fieldErrors.quantity ? this.props.fieldErrors.quantity : ""}
                    maxLength="13"
                    onChange={(event, value) => this.props.handleChange(value, "quantity", false, /^\d{0,10}(\.\d{1,2})?$/, 'Please enter only numbers (with optional 2 decimal values)')}/>
               </Col>
               <Col xs={12} sm={6} md={4} lg={3}>
                 <TextField fullWidth={true} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('License Fee')+' *'}
                    value={this.props.viewLicense.licenseFee?this.props.viewLicense.licenseFee:""}
                    errorText={this.props.fieldErrors.licenseFee ? this.props.fieldErrors.licenseFee : ""}
                    maxLength="13"
                    onChange={(event, value) => this.props.handleChange(value, "licenseFee", false, /^\d{0,10}(\.\d{1,2})?$/, 'Please enter only numbers (with optional 2 decimal values)')}/>
               </Col>
               <Col xs={12} sm={6} md={4} lg={6}>
                 <TextField fullWidth={true} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('Field Inspection Report')+' *'} multiLine={true}
                    value={this.props.viewLicense.fieldInspectionReport?this.props.viewLicense.fieldInspectionReport:""}
                    maxLength="500"
                    onChange={(event, value) => this.props.handleChange(value, "fieldInspectionReport", false, /^.[^]{0,500}$/)}/>
               </Col>
             </Row>
           </CardText>
         </Card>
       )
    }
  }
  collection = () => {
    let {viewLicense} = this.props;
    let userRequest = JSON.parse(localStorage.getItem('userRequest'));
    // console.log(JSON.stringify(userRequest.roles));
    let roleObj = userRequest ? userRequest.roles.find(role => role.name === 'Collection Operator'): '';
    // console.log(roleObj);
    if(!viewLicense.isLegacy && viewLicense.applications && viewLicense.applications[0].state_id && viewLicense.applications[0].statusName == 'Final approval Completed' && roleObj){
     return(
       <div className="text-center">
         <RaisedButton label="Collect License Fee" primary={true} onClick={(e)=>{this.props.setRoute('/non-framework/collection/master/paytax/PayTaxCreate')}}/>
       </div>
     )
   }
  }
  updateWorkFlow = (item, state) => {
    // console.log('came to workflow');
    let {setLoadingStatus, viewLicense} = this.props;
    if((item.key === 'Reject' || item.key === 'Cancel') && !state.approvalComments){
      self.handleError('Comments is mandatory while '+item.key);
      return;
    }
    // console.log(!state.departmentId, !state.designationId, !state.positionId);
    if(item.key === 'Forward'){
      if(this.state.fieldInspection && (!viewLicense.quantity || !viewLicense.licenseFee || !viewLicense.fieldInspectionReport)){
          if(!viewLicense.quantity){
            self.handleError('Trade value for UOM is mandatory');
          }else if(!viewLicense.licenseFee){
            self.handleError('License Fee is mandatory');
          }else if(!viewLicense.fieldInspectionReport){
            self.handleError('Field Inspection Report is mandatory');
          }
          return;
      }
      //validate pattern for UOM and licensefee
      var pattern = /^\d{0,10}(\.\d{1,2})?$/;
      if(this.state.fieldInspection && viewLicense.quantity){
        if (pattern.test(viewLicense.quantity)) {
          // console.log('pattern passed for quantity');
        }else{
          self.handleError('Trade value for UOM - Please enter only numbers (with optional 2 decimal values)');
          return;
        }
      }
      if(this.state.fieldInspection && viewLicense.licenseFee){
        // console.log('came to test license fee');
        if (pattern.test(viewLicense.licenseFee)) {
          // console.log('pattern passed for license fee');
        }else{
          self.handleError('License Fee - Please enter only numbers (with optional 2 decimal values)');
          return;
        }
      }
      if(!state.departmentId || !state.designationId || !state.positionId){
        if(!state.departmentId)
          self.handleError('Approver Department is mandatory');
        else if(!state.designationId)
          self.handleError('Approver Designation is mandatory');
        else
          self.handleError('Approver is mandatory');
        return;
      }
    }

    setLoadingStatus('loading');
    let departmentObj = state.workFlowDepartment.find(x => x.id === state.departmentId)
    let designationObj = state.workFlowDesignation.find(x => x.id === state.designationId)
    // console.log('update the workflow:', this.state.departmentId, this.state.designationId, this.state.positionId, this.state.approvalComments);
    let workFlowDetails = {
      "department": departmentObj ? departmentObj.name : null,
      "designation": designationObj ? designationObj.name : null,
      "assignee": state.positionId ? state.positionId : item.key === 'Approve' ? state.process.initiatorPosition : null,
      "action": item.key,
      "status": state.process.status,
      "comments": state.approvalComments || '',
      "senderName": "",
      "details": "",
      "stateId": state.stateId
    }
    // console.log('Workflow details from response:',this.state.obj.applications[0].workFlowDetails);
    var finalObj = {...state.obj};
    finalObj['application'] = finalObj.applications[0];
    finalObj.supportDocuments = finalObj.applications[0].supportDocuments;

    if(this.state.fieldInspection){
      finalObj['application']['licenseFee'] = viewLicense.licenseFee;
      finalObj['application']['fieldInspectionReport'] = viewLicense.fieldInspectionReport;
    }

    finalObj['application']['workFlowDetails'] = workFlowDetails;
    delete finalObj['applications'];
    delete finalObj['departmentId'];
    delete finalObj['designationId'];
    delete finalObj['positionId'];
    delete finalObj['approvalComments'];
    delete finalObj['licenseFee'];
    delete finalObj['fieldInspectionReport'];

    var finalArray = [];
    finalArray.push(finalObj);
    // console.log('updated copied response:', JSON.stringify(finalArray));
    Api.commonApiPost("tl-services/license/v1/_update", {}, {licenses : finalArray}, false, true).then(function(response) {
        //update workflow
        setLoadingStatus('hide');
        self.handleOpen();
    }, function(err) {
      setLoadingStatus('hide');
      self.handleError(err.message);
    });
  }
  generatePdf = () => {
    // console.log('generate pdf');
    var doc = new jsPDF()
    doc.text('Hello world!', 10, 10)
    doc.save('a4.pdf')
  }
  handleOpen = () => {
    this.setState({open: true});
  }
  handleClose = () => {
    let {setRoute} = this.props;
    this.setState({open: false});
    window.location.reload();
  }
  render(){
    self = this;
    let {handleError} = this;
    let {viewLicense, fieldErrors, isFormValid, handleChange} = this.props;
    const actions = [
      <FlatButton
        label={translate('core.lbl.ok')}
        primary={true}
        onClick={this.handleClose}
      />,
    ];
    return(
      <Grid style={styles.fullWidth}>
        <h3 className="text-center">View Trade License</h3>
        <Card style={styles.marginStyle}>
          <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
             {translate('tl.create.licenses.groups.TradeDetailsTab')}
           < /div>}/>
         <CardText style={styles.cardTextPadding}>
            <List style={styles.zeroPadding}>
              <Row>
                {!viewLicense.isLegacy ?
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText="Application Number"
                    secondaryText={<p style={styles.customColumnStyle}>{viewLicense.applicationNumber}</p>}
                  />
                </Col> : '' }
                {!viewLicense.isLegacy ?
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText="Application Date"
                    secondaryText={<p style={styles.customColumnStyle}>{epochToDate(viewLicense.applicationDate)}</p>}
                  />
              </Col> : '' }
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText="License Number"
                    secondaryText={<p style={styles.customColumnStyle}>{viewLicense.licenseNumber ? viewLicense.licenseNumber : 'N/A'}</p>}
                  />
                </Col>
                {viewLicense.isLegacy ?
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText="Old License Number"
                    secondaryText={<p style={styles.customColumnStyle}>{viewLicense.oldLicenseNumber ? viewLicense.oldLicenseNumber : 'N/A'}</p>}
                  />
                </Col>
                : ''}
              </Row>
            </List>
          </CardText>
        </Card>
        <Card style={styles.marginStyle}>
          <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
             {translate('tl.create.licenses.groups.TradeOwnerDetails')}
           < /div>}/>
         <CardText style={styles.cardTextPadding}>
            <List style={styles.zeroPadding}>
              <Row>
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText={translate('tl.create.licenses.groups.TradeOwnerDetails.AadharNumber')}
                    secondaryText={<p style={styles.customColumnStyle}>{viewLicense.adhaarNumber ? viewLicense.adhaarNumber : 'N/A'}</p>}
                  />
                </Col>
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText={translate('tl.create.licenses.groups.TradeOwnerDetails.Mobile Number')}
                    secondaryText={<p style={styles.customColumnStyle}>{viewLicense.mobileNumber ? viewLicense.mobileNumber : 'N/A'}</p>}
                  />
                </Col>
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText={translate('tl.create.licenses.groups.TradeOwnerDetails.TradeOwnerName')}
                    secondaryText={<p style={styles.customColumnStyle}>{viewLicense.ownerName ? viewLicense.ownerName : 'N/A'}</p>}
                  />
                </Col>
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText={translate('tl.create.licenses.groups.TradeOwnerDetails.FatherSpouseName')}
                    secondaryText={<p style={styles.customColumnStyle}>{viewLicense.fatherSpouseName ? viewLicense.fatherSpouseName : 'N/A'}</p>}
                  />
                </Col>
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText={translate('tl.create.licenses.TradeOwnerDetails.groups.EmailID')}
                    secondaryText={<p style={styles.customColumnStyle}>{viewLicense.emailId ? viewLicense.emailId : 'N/A'}</p>}
                  />
                </Col>
                <Col xs={12} sm={6} md={4} lg={3}>
                  <ListItem
                    primaryText={translate('tl.create.licenses.groups.TradeOwnerDetails.TradeOwnerAddress')}
                    secondaryText={<p style={styles.customColumnStyle}>{viewLicense.ownerAddress ? viewLicense.ownerAddress : 'N/A'}</p>}
                  />
                </Col>
              </Row>
            </List>
          </CardText>
        </Card>
        <Card style={styles.marginStyle}>
          <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
             {translate('tl.create.licenses.groups.TradeLocationDetails')}
           < /div>}/>
         <CardText style={styles.cardTextPadding}>
               <List style={styles.zeroPadding}>
                 <Row>
                   <Col xs={12} sm={6} md={4} lg={3}>
                     <ListItem
                       primaryText={translate('tl.create.licenses.groups.TradeLocationDetails.PropertyAssessmentNo')}
                       secondaryText={<p style={styles.customColumnStyle}>{viewLicense.propertyAssesmentNo ? viewLicense.propertyAssesmentNo : 'N/A'}</p>}
                     />
                   </Col>
                   <Col xs={12} sm={6} md={4} lg={3}>
                     <ListItem
                       primaryText={translate('tl.create.licenses.groups.TradeLocationDetails.Locality')}
                       secondaryText={<p style={styles.customColumnStyle}>{viewLicense.localityName ? viewLicense.localityName : 'N/A'}</p>}
                     />
                   </Col>
                   <Col xs={12} sm={6} md={4} lg={3}>
                     <ListItem
                       primaryText={translate('tl.create.licenses.groups.TradeLocationDetails.adminWardId')}
                       secondaryText={<p style={styles.customColumnStyle}>{viewLicense.adminWardName ? viewLicense.adminWardName : 'N/A'}</p>}
                     />
                   </Col>
                   <Col xs={12} sm={6} md={4} lg={3}>
                     <ListItem
                       primaryText={translate('tl.create.licenses.groups.TradeLocationDetails.revenueWardId')}
                       secondaryText={<p style={styles.customColumnStyle}>{viewLicense.revenueWardName ? viewLicense.revenueWardName : 'N/A'}</p>}
                     />
                   </Col>
                   <Col xs={12} sm={6} md={4} lg={3}>
                     <ListItem
                       primaryText={translate('tl.create.licenses.groups.TradeLocationDetails.OwnershipType')}
                       secondaryText={<p style={styles.customColumnStyle}>{viewLicense.ownerShipType ? viewLicense.ownerShipType : 'N/A'}</p>}
                     />
                   </Col>
                   <Col xs={12} sm={6} md={4} lg={3}>
                     <ListItem
                       primaryText={translate('tl.create.licenses.groups.TradeLocationDetails.TradeAddress')}
                       secondaryText={<p style={styles.customColumnStyle}>{viewLicense.tradeAddress ? viewLicense.tradeAddress : 'N/A'}</p>}
                     />
                   </Col>
                 </Row>
               </List>
           </CardText>
         </Card>
         <Card style={styles.marginStyle}>
           <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
              {translate('tl.create.licenses.groups.TradeDetails')}
            < /div>}/>
          <CardText style={styles.cardTextPadding}>
                <List style={styles.zeroPadding}>
                  <Row>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('tl.create.licenses.groups.TradeDetails.TradeTitle')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.tradeTitle ? viewLicense.tradeTitle : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('tl.create.licenses.groups.TradeDetails.TradeType')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.tradeType ? viewLicense.tradeType : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('tl.create.licenses.groups.TradeDetails.TradeCategory')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.category ? viewLicense.category : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('tl.create.licenses.groups.TradeDetails.TradeSubCategory')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.subCategory ? viewLicense.subCategory : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('tl.create.licenses.groups.TradeDetails.UOM')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.uom ? viewLicense.uom : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('tl.create.licenses.groups.TradeDetails.tradeValueForUOM')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.quantity ? viewLicense.quantity : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('tl.create.licenses.groups.validity')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.validityYears ? viewLicense.validityYears : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('tl.create.licenses.groups.TradeDetails.Remarks')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.remarks ? viewLicense.remarks : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('tl.create.licenses.groups.TradeDetails.TradeCommencementDate')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.tradeCommencementDate ? epochToDate(viewLicense.tradeCommencementDate) : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('License valid from Date')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.licenseValidFromDate ? epochToDate(viewLicense.licenseValidFromDate) : 'N/A'}</p>}
                      />
                    </Col>
                    <Col xs={12} sm={6} md={4} lg={3}>
                      <ListItem
                        primaryText={translate('License Expiry Date')}
                        secondaryText={<p style={styles.customColumnStyle}>{viewLicense.expiryDate ? epochToDate(viewLicense.expiryDate) : 'N/A'}</p>}
                      />
                    </Col>
                  </Row>
                </List>
            </CardText>
          </Card>
          <Card style={styles.marginStyle}>
            <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
               {translate('tl.view.licenses.groups.agreementDetails')}
             < /div>}/>
           <CardText style={styles.cardTextPadding}>
              <List style={styles.zeroPadding}>
                <Row>
                  <Col xs={12} sm={6} md={4} lg={3}>
                    <ListItem
                      primaryText={translate('tl.create.licenses.groups.agreementDetails.agreementDate')}
                      secondaryText={<p style={styles.customColumnStyle}>{viewLicense.agreementDate ? epochToDate(viewLicense.agreementDate) : 'N/A'}</p>}
                    />
                  </Col>
                  <Col xs={12} sm={6} md={4} lg={3}>
                    <ListItem
                      primaryText={translate('tl.create.licenses.groups.agreementDetails.agreementNo')}
                      secondaryText={<p style={styles.customColumnStyle}>{viewLicense.agreementNo ? viewLicense.agreementNo : 'N/A'}</p>}
                    />
                  </Col>
                </Row>
              </List>
            </CardText>
          </Card>
          {viewLicense.isLegacy ? this.renderFeeDetails() : ''}
          {this.supportDocuments()}
          {!viewLicense.isLegacy ? this.showHistory() : ''}
          {!viewLicense.isLegacy && this.state.fieldInspection ? this.fieldInspection() : ''}
          {!viewLicense.isLegacy && viewLicense.applications && viewLicense.applications[0].state_id && viewLicense.applications[0].statusName != 'Final approval Completed' ?
          <Card style={styles.marginStyle}>
            <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
               Workflow
             < /div>}/>
            <CardText style={styles.cardTextPadding}>
               <WorkFlow viewLicense={viewLicense} isFormValid={isFormValid} fieldErrors={fieldErrors} handleChange={handleChange} handleError={handleError} setLoadingStatus={this.props.setLoadingStatus} updateWorkFlow={this.updateWorkFlow}/>
             </CardText>
          </Card> :
          ""}
          {this.collection()}
          <Dialog
            title="Success"
            actions={actions}
            modal={true}
            open={this.state.open}
          >
          Workflow updated succesfully
          </Dialog>
          {/*<div className="text-center">
            <RaisedButton style={{margin:'15px 5px'}} label="License Certificate" primary={true} onClick={(e)=>{this.generatePdf()}}/>
          </div>*/}
      </Grid>
    );
  }
}

const mapStateToProps = state => {
  // console.log(state.form.form);
  return ({viewLicense : state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
};

const mapDispatchToProps = dispatch => ({
  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:false,
      fieldErrors: {},
      validationData: {
        required: {
          current: [],
          required: ['departmentId', 'designationId', 'positionId']
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  handleChange: (value, property, isRequired, pattern, errorMsg) => {
    dispatch({type: "HANDLE_CHANGE", property, value, isRequired, pattern, errorMsg});
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route})
});

export default connect(mapStateToProps, mapDispatchToProps)(viewLicense);
