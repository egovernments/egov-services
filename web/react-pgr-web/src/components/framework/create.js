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

  async initData()
  {
    let {setMetaData,setModuleName,setAtionName}=this.props;

    let hashLocation=window.location.hash;
    let obj=wcSpecs[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
    for (var i = 0; i <obj.groups.length; i++) {
      obj.groups[i].label=translate(obj.groups[i].label);
      for (var j = 0; j < obj.groups[i].fields.length; j++) {
        obj.groups[i].fields[j].label=translate(obj.groups[i].fields[j].label);
      }
    }
    // console.log(wcSpecs);
    setMetaData(wcSpecs);



    // console.log(window.location.hash.split("/")[2] + " "+window.location.hash.split("/")[1]);
    setModuleName(hashLocation.split("/")[2]);

    setAtionName(hashLocation.split("/")[1]);

    //Once api is ready for fetching specs will remove hardcoded specs path
    // let response=Api.commonApiPost("pgr-master/report/metadata/_get",{},{tenantId:"default",reportName:this.props.match.params.reportName}).then(function(response)
    // {
    //
    //   setMetaData(response)
    // },function(err) {
    //     console.log(err);
    // });
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

  handleChange=(e, property, isRequired, pattern)=>
  {

  }





  render() {
    let {metaData,moduleName,actionName}=this.props;
    let {create,handleChange}=this;
    // console.log(!_.isEmpty(metaData) && metaData);
    // console.log(moduleName && moduleName);
    // console.log(actionName && actionName);
    console.log(`${moduleName}.${actionName}`);
    return (
      <div className="Report">
        <form onSubmit={(e) => {
          create(e)
        }}>
        {!_.isEmpty(metaData) && <ShowFields groups={metaData[`${moduleName}.${actionName}`].groups} noCols={metaData[`${moduleName}.${actionName}`].numCols} uiFramework="google" handler={handleChange} fieldErrors={{}}/>}
          <RaisedButton type="submit" disabled={false}  label="Create" />
        </form>
      </div>
    );
  }
}

const mapStateToProps = state => ({metaData:state.framework.metaData,moduleName:state.framework.moduleName,actionName:state.framework.actionName});

const mapDispatchToProps = dispatch => ({
  setMetaData:(metaData)=>{
    dispatch({type:"SET_META_DATA",metaData})
  },
  setModuleName:(moduleName)=>{
    dispatch({type:"SET_MODULE_NAME",moduleName})
  },
  setAtionName:(actionName)=>{
    dispatch({type:"SET_ACTION_NAME",actionName})
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);
