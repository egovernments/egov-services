import React, {Component} from 'react';
import {Grid, Row, Col} from 'react-bootstrap';
import {Card, CardHeader} from 'material-ui/Card';
import {LineChart, Line, AreaChart, Area, ResponsiveContainer, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';
import {translate} from '../../../common/common';
import {HorizontalPageLoader, CustomizedAxisTick, CustomizedYAxisLabel, CustomTooltip, getTenantId} from './ReportUtils';
import Api from '../../../../api/api';
import _ from "lodash";

export default class Top5ComplaintTypes extends Component {

  constructor(){
    super();
    this.state={
      isFetchingData:true
    }
  }

  componentDidMount(){

    let topFiveComplaintsData;
    Promise.all([
      Api.commonApiPost("pgr/dashboard/complainttype",{type : "topfive"}, {tenantId:getTenantId()})
    ]).then((responses)=>{
       try{
          let monthWiseData = _.groupBy(responses[0], 'month');

          topFiveComplaintsData = Object.keys(monthWiseData).map((month)=>{
              let dataArry = monthWiseData[month].map((complaintTypeData) =>{
                 return {
                   name : complaintTypeData.ComplaintType,
                   count : complaintTypeData.count
                 }
              });
              return {name:month, data:dataArry};
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

    const data = this.state.topFiveComplaintsData || [];

    return (
      <div>
      {this.state.isFetchingData && <HorizontalPageLoader></HorizontalPageLoader>}
      <Grid fluid={true}>
        <br/>
        <Card>
          <CardHeader
            title={translate("pgr.dashboard.top5.complaint.types")}/>
             <div style={{ width: '100%', height: 400}}>
                <ResponsiveContainer>
                  <LineChart data={data} margin={{top: 10, right: 30, left: 0, bottom: 0}}>
                   <XAxis tick={<CustomizedAxisTick isMonthNumber={true}></CustomizedAxisTick>} dataKey="name"/>
                   <YAxis name="Number of complaints" label={<CustomizedYAxisLabel title="Number of complaints" />}/>
                   <CartesianGrid strokeDasharray="3 3"/>
                   <Tooltip content={<CustomTooltip/>}/>
                   <Line connectNulls={true} type='monotone' name="data[0].name" dataKey='data[0].count' stroke='#64B5F6' fill='#64B5F6' />
                   <Line connectNulls={true} type='monotone' name="data[1].name" dataKey='data[1].count' stroke='#BA68C8' fill='#BA68C8' />
                   <Line connectNulls={true} type='monotone' name="data[2].name" dataKey='data[2].count' stroke='#99CC00' fill='#99CC00' />
                   <Line connectNulls={true} type='monotone' name="data[3].name" dataKey='data[3].count' stroke='#FFBB33' fill='#FFBB33' />
                   <Line connectNulls={true} type='monotone' name="data[4].name" dataKey='data[4].count' stroke='#EF6C00' fill='#EF6C00' />
                 </LineChart>
                </ResponsiveContainer>
              </div>
              <br/>
        </Card>
      </Grid>
    </div>
    )

  }

}
