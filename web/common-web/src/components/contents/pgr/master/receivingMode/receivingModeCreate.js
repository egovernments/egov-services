import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import Checkbox from 'material-ui/Checkbox';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
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
    margin: 37
  }
};

var _this;

class receivingModeCreate extends Component {
  constructor(props) {
       super(props);
       this.state = {
          open: false,
       }
       this.addOrUpdate=this.addOrUpdate.bind(this);
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
             this.setState({id:this.props.match.params.id});
             var body = {}
             let  _this = this;
             let {setForm} = this.props;

             Api.commonApiPost("/pgr-master/receivingmode/v1/_search",{id:this.props.match.params.id},body).then(function(response){

                 _this.setState({data:response.ReceivingModeType})
                 setForm(response.ReceivingModeType[0])
             }).catch((error)=>{
                 console.log(error);
             })
         } else {
           let {initForm}=this.props;
           initForm();
         }
     }

     close(){

      window.open(window.location, '_self').close();
  }


  addOrUpdate(e) {
    e.preventDefault();
    var _this = this;

    let {changeButtonText,receivingmodeSet}=this.props;
    var body ={
      ReceivingModeType:{
         name :receivingmodeSet.name,
         code :receivingmodeSet.code,
         description :receivingmodeSet.description,
         active :receivingmodeSet.active?receivingmodeSet.active:false,
         tenantId :localStorage.getItem("tenantId"),
         channels :receivingmodeSet.channel
       }
    }
      if(_this.props.match.params.id){

            Api.commonApiPost("pgr-master/receivingmode/v1/"+body.ReceivingModeType.code+"/_update", {},body).then(function(response) {

              _this.setState({
          			open: true
          		});
              _this.props.resetObject('receivingmodeSet');
          }, function(err) {
            _this.props.toggleSnackbarAndSetText(true, err.message);
            _this.props.setLoadingStatus('hide');
        	})
      } else {
          Api.commonApiPost("pgr-master/receivingmode/v1/_create", {},body).then(function(response) {
            _this.setState({
              open: true
            });
            _this.props.resetObject('receivingmodeSet');
        }, function(err) {
          _this.props.toggleSnackbarAndSetText(true, err.message);
          _this.props.setLoadingStatus('hide');
      	})
      }


  }


  render() {
  let url = this.props.location.pathname;
  var _this = this;
   let {addOrUpdate,handleOpenNClose} = this;
   let {open} =this.state;
   let {
     handleChange,
     handleChangeNextOne,
     handleChangeNextTwo,
     isFormValid,
     isTableShow,
     receivingmodeSet,
     fieldErrors,isDialogOpen,msg} = this.props;


   return (
    <div className="receivingModeCreate">
         <form autoComplete="off" onSubmit={(e) => {addOrUpdate(e)}}>
           <Card style={styles.marginStyle}>
            <CardHeader style={{paddingBottom:0}} title={<div style = {styles.headerStyle} > {(this.props.match.params && this.props.match.params.type == "edit" ? "Edit " : "Create ") + "Receiving Mode"} </div>}/>
              <CardText style={{padding:0}}>
                 <Grid>
                   <Row>
                    <Col xs={12} md={3}>
                     <TextField
                        fullWidth={true}
                        floatingLabelText={translate("core.lbl.add.name")+"*"}
                        id="name"
                        errorText={fieldErrors.name ? fieldErrors.name : ""}
                        value={receivingmodeSet.name ? receivingmodeSet.name : "" }
                        onChange={(e) => {handleChange(e, "name", true, /^[a-zA-Z]+$/)}}/>
                    </Col>
                    <Col xs={12} md={3}>
                     <TextField
                        fullWidth={true}
                        floatingLabelText={translate("core.lbl.code")+"*"}
                        id="code"
                        errorText={fieldErrors.code ? fieldErrors.code : ""}
                        value={receivingmodeSet.code ? receivingmodeSet.code : ""}
                        onChange={(e) => {handleChange(e, "code", true, /^[a-zA-Z]+$/)}}
                        disabled={this.state.id ? true : false }/>
                    </Col>
                    <Col xs={12} md={3}>
                     <TextField
                        fullWidth={true}
                        floatingLabelText={translate("core.lbl.description")+"*"}
                        id="description"
                        errorText={fieldErrors.description ? fieldErrors.description : ""}
                        value={receivingmodeSet.description ? receivingmodeSet.description : ""}
                        onChange={(e) => {handleChange(e, "description", true, /^[a-zA-Z]+$/)}}/>
                    </Col>
                    <Col xs={12} md={3}>
                     <SelectField
                          multiple="true"
                          errorText={fieldErrors.description ? fieldErrors.description : ""}
                          value={receivingmodeSet.channel ? receivingmodeSet.channel : ""}
                          id="channel"
                          onChange={(e, index, value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             console.log(value);
                             handleChange(e, "channel", true, "")}}
                          floatingLabelText="Channel*" >
                              <MenuItem value={"WEB"} primaryText="WEB"/>
                              <MenuItem value={"MOBILE"} primaryText="MOBILE"/>
                     </SelectField>
                   </Col>
                    <div className="clearfix"></div>
                    <Col xs={12} md={3}>
                    <Checkbox label={translate("pgr.lbl.active")} id="active" style={styles.checkbox}
                      checked ={receivingmodeSet.active ? true : false}
                      onCheck={(e,isInputChecked) => {
                       var e={
                            "target":{
                              "value":isInputChecked
                            }
                          }
                          handleChange(e, "active", false, "")}}/>
                    </Col>
                   </Row>
                 </Grid>
              </CardText>
           </Card>
           <div style={{textAlign:'center'}}>
                <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label={ !_this.state.id ? 'Create' : 'Update'} backgroundColor={"#5a3e1b"} labelColor={white}/>
                <RaisedButton style={{margin:'15px 5px'}} label="Close" onClick={(e)=>{this.close()}}/>
              </div>
         </form>
         <Dialog
          title="Success"
          actions={[<FlatButton
				        label="Close"
				        primary={true}
				        onTouchTap={handleOpenNClose}
				      />]}
          modal={false}
          open={open}
          onRequestClose={handleOpenNClose}
        >
        Receiving Mode {this.props.match.params && this.props.match.params.type == "edit" ? "updated" : "created"} successfully.
        </Dialog>
        </div>
   );
  }
}



const mapStateToProps = state => {
  return ({receivingmodeSet: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText,isDialogOpen: state.form.dialogOpen, msg: state.form.msg});
}

const mapDispatchToProps = dispatch => ({  initForm: (type) => {
  dispatch({
    type: "RESET_STATE",
    validationData: {
      required: {
        current: [],
        required: ["name","code","description","channel"]
      },
      pattern: {
         current: [],
         required: ["name","code","description"]
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
          current: ["name","code","description","channel"],
          required:["name","code","description","channel"]
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
     dispatch({
       type: "HANDLE_CHANGE",
       property,
       value: e.target.value,
       isRequired, pattern
     });
   },
   setLoadingStatus: (loadingStatus) => {
     dispatch({type: "SET_LOADING_STATUS", loadingStatus});
   },
   toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
     dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
   }
   });



export default connect(mapStateToProps,mapDispatchToProps)(receivingModeCreate);
