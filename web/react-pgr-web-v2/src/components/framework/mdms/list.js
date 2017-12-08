import React, { Component } from 'react';
import { connect } from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import UiMultiFieldAddToTable from '../components/UiMultiFieldAddToTable';
import { translate } from '../../common/common';
import Api from '../../../api/api';
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

	// checkColumnName(headers,column)
	// {
	// 	for (var i = 0; i < headers.length; i++) {
	// 		if(headers[i].label.split(".")[3]==column)
	// 		{
	// 			return true
	// 		}
	// 	}
	// 	return false
	// }

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

		let formData = {
			MasterMetaData: {
				tenantId: localStorage.tenantId,
				moduleName: module,
				masterName: master,
				masterData: []
			}
		};

		self.props.setLoadingStatus('loading');

		//Fetch specs from specs service

		// court,caseType,side,caseType,caseCategory,bench,stamp and charge
		let lcms={
			"Court":"court",
			"Side":"side"
		}
		Api.commonApiPost("/spec-directory/"+module+"/"+master, {}).then(function(res) {
			master=lcms[res.header[0].label.split(".")[3]]||res.header[0].label.split(".")[3];

			data.MdmsCriteria.moduleDetails[0].masterDetails[0].name=master;
			let headers=[];
			for (var i = 0; i < res.header.length; i++) {
				headers.push(res.header[i].label.split(".")[4]);
			}
			// headers.push("modify");
			Api.commonApiPost("/egov-mdms-service/v1/_search", {}, data, false, true).then(function(res2) {
				let arr = _.get(res2, "MdmsRes." + module + "." + master) ||[];
				let temp=[];
				if(arr && arr.length) {

					// let temp=[];

					// self.props.setFormData(formData);
					for(let i=0; i<arr.length; i++) {

						// arr[i].modify = true;
						temp.push(_.pick(arr[i],headers));
					}

					// self.setState({
					// 	valueList: temp
					// })

					self.setState({
						valueList: temp
					})
				}

				res.jsonPath="MdmsMetadata.masterData"
				self.setState({
					item:res
				})


				formData.MasterMetaData.masterData=arr;
				console.log(formData);
				self.props.setFormData(formData);
				self.props.setLoadingStatus('hide');
			}).catch(function(err) {
				self.props.setLoadingStatus('hide');
				self.props.toggleSnackbarAndSetText(true, err.message);
			})
		}).catch(function(err) {
			self.props.setLoadingStatus('hide');
			self.props.toggleSnackbarAndSetText(true, err.message);
		})

		// Api.commonApiPost("/specs/yaml/_search", {
		// 	module,
		// 	master
		// }).then(function(res) {
    //
		// }).catch(function(err) {
		// 	self.props.setLoadingStatus('hide');
		// 	self.props.toggleSnackbarAndSetText(true, err.message);
		// })
	}

	handleChange=(e, property)=> {
		let {formData} = this.props;
		_.set(formData, property, e.target.value);
		this.props.setFormData(formData);

	}

	setDisabled= (bool)=> {
		this.setState({
			isBtnDSisabled: bool || false
		})
	}

	addOrUpdate=()=> {
		let {formData}=this.props;
		let self=this;
			// console.log({...formData.MasterMetaData,formData.MdmsMetadata});
			formData.MasterMetaData.masterData=formData.hasOwnProperty("MdmsMetadata")?formData.MdmsMetadata.masterData:[];
			self.props.setLoadingStatus('loading');
			Api.commonApiPost("/egov-mdms-create/v1/_create", {},{MasterMetaData:formData.MasterMetaData}, false, true).then(function(res) {
				console.log(res);
				self.props.setLoadingStatus('hide');
			}).catch(function(err) {
				self.props.setLoadingStatus('hide');
				self.props.toggleSnackbarAndSetText(true, err.message);
			})
	}

	render() {
		let {item, isBtnDisabled, valueList} = this.state;
		let {handleChange, setDisabled, addOrUpdate} = this;
		// console.log(item);
		// console.log(valueList);
		return (
			<div style={{margin:"20px"}}>
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
				{/*<RaisedButton
					label={translate("ui.framework.submit")}
					onClick={addOrUpdate}
					primary={true}
					disabled={isBtnDisabled}/>*/}
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
