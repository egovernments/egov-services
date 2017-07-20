import React, { Component } from 'react';
import { connect } from 'react-redux';

import { Grid,Row,Col} from 'react-bootstrap';
import {Card, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500,red500, white} from 'material-ui/styles/colors';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import Checkbox from 'material-ui/Checkbox';
import AutoComplete from 'material-ui/AutoComplete';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import FlatButton from 'material-ui/FlatButton';
import FontIcon from 'material-ui/FontIcon';
import Dialog from 'material-ui/Dialog';
import Snackbar from 'material-ui/Snackbar';
import {Redirect} from 'react-router-dom'
import Api from '../../api/api';
import {translate} from '../common/common';
var axios = require('axios');

const styles = {
    errorStyle: {
      color: red500,
    },
    customWidth: {
    width: 150,
    },
    underlineStyle: {
      borderColor: brown500,
    },
    underlineFocusStyle: {
      borderColor: brown500,
    },
    floatingLabelStyle: {
      color: brown500,
    },
    floatingLabelFocusStyle: {
      color: brown500,
    },checkbox: {
      marginBottom: 16,
      marginTop:24
    },
    floatingIconButton: {
      marginRight:20,
      float:'left'
    },
    marginTop: {
      marginTop:30
    },
    buttonTopMargin: {
      marginTop:20
    },
    floatLeft: {
      float:'left'
    },
    fullWidth: {
      width:'100%'
    },
    iconSize: {
      fontSize:24,
      marginRight:5,
      marginLeft:5
    },
    moreDetailsCont: {
      textAlign:'center',
      paddingBottom:20
    },
    marginBottom: {
      marginBottom:30
    }
};


class Login extends Component {

  constructor(props) {
       super(props);
       this.state = {
           isActive: {
             checked: false
           },
           localeready : false,
           locale: localStorage.getItem('locale') ? localStorage.getItem('locale') : 'en_IN',
           dataSource: [],
           errorMsg: "",
           open: false,
           open1: false,
           mobNo: "",
           mobErrorMsg: "",
           srn: "",
           otp: "",
           otpErrorMsg: "",
           hideOtp: false,
           pwdErrorMsg: "",
           newPwd: "",
           pwd: "",
           uuid: "",
           open2: false,
           open3: false,
           optSent: false,
           signUpObject: {
              userName: "",
              mobileNumber: "",
              password: "",
              confirmPassword: "",
              emailId: "",
              name: ""
           },
           signUpErrorMsg: ""
       }
       this.loginRequest = this.loginRequest.bind(this);
       this.showPasswordModal = this.showPasswordModal.bind(this);
       this.handleClose = this.handleClose.bind(this);
       this.handleStateChange = this.handleStateChange.bind(this);
       this.sendRecovery = this.sendRecovery.bind(this);
       this.validateOTP = this.validateOTP.bind(this);
       this.generatePassword = this.generatePassword.bind(this);
       this.handleSignUpModalOpen = this.handleSignUpModalOpen.bind(this);
       this.generateSignUpOTP = this.generateSignUpOTP.bind(this);
       this.signUp = this.signUp.bind(this);
   }

   componentWillMount() {

   }

   componentDidMount() {
     let {initForm, setLoadingStatus, setHome} = this.props;
     initForm();
     setLoadingStatus("loading");
     setHome(false);
     this.handleLocaleChange(this.state.locale);
   }

   componentWillUnmount() {

   }

   componentWillUpdate() {

   }

   componentDidUpdate(prevProps, prevState) {

   }

   handleLocaleChange = (value) => {
     //console.log(value);
     let {setLoadingStatus} = this.props;
     var self = this;
     Api.commonApiGet("/localization/messages", {locale : value}).then(function(response)
     {
       self.setState({'locale':value});
       self.setState({'localeready':true});
       localStorage.setItem("locale", value);
       localStorage.setItem("lang_response", JSON.stringify(response.messages));
       setLoadingStatus("hide");
     },function(err) {
        self.props.toggleSnackbarAndSetText(true, err.message);
     });
   }

