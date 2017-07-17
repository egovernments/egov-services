import React, {Component} from 'react';
import SelectField from 'material-ui/SelectField';

export default class CustomTextField extends Component {
	constructor(props) {
       super(props);
   	}

	renderSelect = (item) => {
		switch (item.ui) {
			case 'google': 
					<SelectField 
						fullWidth={true} 
						floatingLabelText={item.description + (item.isRequired ? " *" : "")} 
						value={item.name} 
						onChange={(e) => this.props.handler(e, item.name, item.isRequired ? true : false, '')} 
						maxHeight={200}>
				            {item.dropDownData.map((dd, index) => (
				                <MenuItem value={dd.key} key={index} primaryText={dd.value} />
				            ))}
		            </SelectField>
		}
	}

	render () {
		return (
	      <div>
	        {this.renderSelect(this.props.item)}
	      </div>
	    );
	}
}