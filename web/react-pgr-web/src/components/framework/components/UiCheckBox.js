import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiCheckBox extends Component {
	constructor(props) {
       super(props);
   	}

	renderCheckBox = (item) => {
		switch (this.props.ui) {
			case 'google': 
				return (
					<TextField 
						fullWidth={true} 
						type="checkbox"
						floatingLabelText={item.label + (item.isRequired ? " *" : "")} 
						value={this.props.getVal(item.jsonPath)}
						disabled={item.isDisabled}
						errorText={this.props.fieldErrors[item.jsonPath]}
						onChange={(e) => this.props.handler(e, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)} />
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderCheckBox(this.props.item)}
	      </div>
	    );
	}
}