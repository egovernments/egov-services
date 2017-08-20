import React, {Component} from 'react';
import DatePicker from 'material-ui/DatePicker';

export default class UiEmailField extends Component {
	constructor(props) {
       super(props);
   	}

	renderDatePicker = (item) => {
		switch (this.props.ui) {
			case 'google':
				return (
					<DatePicker
						style={{"display": (item.hide ? 'none' : 'block'), "marginTop": "24px"}}
						hintText={item.label + (item.isRequired ? " *" : "")}
						disabled={item.isDisabled}
						formatDate={function(date) {
							date =new Date(date);
							return ('0' + date.getDate()).slice(-2) + '/'
             						+ ('0' + (date.getMonth()+1)).slice(-2) + '/'
             						+ date.getFullYear();
						}}
						value={this.props.getVal(item.jsonPath, true)!=""?new Date(this.props.getVal(item.jsonPath, true)):{}}
						errorText={this.props.fieldErrors[item.jsonPath]}
						onChange={(ev, dat) => {
							this.props.handler({target: {value: dat.getTime()}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)
						}}/>
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderDatePicker(this.props.item)}
	      </div>
	    );
	}
}
