import React, { Component } from 'react';
import {Card, CardText} from 'material-ui/Card';
import {Grid, Row, Col} from 'react-bootstrap';
import RaisedButton from 'material-ui/RaisedButton';
import {
    fetchDepartmentAPI,
    fetchDepartmentKPIsAPI,
    fetchULBsAPI,
    fetchFinancialYearsAPI,
    fetchCompareSearchAPI,
    parseDepartmentResponse,
    parseULBResponse,
    parseFinancialYearResponse,
    parseDepartmentKPIsAsPerKPIType
} from '../apis/apis';
import LoadingIndicator from '../../../common/LoadingIndicator';
import DashboardCard from './dashboardcard';
import KPISelectField from './kpiselectfield';
import BarChartCard from './barchartcard';

var jp = require('jsonpath');

export default class Dashboard extends Component {
    constructor(props) {
        super(props)
        this.state = {
            apiLoading: false,
            departments: [],
            showDepartmentView: true,
            showKPIQueryView: false,
            showChartView: false,
            disableViewButton: false,
            kpiIndices: [0],
            ulbIndices: [0],
            fyIndices: [0]
        }

        this.kpiLabel   = "KPIs";
        this.ulbLabel   = "ULB(s)";
        this.fyLabel    = "Financial Years";
        this.ulbRes     = null;
        this.fyRes      = null;
        this.kpiRes     = null;
        this.chartRes   = null;
    }

    render() {
        return(
            <div>
                {
                    this.renderUIBusy()
                }
                {
                    this.renderDepartments()
                }
                {
                    this.renderKPIQueryView()
                }
                {
                    this.renderChart()
                }
            </div>
        )
    }

    componentDidMount() {
        this.busyUI(true)
        fetchDepartmentAPI((err, res) => {
            this.busyUI(false)
            if (err || !res) {
                this.toast('unable to fetch departments')
            } else {
                this.setState({
                    departments: res
                })
            }
        })
    }

    /**
     * helpers
     */
    toast = (msg) => {
        console.log(msg)
    }

    busyUI = (status) => {
        this.setState({
            apiLoading:status
        })
    }

    processOnClickOnCard = (index) => {
        let departments = parseDepartmentResponse(this.state.departments);
        let department  = departments[index];

        this.busyUI(true);
        fetchDepartmentKPIsAPI(department.id, (err, res) => {
            if (err || !res) {
                this.busyUI(false)
                this.toast('unable to fetch kpi for the selected department')
            } else {
                this.kpiRes = res;
                fetchULBsAPI((err, res) => {
                    if (err || !res) {
                        this.busyUI(false)
                        this.toast('unable to fetch ulbs')
                    } else {
                        this.ulbRes = res;
                        fetchFinancialYearsAPI((err, res) => {
                            if (err || !res) {
                                this.busyUI(false)
                                this.toast('unable to fetch financial years')
                            } else {
                                this.fyRes  = res;
                                this.busyUI(false)

                                this.setState({
                                    showDepartmentView: false,
                                    showKPIQueryView: true
                                })
                            }
                        })
                    }
                })
            }
        })
    }

    /**
     * SelectField manipulations.
     */
    processSelectOnKPISelectField = (index, values, label) => {
        if (label === this.kpiLabel) {
            if (values.length > 1) {
                if (this.state.ulbIndices.length > 1 || this.state.fyIndices.length > 1) {
                    this.toast('You have already selected multiple ULBs or Financial Years values')
                } else {
                    this.setState({
                        kpiIndices: values
                    })
                }
            } else {
                this.setState({
                    kpiIndices: values
                })
            }
        }
        if (label === this.ulbLabel) {
            if (values.length > 1) {
                if (this.state.kpiIndices.length > 1 || this.state.fyIndices.length > 1) {
                    this.toast('You have already selected multiple KPIs or Financial Years values')
                } else {
                    this.setState({
                        ulbIndices: values
                    })
                }
            } else {
                this.setState({
                    ulbIndices: values
                })
            }
        }
        if (label === this.fyLabel) {
            if (values.length > 1) {
                if (this.state.kpiIndices.length > 1 || this.state.ulbIndices.length > 1) {
                    this.toast('You have already selected multiple KPIs or ULBs values')
                } else {
                    this.setState({
                        fyIndices: values
                    })
                }
            } else {
                this.setState({
                    fyIndices: values
                })
            }
        }

        if (values.length === 0) {
            this.setState({
                disableViewButton: true
            })
        } else {
            this.setState({
                disableViewButton: false
            })
        }
    }

