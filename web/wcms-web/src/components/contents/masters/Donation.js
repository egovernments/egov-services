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


class Donation extends Component {

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

    let {changeButtonText,Donation}=this.props;
    var donation = {
      applicationType:Donation.applicationType,
      documentTypeId:Donation.documentTypeId,
      active:Donation.active,
      tenantId:'default'
    }
    if(type == "Update"){
      let response=Api.commonApiPost("wcms-masters", "donation", "_update/"+id, {},{donation:donation}).then(function(response)
      {
    },function(err) {
        alert(err);
    });

    }

  else{
    let response=Api.commonApiPost("wcms-masters", "donation", "_create", {},{Donation:donation}).then(function(response)
    {
  },function(err) {
    if(!donation.applicationType && donation.documentTypeId){
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
        Donation,
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
                    title={<strong style={{color:brown500}}>Create Donation </strong>}
                />
                <CardText>
                      <Card>
                          <CardText>
                              <Grid>

                                  <Row>
                                    <Col xs={12} md={6}>

                                      <SelectField  value={Donation.applicationType?Donation.applicationType:""} onChange={(event, index, value) => {
                                          var e = {
                                            target: {
                                              value: value
                                            }
                                          };
                                          handleChange(e, "applicationType", false, "")}} floatingLabelText="Property Type" >
                                        <MenuItem value={1} primaryText=""/>
                                        {renderOption(this.state.list2)}
                                      </SelectField>
                                    </Col>

                                    <Col xs={12} md={6}>
                                      <SelectField value={Donation.documentTypeId?Donation.documentTypeId:""} onChange={(event, index, value) =>{
                                          var e = {
                                            target: {
                                              value: value
                                            }
                                          };
                                          handleChange(e, "documentTypeId", false, "")}
                                        } floatingLabelText="Category " >
                                         <MenuItem value={1} primaryText=""/>
                                         {renderOption(this.state.list)}
                                      </SelectField>
                                    </Col>
                                  </Row>
                                  <Row>
                                  <Col xs={12} md={6}>
                                    <SelectField value={Donation.documentTypeId?Donation.documentTypeId:""} onChange={(event, index, value) =>{
                                        var e = {
                                          target: {
                                            value: value
                                          }
                                        };
                                        handleChange(e, "documentTypeId", false, "")}
                                      } floatingLabelText="UsageType " >
                                       <MenuItem value={1} primaryText=""/>
                                       {renderOption(this.state.list)}
                                    </SelectField>
                                  </Col>
                                  <Col xs={12} md={6}>
                                    <SelectField value={Donation.documentTypeId?Donation.documentTypeId:""} onChange={(event, index, value) =>{
                                        var e = {
                                          target: {
                                            value: value
                                          }
                                        };
                                        handleChange(e, "documentTypeId", false, "")}
                                      } floatingLabelText="Max H.S.C Pipe SIze " >
                                       <MenuItem value={1} primaryText=""/>
                                       {renderOption(this.state.list)}
                                    </SelectField>
                                  </Col>

                                  </Row>
                                  <Row>
                                  <Col xs={12} md={6}>
                                    <SelectField value={Donation.documentTypeId?Donation.documentTypeId:""} onChange={(event, index, value) =>{
                                        var e = {
                                          target: {
                                            value: value
                                          }
                                        };
                                        handleChange(e, "documentTypeId", false, "")}
                                      } floatingLabelText="Min H.S.C Pipe SIze " >
                                       <MenuItem value={1} primaryText=""/>
                                       {renderOption(this.state.list)}
                                    </SelectField>
                                  </Col>
                                  <Col xs={12} md={6}>
                                    <TextField value={Donation.ownerName?Donation.ownerName:""} onChange={(e) => handleChange(e, "ownerName", false, "")} hintText="Category Type" floatingLabelText="Donation" />
                                  </Col>

                                  </Row>
                                  <Row>
                                  <Col xs={12} md={6}>
                                            <DatePicker hintText="From Date " container="inline" />
                                        </Col>
                                        <Col xs={12} md={6}>
                                                  <DatePicker hintText="Effective To " container="inline" />
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
const mapStateToProps = state => ({Donation: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});

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

export default connect(mapStateToProps, mapDispatchToProps)(Donation);
