import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Grid, Row, Col, Table, DropdownButton } from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import IconButton from 'material-ui/IconButton';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import UiTextField from './UiTextField'
import UiSelectField from './UiSelectField'
import UiSelectFieldMultiple from './UiSelectFieldMultiple'
import UiCheckBox from './UiCheckBox'
import UiEmailField from './UiEmailField'
import UiMobileNumber from './UiMobileNumber'
import UiTextArea from './UiTextArea'
import UiMultiSelectField from './UiMultiSelectField'
import UiNumberField from './UiNumberField'
import UiDatePicker from './UiDatePicker'
import UiMultiFileUpload from './UiMultiFileUpload'
import UiSingleFileUpload from './UiSingleFileUpload'
import UiAadharCard from './UiAadharCard'
import UiPanCard from './UiPanCard'
import UiLabel from './UiLabel'
import UiRadioButton from './UiRadioButton'
import UiTextSearch from './UiTextSearch'
import UiDocumentList from './UiDocumentList'
import UiAutoComplete from './UiAutoComplete'
import UiDate from './UiDate';
import UiPinCode from './UiPinCode';
import UiArrayField from './UiArrayField';
import UiFileTable from './UiFileTable';
import { translate } from '../../common/common';
import RaisedButton from 'material-ui/RaisedButton';
import _ from 'lodash';

class UiMultiFieldAddToTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      currentValue: '',
      valueList: [],
      formData: {},
      disableAdd: true,
      index: -1,
      fieldErrors: {},
      isBtnDisabled: false,
      requiredFields: [],
      isInlineEdit: false,
      indexes: [],
      isAddAgain: true
    }
  }

  componentDidMount() {
    let {item, valueList} = this.props;
    let requiredFields = [];
    for(let i=0; i<item.values.length; i++) {
      if(item.values[i].isRequired)
        requiredFields.push(item.values[i].jsonPath);
    }

    this.setState({
      requiredFields,
      isInlineEdit: item.values.length < 5
    });

    if(valueList && valueList.length) {
      this.setState({
        valueList: _.cloneDeep(valueList)
      })
    }
  }

  componentWillReceiveProps(nextProps) {
    if(!_.isEqual(nextProps, this.props)) {
      this.updateValueList(nextProps);
    }
  }

  updateValueList = (nProps) => {
    if(nProps.valueList && nProps.valueList.length && !_.isEqual(nProps.valueList, this.state.valueList)) {
      this.setState({
        valueList: _.cloneDeep(nProps.valueList)
      })
    }
  }

  handler = (e, property, isRequired, pattern, requiredErrMsg = "Required", patternErrMsg = "Pattern Missmatch", expression, expErr, isDate) => {
    let { formData } = this.state;
    let fieldErrors = _.cloneDeep(this.state.fieldErrors);
    let isFormValid = true;
    _.set(formData, property, e.target.value);
    
    //Check if required
    if(isRequired && (e.target.value == "")) {
      fieldErrors[property] = requiredErrMsg;
    } else {
      delete fieldErrors[property];
    }

    //Check for pattern match
    if(pattern && _.get(formData, property) && !new RegExp(pattern).test(_.get(formData, property))) {
      fieldErrors[property] = patternErrMsg ? translate(patternErrMsg) : translate('ui.framework.patternMessage');
      isFormValid = false;
    }

    //Check if any other field is required
    for(let i=0; i<this.state.requiredFields.length; i++) {
      if(typeof _.get(formData, this.state.requiredFields[i]) == "undefined" || _.get(formData, this.state.requiredFields[i]) == "") {
        isFormValid = false;
        break;
      }
    }
    
    this.setState({
      formData,
      fieldErrors,
      isBtnDisabled: !isFormValid || Object.keys(fieldErrors).length > 0
    });
  }

  addToParent = (doNotOpen, ind) => {
    let formData = _.cloneDeep(this.props.formData);
    let localFormData = _.cloneDeep(this.state.formData);
    let myTableInParent = _.get(formData, this.props.item.jsonPath);
    let stateFormDataTable = _.get(localFormData, this.props.item.jsonPath);
    let indexes;
    if(this.state.index == -1) {
      if (!myTableInParent) {
        this.props.handler({ target: { value: stateFormDataTable } }, this.props.item.jsonPath);
      } else {
        myTableInParent.push(stateFormDataTable[0]);
        this.props.handler({ target: { value: myTableInParent } }, this.props.item.jsonPath);
      }
    } else {
      myTableInParent[this.state.index] = stateFormDataTable[0];
      this.props.handler({ target: { value: myTableInParent } }, this.props.item.jsonPath);
    }

    let list = _.get(this.props.formData, this.props.item.jsonPath);

    if(typeof ind != 'undefined') {
      indexes = _.cloneDeep(this.state.indexes);
      for(let i=0;i<indexes.length;i++){
        if(indexes[i] == ind) {
          indexes.splice(i, 1);
          break;
        }
      }
    }

    this.setState({
      valueList: list,
      formData: {},
      open: doNotOpen ? false : (this.state.index > -1 ? false : true),
      index: -1,
      isAddAgain: true,
      indexes: indexes || this.state.indexes
    }, function() {
      if(this.props.setDisabled) this.props.setDisabled(true);
    })
  }

  editRow = (index) => {
    let list = _.cloneDeep(this.state.valueList);
    let formData = {};
    _.set(formData, this.props.item.jsonPath + "[0]", list[index]);
    this.setState({
      formData,
      index,
      open: true
    })
  }

  deleteRow = (index) => {
    let formData = _.cloneDeep(this.props.formData);
    let myTableInParent = _.get(formData, this.props.item.jsonPath);
    if(myTableInParent) {
      myTableInParent.splice(index, 1);
      this.props.handler({ target: { value: myTableInParent } }, this.props.item.jsonPath);
    }
    let list = _.cloneDeep(this.state.valueList);
    list.splice(index, 1)
    this.setState({
      valueList: list,
      isAddAgain: true,
      formData: {}
    }, function() {
      if(this.props.setDisabled) this.props.setDisabled(true);
    })
  }

  editInline = (index) => {
    let {indexes} = this.state;
    let list = _.cloneDeep(this.state.valueList);
    indexes.push(index);
    let formData = {};
    _.set(formData, this.props.item.jsonPath + "[0]", list[index]);
    this.setState({
      formData,
      index,
      indexes,
      isAddAgain: false
    })
  }

  renderFields = (item, screen) => {
    if (screen == "view" && ["documentList", "fileTable", "arrayText", "arrayNumber"].indexOf(item.type) > -1) {
      if (item.type == "datePicker") {
        item.isDate = true;
      }
      item.type = "label";
    }

    item.label = translate(item.label);
    switch (item.type) {
      case 'text':
        return <UiTextField ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'textarea':
        return <UiTextArea ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'singleValueListMultiple':
        return <UiSelectFieldMultiple ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'singleValueList':
        item.fromProps = true;
        item.animated = true;
        item.isSet = !item.isSet;
        return <UiSelectField isSet={item.isSet} ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'multiValueList':
        return <UiMultiSelectField ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'autoCompelete':
        return <UiAutoComplete ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} autoComHandler={this.autoComHandler || ""} />
      case 'number':
        return <UiNumberField ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'mobileNumber':
        return <UiMobileNumber ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'checkbox':
        return <UiCheckBox ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'email':
        return <UiEmailField ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'datePicker':
        return <UiDatePicker ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'singleFileUpload':
        return <UiSingleFileUpload ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'multiFileUpload':
        return <UiMultiFileUpload ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'pan':
        return <UiPanCard ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'aadhar':
        return <UiAadharCard ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'pinCode':
        return <UiPinCode ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'label':
        return <UiLabel getVal={this.getVal} item={item} />
      case 'radio':
        return <UiRadioButton ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.handler} />
      case 'textSearch':
        return <UiTextSearch ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.state.fieldErrors} handler={this.props.handler} autoComHandler={this.autoComHandler} />
    }
  }

  renderArrayField = (item) => {
    switch (this.props.ui) {
      case 'google':
        return (
          <div>
            <Dialog
              title={this.props.item.label}
              actions={
                <div>
                  <FlatButton
                    label={(this.state.index == -1) ? translate("pt.create.groups.ownerDetails.fields.add") : translate("pgr.lbl.update")}
                    secondary={true}
                    disabled={this.state.isBtnDisabled}
                    style={{ "marginTop": 39 }}
                    onClick={this.addToParent}/>
                  <FlatButton
                    label={translate("pt.create.button.viewdcb.close")}
                    primary={true}
                    onClick={this.handleClose}/>
                </div>
              }
              modal={false}
              open={this.state.open}
              onRequestClose={this.handleClose}>
              <Row>
                {
                  this.props.item.values.map((v, i) => {
                    return (
                      <Col xs={12} md={6}>
                        {this.renderFields(v, this.props.screen)}
                      </Col>
                    )
                  }
                  )
                }   
              </Row>
              <br/>

            </Dialog>
            <div style={{"textAlign": "right"}}>
              <RaisedButton label={"Add"}
                onClick={this.handleOpen}
                disabled={!this.state.isAddAgain}
                primary={true}/>
            </div>
            <br/>
            <Table className="table table-striped table-bordered" responsive>
              <thead>
                <tr>
                  <th>#</th>
                  {this.props.item.header.map((v) => {
                    return(
                      <th>{translate(v.label)}</th>
                    )
                  })}
                  <th> Action</th>
                </tr>
              </thead>
              <tbody>
                {
                  this.state.valueList && this.state.valueList.length ?
                  this.state.valueList.map((item, index) => {
                    if(this.state.indexes.indexOf(index) > -1 || typeof item == "string") {
                      return (
                        <tr key={index}>
                          <td> {index + 1} </td>
                          {this.props.item.values.map((v) => {
                              return (
                                  <td>{this.renderFields(v)}</td>
                              )
                            })
                          }
                          <td>
                            <FlatButton
                              label={(this.state.index == -1) ? translate("pt.create.groups.ownerDetails.fields.add") : translate("pgr.lbl.update")}
                              secondary={true}
                              disabled={this.state.isBtnDisabled}
                              onClick={(e) => {this.addToParent(true, index)}}/>
                            <br/>
                            <FlatButton
                              label={translate("pgr.lbl.delete")}
                              secondary={true}
                              onClick={(e) => {this.deleteRow(index)}}/>
                          </td>
                        </tr>
                      )
                    } else {
                      return (
                        <tr key={index}>
                          <td> {index + 1} </td>
                          {Object.values(item).map((v) => {
                              return (
                                  <td>{v}</td>
                              )
                            })
                          }
                          <td>
                            {this.state.isInlineEdit ? 
                            <IconButton 
                                onClick={()=>{
                                  this.editInline(index)
                                }} 
                                disabled={!this.state.isAddAgain}>
                              <i className="material-icons" style={{"color":"#000000"}}>border_color</i>
                            </IconButton>
                             :
                            <IconButton 
                                onClick={()=>{
                                  this.editRow(index)
                                }}
                                disabled={!this.state.isAddAgain}>
                              <i className="material-icons" style={{"color":"#000000"}}>mode_edit</i>
                            </IconButton>}
                            &nbsp;&nbsp;&nbsp;&nbsp;
                            <IconButton
                                onClick={()=>{
                                  this.deleteRow(index)
                                }}
                                disabled={!this.state.isAddAgain}>
                              <i className="material-icons text-danger">delete</i>
                            </IconButton>
                          </td>
                        </tr>
                      )
                    }
                    
                  }) :  <tr>
                          <td colSpan={this.props.item.header.length + 2} className="text-center">
                            No data yet! &nbsp;&nbsp; <a
                                                        href="javascript:void(0)"
                                                        className
                                                        onClick={this.handleOpen}>Click here</a> to add.
                          </td>
                        </tr>
                }
              </tbody>
            </Table>
          </div>
        );
    }
  }

  handleOpen = () => {
    if(this.state.isInlineEdit) {
      let list = _.cloneDeep(this.state.valueList);
      list.push(" ");
      this.setState({
        isBtnDisabled: true,
        index: -1,
        valueList: list,
        isAddAgain: false
      }, function() {
        if(this.props.setDisabled) this.props.setDisabled(false);
      })
    } else 
      this.setState({
        open: true,
        isBtnDisabled: true,
        index: -1
      });
  }

  handleClose = () => {
    this.setState({
      open: false
    });
  }

  getVal = (path, dateBool) => {
    var _val = _.get(this.state.formData, path);

    if (dateBool && typeof _val == 'string' && _val && _val.indexOf("-") > -1) {
      var _date = _val.split("-");
      return new Date(_date[0], (Number(_date[1]) - 1), _date[2]);
    }
    return typeof _val != "undefined" ? _val : "";
  }

  valueFromList = (index) => {
    let list = [...this.state.valueList];
    list.splice(index, 1);
    this.setState({
      valueList: list
    }, () => {
      this.props.handler({ target: { value: this.state.valueList.length ? this.state.valueList : "" } }, this.props.item.jsonPath, this.props.item.isRequired ? true : false, '', this.props.item.requiredErrMsg, this.props.item.patternErrMsg);
    })
  }

  render() {
    return (<div>
      {this.renderArrayField(this.props.item)}
    </div>);
  }
}

const mapStateToProps = state => ({
  formData: state.frameworkForm.form
})

const mapDispatchToProps = dispatch => ({
  setFormData: (data) => {
    dispatch({ type: "SET_FORM_DATA", data });
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(UiMultiFieldAddToTable);