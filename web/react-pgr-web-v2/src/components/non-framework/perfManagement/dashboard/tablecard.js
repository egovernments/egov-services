import React, { Component } from 'react';
import { Card, CardText, CardMedia, CardHeader, CardTitle } from 'material-ui/Card';
import { Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn } from 'material-ui/Table';
import RaisedButton from 'material-ui/RaisedButton';

var jp = require('jsonpath');

export default class TableCard extends Component {
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
                if (this.props.kpiType === 'TEXT') {
                  return {
                    ulbName: jp.query(ulbs, '$.ulbName').join(''),
                    finYear:jp.query(finYears, '$.finYear').join(''),
                    kpiName:jp.query(kpis, '$.kpi.name').join(''),
                    target: jp.query(kpis, '$.kpi.kpiTargets[*].targetValue').join(''),
                    value: jp.query(kpis, '$.consolidatedValue').join(''),
                    period: jp.query(values, '$.period').join('') || 0,
                    name: this.getMonth(jp.query(values, '$.period').join('') || '1'),
                    monthlyValue: jp.query(values, '$.value').join(''),
                  }
                } else {
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
    this.props.toggleDataViewFormat('tableview');
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
      return this.renderTable();
    }
  };
  /**
   * render
   * presents same data in tabular format
   */
  renderTable = () => {
    if (this.state.data.length < 1) {
      return (
        <div style={{ textAlign: 'center' }}>
          <br />
          <br />
          <Card className="uiCard">
            <CardHeader title={<strong> insufficient data to draw the chart </strong>} />
          </Card>
        </div>
      );
    }
    let data    = this.state.data[this.state.chartDataIndex - 1]
    let title   = `Performance for ${this.kpis} for ULB ${data.ulbName} in FinancialYear ${data.finYear}`;
    let headers = Object.keys(data.data[0])
                      .filter((elem) => {return elem.toUpperCase() !== 'ULBNAME'})
                      .filter((elem) => {return elem.toUpperCase() !== 'PERIOD'})
                      .filter((elem) => {return elem.toUpperCase() !== 'VALUE'})
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

          <Table style={{ color: 'black', fontWeight: 'normal', marginTop: '10px' }} bordered responsive className="table-striped">
            <TableHeader adjustForCheckbox={false} displaySelectAll={false}>
              <TableRow>{headers.map((item, index) => <TableHeaderColumn key={index}>{item.toUpperCase()}</TableHeaderColumn>)}</TableRow>
            </TableHeader>

            <TableBody displayRowCheckbox={false}>
              {data.data.map((item, index) => (
                  <TableRow key={index}> {headers.map((el, index) => <TableRowColumn style={{whiteSpace: 'normal', wordWrap: 'break-word'}} key={index}>{item[el]} </TableRowColumn>)} </TableRow>
              ))}
            </TableBody>
          </Table>
        </Card>
      </div>
    );
  };
}
