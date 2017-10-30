import React, {Component} from 'react';
import {connect} from 'react-redux';
import Api from '../../../api/api';
import SearchForm from './searchForm';
import ReportResult from './reportResult';
import $ from 'jquery';

class Report extends Component {

  componentWillReceiveProps(nextProps){
    if(nextProps.match.params.reportName !== this.props.match.params.reportName){
      this.initData(nextProps.match.params.moduleName, nextProps.match.params.reportName);
    }
  }

  componentDidMount(){
    this.initData(this.props.match.params.moduleName, this.props.match.params.reportName);
  }

  initData = (moduleName, reportName) => {
    var _this = this;
    let {setMetaData,setFlag,showTable,setForm,setReportResult}=this.props;

    var tenantId = localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : '';

    Api.commonApiPost("/report/"+moduleName+"/metadata/_get",{},{tenantId:tenantId,reportName:reportName}).then(function(response)
    {
      //console.log(moduleName, reportName);
      setFlag(1);
      showTable(false);
      setReportResult({});
      setMetaData(response);
      // console.log('hide the loader');
      // setForm();
      },function(err) {
        // console.log(err);
        alert('Try again later');
        //_this.props.setLoadingStatus('hide');
        // _this.props.toggleDailogAndSetText(true, 'Try again later');
    });
  };

  render() {
    let {match}=this.props;
    return (
      <div className="">
        <SearchForm match={match}/>

        <ReportResult match={match}/>
      </div>
    );
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => ({
  setMetaData:(metaData)=>{
    dispatch({type:"SET_META_DATA",metaData})
  },
  setFlag:(flag)=>{
      dispatch({type:"SET_FLAG",flag})
  },
  showTable:(state)=>
  {
    dispatch({type:"SHOW_TABLE",state});
  },
  setReportResult:(reportResult)=>
  {
    dispatch({type:"SHOW_TABLE",reportResult});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setForm: (required=[],pattern=[]) => {
    dispatch({
      type: "SET_FORM",
      form:{},
      fieldErrors:{},
      isFormValid:false,
      validationData: {
        required: {
          current: [],
          required: required
        },
        pattern: {
          current: [],
          required: pattern
        }
      }
    });
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);


// var metaData=
// {
//     "requestInfo": null,
//     "tenantId": null,
//     "reportDetails": {
//         "reportName": "GrievanceByType",
//         "summary": "Grievance Type Wise Report",
//         "reportHeader": [
//             {
//                 "name": "name",
//                 "label": "reports.pgr.complainttype.name",
//                 "type": "string"
//             },
//             {
//                 "name": "registered",
//                 "label": "reports.pgr.seva.status.registered",
//                 "type": "string"
//             },
//             {
//                 "name": "inprocess",
//                 "label": "reports.pgr.seva.status.inprocess",
//                 "type": "string"
//             },
//             {
//                 "name": "completed",
//                 "label": "reports.pgr.seva.status.completed",
//                 "type": "string"
//             },
//             {
//                 "name": "rejected",
//                 "label": "reports.pgr.seva.status.rejected",
//                 "type": "string"
//             },
//             {
//                 "name": "reopened",
//                 "label": "reports.pgr.seva.status.reopened",
//                 "type": "string"
//             },
//             {
//                 "name": "withinSLA",
//                 "label": "reports.pgr.withinsla",
//                 "type": "string"
//             },
//             {
//                 "name": "outsideSLA",
//                 "label": "reports.pgr.ousidesla",
//                 "type": "string"
//             },
//             {
//                 "name": "total",
//                 "label": "reports.pgr.total",
//                 "type": "string"
//             }
//         ],
//         "searchParams": [
//             {
//                 "name": "fromDate",
//                 "label": "reports.pgr.datefrom",
//                 "type": "date",
//                 "isMandatory": true
//             },
//             {
//                 "name": "toDate",
//                 "label": "reports.pgr.dateto",
//                 "type": "date",
//                 "isMandatory": true
//             },
//             {
//                 "name": "fromDate",
//                 "label": "reports.pgr.datefrom",
//                 "type": "string",
//                 "isMandatory": true
//             },
//             {
//                 "name": "fromDate",
//                 "label": "reports.pgr.datefrom",
//                 "type": "number",
//                 "isMandatory": true
//             }
//
//
//
//
//             // {
//             //     "name": "complainttype",
//             //     "label": "reports.pgr.complainttype",
//             //     "type": "singlevaluelist",
//             //     "pattern": "/pgr-master/service/v1/_search?tenantId=default|$.Service[*].serviceCode|$.Service[*].serviceName",
//             //     "isMandatory": true
//             // }
//         ],
//         "query": ""
//     },
//     "ResponseInfo": null
// }
