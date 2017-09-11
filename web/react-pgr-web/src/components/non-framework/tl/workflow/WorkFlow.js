import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import styles from '../../../../styles/material-ui';
import {translate} from '../../../common/common';
import Api from '../../../../api/api';

var self;

class WorkFlow extends Component {
  constructor(props) {
    super(props);
    this.state={
      workFlowDepartment : [],
      workFlowDesignation : [],
      workFlowPosition : []
    };
  }
  componentDidMount(){
    if(this.props.viewLicense.applications){
      // console.log('Did Mount',this.props.viewLicense.departmentId, this.props.viewLicense.designationId, this.props.viewLicense.positionId );
      this.initCall(this.props.viewLicense);
    }
  }
  componentWillReceiveProps(nextProps){
    if((this.props.fieldErrors !== nextProps.fieldErrors) && nextProps.viewLicense.applications){
      // console.log('Will Receive Props',nextProps.viewLicense);
      this.initCall(nextProps.viewLicense);
    }
  }
  initCall = (obj) => {
    this.setState({
      obj : obj,
      departmentId : obj.departmentId,
      designationId : obj.designationId,
      positionId : obj.positionId,
      stateId : obj.applications[0].state_id,
      approvalComments : obj.approvalComments
    });
    if(!obj.departmentId){
      Api.commonApiPost( 'egov-common-masters/departments/_search').then((response)=>{
        self.setState({workFlowDepartment: response.Department});
        self.worKFlowActions();
      },function(err) {
        self.props.handleError(err.message);
      });
    }
  }
  worKFlowActions = () => {
    if(this.state.stateId){
      Api.commonApiPost('egov-common-workflows/process/_search', {id:self.state.stateId},{}, false, true).then((response)=>{
        self.setState({process : response.processInstance});
      },function(err) {
        self.props.handleError(err.message);
      });
    }
  }
  handleDesignation = (departmentId, property, isRequired, pattern) => {
    // Load designation based on department
    this.setState({
      workFlowDesignation : [],
      workFlowPosition : []
    });
    this.props.handleChange('', 'designationId', isRequired, pattern);
    this.props.handleChange('', 'positionId', isRequired, pattern);

    if(!departmentId){
      this.props.handleChange('', 'departmentId', isRequired, pattern);
      return;
    }

    let departmentObj = this.state.workFlowDepartment.find(x => x.id === departmentId)
     Api.commonApiPost( 'egov-common-workflows/designations/_search',{businessKey:'New Trade License',departmentRule:'',currentStatus:self.state.process.status,amountRule:'',additionalRule:'',pendingAction:'',approvalDepartmentName:departmentObj.name,designation:''}, {},false, false).then((res)=>{
      for(var i=0; i<res.length;i++){
        Api.commonApiPost('hr-masters/designations/_search', {name:res[i].name}).then((response)=>{
          // response.Designation.unshift({id:-1, name:'None'});
          self.setState({
              ...self.state,
              workFlowDesignation: [
                ...self.state.workFlowDesignation,
                ...response.Designation
              ]
          });
        },function(err) {
          self.props.handleError(err.message);
        });
      }
      self.props.handleChange(departmentId, property, isRequired, pattern);
     },function(err) {
       self.props.handleError(err.message);
     });
  }
  handlePosition = (designationId, property, isRequired, pattern) => {
    this.setState({
      workFlowPosition : []
    });
    self.props.handleChange('', 'positionId', isRequired, pattern);
    // Load position based on designation and department

    if(!designationId)
      return;

    Api.commonApiPost( '/hr-employee/employees/_search', {departmentId:self.state.departmentId, designationId:designationId}).then((response)=>{
        self.setState({workFlowPosition: response.Employee});
        self.props.handleChange(designationId, property, isRequired, pattern);
    },function(err) {
      self.props.handleError(err.message);
    });
  }
  handleChange = (value, property, isRequired, pattern) => {
    //Check position or approval comments
    self.props.handleChange(value, property, isRequired, pattern);
  }
  renderActions = () => {
    var actionValues = this.state.process ? this.state.process.attributes.validActions.values : '';
    if(actionValues && actionValues.length > 0){
      return this.state.process.attributes.validActions.values.map((item, index)=>{
        return(
          <RaisedButton key={index} style={{margin:'15px 5px'}} label={item.name} primary={true} onClick={(e)=>{this.props.updateWorkFlow(item, this.state)}}/>
        )
      })
    }
  }
  render(){
    self = this;
    // console.log(this.state.process ? this.state.process.attributes.nextAction.code : 'process not there');
    return(
      <div>
        {this.state.process && this.state.process.attributes.nextAction.code !== 'END' ?
          <Row>
            <Col xs={12} sm={6} md={4} lg={3}>
              <SelectField fullWidth={true} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('Approver Department')} maxHeight={200}
                dropDownMenuProps={{targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                value = {this.state.departmentId || '' }
                onChange={(event, key, value) => { this.handleDesignation(value, "departmentId", true, "") }}
                >
                  <MenuItem value="" primaryText="Select" />
                  {this.state.workFlowDepartment !== undefined ?
                  this.state.workFlowDepartment.map((department, index) => (
                      <MenuItem value={department.id} key={index} primaryText={department.name} />
                  )) : ''}
              </SelectField>
            </Col>
            <Col xs={12} sm={6} md={4} lg={3}>
              <SelectField fullWidth={true} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('Approver Designation')} maxHeight={200}
                dropDownMenuProps={{targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                value = {this.state.designationId || '' }
                onChange={(event, key, value) => { this.handlePosition(value, "designationId", true, "") }}
                >
                  <MenuItem value="" primaryText="Select" />
                  {this.state.workFlowDesignation !== undefined ?
                  this.state.workFlowDesignation.map((designation, index) => (
                      <MenuItem value={designation.id} key={index} primaryText={designation.name} />
                  )) : ''}
              </SelectField>
            </Col>
            <Col xs={12} sm={6} md={4} lg={3}>
              <SelectField fullWidth={true} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('Approver')} maxHeight={200}
                dropDownMenuProps={{targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                value = {this.state.positionId || '' }
                onChange={(event, key, value) => { this.handleChange(value, "positionId", true, "") }}>
                  <MenuItem value="" primaryText="Select" />
                  {this.state.workFlowPosition !== undefined ?
                  this.state.workFlowPosition.map((position, index) => (
                      <MenuItem value={position.id} key={index} primaryText={position.name} />
                  )) : ''}
              </SelectField>
            </Col>
          </Row>
        : ''}
        <Row>
          <Col xs={12} sm={12} md={12} lg={12}>
            <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true} floatingLabelText={translate('core.lbl.comments')} fullWidth={true} multiLine={true} rows={2} rowsMax={4} maxLength="500"
            value = {this.state.approvalComments || '' }
            onChange={(event, newValue) => { this.handleChange(newValue, "approvalComments", false, /^.[^]{0,500}$/) }} />
          </Col>
          <Col xs={12} sm={12} md={12} lg={12}>
            <div className="text-center">
              {this.renderActions()}
            </div>
          </Col>
      </Row>
      </div>
    )
  }
}

export default WorkFlow;
