import React, {Component} from 'react';
import {connect} from 'react-redux';
import {withRouter} from 'react-router-dom';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import Snackbar from 'material-ui/Snackbar';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import Fields from '../../common/Fields';
import Api from '../../../api/api';
import {translate, validate_fileupload} from '../../common/common';
import FormSection from './FormSection';

const STATUS_NEW = "DSNEW";
const FILES_MODULE_TAG = "citizenservices";

class NewServiceRequest extends Component{

    constructor(){
      super();
      this.prepareAndSubmitRequest = this.prepareAndSubmitRequest.bind(this);
      this.state={
        ack:"",
        openDialog:false
      }

    }

    handleOpen = () => {
      this.setState({openDialog: true});
    };

    handleRefresh = () => {
      this.setState({openDialog: false});
      window.location.reload()
    };

    isGroupExists = (groupCode, groupsArray) => {
      return groupsArray.find((group) => group.code === groupCode);
    };

    fieldGrouping = (response) => {

      let userType=localStorage.getItem('type');

      /*response.attributes=[{
        variable:true,
        code:"firstName",
        dataType:"string",
        description:'core.lbl.add.name',
        required:true,
        groupCode:null,
        roles:[],
        actions:[],
        constraints:null,
        attribValues:[]
      },
      {
        variable:true,
        code:"phone",
        dataType:"Integer",
        description:'core.lbl.mobilenumber',
        required:true,
        groupCode:null,
        roles:[],
        actions:[],
        constraints:null,
        attribValues:[]
      },
      {
        variable:true,
        code:"email",
        dataType:"string",
        description:'core.lbl.email.compulsory',
        required:false,
        groupCode:null,
        roles:[],
        actions:[],
        constraints:null,
        attribValues:[]
      }, ...response.attributes];*/

      //Filter form fields based on current role and action
      /*let formFields = response.attributes.filter((field) => (field.roles.indexOf(userType) > -1
                      && field.actions.indexOf(STATUS_NEW) > -1) ||
                      (field.roles.length == 0 && field.actions.length ==0) ||
                      (field.roles.length == 0 > -1 && field.actions.indexOf(STATUS_NEW) > -1) ||
                      (field.roles.indexOf(userType) > -1 && field.actions.length ==0));*/

      //TODO Remove number condition on tomorrow (10/08/2017)
      let formFields = response.attributes.filter((field) => field.variable
            && field.code != "CHECKLIST" && field.code !="DOCUMENTS" && field.dataType!="number");

      let formSections = [{fields : formFields}];
      let requiredFields = formFields.filter((field)=> field.required);

      this.props.initForm(requiredFields);

      this.setState({
          pageTitle:response.serviceName,
          formSections
      });

      //Basic fields with out any groups
      /*let commonFields = formFields.filter((field) => !field.groupCode ||
                         !this.isGroupExists(field.groupCode, response.groups));

      let formSections = [{fields : commonFields}];

      let requiredFields = [commonFields.map((field)=> field.code)];

      this.props.initForm(requiredFields);

      response.groups.map((group)=>{
         let fields = formFields.filter((field) => field.groupCode == group.code);

         let constraintObj = group.constraints.find((constraint) => constraint.action == STATUS_NEW
                       && constraint.role == userType);

         let constraint = "";

        if(constraintObj){
          constraint = constraintObj.constraint;
        }

        if(fields.length > 0){
            formSections.push({
              ...group,
              constraint,
              fields
            });
        }
      });*/


    }

    renderFormSection = () =>{
         if(this.state.formSections && this.state.formSections.length > 0){
           return this.state.formSections.map((section, index) =>{
             return <FormSection key={index} fields={section.fields} groupName={section.name}
                values={this.props.form} constraint={section.constraint}
                errors={this.props.fieldErrors}
                addFile={this.props.addFile}
                removeFile={this.props.removeFile}
                files={this.props.files}
                dialogOpener={this.props.toggleDailogAndSetText}
                handler={this.props.handleChange}></FormSection>
           });
         }
         else
           return [];
    };