    processOnClickViewButton = () => {
        let finYears    = this.state.fyIndices.map((item, index) => jp.query(this.fyRes, `$.financialYears[${item}].finYearRange`)).join(',')
        let ulbs        = this.state.ulbIndices.map((item, index) => jp.query(this.ulbRes, `$.MdmsRes.tenant.tenants[${item}].code`)).join(',')
        let kpis        = this.state.kpiIndices.map((item, index)=> jp.query(this.kpiRes, `$.KPIs[${item}].code`)).join(',')
        console.log(finYears)
        console.log(ulbs)
        console.log(kpis)

        this.busyUI(true)
        fetchCompareSearchAPI(finYears, kpis, ulbs, (err, res) => {
            this.busyUI(false)
            if (err || !res) {
                this.toast('Unable to get report data')
            } else {
                this.chartRes   = res;
                this.setState({
                    showChartView: true
                });
            }
        })
    }

    processOnClickBackButton = () => {
        this.setState({
            showKPIQueryView: false,
            showChartView: false,
            showDepartmentView: true
        })
    }

    /**
     * render
     * show/hide UI busy
     */
    renderUIBusy = () => {
        return (
            this.state.apiLoading ? <LoadingIndicator status={'loading'} /> : <LoadingIndicator status={'hide'} />
        )
    }

    /**
     * render
     * present card as per departments
     */
    renderDepartments = () => {
        if (!this.state.showDepartmentView) {
            return (
                <div></div>
            )
        }

        let departments = parseDepartmentResponse(this.state.departments)
        if (departments.length > 0) {
            return (
                departments.map((item, index) => <DashboardCard key={index} index={index} onClick={this.processOnClickOnCard} name={item.name} logo={require('../../../../images/headerLogo.png')} />)
            )
        }
    }

    /**
     * render
     * present card with query options
     */
    renderKPIQueryView = () => {
        if (!this.state.showKPIQueryView) {
            return (<div></div>)
        }
        const style = {
            margin: 12,
            refresh: {
                display: 'inline-block',
                position: 'relative',
                zIndex: 9999,
                marginLeft: '50%',
                marginTop: '20%'
            }
        };

        return (
            <div>
                <br />
                <RaisedButton label="Back" style={{'margin': '12px'}} primary={true} type="button" onClick={this.processOnClickBackButton} 
                                icon={<i className="material-icons" style={{color:"white"}}>arrow_back</i>} 
                />
                <br />
                <br />
                <br />
                <Card className="uiCard">
                    <Grid fluid>
                        <CardText>
                            <Row>
                                <Col xs={12} sm={4} md={4} >
                                    <KPISelectField label={this.kpiLabel} mandatory= {true} multiple={true} disabled={false} displayKey={"name"}
                                        value={this.state.kpiIndices}
                                        items={parseDepartmentKPIsAsPerKPIType(this.kpiRes, 'VALUE')}
                                        onItemsSelected={this.processSelectOnKPISelectField}
                                    />
                                </Col>
                                <Col xs={12} sm={4} md={4} >
                                    <KPISelectField label={this.ulbLabel} mandatory= {true} multiple={true} disabled={false} displayKey={"name"}
                                        value={this.state.ulbIndices}
                                        items={parseULBResponse(this.ulbRes)}
                                        onItemsSelected={this.processSelectOnKPISelectField}
                                    />
                                </Col>
                                <Col xs={12} sm={4} md={4} >
                                    <KPISelectField label={this.fyLabel} mandatory= {true} multiple={true} disabled={false} displayKey={"name"}
                                        value={this.state.fyIndices}
                                        items={parseFinancialYearResponse(this.fyRes)}
                                        onItemsSelected={this.processSelectOnKPISelectField}
                                    />
                                </Col>
                            </Row>
                        </CardText>
                    </Grid>
                </Card>

                <div style={{"textAlign": "center"}}>
                    <br/>
                    <RaisedButton label="View" style={style} primary={true} type="button" onClick={this.processOnClickViewButton} disabled={this.state.disableViewButton} />
                </div>

            </div>
        )
    }

    /**
     * render
     * display charts for the selected values
     */
    renderChart = () => {
        if (!this.state.showChartView) {
            return (<div></div>)
        }
        return(
            <BarChartCard data={this.chartRes} />
        )
    }
}