import React, {Component} from 'react';
import {connect} from 'react-redux';
import {EXIF} from 'exif-js';
import {withRouter} from 'react-router-dom';
import ImagePreview from '../../common/ImagePreview.js';
import SimpleMap from '../../common/GoogleMaps.js';
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
import LoadingIndicator from '../../common/LoadingIndicator';
import Api from '../../../api/api';
import {translate, validate_fileupload, format_lat_long} from '../../common/common';
var axios = require('axios');

const styles = {
  headerStyle : {
    fontSize : 19
  },
  marginStyle:{
    margin: '15px'
  }
};

var _this;

var request = {};

class grievanceCreate extends Component {
  constructor(props) {
       super(props);
       this.state = {
         loadingstatus: 'loading',
         type:'',
         receivingModes : [],
         receivingCenter:[],
         topComplaintTypes: [],
         grievanceCategory: [],
         grievanceType: [],
         boundarySource: [],
         boundarySourceConfig: {
          text: 'name',
          value: 'id',
        },
        open: false,
        openOTP : false,
        toastOpen: false
       }
       this.search=this.search.bind(this);
       this.loadReceivingCenter = this.loadReceivingCenter.bind(this);
       this.loadGrievanceType = this.loadGrievanceType.bind(this);
       this.processCreate = this.processCreate.bind(this);
   }

  handleOpen = () => {
    this.setState({open: true});
  };

  handleOTPOpen = () => {
    this.setState({openOTP: true});
  };

  handleOTPClose = () => {
    this.setState({openOTP: false});
  };

  handleView = () => {
    let {initForm, history} = this.props;
    initForm(localStorage.getItem('type'));
    this.setState({open: false});
    history.push("/pgr/viewGrievance/"+this.state.serviceRequestId);
    window.location.reload();
  };

  validateOTP = () => {
    //otp validate
    if(!this.state.otpValue){
      return false;
    }
    this.handleOTPClose();
    this.setState({loadingstatus:'loading'});
    let mob = this.props.grievanceCreate.phone;
    let tenant =  localStorage.getItem('tenantId') ? localStorage.getItem('tenantId') : 'default';
    var rqst = {
      tenantId: tenant,
      otp: this.state.otpValue,
      identity: mob
    };
    Api.commonApiPost("otp/v1/_validate", {}, {otp: rqst}).then(function(response) {
        //create SRN
        let finobj = {
            key: 'otpReference',
            name:response.otp.UUID
        };
        request['serviceRequest'].attribValues.push(finobj);
        _this.createGrievance(request);
    }, function(err) {
      _this.setState({loadingstatus:'hide'});
      _this.handleError(err.message);
    });
  }

   loadReceivingCenter(value){
     var currentThis = this;
     currentThis.setState({isReceivingCenterReq : false});
     if(value === 'MANUAL'){
       Api.commonApiPost("/pgr-master/receivingcenter/v1/_search").then(function(response)
       {
         currentThis.setState({receivingCenter : response.ReceivingCenterType});
       },function(err) {
         currentThis.handleError(err.message);
       });
       this.props.ADD_MANDATORY('receivingCenter');
     }else{
       this.props.REMOVE_MANDATORY('receivingCenter');
       this.props.REMOVE_MANDATORY('externalCRN');
     }
   }

   loadGrievanceType(value){
     var currentThis = this;
     Api.commonApiPost("/pgr-master/service/v1/_search", {type:'category', categoryId : value}).then(function(response)
     {
       currentThis.setState({grievanceType : response.Service});
     },function(err) {
       currentThis.handleError(err.message);
     });
   }

