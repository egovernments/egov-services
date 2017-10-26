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
						className="custom-form-control-for-textarea"
						id={item.jsonPath.split(".").join("-")}
						floatingLabelStyle={{"color": item.isDisabled ? "#A9A9A9" : "#696969", "fontSize": "20px", "white-space": "nowrap"}}
						inputStyle={{"color": "#5F5C57"}}
						floatingLabelFixed={true}
						style={{"display": (item.hide ? 'none' : 'inline-block')}}
						errorStyle={{"float":"left"}}
						fullWidth={true}
						multiLine={true}
						rows={2}
						maxLength={item.maxLength || ""}
						floatingLabelText={<span>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></span>}
						value={this.props.getVal(item.jsonPath)}
						disabled={item.isDisabled}
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

	render () {
		return (
	      <div>
	        {this.renderTextArea(this.props.item)}
	      </div>
	    );
	}
}
