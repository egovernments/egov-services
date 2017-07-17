import React, {Component} from 'react';
import SelectField from 'material-ui/SelectField';

export default class UiTextField extends Component {
	constructor(props) {
       super(props);
   	}

	renderSelect = (item) => {
		switch (item.ui) {
			case 'google': 
				return (
					<SelectField 
						fullWidth={true} 
						floatingLabelText={item.label + (item.isRequired ? " *" : "")} 
						value={eval(item.jsonpath)} 
						onChange={(e) => this.props.handler(e, eval(item.jsonpath), item.isRequired ? true : false, '')} 
						maxHeight={200}>
				            {item.dropDownData.map((dd, index) => (
				                <MenuItem value={dd.key} key={index} primaryText={dd.value} />
				            ))}
		            </SelectField>
				);
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