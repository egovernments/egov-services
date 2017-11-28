import React, {Component} from 'react';
// import TimePicker from 'material-ui/TimePicker';
import {translate} from '../../common/common';
var DateTimeField = require('react-bootstrap-datetimepicker');
var moment = require('moment');

export default class UiTimeField extends Component {
	constructor(props) {
       super(props);
   	}

renderTimePicker = (item) => {
	// let value ="";
	// if(this.props.getVal(item.jsonPath).split(':').length==2){
	// 	value=new Date('2017','12','1',this.props.getVal(item.jsonPath).split(':')[0],this.props.getVal(item.jsonPath).split(':')[1]);
	// }else if(item.defaultValue){
	// 	value=new Date('2017','12','1',item.defaultValue.split(':')[0],item.defaultValue.split(':')[1]);

	// }
		switch (this.props.ui) {
            case 'google':
				return (
					// <TimePicker
					// 	id={item.jsonPath.split(".").join("-")}
					// 	className="custom-form-control-for-textfield"
					// 	style={{"display": (item.hide ? 'none' : 'inline-block')}}
					// 	floatingLabelStyle={{"color": item.isDisabled ? "#A9A9A9" : "#696969", "fontSize": "20px", "white-space": "nowrap"}}
					// 	inputStyle={{"color": "#5F5C57"}}
					// 	floatingLabelFixed={true}
					// 	disabled={item.isDisabled}
					// 	hintText="12hr Format"
					// 	format="ampm"
					// 	floatingLabelText={<span>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></span>}
					// 	errorText={this.props.fieldErrors[item.jsonPath]}
                    //     value={value}//this.props.getVal(item.jsonPath)}
					// 	onChange={(e,time) => {debugger;
                    //      var val = time.getHours()+":"+time.getMinutes();
                    //         this.props.handler({target: {value: val}}, item.jsonPath, item.isRequired ? true : false, item.pattern, item.requiredErrMsg, item.patternErrMsg)
					//     }}/>
						<div style={{"marginTop": "17px", "display": (item.hide ? 'none' : 'inline-block')}} className="custom-form-control-for-datepicker">
						<label>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></label><br/>
						<DateTimeField
							mode='time'
							dateTime={this.props.getVal(item.jsonPath) || undefined}
							size='sm'
							inputFormat='h:mm A'
							inputProps={{
								"placeholder": "h:mm A",
								"id": item.jsonPath.split(".").join("-"),
								"disabled":item.isDisabled
							}}
							defaultText=""
							onChange={(e) => {debugger;
		                            this.props.handler({target: {value: e}}, item.jsonPath, item.isRequired ? true : false, /\d{12,13}/, item.requiredErrMsg, (item.patternErrMsg || translate("framework.time.error.message")), item.expression, item.expressionMsg, true)
		                        }}
							/>
						<div style={{"height": "23px" ,"visibility": (this.props.fieldErrors && this.props.fieldErrors[item.jsonPath] ? "visible" : "hidden"), "position": "relative", "fontSize": "12px", "lineHeight": "23px", "color": "rgb(244, 67, 54)", "transition": "all 450ms cubic-bezier(0.23, 1, 0.32, 1) 0ms", "float": "left"}}>
							{this.props.fieldErrors && this.props.fieldErrors[item.jsonPath] ? this.props.fieldErrors[item.jsonPath] : " "}
						</div>
					</div>
				);
		}
	}

	render () {
		return (
	      this.renderTimePicker(this.props.item)
	    );
	}
}