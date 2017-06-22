import React, {Component} from 'react';
import {connect} from 'react-redux';
import SimpleMap from '../../../common/GoogleMaps.js';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
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
import Api from '../../../../api/api';


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
  },
  checkbox: {
    marginTop: 37
  }
};

var _this;

class ServiceGroupCreate extends Component {
    constructor(props) {
      super(props);
      this.create = this.create.bind();
    }

    componentWillMount() {
    }

    componentDidMount() {
     let {initForm}=this.props;
     initForm();
    }


    create = (e) => {

      e.preventDefault()

      var body = {

          "ServiceGroup":{
           "name" :this.props.serviceGroupCreate.name,
           "code" :this.props.serviceGroupCreate.code,
           "description" :this.props.serviceGroupCreate.description,
           "tenantId":"default"
          }
      }

      Api.commonApiPost("/pgr-master/serviceGroup/_create",{},body).then(function(response){
          console.log(response);
      }).catch((error)=>{
          console.log(error);
      })

    }

    render() {

      let {
        serviceGroupCreate,
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
      let {create} = this;
      return(
        <div className="serviceGroupCreate">
          <form autoComplete="off" onSubmit={(e) => {create(e)}}>
              <Card style={styles.marginStyle}>
                  <CardHeader  style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Add Service Group < /div>} />
                  <CardText style={{padding:0}}>
                      <Grid>
                          <Row>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Name"
                                      value={serviceGroupCreate.name? serviceGroupCreate.name : ""}
                                      errorText={fieldErrors.name ? fieldErrors.name : ""}
                                      onChange={(e) => handleChange(e, "name", true, '')}
                                      id="name"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Code"
                                      value={serviceGroupCreate.code? serviceGroupCreate.code : ""}
                                      errorText={fieldErrors.code ? fieldErrors.code : ""}
                                      onChange={(e) => handleChange(e, "code", true, '')}
                                      id="code"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Description"
                                      value={serviceGroupCreate.description? serviceGroupCreate.description : ""}
                                      errorText={fieldErrors.description ? fieldErrors.description : ""}
                                      onChange={(e) => handleChange(e, "description", true, '')}
                                      multiLine={true}
                                      id="description"
                                  />
                              </Col>


                          </Row>
                      </Grid>
                  </CardText>
              </Card>
              <div style={{textAlign:'center'}}>
                <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label="Create" backgroundColor={"#5a3e1b"} labelColor={white}/>
              </div>
          </form>
        </div>)
    }

}

const mapStateToProps = state => {
  return ({serviceGroupCreate: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["name","code","description"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },

  handleChange: (e, property, isRequired, pattern) => {
    console.log("handlechange"+e+property+isRequired+pattern);
    dispatch({
      type: "HANDLE_CHANGE",
      property,
      value: e.target.value,
      isRequired,
      pattern
    });
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(ServiceGroupCreate);
