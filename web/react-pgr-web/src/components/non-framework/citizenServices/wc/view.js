import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "../../../framework/showFields";

import {translate} from '../../../common/common';
import Api from '../../../../api/api';
import jp from "jsonpath";
import UiButton from '../../../framework/components/UiButton';
import {fileUpload} from '../../../framework/utility/utility';
import UiTable from '../../../framework/components/UiTable';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import TextField from 'material-ui/TextField';

var specifications={};

let reqRequired = [];
class Report extends Component {
  constructor(props) {
    super(props);
    this.state = {
      openAddFee: false,
      openPayFee: false
    }
  }

  setLabelAndReturnRequired(configObject) {
    if(configObject && configObject.groups) {
      for(var i=0;configObject && i<configObject.groups.length; i++) {
        configObject.groups[i].label = translate(configObject.groups[i].label);
        for (var j = 0; j < configObject.groups[i].fields.length; j++) {
              configObject.groups[i].fields[j].label = translate(configObject.groups[i].fields[j].label);
        }

        if(configObject.groups[i].children && configObject.groups[i].children.length) {
          for(var k=0; k<configObject.groups[i].children.length; k++) {
            this.setLabelAndReturnRequired(configObject.groups[i].children[k]);
          }
        }
      }
    }
  }

  setInitialUpdateChildData(form, children) {
    let _form = JSON.parse(JSON.stringify(form));
    for(var i=0; i<children.length; i++) {
      for(var j=0; j<children[i].groups.length; j++) {
        if(children[i].groups[j].multiple) {
          var arr = _.get(_form, children[i].groups[j].jsonPath);
          var ind = j;
          var _stringifiedGroup = JSON.stringify(children[i].groups[j]);
          var regex = new RegExp(children[i].groups[j].jsonPath.replace("[", "\[").replace("]", "\]") + "\\[\\d{1}\\]", 'g');
          for(var k=1; k < arr.length; k++) {
            j++;
            children[i].groups[j].groups.splice(ind+1, 0, JSON.parse(_stringifiedGroup.replace(regex, children[i].groups[ind].jsonPath + "[" + k + "]")));
            children[i].groups[j].groups[ind+1].index = ind+1;
          }
        }

        if(children[i].groups[j].children && children[i].groups[j].children.length) {
          this.setInitialUpdateChildData(form, children[i].groups[j].children);
        }
      }
    }
  }

  hideField(specs, moduleName, actionName, hideObject) {
    if(hideObject.isField) {
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<specs[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(hideObject.name == specs[moduleName + "." + actionName].groups[i].fields[j].name) {
            specs[moduleName + "." + actionName].groups[i].fields[j].hide = true;
            break;
          }
        }
      }
    } else {
      let flag = 0;
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        if(hideObject.name == specs[moduleName + "." + actionName].groups[i].name) {
          flag = 1;
          specs[moduleName + "." + actionName].groups[i].hide = true;
          break;
        }
      }