  componentDidMount()
  {
    this.setState({type:localStorage.getItem('type')});
    let {initForm} = this.props;
    initForm(localStorage.getItem('type'));
    console.log(localStorage.getItem('type'));
    var currentThis = this;

    //Get City details for tenant
    Api.commonApiPost("/tenant/v1/tenant/_search",{code : localStorage.getItem('tenant') || 'default'}).then(function(response)
    {
      currentThis.setState({
        citylat:response.tenant[0].city.latitude,
        citylng:response.tenant[0].city.longitude
      })
    },function(err) {
      currentThis.handleError(err.message);
    });

    //OTP enabled for tenant
    if(localStorage.getItem('type') === null){
      Api.commonApiPost("/pgr-master/OTPConfig/_search").then(function(response)
      {
        currentThis.setState({otpEnabled: response.otpConfig[0].otpEnabledForAnonymousComplaint})
      },function(err) {
        currentThis.handleError(err.message);
      });
    }

    //isMapsEnabled
    Api.commonApiPost("/egov-location/boundarys/isshapefileexist").then(function(response)
    {
      if(response.ShapeFile.fileExist){
        currentThis.setState({isMapsEnabled : response.ShapeFile.fileExist});
      }
    },function(err) {
      currentThis.handleError(err.message);
    });

    //ReceivingMode
    if(localStorage.getItem('type') === 'EMPLOYEE'){
      Api.commonApiPost("/pgr-master/receivingmode/v1/_search").then(function(response)
      {
        currentThis.setState({receivingModes : response.ReceivingModeType});
      },function(err) {
        currentThis.handleError(err.message);
      });
    }

    //Top ComplaintTypes
    Api.commonApiPost("/pgr/services/v1/_search", {type: 'frequency', count: 5}).then(function(response)
    {
      //console.log(JSON.stringify(response))
      var topComplaint = [];
      for (var j = 0; j < response.complaintTypes.length; j++){
        if(response.complaintTypes[j].keywords.indexOf('complaint') > -1)
          topComplaint.push(response.complaintTypes[j]);
      }
      currentThis.setState({topComplaintTypes : topComplaint});
    },function(err) {
      currentThis.handleError(err.message);
    });

    //Grievance Category
    Api.commonApiPost("/pgr-master/serviceGroup/v1/_search").then(function(response)
    {
      currentThis.setState({grievanceCategory : response.ServiceGroups});
    },function(err) {
      currentThis.handleError(err.message);
    });

    this.setState({loadingstatus:'hide'});

  }

  search(e)
  {
      e.preventDefault();
      //console.log('Initial Position:',this.state.citylat, this.state.citylng);
      //console.log('Changed Position:',this.props.grievanceCreate.lat, this.props.grievanceCreate.lng);
      this.setState({loadingstatus:'loading'});
      //validate with API
      if(this.props.grievanceCreate.lat && this.props.grievanceCreate.lng){
        Api.commonApiGet("/egov-location/boundarys",{'boundary.latitude' : this.props.grievanceCreate.lat, 'boundary.longitude' : this.props.grievanceCreate.lng, 'boundary.tenantId' : localStorage.getItem('tenantId') || 'default'}).then(function(response)
        {
          if(response.Boundary.length === 0){
            _this.setState({loadingstatus:'hide'});
            _this.handleError('Location selected is out of the city boundary');
          }else{
            //usual createGrievance
            _this.initialCreateBasedonType();
          }
        },function(err) {
          _this.handleError(err.message);
        });
      }else{
        //usual createGrievance
        this.initialCreateBasedonType();
      }
      //console.log(this.props.grievanceCreate);
  }

  initialCreateBasedonType = () => {
    let type = this.state.type;
    if(type === 'CITIZEN'){
      var userArray = [], userRequest={};
      userArray.push(localStorage.getItem('id'));
      userRequest['id']=userArray;
      userRequest['tenantId']=localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : 'default';
      let userInfo = Api.commonApiPost("/user/v1/_search",{},userRequest).then(function(userResponse)
      {
        var userName = userResponse.user[0].name;
        var userMobile = userResponse.user[0].mobileNumber;
        var userEmail = userResponse.user[0].emailId;
        _this.processCreate(userName,userMobile,userEmail);
      },function(err) {
        _this.setState({loadingstatus:'hide'});
        _this.handleError(err.message);
      });
    }else if(type === 'EMPLOYEE'){
      _this.processCreate();
    }else{
      _this.processCreate();
    }
  }

