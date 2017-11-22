import React, {Component} from 'react';
import RaisedButton from 'material-ui/RaisedButton';
import {connect} from 'react-redux';
import {translate} from '../../common/common';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import UiTextField from './UiTextField'
import UiSelectField from './UiSelectField'
import UiSelectFieldMultiple from './UiSelectFieldMultiple'
import UiCheckBox from './UiCheckBox'
import UiEmailField from './UiEmailField'
import UiMobileNumber from './UiMobileNumber'
import UiTextArea from './UiTextArea'
import UiMultiSelectField from './UiMultiSelectField'
import UiNumberField from './UiNumberField'
import UiDatePicker from './UiDatePicker'
import UiMultiFileUpload from './UiMultiFileUpload'
import UiSingleFileUpload from './UiSingleFileUpload'
import UiAadharCard from './UiAadharCard'
import UiPanCard from './UiPanCard'
import UiLabel from './UiLabel'
import UiRadioButton from './UiRadioButton'
import UiTextSearch from './UiTextSearch'
import UiDocumentList from './UiDocumentList'
import UiAutoComplete from './UiAutoComplete'
import UiDate from './UiDate';
import UiPinCode from './UiPinCode';
import UiArrayField from './UiArrayField';
import UiFileTable from './UiFileTable';
import jp from "jsonpath";
import _ from 'lodash';

function getSum(total, num) {
    return total + num;
}

class UiMultiFieldTable extends Component {
	constructor(props) {
       super(props);
       this.state = {
       	values: [],
       	list: [],
       	index: 0,
		jsonPath: "",
		isintialLoad:false
       };
   	}

   	componentDidMount() {
   		this.setState({
   			values: [this.props.item.tableList.values],
   			list: Object.assign([], this.props.item.tableList.values),
   			jsonPath: this.props.item.jsonPath
   		})
	   }
	componentWillReceiveProps(nextProps){
		var valuesArray=[];
		let {isintialLoad }=this.state;
		if(nextProps.formData){
			var numberOfRowsArray= _.get(nextProps.formData,this.props.item.jsonPath);
			var listValues = _.cloneDeep(this.props.item.tableList.values);
			if(numberOfRowsArray && numberOfRowsArray.length>0 && !isintialLoad){
            for(var i=0;i<numberOfRowsArray.length;i++){
				var listValuesArray=_.cloneDeep(listValues);
				for(var j=0; j<listValuesArray.length; j++) {
					 listValuesArray[j].jsonPath = listValuesArray[j].jsonPath.replace(this.state.jsonPath + "[" + 0 + "]", this.state.jsonPath + "[" +i+ "]");

				}
				valuesArray.push(listValuesArray);

			}
			this.setState({
				values:valuesArray,
				index:(numberOfRowsArray)?(numberOfRowsArray.length-1):0,
				isintialLoad:true
			})
			}else if(numberOfRowsArray && numberOfRowsArray.length == 0){
              this.setState({
				 isintialLoad:true
			  })
			}

		}

	}

   	addNewRow() {
   		var val = JSON.parse(JSON.stringify(this.state.list));
   		var regexp = new RegExp(this.state.jsonPath + "\\[\\d{1}\\]", "g");
   		this.setState({
   			index: this.state.index + 1
   		}, () => {
   			for(var i=0; i<val.length; i++) {
				   //val[i].jsonPath = val[i].jsonPath.replace(regexp, this.state.jsonPath + "[" + this.state.index + "]")
				 val[i].jsonPath = val[i].jsonPath.replace(this.state.jsonPath + "[" + 0 + "]", this.state.jsonPath + "[" + this.state.index + "]");
	   		}
			let values = [...this.state.values];
			values.push(val);
			this.setState({
				values
			});
   		})
   	}

    escapeRegExp = (str) => {
		   return str.replace(/[\-\[\]\/\{\}\(\)\*\+\?\.\\\^\$\|]/g, "\\$&");
		}

   	deleteRow = (ind) => {
		//Changes to handle dependency sum
		let dependencyFlag =0;
		let depField="";
		let sumField="";

		let values = Object.assign([], this.state.values);
		values.splice(ind, 1);
		var regexp = new RegExp(`${this.escapeRegExp(this.state.jsonPath)}\\[\\d+\\]`);
		for(var i=0; i<values.length; i++) {
			for(var j=0; j<values[i].length; j++) {
				values[i][j].jsonPath = values[i][j].jsonPath.replace(regexp, `${this.state.jsonPath}[${i}]`);
				if(values[i][j].dependency)//Changes to handle dependency sum
					{
						dependencyFlag =1;
						depField=values[i][j].dependency;
						sumField=values[i][j].jsonPath;
					}
			}
		}

		this.setState({
			values,
			index: this.state.index-1,
			isintialLoad:false
		}, () => {
			let formData = JSON.parse(JSON.stringify(this.props.formData));
			let formDataArray =_.get(formData,this.state.jsonPath)
			if(formDataArray && formDataArray.length) {
				formDataArray.splice(ind, 1);
				var newArray=_.cloneDeep(formDataArray);
				var newFormData=_.set(formData,this.state.jsonPath,formDataArray);
				this.props.setFormData(newFormData);
			}
		})

		if(dependencyFlag==1)//Changes to handle dependency sum
		{
			let _formData = JSON.parse(JSON.stringify(this.props.formData));
			if( _formData)
					{
				let field= sumField.substr(0,sumField.lastIndexOf("["));
				let last= sumField.substr(sumField.lastIndexOf("]")+2);

				let arrval = _.get(_formData,field);
			if(arrval){
					let len= _.get(_formData,field).length;

				let amtsum=0;
				let svalue="";
				for(var i=0;i<len;i++)
					{
					let ifield=field+'['+i+']'+'.'+last;
					if(i==ind){
						svalue="";
					}
					else{
						svalue=_.get(_formData,ifield);
						amtsum += parseInt(svalue);
					}


					}
			this.props.handler({target: {value: amtsum}}, depField, false, '', '');
			}
		}
		}
	}

