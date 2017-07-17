import React, {Component} from 'react';
import customTextField from './components/customTextField'
import customSelectField from './components/customSelectfield'

export default class ShowField extends Component {
  constructor(props) {
       super(props);
  }

  renderField(item) {
  	switch(item.type) {
  		case 'text': 
  			<CustomTextField ui="google" isRequired={item.isRequired} description={item.description} name={item.name} handler={this.props.handler}/>
  			break;
  		case 'singleValueList': 
  			<CustomTextField ui="google" isRequired={item.isRequired} description={item.description} name={item.name} handler={this.props.handler}/>
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
