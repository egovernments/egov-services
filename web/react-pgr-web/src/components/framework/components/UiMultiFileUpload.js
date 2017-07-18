import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiSingleFileUpload extends Component {
	constructor(props) {
       super(props);
   	}

	renderSingleFileUpload = (item) => {
		switch (this.props.ui) {
			case 'google': 
				return (
					<TextField 
						fullWidth={true} 
						multiple={true}
						type="file"
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
	        {this.renderSingleFileUpload(this.props.item)}
	      </div>
	    );
	}
}