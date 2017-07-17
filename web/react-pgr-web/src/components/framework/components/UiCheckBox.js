import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiCheckBox extends Component {
	constructor(props) {
       super(props);
   	}

	renderCheckBox = (item) => {
		switch (item.ui) {
			case 'google': 
				return (
					<TextField 
						fullWidth={true} 
						type="checkbox"
						floatingLabelText={item.label + (item.isRequired ? " *" : "")} 
						value={eval(item.jsonpath)}
						onChange={(e) => this.props.handler(e, eval(item.jsonpath), item.isRequired ? true : false, '')} />
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderCheckBox(this.props.item)}
	      </div>
	    );
	}
}