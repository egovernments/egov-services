import React, { Component } from 'react';
import { connect } from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import UiMultiFieldAddToTable from '../components/UiMultiFieldAddToTable';
import { translate } from '../../common/common';
import { Api } from '../../../api/api';
import _ from 'lodash';

class MdmsComponent extends Component {
	constructor(props) {
    	super(props);
    	this.state = {
    		item: {},
    		isBtnDisabled: false,
    		valueList: []
    	};
	}

	componentDidMount() {
		//Make call to node server
		//Make call to MDMS service
		//URL path to this page /mdms/:module/:master
		//Get all the fields and pass to table component directly
		//After successful edit/create, make call to mdms _create/_update
		let self = this;
		let module = this.props.match.params.module;
		let master = this.props.match.params.master;
		let data = {
			MdmsCriteria: {
				tenantId: localStorage.tenantId,
				moduleDetails: [{
					moduleName: module,
					masterDetails: [{
						name: master
					}]
				}]
			}
		};

		self.props.setLoadingStatus('loading');
		//Fetch specs from specs service
		Api.commonApiPost("/specs/yaml/_search", {
			module,
			master
		}).then(function(res) {
			//Fetch data from MDMS service
			Api.commonApiPost("/egov-mdms-service/v1/_search", {}, data, false, true).then(function(res) {
				let arr = _.get(res, "$.MdmsRes." + module + "." + master);
				if(arr && arr.length) {
					let formData = _.get(res, "$.MdmsRes");
					self.props.setFormData(formData);
					self.setState({
						valueList: arr
					})
				}
			}).catch(function(err) {
				self.props.setLoadingStatus('hide');
				self.props.toggleSnackbarAndSetText(true, err.message);
			})
		}).catch(function(err) {
			self.props.setLoadingStatus('hide');
			self.props.toggleSnackbarAndSetText(true, err.message);
		})
	}

	handleChange(e, property) {
		let {formData} = this.props.formData;
		_.set(formData, property, e.target.value);
		this.props.setFormData(formData);
	}

	setDisabled (bool) {
		this.setState({
			isBtnDSisabled: bool || false
		})
	}

	addOrUpdate() {

	}

	render() {
		let {item, isBtnDisabled, valueList} = this.state;
		let {handleChange, setDisabled, addOrUpdate} = this;
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
						valueList={valueList}
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
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(MdmsComponent);