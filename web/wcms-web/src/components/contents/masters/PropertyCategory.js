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


class PropertyCategory extends Component {

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
        let response=Api.commonApiPost("wcms-masters", "category", "_search", {id},{}).then((res)=>
        {
           this.setState({
             list: res.Category
         });

     },  (err)=> {
         alert(err);
     });

      let response2=Api.commonApiPost("property", "masters/propertytypes", "_search?", {id:id,tenantId:"ap.kurnool"},{},true).then((res)=>
     {
        this.setState({
          list2: res.masterModel
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

    let {changeButtonText,PropertyCategory}=this.props;
    var propertyCategory = {
      propertyTypeName:PropertyCategory.propertyTypeName,
      categoryTypeName:PropertyCategory.categoryTypeName,
      active:PropertyCategory.active,
      tenantId:'default'
    }
    if(type == "Update"){
      let response=Api.commonApiPost("wcms-masters", "property/category", "_update/"+id, {},{PropertyCategory:PropertyCategory}).then(function(response)
      {
      console.log(response);
    },function(err) {
        alert(err);
    });

    }

  else{
    let response=Api.commonApiPost("wcms-masters", "property/category", "_create", {},{PropertyCategory}).then(function(response)
    {
    // console.log(response);
  },function(err) {
      alert("Selected Property type and Category type already exists");
  });
}
}



    render(){
      let {
        PropertyCategory,
        propertyType,
        handleChange,
        fieldErrors,
      }=this.props;

      let{add}=this;
      let {search} = this;
      let mode=getUrlVars()["type"];

        //  console.log(mode);

      const showActionButton=function() {
        if((!mode) ||mode==="Update")
        {
          // console.log(mode);
          return(<RaisedButton type="submit" label={mode?"Save":"Save"} backgroundColor={brown500} labelColor={white}  onClick={()=> {
                               add("propertyTypeName","categoryTypeName","active")}} />
          )
        }
      };

      // console.log(PropertyCategory);

      const renderOption=function(list)
      {
        // console.log(list);

            if(list)
          {
              return list.map((item)=>
              {
                  return (<MenuItem key={item.id} value={item.id} primaryText={item.name}/>)
              })
          }
      }
      return(
        <div className="PropertyCategory">
            <Card>
                <CardHeader
                    title={<strong style={{color:brown500}}>Create Property Category </strong>}
                />
                <CardText>
                      <Card>
                          <CardText>
                              <Grid>

                                  <Row>
                                    <Col xs={12} md={6}>

                                      <SelectField  value={PropertyCategory.propertyTypeName?PropertyCategory.propertyTypeName:""} onChange={(event, index, value) => {
                                          var e = {
                                            target: {
                                              value: value
                                            }
                                          };
                                          handleChange(e, "propertyTypeName", false, "")}} floatingLabelText="Property Type" >
                                        <MenuItem value={1} primaryText=""/>
                                        {renderOption(this.state.list2)}
                                      </SelectField>
                                    </Col>

                                    <Col xs={12} md={6}>
                                      <SelectField value={PropertyCategory.categoryTypeName?PropertyCategory.categoryTypeName:""} onChange={(event, index, value) =>{
                                          var e = {
                                            target: {
                                              value: value
                                            }
                                          };
                                          handleChange(e, "categoryTypeName", false, "")}
                                        } floatingLabelText="Category Type" >
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
                                                       value={PropertyCategory.active?PropertyCategory.active:""}
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
const mapStateToProps = state => ({PropertyCategory: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});

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

export default connect(mapStateToProps, mapDispatchToProps)(PropertyCategory);
