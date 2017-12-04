import React, { Component } from 'react';
import {BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';
var jp = require('jsonpath')

export default class EGBarChart extends Component {
    constructor(props) {
        super(props)
        this.state  = {
            data: [],
            dataKey: null
        }
    }

    componentDidMount() {
        let chartData = []
        console.log(this.props.data)
        this.formatParsedChartData(this.parseCompareSearchResponse(this.props.data), (data, dataKey) => {
            if (!data || !dataKey) {

            } else {
                this.setState({
                    data: data,
                    dataKey: dataKey
                })
            }
        })
    }

    /**
     * parsing and arranging search response
     */
    flattenArray(array) {
        let self = this
        return array.reduce(function(memo, el) {
          var items = Array.isArray(el) ? self.flattenArray(el) : [el];
          return memo.concat(items);
        }, []);
    }

    parseCompareSearchResponse(res) {
        return this.flattenArray(jp.query(res, '$.ulbs[*]').map((ulbs, index) => {
            return jp.query(ulbs, '$.finYears[*]').map((finYears, index) => {
                return jp.query(finYears, '$.kpiValues[*]').map((kpis, index) => {
                    return {
                        ulbName: jp.query(ulbs, '$.ulbName').join(''),
                        finYear:jp.query(finYears, '$.finYear').join(''),
                        kpiName:jp.query(kpis, '$.kpi.name').join(''),
                        target: parseInt(jp.query(kpis, '$.kpi.kpiTarget.targetValue').join('')),
                        value: parseInt(jp.query(kpis, '$.consolidatedValue').join(''))
                    }
                })
            })
        }))
    }

    formatParsedChartData(data, cb) {
        let parsed = {
            data: data
        }
        let chartData       = [];
        let chartDataKey    = "";
        let ulbs        = [...new Set(jp.query(parsed, '$.data[*].ulbName'))];
        let finYears    = [...new Set(jp.query(parsed, '$.data[*].finYear'))];
        let kpis        = [...new Set(jp.query(parsed, '$.data[*].kpiName'))];

        console.log(kpis)
        if (kpis.length == 1) {
            if (finYears.length > 1 && ulbs.length > 1) {
                chartData       = data.filter((el) => el.ulbName === ulbs[0])
                chartDataKey    = "finYear"
                return cb(chartData, chartDataKey)
            }
            if (finYears.length > 1 && ulbs.length == 1) {
                chartData       = data;
                chartDataKey    = "finYear"
                return cb(chartData, chartDataKey)
            }
            if (finYears.length == 1 && ulbs.length > 1) {
                chartData       = data;
                chartDataKey    = "ulbName"
                return cb(chartData, chartDataKey)
            }
            if (finYears.length == 1 && ulbs.length == 1) {
                chartData       = data;
                chartDataKey    = "finYear"
                return cb(chartData, chartDataKey)
            }
        }

        if (kpis.length > 1) {
            if (finYears.length > 1 && ulbs.length > 1) {
                chartData       = data.filter((el) => el.ulbName === ulbs[0])
                chartDataKey    = "finYear"
                return cb(chartData, chartDataKey)
            }
            if (finYears.length > 1 && ulbs.length == 1) {
                chartData       = data.filter((el) => el.finYear ===finYears[0])
                chartDataKey    = "finYear"
                return cb(chartData, chartDataKey)
            }
            if (finYears.length == 1 && ulbs.length > 1) {
                chartData       = data.filter((el) => el.ulbName === ulbs[0])
                chartDataKey    = "finYear"
                return cb(chartData, chartDataKey)
            }
            if (finYears.length == 1 && ulbs.length == 1) {
                chartData       = data;
                chartDataKey    = "kpiName"
                return cb(chartData, chartDataKey)
            }
        }
        return cb(null, null)
    }

    render () {
        return (
            <BarChart width={600} height={700} data={this.state.data} margin={{top: 20, right: 30, left: 20, bottom: 5}}>
                <XAxis dataKey={this.state.dataKey}/>
                <YAxis yAxisId="left" orientation="left" stroke="#8884d8"/>
                <YAxis yAxisId="right" orientation="right" stroke="#82ca9d"/>
                <Tooltip/>
                <Legend />
                <Bar yAxisId="left" dataKey="target" fill="#8884d8" />
                <Bar yAxisId="right" dataKey="value" fill="#82ca9d" />
            </BarChart>
        );
    }
}