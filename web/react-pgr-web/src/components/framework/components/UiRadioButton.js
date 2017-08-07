import React, {Component} from 'react';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import {translate} from '../../common/common';

export default class UiAadharCard extends Component {
	constructor(props) {
       super(props);
   	}

   	renderRadioButtons = (values) => {
   		return values.map((v, i) => {
   			return (
   				<RadioButton
			        value={v.value}
			        label={translate(v.label)}
			      />
   			);
   		})
   	}

   	renderRadioButtonGroup = (item) => {
         //console.log(item.name + "-" + );
   		switch (this.props.ui) {
			case 'google': 
				return (
					<div style={{"display": (item.hide ? 'none' : 'inline-block')}}>
						<label>{item.label}</label>
						<RadioButtonGroup name="shipSpeed" valueSelected={this.props.getVal(item.jsonPath)} defaultSelected={item.defaultSelected} onChange={(e, val) => {
							this.props.handler({target:{value: val}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)
						}}>
							{this.renderRadioButtons(item.values)}
						</RadioButtonGroup>
					</div>
				)
		}
   	}

   	render () {
   		return (
   			<div>
   				{this.renderRadioButtonGroup(this.props.item)}
   			</div>
   		)
   	}
}