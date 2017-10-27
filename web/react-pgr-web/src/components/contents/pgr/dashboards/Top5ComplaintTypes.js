import React, {Component} from 'react';
import {Grid, Row, Col} from 'react-bootstrap';
import {Card, CardHeader} from 'material-ui/Card';
import {LineChart, Line, AreaChart, Area, ResponsiveContainer, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';
import {translate} from '../../../common/common';
import {PageLoadingIndicator, CustomizedAxisTick, CustomizedYAxisLabel, CustomTooltip, getTenantId} from './ReportUtils';
import Api from '../../../../api/api';
import _ from "lodash";
import moment from 'moment';

const TOP5_REPORT_MONTH_LIMIT = 6; //Last month count including current month

export default class Top5ComplaintTypes extends Component {

  constructor(){
    super();
    this.state={
      isFetchingData:true
    }
  }

  getLastMonthNumbers = (lastMonthLimit) => {
    var lastMonthsNo = [];
    for(let i=1;i<=lastMonthLimit;i++){
      lastMonthsNo.push(parseInt(moment().add(i-lastMonthLimit,'months').format('MM')));
    }
    return lastMonthsNo;
  }

  componentDidMount(){

    let topFiveComplaintsData;
    Promise.all([
      Api.commonApiPost("pgr/dashboard/complainttype",{type : "topfive"}, {tenantId:getTenantId()})
    ]).then((responses)=>{
       try{

          let monthWiseData = _.groupBy(responses[0], 'month');

          let lastMonths = this.getLastMonthNumbers(TOP5_REPORT_MONTH_LIMIT);

          topFiveComplaintsData = lastMonths.map((month)=>{
              let dataArry = monthWiseData[`${month}.0`] && monthWiseData[`${month}.0`].map((complaintTypeData) =>{
                 return {
                   name : complaintTypeData.ComplaintType,
                   count : complaintTypeData.count
                 }
              });
              return {name:month, data:dataArry || []};
          });

       }
       catch(e)
       {
         console.log('error', e);
       }
       finally{
         this.setState({isFetchingData:false, topFiveComplaintsData});
       }
    }, (err)=> {
      console.log('error', err);
      this.setState({isFetchingData:false});
    });

  }

  render(){

    if(this.state.isFetchingData){
      return <PageLoadingIndicator></PageLoadingIndicator>;
    }

    const data = this.state.topFiveComplaintsData || [];

    return (
      <div>
      <Grid fluid={true}>
        <br/>
        <Card>
          <CardHeader
            title={translate("pgr.dashboard.top5.complaint.types")}/>
             <div style={{ width: '100%', height: 400}}>
                <ResponsiveContainer>
                  <AreaChart data={data} margin={{top: 10, right: 30, left: 0, bottom: 0}}>
                   <XAxis tick={<CustomizedAxisTick isMonthNumber={true}></CustomizedAxisTick>} dataKey="name"/>
                   <YAxis name="Number of complaints" label={<CustomizedYAxisLabel title="Number of complaints" />}/>
                   <CartesianGrid strokeDasharray="3 3"/>
                   <Tooltip content={<CustomTooltip/>}/>
                   <Area connectNulls={true} type='monotone' name="data[0].name" dataKey='data[0].count' stroke='#64B5F6' fill='#64B5F6' />
                   <Area connectNulls={true} type='monotone' name="data[1].name" dataKey='data[1].count' stroke='#BA68C8' fill='#BA68C8' />
                   <Area connectNulls={true} type='monotone' name="data[2].name" dataKey='data[2].count' stroke='#99CC00' fill='#99CC00' />
                   <Area connectNulls={true} type='monotone' name="data[3].name" dataKey='data[3].count' stroke='#FFBB33' fill='#FFBB33' />
                   <Area connectNulls={true} type='monotone' name="data[4].name" dataKey='data[4].count' stroke='#EF6C00' fill='#EF6C00' />
                 </AreaChart>
                </ResponsiveContainer>
              </div>
              <br/>
        </Card>
      </Grid>
    </div>
    )

  }

}
