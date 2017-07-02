import React, {Component} from 'react';
import {connect} from 'react-redux';
import FileDownload from 'material-ui/svg-icons/action/get-app';
import {Table,TableBody,TableRow,TableRowColumn} from 'material-ui/Table';
import {Grid, Row, Col, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {white} from 'material-ui/styles/colors';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import Api from '../../../api/api';
import {translate} from '../../common/common';
import Fields from '../../common/Fields';
import LoadingIndicator from '../../common/LoadingIndicator';
import  '../../../styles/custom.css';
var Rating = require('react-rating');

const styles = {
  headerStyle : {
    color: 'rgb(90, 62, 27)',
    fontSize : 19
  },
  addBorderBottom:{
    borderBottom: '1px solid #eee',
    padding: '10px'
  },
  marginStyle:{
    margin: '15px'
  }
};

var currentThis;

class grievanceView extends Component{
  constructor(props){
    super(props);
    this.state={
      loadingstatus: 'loading',
      open: false
    };
  }
  handleOpen = () => {
    this.setState({open: true});
  };

  handleClose = () => {
    this.setState({
      open: false,
      loadingstatus:'loading'
    });
    this.loadSRN();
  };

  componentDidMount(){
    currentThis = this;
    this.loadSRN();
  }

  loadSRN = () =>{

    let {initForm, addMandatory} = this.props;
    initForm();

    Api.commonApiPost("/pgr/servicedefinition/v1/_search",{serviceCode : 'COMPLAINT' }).then(function(response)
    {
      currentThis.setState({SD : response.attributes})
    },function(err) {

    });

    Api.commonApiPost("/pgr/seva/v1/_search",{serviceRequestId:currentThis.props.match.params.srn},{}).then(function(response)
    {
      //console.log(JSON.stringify(response.serviceRequests))
      currentThis.setState({srn : response.serviceRequests});
      currentThis.state.srn.map((item, index) => {
        for(var k in item){
           if(item[k] instanceof Array){
             item[k].map((attribValues, index) => {
               currentThis.setState({[Object.values(attribValues)[0]] : Object.values(attribValues)[1]});
             })
           }else{
             currentThis.setState({[k] : item[k]});
           }
        }
        addMandatory();
      });

      Api.commonApiPost('/workflow/history/v1/_search',{workflowId : currentThis.state.stateId}).then(function(response)
      {
        //console.log(JSON.stringify(response));
        currentThis.setState({workflow : response});

        Api.commonApiGet('/filestore/v1/files/tag',{tag : currentThis.state.serviceRequestId}).then(function(response)
        {
          //console.log(JSON.stringify(response));
          currentThis.setState({files : response.files});
          currentThis.getDepartmentById();

        },function(err) {

        });


      },function(err) {

      });

    },function(err) {

    });
  }
  getDepartmentById = () => {
    Api.commonApiPost("/egov-common-masters/departments/_search", {id:this.state.departmentId}).then(function(response)
    {
      currentThis.setState({departmentName : response.Department[0].name});
      currentThis.getReceivingCenter();
    },function(err) {

    });
  }
  getReceivingCenter(){
    if(this.state.receivingCenter){
      Api.commonApiPost("/pgr-master/receivingcenter/v1/_search", {id:this.state.receivingCenter}).then(function(response)
      {
        currentThis.setState({receivingCenterName : response.receivingCenters[0].name});
        currentThis.getLocation();
      },function(err) {

      });
    }else {
      currentThis.getLocation();
    }
  }
  getLocation(){
    if(this.state.childLocationId)
      Api.commonApiGet("/egov-location/boundarys", {boundary:this.state.childLocationId}).then(function(response)
      {
        currentThis.setState({childLocationName : response.Boundary[0].name});
        currentThis.nextStatus();
      },function(err) {

      });
    else {
      currentThis.setState({childLocationName : ""});
      currentThis.nextStatus();
    }
  }
  nextStatus = () => {
    if(localStorage.getItem('type')){
      Api.commonApiPost("/workflow/v1/nextstatuses/_search", {currentStatusCode:this.state.status}).then(function(response)
      {
        currentThis.setState({nextStatus : response.statuses});
        currentThis.allServices();
      },function(err) {

      });
    }else {
      currentThis.setState({loadingstatus:'hide'});
    }
  }
  allServices = () => {
    if(localStorage.getItem('type') == 'EMPLOYEE'){
      Api.commonApiPost("/pgr/services/v1/_search", {type:'ALL'}).then(function(response)
      {
        currentThis.setState({complaintTypes : response.complaintTypes});
        currentThis.getWard();
      },function(err) {

      });
    }else{
      currentThis.setState({loadingstatus:'hide'});
    }
  }
  getWard = () => {
    Api.commonApiPost("/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName", {boundaryTypeName:'Ward',hierarchyTypeName:'Administration'}).then(function(response)
    {
      currentThis.setState({ward : response.Boundary});
      currentThis.getLocality();
    },function(err) {

    });
  }
  getLocality = () => {
    Api.commonApiPost("/egov-location/boundarys/childLocationsByBoundaryId", {boundaryId:this.state.locationId}).then(function(response)
    {
      currentThis.setState({locality : response.Boundary});
      currentThis.getDepartment();
    },function(err) {

    });
  }
  getDepartment = () => {
    Api.commonApiPost("/egov-common-masters/departments/_search").then(function(response)
    {
      currentThis.setState({department : response.Department});
      currentThis.setState({loadingstatus:'hide'});
    },function(err) {

    });
  }
  renderWorkflow = () =>{
    if(this.state.workflow != undefined){
      return this.state.workflow.map((workflow, index) =>
      {
        var department;
        for(var k in workflow.values.department.values){
          department = Object.values(workflow.values.department.values[k])[1];
        }
        return (
          <Row style={styles.addBorderBottom} key={index}>
            <Col xs={12} md={2}>
              {workflow.created_Date}
            </Col>
            <Col xs={12} md={2}>
              {workflow.sender_name}
            </Col>
            <Col xs={12} md={2}>
              {workflow.status}
            </Col>
            <Col xs={12} md={2}>
              {workflow.owner}
            </Col>
            <Col xs={12} md={2}>
              {department}
            </Col>
            <Col xs={12} md={2}>
              {workflow.comments}
            </Col>
          </Row>
        )
      });
    }
  }
  filesUploaded = () =>{
    if(this.state.files != undefined){
      return this.state.files.map((files, index) => {
        return (
          <Col xs={6} md={3} key={index}>
            <RaisedButton
              href={files.url}
              download
              label={"File " + (index+1)}
              primary={true}
              fullWidth = {true}
              style={styles.marginStyle}
              labelPosition="before"
              icon={<FileDownload />}
            />
          </Col>
        );
      });
    }
  }
  search = (e) => {
    e.preventDefault();
    this.setState({loadingstatus:'loading'});
    let update = [...currentThis.state.srn];
    let req_obj = {};
    req_obj['serviceRequest'] = update[0];
    //console.log(JSON.stringify(req_obj));

    var dat = new Date().toLocaleDateString();
    var time = new Date().toLocaleTimeString();
    var date = dat.split("/").join("-");
    req_obj.serviceRequest['updatedDatetime'] = date+' '+time;

    //change status, position, ward, location in attribValues
    for (var i = 0, len = req_obj.serviceRequest.attribValues.length; i < len; i++) {
      if(req_obj.serviceRequest.attribValues[i]['key'] == 'status'){
        req_obj.serviceRequest.attribValues[i]['name'] = currentThis.props.grievanceView.status ? currentThis.props.grievanceView.status : currentThis.state.status;
      }else if(req_obj.serviceRequest.attribValues[i]['key'] == 'positionId'){
          req_obj.serviceRequest.attribValues[i]['name'] = (currentThis.props.grievanceView.positionId == 0 || currentThis.props.grievanceView.positionId == undefined) ? currentThis.state.positionId : currentThis.props.grievanceView.positionId;
      }else if(req_obj.serviceRequest.attribValues[i]['key'] == 'locationId'){
          req_obj.serviceRequest.attribValues[i]['name'] = currentThis.props.grievanceView.locationId ? currentThis.props.grievanceView.locationId : currentThis.state.locationId;
      }else if(req_obj.serviceRequest.attribValues[i]['key'] == 'childLocationId'){
          req_obj.serviceRequest.attribValues[i]['name'] = currentThis.props.grievanceView.childLocationId ? currentThis.props.grievanceView.childLocationId : currentThis.state.childLocationId;
      }
    }

    //change serviceCode in serviceRequests
    req_obj.serviceRequest.serviceCode = currentThis.props.grievanceView.serviceCode ? currentThis.props.grievanceView.serviceCode :  currentThis.state.serviceCode;

    currentThis.chckkey('approvalComments', req_obj);
    if(localStorage.getItem('type') == 'EMPLOYEE'){
      currentThis.chckkey('PRIORITY', req_obj);
      currentThis.chckkey('priorityColor', req_obj);
    }else if(localStorage.getItem('type') == 'CITIZEN'){
        currentThis.chckkey('rating', req_obj);
    }

    if(currentThis.props.files.length > 0){
      for(let i=0; i<currentThis.props.files.length; i++){
        let formData = new FormData();
        formData.append("tenantId", localStorage.getItem('tenantId'));
        formData.append("module", "PGR");
        formData.append("file", currentThis.props.files[i]);
        Api.commonApiPost("/filestore/v1/files",{},formData).then(function(response)
        {
          var obj = {
            key: 'employeeDocs_'+currentThis.props.files[i]['name'],
            name: response.files[0].fileStoreId
          }
          req_obj.serviceRequest.attribValues.push(obj);
          currentThis.updateSeva(req_obj);
        },function(err) {

        });
      }
    }else{
      currentThis.updateSeva(req_obj);
    }

  }
  chckkey = (key, req_obj) =>{
    //chck approval comments exists in attribvalues
    var result = req_obj.serviceRequest.attribValues.filter(function( obj ) {
      return obj.key == key;
    });

    if(result.length > 0){
      for (var i = 0, len = req_obj.serviceRequest.attribValues.length; i < len; i++) {
        if(req_obj.serviceRequest.attribValues[i]['key'] == key){
          req_obj.serviceRequest.attribValues[i]['name'] = currentThis.props.grievanceView[key];
        }
      }
    }else{
      var finobj = {
          key: key,
          name: currentThis.props.grievanceView[key]
      };
      req_obj.serviceRequest.attribValues.push(finobj);
    }
  }
  updateSeva = (req_obj) =>{
    //console.log('Before Submit',JSON.stringify(req_obj));
    Api.commonApiPost("/pgr/seva/v1/_update",{},req_obj).then(function(updateResponse)
    {
      //console.log('After submit',JSON.stringify(updateResponse));
      currentThis.setState({loadingstatus:'hide'});
      {currentThis.handleOpen()}
    },function(err) {

    });
  }
  employeesDocs = () =>{
    if(this.state.srn != undefined){
      return this.state.srn.map((values, index) => {
        return values.attribValues.map((attrib, aindex) =>{
            if(attrib['key'].indexOf('employeeDocs') !== -1){
              let key = (attrib['key'].split(/_(.+)/)[1]).length > 15 ? (attrib['key'].split(/_(.+)/)[1]).substr(0, 12)+'...' : attrib['key'].split(/_(.+)/)[1];
              return (
                <Col xs={6} md={3} key={aindex}>
                  <RaisedButton
                    href={'/filestore/v1/files/id?fileStoreId=' + attrib['name']+'&tenantId='+localStorage.getItem('tenantId')}
                    download
                    label={key}
                    primary={true}
                    fullWidth = {true}
                    style={styles.marginStyle}
                    labelPosition="before"
                    icon={<FileDownload />}
                  />
                </Col>
              )
            }
        })
      });
    }
  }
  render(){
    let
    {
      search,
      getReceivingCenter,
      getLocation,
      renderWorkflow,
      filesUploaded
       } = this;
    let{
      handleChange,
      handleWard,
      handleDesignation,
      handlePosition,
      grievanceView,
      files,
      fieldErrors,
      isFormValid,
      loadServiceDefinition,
      handleUpload
    } = this.props;
    currentThis = this;
    const actions = [
      <FlatButton
        label="Ok"
        primary={true}
        onTouchTap={this.handleClose}
      />
    ];
    return(
      <div>
      <form autoComplete="off" onSubmit={(e) => { search(e) }}>
      <LoadingIndicator status={this.state.loadingstatus}/>
      <Grid style={{width:'100%'}}>
        <Card style={{margin:'15px 0'}}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
             {translate('pgr.lbl.srn')} : {this.state.serviceRequestId}
           < /div>}/>
           <CardText style={{padding:'8px 16px 0'}}>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('core.lbl.add.name')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.firstName}
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.mobilenumber')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.phone ? this.state.phone : 'N/A'}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('core.lbl.email.compulsory')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.email ? this.state.email : 'N/A'}
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.address')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.requesterAddress ? this.state.requesterAddress : 'N/A'}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('core.lbl.enter.aadharcard.number')}
                </Col>
                <Col xs={6} md={3}>
                  N/A
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.description')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.description}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.grievance.type')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.serviceName}
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.department')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.departmentName}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.registered.date')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.requestedDatetime}
                </Col>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.nextescalation.date')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.expectedDatetime}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.filedvia')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.receivingMode}
                </Col>
                <Col xs={6} md={3}>
                  {translate('pgr.lbl.receivingcenter')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.receivingCenterName ? this.state.receivingCenterName :'N/A'}
                </Col>
              </Row>
              <Row style={styles.addBorderBottom}>
                <Col xs={6} md={3}>
                  {translate('core.lbl.location')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.childLocationName + " - " + this.state.locationName}
                </Col>
                <Col xs={6} md={3}>
                  {translate('core.lbl.landmark')}
                </Col>
                <Col xs={6} md={3}>
                  {this.state.address ? this.state.address : 'N/A'}
                </Col>
              </Row>
              <Row style={{padding:'10px'}}>
                <Col xs={6} md={3}>
                  Files Uploaded
                </Col>
                {this.filesUploaded()}
              </Row>
           </CardText>
        </Card>
      </Grid>
      <Grid style={{width:'100%'}}>
        <Card style={{margin:'15px 0'}}>
        <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
         {translate('core.documents')}
        < /div>}/>
        <CardText style={{padding:'8px 16px 0'}}>
          <Row>
            {this.employeesDocs()}
          </Row>
        </CardText>
        </Card>
      </Grid>
      <Grid style={{width:'100%'}}>
        <Card style={{margin:'15px 0'}}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
           {translate('core.lbl.history')}
          < /div>}/>
          <CardText style={{padding:'8px 16px 0'}}>
            <Row style={styles.addBorderBottom}>
              <Col xs={12} md={2}>
                Date
              </Col>
              <Col xs={12} md={2}>
                {translate('pgr.lbl.updatedby')}
              </Col>
              <Col xs={12} md={2}>
                {translate('core.lbl.status')}
              </Col>
              <Col xs={12} md={2}>
                {translate('pgr.lbl.currentowner')}
              </Col>
              <Col xs={12} md={2}>
                {translate('core.lbl.department')}
              </Col>
              <Col xs={12} md={2}>
                {translate('core.lbl.comments')}
              </Col>
            </Row>
            {this.renderWorkflow()}
          </CardText>
        </Card>
      </Grid>
      { (localStorage.getItem('type') == 'EMPLOYEE' && this.state.status !== 'REJECTED' && this.state.status !== 'COMPLETED') ||  (localStorage.getItem('type') == 'CITIZEN' && this.state.status !== 'WITHDRAWN') ?
      <Grid style={{width:'100%'}}>
        <Card style={{margin:'15px 0'}}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >
           {translate('pgr.lbl.actions')}
          < /div>}/>
          <CardText style={{padding:'8px 16px 0'}}>
            <Row>
              <Col xs={12} md={3}>
                <SelectField fullWidth={true} floatingLabelText={translate('pgr.lbl.change.status')+' *'} maxHeight={200} value={grievanceView.status ? grievanceView.status : this.state.status} onChange={(event, key, value) => {
                  handleChange(value, "status", false, "")
                }}>
                  {this.state.nextStatus !== undefined ?
                  this.state.nextStatus.map((status, index) => (
                      <MenuItem value={status.code} key={index} primaryText={status.name} />
                  )) : ''}
                </SelectField>
              </Col>
              { localStorage.getItem('type') == 'EMPLOYEE' ?
              <Col xs={12} md={3}>
                <SelectField fullWidth={true} floatingLabelText={translate('pgr.lbl.change.grievancetype')+' *'} maxHeight={200} value={grievanceView.serviceCode ? grievanceView.serviceCode : this.state.serviceCode} onChange={(event, key, value) => {
                  handleChange(value, "serviceCode", false, "")}}>
                  {this.state.complaintTypes !== undefined ?
                  this.state.complaintTypes.map((ctype, index) => (
                      <MenuItem value={ctype.serviceCode} key={index} primaryText={ctype.serviceName} />
                  )) : ''}
                </SelectField>
              </Col> : "" }
              { localStorage.getItem('type') == 'EMPLOYEE' ?
              <Col xs={12} md={3}>
                <SelectField fullWidth={true} floatingLabelText="Ward *" maxHeight={200} value={grievanceView.locationId ? grievanceView.locationId : this.state.locationId}  onChange={(event, key, value) => {
                  handleWard(value, "locationId", false, "")}}>
                  {this.state.ward !== undefined ?
                  this.state.ward.map((ward, index) => (
                      <MenuItem value={ward.id} key={index} primaryText={ward.name} />
                  )) : ''}
                </SelectField>
              </Col>: ""}
              { localStorage.getItem('type') == 'EMPLOYEE' ?
              <Col xs={12} md={3}>
                <SelectField fullWidth={true} floatingLabelText={translate('core.lbl.location')+' *'} maxHeight={200} value={grievanceView.childLocationId ? grievanceView.childLocationId : this.state.childLocationId}  onChange={(event, key, value) => {
                  handleChange(value, "childLocationId", true, "")}}>
                  {this.state.locality !== undefined ?
                  this.state.locality.map((locality, index) => (
                      <MenuItem value={locality.id} key={index} primaryText={locality.name} />
                  )) : ''}
                </SelectField>
              </Col> : "" }
            </Row>
            { localStorage.getItem('type') == 'EMPLOYEE' ?
            <Row>
              <Col xs={12} md={3}>
                <SelectField fullWidth={true} floatingLabelText="Forward to Department" maxHeight={200} value={grievanceView.departmentId} onChange={(event, key, value) => {
                  handleDesignation(value, "departmentId", false, ""); }
                }>
                  <MenuItem value={0} primaryText="Select Department" />
                  {this.state.department !== undefined ?
                  this.state.department.map((department, index) => (
                      <MenuItem value={department.id} key={index} primaryText={department.name} />
                  )) : ''}
                </SelectField>
              </Col>
              <Col xs={12} md={3}>
                <SelectField fullWidth={true} floatingLabelText="Forward to Designation" maxHeight={200} value={grievanceView.designationId} onChange={(event, key, value) => {
                  handlePosition(grievanceView.departmentId, value, "designationId", true, "") }}>
                  <MenuItem value={0} primaryText="Select Designation" />
                  {this.state.designation !== undefined ?
                  this.state.designation.map((designation, index) => (
                      <MenuItem value={designation.id} key={index} primaryText={designation.name} />
                  )) : ''}
                </SelectField>
              </Col>
              <Col xs={12} md={3}>
                <SelectField fullWidth={true} floatingLabelText="Forward to Position" maxHeight={200} value={grievanceView.positionId} onChange={(event, key, value) => {
                  handleChange(value, "positionId", true, ""); }}>
                  <MenuItem value={0} primaryText="Select Position" />
                  {this.state.position !== undefined ?
                  this.state.position.map((position, index) => (
                      <MenuItem value={position.assignments[0].position} key={index} primaryText={position.userName} />
                  )) : ''}
                </SelectField>
              </Col>
            </Row>

            : "" }
            { localStorage.getItem('type') == 'EMPLOYEE' ?
            <Row>
              {loadServiceDefinition()}
              <Col xs={12} md={3}>
                <SelectField fullWidth={true} floatingLabelText="Priority Color *" maxHeight={200} value={grievanceView.priorityColor} onChange={(event, key, value) => {
                  handleChange(value, "priorityColor", true, ""); }} errorText={fieldErrors.priorityColor ? fieldErrors.priorityColor : ""}>
                  <MenuItem value="#F44336" primaryText="Red" />
                  <MenuItem value="#4CAF50" primaryText="Green" />
                  <MenuItem value="#FFEB3B" primaryText="Yellow" />
                </SelectField>
              </Col>
            </Row> : ''}
            { localStorage.getItem('type') == 'CITIZEN' && (this.state.status == 'COMPLETED' || currentThis.state.status == 'REJECTED') ?
            <Row>
              <Col xs={12} md={3}>
                <h4>Feedback</h4>
                <Rating initialRate={grievanceView.rating} onClick={(rate, event) => { handleChange(rate,"rating", true,"")}}/>
              </Col>
            </Row> : ''}
            <Row>
              <Col xs={12} md={12}>
                <TextField floatingLabelText={translate('core.lbl.comments')+' *'} fullWidth={true} multiLine={true} rows={2} rowsMax={4}value={grievanceView.approvalComments} onChange={(event, newValue) => {
                  handleChange(newValue, "approvalComments", true, "") }} errorText={fieldErrors.approvalComments ? fieldErrors.approvalComments : ""}/>
              </Col>
            </Row>
            { localStorage.getItem('type') == 'EMPLOYEE' ?
            <Row>
              <Col xs={12} md={3}>
                <h4>{translate('core.documents')}</h4>
              </Col>
              <Col xs={12} md={3}>
                <div className="input-group">
                    <input type="file" className="form-control" ref="file" onChange={(e)=>handleUpload(e)}/>
                    <span className="input-group-addon" onClick={() => this.refs.file.value = ''}><i className="glyphicon glyphicon-trash specific"></i></span>
                </div>
              </Col>
            </Row> : ""}
            <Row>
              <div style={{textAlign: 'center'}}>
                <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label="Submit" backgroundColor={"#5a3e1b"} labelColor={white}/>
              </div>
            </Row>
          </CardText>
        </Card>
      </Grid>
      : ''
      }
      <div style={{textAlign: 'center'}}>
        <RaisedButton style={{margin:'15px 5px'}} label="Close"/>
      </div>
      </form>
      <Dialog
        actions={actions}
        modal={true}
        open={this.state.open}
        onRequestClose={this.handleClose}
      >
        {translate('pgr.msg.success.grievanceupdated')}
      </Dialog>
      </div>
    )
  }
}

