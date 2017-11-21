import React, {Component} from 'react';
import {connect} from 'react-redux';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Api from '../../../api/api';
import jp from "jsonpath";
import _ from 'lodash';


let searchTextCom = "";
class UiAutoComplete extends Component {
	constructor(props) {
       super(props);
			 this.state={
				 searchText:""
			 }
   	}

	 handleUpdateInput = (searchText) => {
     this.setState({
       searchText: searchText,
     });
   }


  initData() {
   		let {item, setDropDownData}=this.props;
			if(item.onLoad == false){
				setDropDownData(item.jsonPath, []);
			}else{
				this.callAPI('');
			}

   }

  // componentWillReceiveProps(nextProps, nextState) {
  //  		if(!_.isEqual(nextProps, this.props)) {
  //  			this.initData(nextProps);
  //  		}
  //  	}

	componentDidMount() {
		this.initData();
	}

	callAPI = (keyUpValue) => {
		let {item, setDropDownData, useTimestamp}=this.props;
		// console.log('API called:', item.hasOwnProperty("url"), item.url.search("\\|"), item.url.search("{"));
		if(item.hasOwnProperty("url") && item.url && item.url.search("\\|")>-1 && item.url.search("{")==-1)
		{
			//console.log(item.url.split("?"));
			let splitArray=item.url.split("?");
			let context="";
			let id={};
			// id[splitArray[1].split("&")[1].split("=")[0]]=e.target.value;
			for (var j = 0; j < splitArray[0].split("/").length; j++) {
				if(j==(splitArray[0].split("/").length-1)){
						context+=splitArray[0].split("/")[j];
				}else{
					context+=splitArray[0].split("/")[j]+"/";
				}
			}


			let queryStringObject=splitArray[1].split("|")[0].split("&");
			for (var i = 0; i < queryStringObject.length; i++) {
				//console.log(queryStringObject[i], queryStringObject[i].split("=")[0], queryStringObject[i].split("=")[1]);
				if (i) {
					if(keyUpValue)
					  id[queryStringObject[i].split("=")[0]] = keyUpValue ? keyUpValue : queryStringObject[i].split("=")[1];
					else
					  id[queryStringObject[i].split("=")[0]] = queryStringObject[i].split("=")[1];
				}
			}

			//console.log(id);

			var response=Api.commonApiPost(context, id, {}, "", useTimestamp || false).then(function(response) {

				if(response) {

					let queries = splitArray[1].split("|");
					let keys=jp.query(response, queries[1]);
					let values=jp.query(response, queries[2]);

					let others=[];
					if(queries.length>3){
						for(let i=3;i<queries.length;i++){
							others.push(jp.query(response, queries[i]) || undefined);
						}
					}

					let dropDownData=[];
					for (var k = 0; k < keys.length; k++) {
							let obj={};
							obj["key"]=keys[k] && keys[k].toString();
							obj["value"]=values[k];

							if(others && others.length>0)
							{
								let otherItemDatas=[];
								for(let i=0;i<others.length;i++){
									otherItemDatas.push(others[i][k] || undefined);
								}
								obj['others'] = otherItemDatas;
							}

							if (item.hasOwnProperty("isKeyValuePair") && item.isKeyValuePair) {
								obj["value"]=keys[k]+"-"+values[k]
							}
							dropDownData.push(obj);
					}
					setDropDownData(item.jsonPath, dropDownData);
				}
			},function(err) {
					console.log(err);
			});
		}
		else if (item.hasOwnProperty("defaultValue") && typeof(item.defaultValue)=="object") {
			setDropDownData(item.jsonPath,item.defaultValue);
		}
	}

	 renderAutoComplete =(item) => {
		let {dropDownData}=this.props;
    const dataSourceConfig = {
        text: 'value',
        value: 'key',
    };

		// console.log(dropDownData.hasOwnProperty(item.jsonpath) && dropDownData[item.jsonpath].replace(".", "\."));
		// console.log(dropDownData);
		// console.log(dropDownData.hasOwnProperty(item.jsonPath));
		// console.log(searchTextCom);
		switch (this.props.ui) {
			case 'google':
				// let {dropDownData}=this.state;
				return (
          <div >
          <AutoComplete
						 className="custom-form-control-for-textfield"
						 id={item.jsonPath.split(".").join("-")}
						 listStyle={{ maxHeight: 100, overflow: 'auto' }}
						 onUpdateInput={this.handleUpdateInput}
						 filter={(searchText, key)=> {
  					 			return key.toLowerCase().includes(searchText.toLowerCase());
						 }}
          	 floatingLabelStyle={{"color": item.isDisabled ? "#A9A9A9" : "#696969", "fontSize": "20px", "white-space": "nowrap"}}
			 		   inputStyle={{"color": "#5F5C57"}}
          	 floatingLabelFixed={true}
             style={{"display": (item.hide ? 'none' : 'inline-block')}}
             errorStyle={{"float":"left"}}
             dataSource={dropDownData.hasOwnProperty(item.jsonPath)?dropDownData[item.jsonPath]:[]}
             dataSourceConfig={dataSourceConfig}
             floatingLabelText={<span>{item.label} <span style={{"color": "#FF0000"}}>{item.isRequired ? " *" : ""}</span></span>}
             fullWidth={true}
						 searchText={this.state.searchText || searchTextCom}
             disabled={item.isDisabled}
             errorText={this.props.fieldErrors[item.jsonPath]}
             onKeyUp={(e) => {
							 item.onLoad == false ? this.callAPI(e.target.value) : '';
             	//this.props.handler({target: {value: (item.allowWrite ? e.target.value : "")}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)
             }}
             onNewRequest={(value,index) =>{
              this.props.handler({target: {value: value.key}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)
							this.handleUpdateInput(value.value);
              if(this.props.autoComHandler && item.autoCompleteDependancy) {
              	this.props.autoComHandler(item.autoCompleteDependancy, item.jsonPath)
              }

            }}
           />
        {/*
          <SelectField
   						style={{"display": (item.hide ? 'none' : 'inline-block')}}
   						errorStyle={{"float":"left"}}
   						fullWidth={true}
   						floatingLabelText={item.label + (item.isRequired ? " *" : "")}
   						value={this.props.getVal(item.jsonPath)}
   						onChange={(event, key, value) =>{
   							this.props.handler({target: {value: value}}, item.jsonPath, item.isRequired ? true : false, '', item.requiredErrMsg, item.patternErrMsg)
   						}}
   						disabled={item.isDisabled}
   						errorText={this.props.fieldErrors[item.jsonPath]}
   						maxHeight={200}>
   				            {dropDownData.hasOwnProperty(item.jsonPath) && dropDownData[item.jsonPath].map((dd, index) => (
   				                <MenuItem value={dd.key} key={index} primaryText={dd.value} />
   				            ))}
   		            </SelectField>
          */}
          </div>

				);
		}
	}

	render () {
		let self = this;
		searchTextCom = self.props.getVal(this.props.item.jsonPath);
		return (
	      <div>
	        {this.renderAutoComplete(this.props.item)}
	      </div>
	    );
	}
}

const mapStateToProps = state => ({dropDownData:state.framework.dropDownData});

const mapDispatchToProps = dispatch => ({
  setDropDownData:(fieldName,dropDownData)=>{
    dispatch({type:"SET_DROPDWON_DATA",fieldName,dropDownData})
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(UiAutoComplete);
