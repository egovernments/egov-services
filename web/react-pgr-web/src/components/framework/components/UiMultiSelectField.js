import React, {Component} from 'react';
import {connect} from 'react-redux';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import Api from '../../../api/api';
import jp from "jsonpath";
import _ from 'lodash';

class UiMultiSelectField extends Component {
	constructor(props) {
       super(props);
			 this.state={
				dropDownData:[]
			}
   	}

   	componentWillReceiveProps(nextProps, nextState) {
   		if(!_.isEqual(nextProps, this.props)) {
   			this.initDat(nextProps);
   		}
   	}
   	
   	initDat(props) {
   		let {item, setDropDownData, useTimestamp}=props;
		if(item.hasOwnProperty("url") && item.url.search("\\|")>-1)
		{
			let splitArray=item.url.split("?");
			let context="";
			let id={};
			for (var j = 0; j < splitArray[0].split("/").length; j++) {
				context+=splitArray[0].split("/")[j]+"/";
			}

			Api.commonApiPost(context, id, {}, "", useTimestamp || false).then(function(response) {
				if(response) {
					let keys=jp.query(response, splitArray[1].split("|")[1]);
					let values=jp.query(response, splitArray[1].split("|")[2]);
					let dropDownData=[];
					for (var k = 0; k < keys.length; k++) {
							let obj={};
							obj["key"]= item.convertToString ? keys[k].toString() : keys[k];
							obj["value"]=values[k];
							dropDownData.push(obj);
					}

					setDropDownData(item.jsonPath, dropDownData);
				}
			},function(err) {
				console.log(err);
			});
		}
   	}

   	componentDidMount() {
		this.initDat(this.props);
	}

	renderMultiSelect = (item) => {
		let {dropDownData}=this.props;
		switch (this.props.ui) {
			case 'google':
				return (
					<div style={{"display": "flex", "flex-direction": "column-reverse"}}>
						<SelectField
							style={{"display": (item.hide ? 'none' : 'block')}}
							errorStyle={{"float":"left"}}
							fullWidth={true}
							multiple={true}
							floatingLabelText={item.label + (item.isRequired ? " *" : "")}
							value={this.props.getVal(item.jsonPath)}
							disabled={item.isDisabled}
							onChange={(ev, key, val) => {
								this.props.handler({target: {value: val}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)}
							}
							errorText={this.props.fieldErrors[item.jsonPath]}
							maxHeight={200}>
								<MenuItem value={null} key="00000" primaryText="" />
					            {dropDownData.hasOwnProperty(item.jsonPath) && dropDownData[item.jsonPath].map((dd, index) => (
					                <MenuItem value={dd.key} key={index} primaryText={dd.value} />
					            ))}
			            </SelectField>
		            </div>
				);
		}
	}

	render () {
		return (
	      <div>
	        {this.renderMultiSelect(this.props.item)}
	      </div>
	    );
	}
}

const mapStateToProps = state => ({dropDownData: state.framework.dropDownData});

const mapDispatchToProps = dispatch => ({
  setDropDownData:(fieldName,dropDownData)=>{
    dispatch({type:"SET_DROPDWON_DATA", fieldName, dropDownData})
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(UiMultiSelectField);