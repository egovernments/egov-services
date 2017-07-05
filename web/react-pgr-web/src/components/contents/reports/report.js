import React, {Component} from 'react';
import {connect} from 'react-redux';

import Api from '../../../api/api';
import SearchForm from './searchForm';
import ReportResult from './reportResult';

var metaData=
{
    "requestInfo": null,
    "tenantId": null,
    "reportDetails": {
        "reportName": "GrievanceByType",
        "summary": "Grievance Type Wise Report",
        "reportHeader": [
            {
                "name": "name",
                "label": "reports.pgr.complainttype.name",
                "type": "string"
            },
            {
                "name": "registered",
                "label": "reports.pgr.seva.status.registered",
                "type": "string"
            },
            {
                "name": "inprocess",
                "label": "reports.pgr.seva.status.inprocess",
                "type": "string"
            },
            {
                "name": "completed",
                "label": "reports.pgr.seva.status.completed",
                "type": "string"
            },
            {
                "name": "rejected",
                "label": "reports.pgr.seva.status.rejected",
                "type": "string"
            },
            {
                "name": "reopened",
                "label": "reports.pgr.seva.status.reopened",
                "type": "string"
            },
            {
                "name": "withinSLA",
                "label": "reports.pgr.withinsla",
                "type": "string"
            },
            {
                "name": "outsideSLA",
                "label": "reports.pgr.ousidesla",
                "type": "string"
            },
            {
                "name": "total",
                "label": "reports.pgr.total",
                "type": "string"
            }
        ],
        "searchParams": [
            {
                "name": "fromDate",
                "label": "reports.pgr.datefrom",
                "type": "date",
                "isMandatory": true
            },
            {
                "name": "toDate",
                "label": "reports.pgr.dateto",
                "type": "date",
                "isMandatory": true
            },
            {
                "name": "complainttype",
                "label": "reports.pgr.complainttype",
                "type": "singlevaluelist",
                "pattern": "/pgr-master/service/v1/_search?tenantId=default|$.Service[*].serviceCode|$.Service[*].serviceName",
                "isMandatory": true
            }
        ],
        "query": ""
    },
    "ResponseInfo": null
}

class Report extends Component {

  componentWillMount()
  {
    let {setMetaData}=this.props;

    //Call api
    setMetaData(metaData);
    // let response=Api.commonApiPost("egf-masters", "functions", "_search").then(function(response)
    // {
    //   console.log(response)
    // },function(err) {
    //     console.log(err);
    // });
    //call boundary service fetch wards,location,zone data
  }


  render() {
    const viewTabel=()=>
    {
      return (
          <ReportResult />
        )
    }
    return (
      <div className="Report">
        <SearchForm />

        <ReportResult />
      </div>
    );
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => ({
  setMetaData:(metaData)=>{
    dispatch({type:"SET_META_DATA",metaData})
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);
