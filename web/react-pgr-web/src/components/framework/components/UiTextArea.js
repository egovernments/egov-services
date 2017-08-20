import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiTextArea extends Component {
	constructor(props) {
       super(props);
   	}

	renderTextArea = (item) => {
		switch (this.props.ui) {
			case 'google':
				return (
					<TextField
						style={{"display": (item.hide ? 'none' : 'inline-block')}}
						errorStyle={{"float":"left"}}
						fullWidth={true}
						multiLine={true}
						rows={2}
						floatingLabelText={item.label + (item.isRequired ? " *" : "")}
						value={this.props.getVal(item.jsonPath)}
						disabled={item.isDisabled}
						errorText={this.props.fieldErrors[item.jsonPath]}
						onChange={(e) => this.props.handler(e, item.jsonPath, item.isRequired ? true : false, item.pattern, item.requiredErrMsg, item.patternErrMsg)} />
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderTextArea(this.props.item)}
	      </div>
	    );
	}
}
