import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class CustomTextField extends Component {
	constructor(props) {
       super(props);
   	}

	renderTextBox = (item) => {
		switch (item.ui) {
			case 'google': 
					<TextField 
						fullWidth={true} 
						floatingLabelText={item.description + (item.isRequired ? " *" : "")} 
						onChange={(e) => this.props.handler(e, item.name, item.isRequired ? true : false, '')} />
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