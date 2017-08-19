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
						style={{"display": (item.hide ? 'none' : 'inline-block')}}
						errorStyle={{"float":"left"}}
						fullWidth={true} 
						floatingLabelText={item.label + (item.isRequired ? " *" : "")} 
						value={this.props.getVal(item.jsonPath)}
						disabled={item.isDisabled}
						errorText={this.props.fieldErrors[item.jsonPath]}
						onChange={(e) => {
							this.props.handler(e, item.jsonPath, item.isRequired ? true : false, '^(([^<>()[\\]\\.,;:\\s@\\"]+(\.[^<>()[\\]\\.,;:\s@\\"]+)*)|(\\".+\\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$', item.requiredErrMsg, item.patternErrMsg)
						}} />
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