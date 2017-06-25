import React, {Component} from 'react';
import {connect} from 'react-redux';
import {withRouter} from 'react-router-dom';
import ImagePreview from '../common/ImagePreview.js';
import SimpleMap from '../common/GoogleMaps.js';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import LoadingIndicator from '../common/LoadingIndicator';
import Api from '../../api/api';

var flag = 0;
const styles = {
  headerStyle : {
    color: 'rgb(90, 62, 27)',
    fontSize : 19
  },
  marginStyle:{
    margin: '15px'
  },
  paddingStyle:{
    padding: '15px'
  },
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
  }
};

var _this;

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
        open: false
       }
       this.search=this.search.bind(this);
       this.loadReceivingCenter = this.loadReceivingCenter.bind(this);
       this.loadGrievanceType = this.loadGrievanceType.bind(this);
       this.createGrievance = this.createGrievance.bind(this);
   }

   handleOpen = () => {
    this.setState({open: true});
  };

  handleClose = () => {
    this.setState({open: false});
    let {initForm, history} = this.props;
    initForm(localStorage.getItem('type'));
  };

  handleView = () => {
    let {initForm, history} = this.props;
    initForm(localStorage.getItem('type'));
    this.setState({open: false});
    history.push("/viewGrievance/"+this.state.serviceRequestId);
  };

   loadReceivingCenter(value){
     if(value == 'MANUAL'){
       var currentThis = this;
       Api.commonApiPost("/pgr-master/receivingcenter/_search").then(function(response)
       {
         currentThis.setState({receivingCenter : response.ReceivingCenterType});
       },function(err) {

       });
     }else{
       //currentThis.setState({receivingCenter : []});
     }
   }

   loadGrievanceType(value){
     var currentThis = this;
     Api.commonApiPost("/pgr/services/_search", {type:'category', categoryId : value}).then(function(response)
     {
       currentThis.setState({grievanceType : response.complaintTypes});
     },function(err) {

     });
   }

  componentDidMount()
  {
    this.setState({type:localStorage.getItem('type')});
    let {initForm} = this.props;
    initForm(localStorage.getItem('type'));

    //let {toggleDailogAndSetText}=this.props;

    var currentThis = this;

    //ReceivingMode
    if(localStorage.getItem('type') == 'EMPLOYEE'){
      Api.commonApiPost("/pgr-master/receivingmode/_search").then(function(response)
      {
        currentThis.setState({receivingModes : response.ReceivingModeType});
      },function(err) {

      });
    }

    //Top ComplaintTypes
    Api.commonApiPost("/pgr/services/_search", {type: 'frequency', count: 5}).then(function(response)
    {
      //console.log(JSON.stringify(response))
      var topComplaint = [];
      for (var j = 0; j < response.complaintTypes.length; j++){
        if(response.complaintTypes[j].keywords.indexOf('complaint') > -1)
          topComplaint.push(response.complaintTypes[j]);
      }
      currentThis.setState({topComplaintTypes : topComplaint});
    },function(err) {

    });

    //Grievance Category
    Api.commonApiPost("/pgr-master/serviceGroup/_search").then(function(response)
    {
      currentThis.setState({grievanceCategory : response.ServiceGroups});
    },function(err) {

    });

    this.setState({loadingstatus:'hide'});

  }

  search(e)
  {
      let type = this.state.type;
      if(type == 'CITIZEN'){
        var userArray = [], userRequest={};
        userArray.push(localStorage.getItem('id'));
        userRequest['id']=userArray;
        userRequest['tenantId']=localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : 'default';
        let userInfo = Api.commonApiPost("/user/_search",{},userRequest).then(function(userResponse)
        {
          var userName = userResponse.user[0].name;
  				var userMobile = userResponse.user[0].mobileNumber;
  				var userEmail = userResponse.user[0].emailId;
          _this.createGrievance(userName,userMobile,userEmail);
        },function(err) {

        });
      }else if(type == 'EMPLOYEE'){
        _this.createGrievance();
      }else{
        _this.createGrievance();
      }
      e.preventDefault();
      let {showTable,changeButtonText}=this.props;
      //console.log(this.props.grievanceCreate);

  }

  createGrievance(userName='',userMobile='',userEmail=''){

    this.setState({loadingstatus:'loading'});

    var data={};
    data['serviceCode']=this.props.grievanceCreate.serviceCode;
    data['description']=this.props.grievanceCreate.description;
    data['addressId']= this.props.grievanceCreate.addressId == 0 ? '' : this.props.grievanceCreate.addressId.id;
    data['lat']=this.props.grievanceCreate.lat;
    data['lng']=this.props.grievanceCreate.lng;
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

    var finobj = {};
    finobj = {
        key: 'receivingMode',
        name: this.props.grievanceCreate.receivingMode ? this.props.grievanceCreate.receivingMode : 'Website'
    };
    data['attribValues'].push(finobj);
    finobj = {
        key: 'receivingCenter',
        name: this.props.grievanceCreate.receivingCenter ? this.props.grievanceCreate.receivingCenter : ''
    };
    data['attribValues'].push(finobj);
    finobj = {
        key: 'status',
        name: 'REGISTERED'
    };
    data['attribValues'].push(finobj);
    finobj = {
        key: 'requesterAddress',
        name: this.props.grievanceCreate.requesterAddress ? this.props.grievanceCreate.requesterAddress : ''
    };
    data['attribValues'].push(finobj);
    finobj = {};
    finobj = {
        key: 'keyword',
        name:'Complaint'
    };
    data['attribValues'].push(finobj);

    var request = {};
    request['serviceRequest'] = data;

    var currentThis = this;

    //console.log(JSON.stringify(request));

    Api.commonApiPost("/pgr/seva/_create",{},request).then(function(createresponse)
    {

      var srn = createresponse.serviceRequests[0].serviceRequestId;
      currentThis.setState({serviceRequestId:srn});
      var ack = 'Service Request is received and is under process. SRN (Service Request No.) is '+srn+'. Please use this for all your future references.'
      currentThis.setState({srn:'SRN (Service Request No.): '+srn});
      currentThis.setState({acknowledgement:ack});

      if(!currentThis.props.files){
        currentThis.setState({loadingstatus:'hide'});
        {currentThis.handleOpen()}
      }
      console.log('create succesfully done');
      if(currentThis.props.files){
        for(let i=0;i<currentThis.props.files.length;i++){
          //this.props.files.length[i]
          let formData = new FormData();
          formData.append("tenantId", localStorage.getItem('tenantId'));
          formData.append("module", "PGR");
          formData.append("tag", createresponse.serviceRequests[0].serviceRequestId);
          formData.append("file", currentThis.props.files[i]);
          Api.commonApiPost("/filestore/v1/files",{},formData).then(function(response)
          {

          },function(err) {

          });
        }
        console.log('file succesfully uploaded');
        currentThis.setState({loadingstatus:'hide'});
        {currentThis.handleOpen()}
      }
    },function(err) {

    });
  }

  render() {
    const actions = [
      <FlatButton
        label="Cancel"
        primary={true}
        onTouchTap={this.handleClose}
      />,
      <FlatButton
        label="Proceed to view"
        primary={true}
        onTouchTap={this.handleView}
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
      handleMap,
      setCategoryandType,
      handleAutoCompleteKeyUp,
      handleChangeNextOne,
      handleChangeNextTwo,
      buttonText
    } = this.props;
    let {search, createGrievance} = this;
    return (
      <div className="grievanceCreate">
        <LoadingIndicator status={this.state.loadingstatus}/>
        <form autoComplete="off" onSubmit={(e) => {
          search(e)
        }}>
          {this.state.type == 'EMPLOYEE' || this.state.type == null ?
            <Card style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Contact Information < /div>}/>
                <CardText style={{padding:0}}>
                  <Grid>
                    <Row>
                      {this.state.type == 'EMPLOYEE' ?
                        <Col xs={12} md={3}>
                          <SelectField maxHeight={200} fullWidth={true} floatingLabelText="Receiving Mode *"  value={grievanceCreate.receivingMode?grievanceCreate.receivingMode:""} onChange={(event, index, value) => {
                            var e = {
                              target: {
                                value: value
                              }
                            };
                            this.loadReceivingCenter(e.target.value)
                            handleChange(e, "receivingMode", true, "")}} errorText={fieldErrors.receivingMode ? fieldErrors.receivingMode : ""} >
                            {this.state.receivingModes !== undefined ?
                            this.state.receivingModes.map((receivingmode, index) => (
                                <MenuItem value={receivingmode.code} key={index} primaryText={receivingmode.name} />
                            )) : ''}
                          </SelectField>
                        </Col> : ''
                      }
                      {grievanceCreate.receivingMode == 'MANUAL' ?
                        loadReceivingCenterDD('receivingCenter')
                         : ''
                      }
                      <Col xs={12} md={3}>
                        <TextField fullWidth={true} floatingLabelText="Name *" value={grievanceCreate.firstName?grievanceCreate.firstName:""} errorText={fieldErrors.firstName ? fieldErrors.firstName : ""} onChange={(e) => handleChange(e, "firstName", true, '')}
                        />
                      </Col>
                      <Col xs={12} md={3}>
                        <TextField fullWidth={true} floatingLabelText="Mobile number *" errorText={fieldErrors.phone ? fieldErrors.phone : ""} value={grievanceCreate.phone?grievanceCreate.phone:""} onChange={(e) => handleChange(e, "phone", true, /^\d{10}$/g)} />
                      </Col>
                    </Row>
                    <Row>
                      <Col xs={12} md={3}>
                        <TextField fullWidth={true} floatingLabelText="Email" errorText={fieldErrors.email ? fieldErrors.email : ""} value={grievanceCreate.email?grievanceCreate.email:""} onChange={(e) => handleChange(e, "email", false, /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/)}  />
                      </Col>
                      <Col xs={12} md={3}>
                        <TextField fullWidth={true} floatingLabelText="Address" multiLine={true} errorText={fieldErrors.requesterAddress ? fieldErrors.requesterAddress : ""} value={grievanceCreate.requesterAddress?grievanceCreate.requesterAddress:""} onChange={(e) => handleChange(e, "requesterAddress", false, '')} />
                      </Col>
                    </Row>
                  </Grid>
                </CardText>
            </Card> : ''
          }
          <Card style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Grievance Information < /div>}/>
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
                  <Row>
                    <Col xs={12} md={12} style={{textAlign:'center'}}>
                      <FlatButton
                        backgroundColor="#a4c639"
                        hoverColor="#8AA62F"
                        label="OR"
                      />
                    </Col>
                  </Row>
                  <Row>
                    <Col xs={12} md={3}>
                      <SelectField fullWidth={true} floatingLabelText="Grievance Category *" maxHeight={200} value={grievanceCreate.serviceCategory?grievanceCreate.serviceCategory:""} errorText={fieldErrors.serviceCategory ? fieldErrors.serviceCategory : ""} onChange={(event, index, value) => {
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
                      <SelectField fullWidth={true} floatingLabelText="Grievance Type *" maxHeight={200} value={grievanceCreate.serviceCode?grievanceCreate.serviceCode:""} errorText={fieldErrors.serviceCode ? fieldErrors.serviceCode : ""} onChange={(event, index, value) => {
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
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > More Details < /div>}/>
              <CardText style={{padding:0}}>
                <Grid>
                  <Row>
                    <Col xs={12} md={3}>
                      <TextField fullWidth={true} hintText="Min: 10 Characters" floatingLabelText="Grievance Details *" multiLine={true} errorText={fieldErrors.description ? fieldErrors.description : ""} value={grievanceCreate.description?grievanceCreate.description:""} onChange={(e) => handleChange(e, "description", true, /^.{10,500}$/)}/>
                    </Col>
                    <Col xs={12} md={3}>
                      <TextField fullWidth={true} floatingLabelText="Landmark" multiLine={true} errorText={fieldErrors.address ? fieldErrors.address : ""} value={grievanceCreate.address?grievanceCreate.address:""} onChange={(e) => handleChange(e, "address", false, '')}/>
                    </Col>
                    <Col xs={12} md={6}>
                      <AutoComplete
                        hintText="Type your location or select it from maps"
                        floatingLabelText="Grievance Location *"
                        filter={AutoComplete.noFilter}
                        fullWidth={true}
                        dataSource={this.state.boundarySource}
                        dataSourceConfig={this.state.boundarySourceConfig}
                        menuStyle={{overflow:'auto', maxHeight: '150px'}}  listStyle={{overflow:'auto'}}
                        onKeyUp={handleAutoCompleteKeyUp}
                        errorText={fieldErrors.addressId ? fieldErrors.addressId : ""} value={grievanceCreate.addressId ? grievanceCreate.addressId : ""}
                        onNewRequest={(chosenRequest, index) => {
                        var e = {
                          target: {
                            value: chosenRequest
                          }
                        };
                        handleChange(e, "addressId", true, "");
                       }}
                      />
                    </Col>
                  </Row>
                  <Row>
                    <Col xs={12} md={3}>
                      <RaisedButton label="Select Photo" containerElement="label" style={{ marginTop: '20px', marginBottom:'20px'}}>
                          <input type="file" accept="image/*" style={{display:'none'}} multiple onChange={(e)=>handleUpload(e)}/>
                      </RaisedButton>
                    </Col>
                    <ImagePreview files={files}/>
                  </Row>
                  <Row>
                    <Col md={12}>
                      <div style={{width: '100%', height: 400}}>
                        <SimpleMap  markers={[]} handler={(places)=>{handleMap(places, "address")}}/>
                      </div>
                    </Col>
                  </Row>
                </Grid>
              </CardText>
          </Card>
          <div style={{textAlign: 'center'}}>
            <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label="Create" backgroundColor={"#5a3e1b"} labelColor={white}/>
            <RaisedButton style={{margin:'15px 5px'}} label="Close"/>
          </div>
        </form>
        <div>
        <Dialog
          title={this.state.srn}
          actions={actions}
          modal={false}
          open={this.state.open}
          onRequestClose={this.handleClose}
        >
        {this.state.acknowledgement}
        </Dialog>
        </div>
      </div>
    );
  }
}

  const mapStateToProps = state => {
    //console.log(state.form.form)
    //console.log(state.form.isFormValid)
    return ({grievanceCreate: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
  }

const mapDispatchToProps = dispatch => ({
  initForm: (type) => {
    var requiredArray = [];
    if(type == 'CITIZEN'){
      requiredArray = ["serviceCategory","serviceCode","description","addressId"]
    }else if(type == 'EMPLOYEE'){
      requiredArray = ["receivingMode","firstName","phone","serviceCategory","serviceCode","description","addressId"]
    }else{
      requiredArray = ["firstName","phone","serviceCategory","serviceCode","description","addressId"]
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
  loadReceivingCenterDD: (name) => {
    dispatch({type: "ADD_MANDATORY", property: name, value: '', isRequired : true, pattern: ''});
    return (
      <Col xs={12} md={3}>
        <SelectField maxHeight={200} floatingLabelText="Receiving Center *" value={_this.props.grievanceCreate.receivingCenter?  _this.props.grievanceCreate.receivingCenter:""} onChange={(event, index, value) => {
          var e = {
            target: {
              value: value
            }
          };
          _this.props.handleChange(e, "receivingCenter", true, "")}} errorText={_this.props.fieldErrors.receivingCenter ? _this.props.fieldErrors.receivingCenter : ""} >
          {_this.state.receivingCenter.map((receivingcenter, index) => (
              <MenuItem value={receivingcenter.id} key={index} primaryText={receivingcenter.name} />
          ))}
        </SelectField>
      </Col>
    );
  },
  handleAutoCompleteKeyUp : (e) => {
    var currentThis = _this;
    dispatch({type: "HANDLE_CHANGE", property: 'addressId', value: '', isRequired : true, pattern: ''});
    if(e.target.value){
      Api.commonApiGet("/egov-location/boundarys/getLocationByLocationName", {locationName : e.target.value}).then(function(response)
      {
        currentThis.setState({boundarySource : response});
      },function(err) {

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
    Api.commonApiPost("/pgr/services/_search", {type:'category', categoryId : group}).then(function(response)
    {
      currentThis.setState({grievanceType : response.complaintTypes});
      e = {
        target: {
          value: sCode
        }
      };
      dispatch({type: "HANDLE_CHANGE", property: 'serviceCode', value: e.target.value, isRequired : true, pattern: ''});
    },function(err) {

    });

  },
  handleMap: (places, field) => {
    dispatch({type: "HANDLE_CHANGE", property:'lat', value: places[0].geometry.location.lat().toString(), isRequired : false, pattern: ''});
    dispatch({type: "HANDLE_CHANGE", property:'lng', value: places[0].geometry.location.lng().toString(), isRequired : false, pattern: ''});
    dispatch({type: "HANDLE_CHANGE", property: 'addressId', value: '0', isRequired : true, pattern: ''});
  },
  handleUpload: (e) => {
    dispatch({type: 'FILE_UPLOAD', files: e.target.files})
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
    if(property == 'addressId'){
      dispatch({type: "HANDLE_CHANGE", property:'lat', value: '0.0', isRequired : false, pattern: ''});
      dispatch({type: "HANDLE_CHANGE", property:'lng', value: '0.0', isRequired : false, pattern: ''});
    }
  },
  changeButtonText:(text)=>
  {
    dispatch({type:"BUTTON_TEXT",text});
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(grievanceCreate);
