import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiPanCard extends Component {
	constructor(props) {
       super(props);
   	}

	renderPanCard = (item) => {
		switch (this.props.ui) {
			case 'google': 
				return (
					<TextField 
						style={{"display": (item.hide ? 'none' : 'block')}}
						fullWidth={true} 
						floatingLabelText={item.label + (item.isRequired ? " *" : "")} 
						value={this.props.getVal(item.jsonPath)}
						disabled={item.isDisabled}
						errorText={this.props.fieldErrors[item.jsonPath]}
						onChange={(e) => this.props.handler(e, item.jsonPath, item.isRequired ? true : false, '/^[a-zA-z]{5}\d{4}[a-zA-Z]{1}$/', item.requiredErrMsg, item.patternErrMsg)} />
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderPanCard(this.props.item)}
	      </div>
	    );
	}
}