   loginRequest (e) {
	var current = this;
    this.props.setLoadingStatus('loading');
	   e.preventDefault();
      var self = this, props = this.props;
      let {setActionList}=this.props;
      self.setState({
          errorMsg: ""
      })
      var instance = axios.create({
        baseURL: window.location.origin,
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'Authorization': 'Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0',
        }
      });

      var params = new URLSearchParams();
      params.append('username', props.credential.username);
      params.append('password', props.credential.password);
      params.append('grant_type', 'password');
      params.append('scope', 'read');
      params.append('tenantId', 'default');

      instance.post('/user/oauth/token', params).then(function(response) {
        localStorage.setItem("token", response.data.access_token);
        localStorage.setItem("userRequest", JSON.stringify(response.data.UserRequest));
        localStorage.setItem("auth", response.data.access_token);
				localStorage.setItem("type", response.data.UserRequest.type);
				localStorage.setItem("id", response.data.UserRequest.id);
				localStorage.setItem("tenantId", response.data.UserRequest.tenantId);
        props.login(false, response.data.access_token, response.data.UserRequest);

        let roleCodes=[];
        for (var i = 0; i < response.data.UserRequest.roles.length; i++) {
          roleCodes.push(response.data.UserRequest.roles[i].code);
        }


   Api.commonApiPost("access/v1/actions/_get",{},{tenantId:"default",roleCodes}).then(function(response){

				//console.log(response)
		  localStorage.setItem("actions", JSON.stringify(response.actions));
			setActionList(response.modules)
        },function(err) {
            console.log(err);
        });

      }).catch(function(response) {
		  current.props.setLoadingStatus('hide');
        self.setState({
          errorMsg: "Please check your username and password"
        });
      });

   }

   handleCheckBoxChange = (prevState) => {
      this.setState((prevState) => {prevState.isActive.checked = !prevState.isActive.checked})
   }

   handleUpdateInput = (value) => {
     this.setState({
       dataSource: [
         value,
         value + value,
         value + value + value,
       ],
     });
   };

   showPasswordModal() {
    this.setState({
      open: true,
      hideOtp: false
    })
   }

   handleClose(name) {
    this.setState({
      [name]: false
    })
   }

   handleStateChange(e, name) {
    if(/\./.test(name)) {
      var names = name.split(".");
      this.setState({
        mobErrorMsg: "",
        [names[0]]: {
          ...this.state[names[0]],
          [names[1]]: e.target.value
        }
      })
    } else {
      this.setState({
        mobErrorMsg: "",
        [name]: e.target.value
      })
    }
   }

   sendRecovery(type) {
    var self = this;
    if(!self.state.mobNo)
      return self.setState({
        mobErrorMsg: translate('pgr.lbl.loginreqrd')
      })

    else
      self.setState({
        mobErrorMsg: ""
      });

    if(type == "link") {

    } else {
      var rqst = {
          "mobileNumber": self.state.mobNo,
          "tenantId": localStorage.getItem("tenantId") || "default"
      }

      Api.commonApiPost("user-otp/v1/_send", {}, {otp: rqst}).then(function(response) {
          self.setState({
            open: false
          }, function() {
            self.setState({
              open1: true
            })
          })
      }, function(err) {
        self.props.toggleSnackbarAndSetText(true, err.message);
      });
    }
   }

   searchGrievance = (e) => {
     let {setRoute, setHome} = this.props;
     if(this.state.srn) {
        setRoute("/pgr/viewGrievance/"+this.state.srn);
        setHome(true);
     }
   }

   validateOTP() {
      var self = this;
      if(!self.state.otp) {
        return self.setState({
          otpErrorMsg: translate('pgr.lbl.otprqrd')
        })
      } else {
        self.setState({
          otpErrorMsg: ""
        });
      }

      var rqst = {
        tenantId: localStorage.getItem("tenantId") || "default",
        otp: self.state.otp,
        identity: self.state.mobNo
      };
      Api.commonApiPost("otp/v1/_validate", {}, {otp: rqst}).then(function(response) {
          self.setState({
            hideOtp: true,
            uuid: response.otp.UUID
          })
      }, function(err) {
        self.props.toggleSnackbarAndSetText(true, err.message);
      });
   }

   handleSignUpModalOpen() {
      this.setState({
        open3: !this.state.open3,
        optSent: false
      })
   }

   generatePassword() {
      var self = this;
      if(!self.state.pwd || !self.state.newPwd) {
        return self.setState({
          pwdErrorMsg: translate('Password field is required.')
        })
      } else {
        self.setState({
          pwdErrorMsg: ""
        })
      }

      if(self.state.pwd != self.state.newPwd) {
        return self.setState({
          pwdErrorMsg: translate('core.error.password.confirmpassword.same')
        })
      } else {
        var rqst = {
          userName: self.state.mobNo,
          newPassword: self.state.newPwd,
          tenantId: localStorage.getItem("tenantId") || "default",
          otpReference: self.state.uuid
        };

        Api.commonApiPost("user/password/nologin/_update", {}, {...rqst}, true).then(function(response) {
          self.setState({
            open1: false,
            mobNo: "",
            pwd: "",
            newPwd: "",
            open2: true
          });
        }, function(err) {
          self.props.toggleSnackbarAndSetText(true, err.message);
        });
      }
   }

   generateSignUpOTP() {
      var self = this;
      if(self.state.signUpObject.mobileNumber.length != 10) {
        self.setState({
          signUpErrorMsg: translate('core.lbl.enter.mobilenumber')
        })
      } else if (self.state.signUpObject.password != self.state.signUpObject.confirmPassword) {
        self.setState({
          signUpErrorMsg: translate('core.error.password.confirmpassword.same')
        })
      } else {
        var signUpObject = Object.assign({}, self.state.signUpObject);
        delete signUpObject.confirmPassword;
        signUpObject.userName = signUpObject.mobileNumber;
        //Generate OTP
        Api.commonApiPost("user-otp/v1/_send", {}, {"otp": {mobileNumber: signUpObject.mobileNumber, tenantId: localStorage.getItem("tenantId") || "default"}}).then(function(response){
          self.setState({
            signUpErrorMsg: "",
            optSent: true
          })
        }, function (err){
          self.props.toggleSnackbarAndSetText(true, err.message);
        })
      }
   }

   signUp() {
      var self = this;
      if(!self.state.signUpObject.otp) {
        self.setState({
          signUpErrorMsg: translate("pgr.lbl.otprqrd")
        })
      } else {
        Api.commonApiPost("otp/v1/_validate", {}, {
          otp: {
            "tenantId": localStorage.getItem("tenantId") || "default",
            "otp": self.state.signUpObject.otp,
            "identity": self.state.signUpObject.mobileNumber
          }
        }).then(function(response) {
            var user = Object.assign({}, self.state.signUpObject);
            delete user.confirmPassword;
            user.userName = user.mobileNumber;
            user.otpReference = response.otp.UUID;
            user.tenantId = localStorage.getItem("tenantId") || "default";
            Api.commonApiPost("user/citizen/_create", {}, {
              User: user
            }).then(function(response){
              self.setState({
                open3: false,
                signUpErrorMsg: "",
                optSent: false,
                open4: true
              })
            }, function(err) {
              self.props.toggleSnackbarAndSetText(true, err.message);
            })
        }, function(err) {
          self.props.toggleSnackbarAndSetText(true, err.message);
        })
      }
   }
   openAnonymousComplaint = () => {
     let {setRoute, setHome} = this.props;
     setRoute('/pgr/createGrievance');
     setHome(true);
   }
   render() {
      //console.log("IN LOGIN");
      let {
        login,
        credential,
        handleChange,
        isFormValid,
        fieldErrors,
        history
      }=this.props;
      let {
        loginRequest,
        showPasswordModal,
        handleClose,
        handleStateChange,
        sendRecovery,
        searchGrievance,
        validateOTP,
        generatePassword,
        handleSignUpModalOpen,
        generateSignUpOTP,
        signUp
      } = this;
      let {
        errorMsg,
        open,
        mobErrorMsg,
        mobNo,
        srn,
        otp,
        otpErrorMsg,
        open1,
        open2,
        hideOtp,
        pwdErrorMsg,
        pwd,
        newPwd,
        open3,
        optSent,
        signUpObject
      } = this.state;

      // if (token) {
      //     return (
      //       <Redirect to="/dashboard"/>
      //     )
      // } else {

      const showError = function() {
        if(errorMsg) {
          return (<p className="text-danger">{errorMsg}</p>)
        }
      }

      const showForOTP = function() {
        if(!hideOtp) {
          return (
            <TextField
                  floatingLabelText={translate('core.lbl.enter.OTP')}
                  style={styles.fullWidth}
                  errorText={otpErrorMsg} id="otp" value={otp} onChange={(e) => handleStateChange(e, "otp")}
              />
          )
        }
      }

      const showForPwd = function() {
        if(hideOtp) {
          return (
            <div>
              <TextField
                    floatingLabelText="New Password"
                    style={styles.fullWidth}
                    errorText={pwdErrorMsg} id="otp" value={pwd} onChange={(e) => handleStateChange(e, "pwd")}
                />
              <TextField
                    floatingLabelText="Confirm Password"
                    style={styles.fullWidth}
                    errorText={pwdErrorMsg} id="otp" value={newPwd} onChange={(e) => handleStateChange(e, "newPwd")}
                />
            </div>
          )
        }
      }

      const isAllFields = function() {
        if(signUpObject.mobileNumber && signUpObject.name && signUpObject.password && signUpObject.confirmPassword) {
          return true;
        }

        return false;
      }


        return(
          <div>
          {this.state.localeready ?
          <div className="Login">
            <Grid>
             <Row>
                  <Col lg={12}>
                      <SelectField
                        floatingLabelText={translate('core.lbl.setlanguage')}
                        value={this.state.locale}
                        onChange={(event, key, payload) => {
                          this.props.setLoadingStatus("loading");
                          this.setState({'localeready':false});
                          this.handleLocaleChange(payload)
                        }}
                        className="pull-right"
                      >
                        <MenuItem value={"en_IN"} primaryText="English" />
                        <MenuItem value={"mr_IN"} primaryText="Marathi" />
                      </SelectField>
                  </Col>
              </Row>
              <Row style={styles.marginTop}>
                  <Col xs={12} md={6} mdPush={6} style={styles.marginBottom}>
					<form autoComplete="off" onSubmit={(e) => {
					loginRequest(e)}}>
                    <Card>
                      <CardText>
                          <Row>
                              <Col lg={12}>
                              <h4>{translate('core.lbl.signin')}</h4>
                                <Col lg={12}>
                                <TextField
                                    floatingLabelText={translate('core.lbl.addmobilenumber/login')}
                                    style={styles.fullWidth}
                                    errorText={fieldErrors.username
                                      ? fieldErrors.username
                                      : ""} id="username" value={credential.username?credential.username:""} onChange={(e) => handleChange(e, "username", true, "")} hintText="eg:-8992233223"
                                />
                                </Col>
                                <Col lg={12}>
                                <TextField tabindex="0"
                                    floatingLabelText={translate('core.lbl.password')}
                                    type="password"
                                    style={styles.fullWidth}
                                    errorText={fieldErrors.password
                                      ? fieldErrors.password
                                      : ""} id="password" value={credential.password?credential.password:""} onChange={(e) => handleChange(e, "password", true, "")} hintText="eg:-*******"
                                />
                                </Col>
                                <Col lg={12}>
                                  {showError()}
                                </Col>
                                <Col lg={12}>
                                  <RaisedButton disabled={!isFormValid} type="submit" label={translate('core.lbl.signin')} style={styles.buttonTopMargin} className="pull-right" primary={true} />
                                  <FlatButton label={translate('core.lbl.forgot.password')} style={styles.buttonTopMargin} onClick={showPasswordModal}/>
                                </Col>
                              </Col>
                          </Row>
                          </CardText>
                    </Card>
					</form>
                  </Col>
                  <Col xs={12}  md={6} mdPull={6}>
                    <Row>
                      <Col xs={12} md={12}>
                        <FloatingActionButton  style={styles.floatingIconButton}>
                            <i className="material-icons">person</i>
                        </FloatingActionButton>
                        <div style={{"float": "left", "cursor": "pointer"}} onClick={handleSignUpModalOpen}>
                          <h4>{translate('pgr.title.create.account')}</h4>
                          <p>{translate('pgr.msg.creategrievance.avail.onlineservices')}</p>
                        </div>
                      </Col>
                      <Col xs={12} md={12} style={styles.buttonTopMargin}>
                        <FloatingActionButton  style={styles.floatingIconButton}>
                            <i className="material-icons">mode_edit</i>
                        </FloatingActionButton>
                        <div style={{"float": "left", "cursor": "pointer"}} onClick={this.openAnonymousComplaint}>
                          <h4>{translate('pgr.lbl.register.grievance')}</h4>
                          <p>{translate('pgr.lbl.register.grievance')}</p>
                        </div>
                      </Col>
                      <Col xs={12} md={12} style={styles.buttonTopMargin}>
                        <FloatingActionButton  style={styles.floatingIconButton}>
                            <i className="material-icons">search</i>
                        </FloatingActionButton>
                        <div style={styles.floatLeft}>
                          <h4>{translate('pgr.msg.complaintstatus.anytime')}</h4>
                          <TextField
                            hintText={translate('pgr.lbl.complaintnumber')}
                            value={srn}
                            onChange={(e) => {handleStateChange(e, "srn")}}
                          />
                          <RaisedButton label={translate('core.lbl.search')} onClick={(e)=>{searchGrievance(e)}} primary={true}/>
                        </div>
                      </Col>
                      <Col xs={12} md={12} style={styles.buttonTopMargin}>
                        <FloatingActionButton  style={styles.floatingIconButton}>
                            <i className="material-icons">phone</i>
                        </FloatingActionButton>
                        <div style={styles.floatLeft}>
                          <h4>{translate('pgr.lbl.grievancecell')}</h4>
                          <p>{translate('Call 1800-425-9766 to register your grievance')}</p>
                        </div>
                      </Col>
                    </Row>
                  </Col>
              </Row>
            </Grid>
            <hr/>
            <Grid>
              <Row style={styles.moreDetailsCont}>
                  <Col xs={12} md={4} style={styles.buttonTopMargin}>
                      <FontIcon style={styles.iconSize}>
                        <i className="material-icons">location_on</i>
                      </FontIcon>
                      <p>Kurnool Municipal Corporation, N.R.Peta, Kurnool - 518004 , Andhra Pradesh - India.</p>
                      <a href="#">Find us on google maps</a>
                  </Col>
                  <Col xs={12} md={4} style={styles.buttonTopMargin}>
                      <FontIcon   style={styles.iconSize}>
                        <i className="material-icons">phone</i>
                      </FontIcon>
                      <p>08518-221847</p>
                      <a href="mailto:mc.kurnool@cdma.gov.in" >mc.kurnool@cdma.gov.in</a>
                  </Col>
                  <Col xs={12} md={4} style={styles.buttonTopMargin}>
                      <FontIcon style={styles.iconSize}>
                        <i className="material-icons">share</i>
                      </FontIcon>
                      <p>Share us on</p>
                      <a href="#" ><i className="fa fa-twitter" style={styles.iconSize}></i></a>
                      <a href="#" ><i className="fa fa-facebook" style={styles.iconSize}></i></a>
                  </Col>
              </Row>
            </Grid>
            <Dialog
              title={translate('pgr.lbl.recoverpswd')}
              actions={[
                <FlatButton
                  label={translate('core.lbl.cancel')}
                  primary={false}
                  onTouchTap={() => {handleClose("open")}}
                />,
                <FlatButton
                  label={translate('pgr.lbl.sendotp')}
                  secondary={true}
                  onTouchTap={(e)=>{sendRecovery("otp")}}
                />
              ]}
              modal={false}
              open={open}
              onRequestClose={(e) => {handleClose("open")}}
            >
              <TextField
                  floatingLabelText={translate('core.lbl.addmobilenumber/login')}
                  style={styles.fullWidth}
                  errorText={mobErrorMsg} id="mobNo" value={mobNo} onChange={(e) => handleStateChange(e, "mobNo")}
              />
              <div style={{textAlign: "right", fontSize: "12px"}}>
                {translate('pgr.lbl.recoverylink')}
              </div>
            </Dialog>
            <Dialog
              title={!hideOtp ? translate('pgr.lbl.otpnumber') : translate('core.lbl.new.password')}
              actions={[
                <FlatButton
                  label={translate('core.lbl.cancel')}
                  primary={false}
                  onTouchTap={(e) => {handleClose("open1")}}
                />,
                <FlatButton
                  label={!hideOtp ? translate('pgr.lbl.verify') : translate('core.lbl.submit')}
                  secondary={true}
                  onTouchTap={(e)=>{!hideOtp ? validateOTP() : generatePassword()}}
                />
              ]}
              modal={false}
              open={open1}
              onRequestClose={(e) => {handleClose("open1")}}
            >
              {showForOTP()}
              {showForPwd()}
            </Dialog>
            <Snackbar
              open={this.state.open2}
              message={translate('core.msg.success.password.updated')}
              style={{"textAlign": "center"}}
              onRequestClose={(e) => {handleClose("open2")}}
              autoHideDuration={4000}
            />
            <Snackbar
              open={this.state.open4}
              message={translate('core.account.created.successfully')}
              style={{"textAlign": "center"}}
              autoHideDuration={4000}
              onRequestClose={(e) => {handleClose("open4")}}
            />
            <Dialog
              title={translate('pgr.title.create.account')}
              autoScrollBodyContent="true"
              actions={[
                <FlatButton
                  label={translate('core.lbl.cancel')}
                  primary={false}
                  onTouchTap={(e) => {handleClose("open3")}}
                />,
                <FlatButton
                  label={!optSent ? translate('pgr.lbl.generate.otp') : translate('core.lbl.signup')}
                  secondary={true}
                  disabled={!isAllFields()}
                  onTouchTap={(e)=>{!optSent ? generateSignUpOTP() : signUp()}}
                />
              ]}
              modal={true}
              open={open3}
              onRequestClose={(e) => {handleClose("open3")}}
              contentStyle={{"width": "500"}}
            >
                <Row>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText={translate('core.lbl.mobilenumber')}
                        style={styles.fullWidth}
                        value={signUpObject.mobileNumber}
                        type="number"
                        disabled={optSent}
                        onChange={(e) => handleStateChange(e, "signUpObject.mobileNumber")}
                    />
                  </Col>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText={translate('core.lbl.password')}
                        style={styles.fullWidth}
                        value={signUpObject.password}
                        disabled={optSent}
                        type="password"
                        onChange={(e) => handleStateChange(e, "signUpObject.password")}
                    />
                  </Col>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText={translate('core.lbl.confirm.password')}
                        style={styles.fullWidth}
                        value={signUpObject.confirmPassword}
                        disabled={optSent}
                        type="password"
                        onChange={(e) => handleStateChange(e, "signUpObject.confirmPassword")}
                    />
                  </Col>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText={translate('core.lbl.fullname')}
                        style={styles.fullWidth}
                        value={signUpObject.name}
                        disabled={optSent}
                        onChange={(e) => handleStateChange(e, "signUpObject.name")}
                    />
                  </Col>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText={translate('core.lbl.email')}
                        style={styles.fullWidth}
                        value={signUpObject.emailId}
                        disabled={optSent}
                        onChange={(e) => handleStateChange(e, "signUpObject.emailId")}
                    />
                  </Col>
                  <Col xs={12} md={12}>
                    {
                      (optSent) ?
                        (<TextField
                          floatingLabelText={translate('core.lbl.otp')}
                          style={styles.fullWidth}
                          value={signUpObject.otp}
                          onChange={(e) => handleStateChange(e, "signUpObject.otp")}
                        />) : ""
                    }
                  </Col>
                  <Col md={12}>
                    <p className="text-danger">
                      {this.state.signUpErrorMsg}
                    </p>
                  </Col>
                </Row>
            </Dialog>
          </div>
          : ""}
          </div>
        )
      // }

    }
}

const mapStateToProps = state => ({  credential: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["username","password"]
        }
      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  },
  login: (error, token, userRequest) =>{
    let payload = {
      "access_token": token, "UserRequest": userRequest
    };
    dispatch({type: "LOGIN", error, payload})
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  setActionList:(actionList)=>{
    dispatch({type:"SET_ACTION_LIST",actionList});
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route}),
  setHome: (showHome) => dispatch({type: "SET_HOME", showHome})
});

export default connect(mapStateToProps, mapDispatchToProps)(Login);
