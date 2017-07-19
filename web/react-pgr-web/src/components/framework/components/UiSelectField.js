import React, {Component} from 'react';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import Api from '../../../api/api';


export default class UiTextField extends Component {
	constructor(props) {
       super(props);
			 this.state={
				 dropDownData:[]
			 }
   	}



	 renderSelect =(item) => {
		let {dropDownData}=this.state;
		console.log(item);

		switch (this.props.ui) {
			case 'google':
				let {dropDownData}=this.state;
				return (
					<SelectField
						fullWidth={true}
						floatingLabelText={item.label + (item.isRequired ? " *" : "")}
						value={eval(item.jsonpath)}
						onChange={(e) => this.props.handler(e, eval(item.jsonpath), item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)}
						disabled={item.isDisabled}
						errorText={this.props.fieldErrors[eval(item.jsonpath)]}
						maxHeight={200}>
				            {dropDownData.map((dd, index) => (
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
