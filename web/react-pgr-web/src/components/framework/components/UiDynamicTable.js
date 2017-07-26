import React, {Component} from 'react';
import {connect} from 'react-redux';
import {translate} from '../../common/common';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import UiTextField from './UiTextField'
import UiSelectField from './UiSelectField'
import UiButton from './UiButton'
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

export default class UiDynamicTable extends Component {
	constructor(props) {
       super(props);
   	}

   	render() {
   		let self = this;
   		const renderFields = function(field) {
   			switch(field.type) {
   				case 'text':
   					return (
   						<UiTextField ui="google" getVal={self.props.getVal} item={field}  fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
   					);
   				case 'singleValueList':
   					return (
   						<UiSelectField ui="google" getVal={self.props.getVal} item={field}  fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
   					);
   				case 'multiValueList':
   					return (
   						<UiMultiSelectField ui="google" getVal={self.props.getVal} item={field}  fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
   					);
   				case 'number':
   					return (
   						<UiNumberField ui="google" getVal={self.props.getVal} item={field}  fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
   					);
   				case 'textarea':
   					return (
   						<UiTextArea ui="google" getVal={self.props.getVal} item={field}  fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
   					);
   				case 'mobileNumber':
   					return (
   						<UiMobileNumber ui="google" getVal={self.props.getVal} item={field}  fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
   					);
   				case 'email':
   					return (
   						<UiEmailField ui="google" getVal={self.props.getVal} item={field}  fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
   					);
   				case 'datePicker':
   					return (
   						<UiDatePicker ui="google" getVal={self.props.getVal} item={field}  fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
   					);
   				case 'pan':
			        return (
			        	<UiPanCard ui="google" getVal={self.props.getVal} item={field} fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
			    	)
			      case 'aadhar':
			        return (
			        	<UiAadharCard ui="google" getVal={self.props.getVal} item={field} fieldErrors={self.props.fieldErrors} handler={self.props.handler}/>
   					)
   				case 'table':
   					renderTable(field);
               case 'label':
                  return <UiLabel getVal={this.props.getVal} item={field}/>
   			}
   		}

   		const renderTable = function(item) {
   			return (
   				<Card>
			      <CardHeader title={<strong> {item.label} </strong>}/>
			        <CardText>
			          <Table className="dynamicTable" bordered responsive>
				          <thead>
				            <tr>
				              {item.header && item.header.length && item.header.map((item2, i) => {
				                return (
				                  <th key={i}>{translate(item2.label)}</th>
				                )
				              })}
				            </tr>
				          </thead>
				          <tbody>
				                {
				                	item.values.map((item2, i2) => {
					                  return (
					                    <tr key={i2}>
                                    { item2.cols.map(function(item3, i3) {
                                       return (<td>{renderFields(item3)}</td>);
                                    })}
					                    </tr>
					                   )
				                	})
				                }
				          </tbody>
			          </Table>
			      	</CardText>
			    </Card>
   			)
   		}

   		return (
   			<div>{renderTable(self.props.item)}</div>
   		)
   	}
}