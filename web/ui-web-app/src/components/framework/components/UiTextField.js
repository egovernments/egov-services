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
		var disabledValue=false;
		     if(item.isDisablePath && (typeof this.props.getVal(item.isDisablePath)== "boolean")){
						 disabledValue = !this.props.getVal(item.isDisablePath);
					 }else{
						 disabledValue = item.isDisabled;
					 }
		switch (this.props.ui) {
			case 'google':
				if (item.hasOwnProperty("isLabel") && !item.isLabel) {
					return (
						<TextField
              className="custom-form-control-for-textfield"
              id={item.jsonPath.split(".").join("-")}
							inputStyle={{"color": "#5F5C57","textAlign":item.hasOwnProperty("textAlign")?item.textAlign:"left"}}
							style={{"display": (item.hide ? 'none' : 'inline-block')}}
							errorStyle={{"float":"left"}}
							fullWidth={false}
							maxLength={item.maxLength || ""}
							value={this.props.getVal(item.jsonPath)}
							disabled={disabledValue}
							errorText={this.props.fieldErrors[item.jsonPath]}
							onChange={(e) => {
								if(e.target.value) {
									e.target.value = e.target.value.replace(/^\s*/, "");
									if(e.target.value[e.target.value.length-1] == " " && e.target.value[e.target.value.length-2] == " ")
										return;
								}
								this.props.handler(e, item.jsonPath, item.isRequired ? true : false, item.pattern, item.requiredErrMsg, item.patternErrMsg)}
							} />
					);
				}
				else {
					return (
						<TextField
              className="custom-form-control-for-textfield"
              id={item.jsonPath.split(".").join("-")}
							floatingLabelStyle={{"color": item.isDisabled ? "#A9A9A9" : "#696969", "fontSize": "20px", "white-space": "nowrap"}}
              inputStyle={{"color": "#5F5C57","textAlign":item.hasOwnProperty("textAlign")?item.textAlign:"left"}}
							floatingLabelFixed={true}
							maxLength={item.maxLength || ""}
							style={{"display": (item.hide ? 'none' : 'inline-block')}}
							errorStyle={{"float":"left"}}
							fullWidth={true}
							floatingLabelText={<span>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></span>}
							value={this.props.getVal(item.jsonPath)}
							disabled={disabledValue}
							errorText={this.props.fieldErrors[item.jsonPath]}
							onChange={(e) => {
								if(e.target.value) {
									e.target.value = e.target.value.replace(/^\s*/, "");
									if(e.target.value[e.target.value.length-1] == " " && e.target.value[e.target.value.length-2] == " ")
										return;
								}
								this.props.handler(e, item.jsonPath, item.isRequired ? true : false, item.pattern, item.requiredErrMsg, item.patternErrMsg, item.expression, item.expressionMsg)}
							} />
					);
				}
		}
	}

	render () {
		return (
	        this.renderTextBox(this.props.item)

	    );
	}
}
