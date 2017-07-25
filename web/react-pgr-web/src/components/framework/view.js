import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "./showFields";
import wcSpecs from './specs/wc/wc';

import {translate} from '../common/common';
import Api from '../../api/api';
import jp from "jsonpath";
import UiButton from './components/UiButton';

class View extends Component { 
	constructor(props) {
		super(props);
		this.state = {
			resultList : []
		}
	}
	
	componentDidMount() {
		
		var moduleName = this.props.match.params.moduleName;
		var controller = this.props.match.params.controller;
		var version = this.props.match.params.version;
		var id = this.props.match.params.id || '';
		
		var query = {
			id : id
		}

		Api.commonApiPost(moduleName+'/'+controller+'/'+version+'/_search', query ,{}, false, true).then((res)=>{   
			console.log(jp.query(res, "$..[0]"));
			this.setState({
				resultList: jp.query(res, "$..[0]")
			});
		  }).catch((err)=> {
			console.log(err.message);
		  })
	}
	
	render() {
		let {resultList} = this.state;
		Object.keys(resultList).map(function(key, index) {
		   resultList[key] *= 2;
		   console.log(key, index);
		});
		return(<div>Boom</div>);
	}

}


const mapStateToProps = state => ({
 
});

const mapDispatchToProps = dispatch => ({
	
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState,toastMsg});
  }
  
});

export default connect(mapStateToProps, mapDispatchToProps)(View);
