import React, {Component} from 'react';
import {Grid, Row, Col} from 'react-bootstrap';
import {Card, CardHeader} from 'material-ui/Card';
import {BarChart, Bar, ResponsiveContainer, XAxis, YAxis, Tooltip} from 'recharts';
import {translate} from '../../../common/common';
import {HorizontalPageLoader, CustomizedAxisTick, CustomizedYAxisLabel, getTenantId} from './ReportUtils';
import Api from '../../../../api/api';

export default class PendencyReport extends Component {

  constructor(){
    super();
    this.state={
      isFetchingData:true
    }
  }

  componentDidMount(){

    const mappingKeys =[ {"greaterThan90":"> 90 days"}, {"lessThan90":"90 - 45 days"},
      {"lessThan45":"44 - 15 days"}, {"lessThan15":"< 15 days"}];

    let ageWiseData;

    Promise.all([
      Api.commonApiPost("pgr/dashboard/ageing",{}, {tenantId:getTenantId()})
    ]).then((responses)=>{
       try{
          let responseData = responses[0][0];
          ageWiseData = mappingKeys.map((keyObj)=>{
            let key = Object.keys(keyObj)[0];
            let name = keyObj[key];
            let noOfComplaints = parseInt(responseData[key]) || 0;
            return {name, noOfComplaints}
          });
       }
       catch(e)
       {
         console.log('error', e);
       }
       finally{
         this.setState({isFetchingData:false, ageWiseData});
       }
    }, (err)=> {
      console.log('error', err);
      this.setState({isFetchingData:false});
    });
  }

  render(){

    let styles = this.props.styles;

    const data = this.state.ageWiseData || [];

    return (
      <div>
      {this.state.isFetchingData && <HorizontalPageLoader></HorizontalPageLoader>}
      <Grid fluid={true}>
        <Row>
          <Col md={6} lg={6} sm={6} xs={12} style={styles.pendencyChart}>
            <Card style={styles.cardStyle}>
              <CardHeader
                title={translate("pgr.dashboar.chart.agewise")}/>
               <div style={{ width: '100%', height: 230}}>
                <ResponsiveContainer>
                  <BarChart data={data} margin={{top: 5, right: 30, left: 5, bottom: 15}}>
                    <XAxis tick={<CustomizedAxisTick></CustomizedAxisTick>} dataKey="name" />
                    <YAxis name="Number of complaints" label={<CustomizedYAxisLabel title="Number of complaints" />}/>
                    <Tooltip/>
                    <Bar name="Number of complaints" dataKey="noOfComplaints" fill="#8884d8" />
                  </BarChart>
                </ResponsiveContainer>
               </div>
            </Card>
          </Col>
          <Col md={6} lg={6} sm={6} xs={12} style={styles.pendencyChart}>
            <Card style={styles.cardStyle}>
              <CardHeader/>
            </Card>
          </Col>
          <Col md={6} lg={6} sm={6} xs={12} style={styles.pendencyChart}>
            <Card style={styles.cardStyle}>
              <CardHeader />
            </Card>
          </Col>
          <Col md={6} lg={6} sm={6} xs={12} style={styles.pendencyChart}>
            <Card style={styles.cardStyle}>
              <CardHeader/>
            </Card>
          </Col>
          <Col md={12} lg={12} sm={12} xs={12} style={styles.pendencyChart}>
            <Card style={styles.cardStyle}>
              <CardHeader/>
            </Card>
          </Col>
        </Row>
      </Grid>
    </div>
    )

  }

}
