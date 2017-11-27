import React, {Component} from 'react';
import {connect} from 'react-redux';
import Checkbox from 'material-ui/Checkbox';
import _ from 'lodash';

class UiCheckBox extends Component {
	constructor(props) {
	   super(props);
	}

	getVal = (path, dateBool) => {
    var _val = _.get(this.props.formData, path);
    if(dateBool && typeof _val == 'string' && _val && _val.indexOf("-") > -1) {
      var _date = _val.split("-");
      return new Date(_date[0], (Number(_date[1])-1), _date[2]);
    }

    return typeof _val != "undefined" ? _val : "";
  }

	renderCheckBox = (item,isSelected) => {
		let {formData} = this.props;
		let {getVal}=this;
		let tempDisabled = eval(item.isDisabled);
		switch (this.props.ui) {
			case 'google':
				return (
					<Checkbox
						id={item.jsonPath.split(".").join("-")}
						style={{"display": (item.hide ? 'none' : 'inline-block'), "marginTop": "43px", "marginLeft": "-5px"}}
						label={item.label + (item.isRequired ? " *" : "")}
						checked={this.props.getVal?this.props.getVal(item.jsonPath):isSelected}
						disabled={tempDisabled}
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

const mapStateToProps = state => ({
	formData: state.frameworkForm.form
});

const mapDispatchToProps = dispatch => ({
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(UiCheckBox);
