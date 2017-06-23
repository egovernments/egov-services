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

import {Redirect} from 'react-router-dom'
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
           mobNo: "",
           mobErrorMsg: "",
           srn: ""
       }
       this.loginRequest = this.loginRequest.bind(this);
       this.showPasswordModal = this.showPasswordModal.bind(this);
       this.handleClose = this.handleClose.bind(this);
       this.handleStateChange = this.handleStateChange.bind(this);
       this.sendRecovery = this.sendRecovery.bind(this);
       this.searchGrievance = this.searchGrievance.bind(this);
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
        localStorage.setItem("", response.data.UserRequest);
        props.login(false, response.data.access_token, response.data.UserRequest);
      }).catch(function(response) {
        self.setState({
          errorMsg: "Please check your username and password"
        })
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
      open: true
    })
   }

   handleClose() {
    this.setState({
      open: false
    })
   }

   handleStateChange(e, name) {
    this.setState({
      mobErrorMsg: "",
      [name]: e.target.value
    })
   }

   sendRecovery(type) {
    if(!this.state.mobNo)
      return this.setState({
        mobErrorMsg: "Mobile Number / Login ID is required"
      })

    else 
      this.setState({
        mobErrorMsg: ""
      });      

    if(type == "link") {

    } else {

    }
   }

   searchGrievance(e) {
      if(this.state.srn) {
        window.open("/searchGrievance?srn=" + this.state.srn, '_blank');
      }
   }

   render() {
      // console.log();
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
        searchGrievance
      } = this;
      let {
        errorMsg,
        open,
        mobErrorMsg,
        mobNo,
        srn
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
                        <div style={styles.floatLeft}>
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
                  onTouchTap={handleClose}
                />,
                <FlatButton
                  label="Send Recovery Link"
                  secondary={true}
                  onTouchTap={(e)=>{sendRecovery("link")}}
                />,
                <FlatButton
                  label="Send Recovery OTP"
                  secondary={true}
                  onTouchTap={(e)=>{sendRecovery("otp")}}
                />
              ]}
              modal={false}
              open={open}
              onRequestClose={handleClose}
            >
              <TextField
                  floatingLabelText="Mobile Number/Login ID"
                  type="password"
                  style={styles.fullWidth}
                  errorText={mobErrorMsg} id="mobNo" value={mobNo} onChange={(e) => handleStateChange(e, "mobNo")} hintText="eg:-*******"
              />
              <div style={{textAlign: "right", fontSize: "12px"}}>
                Recovery link or OTP will be sent to your registered email / mobile
              </div>
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
