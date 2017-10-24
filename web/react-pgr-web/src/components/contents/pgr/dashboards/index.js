import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {ResponsiveContainer, PieChart, Pie, Sector, Cell, Tooltip,
        LineChart, Line, XAxis, YAxis, CartesianGrid, Legend,
        AreaChart, Area }  from 'recharts';
import Api from '../../../../api/api';
import RaisedButton from 'material-ui/RaisedButton';
import styles from '../../../../styles/material-ui';
var moment = require('moment');

const style = {
  position:'absolute',
  zIndex:100,
  right:20,
  top:80,
  margin:12
};

const COLORS = ['#0088FE', '#00C49F', '#008F7D', '#FFBB28', '#FF8042'];

const renderActiveShape = (props) => {
  const RADIAN = Math.PI / 180;
  const { cx, cy, midAngle, innerRadius, outerRadius, startAngle, endAngle,
    fill, percent, ComplaintType } = props;
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
      <text x={ex + (cos >= 0 ? 1 : -1) * 12} y={ey} dy={18} textAnchor={textAnchor} fill="#333">{`${ComplaintType} : ${(percent * 100).toFixed(2)}%`}</text>
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

    let {setLoadingStatus, toggleDailogAndSetText} = this.props;
    var self = this;
    setLoadingStatus('loading');

    //get last 7 days
    let last7days=[], last7months=[];
    let dateFrom = moment().subtract(7,'d').format('YYYY-MM-DD');
    for(let i=1; i<=7; i++){
      let obj={};
      obj['REGISTERED']=0;
      obj['RESOLVED']=0;
      obj['name']=`${moment(dateFrom).add(i, 'days').format('dddd').toUpperCase()}-${moment(dateFrom).add(i, 'days').format('DD')}`;
      last7days.push(obj);
    }

    //get last months
    let monthFrom = moment().subtract(7,'month').format('YYYY-MM-DD');
    for(let i=1; i<=7; i++){
      let obj={};
      obj['REGISTERED']=0;
      obj['name']=`${moment(monthFrom).add(i, 'months').format('MMM').toUpperCase()}-${moment(monthFrom).add(i, 'months').format('YYYY')}`;
      last7months.push(obj);
    }

    Promise.all([
      Api.commonApiPost("/pgr/dashboard", {}),//last 7 months
      Api.commonApiPost("/pgr/dashboard", {type:'weekly'}),//last 7 days
      Api.commonApiPost("/pgr/dashboard/complainttype", {size:10})//top complainttype
    ]).then(response => {

      try{

        response[0].map((data)=>{
          let values = Object.values(data)
          let index = last7months.map(function(o) { return o.name; }).indexOf(values[2]);
          last7months[index]['REGISTERED']=values[0];
          last7months[index]['RESOLVED']=values[1];
          return last7months;
        });

        response[1].map((data)=>{
          let values = Object.values(data)
          let index = last7days.map(function(o) { return o.name; }).indexOf(values[2]);
          last7days[index]['REGISTERED']=values[0];
          last7days[index]['RESOLVED']=values[1];
          return last7days;
        });

         self.setState({
           last7months:last7months,
           last7days:last7days,
           topComplaintType:response[2]
         });

         setLoadingStatus('hide');

      }catch(e){
        setLoadingStatus('hide');
      }

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
        <RaisedButton
          label="PGR Analytics"
          primary={true}
          onClick={(e)=>{this.props.setRoute('/pgr/analytics')}}
          style={style} />
        <Row>
          <Col xs={12} sm={12} md={6} lg={6}>
            <Card style={styles.cardMargin}>
              <CardHeader style={styles.cardHeaderPadding}
              title="No. of Complaints (Last 7 Days)"/>
               <CardText>
                 <ResponsiveContainer width='100%' aspect={4.0/2.0}>
                   <AreaChart data={this.state.last7days}>
                     <XAxis dataKey="name"/>
                     <YAxis allowDecimals={false}/>
                     <CartesianGrid strokeDasharray="3 3"/>
                     <Tooltip/>
                     <Legend verticalAlign="top" height={36}/>
                     <Area type='monotone' dataKey='REGISTERED' stroke='#8884d8' fill='#8884d8' />
                     <Area type='monotone' dataKey='RESOLVED' stroke='#82ca9d' fill='#82ca9d' />
                   </AreaChart>
                 </ResponsiveContainer>
               </CardText>
            </Card>
          </Col>
          <Col xs={12} sm={12} md={6} lg={6}>
            <Card style={styles.cardMargin}>
              <CardHeader style={styles.cardHeaderPadding}
              title="No. of Complaints (Last 7 Months)"/>
               <CardText>
                 <ResponsiveContainer width='100%' aspect={4.0/2.0}>
                   <LineChart data={this.state.last7months}>
                    <XAxis dataKey="name"/>
                    <YAxis allowDecimals={false}/>
                    <CartesianGrid strokeDasharray="3 3"/>
                    <Tooltip/>
                    <Legend verticalAlign="top" height={36}/>
                    <Line type="monotone" dataKey="REGISTERED" stroke="#8884d8" activeDot={{r: 8}}/>
                   </LineChart>
                 </ResponsiveContainer>
               </CardText>
            </Card>
          </Col>
          <Col xs={12} sm={12} md={12} lg={12}>
            <Card style={styles.cardMargin}>
              <CardHeader style={styles.cardHeaderPadding} title="Complaint Type Share"/>
               <CardText>
                 <ResponsiveContainer width='100%' aspect={4.0/1.0}>
                   <PieChart margin={{bottom: 30}}>
                     <Pie dataKey="count"
                       activeIndex={this.state.activeIndex}
                       data={this.state.topComplaintType}
                       activeShape={renderActiveShape}
                       onMouseEnter={this.onPieEnter}
                       fill="#8884d8"
                       >
                       {this.state.topComplaintType && this.state.topComplaintType.map((data, index)=> <Cell key={data} fill={COLORS[index % COLORS.length]}/> )}
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
  },
  setRoute:(route)=>dispatch({type:'SET_ROUTE',route})
})

export default connect(mapStateToProps,mapDispatchToProps)(charts);
