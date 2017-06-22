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
    margin: 37
  }
};

var _this;

class receivingModeCreate extends Component {
  constructor(props) {
       super(props);
       this.state = {
         list:[],
         list2:[]
       }
       this.addOrUpdate=this.addOrUpdate.bind(this);
   }



  componentWillMount() {
    if(this.props.match.params.code) {
      let {receivingCenterCreate} = this.props;
       this.setState({code:this.props.match.params.code});
       var body = {}
       let  current = this;
       Api.commonApiPost("/pgr-master/receivingmode/_search",{code:this.state.code},body).then(function(response){
          console.log(response);
          this.setState({data:response.ReceivingModeType})
        }).catch((error)=>{
          console.log(error);
        })
       }
    }

  componentDidMount()
  {
    this.props.initForm();
    this.props.handleChange({
                            "target":{
                              "value":true
                            }}, "active", true, "");

  }


  addOrUpdate(e) {
    e.preventDefault();
    console.log(this.props.receivingmodeSet);

    let {changeButtonText,receivingmodeSet}=this.props;
    var body ={
      ReceivingModeType:{
         name :receivingmodeSet.name,
         code :receivingmodeSet.code,
         description :receivingmodeSet.description,
          active :receivingmodeSet.active,
         tenantId :localStorage.getItem("tenantId"),
         channel :receivingmodeSet.channel
       }
    }
      if (this.props.match.param.code) {
        var code = this.props.match.param.code;
            Api.commonApiPost("pgr-master/receivingmode/$code/_update", {},body).then(function(response) {
            alert("ReceivingMode Type Created successfully");
            receivingmodeSet:[]
          }).catch((error)=>{
            console.log(error);
          })
      } else {
          Api.commonApiPost("pgr-master/receivingmode/_create", {},body).then(function(response) {
          alert("ReceivingMode Type Created successfully");
          receivingmodeSet:[]
        }).catch((error)=>{
          console.log(error);
        })
      }


  }


  render() {
   let {search,addOrUpdate} = this;
   let {
     handleChange,
     handleChangeNextOne,
     handleChangeNextTwo,
     isFormValid,
     isTableShow,
     receivingmodeSet,
     fieldErrors} = this.props;


   return (
    <div className="receivingModeCreate">
         <form autoComplete="off" onSubmit={(e) => {addOrUpdate(e)}}>
           <Card style={styles.marginStyle}>
            <CardHeader style={{paddingBottom:0}} title={<div style = {styles.headerStyle} > Create ReceivingMode </div>}/>
              <CardText style={{padding:0}}>
                 <Grid>
                   <Row>
                    <Col xs={12} md={3}>
                     <TextField fullWidth={true} floatingLabelText="Name" errorText={fieldErrors.name} value={receivingmodeSet.name} onChange={(e) => {handleChange(e, "name", true, "")}}/>
                    </Col>
                    <Col xs={12} md={3}>
                     <TextField fullWidth={true} floatingLabelText="Code" errorText={fieldErrors.code} value={receivingmodeSet.code} onChange={(e) => {handleChange(e, "code", true, "")}}/>
                    </Col>
                    <Col xs={12} md={3}>
                     <TextField fullWidth={true} floatingLabelText="Description" errorText={fieldErrors.description} value={receivingmodeSet.description} onChange={(e) => {handleChange(e, "description", true, "")}}/>
                    </Col>
                    <Col xs={12} md={3}>
                     <SelectField  value={receivingmodeSet.channel} onChange={(e, index, value) => {
                         var e = {
                           target: {
                             value: value
                           }
                         };
                         handleChange(e, "channel", false, "")}} floatingLabelText="channel" >
                       <MenuItem value={1} primaryText=""/>
                       <MenuItem value={"WEB"} primaryText="WEB"/>
                       <MenuItem value={"MOBILE"} primaryText="MOBILE"/>
                     </SelectField>
                   </Col>

                    <Col xs={12} md={3}>
                    <Checkbox label="Active" style={styles.checkbox}
                    defaultChecked={true}
                    value={receivingmodeSet.active} onCheck={(e,isInputChecked) => { var e={
                                            "target":{
                                              "value":isInputChecked
                                            }
                                          }
                                          handleChange(e, "active", true, "")}}/>
                    </Col>
                   </Row>

                 </Grid>
              </CardText>
           </Card>
           <div style={{textAlign: 'center'}}>
             <RaisedButton style={{margin:'15px 5px'}} type="submit" label="Create" disabled={!isFormValid} backgroundColor={"#5a3e1b"} labelColor={white}/>
             <RaisedButton style={{margin:'15px 5px'}} label="Close"/>
           </div>
         </form>
        </div>
   );
  }
}



const mapStateToProps = state => {
  return ({receivingmodeSet: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
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
         required: []
       }
      }
    });
  },

   handleChange: (e, property, isRequired, pattern) => {
     dispatch({
       type: "HANDLE_CHANGE",
       property,
       value: e.target.value,
       isRequired, pattern
     });
   },
   changeButtonText:(text) => {
     dispatch({type: "BUTTON_TEXT", text});
   }});



export default connect(mapStateToProps,mapDispatchToProps)(receivingModeCreate);
