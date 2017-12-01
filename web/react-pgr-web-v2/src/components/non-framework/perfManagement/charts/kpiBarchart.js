import React, { Component } from 'react';
import {BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend} from 'recharts';
var jp = require('jsonpath')

export default class EGBarChart extends Component {
    constructor(props) {
        super(props)
        this.state  = {
            barChartData: [],
            barChartLegend: []
        }
    }

    componentWillMount() {
        let chartData = []
        // if ((this.props.FY.length == 1) && (this.props.ULB.length == 1) && (this.props.KPI.length == 1)) {
        //     chartdata   = this.formatChartData(this.parseWhenSingleFYSingleULBSingleKPI(this.props.data))
        // }
        // if ((this.props.FY.length == 1) && (this.props.ULB.length == 1) && (this.props.KPI.length > 1)) {
        //     chartdata   = this.formatChartData(this.parseWhenSingleFYSingleULBMultiKPI(this.props.data))
        // }
        chartData       = this.formatChartData(this.parseWhenSingleFYSingleULBMultiKPI(this.props.data))
        let chartLegend = this.formatChartLegends(chartData)

        this.setState({
            barChartData: chartData,
            barChartLegend: chartLegend
        })
    }

    getMonth(month) {
        switch(month) {
            case '1':
                return 'JANUARY'
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
            default:
                return 'JANUARY'
        }
    }

    parseWhenSingleFYSingleULBSingleKPI(data) {

    }

    parseWhenSingleFYSingleULBMultiKPI(data) {
        let finalData = jp.query(data, '$.kpiValues[*]').map((item, index) => {
            let obj     = {}
            if (jp.query(item, '$.kpi.name').length > 0) {
                obj.name    = jp.query(item, '$.kpi.name')[0]
            }
            if (jp.query(item, '$.kpi.kpiTarget.targetValue').length > 0) {
                obj.amt     = parseInt(jp.query(item, '$.kpi.kpiTarget.targetValue')[0])
            }
            obj.values      = jp.query(item, '$.kpiValue.valueList[*]').sort((obj1, obj2)=> {
                if (parseInt(obj1.period) > parseInt(obj2.period)) {
                    return 1;
                }
                if (parseInt(obj1.period) < parseInt(obj2.period)) {
                    return -1;
                }
                return 0;
            }).map((value, index) => {
                return {
                    dataKey: this.getMonth(value.period),
                    value: value.value === '' ? 0:  parseInt(value.value)
                }
            })
            return obj
        })
        return finalData
    }

    formatChartData(data) {
        return data.map((item, index) => {
            let obj     = {}
            obj.name    = item.name
            obj.amt     = item.amt
    
            for (let index = 0; index < item.values.length; index++) {
                obj[item.values[index].dataKey]     = item.values[index].value
            }
            return obj;
        })
    }

    formatChartLegends(data) {
        if (data && data.length > 0) {
            let items   = Object.keys(data[0])
            items       = items.filter(item => item !== 'name')
            items       = items.filter(item => item !== 'amt')
                    
            return items.map((item, index) => {
                return (
                    {
                        dataKey: item,
                        fill: '#'+(Math.random()*0xFFFFFF<<0).toString(16)
                    }            
                )
            })
        }
    }

    render () {
        return (
            <div style={{"textAlign": "center"}}>
                <BarChart width={600} height={600} data={this.state.barChartData}
                    margin={{top: 5, right: 30, left: 20, bottom: 5}}>
                    <XAxis dataKey="name"/>
                    <YAxis/>
                    <CartesianGrid strokeDasharray="3 3"/>
                    <Tooltip/>
                    <Legend />
                        {
                            this.state.barChartLegend.map((item, index) => {
                                return <Bar key={index} dataKey={item.dataKey} fill={item.fill} />
                            })
                        }
                </BarChart>
            </div>
        );
    }
}