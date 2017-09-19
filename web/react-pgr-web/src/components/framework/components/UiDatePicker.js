import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
const datePat = /^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/g;
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
   				return new Date(new Date().getTime() + (Number(dateStr) * oneDay)).getTime();
   			} else {
   				var oneDay = 24 * 60 * 60 * 1000;
   				dateStr = dateStr.split("-")[1];
   				return new Date(new Date().getTime() - (Number(dateStr) * oneDay)).getTime();
   			}
   		} else {
   			return "";
   		}
   	}

   	getDateFormat = (timeLong) => {
   		if(timeLong) {
   			if((timeLong.toString().length == 12 || timeLong.toString().length == 13) && new Date(Number(timeLong)).getTime() > 0) {
	   			var _date = new Date(Number(timeLong));
	   			return ('0' + _date.getDate()).slice(-2) + '/'
	             + ('0' + (_date.getMonth()+1)).slice(-2) + '/'
	             + _date.getFullYear();
	   		} else {
	   			return timeLong;
	   		}
   		}
   	}

	renderDatePicker = (item) => {
		switch (this.props.ui) {
			case 'google':
				return (
					<TextField
						id={item.jsonPath.split(".").join("-")}
						style={{"display": (item.hide ? 'none' : 'inline-block')}}
						floatingLabelStyle={{"color": item.isDisabled ? "#A9A9A9" : "#696969", "fontSize": "20px", "white-space": "nowrap"}}
						inputStyle={{"color": "#5F5C57"}}
						floatingLabelFixed={true}
						disabled={item.isDisabled}
						hintText="DD/MM/YYYY"
						maxLength={10}
						floatingLabelText={<span>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></span>}
						errorText={this.props.fieldErrors[item.jsonPath]}
						value={this.getDateFormat(this.props.getVal(item.jsonPath))}
						onChange={(e) => {

							var val = e.target.value;
							console.log(val);
							if(e.target.value.length == 2 && !e.target.value.match('/')){
								val+='/';
							} else if(e.target.value.length == 5) {
								var a = e.target.value.split('/');
								if(!a[1].match('/')){
									val+='/';
								}
							}
							if(e.target.value) {
								e.target.value = e.target.value.trim();
								if(datePat.test(e.target.value)){
									var _date = e.target.value;
									_date = _date.split("/");
									var newDate = _date[1]+"-"+_date[0]+"-"+_date[2];
									val = Number(new Date(newDate).getTime());
									if(item.minDate && val< this.calcMinMaxDate(item.minDate)) {
									return ;
								} else if(item.maxDate && val > this.calcMinMaxDate(item.maxDate)) {
										return ;
									}
								}
							}

                            this.props.handler({target: {value: val}}, item.jsonPath, item.isRequired ? true : false, /\d{12,13}/, item.requiredErrMsg, item.patternErrMsg)
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
