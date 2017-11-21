import React, { Component } from 'react';
import { Grid, Row, Col, Table, DropdownButton } from 'react-bootstrap';
import TextField from 'material-ui/TextField';
import FloatingActionButton from 'material-ui/FloatingActionButton';
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
import _ from 'lodash';

export default class multiFieldAddToTable extends Component {
  constructor(props) {
    super(props);
    this.state = {
      open: false,
      currentValue: '',
      valueList: [],
      formData: {},
      index: 0
    }
  }
  // componentWillReceiveProps(nextProps) {
  //   let arrayValue = this.props.getVal(this.props.item.jsonPath);
  //   console.log(arrayValue)
  //   let { valueList } = this.state;
  //   if (_.isArray(arrayValue) && JSON.stringify(arrayValue) != JSON.stringify(valueList)) {
  //     this.setState({
  //       valueList: arrayValue
  //     })
  //   }
  // }
  
  handler = (e, property, isRequired, pattern, requiredErrMsg="Required", patternErrMsg="Pattern Missmatch", expression, expErr, isDate) => {
    let {formData} = this.state;
    _.set(formData, property, e.target.value);
    this.setState({
      formData
    }, function() {
      this.props.getVal()
    });

  }

  addToParent = () => {
    
    this.setState({
    })
  }

  renderFields = (item, screen) => {
    if (screen == "view" && ["documentList", "fileTable", "arrayText", "arrayNumber"].indexOf(item.type) > -1) {
      if (item.type == "datePicker") {
        item.isDate = true;
      }
      item.type = "label";
    } 
    switch (item.type) {
      case 'text':
        return <UiTextField ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'textarea':
        return <UiTextArea ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'singleValueListMultiple':
        return <UiSelectFieldMultiple ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'singleValueList':
        return <UiSelectField ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'multiValueList':
        return <UiMultiSelectField ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'autoCompelete':
        return <UiAutoComplete ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} autoComHandler={this.autoComHandler || ""} />
      case 'number':
        return <UiNumberField ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'mobileNumber':
        return <UiMobileNumber ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'checkbox':
        return <UiCheckBox ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'email':
        return <UiEmailField ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'datePicker':
        return <UiDatePicker ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'singleFileUpload':
        return <UiSingleFileUpload ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'multiFileUpload':
        return <UiMultiFileUpload ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'pan':
        return <UiPanCard ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'aadhar':
        return <UiAadharCard ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'pinCode':
        return <UiPinCode ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'label':
        return <UiLabel getVal={this.getVal} item={item} />
      case 'radio':
        return <UiRadioButton ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.handler} />
      case 'textSearch':
        return <UiTextSearch ui={this.props.ui} getVal={this.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} autoComHandler={this.autoComHandler} />
    }
  }

  renderArrayField = (item) => {
    switch (this.props.ui) {
      case 'google':
        return (
          <div>
            
            {/* this.renderField(item) */}
            <Dialog
              title={this.props.item.label}
              actions={
                <FlatButton
                  label={translate("pt.create.button.viewdcb.close")}
                  primary={true}
                  onClick={this.handleClose}
                />
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
                {/* //         {!item.tableList.actionsNotRequired ?
                //           <td>
                //             <div className="material-icons" onClick={()=>{
                //               this.deleteRow(i)
                //             }}>delete</div>
                //           </td>
                //         : ''}
                //       </tr>
                //     )
                //   })
                // }
                // <Col >
                // </Col> */}
                <Col xs={12} md={4}>
                  <FlatButton
                    label={translate("pt.create.groups.ownerDetails.fields.add")}
                    secondary={true}
                    style={{ "marginTop": 39 }}
                    onClick={this.addToParent}
                  />
                </Col>
              </Row>
              <br />
              
            </Dialog>
            {/* <Table className="table table-striped table-bordered" responsive>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>{translate("collection.pay.value")}</th>
                    <th>{translate("reports.common.action")}</th>
                  </tr>
                </thead>
                <tbody>
                  {
                    this.state.valueList.map((v, i) => {
                      return (
                        <tr key={i}>
                          <td>{i + 1}</td>
                          <td>{v}</td>
                          <td>
                            <div className="material-icons" onClick={() => {
                              this.valueFromList(i)
                            }}>delete</div>
                          </td>
                        </tr>
                      )
                    })
                  }
                </tbody>
              </Table> */}
            <FlatButton label={"click to add location details"}
              onClick={(e) => {
                this.setState({open: true})
              }}
            />
          </div>
        );
    }
  }

  handleOpen = () => {
    this.setState({
      open: true
    });
  }

  handleClose = () => {
    this.setState({
      open: false
    });
  }

  getVal = (path, dateBool) => {
    var _val = _.get(this.state.formData, path);
    console.log(path + "--" + _.get(this.state.formData, path));
    
    if(dateBool && typeof _val == 'string' && _val && _val.indexOf("-") > -1) {
      var _date = _val.split("-");
      return new Date(_date[0], (Number(_date[1])-1), _date[2]);
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
    console.log(this.state.formData);
    return (<div>
      {this.renderArrayField(this.props.item)}
    </div>);
  }
}

const mapStateToProps = state => ({
  formData:state.frameworkForm.form
})   

const mapDispatchToProps = dispatch => ({
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
  }
})  