    componentWillMount(){

      //console.log('service code', this.props.match.params.serviceCode);
      /*let response=JSON.parse(`{ "responseInfo": null, "tenantId": "default", "serviceCode": "NOC", "attributes": [ { "variable": true, "code": "NOCNAME", "dataType": "string", "required": true, "dataTypeDescription": null, "description": "core.noc.name", "url": null, "groupCode": "group1", "roles": [], "actions": [], "constraints": null, "attribValues": [] }, { "variable": true, "code": "NOCAGE", "dataType": "double", "required": true, "dataTypeDescription": null, "description": "core.noc.age", "url": null, "groupCode": "group1", "roles": [], "actions": [], "constraints": null, "attribValues": [] }, { "variable": true, "code": "NOCFEES", "dataType": "integer", "required": false, "dataTypeDescription": null, "description": "core.noc.fees", "url": null, "groupCode": null, "roles": [], "actions": [], "constraints": null, "attribValues": [] }, { "variable": true, "code": "NOCDATE", "dataType": "date", "required": false, "dataTypeDescription": null, "description": "core.noc.date", "url": null, "groupCode": null, "roles": [], "actions": [], "constraints": null, "attribValues": [] }, { "variable": true, "code": "NOCENDDATE", "dataType": "datetime", "required": true, "dataTypeDescription": null, "description": "core.noc.enddate", "url": null, "groupCode": null, "roles": [], "actions": [], "constraints": null, "attribValues": [] }, { "variable": true, "code": "NOCSTATUS", "dataType": "singlevaluelist", "required": true, "dataTypeDescription": null, "description": "core.noc.status", "url": null, "groupCode": null, "roles": [], "actions": [], "constraints": null, "attribValues": [ { "key": "NOCSTATUSAPPROVED", "name": "core.noc.status.approved", "isActive": true }, { "key": "NOCSTATUSREJECTED", "name": "core.noc.status.rejected", "isActive": true } ] }, { "variable": true, "code": "NOCSLIMIT", "dataType": "multivaluelist", "required": false, "dataTypeDescription": null, "description": "core.noc.limit", "url": null, "groupCode": null, "roles": [], "actions": [], "constraints": null, "attribValues": [ { "key": "NOCLIMITONE", "name": "core.noc.limit.one", "isActive": true }, { "key": "NOCLIMITTWO", "name": "core.noc.limit.two", "isActive": true }, { "key": "NOCLIMITTHREE", "name": "core.noc.limit.three", "isActive": true }, { "key": "NOCLIMITFOUR", "name": "core.noc.limit.four", "isActive": false } ] }, { "variable": true, "code": "NOCFILE", "dataType": "file", "required": true, "dataTypeDescription": null, "description": "core.noc.file", "url": null, "groupCode": null, "roles": [], "actions": [], "constraints": null, "attribValues": [] }, { "variable": true, "code": "NOCMULTIFILE", "dataType": "multifile", "required": false, "dataTypeDescription": null, "description": "core.noc.multifile", "url": null, "groupCode": null, "roles": [], "actions": [], "constraints": null, "attribValues": [] } ] }`);
      /*response['serviceName']="NOC Certificate";
      this.fieldGrouping(response);*/

      this.setState({pageTitle:""});
      this.props.setLoadingStatus('loading');

      var _this=this;
      Api.commonApiPost("/pgr-master/service/v2/_search",{serviceCode : this.props.match.params.serviceCode, keywords:"deliverable"}).then(function(response)
      {
        _this.props.setLoadingStatus('hide');
        _this.fieldGrouping(response[0]);
      },function(err) {
        _this.props.setLoadingStatus('hide');
      });

    }

    getAttribValuesFromFields = (form)=>{
      let attribValues=[];
      attribValues.push({key:"systemStatus", name:STATUS_NEW});
      Object.keys(this.props.form).map((key)=>{
        var name=this.props.form[key];
        if(name instanceof Array){
           name.map((value)=>{
             attribValues.push({key, name:value});
           });
        }
        else
          attribValues.push({key, name});
      });
      return attribValues;
    }

