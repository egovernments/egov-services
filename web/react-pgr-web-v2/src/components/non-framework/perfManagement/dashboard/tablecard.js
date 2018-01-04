import React, { Component } from 'react';
import { Card, CardText, CardMedia, CardHeader, CardTitle } from 'material-ui/Card';
import { Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn } from 'material-ui/Table';
import RaisedButton from 'material-ui/RaisedButton';
import {
  parseCompareSearchResponse
} from '../apis/apis';
import {
  formatChartData,
  formatParsedChartData,
} from '../apis/helpers';

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
    formatChartData(parseCompareSearchResponse(this.props.data), (data, dataKey) => {
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
