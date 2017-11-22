import React, {Component} from 'react';
import Checkbox from 'material-ui/Checkbox';

export default class UiCheckBox extends Component {
	constructor(props) {
       super(props);
   	}

	renderCheckBox = (item,isSelected) => {
		switch (this.props.ui) {
			case 'google':
				return (
					<Checkbox
						id={item.jsonPath.split(".").join("-")}		
						style={{"display": (item.hide ? 'none' : 'inline-block'), "marginTop": "43px", "marginLeft": "-5px"}}
						label={item.label + (item.isRequired ? " *" : "")}
						checked={this.props.getVal?this.props.getVal(item.jsonPath):isSelected}
						disabled={item.isDisabled}
						errorText={this.props.fieldErrors?this.props.fieldErrors[item.jsonPath]:"Empty"}
						onCheck={(e) => this.props.handler({target: {value: e.target.checked}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg, item.expression, item.expressionMsg)} />
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderCheckBox(this.props.item,this.props.isSelected)}
	      </div>
	    );
	}
}
