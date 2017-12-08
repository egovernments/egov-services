import React, { Component } from 'react';
import Snackbar from 'material-ui/Snackbar';
import {Card, CardText, CardHeader} from 'material-ui/Card';
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
const kpiTypes = [
    {
        'name': 'VALUE',
        'code': 'VALUE'
    },
    {
        'name': 'OBJECTIVE',
        'code': 'OBJECTIVE'
    },
    {
        'name': 'TEXT',
        'code': 'TEXT'
    }
]

export default class Dashboard extends Component {
    constructor(props) {
        super(props)
        this.state = {
            apiLoading: false,
            departments: [],
            showDepartmentView: true,
            showKPIQueryView: false,
            showChartView: false,
            showToast: false,

            disableViewButton: false,
            kpiTypeIndex: 0,
            kpiIndices: [0],
            ulbIndices: [0],
            fyIndices: [0],

            toastMsg: ''
        }
        this.kpiTypeLabel   = "KPI Type"
        this.kpiLabel       = "KPIs";
        this.ulbLabel       = "ULB(s)";
        this.fyLabel        = "Financial Years";
        this.ulbRes         = null;
        this.fyRes          = null;
        this.kpiRes         = null;
        this.chartRes       = null;
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
                {
                    this.renderToast()
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

        this.setState({
            showToast: true,
            toastMsg: msg
        })
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

                                let departmentKPIs = parseDepartmentKPIsAsPerKPIType(this.kpiRes, kpiTypes[this.state.kpiTypeIndex].name)
                                if (departmentKPIs.length === 0) {
                                    this.setState({
                                        disableViewButton: true
                                    })
                                }
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
        if (label === this.kpiTypeLabel) {
            this.setState({
                kpiTypeIndex: index
            })
            let departmentKPIs = parseDepartmentKPIsAsPerKPIType(this.kpiRes, kpiTypes[this.state.kpiTypeIndex].name)
            if (departmentKPIs.length === 0) {
                this.setState({
                    disableViewButton: true
                })
            }
        }

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

    getDepartmentLogo = (department) => {
        if (department === 'ADMINISTRATION') {
            return process.env.PUBLIC_URL + './temp/images/pms/Administration.png'
        }
        if (department === 'ACCOUNTS') {
            return process.env.PUBLIC_URL + './temp/images/pms/accounts.png'
        }
        if (department === 'ENGINEERING') {
            return process.env.PUBLIC_URL + './temp/images/pms/Engineering.png'
        }
        if (department === 'TOWN PLANNING') {
            return process.env.PUBLIC_URL + './temp/images/pms/TownPlanning.png'
        }
        if (department === 'REVENUE') {
            return process.env.PUBLIC_URL + './temp/images/pms/Revenue.png'
        }
        if (department === 'PUBLIC HEALTH AND SANITATION') {
            return process.env.PUBLIC_URL + './temp/images/pms/PublicHealthSanitation.png'
        }
        if (department === 'URBAN POVERTY ALLEVIATION') {
            return process.env.PUBLIC_URL + './temp/images/pms/UrbanPovertyAlleviation.png'
        }
        if (department === 'EDUCATION') {
            return process.env.PUBLIC_URL + './temp/images/pms/Education.png'
        }

        return process.env.PUBLIC_URL + './temp/images/pms/kpi-default.png'
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
                departments.map((item, index) => <DashboardCard key={index} index={index} onClick={this.processOnClickOnCard} name={item.name} logo={this.getDepartmentLogo(item.name)} />)
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
        let departmentKPIs = parseDepartmentKPIsAsPerKPIType(this.kpiRes, kpiTypes[this.state.kpiTypeIndex].name)

        return (
            <div>
                <br />
                <RaisedButton label="Back" style={{'margin': '15px'}} primary={true} type="button" onClick={this.processOnClickBackButton} 
                                icon={<i className="material-icons" style={{color:"white"}}>arrow_back</i>} 
                />
                <br />
                <br />
                <Card className="uiCard">
                    <CardHeader title={<strong style={{fontSize:"18px"}}>{"Key Performance Indicator Dashboard"}</strong>}/>
                    <Grid fluid>
                            <Row>
                                <Col xs={12} sm={3} md={3} >
                                    <KPISelectField label={this.kpiTypeLabel} mandatory= {true} multiple={false} disabled={false} displayKey={"name"}
                                        value={this.state.kpiTypeIndex}
                                        items={kpiTypes}
                                        onItemsSelected={this.processSelectOnKPISelectField}
                                    />
                                </Col>
                                <Col xs={12} sm={3} md={3} >
                                    <KPISelectField label={this.kpiLabel} mandatory= {true} multiple={true} disabled={false} displayKey={"name"}
                                        value={this.state.kpiIndices}
                                        items={departmentKPIs}
                                        onItemsSelected={this.processSelectOnKPISelectField}
                                    />
                                </Col>
                                <Col xs={12} sm={3} md={3} >
                                    <KPISelectField label={this.ulbLabel} mandatory= {true} multiple={true} disabled={false} displayKey={"name"}
                                        value={this.state.ulbIndices}
                                        items={parseULBResponse(this.ulbRes)}
                                        onItemsSelected={this.processSelectOnKPISelectField}
                                    />
                                </Col>
                                <Col xs={12} sm={3} md={3} >
                                    <KPISelectField label={this.fyLabel} mandatory= {true} multiple={true} disabled={false} displayKey={"name"}
                                        value={this.state.fyIndices}
                                        items={parseFinancialYearResponse(this.fyRes)}
                                        onItemsSelected={this.processSelectOnKPISelectField}
                                    />
                                </Col>
                            </Row>
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
        let finYears    = this.state.fyIndices.map((item, index) => jp.query(this.fyRes, `$.financialYears[${item}].finYearRange`)).join(',')
        let ulbs        = this.state.ulbIndices.map((item, index) => jp.query(this.ulbRes, `$.MdmsRes.tenant.tenants[${item}].name`)).join(',')
        let kpis        = this.state.kpiIndices.map((item, index)=> jp.query(this.kpiRes, `$.KPIs[${item}].name`)).join(',')

        return(
            <BarChartCard data={this.chartRes} 
                    finYears={finYears}
                    ulbs={ulbs}
                    kpis={kpis}
            />
        )
    }

    /**
     * render
     * display Snackbar to inform user
     */
    renderToast = () => {
        if (!this.state.showToast) {
            return (
                <div></div>
            )
        }
        return (
            <Snackbar open={this.state.showToast} message={this.state.toastMsg} autoHideDuration={3000} onRequestClose={()=>{
                this.setState({
                    showToast: false
                })
            }}
          />
        )
    }
}