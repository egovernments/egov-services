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
import UiDatePicker from './components/UiDatePicker'

export default class ShowField extends Component {
  constructor(props) {
       super(props);
  }

  renderField(item) {
  	switch(item.type) {
  		case 'text': 
  			<UiTextField ui="google" handler={this.props.handler}/>
  			break;
  		case 'singleValueList': 
  			<UiSelectField ui="google" handler={this.props.handler}/>
  			break;
  		case 'multiValueList': 
  			<UiMultiSelectField ui="google" />
  			break;
  		case 'number': 
  			<UiNumberField ui="google" handler={this.props.handler}/>
  			break;
  		case 'textarea': 
  			<UiTextArea ui="google" handler={this.props.handler}/>
  			break;
  		case 'mobileNumber': 
  			<UiMobileNumber ui="google" handler={this.props.handler}/>
  			break;
  		case 'checkbox': 
  			<UiCheckBox ui="google" handler={this.props.handler}/>
  			break;
  		case 'email': 
  			<UiEmailField ui="google" handler={this.props.handler}/>
  			break;
  		case 'button': 
  			<UiButton ui="google" handler={this.props.handler}/>
  			break;
  		case 'datePicker':
  			<UiDatePicker ui="google" handler={this.props.handler}/>
  	}
  }

  render() {
  	return 
  	 <div>
  	 	{this.renderField(this.props.item)}
  	 </div>
  }
}
