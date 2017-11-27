import React, {Component} from 'react';
import TextField from 'material-ui/TextField';
import {orange500, blue500} from 'material-ui/styles/colors';

const styles = {
  errorStyle: {
    color: orange500,
  },
  underlineStyle: {
    borderColor: orange500,
  },
  floatingLabelStyle: {
    color: orange500,
  },
  floatingLabelFocusStyle: {
    color: blue500,
  },
};

export default class UiImage extends Component {
	constructor(props) {
       super(props);
		 }

	renderImage = (item) => {
    console.log(item);
		var disabledValue=false;
		     if(item.isDisablePath && (typeof this.props.getVal(item.isDisablePath)== "boolean")){
						 disabledValue = !this.props.getVal(item.isDisablePath);
					 }else{
						 disabledValue = item.isDisabled;
					 }
		switch (this.props.ui) {
			case 'google':

					return (
            <div>
            <label className="custom-form-control-for-textfield">
              <span>
                {item.label}
              </span>
            </label> <br/>
              <img src={item.imagePath}  width="20%" height = "60%"  label= {item.label}/>
            </div>
						// <TextField
            //   className="custom-form-control-for-textfield"
            //   id={item.jsonPath.split(".").join("-")}
						// 	inputStyle={{"color": "#5F5C57","textAlign":item.hasOwnProperty("textAlign")?item.textAlign:"left"}}
						// 	style={{"display": (item.hide ? 'none' : 'inline-block')}}
						// 	errorStyle={{"float":"left"}}
						// 	fullWidth={false}
						// 	maxLength={item.maxLength || ""}
						// 	value={this.props.getVal(item.jsonPath)}
						// 	disabled={disabledValue}
						// 	errorText={this.props.fieldErrors[item.jsonPath]}
						// 	onChange={(e) => {
						// 		if(e.target.value) {
						// 			e.target.value = e.target.value.replace(/^\s*/, "");
						// 			if(e.target.value[e.target.value.length-1] == " " && e.target.value[e.target.value.length-2] == " ")
						// 				return;
						// 		}
						// 		this.props.handler(e, item.jsonPath, item.isRequired ? true : false, item.pattern, item.requiredErrMsg, item.patternErrMsg)}
						// 	} />
					);
		}
	}

	render () {

		return (
	        this.renderImage(this.props.item)

	    );
	}
}
