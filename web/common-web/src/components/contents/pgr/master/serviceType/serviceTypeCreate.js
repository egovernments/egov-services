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

class ServiceTypeCreate extends Component {
    constructor(props) {
      super(props);
      this.state = {
          id:'',
          data:'',
          nomineeFieldsDefination: [],
          isCustomFormVisible: true,
          showMsg:true,
          category:[],
          isCustomFormVisible:false,
          assetFieldsDefination: [],
          open:false,
          editIndex: -1,
          isDataType: false
      }
      this.showCustomFieldForm=this.showCustomFieldForm.bind(this);
      this.addAsset = this.addAsset.bind(this);
      this.renderDelEvent = this.renderDelEvent.bind(this);
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

            Api.commonApiPost("/pgr-master/service/v1/_search",{id:this.props.match.params.id},body).then(function(response){
                console.log("response",response);
                  console.log("response object",response.Service[0]);
                current.setState({data:response.Service})
                setForm(response.Service[0])
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
        let self = this;
      Api.commonApiPost("/pgr-master/serviceGroup/v1/_search").then(function(response) {
        console.log("response",response);
          self.setState({
            category: response.ServiceGroups
          })
      }, function(err) {
        self.setState({
            category: []
          })
      });
    }

    componentDidUpdate() {


    }


    addAsset(to="") {
      alert("hi");
      var {
        isEdit,
        index,
        list,
        customField,
        column,
        assetCategory
      } = this.state;

      if(!to && (!customField.name || !customField.type)) {
        return this.setState({
          showMsg: true,
          readonly: false
        })
      } else {
        var _this = this;
        setTimeout(function() {
          _this.setState({
            showMsg: false,
            readonly: false
          });
        }, 300);
      }

      if (isEdit) {
        // console.log(isEdit,index);
        //update holidays with current holiday
        assetCategory["assetFieldsDefination"][index] = customField
        this.setState({
          assetCategory,
          isEdit: false,
          readonly: false
        })
        //this.setState({isEdit:false})
      } else {
        //get asset Category from state
        // customFieldData["columns"]=[];

        if (to=="column") {
          var temp = customField;
          temp.columns.push(column);
          this.setState({
            customField: temp,
            readonly: false,
            column:{
                 "name": null,
                 "type": null,
                 "isActive": false,
                 "isMandatory": false,
                 "values": null,
                 "localText": null,
                 "regExFormate": null,
                 "url": null,
                 "order": null,
                 "columns": []
            }
          })
        } else {
          var temp = Object.assign({}, assetCategory);
          temp.assetFieldsDefination.push(customField);
          this.setState({
            assetCategory: temp,
            readonly: false,
            customField: {
                 "name": null,
                 "type": null,
                 "isActive": false,
                 "isMandatory": false,
                 "values": null,
                 "localText": null,
                 "regExFormate": null,
                 "url": null,
                 "order": null,
                 "columns": []
            } ,
            isCustomFormVisible:false,
            column: {
                 "name": null,
                 "type": null,
                 "isActive": false,
                 "isMandatory": false,
                 "values": null,
                 "localText": null,
                 "regExFormate": null,
                 "url": null,
                 "order": null,
                 "columns": []
            }
          })
        }

        //use push to add new customField inside assetCategory
        //set back assetCategory to state
      }
    }



    submitForm = (e) => {

      e.preventDefault()
      var current = this;
      console.log(this.props);
      var body = {
          "Service":{
           "serviceCode": this.props.createServiceType.serviceCode,
           "serviceName" :this.props.createServiceType.serviceName,
           "description" :this.props.createServiceType.description,
           "active" :this.props.createServiceType.active,
           "type" :this.props.createServiceType.type,
           "keywords" :this.props.createServiceType.keywords,
           "group" :this.props.createServiceType.group,
           "category" :this.props.createServiceType.category,
           "hasFinancialImpact" :this.props.createServiceType.hasFinancialImpact,
           "attributes" :this.props.createServiceType.attributes,
           "slaHours" : this.props.createServiceType.slaHours,
           "tenantId":"default"
          }
      }
      console.log("body",body);

      if(this.props.match.params.id){
        console.log("hi");
          Api.commonApiPost("/pgr-master/service/v1/"+body.Service.serviceCode+"/_update",{},body).then(function(response){
              console.log(response);
              current.setState({
                open: true
              });
          }, function(err) {
            current.props.toggleSnackbarAndSetText(true, err.message);
            current.props.setLoadingStatus('hide');
        	})
      } else {
          Api.commonApiPost("/pgr-master/service/v1/_create",{},body).then(function(response){
              console.log(response);
              current.setState({
                open: true
              });
          }, function(err) {
            current.props.toggleSnackbarAndSetText(true, err.message);
            current.props.setLoadingStatus('hide');
        	})
      }


    }

    renderDelEvent(index,to="") {
      if (to==="column") {
        var columns = this.state.customField.columns;
        columns.splice(index, 1);
        this.setState({
          customField:{
            ...this.state.customField,
            columns
          }
        });
      } else {
        var assetFieldsDefination = this.state.assetCategory.assetFieldsDefination;
        assetFieldsDefination.splice(index, 1);
        this.setState({
          assetFieldsDefination
        });
      }
    }
    showCustomFieldForm(isShow)
    {
      this.setState({isCustomFormVisible:isShow})
    }

    handleClose = () => {
      this.setState({open: false});
    };


    render() {

      var _this = this;

      let {
        createServiceType ,
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

      let {submitForm,showCustomFieldForm,renderDelEvent,addAsset,handleOpenNClose} = this;
      let {nomineeFieldsDefination,isCustomFormVisible,showMsg,customField,assetFieldsDefination,isDataType, editIndex} = this.state;


      const viewTypes = function() {
        console.log("createServiceType",createServiceType);
          if( (createServiceType.dataType!=2) && (createServiceType.dataType!=undefined))
          return (
<div>
                <div className="clearfix"></div>
                  <Row>
                    <Col xs={12} md={3} sm={6}>
                        <TextField
                            fullWidth={true}
                            floatingLabelText="Key"
                            value={createServiceType.dataTypes ? createServiceType.dataTypes.attributesKey : ""}
                            errorText={fieldErrors.dataTypes ? fieldErrors.dataTypes.attributesKey : ""}
                              onChange={(e) => handleChangeNextOne(e,"dataTypes" ,"attributesKey", false, "")}
                            id="attributesKey"
                        />
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <TextField
                            fullWidth={true}
                            floatingLabelText="Name"
                            value={createServiceType.dataTypes ? createServiceType.dataTypes.attributesName : ""}
                            errorText={fieldErrors.dataTypes ? fieldErrors.dataTypes.attributesName : ""}
                              onChange={(e) => handleChangeNextOne(e,"dataTypes" ,"attributesName", false, "")}
                            id="attributesName"
                        />
                    </Col>
                    <Col xs={12}  md={3} sm={6} style={{textAlign:"center"}}>
                      {editIndex<0 && <RaisedButton style={{margin:'15px 5px'}}  label={translate("pgr.lbl.add")} backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
                        _this.props.addNestedFormDataTwo("attributes","attributes",'dataTypes');
                        _this.props.resetObject("dataTypes");
                      }}/>}
                      {editIndex>=0 && <RaisedButton style={{margin:'15px 5px'}}  label={translate("pgr.lbl.update")} backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
                        //updateEscalation();
                      }}/>}
                    </Col>
                  </Row>


              <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
                <thead style={{backgroundColor:"#f2851f",color:"white"}}>
                  <tr>
                    <th>No.</th>
                    <th>Key</th>
                    <th>Value</th>
                    <th></th>
                  </tr>
                </thead>
                <tbody>
                  {renderBody()}
                </tbody>
              </Table>
        </div>
      )
      }

