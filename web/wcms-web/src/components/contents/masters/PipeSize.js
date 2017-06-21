function getUrlVars() {
   var vars = [],
       hash;
   var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
   for (var i = 0; i < hashes.length; i++) {
       hash = hashes[i].split('=');
       vars.push(hash[0]);
       vars[hash[0]] = hash[1];
   }
   return vars;
}
import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import Checkbox from 'material-ui/Checkbox';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import Api from '../../../api/wCAPIS';
import App from '../../App';



const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');

// const buttons = require('datatables.net-buttons-bs');

require('datatables.net-buttons/js/buttons.colVis.js'); // Column visibility
require('datatables.net-buttons/js/buttons.html5.js'); // HTML 5 file export
require('datatables.net-buttons/js/buttons.flash.js'); // Flash file export
require('datatables.net-buttons/js/buttons.print.js'); // Print view button

var flag = 0;
const styles = {
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
  }
};

class PipeSize extends Component {
  constructor(props) {
       super(props);
       this.state = {
         searchBtnText : 'Search'
       }
       this.add=this.add.bind(this);
       this.handleChangeState = this.handleChangeState.bind(this);
   }


  componentDidMount()
  {
    let {initForm,setForm} = this.props;
    initForm();
    var _this=this;



       var type=getUrlVars()["type"];
        var id=getUrlVars()["id"];

        // if(getUrlVars()["type"]==="View")
        // {
        //     $("input,select,textarea,TextField").prop("disabled", true);
        //   }

          if(type==="Update"||type==="View")
          {
            let response=Api.commonApiPost("wcms-masters", "pipesize", "_search", {id},{}).then((res)=>
           {
             console.log(res.PipeSize[0]);
               setForm(res.PipeSize[0]);

            //   this.setState({.PipeSize[0]
            //     list: res.PipeSize
            // });

        },  (err)=> {
            alert("Please Enter the details");
        })
      }


  }

  add(e)
  {
    var type=getUrlVars()["type"];
    var id=getUrlVars()["id"];
    let {changeButtonText,pipeSize,toggleDailogAndSetText}=this.props;
    var PipeSize = {
        sizeInMilimeter:pipeSize.sizeInMilimeter,
        description:pipeSize.Description,
        sizeInInch:pipeSize.sizeInInch,
        active:pipeSize.Active,
        tenantId:'default'
    }
      if(type == "Update"){

        let response=Api.commonApiPost("wcms-masters", "pipesize/"+id, "_update", {},{PipeSize:PipeSize}).then(function(response)
        {
        // console.log(response);
        alert("Pipe Size Updated Successfully")
      },function(err) {
        if(!PipeSize.sizeInMilimeter){
          alert("Please Enter PipeSize in mm");
        }
        else{
          alert("Enetered Pipe Size already exist");
        }
      });

      }

    else{
      let response=Api.commonApiPost("wcms-masters", "pipesize", "_create", {},{PipeSize}).then(function(response)
      {
      // console.log(response);
       alert("Pipe Size Created Successfully");
      // toggleDailogAndSetText(true," done");
    },function(err) {
       console.log(response);
        alert("Pipe Size enetered in mm already exist");

       toggleDailogAndSetText(true,"Not Done");
    });
  }
    }





  handleChangeState(e, name, isRequired, pattern) {
    this.props.handleChange(e, name, isRequired, pattern);
    var inches = e.target.value * 0.039;
    this.props.handleChange({target:{value: inches}}, "sizeInInch", false, "");
  }

  render() {
    let {
      pipeSize,
      fieldErrors,
      // isFormValid,
      handleChange,
      toggleDailogAndSetText,

    } = this.props;
    let {add, handleChangeState} = this;
    let mode=getUrlVars()["type"];
    // console.log(pipeSize);
    const showActionButton=function() {
      if((!mode) ||mode==="Update")
      {
        // console.log(mode);
        return(<RaisedButton type="submit" label={mode?"Save":"Save"} backgroundColor={brown500} labelColor={white}  onClick={()=> {
                             add("sizeInMilimeter","sizeInInch","active","description")}} />
        )
      }
    };
    //  console.log(pipeSize);
    //  let type=getUrlVars()["type"]);
        return (
      <div className="pipeSize">
          <Card>
            <CardHeader title={< strong style = {{color:"#5a3e1b"}} >  Pipe Size < /strong>}/>

            <CardText>
              <Card>
                <CardText>
                  <Grid>
                    <Row>
                    <Col xs={12} md={6}>
                      <TextField errorText="This field is required." value={pipeSize.sizeInMilimeter?pipeSize.sizeInMilimeter:""}  onChange={(e) =>{ handleChangeState(e, "sizeInMilimeter", false, "");


                      } } hintText="123456" floatingLabelText="H.S.C Pipe Size (mm)" />
                    </Col>

                    <Col xs={12} md={6}>
                      <TextField  disabled={true} errorText={fieldErrors.sizeInInch
                        ? fieldErrors.sizeInInch
                        : ""} value={pipeSize.sizeInInch?pipeSize.sizeInInch:""} onChange={(e) => handleChange(e, "sizeInInch", false, "")}  floatingLabelText="H.S.C Pipe Size (inch)" />
                    </Col>
                    </Row>
                    <Row>

                    <Col xs={12} md={6}>
                                        <Checkbox
                                         label="Active"
                                         defaultChecked={true}
                                         value={pipeSize.active?pipeSize.active:""}
                                         onCheck={(event,isInputChecked) => {
                                           var e={
                                             "target":{
                                               "value":isInputChecked
                                             }
                                           }
                                           handleChange(e, "Active", false, "")}
                                         }
                                         style={styles.checkbox}
                                         style={styles.topGap}
                                        />
                          </Col>
                          <Col xs={12} md={6}>
                            <TextField errorText={fieldErrors.Descrption
                              ? fieldErrors.Description
                              : ""} value={pipeSize.Description?pipeSize.Description:""} multiLine={true} onChange={(e) => handleChange(e, "Description", false, "")} hintText="Description" floatingLabelText="Description" />
                          </Col>
                        </Row>

                    </Grid>
                  </CardText>
              </Card>





              <div style={{
                textAlign: "center"
              }}>


              {showActionButton()}
              <RaisedButton label="Close"/>
              </div>
            </CardText>
          </Card>






      </div>
    );
  }
}

const mapStateToProps = state => ({pipeSize: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,buttonText:state.form.buttonText});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: [ ]
        },
        pattern: {
          current: [],
          required: ["sizeInMilimeter"]
        }
      }
    });
  },
  setForm: (form) => {
    dispatch({
      type: "SET_FORM",
      data:form,
      validationData: {
        required: {
          current: [],
          required: [ ]
        },
        pattern: {
          current: [],
          required: ["sizeInMilimeter"]
        }
      }
    });
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({
      type: "TOGGLE_DAILOG_AND_SET_TEXT",
      dailogState,
      msg
    })
},

  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});


  },

});

export default connect(mapStateToProps, mapDispatchToProps)(PipeSize);
