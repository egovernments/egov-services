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
import DatePicker from 'material-ui/DatePicker';
import Checkbox from 'material-ui/Checkbox';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import Api from '../../../api/wCAPIS';


const $ = require('jquery');

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

class DocumentType extends Component {
  constructor(props) {
       super(props);
       this.state = {
         searchBtnText : 'Search'
       }
       this.add=this.add.bind(this);
   }


  componentDidMount()
  {
    let {initForm,setForm} = this.props;
    initForm();
    var _this=this;



       var type=getUrlVars()["type"];
        var id=getUrlVars()["id"];

        if(getUrlVars()["type"]==="View")
        {
            $("input,select,textarea").prop("disabled", true);
          }

          if(type==="Update"||type==="View")
          {
            let response=Api.commonApiPost("wcms-masters", "documenttype", "_search", {id},{}).then((res)=>
           {
            //   this.setState({
            //     list: res.documentTypes
            // });
            console.log(res.documentTypes[0]);
              setForm(res.documentTypes[0]);


        },  (err)=> {
            alert(err);
        })
      }
}


  close(){
        // widow.close();
        open(location, '_self').close();
    }



  add(e)
  {
    var type=getUrlVars()["type"];
    var id=getUrlVars()["id"];

    // let mode=getUrlVars()["type"];

      let {changeButtonText,documenttype}=this.props;
      var DocumentType = {
        name:documenttype.ownerName,
        description:documenttype.Description,
        active:documenttype.active,
        tenantId:'default'
      }
      if(type == "Update"){
        let response=Api.commonApiPost("wcms-masters", "documenttype/"+id, "_update", {},{DocumentType:DocumentType}).then(function(response)
        {
        console.log(response);
      },function(err) {
          alert(err);
      });

      }

    else{
      let response=Api.commonApiPost("wcms-masters", "documenttype", "_create", {},{DocumentType}).then(function(response)
      {
      // console.log(response);
      alert("Document Type create successfully")
    },function(err) {
      if(!DocumentType.name){
        alert("Please enter Document type");

      }
      else{
        alert("Entered Document type already exists");
    }
  });
}
    }





  render() {
    let {
      documenttype,
      fieldErrors,
      isFormValid,
      handleChange,
      buttonText,

    } = this.props;
    let{add}=this;
    let {search} = this;
    let mode=getUrlVars()["type"];

       console.log(mode);

    const showActionButton=function() {
      if((!mode) ||mode==="Update")
      {
        // console.log(mode);
        return(<RaisedButton type="submit" label={mode?"Save":"Save"} backgroundColor={brown500} labelColor={white}  onClick={()=> {
                             add("name","description","active")}} />
        )
      }
    };
        return (
          <div className="documenttype">
          <Card>
            <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Create Document Type< /strong>}/>

            <CardText>
              <Card>
                <CardText>
                  <Grid>
                    <Row>
                    <Col xs={12} md={6}>
                      <TextField errorText={fieldErrors.ownerName
                        ? fieldErrors.ownerName
                        : ""} value={documenttype.ownerName?documenttype.ownerName:""} onChange={(e) => handleChange(e, "ownerName", false, "")} hintText="Name" floatingLabelText="Name" />
                    </Col>

                    <Col xs={12} md={6}>
                      <TextField errorText={fieldErrors.Descrption
                        ? fieldErrors.Description
                        : ""} value={documenttype.Description?documenttype.Description:""} multiLine={true} onChange={(e) => handleChange(e, "Description", false, "")} hintText="Description" floatingLabelText="Description" />
                    </Col>
                    </Row>
                    <Row>
                    <Col xs={12} md={6}>
                                        <Checkbox
                                         label="Active"
                                         defaultChecked={true}
                                         value={documenttype.active?documenttype.active:""}
                                         onCheck={(event,isInputChecked) => {
                                           var e={
                                             "target":{
                                               "value":isInputChecked
                                             }
                                           }
                                           handleChange(e, "active", true, "")}
                                         }
                                         style={styles.checkbox}
                                         style={styles.topGap}
                                        />
                          </Col>
                        </Row>

                    </Grid>
                  </CardText>
              </Card>





              <div style={{
                textAlign: "center"
              }}>

               {showActionButton()}
              <RaisedButton label="Close" onClick={(e)=>{this.close()}}/>
              </div>
            </CardText>
          </Card>
          </div>
    );
  }
}

const mapStateToProps = state => ({documenttype: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,buttonText:state.form.buttonText});

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
          required: ["ownerName",]
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
          required: ["ownerName"]
        }
      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  },

  showTable:(state)=>
  {
    dispatch({type:"SHOW_TABLE",state});
  },
  changeButtonText:(text)=>
  {
    dispatch({type:"BUTTON_TEXT",text});
  }


});

export default connect(mapStateToProps, mapDispatchToProps)(DocumentType);
