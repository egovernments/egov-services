import React, { Component } from 'react';
import { Card, CardText, CardMedia, CardHeader, CardTitle } from 'material-ui/Card';
import { Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn } from 'material-ui/Table';
import RaisedButton from 'material-ui/RaisedButton';
import {
  parseCompareSearchResponse,
  parseCompareSearchConsolidatedResponse
} from '../apis/apis';
import {
  formatChartData,
  formatConsolidatedChartData,
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
    if (this.props.isReportConsolidated) {
      formatConsolidatedChartData(parseCompareSearchConsolidatedResponse(this.props.data), (data, dataKey) => {
        if (!data || !dataKey) {
        } else {
          this.setState({
            data: data,
            dataKey: dataKey,
          });
        }
      });
    } else {
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

  getTableHeaders = () => {
    if (this.props.isReportConsolidated) {
      return Object.keys(this.state.data[0]);
    }
    return Object.keys(this.state.data.data[0])
          .filter((elem) => {return elem.toUpperCase() !== 'ULBNAME'})
          .filter((elem) => {return elem.toUpperCase() !== 'PERIOD'})
          .filter((elem) => {return elem.toUpperCase() !== 'VALUE'});
  }

  getTableData = () => {
    if (this.props.isReportConsolidated) {
      return this.state.data;
    }
    return this.state.data[this.state.chartDataIndex - 1];
  }

  getReportTitle = () => {

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
      return this.renderReportTable();
    }
  };

  /**
   * render
   * render insufficient data to draw the chart
   */
  renderInsufficientDataForChart = () => {
    console.log('insufficient data for chart')
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

  /**
   * render
   * presents same data in tabular format
   */
  renderReportTable = () => {
    if (this.state.data.length < 1) {
      return (
        this.renderInsufficientDataForChart()
      )
    }

    let data    = this.getTableData();
    let headers = this.getTableHeaders();

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
    
    
    console.log(data)
    console.log(headers)

    return (
      <div>
        <br />
        <br />
        <Card className="uiCard" style={{ textAlign: 'center' }}>
          <CardHeader style={{ paddingBottom: 0 }} title={<div style={{ fontSize: 16, marginBottom: '25px' }}> {title} </div>} />
          {this.renderReportNavigationButton('Charts')}
          {this.renderTable(headers, data)}
        </Card>
      </div>
    );
  };

  /**
   * render
   * render table as per provided headers
   */
  renderTable = (headers, data) => {
    if (this.props.isReportConsolidated) {
      return (
        <Table style={{ color: 'black', fontWeight: 'normal', marginTop: '10px' }} bordered responsive className="table-striped">
            <TableHeader adjustForCheckbox={false} displaySelectAll={false}>
              <TableRow>{headers.map((item, index) => <TableHeaderColumn key={index}>{item.toUpperCase()}</TableHeaderColumn>)}</TableRow>
            </TableHeader>

            <TableBody displayRowCheckbox={false}>
              {data.map((item, index) => (
                <TableRow key={index}> {headers.map((el, index) => <TableRowColumn key={index}>{item[el]} </TableRowColumn>)} </TableRow>
              ))}
            </TableBody>
          </Table>
      )
    }

    return (
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
    )
  }

  /**
   * render
   * render next/prev button to navigate when report is not consolidated
   */
  renderReportNavigationButton = (label) => {
    if (this.props.isReportConsolidated) {
      return (
        <RaisedButton
          style={{ marginLeft: '90%' }}
          label={label}
          primary={true}
          type="button"
          disabled={false}
          onClick={this.processOnClickKPIDataRepresentation}
        />
      )
    }

    return (
      <div>
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
          label={label}
          primary={true}
          type="button"
          disabled={false}
          onClick={this.processOnClickKPIDataRepresentation}
        />
      </div>
    )
  }
}
