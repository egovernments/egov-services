import React, {Component} from 'react';
import DatePicker from 'material-ui/DatePicker';

export default class UiEmailField extends Component {
	constructor(props) {
       super(props);
   	}

	renderDatePicker = (item) => {
		switch (item.ui) {
			case 'google': 
				return (
					<DatePicker 
						hintText={item.label + (item.isRequired ? " *" : "")} 
						disabled={item.isDisabled} 
						value={eval(item.jsonpath)}
						errorText={this.props.fieldErrors[eval(item.jsonpath)]}
						onChange={(e) => this.props.handler(e, eval(item.jsonpath), item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)}/>
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderDatePicker(this.props.item)}
	      </div>
	    );
	}
}