import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiTextField extends Component {
	constructor(props) {
       super(props);
   	}

	renderTextBox = (item) => {
		switch (item.ui) {
			case 'google': 
				return (
					<TextField 
						fullWidth={true} 
						floatingLabelText={item.label + (item.isRequired ? " *" : "")} 
						value={eval(item.jsonpath)}
						onChange={(e) => this.props.handler(e, eval(item.jsonpath), item.isRequired ? true : false, '')} />
				);
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