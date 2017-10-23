import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import {ResponsiveContainer, PieChart, Pie, Sector, Cell, Tooltip,
        LineChart, Line, XAxis, YAxis, CartesianGrid, Legend,
        AreaChart, Area }  from 'recharts';
import Api from '../../../../api/api';
import styles from '../../../../styles/material-ui';

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
    let serviceName = [];
    let obj={};
    let {setLoadingStatus, toggleDailogAndSetText} = this.props;
    var self = this;
    setLoadingStatus('loading');

    Promise.all([
      Api.commonApiPost("/pgr/dashboard", {}),//last 7 months
      Api.commonApiPost("/pgr/dashboard", {type:'weekly'}),//last 7 days
      Api.commonApiPost("/pgr/dashboard/complainttype", {size:10})//top complainttype
    ]).then(response => {

      try{

        let last7days = [];

        response[1].map((data, index)=>{
          let keys = Object.keys(data);
          let values = Object.values(data)
          let days = {};
          let obj = last7days.find((days)=>{return days.name==values[2]});
          if(!obj){
            days[keys[0]]=values[0];
            days[keys[1]]=values[1];
            days[keys[2]]=values[2];
            last7days.push(days);
          }else {
            if(values[0] != 0){//REGISTERED
              last7days[last7days.length-1][keys[0]]=values[0];
            }else {//RESOLVED
              last7days[last7days.length-1][keys[1]]=values[1];
            }
          }
        });

         self.setState({
           last7months:response[0],
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
        <Row>
          <Col xs={12} sm={12} md={6} lg={6}>
            <Card style={styles.cardMargin}>
              <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
                No. of Complaints
               < /div>}/>
               <CardText>
                 <ResponsiveContainer width='100%' aspect={4.0/2.0}>
                   <AreaChart data={this.state.last7days}>
                     <XAxis dataKey="name"/>
                     <YAxis/>
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
              <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
                No. of Complaints
               < /div>}/>
               <CardText>
                 <ResponsiveContainer width='100%' aspect={4.0/2.0}>
                   <LineChart data={this.state.last7months}>
                    <XAxis dataKey="name"/>
                    <YAxis/>
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
              <CardHeader style={styles.cardHeaderPadding} title={< div style = {styles.headerStyle} >
                Complaint Type Share
               < /div>}/>
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
  }
})

export default connect(mapStateToProps,mapDispatchToProps)(charts);
