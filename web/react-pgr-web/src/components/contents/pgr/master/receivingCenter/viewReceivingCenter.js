import React, {Component} from 'react';
import {connect} from 'react-redux';
import SimpleMap from '../../../../common/GoogleMaps.js';
import Divider from 'material-ui/Divider';
import {Grid, Row, Col, DropdownButton, ListGroup, ListGroupItem} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {List, ListItem} from 'material-ui/List';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import Checkbox from 'material-ui/Checkbox';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import Api from '../../../../../api/api';
import {translate} from '../../../../common/common';

var flag = 0;
const styles = {
  headerStyle : {
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
  },
  checkbox: {
    marginTop: 37
  },
  bold: {
    fontWeight: "bolder"
  }
};

var _this;

class ViewReceivingCenter extends Component {
    constructor(props) {
      super(props);
      this.state = {
          id:'',
          data:''
      }
    }

    componentWillMount() {

        if(this.props.match.params.id) {

            this.setState({id:this.props.match.params.id});
            let  current = this;
            let {setForm} = this.props;

            Api.commonApiPost("/pgr-master/receivingcenter/v1/_search",{id:this.props.match.params.id},{}).then(function(response){
                console.log(response);
                current.setState({data:response.ReceivingCenterType})
                setForm(response.ReceivingCenterType[0])
            }).catch((error)=>{
                console.log(error);
            })
        }

    }

    componentDidMount() { }

    render() {

      let {
        viewReceivingCenter ,
        fieldErrors,
        isFormValid,
        isTableShow,
        handleUpload,
        files,
        handleChange,
        handleMap,
        handleChangeNextOne,
        handleChangeNextTwo,
        buttonText
      } = this.props;

      let {submitForm} = this;

      console.log(isFormValid);

      return(
        <div className="viewReceivingCenter">
              <Card style={styles.marginStyle}>
                  <CardHeader  style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >Receiving Center< /div>} />
                  <CardText style={{padding:0}}>

                      <Grid>
                          <br/>
                          <Card>
                              <CardText>
                                  <ListGroup>
                                      <ListGroupItem>
                                        <Row>
                                          <Col xs={4} md={2} style={styles.bold}>
                                               ID
                                          </Col>
                                          <Col xs={8} md={10}>
                                              {viewReceivingCenter.id ? viewReceivingCenter.id : ''}
                                          </Col>
                                        </Row>
                                      </ListGroupItem>
                                      <ListGroupItem>
                                        <Row>
                                          <Col xs={4} md={2} style={styles.bold}>
                                               {translate("core.lbl.add.name")}
                                          </Col>
                                          <Col xs={8} md={10}>
                                              {viewReceivingCenter.name ? viewReceivingCenter.name : ''}
                                          </Col>
                                        </Row>
                                      </ListGroupItem>
                                      <ListGroupItem>
                                        <Row>
                                          <Col xs={4} md={2} style={styles.bold}>
                                               {translate("core.lbl.code")}
                                          </Col>
                                          <Col xs={8} md={10}>
                                              {viewReceivingCenter.code ? viewReceivingCenter.code : ''}
                                          </Col>
                                        </Row>
                                      </ListGroupItem>
                                      <ListGroupItem>
                                        <Row>
                                          <Col xs={4} md={2} style={styles.bold}>
                                               {translate("core.lbl.description")}
                                          </Col>
                                          <Col xs={8} md={10}>
                                              {viewReceivingCenter.description? viewReceivingCenter.description : ''}
                                          </Col>
                                        </Row>
                                      </ListGroupItem>
                                      <ListGroupItem>
                                        <Row>
                                          <Col xs={4} md={2} style={styles.bold}>
                                               {translate("pgr.lbl.active")}
                                          </Col>
                                          <Col xs={8} md={10}>
                                              {viewReceivingCenter.active? "true": 'false'}
                                          </Col>
                                        </Row>
                                      </ListGroupItem>
                                      <ListGroupItem>
                                        <Row>
                                          <Col xs={4} md={2} style={styles.bold}>
                                               {translate("pgr.lblauditdetails")}
                                          </Col>
                                          <Col xs={8} md={10}>
                                              {viewReceivingCenter.auditdetails ? viewReceivingCenter.auditdetails : ''}
                                          </Col>
                                        </Row>
                                      </ListGroupItem>
                                      <ListGroupItem>
                                        <Row>
                                          <Col xs={4} md={2} style={styles.bold}>
                                               {translate("pgr.lbl.crn")}
                                          </Col>
                                          <Col xs={8} md={10}>
                                              {viewReceivingCenter.iscrnrequired? "true": 'false'}
                                          </Col>
                                        </Row>
                                      </ListGroupItem>
                                      <ListGroupItem>
                                        <Row>
                                          <Col xs={4} md={2} style={styles.bold}>
                                               {translate("pgr.lbl.order.no")}
                                          </Col>
                                          <Col xs={8} md={10}>
                                              {viewReceivingCenter.orderno ? viewReceivingCenter.orderno : ''}
                                          </Col>
                                        </Row>
                                      </ListGroupItem>
                                  </ListGroup>
                              </CardText>
                          </Card>
                      </Grid>
                  </CardText>
              </Card>
        </div>)
    }
}

const mapStateToProps = state => {
  return ({viewReceivingCenter : state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({

  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:true,
      fieldErrors: {},
      validationData: {
        required: {
          current: [],
          required: []
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  }

})

export default connect(mapStateToProps, mapDispatchToProps)(ViewReceivingCenter);