  processCreate(userName='',userMobile='',userEmail=''){

    request = {};
    var data={};
    var finobj={};
    data['serviceCode']=this.props.grievanceCreate.serviceCode;
    data['description']=this.props.grievanceCreate.description;
    data['addressId']= this.props.grievanceCreate.addressId ? this.props.grievanceCreate.addressId.id : '';
    data['lat']= this.props.grievanceCreate.addressId ? '' : this.props.grievanceCreate.lat;
    data['lng']= this.props.grievanceCreate.addressId ? '' :this.props.grievanceCreate.lng;
    data['address']=this.props.grievanceCreate.address;
    data['serviceRequestId']='';
    data['firstName']=this.props.grievanceCreate.firstName ? this.props.grievanceCreate.firstName : userName;
    data['phone']=this.props.grievanceCreate.phone ? this.props.grievanceCreate.phone : userMobile;
    data['email']=this.props.grievanceCreate.email ? this.props.grievanceCreate.email : userEmail;
    data['status']=true;
    data['serviceName']='';
    data['requestedDatetime']="";
    data['mediaUrl']="";
    data['tenantId']='default';
    data['isAttribValuesPopulated']=true;
    data['attribValues'] = [];

    finobj = {
        key: 'receivingMode',
        name: this.props.grievanceCreate.receivingMode ? this.props.grievanceCreate.receivingMode : 'Website'
    };
    data['attribValues'].push(finobj);

    if(this.props.grievanceCreate.receivingCenter) {
      finobj = {
        key: 'receivingCenter',
        name: this.props.grievanceCreate.receivingCenter ? this.props.grievanceCreate.receivingCenter : ''
      };
      data['attribValues'].push(finobj);
      finobj = {
        key: 'externalCRN',
        name: this.props.grievanceCreate.externalCRN ? this.props.grievanceCreate.externalCRN : ''
      };
      data['attribValues'].push(finobj);
    }

    finobj = {
        key: 'status',
        name: 'REGISTERED'
    };
    data['attribValues'].push(finobj);

    if(this.props.grievanceCreate.requesterAddress) {
      finobj = {
        key: 'requesterAddress',
        name: this.props.grievanceCreate.requesterAddress ? this.props.grievanceCreate.requesterAddress : ''
      };
      data['attribValues'].push(finobj);
    }

    finobj = {
        key: 'keyword',
        name:'Complaint'
    };
    data['attribValues'].push(finobj);

    if(localStorage.getItem('type') === 'CITIZEN'){
      finobj = {
          key: 'citizenUserId',
          name: localStorage.getItem('id')
      };
      data['attribValues'].push(finobj);
    }

    request['serviceRequest'] = data;

    //console.log(JSON.stringify(request));
    var currentThis = this;

    if(localStorage.getItem('type') === null){

      let mob = this.props.grievanceCreate.phone;
      let tenant =  localStorage.getItem('tenantId') ? localStorage.getItem('tenantId') : 'default';
      //send OTP and validate it
      Api.commonApiPost("/pgr/v1/otp/_send",{},{mobileNumber:mob, tenantId:tenant}).then(function(response)
      {
        //validate OTP
        currentThis.setState({loadingstatus:'hide'});
        if(response.isSuccessful){
          {currentThis.handleOTPOpen()}
        }else {
          currentThis.handleError(translate('core.error.opt.generation'));
        }
      },function(err) {
        currentThis.setState({loadingstatus:'hide'});
        currentThis.handleError(err.message);
      });
    }else{
      this.createGrievance(request);
    }

  }

