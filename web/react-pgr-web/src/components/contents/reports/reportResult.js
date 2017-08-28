import React, {Component} from 'react';
import {connect} from 'react-redux';
import MenuItem from 'material-ui/MenuItem';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import Api from '../../../api/api';
import {translate} from '../../common/common';
import _ from 'lodash';

const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');
const buttons = require('datatables.net-buttons-bs');
require('datatables.net-buttons/js/buttons.colVis.js'); // Column visibility
require('datatables.net-buttons/js/buttons.html5.js'); // HTML 5 file export
require('datatables.net-buttons/js/buttons.flash.js'); // Flash file export
require('datatables.net-buttons/js/buttons.print.js'); // Print view button

var sumColumn = [];

class ShowField extends Component {
  constructor(props) {
       super(props);
   }

  componentWillUnmount()
  {
     $('#reportTable')
     .DataTable()
     .destroy(true);
  }

  componentWillUpdate() {
    // console.log('will update');
    let {flag}=this.props;
    if(flag == 1) {
      flag = 0;
      $('#reportTable').dataTable().fnDestroy();
    }
  }

  componentWillReceiveProps(nextprops){
  }

  componentDidUpdate() {
    // console.log('did update');
    $('#reportTable').DataTable({
      dom: '<"col-md-4"l><"col-md-4"B><"col-md-4"f>rtip',
      buttons: [
               'copy', 'csv', 'excel', 'pdf', 'print'
       ],
       ordering: false,
       bDestroy: true
    });
  }

  drillDown=(e,i,i2,item,item1)=>{
     let { reportResult ,searchForm ,setReportResult,setFlag,toggleSnackbarAndSetText,searchParams,setRoute,match} = this.props;
     let object=reportResult.reportHeader[i2];

    if (object.defaultValue && object.defaultValue.search("_parent")>-1) {
        let splitArray=object.defaultValue.split("&");

        for (var i = 1; i < splitArray.length; i++) {
            let key,value;
            if(splitArray[i].search("{")>-1)
            {

              key=splitArray[i].split("=")[0];
              let inputparam=splitArray[i].split("{")[1].split("}")[0];
              for (var j = 0; j < reportResult.reportHeader.length; j++) {
                  if (reportResult.reportHeader[j].name==inputparam) {
                      value=item[j];
                  }
              }
            }
            else {
              key=splitArray[i].split("=")[0];
              if (key=="status") {
                  value=splitArray[i].split("=")[1].toUpperCase();
              }
              else {
                  value=splitArray[i].split("=")[1];
              }


            }
              searchParams.push({"name":key,"input":value});
        }

            var tenantId = localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : '';


        let response=Api.commonApiPost("/report/"+match.params.moduleName+"/_get",{},{tenantId:tenantId,reportName:splitArray[0].split("=")[1],searchParams}).then(function(response)
        {
          // console.log(response)
          setReportResult(response)
          setFlag(1);
        },function(err) {
            console.log(err);
        });
    }
    else if(object.defaultValue && object.defaultValue.search("_url")>-1) {
      // console.log(item1);
      setRoute(`/pgr/viewGrievance/${item1}`);
    }
  }

  checkIfDate = (val, i) => {
    let {reportResult}=this.props;
    if(reportResult && reportResult.reportHeader && reportResult.reportHeader.length && reportResult.reportHeader[i] && reportResult.reportHeader[i].type == "epoch") {
      var _date = new Date(Number(val));
      return ('0' + _date.getDate()).slice(-2) + '/'
             + ('0' + (_date.getMonth()+1)).slice(-2) + '/'
             + _date.getFullYear();
    } else {
      return val;
    }
  }

  renderHeader = () => {
    let { reportResult } = this.props;
    return(
      <thead style={{backgroundColor:"#f2851f",color:"white"}}>
        <tr>
          {reportResult.hasOwnProperty("reportHeader") && reportResult.reportHeader.map((item,i)=>
          {
            if(item.showColumn){
              return (
                <th key={i}>{translate(item.label)}</th>
              )
            }else{
              return null;
            }
          })
        }
        </tr>
      </thead>
    )
  }

