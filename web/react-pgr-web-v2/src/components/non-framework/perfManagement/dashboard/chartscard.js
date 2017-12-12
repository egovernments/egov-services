import React, { Component } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, PieChart, Pie, Sector, Cell } from 'recharts';

import { Card, CardText, CardMedia, CardHeader, CardTitle } from 'material-ui/Card';

import RaisedButton from 'material-ui/RaisedButton';

var jp = require('jsonpath');

export default class BarChartCard extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: [],
      dataKey: null,
      showChartView: true,
    };
  }

  componentDidMount() {
    this.formatParsedChartData(this.parseCompareSearchResponse(this.props.data), (data, dataKey) => {
      if (!data || !dataKey) {
      } else {
        this.setState({
          data: data,
          dataKey: dataKey,
        });
      }
    });
  }

  /**
   * parsing and arranging search response
   */
  flattenArray(array) {
    let self = this;
    return array.reduce(function(memo, el) {
      var items = Array.isArray(el) ? self.flattenArray(el) : [el];
      return memo.concat(items);
    }, []);
  }

  parseCompareSearchResponse(res) {
    return this.flattenArray(
      jp.query(res, '$.ulbs[*]').map((ulbs, index) => {
        return jp.query(ulbs, '$.finYears[*]').map((finYears, index) => {
          return jp.query(finYears, '$.kpiValues[*]').map((kpis, index) => {
            return {
              ulbName: jp.query(ulbs, '$.ulbName').join(''),
              finYear: jp.query(finYears, '$.finYear').join(''),
              kpiName: jp.query(kpis, '$.kpi.name').join(''),
              target: parseInt(jp.query(kpis, '$.kpi.kpiTargets[*].targetValue').join('')),
              value: parseInt(jp.query(kpis, '$.consolidatedValue').join('')),
            };
          });
        });
      })
    );
  }

  formatParsedChartData(data, cb) {
    let parsed = {
      data: data,
    };
    let chartData = [];
    let chartDataKey = '';
    let ulbs = [...new Set(jp.query(parsed, '$.data[*].ulbName'))];
    let finYears = [...new Set(jp.query(parsed, '$.data[*].finYear'))];
    let kpis = [...new Set(jp.query(parsed, '$.data[*].kpiName'))];

    if (kpis.length === 1) {
      if (finYears.length > 1 && ulbs.length > 1) {
        chartData = data.filter(el => el.ulbName === ulbs[0]);
        chartDataKey = 'finYear';
        return cb(chartData, chartDataKey);
      }
      if (finYears.length > 1 && ulbs.length === 1) {
        chartData = data;
        chartDataKey = 'finYear';
        return cb(chartData, chartDataKey);
      }
      if (finYears.length === 1 && ulbs.length > 1) {
        chartData = data;
        chartDataKey = 'ulbName';
        return cb(chartData, chartDataKey);
      }
      if (finYears.length === 1 && ulbs.length === 1) {
        chartData = data;
        chartDataKey = 'finYear';
        return cb(chartData, chartDataKey);
      }
    }

    if (kpis.length > 1) {
      if (finYears.length > 1 && ulbs.length > 1) {
        chartData = data.filter(el => el.ulbName === ulbs[0]);
        chartDataKey = 'finYear';
        return cb(chartData, chartDataKey);
      }
      if (finYears.length > 1 && ulbs.length === 1) {
        chartData = data.filter(el => el.finYear === finYears[0]);
        chartDataKey = 'finYear';
        return cb(chartData, chartDataKey);
      }
      if (finYears.length === 1 && ulbs.length > 1) {
        chartData = data.filter(el => el.ulbName === ulbs[0]);
        chartDataKey = 'finYear';
        return cb(chartData, chartDataKey);
      }
      if (finYears.length === 1 && ulbs.length === 1) {
        chartData = data;
        chartDataKey = 'kpiName';
        return cb(chartData, chartDataKey);
      }
    }
    return cb(null, null);
  }

  processOnClickKPIDataRepresentation = () => {
    this.props.toggleDataViewFormat('chartview');
  };

  render() {
    return <div>{this.renderKPIData()}</div>;
  }

  /**
   * render
   * toggle between chart & table format
   */
  renderKPIData = () => {
    if (this.state.showChartView) {
      return this.renderChart();
    }
  };

  /**
   * render
   * presents chart
   */
  renderChart = () => {
    if (this.state.data.length < 1) {
      return (
        <div style={{ textAlign: 'center' }}>
          <br />
          <br />
          <Card className="uiCard">
            <CardHeader title={<div style={{ fontSize: '16px' }}> insufficient data to draw the chart </div>} />
          </Card>
        </div>
      );
    }

    let title = `KPI representation for ${this.props.kpis}`;
    return (
      <div>
        <br />
        <br />
        <Card className="uiCard" style={this.props.kpiType === 'VALUE' ? { textAlign: 'left' } : { textAlign: 'center' }}>
          <RaisedButton
            style={{ marginTop: '15px', marginLeft: '90%' }}
            label={'Tabular'}
            primary={true}
            type="button"
            disabled={false}
            onClick={this.processOnClickKPIDataRepresentation}
          />
          <CardHeader style={{ paddingBottom: 0 }} title={<div style={{ fontSize: 16, marginBottom: '25px' }}> {title} </div>} />
          {this.renderChartType()}
        </Card>
      </div>
    );
  };

  /**
   * render
   * render BarChart or PieChart base upon the KPITypes selected
   */
  renderChartType = () => {
    if (this.props.kpiType === 'VALUE') {
      return this.renderBarChart();
    }

    return this.renderPieChart();
  };

  /**
   * render
   * renders BarChart for VALUE type KPI
   */
  renderBarChart = () => {
    return (
      <div>
        <BarChart padding={'50%'} width={600} height={500} data={this.state.data} margin={{ top: 20, right: 30, left: 30, bottom: 5 }}>
          <XAxis dataKey={this.state.dataKey} />
          <YAxis />
          <CartesianGrid strokeDasharray="3 3" />
          <Tooltip />
          <Legend />
          <Bar name="KPI Target" dataKey="target" fill="#0088FE" />
          <Bar name="Actual Value" dataKey="value" fill="#00C49F" />
        </BarChart>
      </div>
    );
  };

  /**
   * render
   * renders PieChart for OBJECTIVE type KPI
   */
  renderPieChart = () => {
    let data = [
      {
        name: 'YES',
        value: this.state.data.filter(el => el.value === 1).length,
      },
      {
        name: 'NO',
        value: this.state.data.filter(el => el.value === 2).length,
      },
      {
        name: 'IN PROGRESS',
        value: this.state.data.filter(el => el.value === 3).length,
      },
    ];
    const COLORS = ['#0088FE', '#00C49F', '#FFBB28'];

    return (
      <div style={{ marginLeft: '35%' }}>
        <PieChart width={600} height={500} data={this.state.data} margin={{ top: 20, right: 30, left: 40, bottom: 5 }}>
          <Pie dataKey={'value'} isAnimationActive={true} data={data} cx={200} cy={200} outerRadius={220} fill="#8884d8" labelLine={false}>
            {data.map((entry, index) => <Cell key={index} fill={COLORS[index % COLORS.length]} />)}
          </Pie>
          <Tooltip />
          <Legend />
        </PieChart>
      </div>
    );
  };
}