  createGrievance = (request) => {
    var currentThis = this;
    Api.commonApiPost("/pgr/seva/v1/_create",{},request).then(function(createresponse)
    {

      console.log(JSON.stringify(createresponse));

      var srn = createresponse.serviceRequests[0].serviceRequestId;
      currentThis.setState({serviceRequestId:srn});
      var ack = translate('pgr.msg.servicerequest.underprocess')+'. '+translate('pgr.lbl.srn')+' is '+srn+'. '+translate('pgr.msg.future.reference')+'.';
      currentThis.setState({srn:translate('pgr.lbl.srn')+' : '+srn});
      currentThis.setState({acknowledgement:ack});

      if(currentThis.props.files){
        if(currentThis.props.files.length === 0){
          //console.log('create succesfully done. No file uploads');
          currentThis.setState({loadingstatus:'hide'});
          {currentThis.handleOpen()}
        }else{
          //console.log('create succesfully done. still file upload pending');
          for(let i=0;i<currentThis.props.files.length;i++){
            //this.props.files.length[i]
            let formData = new FormData();
            formData.append("tenantId", localStorage.getItem('tenantId'));
            formData.append("module", "PGR");
            formData.append("tag", createresponse.serviceRequests[0].serviceRequestId);
            formData.append("file", currentThis.props.files[i]);
            Api.commonApiPost("/filestore/v1/files",{},formData).then(function(response)
            {
              if(i === (currentThis.props.files.length - 1)){
                //console.log('All files succesfully uploaded');
                currentThis.setState({loadingstatus:'hide'});
                {currentThis.handleOpen()}
              }
            },function(err) {
              currentThis.handleError(err.message);
            });
          }
        }
      }
    },function(err) {
          currentThis.setState({loadingstatus:'hide'});
    });
  }

  handleError = (msg) => {
    let {toggleDailogAndSetText, toggleSnackbarAndSetText}=this.props;
    toggleDailogAndSetText(true, msg);
    //toggleSnackbarAndSetText(true, "Could not able to create complaint. Try again")
  }


  handleTouchTap = () => {
    this.setState({
      toastOpen: true,
    });
  };

  handleRequestClose = () => {
    this.setState({
      toastOpen: false,
    });
  };

  handleRCChange = (e) => {
    var currentThis = this;
    var valid = false;

    this.state.receivingCenter.forEach(function(item, index){
      if(item.id === e.target.value){
        currentThis.setState({isReceivingCenterReq : item.iscrnrequired});
        valid = item.iscrnrequired;
        //currentThis.props.ADD_MANDATORY('externalCRN');
      }else{
        //currentThis.props.REMOVE_MANDATORY('externalCRN');
      }
    });

    if(valid)
      currentThis.props.ADD_MANDATORY('externalCRN');
    else
      currentThis.props.REMOVE_MANDATORY('externalCRN');

  }

  loadCRN = (valid) => {
    if(valid)
      this.props.ADD_MANDATORY('externalCRN');
    else
      this.props.REMOVE_MANDATORY('externalCRN');
  }

  handleUploadValidation = (e, formats) => {
    let validFile = validate_fileupload(e.target.files, formats);
    //console.log('is valid:', validFile);
    if(validFile === true){
      EXIF.getData(e.target.files[0], function() {
        var imagelat = EXIF.getTag(this, "GPSLatitude"),
        imagelongt = EXIF.getTag(this, "GPSLongitude");
        if(imagelat && imagelongt){
          var formatted_lat = format_lat_long(imagelat.toString());
          var formatted_long = format_lat_long(imagelongt.toString());
          if(formatted_lat && formatted_long){
            axios.post('https://maps.googleapis.com/maps/api/geocode/json?latlng='+formatted_lat+','+formatted_long+'&sensor=true')
            .then(function (response) {
              //console.log(response.data.results[0].formatted_address);
              _this.props.handleMap(formatted_lat, formatted_long, "address");
            });
          }
        }
      });
      this.props.handleUpload(e, formats);
    }
    else
      this.handleError(validFile);
  }

