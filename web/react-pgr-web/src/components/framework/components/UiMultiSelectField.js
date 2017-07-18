import React, {Component} from 'react';
import SelectField from 'material-ui/SelectField';

export default class UiMultiSelectField extends Component {
	constructor(props) {
       super(props);
   	}

	renderMultiSelect = (item) => {
		switch (this.props.ui) {
			case 'google': 
				return (
					<SelectField 
						fullWidth={true} 
						multi={true}
						floatingLabelText={item.label + (item.isRequired ? " *" : "")} 
						value={eval(item.jsonpath)}
						disabled={item.isDisabled}
						onChange={(e) => this.props.handler(e, eval(item.jsonpath), item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)} 
						errorText={this.props.fieldErrors[eval(item.jsonpath)]}
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
	        {this.renderMultiSelect(this.props.item)}
	      </div>
	    );
	}
}