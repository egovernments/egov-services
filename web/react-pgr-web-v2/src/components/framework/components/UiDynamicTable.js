import React, { Component } from 'react';
import { connect } from 'react-redux';
import { translate } from '../../common/common';
import { Grid, Row, Col, Table, DropdownButton } from 'react-bootstrap';
import { Card, CardHeader, CardText } from 'material-ui/Card';
import UiTextField from './UiTextField';
import UiSelectField from './UiSelectField';
import UiButton from './UiButton';
import UiCheckBox from './UiCheckBox';
import UiEmailField from './UiEmailField';
import UiMobileNumber from './UiMobileNumber';
import UiTextArea from './UiTextArea';
import UiMultiSelectField from './UiMultiSelectField';
import UiNumberField from './UiNumberField';
import UiDatePicker from './UiDatePicker';
import UiMultiFileUpload from './UiMultiFileUpload';
import UiSingleFileUpload from './UiSingleFileUpload';
import UiAadharCard from './UiAadharCard';
import UiPanCard from './UiPanCard';
import UiLabel from './UiLabel';
import UiRadioButton from './UiRadioButton';
import UiTextSearch from './UiTextSearch';
import UiDocumentList from './UiDocumentList';

const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');

class UiDynamicTable extends Component {
  constructor(props) {
    super(props);
  }

  // 	componentWillMount() {
  //     $('#dynamicTable').DataTable({
  //        dom: 'lBfrtip',
  //        buttons: [],
  //        bDestroy: true,
  //        language: {
  //            "emptyTable": "No Records"
  //        }
  //     });
  //  	}
  //
  //  	componentWillUnmount() {
  //   	$('#dynamicTable').DataTable().destroy(true);
  // 	}
  //
  // 	componentWillUpdate() {
  // 		let {flag} = this.props;
  //     if(flag == 1) {
  //       flag = 0;
  //       $('#dynamicTable').dataTable().fnDestroy();
  //     }
  // }
  //
  // componentDidUpdate() {
  //     $('#dynamicTable').DataTable({
  //          dom: 'lBfrtip',
  //          buttons: [],
  //           ordering: false,
  //           bDestroy: true,
  //           language: {
  //              "emptyTable": "No Records"
  //           }
  //     });
  // 	}