    const addDropdown = function() {
        if(createServiceType.dataType==1) {
        return (
          <div>
            <div className="clearfix"></div>
            <Col xs={12} md={3} sm={6}>
                <TextField
                    fullWidth={true}
                    floatingLabelText="Key"
                    value={createServiceType.attributesKey? createServiceType.attributesKey : ""}
                    errorText={fieldErrors.attributesKey ? fieldErrors.attributesKey : ""}
                      onChange={(e) => handleChange(e, "attributesKey", false, '')}
                    id="attributesKey"
                />
            </Col>
            <Col xs={12} md={3} sm={6}>
                <TextField
                    fullWidth={true}
                    floatingLabelText="Name"
                    value={createServiceType.attributesName? createServiceType.attributesName : ""}
                    errorText={fieldErrors.attributesName ? fieldErrors.attributesName : ""}
                      onChange={(e) => handleChange(e, "attributesName", false, '')}
                    id="attributesName"
                />
            </Col>
          <Col xs={12} md={3} sm={6}>
          <button type="button" className="btn btn-primary " onClick={()=>{addCustomOption(true)}}>Add Option</button>
          <span>" "</span><button type="button" className="btn btn-primary  " onClick={()=>{showCustomFieldForm(true)}}>delete Option</button>
          </Col>
          </div>
        )
      }
      }

