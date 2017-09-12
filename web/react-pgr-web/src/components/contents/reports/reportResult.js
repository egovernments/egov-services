import React, {Component} from 'react';
import {connect} from 'react-redux';
import MenuItem from 'material-ui/MenuItem';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import Api from '../../../api/api';
import {translate} from '../../common/common';
import _ from 'lodash';

import $ from 'jquery';
import 'datatables.net-buttons/js/buttons.html5.js';// HTML 5 file export
import 'datatables.net-buttons/js/buttons.flash.js';// Flash file export
import jszip from 'jszip/dist/jszip';
import pdfMake from "pdfmake/build/pdfmake";
import pdfFonts from "pdfmake/build/vfs_fonts";
pdfMake.vfs = pdfFonts.pdfMake.vfs;

var sumColumn = [];
var footerexist = false;

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

  componentDidMount(){
    this.setState({reportName : this.props.match.params.reportName});
  }

  componentWillReceiveProps(nextprops){
    if((this.props.match.params.moduleName !== nextprops.match.params.moduleName) || (this.props.match.params.reportName !== nextprops.match.params.reportName))
    this.setState({reportName : nextprops.match.params.reportName});
  }


  componentDidUpdate() {
    // console.log('did update');
    $('#reportTable').DataTable({
      dom: '<"col-md-4"l><"col-md-4"B><"col-md-4"f>rtip',
      buttons: [
         'copy', 'csv', 'excel',
         { extend: 'pdf', text: 'Pdf', footer : true,  orientation: 'landscape', pageSize: 'TABLOID',
          customize: function ( doc ) {
              content: [ {
                  alignment: 'justify',
                  columns: [
                          { width: 'auto' },
                          { width: '*' },
                          { width: '*' }
                  ],
                  table: { widths: [ 'auto', '*', '*' ] }
              } ]
          }
        }, 'print'
       ],
      //  ordering: false,
       bDestroy: true,
       footerCallback: function ( row, data, start, end, display ) {
         var api = this.api(), data, total, pageTotal;

        //  console.log(footerexist, sumColumn);

         {sumColumn.map((columnObj, index) => {
           if(columnObj.total){
             // Remove the formatting to get integer data for summation
             var intVal = function ( i ) {
                 return typeof i === 'string' ?
                     i.replace(/[\$,]/g, '')*1 :
                     typeof i === 'number' ?
                         i : 0;
             };

             // Total over all pages
             total = api
                 .column( index )
                 .data()
                 .reduce( function (a, b) {
                     return intVal(a) + intVal(b);
                 }, 0 );

             // Total over this page
             pageTotal = api
                 .column( index, { page: 'current'} )
                 .data()
                 .reduce( function (a, b) {
                     return intVal(a) + intVal(b);
                 }, 0 );

             // Update footer
             $( api.column( index ).footer() ).html(
                 pageTotal.toLocaleString('en-IN') +' ('+ total.toLocaleString('en-IN') +')'
             );
           }
         })}

       }
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
      let afterURL = object.defaultValue.split('?')[1];
      let URLparams = afterURL.split(':');
      // console.log(URLparams, URLparams.length);
      if(URLparams.length > 1){
        setRoute(`${URLparams[0]+item1}`);
      }else{
        setRoute(URLparams[0]);
      }
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
                  return (
                    <td key={itemIndex} onClick={(e)=>{ drillDown(e,dataIndex,itemIndex,dataItem,item) }}>
                      {respHeader.defaultValue ? <a href="javascript:void(0)">{checkIfDate(item,itemIndex)}</a> : checkIfDate(item,itemIndex)}
                    </td>
                  )
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
    let { reportResult } = this.props;
    let reportHeaderObj = reportResult.reportHeader;
    footerexist = false;

    {reportHeaderObj.map((headerObj, index) => {
      let columnObj = {};
      if(headerObj.showColumn){
        columnObj['showColumn'] = headerObj.showColumn;
        columnObj['total'] = headerObj.total;
        sumColumn.push(columnObj);
      }
      if(headerObj.total){
        footerexist = true;
      }
      // console.log(headerObj.showColumn, headerObj.total);
    })};

    if(footerexist){
      return(
        <tfoot>
        <tr>
          {sumColumn.map((columnObj, index) => {
            return (
              <th key={index}>{index === 0 ? 'Total (Grand Total)' : ''}</th>
            )
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
          <Table id="reportTable" style={{color:"black",fontWeight: "normal",padding:"0 !important"}} bordered responsive>
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