  getAddress = (lat, lng) =>{
    axios.post('https://maps.googleapis.com/maps/api/geocode/json?latlng='+lat+','+lng+'&sensor=true')
      .then(function (response) {
        _this.setState({
          customAddress : response.data.results[0].formatted_address
        });
      });
  }

  render() {

    //console.log(this.state.customAddress);
    //console.log(this.props.grievanceCreate.addressId);
    const actions = [
      <FlatButton
        label={translate('pgr.lbl.view')}
        primary={true}
        onTouchTap={this.handleView}
      />,
    ];
    const otpActions = [
      <FlatButton
        label={translate('core.lbl.ok')}
        primary={true}
        onTouchTap={this.validateOTP}
      />,
    ];
   _this = this;
    let {
      grievanceCreate,
      fieldErrors,
      isFormValid,
      isTableShow,
      handleUpload,
      files,
      handleChange,
      loadReceivingCenterDD,
      setCategoryandType,
      handleAutoCompleteKeyUp,
      handleChangeNextOne,
      handleChangeNextTwo,
      buttonText
    } = this.props;
    let {search, processCreate, isMapsEnabled, loadCRN, handleUploadValidation, getAddress} = this;

    return (
      <div className="grievanceCreate">
        <LoadingIndicator status={this.state.loadingstatus}/>
        <form autoComplete="off" onSubmit={(e) => {
          search(e)
        }}>
          {this.state.type === 'EMPLOYEE' || this.state.type === null ?
            <Card style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {translate('core.lbl.contact.information')}< /div>}/>
                <CardText style={{padding:0}}>
                  <Grid>
                    <Row>
                      {this.state.type === 'EMPLOYEE' ?
                        <Col xs={12} md={3}>
                          <SelectField maxHeight={200} fullWidth={true} floatingLabelText={translate('pgr.lbl.receivingmode')+' *'}  value={grievanceCreate.receivingMode?grievanceCreate.receivingMode:""} onChange={(event, index, value) => {
                            var e = {
                              target: {
                                value: value
                              }
                            };
                            this.loadReceivingCenter(e.target.value)
                            handleChange(e, "receivingMode", true, "")}} errorText={fieldErrors.receivingMode ? fieldErrors.receivingMode : ""} >
                            {this.state.receivingModes !== undefined ?
                            this.state.receivingModes.map((receivingmode, index) => (
                                receivingmode.active ? <MenuItem value={receivingmode.code} key={index} primaryText={receivingmode.name} /> : ''
                            )) : ''}
                          </SelectField>
                        </Col> : ''
                      }
                      {grievanceCreate.receivingMode === 'MANUAL' ?
                        loadReceivingCenterDD('receivingCenter')
                         : ''
                      }
                      {this.state.isReceivingCenterReq === true ?
                        //loadCRN(this.state.isReceivingCenterReq)
                        <Col xs={12} md={3}>
                          <TextField floatingLabelText={translate('CRN')+' *'} multiLine={true} errorText={this.props.fieldErrors.externalCRN ? this.props.fieldErrors.externalCRN : ""} value={this.props.grievanceCreate.externalCRN?this.props.grievanceCreate.externalCRN:""} onChange={(e) => this.props.handleChange(e, "externalCRN", true, '')}/>
                        </Col>
                        : ''}
                    </Row>
                    <Row>
                      <Col xs={12} md={3}>
                        <TextField fullWidth={true} floatingLabelText={translate('core.lbl.add.name')+' *'} value={grievanceCreate.firstName?grievanceCreate.firstName:""} errorText={fieldErrors.firstName ? fieldErrors.firstName : ""} onChange={(e) => handleChange(e, "firstName", true, '')}
                        />
                      </Col>
                      <Col xs={12} md={3}>
                        <TextField fullWidth={true} floatingLabelText={translate('core.lbl.mobilenumber')+' *'} errorText={fieldErrors.phone ? fieldErrors.phone : ""} value={grievanceCreate.phone?grievanceCreate.phone:""} onChange={(e) => handleChange(e, "phone", true, /^\d{10}$/g)} />
                      </Col>
                      <Col xs={12} md={3}>
                        <TextField fullWidth={true} floatingLabelText={translate('core.lbl.email.compulsory')} errorText={fieldErrors.email ? fieldErrors.email : ""} value={grievanceCreate.email?grievanceCreate.email:""} onChange={(e) => handleChange(e, "email", false, /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)}  />
                      </Col>
                      <Col xs={12} md={3}>
                        <TextField fullWidth={true} floatingLabelText={translate('core.lbl.address')} multiLine={true} errorText={fieldErrors.requesterAddress ? fieldErrors.requesterAddress : ""} value={grievanceCreate.requesterAddress?grievanceCreate.requesterAddress:""} onChange={(e) => handleChange(e, "requesterAddress", false, '')} />
                      </Col>
                    </Row>
                  </Grid>
                </CardText>
            </Card> : ''
          }
          <Card style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {translate('pgr.lbl.grievence.information')} < /div>}/>
              <CardText style={{padding:0}}>
                <Grid>
                  <Row>
                    <Col xs={12} md={12}>
                      <div>
                        {this.state.topComplaintTypes.map((topComplaint, index) => (
                            <RaisedButton label={topComplaint.serviceName} key={index} style={{margin:'15px 5px'}} onTouchTap={(event) => {
                              setCategoryandType({serviceCode : topComplaint.serviceCode, groupId : topComplaint.groupId})
                            }} />
                        ))}
                      </div>
                    </Col>
                  </Row>
                  {this.state.topComplaintTypes.length > 0 ?
                  <Row>
                    <Col xs={12} md={12} style={{textAlign:'center'}}>
                      <FlatButton
                        primary= {true}
                        label={translate('core.lbl.or')}
                        disabled = {true}
                      />
                    </Col>
                  </Row> : ''}
                  <Row>
                    <Col xs={12} md={3}>
                      <SelectField fullWidth={true} floatingLabelText={translate('pgr.lbl.grievance.category')+' *'} maxHeight={200} value={grievanceCreate.serviceCategory?grievanceCreate.serviceCategory:""} errorText={fieldErrors.serviceCategory ? fieldErrors.serviceCategory : ""} onChange={(event, index, value) => {
                        var e = {
                          target: {
                            value: value
                          }
                        };
                        this.loadGrievanceType(e.target.value),
                        handleChange(e, "serviceCategory", true, "")}}>
                        {this.state.grievanceCategory !== undefined ?
                        this.state.grievanceCategory.map((grievanceCategory, index) => (
                          <MenuItem value={grievanceCategory.id} key={index} primaryText={grievanceCategory.name} />
                        )) : ''}
                      </SelectField>
                    </Col>
                    <Col xs={12} md={3}>
                      <SelectField fullWidth={true} floatingLabelText={translate('pgr.lbl.grievance.type')+' *'} maxHeight={200} value={grievanceCreate.serviceCode?grievanceCreate.serviceCode:""} errorText={fieldErrors.serviceCode ? fieldErrors.serviceCode : ""} onChange={(event, index, value) => {
                        var e = {
                          target: {
                            value: value
                          }
                        };
                        handleChange(e, "serviceCode", true, "")}}>
                        {this.state.grievanceType.map((grievanceType, index) => (
                            <MenuItem value={grievanceType.serviceCode} key={index} primaryText={grievanceType.serviceName} />
                        ))}
                      </SelectField>
                    </Col>
                  </Row>
                </Grid>
              </CardText>
          </Card>
          <Card style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {translate('core.lbl.moredetails')} < /div>}/>
              <CardText style={{padding:0}}>
                <Grid>
                  <Row>
                    <Col xs={12} md={3}>
                      <TextField fullWidth={true} hintText="Min: 10 Characters" floatingLabelText={translate('pgr.lbl.grievancedetails')+' *'} multiLine={true} errorText={fieldErrors.description ? fieldErrors.description : ""} value={grievanceCreate.description?grievanceCreate.description:""} onChange={(e) => handleChange(e, "description", true, /^.{10,500}$/)}/>
                    </Col>
                    <Col xs={12} md={3}>
                      <TextField fullWidth={true} floatingLabelText={translate('core.lbl.landmark')} multiLine={true} errorText={fieldErrors.address ? fieldErrors.address : ""} value={grievanceCreate.address?grievanceCreate.address:""} onChange={(e) => handleChange(e, "address", false, '')}/>
                    </Col>
                    <Col xs={12} md={6}>
                      <AutoComplete
                        hintText="Type your location or select it from maps"
                        ref="autocomplete"
                        floatingLabelText={translate('pgr.lbl.grievance.location')}
                        filter={AutoComplete.noFilter}
                        fullWidth={true}
                        dataSource={this.state.boundarySource}
                        dataSourceConfig={this.state.boundarySourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        onKeyUp={handleAutoCompleteKeyUp}
                        value={grievanceCreate.addressId}
                        onNewRequest={(chosenRequest, index) => {
                          if(index === -1){
                            this.refs['autocomplete'].setState({searchText:''});
                          }else {
                            var e = {
                              target: {
                                value: chosenRequest
                              }
                            };
                            handleChange(e, "addressId", false, "");
                          }
                       }}
                      />
                    </Col>
                  </Row>
                  <Row>
                    <Col xs={12} md={3}>
                      <RaisedButton label={translate('core.lbl.select.photo')} containerElement="label" style={{ marginTop: '20px', marginBottom:'20px'}}>
                          <input type="file" accept="image/*" style={{display:'none'}} multiple onChange={(e)=>handleUploadValidation(e, ['jpg', 'jpeg', 'png'])}/>
                      </RaisedButton>
                    </Col>
                    <ImagePreview files={files}/>
                  </Row>
                  {this.state.isMapsEnabled ?
                  <Row>
                    <Col md={12}>
                      <div style={{width: '100%', height: 400}}>
                        <SimpleMap lat={this.props.grievanceCreate.lat ? this.props.grievanceCreate.lat : this.state.citylat} lng={this.props.grievanceCreate.lng ? this.props.grievanceCreate.lng : this.state.citylng}  markers={[]} handler={(lat, lng)=>{getAddress(lat, lng);this.props.handleMap(lat, lng, "address")}}/>
                      </div>
                    </Col>
                  </Row> : ''}
                </Grid>
              </CardText>
          </Card>
          <div style={{textAlign: 'center'}}>
            <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label="Create" backgroundColor={"#5a3e1b"} labelColor={white}/>
          </div>
        </form>
        <div>
        <Dialog
          title={this.state.srn}
          actions={actions}
          modal={true}
          open={this.state.open}
        >
        {this.state.acknowledgement}
        </Dialog>
        <Dialog
          title={translate('core.lbl.otp')}
          actions={otpActions}
          modal={true}
          open={this.state.openOTP}
        >
          <TextField fullWidth={true} hintText={translate('core.msg.enter.otp')} errorText="" onChange={(event, newString) => this.setState({otpValue : newString})} />
        </Dialog>
        </div>
      </div>
    );
  }
}

  const mapStateToProps = state => {
    //console.log(state.form.isFormValid)
    return ({grievanceCreate: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
  }

const mapDispatchToProps = dispatch => ({
  initForm: (type) => {
    var requiredArray = [];
    if(type == 'CITIZEN'){
      requiredArray = ["serviceCategory","serviceCode","description"]
    }else if(type == 'EMPLOYEE'){
      requiredArray = ["receivingMode","firstName","phone","serviceCategory","serviceCode","description"]
    }else{
      requiredArray = ["firstName","phone","serviceCategory","serviceCode","description"]
    }

    var patternarray = ["phone","email","description"];

    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: requiredArray
        },
        pattern: {
          current: [],
          required: patternarray
        }
      }
    });
  },
  ADD_MANDATORY : (property) => {
     dispatch({type: "ADD_MANDATORY", property, value: '', isRequired : false, pattern: ''});
     dispatch({type: "HANDLE_CHANGE", property, value:'', isRequired:false, pattern:''});
  },
  REMOVE_MANDATORY : (property) => {
     dispatch({type: "REMOVE_MANDATORY", property, value: '', isRequired : false, pattern: ''});
     dispatch({type: "HANDLE_CHANGE", property, value:'', isRequired:false, pattern:''});
  },
  loadReceivingCenterDD: (name) => {
    dispatch({type: "ADD_MANDATORY", property: name, value: '', isRequired : true, pattern: ''});
    return (
      <Col xs={12} md={3}>
        <SelectField maxHeight={200} floatingLabelText={translate('pgr.lbl.receivingcenter')+' *'} value={_this.props.grievanceCreate.receivingCenter?  _this.props.grievanceCreate.receivingCenter:""} onChange={(event, index, value) => {
          var e = {
            target: {
              value: value
            }
          };
          dispatch({type: "HANDLE_CHANGE", property: name, value: e.target.value, isRequired : true, pattern: ''});
          _this.handleRCChange(e)
          }} errorText={_this.props.fieldErrors.receivingCenter ? _this.props.fieldErrors.receivingCenter : ""} >
          {_this.state.receivingCenter.map((receivingcenter, index) => (
              receivingcenter.active ? <MenuItem value={receivingcenter.id} key={index} primaryText={receivingcenter.name} /> : ''
          ))}
        </SelectField>
      </Col>
    );
  },
  handleAutoCompleteKeyUp : (e) => {
    var currentThis = _this;
    dispatch({type: "HANDLE_CHANGE", property: 'addressId', value: '', isRequired : false, pattern: ''});
    if(e.target.value){
      Api.commonApiGet("/egov-location/boundarys/getLocationByLocationName", {locationName : e.target.value}).then(function(response)
      {
        currentThis.setState({boundarySource : response});
      },function(err) {
        currentThis.handleError(err.message);
      });
    }
  },
  setCategoryandType : (e) => {

    var group = e.groupId;
    var sCode = e.serviceCode;

    var e = {
      target: {
        value: e.groupId
      }
    };

    dispatch({type: "HANDLE_CHANGE", property: 'serviceCategory', value: e.target.value, isRequired : true, pattern: ''});

    var currentThis = _this;
    Api.commonApiPost("/pgr-master/service/v1/_search", {type:'category', categoryId : group}).then(function(response)
    {
      currentThis.setState({grievanceType : response.Service});
      e = {
        target: {
          value: sCode
        }
      };
      dispatch({type: "HANDLE_CHANGE", property: 'serviceCode', value: e.target.value, isRequired : true, pattern: ''});
    },function(err) {
      currentThis.handleError(err.message);
    });

  },
  handleMap: (lat, lng, field) => {
    dispatch({type: "HANDLE_CHANGE", property:'lat', value: lat, isRequired : false, pattern: ''});
    dispatch({type: "HANDLE_CHANGE", property:'lng', value: lng, isRequired : false, pattern: ''});
    dispatch({type: "HANDLE_CHANGE", property: 'addressId', value: '', isRequired : false, pattern: ''});
    _this.refs['autocomplete'].setState({searchText:''});
  },
  handleUpload: (e, formats) => {
    dispatch({type: 'FILE_UPLOAD', files: e.target.files})
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
    if(property === 'addressId'){
      dispatch({type: "HANDLE_CHANGE", property:'lat', value: '', isRequired : false, pattern: ''});
      dispatch({type: "HANDLE_CHANGE", property:'lng', value: '', isRequired : false, pattern: ''});
    }
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState,toastMsg});
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(grievanceCreate);