      const addCustomOption = function() {
        return (
          <div>
            HI
          </div>
        )
      }


      const showAddNewBtn = function() {

          return (

              <button type="button" className="btn btn-primary  pull-right" onClick={()=>{showCustomFieldForm(true)}}>Add New</button>
            )

      }

      const renderBody=function(to="")
      {
          if (to=="column") {
            if(customField.columns.length>0) {
                return customField.columns.map((item,index)=> {
                    return (<tr  key={index} className="text-center">
                    <td>{index+1}</td>
                      <td  >
                    {item.name}
                      </td>
                      <td  >
                        {item.type}
                      </td>
                      <td  >
                    {item.isActive?"true":"false"}
                      </td>
                      <td  >
                    {item.isMandatory?"true":"false"}
                      </td>
                      <td  >
                    {item.values}
                      </td>
                      <td  >
                    {item.order}
                      </td>

                    {/*  <td  >
                    {item.columns.length>0?item.columns.length:""}
                      </td>
                    */}

                      <td data-label="Action">
                                  <button type="button" className="btn btn-default btn-action" onClick={(e)=>{renderDelEvent(index,"column")}} ><span className="glyphicon glyphicon-trash"></span></button>
                    </td></tr>)
                })
            }
          } else {
            if(assetFieldsDefination.length>0) {
                return assetFieldsDefination.map((item,index)=> {
                    return (<tr  key={index} className="text-center">
                    <td>{index+1}</td>
                      <td  >
                    {item.name}
                      </td>
                      <td  >
                        {item.type}
                      </td>
                      <td>
                    {item.isActive?"true":"false"}
                      </td>
                      <td  >
                    {item.isMandatory?"true":"false"}
                      </td>
                      <td  >
                    {item.values}
                      </td>

                      <td  >
                    {item.order}
                      </td>

                      <td  >
                    {item.columns.length>0?item.columns.length:""}
                      </td>

                      <td data-label="Action">
                      <button type="button" className="btn btn-default btn-action" onClick={(e)=>{renderDelEvent(index)}} ><span className="glyphicon glyphicon-trash"></span></button>
                    </td></tr>)
                })
            }
          }


      }


