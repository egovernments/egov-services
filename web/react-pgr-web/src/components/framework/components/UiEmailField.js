import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiEmailField extends Component {
	constructor(props) {
       super(props);
   	}

	renderEmailBox = (item) => {
		switch (item.ui) {
			case 'google': 
				return (
					<TextField 
						fullWidth={true} 
						type="email"
						floatingLabelText={item.label + (item.isRequired ? " *" : "")} 
						value={eval(item.jsonpath)}
						disabled={item.isDisabled}
						errorText={this.props.fieldErrors[eval(item.jsonpath)]}
						onChange={(e) => this.props.handler(e, eval(item.jsonpath), item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)} />
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