  renderFields = item => {
    switch (item.type) {
      case 'text':
        return (
          <UiTextField ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} />
        );
        break;
      case 'singleValueList':
        return (
          <UiSelectField
            ui={this.props.ui}
            useTimestamp={this.props.useTimestamp}
            getVal={this.props.getVal}
            item={item}
            fieldErrors={this.props.fieldErrors}
            handler={this.props.handler}
          />
        );
        break;
      case 'multiValueList':
        return (
          <UiSingleFileUpload
            ui={this.props.ui}
            getVal={this.props.getVal}
            item={item}
            fieldErrors={this.props.fieldErrors}
            handler={this.props.handler}
          />
        );
        break;
      case 'number':
        return (
          <UiNumberField
            ui={this.props.ui}
            getVal={this.props.getVal}
            item={item}
            fieldErrors={this.props.fieldErrors}
            handler={this.props.handler}
          />
        );
        break;
      case 'textarea':
        return (
          <UiTextArea ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} />
        );
        break;
      case 'mobileNumber':
        return (
          <UiMobileNumber
            ui={this.props.ui}
            getVal={this.props.getVal}
            item={item}
            fieldErrors={this.props.fieldErrors}
            handler={this.props.handler}
          />
        );
        break;
      case 'checkbox':
        return (
          <UiCheckBox ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} />
        );
        break;
      case 'email':
        return (
          <UiEmailField ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} />
        );
        break;
      case 'button':
        return (
          <UiButton ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} />
        );
        break;
      case 'datePicker':
        return (
          <UiDatePicker ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} />
        );
        break;
      case 'singleFileUpload':
        return (
          <UiSingleFileUpload
            ui={this.props.ui}
            getVal={this.props.getVal}
            item={item}
            fieldErrors={this.props.fieldErrors}
            handler={this.props.handler}
          />
        );
        break;

      case 'multiFileUpload':
        return (
          <UiMultiSelectField
            ui={this.props.ui}
            getVal={this.props.getVal}
            item={item}
            fieldErrors={this.props.fieldErrors}
            handler={this.props.handler}
          />
        );
      case 'pan':
        return (
          <UiPanCard ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} />
        );
      case 'aadhar':
        return (
          <UiAadharCard ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} />
        );
      case 'label':
        return <UiLabel getVal={this.props.getVal} item={item} />;
      case 'radio':
        return (
          <UiRadioButton
            ui={this.props.ui}
            getVal={this.props.getVal}
            item={item}
            fieldErrors={this.props.fieldErrors}
            handler={this.props.handler}
          />
        );
      case 'textSearch':
        return (
          <UiTextSearch
            ui={this.props.ui}
            getVal={this.props.getVal}
            item={item}
            fieldErrors={this.props.fieldErrors}
            handler={this.props.handler}
            autoComHandler={this.props.autoComHandler}
          />
        );
      case 'documentList':
        return (
          <UiDocumentList
            ui={this.props.ui}
            getVal={this.props.getVal}
            item={item}
            fieldErrors={this.props.fieldErrors}
            handler={this.props.handler}
          />
        );
    }
  };

  render() {
    let self = this;
    let { resultList, getVal } = this.props;
    let { renderFields } = this;
    // console.log(resultList);

    // const totalAmount=()=>{
    // 	 let bill=getVal("Receipt[0].Bill");
    // 	 let sum=0;
    // 	 for (var i = 0; i < bill.length; i++) {
    // 		 	if (bill[i].hasOwnProperty("amountPaid")) {
    // 		 		sum+=bill[i].amountPaid;
    // 		 	}
    // 	 }
    // 	 return sum;
    // }

    const renderTable = function() {
      return (
        <Card className="uiCard">
          <CardHeader title={<strong> {translate('ui.table.title')} </strong>} />
          <CardText>
            {/*
								resultList.hasOwnProperty("resultValues") && resultList.resultValues.map((item, i) => {
									 return (
											 <Card className="uiCard">
												 <CardHeader key={i} title={<strong> {translate(getVal("Receipt[0].Bill[0].billDetails["+i+"].businessService"))} </strong>}/>
												   <CardText key={i2}>
															 {
																 item.map((item2, i2)=>{
																	 return renderFields(item2)
															 		})
														 		}
													 </CardText>
												  </CardHeader>
												 </Card>
										 )

							})*/}

            <Table id="dynamicTable" bordered responsive className="table-striped">
              <thead>
                <tr>
                  {resultList.resultHeader &&
                    resultList.resultHeader.length &&
                    resultList.resultHeader.map((item, i) => {
                      return (
                        <th style={{ textAlign: 'center' }} key={i}>
                          {translate(item.label)}
                        </th>
                      );
                    })}
                </tr>
              </thead>
              <tbody>
                {resultList.hasOwnProperty('resultValues') &&
                  resultList.resultValues.map((item, i) => {
                    return (
                      <tr key={i}>
                        {item.map((item2, i2) => {
                          return (
                            <td style={{ textAlign: 'center' }} key={i2}>
                              {renderFields(item2)}
                            </td>
                          );
                        })}
                      </tr>
                    );
                  })}
              </tbody>
            </Table>
            <strong>{'Amount paid (Rs)' + ' ' + getVal('Receipt[0].instrument.amount')}</strong>
          </CardText>
        </Card>
      );
    };

    return <div>{resultList && renderTable()}</div>;
  }
}

const mapStateToProps = state => ({ flag: state.report.flag });

const mapDispatchToProps = dispatch => ({
  setFlag: flag => {
    dispatch({ type: 'SET_FLAG', flag });
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(UiDynamicTable);