      const  promotionFunc =function() {
          if(createServiceType.attributes=="true"||createServiceType.attributes==true){
            console.log("hi");
            return (
              <div className="form-section">
                <h3 style = {styles.headerStyle} >Attributes</h3>
                <div className="row" style={{"paddingRight": "18px"}}>
                  {showAddNewBtn()}
                </div>
                <div className="land-table table-responsive">
                    <table className="table table-bordered">
                        <thead>
                        <tr>
                          <th>Code</th>
                           <th>Datatype</th>
                           <th>Description</th>
                           <th>Variable</th>
                           <th>Required</th>


                         </tr>
                        </thead>
                        <tbody>
                          {renderBody()}
                        </tbody>
                    </table>
                </div>
                {showCustomFieldAddForm()}
              </div>

            )
          }
        };
        const showCustomFieldAddForm=function()
        {
            if(isCustomFormVisible)
            {
              return (
                <div>
                  <Col xs={12} md={3} sm={6}>
                      <TextField
                          fullWidth={true}
                          floatingLabelText="Code"
                          value={createServiceType.code? createServiceType.code : ""}
                          errorText={fieldErrors.code ? fieldErrors.code : ""}
                            onChange={(e) => handleChange(e, "code", false, '')}
                          id="code"
                      />
                  </Col>


                    <Col xs={12} md={3} sm={6}>
                      <TextField
                          fullWidth={true}
                          floatingLabelText="Description"
                          value={createServiceType.description? createServiceType.description : ""}
                          errorText={fieldErrors.description ? fieldErrors.description : ""}
                          onChange={(e) => handleChange(e, "description", false, '')}
                          multiLine={true}
                          id="description"
                      />
                  </Col>
                  <Col xs={12} md={3} sm={6}>
                      <TextField
                          fullWidth={true}
                          floatingLabelText="Group Code"
                          value={createServiceType.groupCode? createServiceType.groupCode : ""}
                          errorText={fieldErrors.groupCode ? fieldErrors.groupCode : ""}
                          onChange={(e) => handleChange(e, "groupCode", false, '')}
                          multiLine={true}
                          id="groupCode"
                      />
                  </Col>
                  <Col xs={12} md={3} sm={6}>
                      <Checkbox
                        label="Required"
                        style={styles.required}
                        checked = {createServiceType.required || false}
                        onCheck = {(e, i, v) => { console.log(createServiceType.required, i);

                          var e = {
                            target: {
                              value:i
                            }
                          }
                          handleChange(e, "required", false, '')
                        }}
                        id="required"
                      />
                  </Col>
                  <Col xs={12} md={3} sm={6}>
                      <Checkbox
                        label="Variable"
                        style={styles.variable}
                        checked = {createServiceType.variable || false}
                        onCheck = {(e, i, v) => { console.log(createServiceType.variable, i);

                          var e = {
                            target: {
                              value:i
                            }
                          }
                          handleChange(e, "variable", false, '')
                        }}
                        id="variable"
                      />
                  </Col>
                    <div className="clearfix"></div>
                  <Col  xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText="Data Type"
                           fullWidth={true}
                           value={createServiceType.dataType ? createServiceType.dataType : ""}
                           onChange= {(e, index ,values) => {
                             var e = {
                               target: {
                                 value: values
                               }
                             };
                             handleChange(e, "dataType", false, "");
                            }}
                         >
                        <MenuItem value={1} primaryText={"Single value list"} />
                        <MenuItem  value={2} primaryText={"Text"} />


                      </SelectField>
                  </Col>
                    {viewTypes()}
          <div className="clearfix"></div>
                  <div className="text-center">
                    <button type="button" className="btn btn-primary" onClick={(e)=>{addAsset()}} >Add/Edit</button>
                  </div>

                </div>
              )
            }
        }
        const showNoteMsg = function() {
          if(showMsg) {
            return (<p className="text-danger">ALl mandatory field are required.</p>)
          } else
            return "";
        }



      return(
        <div className="createServiceType">
          <form autoComplete="off" onSubmit={(e) => {submitForm(e)}}>
              <Card style={styles.marginStyle}>
                  <CardHeader  style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {this.state.id != '' ? 'Update Service' : 'Create Service'} < /div>} />
                  <CardText style={{padding:0}}>
                      <Grid>
                          <Row>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Service Code*"
                                      value={createServiceType.serviceCode? createServiceType.serviceCode : ""}
                                      errorText={fieldErrors.serviceCode ? fieldErrors.serviceCode : ""}
                                        onChange={(e) => handleChange(e, "serviceCode", true, '')}
                                      id="serviceCode"
                                      disabled={this.state.id ? true : false }
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText={translate("core.lbl.servicename")+"*"}
                                      value={createServiceType.serviceName? createServiceType.serviceName : ""}
                                      errorText={fieldErrors.serviceName ? fieldErrors.serviceName : ""}
                                      onChange={(e) => handleChange(e, "serviceName", true, '')}
                                      id="serviceName"

                                  />
                              </Col>

                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText={translate("core.lbl.description")}
                                      value={createServiceType.description? createServiceType.description : ""}
                                      errorText={fieldErrors.description ? fieldErrors.description : ""}
                                      onChange={(e) => handleChange(e, "description", false, '')}
                                      multiLine={true}
                                      id="description"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Type"
                                      value={createServiceType.type? createServiceType.type : ""}
                                      errorText={fieldErrors.type ? fieldErrors.type : ""}
                                      onChange={(e) => handleChange(e, "type", false, '')}
                                      multiLine={true}
                                      id="type"
                                  />
                              </Col>
                              <Col xs={12} md={3}>
                               <SelectField
                                    multiple="true"
                                    errorText={fieldErrors.keywords ? fieldErrors.keywords : ""}
                                    value={createServiceType.keywords ? createServiceType.keywords : ""}
                                    id="keywords"
                                    onChange={(e, index, value) => {
                                       var e = {
                                         target: {
                                           value: value
                                         }
                                       };
                                       console.log(value);
                                       handleChange(e, "keywords", true, "")}}
                                    floatingLabelText="keywords*" >
                                        <MenuItem value={"deliverable"} primaryText="deliverable"/>
                                        <MenuItem value={"complaint"} primaryText="complaint"/>
                               </SelectField>
                             </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="Group"
                                      value={createServiceType.group? createServiceType.group : ""}
                                      errorText={fieldErrors.group ? fieldErrors.group : ""}
                                      onChange={(e) => handleChange(e, "group", false, '')}
                                      multiLine={true}
                                      id="group"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                  <TextField
                                      fullWidth={true}
                                      floatingLabelText="SLA Hours*"
                                      value={createServiceType.slaHours? createServiceType.slaHours : ""}
                                      errorText={fieldErrors.slaHours ? fieldErrors.slaHours : ""}
                                      onChange={(e) => handleChange(e, "slaHours", true, '')}
                                      multiLine={true}
                                      id="slaHours"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                                    <SelectField
                                       floatingLabelText={translate("core.category")+"*"}
                                       fullWidth={true}
                                       value={createServiceType.category ? createServiceType.category : ""}
                                       onChange= {(e, index ,values) => {
                                         var e = {
                                           target: {
                                             value: values
                                           }
                                         };
                                         handleChange(e, "category", true, "");
                                        }}
                                     >
                                     {this.state.category.map((item, index) => (
                                               <MenuItem
                                                 value={item.id}
                                                 key={index}
                                                 primaryText={item.name}

                                               />
                                      ))}
                                      </SelectField>
                              </Col>

