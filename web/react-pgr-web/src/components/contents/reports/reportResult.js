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




class ShowField extends Component {
  constructor(props) {
       super(props);

   }



   componentWillMount()
   {
     $('#searchTable').DataTable({
       dom: 'lBfrtip',
       buttons: [],
        bDestroy: true,
        language: {
           "emptyTable": "No Records"
        }
      });
   }

  componentWillUnmount()
  {
     $('#reportTable')
     .DataTable()
     .destroy(true);
  }




  componentWillUpdate() {
    let {flag}=this.props;
    if(flag == 1) {
      flag = 0;
      $('#reportTable').dataTable().fnDestroy();
    }
  }

  componentDidUpdate() {
          $('#reportTable').DataTable({
            dom: 'lBfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true,

          });
  }

  drillDown=(e,i,i2,item,item1)=>{
     let { reportResult ,searchForm ,setReportResult,setFlag,toggleSnackbarAndSetText} = this.props;
     let object=reportResult.reportHeader[i2];

    //  console.log(object);
    //  console.log(searchForm);

    if (object.defaultValue && object.defaultValue.search("_parent")>-1) {
        let splitArray=object.defaultValue.split("&");
        let searchParams=[]


        for (var variable in searchForm) {

          searchParams.push({
            name:variable,
            // value:typeof(searchForm[variable])=="object"?new Date(searchForm[variable]).getTime():searchForm[variable]
            input:typeof(searchForm[variable])=="object"?variable=="fromDate"?(new Date(searchForm[variable]).getFullYear() + "-" + (new Date(searchForm[variable]).getMonth()>9?(new Date(searchForm[variable]).getMonth()+1):("0"+(new Date(searchForm[variable]).getMonth()+1))) + "-" +(new Date(searchForm[variable]).getDate()>9?new Date(searchForm[variable]).getDate():"0"+new Date(searchForm[variable]).getDate())+" "+"00:00:00"):(new Date(searchForm[variable]).getFullYear() + "-" + (new Date(searchForm[variable]).getMonth()>9?(new Date(searchForm[variable]).getMonth()+1):("0"+(new Date(searchForm[variable]).getMonth()+1))) + "-" +(new Date(searchForm[variable]).getDate()>9?new Date(searchForm[variable]).getDate():"0"+new Date(searchForm[variable]).getDate())+" "+"23:59:59"):searchForm[variable]

          })
        }

        // let  queryString={};

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
              // queryString[key]=value;
              // queryString["name"]=key;
              // queryString["input"]=value;
              searchParams.push({"name":key,"input":value});
        }

        // console.log(queryString);
        console.log(splitArray[0].split("=")[1]);

        let response=Api.commonApiPost("pgr-master/report/_get",{},{tenantId:"default",reportName:splitArray[0].split("=")[1],searchParams}).then(function(response)
        {
          // console.log(response)
          setReportResult(response)
          setFlag(1);
        },function(err) {
            console.log(err);
        });
    }
    else {
      alert("No drilldown reports.")
        // toggleSnackbarAndSetText(true, "No drilldown reports.");
    }



  }

  render() {
    let {drillDown}=this
    let {
      isTableShow,
      metaData,
      reportResult
    } = this.props;
    // console.log(metaData);

    // const showRow=()=>{
    //   return
    // }

    const viewTabel=()=>
    {
      return (
        <Card>
          <CardHeader title={< strong > Result < /strong>}/>
          <CardText>
          <Table id="reportTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
          <thead style={{backgroundColor:"#f2851f",color:"white"}}>
            <tr>
              {reportResult.hasOwnProperty("reportHeader") && reportResult.reportHeader.map((item,i)=>
              {
                return (
                  <th key={i}>{translate(item.label)}</th>
                )
              })}
            {/*
              <th>#</th>
                <th>Assessment Number</th>
                <th>Owner Name</th>
                <th>Address</th>
                <th>Current Demand</th>
                <th>Arrears Demand</th>
                <th>Property usage</th>*/}

            </tr>
          </thead>
          <tbody>

                {reportResult.hasOwnProperty("reportData") && reportResult.reportData.map((item,i)=>
                {
                  return (
                    <tr key={i}>
                      {item.map((item1,i2)=>{
                        return (
                          <td key={i2} onClick={(e)=>{
                            drillDown(e,i,i2,item,item1)
                          }}>{item1}</td>
                        )
                      })}
                    </tr>
                    )

                })}


          </tbody>
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

const mapStateToProps = state => ({isTableShow:state.form.showTable,metaData:state.report.metaData,reportResult:state.report.reportResult,flag:state.report.flag,searchForm:state.form.form});

const mapDispatchToProps = dispatch => ({
  setReportResult:(reportResult)=>{
    dispatch({type:"SET_REPORT_RESULT",reportResult})
  },
  setFlag:(flag)=>{
      dispatch({type:"SET_FLAG",flag})
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(ShowField);
