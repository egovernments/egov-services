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
import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Grid,Row,Col,Table } from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500,red500, white} from 'material-ui/styles/colors';
import Checkbox from 'material-ui/Checkbox';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import Api from '../../../api/wCAPIS';

const styles = {
    errorStyle: {
      color: red500,
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
    },
};


class DocumentTypeApplicationType extends Component {

  constructor(props) {
       super(props);
       this.state = {
         list:[]
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

        // if(getUrlVars()["type"]==="View")
        // {
        //     $("input,select,textarea").prop("disabled", true);
        //   }
        let response=Api.commonApiPost("wcms-masters", "documenttype", "_search", {id},{}).then((res)=>
        {
           this.setState({
             list: res.documentTypes
         });

     },  (err)=> {
         alert(err);
     });

      let response2=Api.commonApiPost("wcms-masters", "master", "_getapplicationtypes", {id},{}).then((res)=>
     {

        this.setState({
          list2: res
      });

  },  (err)=> {
      alert(err);
  })

}

add(e)
{
  var type=getUrlVars()["type"];
  var id=getUrlVars()["id"];

  // let mode=getUrlVars()["type"];

    let {changeButtonText,DocumentTypeApplicationType}=this.props;
    var documentTypeApplicationType = {
      applicationType:DocumentTypeApplicationType.applicationType,
      documentTypeId:DocumentTypeApplicationType.documentTypeId,
      active:DocumentTypeApplicationType.active,
      tenantId:'default'
    }
    if(type == "Update"){
      let response=Api.commonApiPost("wcms-masters", "documenttype-applicationtype/"+id, "_update", {},{documentTypeApplicationType:documentTypeApplicationType}).then(function(response)
      {
    },function(err) {
        alert(err);
    });

    }

  else{
    let response=Api.commonApiPost("wcms-masters", "documenttype-applicationtype", "_create", {},{DocumentTypeApplicationType:documentTypeApplicationType}).then(function(response)
    {
      alert("Document Type Application Type Create successfully");
  },function(err) {
    if(!documentTypeApplicationType.applicationType && documentTypeApplicationType.documentTypeId){
      alert("Please Select Application type and Document Type");
    }
    else{
      alert("Selected Document type and Application type already exist");
    }
  });
}
}



    render(){
      let {
        DocumentTypeApplicationType,
        handleChange,
        fieldErrors,
      }=this.props;

      let{add}=this;
      let {search} = this;
      let mode=getUrlVars()["type"];


      const showActionButton=function() {
        if((!mode) ||mode==="Update")
        {
          return(<RaisedButton type="submit" label={mode?"Save":"Save"} backgroundColor={brown500} labelColor={white}  onClick={()=> {
                               add("applicationType","documentTypeId","active")}} />
          )
        }
      };


      const renderOption=function(list)
      {

            if(list)
            {
              if(list.length){
                return list.map((item)=>
                {
                    return (<MenuItem key={item.id} value={item.id} primaryText={item.name} />)
                })
              }


              else {
                return Object.keys(list).map((k,index)=>
                 {
                   return (<MenuItem key={index} value={k} primaryText={list[k]}
                     />)

                 })
                }
              }

      }

      return(
        <div className="DocumentTypeApplicationType">
            <Card>
                <CardHeader
                    title={<strong style={{color:brown500}}>Create DocumentType Application Type </strong>}
                />
                <CardText>
                      <Card>
                          <CardText>
                              <Grid>

                                  <Row>
                                    <Col xs={12} md={6}>

                                      <SelectField  value={DocumentTypeApplicationType.applicationType?DocumentTypeApplicationType.applicationType:""} onChange={(event, index, value) => {
                                          var e = {
                                            target: {
                                              value: value
                                            }
                                          };
                                          handleChange(e, "applicationType", false, "")}} floatingLabelText="Application Type" >
                                        <MenuItem value={1} primaryText=""/>
                                        {renderOption(this.state.list2)}
                                      </SelectField>
                                    </Col>

                                    <Col xs={12} md={6}>
                                      <SelectField value={DocumentTypeApplicationType.documentTypeId?DocumentTypeApplicationType.documentTypeId:""} onChange={(event, index, value) =>{
                                          var e = {
                                            target: {
                                              value: value
                                            }
                                          };
                                          handleChange(e, "documentTypeId", false, "")}
                                        } floatingLabelText="Document Type" >
                                         <MenuItem value={1} primaryText=""/>
                                         {renderOption(this.state.list)}
                                      </SelectField>
                                    </Col>
                                  </Row>
                                  <Row>
                                  <Col xs={12} md={6}>
                                                      <Checkbox
                                                       label="Active"
                                                       defaultChecked={true}
                                                       value={DocumentTypeApplicationType.active?DocumentTypeApplicationType.active:""}
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


                      <div style={{textAlign:"center"}}>
                      {showActionButton()}
                      <RaisedButton type="button" label="Close" />
                      </div>
                </CardText>
            </Card>
        </div>
      )
    }

}
const mapStateToProps = state => ({DocumentTypeApplicationType: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});

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
          required: ["name"]
        }
      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  }



});

export default connect(mapStateToProps, mapDispatchToProps)(DocumentTypeApplicationType);
