import React, { Component } from 'react';
import { connect } from 'react-redux';

import { Grid,Row,Col,Table } from 'react-bootstrap';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
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


class MainPage extends Component {

  constructor(props) {
       super(props);
       this.state = {

           isActive: {
             checked: false
           },
           dataSource: []
       }

   }

   componentWillMount() {
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



    render(){
      let {
        MainPage,
        handleChange,
        isFormValid,
        fieldErrors,
        buttonText
      }=this.props;
      return(
        <div className="MainPage">
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
                              />
                              </Col>
                              <Col lg={12}>
                              <TextField
                                  floatingLabelText="Password"
                                  type="password"
                                  style={styles.fullWidth}
                              />
                              </Col>
                              <Col lg={12}>
                                <RaisedButton label="Sign in" style={styles.buttonTopMargin} className="pull-right" backgroundColor={brown500} labelColor={white}/>
                                <FlatButton label="Forgot Password?" style={styles.buttonTopMargin} />
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
                        />
                        <RaisedButton label="Search"  backgroundColor={brown500} labelColor={white}/>
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
        </div>
      )
    }
}

const mapStateToProps = state => ({MainPage: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: [ ]
        },

      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  },



});

export default connect(mapStateToProps, mapDispatchToProps)(MainPage);
