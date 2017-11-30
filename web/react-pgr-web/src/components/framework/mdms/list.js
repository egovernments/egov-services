import React, { Component } from 'react';
import { connect } from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import UiMultiFieldAddToTable from '../components/UiMultiFieldAddToTable';
import { translate } from '../../common/common';
import _ from 'lodash';

class MdmsComponent extends Component {
	constructor(props) {
    	super(props);
    	this.state = {
    		item: {},
    		isBtnDisabled: false
    	};
	}

	componentDidMount() {
		//Make call to node server
		//Make call to MDMS service
		//URL path to this page /mdms/:module/:master
		//Get all the fields and pass to table component directly
		//After successful edit/create, make call to mdms _create/_update
	}

	handleChange(e, property) {

	}

	setDisabled (bool) {
		this.setState({
			isBtnDSisabled: bool || false
		})
	}

	addOrUpdate() {

	}

	render() {
		let {item, isBtnDisabled, addOrUpdate} = this.state;
		let {handleChange, setDisabled} = this;
		return (
			<div>
				{
					item && (Object.keys(item).length && item.values && item.values.length)
					?
					<UiMultiFieldAddToTable
						ui="google"
						useTimestamp={true}
						handler={handleChange}
						item={item}
						setDisabled={setDisabled}/>
					:
					<div></div>
				}
				<br/>
				<RaisedButton
					label={translate("ui.framework.submit")}
					onClick={addOrUpdate}
					primary={true}
					disabled={isBtnDisabled}/>
			</div>
		)
	}
}

const mapStateToProps = state => ({
  formData: state.frameworkForm.form
})

const mapDispatchToProps = dispatch => ({
  setFormData: (data) => {
    dispatch({ type: "SET_FORM_DATA", data });
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(MdmsComponent);