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
      chartDataIndex: 1,
      maxChartData: 0,
    };
    this.kpis = this.props.kpis;
  }

  componentDidMount() {
    this.formatChartData(this.parseCompareSearchResponse(this.props.data), (data, dataKey) => {
      if (!data || !dataKey) {
      } else {
        this.setState({
          data: data,
          dataKey: dataKey,
          chartDataIndex: 1,
          maxChartData: data.length,
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

  getMonth(month) {
    switch(month) {
        case '1':
            return 'JANUNARY'
        case '2':
            return 'FEBRUARY'
        case '3':
            return 'MARCH'
        case '4':
            return 'APRIL'
        case '5':
            return 'MAY'
        case '6':
            return 'JUNE'
        case '7':
            return 'JULY'
        case '8':
            return 'AUGUST'
        case '9':
            return 'SEPTEMBER'
        case '10':
            return 'OCTOBER'
        case '11':
            return 'NOVEMBER'
        case '12':
            return 'DECEMBER'
    }
}

parseCompareSearchResponse(res) {
  return this.flattenArray(
    jp.query(res, '$.ulbs[*]').map((ulbs, index) => {
      return jp.query(ulbs, '$.finYears[*]').map((finYears, index) => {
          return jp.query(finYears, '$.kpiValues[*]').map((kpis, index) => {
              return jp.query(kpis, '$.valueList[*]').map((values, index) => {
                  return {
                    ulbName: jp.query(ulbs, '$.ulbName').join(''),
                    finYear:jp.query(finYears, '$.finYear').join(''),
                    kpiName:jp.query(kpis, '$.kpi.name').join(''),
                    target: parseInt(jp.query(kpis, '$.kpi.kpiTargets[*].targetValue').join('')) || 0,
                    value: parseInt(jp.query(kpis, '$.consolidatedValue').join('')) || 0,
                    period: jp.query(values, '$.period').join('') || 0,
                    name: this.getMonth(jp.query(values, '$.period').join('') || '1'),
                    monthlyValue: parseInt(jp.query(values, '$.value').join('')) || 0,
                  }
              })
          })
      })
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

  formatChartData(data, cb) {
    let parsed = {
      data: data
    }

    let ulbs        = [...new Set(jp.query(parsed, '$.data[*].ulbName'))];
    let finYears    = [...new Set(jp.query(parsed, '$.data[*].finYear'))];
    let array       = new Array()

    for (let index = 0; index < ulbs.length; index++) {
        const element = ulbs[index];
        for (let finYearIndex = 0; finYearIndex < finYears.length; finYearIndex++) {
            const element = finYears[finYearIndex];
            array.push({
                ulbName: ulbs[index],
                finYear: finYears[finYearIndex],
                data: data.filter(  (ele) => {
                    if ((ele.ulbName ===  ulbs[index]) && (ele.finYear === finYears[finYearIndex])) {
                        return ele;
                    }
                })
            })
        }
    }

    let parsedData = array.map((elem, index) => {
        return {
            ulbName: elem.ulbName,
            finYear: elem.finYear,
            data: elem.data.sort((obj1, obj2) => obj1.period - obj2.period)
        }
    })

    cb(parsedData, "monthlyValue")
  }

  processOnClickKPIDataRepresentation = () => {
    this.props.toggleDataViewFormat('chartview');
  };

  processOnClickNextKPIData = () => {
    if (this.state.chartDataIndex < this.state.maxChartData) {
      this.setState({
        chartDataIndex: this.state.chartDataIndex + 1
      })
    } else {
      this.setState({
        chartDataIndex: this.state.maxChartData
      })
    }
  }

  processOnClickPreviousKPIData = () => {
    if (this.state.chartDataIndex > 1) {
      this.setState({
        chartDataIndex: this.state.chartDataIndex - 1
      })
    } else {
      this.setState({
        chartDataIndex: 1
      })
    }
  }

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
    let data = this.state.data[this.state.chartDataIndex - 1]

    let ulb = this.props.ulbs.filter((item) => {
      if (item[0].code === data.ulbName) {
        return item[0];
      }
    })
    let ulbName = data.ulbName;
    if (ulb && ulb[0] && ulb[0][0] && ulb[0][0]['name']) {
      ulbName = ulb[0][0].name
    }
    let title = `Performance for ${this.kpis} for ULB ${ulbName} in FinancialYear ${data.finYear}`;

    return (
      <div>
        <br />
        <br />
        <Card className="uiCard" style={{ textAlign: 'center' }}>
          <CardHeader style={{ paddingBottom: 0 }} title={<div style={{ fontSize: 16, marginBottom: '25px' }}> {title} </div>} />
          <RaisedButton
            style={{ marginLeft: '40%' }}
            label={'Previous'}
            primary={true}
            type="button"
            disabled={false}
            onClick={this.processOnClickPreviousKPIData}
          />

          <RaisedButton
            style={{ marginLeft: '10px' }}
            label={'Next'}
            primary={true}
            type="button"
            disabled={false}
            onClick={this.processOnClickNextKPIData}
          />
          <RaisedButton
            style={{ marginLeft: '40%' }}
            label={'Tabular'}
            primary={true}
            type="button"
            disabled={false}
            onClick={this.processOnClickKPIDataRepresentation}
          />
          
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
    let data = this.state.data[this.state.chartDataIndex - 1]
    console.log(`rendering bar chart for ${data}`)

    return (
      <div style={{ marginLeft: '35%', marginTop: '10px' }}>
        <BarChart width={600} height={500} data={data.data} margin={{ top: 20, right: 30, left: 30, bottom: 5 }}>
          <XAxis dataKey={this.state.dataKey} />
          <YAxis />
          <CartesianGrid strokeDasharray="3 3" />
          <Tooltip />
          <Legend />

          <Bar name="Target Value" dataKey="target" fill="#0088FE" />
          <Bar name="Actual Value" dataKey="monthlyValue" fill="#00C49F" />
        </BarChart>
      </div>
    );
  };

  /**
   * render
   * renders PieChart for OBJECTIVE type KPI
   */
  renderPieChart = () => {
    
    let cdata    = this.state.data[this.state.chartDataIndex - 1]
    let data = [
      {
        name: 'YES',
        value: cdata.data.filter(el => el.monthlyValue === 1).length,
      },
      {
        name: 'NO',
        value: cdata.data.filter(el => el.monthlyValue === 2).length,
      },
      {
        name: 'IN PROGRESS',
        value: cdata.data.filter(el => el.monthlyValue === 3).length,
      },
    ];
    const COLORS = ['#0088FE', '#00C49F', '#FFBB28'];
    console.log(data)

    return (
      <div style={{ marginLeft: '35%', marginTop: '10px' }}>
        <PieChart width={600} height={500} data={data} margin={{ top: 20, right: 30, left: 40, bottom: 5 }}>
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