    prepareAndSubmitRequest = () =>{

        let userType=localStorage.getItem('type');
        let serviceRequest={};
        this.props.setLoadingStatus('loading');
        let _this=this;
        serviceRequest['serviceCode'] = this.props.match.params.serviceCode;
        serviceRequest["attribValues"]=this.getAttribValuesFromFields(this.props.form);

        if(userType === 'CITIZEN'){
          var tenantId = localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : 'default';
          var userRequest={};
          userRequest['id']=[localStorage.getItem('id')];
          userRequest['tenantId']=tenantId;
          serviceRequest['tenantId'] = tenantId;

          //for getting citizen profile details to get name, mobile, email
          Api.commonApiPost("/user/v1/_search",{},userRequest).then(function(userResponse){

            if(!userResponse)
               return;

            let user=userResponse.user[0];
            serviceRequest['firstName'] = user.name;
            serviceRequest['phone'] = user.mobileNumber ;
            serviceRequest['email'] = user.emailId || "";

            if(_this.props.files && _this.props.files.length > 0){
               _this.uploadFilesAndRaiseRequest(serviceRequest, _this.props.files, tenantId);
            }
            else{
              console.log('files is not there');
              _this.raiseRequest(serviceRequest);
            }
          }, function(err) {
            _this.props.setLoadingStatus('hide');

          });
        }
    }

    uploadFilesAndRaiseRequest=(serviceRequest, files, tenantId)=>{
      let fileAttribValues=[]; //store filestoreId by code
      var _this=this;
      let formData = new FormData();
      formData.append("tenantId", tenantId);
      formData.append("module", FILES_MODULE_TAG);
      files.map((field)=>{
          var fileAttribValue={key:field.code, name:""};
          field.files.map((file)=>{
            fileAttribValues.push(fileAttribValue);
            formData.append("file", file);
          });
      });

      Api.commonApiPost("/filestore/v1/files",{},formData).then(function(fileResponse){
        fileResponse.files.map((file, index)=>{
          fileAttribValues[index]={...fileAttribValues[index], name:file.fileStoreId};
        });
        serviceRequest["attribValues"]=[...serviceRequest.attribValues, ...fileAttribValues];
        _this.raiseRequest(serviceRequest);
      }, function(err){
        _this.props.setLoadingStatus('hide');
      });
    }

    raiseRequest=(serviceRequest)=>{
      var _this=this;
      console.log('raising request...', serviceRequest);

      Api.commonApiPost("/pgr/seva/v1/_create",{},{serviceRequest}).then(function(response){
        _this.props.setLoadingStatus("hide");
        console.log('request submited succesfully', response);
        var srn = response.serviceRequests[0].serviceRequestId;
        var ack = `Your application is received and is under process. ${translate('pgr.lbl.srn')} is ${srn}. ${translate('pgr.msg.future.reference')}.`;
        _this.setState({ack, openDialog:true});
      }, function(err) {
        _this.props.setLoadingStatus('hide');
        _this.props.toggleDailogAndSetText(true, "Oops something went wrong, please try again!");
      });

    }

    render(){

       const formSections=this.renderFormSection();

       const actions = [
          <FlatButton
            label={translate('core.lbl.ok')}
            primary={true}
            onTouchTap={this.handleRefresh}
          />
        ];

       return(
         <Grid fluid={true}>
           <h1 className="application-title">{this.state.pageTitle}</h1>
           { formSections }
           <br/>
           <Row>
             <Col xs={12} className="text-center">
               <RaisedButton label="Submit" fullWidth={false} onClick={this.prepareAndSubmitRequest} primary={true} disabled={!this.props.isFormValid} />
             </Col>
           </Row>
           <Dialog
             actions={actions}
             modal={false}
             open={this.state.openDialog}
             onRequestClose={this.handleRefresh}>
             {this.state.ack}
           </Dialog>
         </Grid>
       )
    }
}

export default NewServiceRequest;
