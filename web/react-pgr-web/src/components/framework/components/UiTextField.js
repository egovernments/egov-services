import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import {orange500, blue500} from 'material-ui/styles/colors';

const styles = {
  errorStyle: {
    color: orange500,
  },
  underlineStyle: {
    borderColor: orange500,
  },
  floatingLabelStyle: {
    color: orange500,
  },
  floatingLabelFocusStyle: {
    color: blue500,
  },
};

export default class UiTextField extends Component {
	constructor(props) {
       super(props);
   	}

	renderTextBox = (item) => {
		switch (this.props.ui) {
			case 'google':
				if (item.hasOwnProperty("isLabel") && !item.isLabel) {
					return (
						<TextField
							style={{"display": (item.hide ? 'none' : 'inline-block')}}
							errorStyle={{"float":"left"}}
							fullWidth={false}
							floatingLabelText={item.label + (item.isRequired ? " *": "")}
							

							value={this.props.getVal(item.jsonPath)}
							disabled={item.isDisabled}
							errorText={this.props.fieldErrors[item.jsonPath]}
							onChange={(e) => this.props.handler(e, item.jsonPath, item.isRequired ? true : false, item.pattern, item.requiredErrMsg, item.patternErrMsg)} />
					);
				}
				else {
					return (
						<TextField
							style={{"display": (item.hide ? 'none' : 'inline-block')}}
							errorStyle={{"float":"left"}}
							fullWidth={true}
							floatingLabelText={item.label + (item.isRequired ? " *" : "")}
							value={this.props.getVal(item.jsonPath)}
							disabled={item.isDisabled}
							errorText={this.props.fieldErrors[item.jsonPath]}
							onChange={(e) => this.props.handler(e, item.jsonPath, item.isRequired ? true : false, item.pattern, item.requiredErrMsg, item.patternErrMsg)} />
					);
				}
		}
	}

	render () {
		return (
	      <div>
	        {this.renderTextBox(this.props.item)}
	      </div>
	    );
	}
}
