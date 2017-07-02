import React, {Component} from 'react';
import {connect} from 'react-redux';
import SimpleMap from '../../../../common/GoogleMaps.js';

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
import Api from '../../../../../api/api';
import {translate} from '../../../../common/common';


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
          data:'',
          open:false
      }
      this.handleOpenNClose=this.handleOpenNClose.bind(this);
    }

    handleOpenNClose() {
      this.setState({
      	open: !this.state.open
      });

      let {initForm}=this.props;
      initForm();
    };

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
            }, function(err) {
              current.props.toggleSnackbarAndSetText(true, err.message);
              current.props.setLoadingStatus('hide');
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
    close(){
      window.close();

    }

    submitForm = (e) => {

      e.preventDefault()
        var current = this;

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

          Api.commonApiPost("/pgr-master/serviceGroup/v1/"+body.ServiceGroup.code+"/_update",{},body).then(function(response){
              console.log(response);
              current.setState({
                open: true
              });
          }, function(err) {
            current.props.toggleSnackbarAndSetText(true, err.message);
            current.props.setLoadingStatus('hide');
        	})
      } else {
          Api.commonApiPost("/pgr-master/serviceGroup/v1/_create",{},body).then(function(response){
              console.log(response);
              current.setState({
                open: true
              });
              current.props.resetObject('createServiceGroup');
          }, function(err) {
            current.props.toggleSnackbarAndSetText(true, err.message);
            current.props.setLoadingStatus('hide');
        	})
      }


    }
    handleClose = () => {
      this.setState({open: false});
    };

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

      let {submitForm,handleOpenNClose} = this;

      console.log(isFormValid);

      return(
        <div className="createServiceGroup">
          <form autoComplete="off" onSubmit={(e) => {submitForm(e)}}>
              <Card style={styles.marginStyle}>
                  <CardHeader  style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {this.state.id != '' ? 'Update Service Group' : 'Create Service Group'} < /div>} />
                  <CardText style={{padding:0}}>
                      <Grid>
                          <Row>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText={translate("core.lbl.add.name")+"*"}
                                      value={createServiceGroup.name? createServiceGroup.name : ""}
                                      errorText={fieldErrors.name ? fieldErrors.name : ""}
                                        onChange={(e) => handleChange(e, "name", true, '')}
                                      id="name"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText={translate("core.lbl.code")+"*"}
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
                                      floatingLabelText={translate("core.lbl.description")}
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
                <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label={this.state.id != '' ? translate("pgr.lbl.update") : translate("pgr.lbl.create")} backgroundColor={"#5a3e1b"} labelColor={white}/>
                <RaisedButton style={{margin:'15px 5px'}} label={translate("core.lbl.close")} onClick={(e)=>{this.close()}}/>
              </div>
          </form>
          <Dialog
               title={this.state.id != '' ? "Service Group Updated Successfully" : "Service Group Added Successfully"}
               actions={<FlatButton
   				        label={translate("core.lbl.close")}
   				        primary={true}
   				        onTouchTap={this.state.id != '' ? this.handleClose : handleOpenNClose}
   				      />}
               modal={false}
               open={this.state.open}
              onRequestClose={this.state.id != '' ? this.handleClose : handleOpenNClose}
             >
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

  resetObject: (object) => {
    console.log(object);
   dispatch({
     type: "RESET_OBJECT",
     object
   })
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
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState,toastMsg});
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(ServiceGroupCreate);