const mapStateToProps = state => {
  //console.log(state.form.form);
  return ({grievanceView: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ['approvalComments']
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  addMandatory : () => {
    if(localStorage.getItem('type') == 'CITIZEN' && (currentThis.state.status == 'COMPLETED' || currentThis.state.status == 'REJECTED'))
      dispatch({type: "ADD_MANDATORY", property: "rating", value: '', isRequired : true, pattern: ''})
  },
  handleChange: (value, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value, isRequired, pattern});
  },
  handleWard : (value, property, isRequired, pattern) => {
    Api.commonApiPost("/egov-location/boundarys/childLocationsByBoundaryId",{boundaryId : value}).then(function(response)
    {
      currentThis.setState({locality : response.Boundary});
      currentThis.setState({childLocationId : ''});
      dispatch({type: "ADD_MANDATORY", property: "childLocationId", value: '', isRequired : true, pattern: ''});
      dispatch({type: "HANDLE_CHANGE", property: "childLocationId", value:'', isRequired:true, pattern:''});
      dispatch({type: "HANDLE_CHANGE", property, value, isRequired, pattern});
    },function(err) {

    });
  },
  handleDesignation: (value, property, isRequired, pattern) => {
    if(property == 'departmentId' && value == 0)
    {
      currentThis.setState({designation : []});
      currentThis.setState({position : []});
      dispatch({type: "REMOVE_MANDATORY", property: "designationId", value: '', isRequired : false, pattern: ''});
      dispatch({type: "REMOVE_MANDATORY", property: "positionId", value: '', isRequired : false, pattern: ''});
      dispatch({type: "HANDLE_CHANGE", property:'departmentId', value: 0, isRequired, pattern});
      dispatch({type: "HANDLE_CHANGE", property:'designationId', value: 0, isRequired, pattern});
      dispatch({type: "HANDLE_CHANGE", property:'positionId', value: 0, isRequired, pattern});
    }else{
      Api.commonApiPost("/hr-masters/designations/_search").then(function(response)
      {
        currentThis.setState({designation : response.Designation});
        dispatch({type: "ADD_MANDATORY", property: "designationId", value: '', isRequired : true, pattern: ''});
        dispatch({type: "ADD_MANDATORY", property: "positionId", value: '', isRequired : true, pattern: ''});
        dispatch({type: "HANDLE_CHANGE", property, value, isRequired, pattern});
      },function(err) {

      });
    }
  },
  handlePosition: (dep, value, property, isRequired, pattern) => {
    if(property == 'designationId' && value == 0)
    {
      dispatch({type: "HANDLE_CHANGE", property:'positionId', value: 0, isRequired, pattern});
    }else{
      let des = value;
      if(dep && des){
        Api.commonApiPost("/hr-employee/employees/_search",{departmentId:dep, designationId: des}).then(function(response)
        {
          currentThis.setState({position : response.Employee});
          dispatch({type: "HANDLE_CHANGE", property, value, isRequired, pattern});
        },function(err) {

        });
      }else {
        currentThis.setState({position : []});
      }
    }

  },
  loadServiceDefinition: () => {
    if(currentThis.state.SD != undefined && localStorage.getItem('type') == 'EMPLOYEE'){
      dispatch({type: "ADD_MANDATORY", property: "priorityColor", value: '', isRequired : true, pattern: ''})
      let FormFields = currentThis.state.SD.filter(function (el) {
        return (el.code != 'CHECKLIST' && el.code != 'DOCUMENTS') ;
      });
      if(FormFields.length > 0){
        return FormFields.map((item,index) =>
        {
          item.required ? dispatch({type: "ADD_MANDATORY", property: item.code, value: '', isRequired : true, pattern: ''}) : '';
          return (
            <Fields key={index} obj={item} value={currentThis.props.grievanceView[item.code]} handler={currentThis.props.handleChange}/>
          );
        })
      }
    }
  },
  handleUpload: (e) => {
    dispatch({type: 'FILE_UPLOAD', files: e.target.files})
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(grievanceView);
