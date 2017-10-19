import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {ResponsiveContainer, PieChart, Pie, Sector, Cell, Tooltip,
LineChart, Line, XAxis, YAxis, CartesianGrid, Legend,
AreaChart, Area }
from 'recharts';
import Api from '../../../../api/api';
import styles from '../../../../styles/material-ui';

const data = [
      {name: '23rd Tue', Registered: 4000, Resolved: 2400},
      {name: '24th Wed', Registered: 3000, Resolved: 1398},
      {name: '25th Thu', Registered: 2000, Resolved: 9800},
      {name: '26th Fri', Registered: 2780, Resolved: 3908},
      {name: '27th Sat', Registered: 1890, Resolved: 4800},
      {name: '28th Sun', Registered: 2390, Resolved: 380},
      {name: '29th Mon', Registered: 3490, Resolved: 4300}
];

const COLORS = ['#0088FE', '#00C49F', '#008F7D', '#FFBB28', '#FF8042'];

const renderActiveShape = (props) => {
  const RADIAN = Math.PI / 180;
  const { cx, cy, midAngle, innerRadius, outerRadius, startAngle, endAngle,
    fill, payload, percent, value, name } = props;
  const sin = Math.sin(-RADIAN * midAngle);
  const cos = Math.cos(-RADIAN * midAngle);
  const sx = cx + (outerRadius + 10) * cos;
  const sy = cy + (outerRadius + 10) * sin;
  const mx = cx + (outerRadius + 30) * cos;
  const my = cy + (outerRadius + 30) * sin;
  const ex = mx + (cos >= 0 ? 1 : -1) * 22;
  const ey = my;
  const textAnchor = cos >= 0 ? 'start' : 'end';

  return (
    <g>
      <Sector
        cx={cx}
        cy={cy}
        innerRadius={innerRadius}
        outerRadius={outerRadius}
        startAngle={startAngle}
        endAngle={endAngle}
        fill={fill}
      />
      <Sector
        cx={cx}
        cy={cy}
        startAngle={startAngle}
        endAngle={endAngle}
        innerRadius={outerRadius + 6}
        outerRadius={outerRadius + 10}
        fill={fill}
      />
      <path d={`M${sx},${sy}L${mx},${my}L${ex},${ey}`} stroke={fill} fill="none"/>
      <circle cx={ex} cy={ey} r={2} fill={fill} stroke="none"/>
      <text x={ex + (cos >= 0 ? 1 : -1) * 12} y={ey} dy={18} textAnchor={textAnchor} fill="#333">{`${name} : ${(percent * 100).toFixed(2)}%`}</text>
    </g>
  );
};

class charts extends Component{
  constructor(props){
    super(props);
    this.state={
      activeIndex : 0
    }
  }

  componentDidMount(){
    let serviceName = [];
    let obj={};
    let {setLoadingStatus, toggleDailogAndSetText} = this.props;
    var self = this;
    setLoadingStatus('loading');

    Promise.all([
      Api.commonApiPost("/pgr/seva/v1/_search", {})
    ]).then(response => {

      response[0].serviceRequests.filter((o) => {
          obj[o.serviceName] = isNaN(obj[o.serviceName]) ? 1 : obj[o.serviceName]+1;
      });

      for(var key in obj){
        let serviceObj = {};
        serviceObj['name'] = key;
        serviceObj['value'] = obj[key];
        serviceName.push(serviceObj);
      }

      self.setState({data:serviceName.slice(1,11)});

      setLoadingStatus('hide');

    }).catch(reason => {
      setLoadingStatus('hide');
      toggleDailogAndSetText(true, 'Error');
    });

  }

  onPieEnter = (data, index) => {
    this.setState({
      activeIndex: index
    });
  }

  render(){
    return(
      <Grid fluid={true}>
        <Row>
          <Col xs={12} sm={6} md={6} lg={6}>
            <Card style={styles.marginStyle}>
              <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
                No. of Complaints
               < /div>}/>
               <CardText>
                 <ResponsiveContainer width='100%' aspect={4.0/2.0}>
                   <AreaChart data={data}>
                     <XAxis dataKey="name"/>
                     <YAxis/>
                     <CartesianGrid strokeDasharray="3 3"/>
                     <Tooltip/>
                     <Legend verticalAlign="top" height={36}/>
                     <Area type='monotone' dataKey='Registered' stroke='#8884d8' fill='#8884d8' />
                     <Area type='monotone' dataKey='Resolved' stroke='#82ca9d' fill='#82ca9d' />
                   </AreaChart>
                 </ResponsiveContainer>
               </CardText>
            </Card>
          </Col>
          <Col xs={12} sm={6} md={6} lg={6}>
            <Card style={styles.marginStyle}>
              <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
                No. of Complaints
               < /div>}/>
               <CardText>
                 <ResponsiveContainer width='100%' aspect={4.0/2.0}>
                   <LineChart data={data}>
                    <XAxis dataKey="name"/>
                    <YAxis/>
                    <CartesianGrid strokeDasharray="3 3"/>
                    <Tooltip/>
                    <Legend verticalAlign="top" height={36}/>
                    <Line type="monotone" dataKey="Registered" stroke="#8884d8" activeDot={{r: 8}}/>
                   </LineChart>
                 </ResponsiveContainer>
               </CardText>
            </Card>
          </Col>
          <Col xs={12} sm={12} md={12} lg={12}>
            <Card style={styles.marginStyle}>
              <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
                Complaint Type Share
               < /div>}/>
               <CardText>
                 <ResponsiveContainer width='100%' aspect={4.0/1.0}>
                   <PieChart margin={{bottom: 30}}>
                     <Pie dataKey="value"
                       activeIndex={this.state.activeIndex}
                       data={this.state.data}
                       activeShape={renderActiveShape}
                       onMouseEnter={this.onPieEnter}
                       fill="#8884d8"
                       >
                       {this.state.data && this.state.data.map((data, index)=> <Cell key={data} fill={COLORS[index % COLORS.length]}/> )}
                     </Pie>
                  </PieChart>
                 </ResponsiveContainer>
               </CardText>
            </Card>
          </Col>
        </Row>
      </Grid>
    )
  }
}

const mapStateToProps = state => {
  return ({});
};

const mapDispatchToProps = dispatch => ({
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  }
})

export default connect(mapStateToProps,mapDispatchToProps)(charts);
