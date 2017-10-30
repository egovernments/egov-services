import React, {Component} from 'react';
import {connect} from 'react-redux';
import MenuItem from 'material-ui/MenuItem';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import Api from '../../../api/api';
import {translate, epochToDate} from '../../common/common';
import _ from 'lodash';

import $ from 'jquery';
import JSZip from 'jszip/dist/jszip';
import pdfMake from "pdfmake/build/pdfmake";
import pdfFonts from "pdfmake/build/vfs_fonts";
import 'datatables.net-buttons/js/buttons.html5.js';// HTML 5 file export
import 'datatables.net-buttons/js/buttons.flash.js';// Flash file export
import {fonts} from '../../common/pdf-generation/PdfConfig';
pdfMake.fonts = fonts;
window.JSZip = JSZip;

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
    // console.log('Did Mount');
    this.setState({
      reportName : this.props.match.params.reportName,
      moduleName : this.props.match.params.moduleName
    });
    this.subHeader(this.props.match.params.moduleName);
  }

  componentWillReceiveProps(nextprops){
    // if((this.props.match.params.moduleName !== nextprops.match.params.moduleName) || (this.props.match.params.reportName !== nextprops.match.params.reportName)){
      // console.log('nextprops');
      this.setState({
        reportName : nextprops.match.params.reportName,
        moduleName : nextprops.match.params.moduleName
      });
      this.subHeader(nextprops.match.params.moduleName);
    // }
  }

  componentDidUpdate() {
    // console.log('did update');
    $('#reportTable').DataTable({
      dom: '<"col-md-4"l><"col-md-4"B><"col-md-4"f>rtip',
      order: [],
      buttons: [
         'copy', 'csv', 'excel',
         {
            extend: 'pdf',
            filename : this.state.reportName,
            title : this.state.reportSubTitle,
            orientation: 'landscape',
            pageSize: 'TABLOID',
            footer : true
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

  openTemplate = (item) => {
    if(this.props.metaData.reportDetails.viewPath) {
      localStorage.reportData = JSON.stringify(item);
      this.props.setRoute("/print/report/" + this.props.metaData.reportDetails.viewPath);
    }
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
            <tr key={dataIndex} onClick={() => {
              this.openTemplate(dataItem);
            }}>
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

  subHeader = (moduleName) => {
    let {metaData,searchParams}=this.props;
    let paramsLength = searchParams.length;
    // console.log(paramsLength);
    if(_.isEmpty(metaData)){
      return;
    }
    let result = `${metaData.reportDetails.summary} for `;
    searchParams.map((search, index) => {
      let idx = index+1;
      let lastText = (idx == paramsLength);
      let obj = metaData.reportDetails.searchParams.find((obj)=>{ return search.name === obj.name});
      if(moduleName === 'pgr'){
        if(obj.name === 'fromDate' || obj.name === 'toDate'){//split date and time
          let date = `${search.input}`.split(' ')[0];
          let customDate = date.split('-');
          // result +=  obj.name === 'toDate' ? ' - ' : '';
          result += `${customDate[2]}/${customDate[1]}/${customDate[0]} (${translate(obj.label)})`;
          // result +=  lastText ? '' : obj.name === 'toDate' ? ', ' : '';
        }else{
          result += `${search.input} (${translate(obj.label)})`;
          // result += !lastText ? ', ' : '';
        }
      }else{
        if(obj.name === 'fromDate' || obj.name === 'toDate'){//epoch to date
          // result +=  obj.name === 'toDate' ? ' - ' : '';
          result += `${epochToDate(search.input)} (${translate(obj.label)})`;
          // result +=  lastText ? '' : obj.name === 'toDate' ? ', ' : '';
        }else{
            result += `${search.input} (${translate(obj.label)})`;
            // result += !lastText ? ', ' : '';
        }
      }
      result += !lastText ? ', ' : '';
    });
    this.setState({reportSubTitle:result})
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
        <div>
        <Card>
          <CardHeader title={this.state.reportSubTitle}/>
          <CardText>
          <Table id="reportTable" style={{color:"black",fontWeight: "normal",padding:"0 !important"}} bordered responsive>
            {this.renderHeader()}
            {this.renderBody()}
            {this.renderFooter()}
        </Table>
      </CardText>
      </Card>
      <br/>


   {metaData.reportDetails
         .summary=="Cash Collection Report" && <Grid><Row><Col xs={12} md={8} mdOffset={2}><Card>
     <CardHeader title={< strong >{ translate("Denomination Report")} < /strong>}/>
     <CardText>
     <Table style={{color:"black",fontWeight: "normal",padding:"0 !important"}} bordered responsive>
       <thead>
         <tr>
           <th>{translate("Denomination")}</th>
           <th>{translate("*")}</th>
           <th>{translate("Number")}</th>
           <th>{translate("Total")}</th>
         </tr>
       </thead>
       <tbody>
         <tr>
           <td>2000</td>
           <td>*</td>
           <td></td>
           <td></td>
         </tr>
         <tr>
           <td>500</td>
           <td>*</td>
           <td></td>
           <td></td>
         </tr>
         <tr>
           <td>200</td>
           <td>*</td>
           <td></td>
           <td></td>
         </tr>
         <tr>
           <td>100</td>
           <td>*</td>
           <td></td>
           <td></td>
         </tr>
         <tr>
           <td>50</td>
           <td>*</td>
           <td></td>
           <td></td>
         </tr>
         <tr>
           <td>20</td>
           <td>*</td>
           <td></td>
           <td></td>
         </tr>
         <tr>
           <td>10</td>
           <td>*</td>
           <td></td>
           <td></td>
         </tr>
         <tr>
           <td>5</td>
           <td>*</td>
           <td></td>
           <td></td>
         </tr>
         <tr>
           <td>1</td>
           <td>*</td>
           <td></td>
           <td></td>
         </tr>
         <tr>
           <td colSpan={3} style={{textAlign:"center"}}><strong>{translate("Total Amount Collected")}</strong></td>
           <td style={{textAlign:"right"}}></td>
         </tr>
       </tbody>
     </Table>




     </CardText>
   </Card></Col></Row></Grid>}

      </div>
      )
    }
    return (
      <div className="PropertyTaxSearch">
        {isTableShow && !_.isEmpty(reportResult) && reportResult.hasOwnProperty("reportData") && viewTabel()}
      </div>
    );
  }
}

const mapStateToProps = state => {
  // console.log(state);
  return ({isTableShow:state.form.showTable,metaData:state.report.metaData,reportResult:state.report.reportResult,flag:state.report.flag,searchForm:state.form.form,searchParams:state.report.searchParams});
}

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
