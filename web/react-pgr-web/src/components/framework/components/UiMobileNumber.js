import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiMobileField extends Component {
	constructor(props) {
       super(props);
   	}

	renderMobileNumberBox = (item) => {
		switch (this.props.ui) {
			case 'google': 
				return (
					<TextField 
						style={{"display": (item.hide ? 'none' : 'inline-block')}}
						errorStyle={{"float":"left"}}
						fullWidth={true} 
						type="text"
						maxLength="10"
						floatingLabelText={item.label + (item.isRequired ? " *" : "")} 
						value={this.props.getVal(item.jsonPath)}
						disabled={item.isDisabled}
						errorText={this.props.fieldErrors[item.jsonPath]}
						onChange={(e) => {
							if(e.target.value && !/^\d*$/g.test(e.target.value)) return;
							this.props.handler(e, item.jsonPath, item.isRequired ? true : false, '^\\d{10}$', item.requiredErrMsg, item.patternErrMsg)
						}} />
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderMobileNumberBox(this.props.item)}
	      </div>
	    );
	}
}