      if(flag == 0) {
        for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
          if(specs[moduleName + "." + actionName].groups[i].children && specs[moduleName + "." + actionName].groups[i].children.length) {
            for(let j=0; j<specs[moduleName + "." + actionName].groups[i].children.length; j++) {
              for(let k=0; k<specs[moduleName + "." + actionName].groups[i].children[j].groups.length; k++) {
                if(hideObject.name == specs[moduleName + "." + actionName].groups[i].children[j].groups[k].name) {
                  specs[moduleName + "." + actionName].groups[i].children[j].groups[k].hide = true;
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  showField(specs, moduleName, actionName, showObject) {
    if(showObject.isField) {
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<specs[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(showObject.name == specs[moduleName + "." + actionName].groups[i].fields[j].name) {
            specs[moduleName + "." + actionName].groups[i].fields[j].hide = false;
            break;
          }
        }
      }
    } else {
      let flag = 0;
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        if(showObject.name == specs[moduleName + "." + actionName].groups[i].name) {
          flag = 1;
          specs[moduleName + "." + actionName].groups[i].hide = false;
          break;
        }
      }

      if(flag == 0) {
        for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
          if(specs[moduleName + "." + actionName].groups[i].children && specs[moduleName + "." + actionName].groups[i].children.length) {
            for(let j=0; j<specs[moduleName + "." + actionName].groups[i].children.length; j++) {
              for(let k=0; k<specs[moduleName + "." + actionName].groups[i].children[j].groups.length; k++) {
                if(showObject.name == specs[moduleName + "." + actionName].groups[i].children[j].groups[k].name) {
                  specs[moduleName + "." + actionName].groups[i].children[j].groups[k].hide = false;
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  setInitialUpdateData(form, specs, moduleName, actionName, objectName) {
    let {setMockData} = this.props;
    let _form = JSON.parse(JSON.stringify(form));
    var ind;
    for(var i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
      if(specs[moduleName + "." + actionName].groups[i].multiple) {
        var arr = _.get(_form, specs[moduleName + "." + actionName].groups[i].jsonPath);
        ind = i;
        var _stringifiedGroup = JSON.stringify(specs[moduleName + "." + actionName].groups[i]);
        var regex = new RegExp(specs[moduleName + "." + actionName].groups[i].jsonPath.replace(/\[/g, "\\[").replace(/\]/g, "\\]") + "\\[\\d{1}\\]", 'g');
        for(var j=1; j < arr.length; j++) {
          i++;
          specs[moduleName + "." + actionName].groups.splice(ind+1, 0, JSON.parse(_stringifiedGroup.replace(regex, specs[moduleName + "." + actionName].groups[ind].jsonPath + "[" + j + "]")));
          specs[moduleName + "." + actionName].groups[ind+1].index = ind+1;
        }
      }

      for(var j=0; j<specs[moduleName + "." + actionName].groups[i].fields.length; j++) {
        if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields && specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields.length) {
          for(var k=0; k<specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields.length; k++) {
            if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].ifValue == _.get(form, specs[moduleName + "." + actionName].groups[i].fields[j].jsonPath)) {
              if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide && specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide.length) {
                for(var a=0; a<specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide.length; a++) {
                  this.hideField(specs, moduleName, actionName, specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide[a]);
                }
              }

              if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show && specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show.length) {
                for(var a=0; a<specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show.length; a++) {
                  this.showField(specs, moduleName, actionName, specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show[a]);
                }
              }
            }
          }
        }
      }

      if(specs[moduleName + "." + actionName].groups[ind || i].children && specs[moduleName + "." + actionName].groups[ind || i].children.length) {
        this.setInitialUpdateChildData(form, specs[moduleName + "." + actionName].groups[ind || i].children);
      }
    }

    setMockData(specs);
  }

  initData() {
    specifications = require("../../../framework/specs/citizenService/wc/NewConnection").default;

    let { setMetaData, setModuleName, setActionName, setMockData } = this.props;
    let self = this;
    let obj = specifications["wc.create"];
    self.setLabelAndReturnRequired(obj);
    setMetaData(specifications);
    setMockData(JSON.parse(JSON.stringify(specifications)));
    setModuleName("wc");
    setActionName("create");
    //Get view form data
    var url = specifications["wc.create"].url.split("?")[0];
    var hash = window.location.hash.split("/");
    var query = {
      acknowledgementNumber: this.props.match.params.ackNo
    };

    Api.commonApiPost("/wcms-connection/connection/_search", query, {}, false, specifications["wc.create"].useTimestamp).then(function(res){
      self.props.setFormData(res);
      self.setInitialUpdateData(res, JSON.parse(JSON.stringify(specifications)), "wc", "create", specifications["wc.create"].objectName);
    }, function(err){

    })
  }

  componentDidMount() {
      this.initData();
  }

  getVal = (path) => {
    var val = _.get(this.props.formData, path);

    if(val && ((val + "").length == 13 || (val + "").length == 12) && new Date(Number(val)).getTime() > 0) {
      var _date = new Date(Number(val));
      return ('0' + _date.getDate()).slice(-2) + '/'
               + ('0' + (_date.getMonth()+1)).slice(-2) + '/'
               + _date.getFullYear();
    }

    return  typeof val != "undefined" && (typeof val == "string" || typeof val == "number" || typeof val == "boolean") ? (val + "") : "";
  }

  printer = () => {
    window.print();
  }

  openAddFeeModal = () => {
    this.setState({
      openAddFee: !this.state.openAddFee
    })
  }

  openPayFeeModal = () => {
    this.setState({
      openPayFee: !this.state.openPayFee
    })
  }

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors} = this.props;
    let {handleChange, getVal, addNewCard, removeCard, printer} = this;
    let self = this;

    return (
      <div className="Report">
        <form id="printable">
          {!_.isEmpty(mockData) && mockData["wc.create"] && <ShowFields groups={mockData["wc.create"].groups} noCols={mockData["wc.create"].numCols} ui="google" handler={""} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData["wc.create"].useTimestamp || false} addNewCard={""} removeCard={""} screen="view"/>}
          <br/>
          <Card className="uiCard">
            <CardHeader style={{paddingTop:4,paddingBottom:0}} title={<div style={{color:"#354f57", fontSize:18,margin:'8px 0'}}>Workflow Details</div>}/>
            <CardText style={{paddingTop:0,paddingBottom:0}}>
              <Grid style={{paddingTop:0}}>
                <Row>
                  <Col xs={12} md={3}>
                    <SelectField
                      floatingLabelStyle={{"color": "#696969", "fontSize": "20px", "white-space": "nowrap"}}
                      labelStyle={{"color": "#5F5C57"}}
                      floatingLabelFixed={true} 
                      dropDownMenuProps={{animated: false, targetOrigin: {horizontal: 'left', vertical: 'bottom'}}}
                      errorStyle={{"float":"left"}}
                      fullWidth={true}
                      floatingLabelText={<span>Status <span style={{"color": "#FF0000"}}>{" *"}</span></span>} 
                      value={"SUCCESS"}
                      onChange={(event, key, value) =>{
                        
                      }}
                      maxHeight={200}>
                              <MenuItem value={"SUCCESS"} primaryText={"SUCCESS"} />
                    </SelectField>
                  </Col>
                  <Col xs={12} md={3}>
                    <RaisedButton
                      floatingLabelStyle={{"color": "#696969"}}
                      style={{"marginTop": "26px"}}
                      containerElement='label'
                      fullWidth={true} 
                      label={"Upload Files"}>
                        <input type="file" style={{ display: 'none' }} onChange={(e) => {}}/>
                    </RaisedButton>
                  </Col>
                </Row>
              </Grid>
            </CardText>
          </Card>
          <br/>
          <div style={{"textAlign": "center"}}>
            <RaisedButton primary={true} label={"Add Fee"} onClick={self.openAddFeeModal}/>&nbsp;&nbsp;
            <RaisedButton primary={true} label={"Pay Fee"} onClick={self.openPayFeeModal}/>
          </div>

        </form>
        <Dialog
                title="Add Fee Amount"
                modal={false}
                open={self.state.openAddFee}
                onRequestClose={self.openAddFeeModal}
                autoScrollBodyContent={true}
              >
              <div style={{textAlign:"center"}}>
                <Row>
                  <Col xs={12} md={4}>
                    <TextField
                      type="number"
                      inputStyle={{"color": "#5F5C57"}}
                      errorStyle={{"float":"left"}}
                      fullWidth={false}
                      onChange={(e) => {}}/>
                  </Col>
                </Row>
              </div>

              <UiButton handler={self.openAddFeeModal} item={{"label": "Cancel", "uiType":"button"}} ui="google"/>&nbsp;&nbsp;
              <UiButton handler={self.addFee} item={{"label": "Add", "uiType":"button"}} ui="google"/>

        </Dialog>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  metaData:state.framework.metaData,
  mockData: state.framework.mockData,
  moduleName:state.framework.moduleName,
  actionName:state.framework.actionName,
  formData:state.frameworkForm.form,
  fieldErrors: state.frameworkForm.fieldErrors
});

const mapDispatchToProps = dispatch => ({
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
  },
  setMetaData: (metaData) => {
    dispatch({type:"SET_META_DATA", metaData})
  },
  setMockData: (mockData) => {
    dispatch({type: "SET_MOCK_DATA", mockData});
  },
  setModuleName: (moduleName) => {
    dispatch({type:"SET_MODULE_NAME", moduleName})
  },
  setActionName: (actionName) => {
    dispatch({type:"SET_ACTION_NAME", actionName})
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);