                              <div className="clearfix"></div>

                              <Col xs={12} md={3} sm={6}>
                              {console.log(createServiceType.active)}
                                  <Checkbox
                                    label={translate("pgr.lbl.active")}
                                    style={styles.active}
                                    checked = {createServiceType.active || false}
                                    onCheck = {(e, i, v) => { console.log(createServiceType.active, i);

                                      var e = {
                                        target: {
                                          value:i
                                        }
                                      }
                                      handleChange(e, "active", false, '')
                                    }}
                                    id="active"
                                  />
                              </Col>
                              <Col xs={12} md={3} sm={6}>
                              {console.log(createServiceType.hasFinancialImpact)}
                                  <Checkbox
                                    label="Has Financial Impact"
                                    style={styles.hasFinancialImpact}
                                    checked = {createServiceType.hasFinancialImpact || false}
                                    onCheck = {(e, i, v) => { console.log(createServiceType.hasFinancialImpact, i);

                                      var e = {
                                        target: {
                                          value:i
                                        }
                                      }
                                      handleChange(e, "hasFinancialImpact", false, '')
                                    }}
                                    id="hasFinancialImpact"
                                  />
                              </Col>
                               <Col xs={12} md={3} sm={6}>
                              {console.log(createServiceType.attributes)}
                                  <Checkbox
                                    label="Attributes"
                                    style={styles.attributes}
                                    checked = {createServiceType.attributes || false}
                                    onCheck = {(e, i, v) => { console.log(createServiceType.attributes, i);

                                      var e = {
                                        target: {
                                          value:i
                                        }
                                      }
                                      handleChange(e, "attributes", false, '')
                                    }}
                                    id="attributes"
                                  />
                              </Col>
                              <div className="clearfix"></div>
                                {promotionFunc()}

                          </Row>
                      </Grid>
                  </CardText>
              </Card>
              <div style={{textAlign:'center'}}>
                <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label={this.state.id != '' ? translate("pgr.lbl.update") : translate("pgr.lbl.create")} backgroundColor={"#5a3e1b"} labelColor={white}/>
                <RaisedButton style={{margin:'15px 5px'}} label={translate("core.lbl.close")}/>
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

  return ({createServiceType : state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["serviceName","serviceCode","category","slaHours","keywords"]
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
          current: ["serviceName","serviceCode","category","slaHours","keywords"],
          required: ["serviceName","serviceCode","category","slaHours","keywords"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },

  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState,toastMsg});
  },


    handleChangeNextOne: (e, property, propertyOne, isRequired, pattern) => {
      dispatch({
        type: "HANDLE_CHANGE_NEXT_ONE",
        property,
        propertyOne,
        value: e.target.value,
        isRequired,
        pattern
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


    addNestedFormDataTwo: (formObject, formArray, formData) => {
      dispatch({
        type: "PUSH_ONE_ARRAY",
        formObject,
        formArray,
        formData
      })
    },
    resetObject: (object) => {
      dispatch({
        type: "RESET_OBJECT",
        object
      })
    },
})

export default connect(mapStateToProps, mapDispatchToProps)(ServiceTypeCreate);
