import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiEmailField extends Component {
	constructor(props) {
       super(props);
   	}

	renderEmailBox = (item) => {
		switch (this.props.ui) {
			case 'google': 
				return (
					<TextField 
						fullWidth={true} 
						type="email"
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
	        {this.renderEmailBox(this.props.item)}
	      </div>
	    );
	}
}