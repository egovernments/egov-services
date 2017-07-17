import React, {Component} from 'react';
import UiTextField from './components/UiTextField'
import UiSelectField from './components/UiSelectField'
import UiButton from './components/UiButton'
import UiCheckBox from './components/UiCheckBox'
import UiEmailField from './components/UiEmailField'
import UiMobileNumber from './components/UiMobileNumber'
import UiTextArea from './components/UiTextArea'
import UiMultiSelectField from './components/UiMultiSelectField'
import UiNumberField from './components/UiNumberField'


export default class ShowField extends Component {
  constructor(props) {
       super(props);
  }

  renderField(item) {
  	switch(item.type) {
  		case 'text': 
  			<UiTextField ui="google" isDisabled={item.isDisabled} isRequired={item.isRequired} label={item.label} jsonpath={item.jsonpath} handler={this.props.handler}/>
  			break;
  		case 'singleValueList': 
  			<UiSelectField ui="google" isDisabled={item.isDisabled} isRequired={item.isRequired} label={item.label} jsonpath={item.jsonpath} handler={this.props.handler} dropDownData={item.dropDownData}/>
  			break;
  		case 'multiValueList': 
  			<UiMultiSelectField ui="google" isDisabled={item.isDisabled} isRequired={item.isRequired} label={item.label} jsonpath={item.jsonpath} handler={this.props.handler} dropDownData={item.dropDownData}/>
  			break;
  		case 'number': 
  			<UiNumberField ui="google" isDisabled={item.isDisabled} isRequired={item.isRequired} label={item.label} jsonpath={item.jsonpath} handler={this.props.handler}/>
  			break;
  		case 'textarea': 
  			<UiTextArea ui="google" isDisabled={item.isDisabled} isRequired={item.isRequired} label={item.label} jsonpath={item.jsonpath} handler={this.props.handler}/>
  			break;
  		case 'mobileNumber': 
  			<UiMobileNumber ui="google" isDisabled={item.isDisabled} isRequired={item.isRequired} label={item.label} jsonpath={item.jsonpath} handler={this.props.handler}/>
  			break;
  		case 'checkbox': 
  			<UiCheckBox ui="google" isDisabled={item.isDisabled} isRequired={item.isRequired} label={item.label} jsonpath={item.jsonpath} handler={this.props.handler}/>
  			break;
  		case 'email': 
  			<UiEmailField ui="google" isDisabled={item.isDisabled} isRequired={item.isRequired} label={item.label} jsonpath={item.jsonpath} handler={this.props.handler}/>
  			break;
  		case 'button': 
  			<UiButton ui="google" isDisabled={item.isDisabled} label={item.label} handler={this.props.handler}/>
  			break;
  	}
  }

  render() {
  	return 
  	 <div>
  	 	{this.renderField(this.props.item)}
  	 </div>
  }
}
