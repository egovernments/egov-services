import React, {Component} from 'react';
import {Grid, Row, Col} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import Snackbar from 'material-ui/Snackbar';
import Api from '../../../../api/api';
import {Tabs, Tab} from 'material-ui/Tabs';
import ActionFlightTakeoff from 'material-ui/svg-icons/action/flight-takeoff';
import {translate} from '../../../common/common';
import {BarChart, AreaChart, Area, ResponsiveContainer, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';
import TypeDistributionReport from './TypeDistributionReport';

const styles = {
  pendencyChart : {
    height :300,
    padding:"10px 10px 5px 10px",
    marginTop : 10
  },
  singleChart : {
    height :400,
    padding:"10px 10px 5px 10px",
    marginTop : 10
  },
  fullHeightGrid:{
    position: "fixed",
    height: "calc(100% - 158px)",
    width: "100%",
    padding: 0
  },
  cardStyle:{
    height:"100%"
  }
}

const CustomizedAxisTick = (props) =>{
    const {x, y, stroke, payload} = props;
   	return (
        <text x={x} y={y+15} textAnchor="middle" fill="#666">{payload.value}</text>
    );
};

const CustomizedYAxisLabel = (props) =>{
    const {viewBox, title} = props;
    console.log('props', props);

   	return (
      <g transform={`translate(${viewBox.x},${viewBox.y})`}>
        <text x={-(viewBox.height/2)} y={viewBox.y+15} textAnchor="middle" fill="#666" transform="rotate(-90)">{title}</text>
      </g>
    );
};

export default class PgrAnalytics extends Component {

  constructor(){
    super();
  }

  render(){
    return(
      <Tabs>
        <Tab label={translate('pgr.dashboard.pendency')}>
          <PendencyReport></PendencyReport>
        </Tab>
        <Tab label={translate('pgr.dashboard.type.distribution')}>
          <TypeDistribution></TypeDistribution>
        </Tab>
        <Tab label={translate('pgr.dashboard.top5.complaint.types')}>
          <Top5ComplaintTypes></Top5ComplaintTypes>
        </Tab>
        <Tab label={translate('pgr.dashboard.gisanalysis')} >
          <GisAnalysis></GisAnalysis>
        </Tab>
     </Tabs>

    );
  }

}

const PendencyReport = () => {

  const data = [
      {name: '> 90 days', noOfComplaints:300},
      {name: '90 - 45 days',  noOfComplaints:150},
      {name: '44 - 15 days', noOfComplaints:75},
      {name: '< 15 days', noOfComplaints:37}
  ];

  return (
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
  )
}

const TypeDistribution = () => {
  return (
    <Grid fluid={true} style={styles.fullHeightGrid}>
      <TypeDistributionReport></TypeDistributionReport>
    </Grid>
  )
}



const GisAnalysis = () => {
  return (
    <div>
      <h2>GIS</h2>
      {/* <p>
        This is an example tab.
      </p>
      <p>
        You can put any sort of HTML or react component in here. It even keeps the component state!
      </p> */}
    </div>
  )
}


const Top5ComplaintTypes = () => {
  const data = [
      {name: 'Apr',
        data:[
          {name:"Dog menance", count:125},
          {name:"Mosquito menance", count:158},
          {name:"Non Burning Street Lights", count:187},
          {name:"Removal of garbage", count:110},
          {name:"Stagination of water", count:90}
        ]
      },
      {name: 'May',
        data:[
          {name:"Dog menance", count:345},
          {name:"Mosquito menance", count:563},
          {name:"Non Burning Street Lights", count:123},
          {name:"Removal of garbage", count:212},
          {name:"Stagination of water", count:123}
        ]
      },
      {name: 'Jun',
        data:[
          {name:"Dog menance", count:65},
          {name:"Mosquito menance", count:34},
          {name:"Non Burning Street Lights", count:23},
          {name:"Removal of garbage", count:43},
          {name:"Stagination of water", count:65}
        ]
      },
      {name: 'Jul',
        data:[
          {name:"Dog menance", count:110},
          {name:"Mosquito menance", count:241},
          {name:"Non Burning Street Lights", count:225},
          {name:"Removal of garbage", count:122},
          {name:"Stagination of water", count:158}
        ]
      },
      {name: 'Oct',
        data:[
          {name:"Dog menance", count:250},
          {name:"Mosquito menance", count:150},
          {name:"Non Burning Street Lights", count:175},
          {name:"Removal of garbage", count:225},
          {name:"Stagination of water", count:125}
        ]
      }
  ];

  return (
    <Grid fluid={true}>
      <Card style={styles.cardStyle}>
        <CardHeader
          title={translate("pgr.dashboard.top5.complaint.types")}/>
           <div style={{ width: '100%', height: 400}}>
              <ResponsiveContainer>
                <AreaChart data={data} margin={{top: 10, right: 30, left: 0, bottom: 0}}>
                 <XAxis tick={<CustomizedAxisTick></CustomizedAxisTick>} dataKey="name"/>
                 <YAxis name="Number of complaints" label={<CustomizedYAxisLabel title="Number of complaints" />}/>
                 <CartesianGrid strokeDasharray="3 3"/>
                 <Tooltip formatter={(value, name, payload) => {
                   name = payload.payload.data[parseInt(name)].name;
                   return `${name} : ${value}`;
                 }}/>
                 <Area connectNulls={true} type='monotone' name="0" dataKey='data[0].count' stroke='#8884d8' fill='#8884d8' />
                 <Area connectNulls={true} type='monotone' name="1" dataKey='data[1].count' stroke='#AA66CC' fill='#AA66CC' />
                 <Area connectNulls={true} type='monotone' name="2" dataKey='data[2].count' stroke='#99CC00' fill='#99CC00' />
                 <Area connectNulls={true} type='monotone' name="3" dataKey='data[3].count' stroke='#FFBB33' fill='#FFBB33' />
                 <Area connectNulls={true} type='monotone' name="4" dataKey='data[4].count' stroke='#BE5C47' fill='#BE5C47' />
               </AreaChart>
              </ResponsiveContainer>
            </div>
      </Card>
    </Grid>
  )
}
