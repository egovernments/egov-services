import React, { Component } from 'react';
import { Card, CardText, CardMedia, CardHeader, CardTitle } from 'material-ui/Card';
import { Table, TableBody, TableHeader, TableHeaderColumn, TableRow, TableRowColumn } from 'material-ui/Table';
import RaisedButton from 'material-ui/RaisedButton';
import {
  parseCompareSearchResponse,
  parseCompareSearchConsolidatedResponse,
  parseTenantName
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

      formatConsolidatedChartData(parseCompareSearchConsolidatedResponse(this.props.data, this.props.kpiType === 'TEXT' ? true : false), (data, dataKey) => {
        if (!data || !dataKey) {
        } else {
          this.setState({
            data: data,
            dataKey: dataKey,
          });
        }
      });
    } else {
      formatChartData(parseCompareSearchResponse(this.props.data, this.props.kpiType === 'TEXT' ? true : false), (data, dataKey) => {
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
    return Object.keys(this.state.data[this.state.chartDataIndex - 1].data[0])
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

  getULBName = (code) => {
    let ulbName = parseTenantName(this.props.ulbs, code);
    if (ulbName.length == 0) {
      return code
    }
    return ulbName[0]['name']
  }

  getObjectiveValue(value) {
    switch (value) {
      case 1:
        return 'YES';
      case 2:
        return 'NO';
      case 3:
        return 'IN PROGRESS';
    
      default:
        return 'NO';
    }
  }

  getModifiedChartData = (data) => {
    if (this.props.kpiType === 'OBJECTIVE') {
      if (this.props.isReportConsolidated) {
        return data.map((item, index) => {
          return {
            ...item,
            ulbName: this.getULBName(item.ulbName),
            target: this.getObjectiveValue(item.target),
            value: this.getObjectiveValue(item.value)
          }
        })
      }
      return data.map((item, index) => {
        return {
          ...item,
          ulbName: this.getULBName(item.ulbName),
          target: this.getObjectiveValue(item.target),
          monthlyValue: this.getObjectiveValue(item.monthlyValue)
        }
      })
    }
    return data.map((item, index) => {
      return {
        ...item,
        ulbName: this.getULBName(item.ulbName)
      }
    })
  }

  getChartData = () => {
    if (this.state.data.length === 0) {
      return []
    }

    if (this.props.isReportConsolidated) {
      return this.state.data;
    }
    return this.state.data[this.state.chartDataIndex - 1].data;
  }

  getReportTitle = () => {
    if (this.props.isReportConsolidated) {
      return `Consolidated performance of KPI  ${this.props.kpis}`
    }
    let data = this.state.data[this.state.chartDataIndex - 1]
    let ulbName = this.getULBName(data['ulbName']); 
    return `Monthly performance of KPI ${this.props.kpis} for ULB ${ulbName} in FinancialYear ${data.finYear}`
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
    
    if (this.getChartData().length < 1) {
      return (
        this.renderInsufficientDataForChart()
      )
    }

    return (
      <div>
        <br />
        <br />
        <Card className="uiCard" style={{ textAlign: 'center' }}>
          <CardHeader style={{ paddingBottom: 0 }} title={<div style={{ fontSize: 16, marginBottom: '25px' }}> {this.getReportTitle()} </div>} />
          {this.renderReportNavigationButton('Charts')}
          {this.renderTable()}
        </Card>
      </div>
    );
  };

  /**
   * render
   * render table as per provided headers
   */
  renderTable = () => {
    let headers = this.getTableHeaders();
    let data    = this.getModifiedChartData(this.getChartData())
    return (
      <Table style={{ color: 'black', fontWeight: 'normal', marginTop: '10px' }} bordered responsive className="table-striped">
            <TableHeader adjustForCheckbox={false} displaySelectAll={false}>
              <TableRow>{headers.map((item, index) => <TableHeaderColumn key={index}>{item.toUpperCase()}</TableHeaderColumn>)}</TableRow>
            </TableHeader>

            <TableBody displayRowCheckbox={false}>
              {data.map((item, index) => (
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
