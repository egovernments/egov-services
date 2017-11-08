import React, {Component} from 'react';
import TimePicker from 'material-ui/TimePicker';

export default class UiTimeField extends Component {
	constructor(props) {
       super(props);
   	}

renderTimePicker = (item) => {
		switch (this.props.ui) {
            case 'google':
				return (
					<TimePicker
						id={item.jsonPath.split(".").join("-")}
						className="custom-form-control-for-textfield"
						style={{"display": (item.hide ? 'none' : 'inline-block')}}
						floatingLabelStyle={{"color": item.isDisabled ? "#A9A9A9" : "#696969", "fontSize": "20px", "white-space": "nowrap"}}
						inputStyle={{"color": "#5F5C57"}}
						floatingLabelFixed={true}
						disabled={item.isDisabled}
						hintText="12hr Format"
						format="ampm"
						floatingLabelText={<span>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></span>}
						errorText={this.props.fieldErrors[item.jsonPath]}
                        value={new Date('2017','12','1',this.props.getVal(item.jsonPath).split(':')[0],this.props.getVal(item.jsonPath).split(':')[1])}//this.props.getVal(item.jsonPath)}
						onChange={(e,time) => {debugger;
                         var val = time.getHours()+":"+time.getMinutes();
                            this.props.handler({target: {value: val}}, item.jsonPath, item.isRequired ? true : false, item.pattern, item.requiredErrMsg, item.patternErrMsg)
                        }}/>
				);
		}
	}

	render () {
		return (
	      this.renderTimePicker(this.props.item)
	    );
	}
}