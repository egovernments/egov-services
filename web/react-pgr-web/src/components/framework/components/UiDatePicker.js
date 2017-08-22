import React, {Component} from 'react';
import TextField from 'material-ui/TextField';

export default class UiEmailField extends Component {
	constructor(props) {
       super(props);
   	}

   	calcMinMaxDate = (dateStr) => {
   		if(dateStr) {
   			if(dateStr == "today") {
   				return new Date();
   			} else if(dateStr.indexOf("+") > -1) {
   				var oneDay = 24 * 60 * 60 * 1000;
   				dateStr = dateStr.split("+")[1];
   				return new Date(new Date().getTime() + (Number(dateStr) * oneDay)); 
   			} else {
   				var oneDay = 24 * 60 * 60 * 1000;
   				dateStr = dateStr.split("-")[1];
   				return new Date(new Date().getTime() - (Number(dateStr) * oneDay)); 
   			}
   		} else {
   			return "";
   		}
   	}

	renderDatePicker = (item) => {
		switch (this.props.ui) {
			case 'google':
				return (
					<TextField 
						style={{"display": (item.hide ? 'none' : 'inline-block')}}
						floatingLabelStyle={{"color": "#696969", "fontSize": "20px"}} 
						floatingLabelFixed={true} 
						disabled={item.isDisabled}
						hintText="21/12/1993" 
						floatingLabelText={<span>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></span>} 
						errorText={this.props.fieldErrors[item.jsonPath]}
						value={this.props.getVal(item.jsonPath)}
						onChange={(e) => {
                            this.props.handler(e, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)
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
