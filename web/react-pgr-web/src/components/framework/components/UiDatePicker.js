import React, {Component} from 'react';
import DatePicker from 'material-ui/DatePicker';

export default class UiEmailField extends Component {
	constructor(props) {
       super(props);
   	}

	renderDatePicker = (item) => {
		switch (this.props.ui) {
			case 'google': 
				return (
					<DatePicker 
						hintText={item.label + (item.isRequired ? " *" : "")} 
						disabled={item.isDisabled} 
						value={this.props.getVal(item.jsonPath)}
						errorText={this.props.fieldErrors[item.jsonPath]}
						onChange={(ev, dat) => {
							this.props.handler({target: {value: dat}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)
						}}/>
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