   	renderFields = (item, screen) => {
   		if(screen == "view" && ["documentList", "fileTable", "arrayText", "arrayNumber"].indexOf(item.type) > -1 ) {
	      if (item.type == "datePicker") {
	        item.isDate = true;
	      }
	      item.type = "label";
	    }
	  	switch(item.type) {
	  		case 'text':
	  			 return <UiTextField ui={this.props.ui} getVal={this.props.getVal} item={item}  fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
			 case 'textarea':
						return <UiTextArea ui={this.props.ui} getVal={this.props.getVal} item={item}  fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	  		case 'singleValueListMultiple':
	  			return <UiSelectFieldMultiple ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	  		case 'singleValueList':
	  			return <UiSelectField ui={this.props.ui} useTimestamp={this.props.useTimestamp} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	  		case 'multiValueList':
	      		return <UiMultiSelectField ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	      	case 'autoCompelete':
	        	return <UiAutoComplete ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} autoComHandler={this.props.autoComHandler || ""}/>
	    	case 'number':
	  			return <UiNumberField ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	  		case 'mobileNumber':
	  			return <UiMobileNumber ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	  		case 'checkbox':
	  			return <UiCheckBox ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	  		case 'email':
	  			return <UiEmailField ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	  		case 'datePicker':
	  			return <UiDatePicker ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	      	case 'singleFileUpload':
	  			return <UiSingleFileUpload ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	      	case 'multiFileUpload':
	    		return <UiMultiFileUpload ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	  	  	case 'pan':
	        	return <UiPanCard ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	      	case 'aadhar':
	        	return <UiAadharCard ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	      	case 'pinCode':
	        	return <UiPinCode ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	      	case 'label':
	        	return <UiLabel getVal={this.props.getVal} item={item}/>
	      	case 'radio':
	        	return <UiRadioButton ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler}/>
	      	case 'textSearch':
	        	return <UiTextSearch ui={this.props.ui} getVal={this.props.getVal} item={item} fieldErrors={this.props.fieldErrors} handler={this.props.handler} autoComHandler={this.props.autoComHandler}/>
	    }
	}

	renderTable = (item) => {
		// console.log(item.tableList.hasFooter, item.tableList.footer);
		let {formData} = this.props;
		let footerArray=[];

		item.tableList.values.map((val=>{
			footerArray.push(undefined);
		}));

    // console.log(formData);

		if(item.tableList.hasFooter){
			for(var i=0;i<item.tableList.footer.length;i++){
				// console.log(JSON.stringify(formData));
				for(var key in item.tableList.footer[i]){
					// console.log(key);
					let array = jp.query(formData, `$..${item.tableList.footer[i][key]}`);
          // console.log(array);
					footerArray.splice(key+1,0,array.reduce( (previousValue, currentValue) => Number(previousValue) + Number(currentValue), 0))
				}
			}
		}

    // console.log(footerArray);

		return (
			<div>
				<Table className="table table-striped table-bordered" responsive>
					<thead>
						<tr>
							<th>#</th>
							{
								item.tableList.header.map((v, i) => {
									 var style={};
									 if(v.style){
										style =v.style;
									 }

									return (
										<th style={style} key={i}>{translate(v.label)}</th>
									)
								})
							}
							{!item.tableList.actionsNotRequired ? <th>{translate("reports.common.action")}</th> : ''}
						</tr>
					</thead>
					<tbody>
						{
							this.state.values.map((v, i) => {
								return (
									<tr key={i}>
										<td>{i + 1}</td>
										{
											v.map((v2, i2) => {
												return (
													<td key={i2}>
														{this.renderFields(v2, this.props.screen)}
													</td>
												)
											})
										}
										{!item.tableList.actionsNotRequired ?
											<td>
												<div className="material-icons" onClick={()=>{
													this.deleteRow(i)
												}}>delete</div>
											</td>
										 : ''}
									</tr>
								)
							})
						}
					</tbody>
					{item.tableList.hasFooter ?
					<tfoot>
						<tr>
							{footerArray.map((val, index) => {
								let value = val !== undefined ? val.toFixed(2) : '';
							  return (<td key={index} className="text-right" style={{fontWeight:'bold'}}>{value ? `Total: ${value}` : ''}</td>)
							})}
							{!item.tableList.actionsNotRequired ? <td></td> : ''}
						</tr>
					</tfoot> : ''}
				</Table>
				{!item.tableList.actionsNotRequired ?
				<div style={{"textAlign": "right"}}>
					<RaisedButton label={translate("pgr.lbl.add")} primary={true} onClick={() => {
						this.addNewRow()
					}}/>
				</div> : ''}
			</div>
		)
	}

	render () {
		return (
			<div>
				{this.renderTable(this.props.item)}
			</div>
		)
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

export default connect(mapStateToProps, mapDispatchToProps)(UiMultiFieldTable);
