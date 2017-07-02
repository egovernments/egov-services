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
// import Api from '../../api/financialsApi';
import AutoComplete from 'material-ui/AutoComplete';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import ContentAdd from 'material-ui/svg-icons/content/add';
import FlatButton from 'material-ui/FlatButton';
import FontIcon from 'material-ui/FontIcon';
import Dialog from 'material-ui/Dialog';
import Snackbar from 'material-ui/Snackbar';
import {Redirect} from 'react-router-dom'
import Api from '../../api/api';
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
       this.searchGrievance = this.searchGrievance.bind(this);
       this.validateOTP = this.validateOTP.bind(this);
       this.generatePassword = this.generatePassword.bind(this);
       this.handleSignUpModalOpen = this.handleSignUpModalOpen.bind(this);
       this.generateSignUpOTP = this.generateSignUpOTP.bind(this);
       this.signUp = this.signUp.bind(this);
   }

   componentWillMount() {
    //  console.log(this);
     //call boundary service fetch wards,location,zone data
   }

   componentDidMount() {
     let {initForm} = this.props;
     initForm();

    //  Api.commonApiPost("egf-masters", "functionaries", "_search").then(function(response)
    //  {
    //  console.log(response);
    //  },function(err) {
    //  console.log(err);
    //  });
   }

   componentWillUnmount() {

   }

   componentWillUpdate() {

   }

   componentDidUpdate(prevProps, prevState) {

   }

   loginRequest () {
      var self = this, props = this.props;
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
        props.login(false, response.data.access_token, response.data.UserRequest);
      }).catch(function(response) {
        self.setState({
          errorMsg: "Please check your username and password"
        })
      });

      instance.get('/localization/messages?tenantId=default&locale=en_IN').then(function(response) {
        localStorage.setItem("lang_response", JSON.stringify(response.data.messages));
      }).catch(function(response) {
        throw new Error(response.data.message);
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
        mobErrorMsg: "Mobile Number / Login ID is required"
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

      });
    }
   }

   searchGrievance(e) {
      if(this.state.srn) {
        window.open("/searchGrievance?srn=" + this.state.srn, '_blank');
      }
   }

   validateOTP() {
      var self = this;
      if(!self.state.otp) {
        return self.setState({
          otpErrorMsg: "OTP is required."
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
          pwdErrorMsg: "Password field is required."
        })
      } else {
        self.setState({
          pwdErrorMsg: ""
        })
      }

      if(self.state.pwd != self.state.newPwd) {
        return self.setState({
          pwdErrorMsg: "Passwords do not match."
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

        });
      }
   }

   generateSignUpOTP() {
      var self = this;
      if(self.state.signUpObject.mobileNumber.length != 10) {
        self.setState({
          signUpErrorMsg: "Mobile number should be 10 digits"
        })
      } else if (self.state.signUpObject.password != self.state.signUpObject.confirmPassword) {
        self.setState({
          signUpErrorMsg: "Passwords do not match"
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

        })
      }
   }

   signUp() {
      var self = this;
      if(!self.state.signUpObject.otp) {
        self.setState({
          signUpErrorMsg: "OTP is required"
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

            })
        }, function(err) {

        })
      }
   }

   render() {
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
                  floatingLabelText="Enter OTP"
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
          <div className="Login">
            <Grid>
             <Row>
                  <Col lg={12}>
                      <SelectField
                        floatingLabelText="Select Language"
                        value={this.state.value}
                        onChange={this.handleChange}
                        className="pull-right"
                      >
                        <MenuItem value={1} primaryText="Engilish" />
                        <MenuItem value={2} primaryText="Hindi" />
                      </SelectField>
                  </Col>
              </Row>
              <Row style={styles.marginTop}>
                  <Col xs={12} md={6} mdPush={6} style={styles.marginBottom}>
                    <Card>
                      <CardText>
                          <Row>
                              <Col lg={12}>
                              <h4>Sign In</h4>
                                <Col lg={12}>
                                <TextField
                                    floatingLabelText="Mobile Number / Login ID"
                                    style={styles.fullWidth}
                                    errorText={fieldErrors.username
                                      ? fieldErrors.username
                                      : ""} id="username" value={credential.username?credential.username:""} onChange={(e) => handleChange(e, "username", true, "")} hintText="eg:-8992233223"
                                />
                                </Col>
                                <Col lg={12}>
                                <TextField
                                    floatingLabelText="Password"
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
                                  <RaisedButton disabled={!isFormValid}  label="Sign in" style={styles.buttonTopMargin} className="pull-right" backgroundColor={"#354f57"}  labelColor={white} onClick={(e)=>{
                                    loginRequest()
                                  }}/>
                                  <FlatButton label="Forgot Password ?" style={styles.buttonTopMargin} onClick={showPasswordModal}/>
                                </Col>
                              </Col>
                          </Row>
                          </CardText>
                    </Card>
                  </Col>
                  <Col xs={12}  md={6} mdPull={6}>
                    <Row>
                      <Col xs={12} md={12}>
                        <FloatingActionButton  style={styles.floatingIconButton}>
                            <i className="material-icons">person</i>
                        </FloatingActionButton>
                        <div style={{"float": "left", "cursor": "pointer"}} onClick={handleSignUpModalOpen}>
                          <h4>Create an account</h4>
                          <p>Create an account to avail our services</p>
                        </div>
                      </Col>
                      <Col xs={12} md={12} style={styles.buttonTopMargin}>
                        <FloatingActionButton  style={styles.floatingIconButton}>
                            <i className="material-icons">mode_edit</i>
                        </FloatingActionButton>
                        <div style={styles.floatLeft}>
                          <h4>Register a grievance</h4>
                          <p>Register your grievance</p>
                        </div>
                      </Col>
                      <Col xs={12} md={12} style={styles.buttonTopMargin}>
                        <FloatingActionButton  style={styles.floatingIconButton}>
                            <i className="material-icons">search</i>
                        </FloatingActionButton>
                        <div style={styles.floatLeft}>
                          <h4>Check your grievance status</h4>
                          <TextField
                            hintText="Search Text"
                            value={srn}
                            onChange={(e) => {handleStateChange(e, "srn")}}
                          />
                          <RaisedButton label="Search" backgroundColor={"#354f57"} labelColor={white} onClick={(e)=>{searchGrievance(e)}}/>
                        </div>
                      </Col>
                      <Col xs={12} md={12} style={styles.buttonTopMargin}>
                        <FloatingActionButton  style={styles.floatingIconButton}>
                            <i className="material-icons">phone</i>
                        </FloatingActionButton>
                        <div style={styles.floatLeft}>
                          <h4>Register via grievance cell</h4>
                          <p>Call 1800-425-9766 to register your grievance</p>
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
              title="Recover Password"
              actions={[
                <FlatButton
                  label="Cancel"
                  primary={false}
                  onTouchTap={() => {handleClose("open")}}
                />,
                <FlatButton
                  label="Send Recovery OTP"
                  secondary={true}
                  onTouchTap={(e)=>{sendRecovery("otp")}}
                />
              ]}
              modal={false}
              open={open}
              onRequestClose={(e) => {handleClose("open")}}
            >
              <TextField
                  floatingLabelText="Mobile Number/Login ID"
                  style={styles.fullWidth}
                  errorText={mobErrorMsg} id="mobNo" value={mobNo} onChange={(e) => handleStateChange(e, "mobNo")}
              />
              <div style={{textAlign: "right", fontSize: "12px"}}>
                Recovery link or OTP will be sent to your registered email / mobile
              </div>
            </Dialog>
            <Dialog
              title={!hideOtp ? "Enter OTP Sent To Your Mobile Number" : "New Password"}
              actions={[
                <FlatButton
                  label="Cancel"
                  primary={false}
                  onTouchTap={(e) => {handleClose("open1")}}
                />,
                <FlatButton
                  label={!hideOtp ? "Verify" : "Submit"}
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
              message="Password changed successfully."
              style={{"textAlign": "center"}}
              autoHideDuration={4000}
            />
            <Snackbar
              open={this.state.open4}
              message="Account created successfully."
              style={{"textAlign": "center"}}
              autoHideDuration={4000}
            />
            <Dialog
              title="Create An Account"
              autoScrollBodyContent="true"
              actions={[
                <FlatButton
                  label="Cancel"
                  primary={false}
                  onTouchTap={(e) => {handleClose("open3")}}
                />,
                <FlatButton
                  label={!optSent ? "Generate OTP" : "Sign Up"}
                  secondary={true}
                  disabled={!isAllFields()}
                  onTouchTap={(e)=>{!optSent ? generateSignUpOTP() : signUp()}}
                />
              ]}
              modal={false}
              open={open3}
              onRequestClose={(e) => {handleClose("open3")}}
              contentStyle={{"width": "500"}}
            >
                <Row>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText="Mobile Number"
                        style={styles.fullWidth}
                        value={signUpObject.mobileNumber}
                        type="number"
                        disabled={optSent}
                        onChange={(e) => handleStateChange(e, "signUpObject.mobileNumber")}
                    />
                  </Col>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText="Password"
                        style={styles.fullWidth}
                        value={signUpObject.password}
                        disabled={optSent}
                        type="password"
                        onChange={(e) => handleStateChange(e, "signUpObject.password")}
                    />
                  </Col>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText="Confirm Password"
                        style={styles.fullWidth}
                        value={signUpObject.confirmPassword}
                        disabled={optSent}
                        type="password"
                        onChange={(e) => handleStateChange(e, "signUpObject.confirmPassword")}
                    />
                  </Col>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText="Full Name"
                        style={styles.fullWidth}
                        value={signUpObject.name}
                        disabled={optSent}
                        onChange={(e) => handleStateChange(e, "signUpObject.name")}
                    />
                  </Col>
                  <Col xs={12} md={12}>
                    <TextField
                        floatingLabelText="Email Address (Optional)"
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
                          floatingLabelText="OTP"
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
  }



});

export default connect(mapStateToProps, mapDispatchToProps)(Login);
