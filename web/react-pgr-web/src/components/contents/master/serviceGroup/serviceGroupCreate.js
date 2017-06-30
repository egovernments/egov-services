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
      this.state = {
          id:'',
          data:''
      }
    }

    componentWillMount() {

        if(this.props.match.params.id) {
          console.log();
            this.setState({id:this.props.match.params.id});
            var body = {}
            let  current = this;
            let {setForm} = this.props;

            Api.commonApiPost("/pgr-master/serviceGroup/v1/_search",{id:this.props.match.params.id},body).then(function(response){
                console.log("response",response);
                  console.log("response object",response.ServiceGroups[0]);
                current.setState({data:response.ServiceGroups})
                setForm(response.ServiceGroups[0])
            }).catch((error)=>{
                console.log(error);
            })
        } else {
          let {initForm}=this.props;
          initForm();
        }
    }

    componentDidMount() {

    }

    componentDidUpdate() {


    }

    submitForm = (e) => {

      e.preventDefault()

      var body = {
          "ServiceGroup":{
           "id": this.props.createServiceGroup.id,
           "name" :this.props.createServiceGroup.name,
           "code" :this.props.createServiceGroup.code,
           "description" :this.props.createServiceGroup.description,
           "tenantId":"default"
          }
      }

      if(this.props.match.params.id){
        console.log("hi");
          Api.commonApiPost("/pgr-master/serviceGroup/v1/"+body.ServiceGroup.code+"/_update",{},body).then(function(response){
              console.log(response);
          }).catch((error)=>{
              console.log(error);
          })
      } else {
          Api.commonApiPost("/pgr-master/serviceGroup/v1/_create",{},body).then(function(response){
              console.log(response);
          }).catch((error)=>{
              console.log(error);
          })
      }


    }

    render() {

      let {
        createServiceGroup ,
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
        <div className="createServiceGroup">
          <form autoComplete="off" onSubmit={(e) => {submitForm(e)}}>
              <Card style={styles.marginStyle}>
                  <CardHeader  style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Contact Information < /div>} />
                  <CardText style={{padding:0}}>
                      <Grid>
                          <Row>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Name"
                                      value={createServiceGroup.name? createServiceGroup.name : ""}
                                      errorText={fieldErrors.name ? fieldErrors.name : ""}
                                        onChange={(e) => handleChange(e, "name", true, '')}
                                      id="name"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Code"
                                      value={createServiceGroup.code? createServiceGroup.code : ""}
                                      errorText={fieldErrors.code ? fieldErrors.code : ""}
                                      onChange={(e) => handleChange(e, "code", true, '')}
                                      id="code"
                                      disabled={this.state.id ? true : false }
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Description"
                                      value={createServiceGroup.description? createServiceGroup.description : ""}
                                      errorText={fieldErrors.description ? fieldErrors.description : ""}
                                      onChange={(e) => handleChange(e, "description", false, '')}
                                      multiLine={true}
                                      id="description"
                                  />
                              </Col>
                          </Row>
                      </Grid>
                  </CardText>
              </Card>
              <div style={{textAlign:'center'}}>
                <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label={this.state.id != '' ? 'Update' : 'Create'} backgroundColor={"#5a3e1b"} labelColor={white}/>
                <RaisedButton style={{margin:'15px 5px'}} label="Close"/>
              </div>
          </form>
          <Dialog
               title="Data Added Successfully"
               actions={<FlatButton
   				        label="Close"
   				        primary={true}
   				        onTouchTap={this.handleClose}
   				      />}
               modal={false}
               open={this.state.open}
               onRequestClose={this.handleClose}
             >
              Data Added Successfully
         </Dialog>
        </div>)
    }

}

const mapStateToProps = state => {
  return ({createServiceGroup : state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["name","code"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },

  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:true,
      fieldErrors: {},
      validationData: {
        required: {
          current: ["name","code"],
          required: ["name","code"]
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
