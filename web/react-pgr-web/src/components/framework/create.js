import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "./showFields";
import wcSpecs from './specs/wc/wc';

import {translate} from '../common/common';
import Api from '../../api/api';
import jp from "jsonpath";

class Report extends Component {

  initData()
  {
    let {setMetaData,setModuleName,setAtionName,initForm}=this.props;

    let hashLocation=window.location.hash;
    let obj=wcSpecs[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
    for (var i = 0; i <obj.groups.length; i++) {
      obj.groups[i].label=translate(obj.groups[i].label);
      for (var j = 0; j < obj.groups[i].fields.length; j++) {
        obj.groups[i].fields[j].label=translate(obj.groups[i].fields[j].label);
      }
    }
    // console.log(wcSpecs);
    initForm();

    setMetaData(wcSpecs);

    setModuleName(hashLocation.split("/")[2]);

    setAtionName(hashLocation.split("/")[1]);

  }

  componentDidMount()
  {

      this.initData();
  }

  create=(e)=>
  {
    e.preventDefault();
  }

  // componentDidUpdate()
  // {
  //     // this.initData();
  // }

  handleChange=(e, property, isRequired, pattern,requiredErrMsg="Required",patternErrMsg="Pattern Missmatch")=>
  {
      let {handleChange}=this.props;
      // console.log(e + " "+ property + " "+ isRequired +" "+pattern);
      handleChange(e,property,isRequired,pattern,requiredErrMsg,patternErrMsg);
  }





  render() {
    let {metaData,moduleName,actionName,formData, getVal}=this.props;
    let {create,handleChange}=this;
    console.log(formData);
    // console.log(!_.isEmpty(metaData) && metaData);
    // console.log(moduleName && moduleName);
    // console.log(actionName && actionName);
    // console.log(`${moduleName}.${actionName}`);
    return (
      <div className="Report">
        <form onSubmit={(e) => {
          create(e)
        }}>
        {!_.isEmpty(metaData) && <ShowFields groups={metaData[`${moduleName}.${actionName}`].groups} noCols={metaData[`${moduleName}.${actionName}`].numCols} uiFramework="google" handler={handleChange} getVal={getVal} fieldErrors={{}} formData={formData}/>}
          <RaisedButton type="submit" disabled={false}  label="Create" />
        </form>
      </div>
    );
  }
}

const mapStateToProps = state => ({metaData:state.framework.metaData,moduleName:state.framework.moduleName,actionName:state.framework.actionName,formData:state.frameworkForm.form});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: [ ]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  setMetaData:(metaData)=>{
    dispatch({type:"SET_META_DATA",metaData})
  },
  setModuleName:(moduleName)=>{
    dispatch({type:"SET_MODULE_NAME",moduleName})
  },
  setAtionName:(actionName)=>{
    dispatch({type:"SET_ACTION_NAME",actionName})
  },
  handleChange:(e,property,isRequired,pattern,requiredErrMsg,patternErrMsg)=>{
    dispatch({type:"HANDLE_CHANGE_VERSION_TWO",property,value: e.target.value, isRequired, pattern,requiredErrMsg,patternErrMsg});
  },
  getVal: () => {

  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);