  renderBody = () => {
    sumColumn = [];
    let { reportResult } = this.props;
    let {drillDown, checkIfDate} = this;
    return (
      <tbody>
        {reportResult.hasOwnProperty("reportData") && reportResult.reportData.map((dataItem,dataIndex)=>
        {
          //array of array
          let reportHeaderObj = reportResult.reportHeader;
          return(
            <tr key={dataIndex}>
              {dataItem.map((item,itemIndex)=>{
                var columnObj = {};
                //array for particular row
                var respHeader = reportHeaderObj[itemIndex];
                if(respHeader.showColumn){
                  columnObj = {};
                  if(respHeader.total){
                    columnObj['showColumn'] = respHeader.showColumn;
                    columnObj['total'] = respHeader.total;
                    columnObj['value'] = sumColumn[itemIndex] == undefined ? item : (sumColumn[itemIndex].value)+item;
                    if(sumColumn[itemIndex]){
                      //remove the object from that itemIndex
                      sumColumn.splice(itemIndex, 1);
                      //add that object in that index
                      sumColumn.splice(itemIndex,0,columnObj)
                    }else{
                      sumColumn.push(columnObj)
                    }
                    // console.log(itemIndex,':',item,':',sumColumn[itemIndex]);
                  }else{
                    columnObj['showColumn'] = respHeader.showColumn;
                    columnObj['total'] = respHeader.total;
                    columnObj['value'] = 0;
                    if(sumColumn[itemIndex]){
                      //remove the object from that itemIndex
                      sumColumn.splice(itemIndex, 1);
                      //add that object in that index
                      sumColumn.splice(itemIndex,0,columnObj)
                    }else{
                      sumColumn.push(columnObj)
                    }
                  }
                  return (
                    <td key={itemIndex} onClick={(e)=>{ drillDown(e,dataIndex,itemIndex,dataItem,item) }}>
                      {respHeader.defaultValue ? <a href="javascript:void(0)">{checkIfDate(item,itemIndex)}</a> : checkIfDate(item,itemIndex)}
                    </td>
                  )
                }else{
                  columnObj['showColumn'] = respHeader.showColumn;
                  columnObj['total'] = respHeader.total;
                  columnObj['value'] = 0;
                  if(sumColumn[itemIndex]){
                    //remove the object from that itemIndex
                    sumColumn.splice(itemIndex, 1);
                    //add that object in that index
                    sumColumn.splice(itemIndex,0,columnObj)
                  }else{
                    sumColumn.push(columnObj)
                  }
                  return null;
                }
              }
            )}
            </tr>
          )
        })}
      </tbody>
    )
  }

  renderFooter = () => {
    var footerexist = false;
    {sumColumn.map((columnObj, index) => {
      for (var [key, value] of Object.entries(columnObj)) {
        if(key === 'total' && value === true){
          footerexist = true;
        }
      }
    })};
    if(footerexist){
      return(
        <tfoot>
        <tr>
          {sumColumn.map((columnObj, index) => {
            if(columnObj.showColumn){
              return(
                <td key={index}>{columnObj.total ? columnObj.value : ''}</td>
              )
            }else{
              return null;
            }
          })}
        </tr>
        </tfoot>
      )
    }
  }

  render() {
    let {drillDown,checkIfDate}=this
    let {
      isTableShow,
      metaData,
      reportResult
    } = this.props;

    const viewTabel=()=>
    {
      return (
        <Card>
          <CardHeader title={< strong > Result < /strong>}/>
          <CardText>
          <Table id="reportTable" style={{color:"black",fontWeight: "normal"}} bordered responsive className="table-striped">
            {this.renderHeader()}
            {this.renderBody()}
            {this.renderFooter()}
        </Table>
      </CardText>
      </Card>
      )
    }
    return (
      <div className="PropertyTaxSearch">
        {isTableShow && !_.isEmpty(reportResult) && reportResult.hasOwnProperty("reportData") && viewTabel()}
      </div>
    );
  }
}

const mapStateToProps = state => ({isTableShow:state.form.showTable,metaData:state.report.metaData,reportResult:state.report.reportResult,flag:state.report.flag,searchForm:state.form.form,searchParams:state.report.searchParams});

const mapDispatchToProps = dispatch => ({
  setReportResult:(reportResult)=>{
    dispatch({type:"SET_REPORT_RESULT",reportResult})
  },
  setFlag:(flag)=>{
      dispatch({type:"SET_FLAG",flag})
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route})
});

export default connect(mapStateToProps, mapDispatchToProps)(ShowField);
