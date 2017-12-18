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
              target: jp.query(kpis, '$.kpi.kpiTargets[*].targetDescription').join(''),
              value: jp.query(kpis, '$.consolidatedValue').join(''),
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
    this.props.toggleDataViewFormat('tableview');
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
    let title = `KPI representation for ${this.props.kpis}`;
    let headers = Object.keys(this.state.data[0]);

    return (
      <div>
        <br />
        <br />
        <Card className="uiCard" style={{ textAlign: 'center' }}>
          <RaisedButton
            style={{ marginTop: '15px', marginLeft: '90%' }}
            label={'Charts'}
            primary={true}
            type="button"
            disabled={false}
            onClick={this.processOnClickKPIDataRepresentation}
          />
          <CardHeader style={{ paddingBottom: 0 }} title={<div style={{ fontSize: 16, marginBottom: '20px' }}> {title} </div>} />
          <Table style={{ color: 'black', fontWeight: 'normal' }} bordered responsive className="table-striped">
            <TableHeader adjustForCheckbox={false} displaySelectAll={false}>
              <TableRow>{headers.map((item, index) => <TableHeaderColumn key={index}>{item.toUpperCase()}</TableHeaderColumn>)}</TableRow>
            </TableHeader>

            <TableBody displayRowCheckbox={false}>
              {this.state.data.map((item, index) => (
                <TableRow key={index}> {headers.map((el, index) => <TableRowColumn key={index}>{item[el]} </TableRowColumn>)} </TableRow>
              ))}
            </TableBody>
          </Table>
        </Card>
      </div>
    );
  };
}
