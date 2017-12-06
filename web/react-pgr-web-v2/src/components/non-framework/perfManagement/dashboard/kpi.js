import React, { Component } from 'react';
import {Card, CardText, CardMedia, CardHeader, CardTitle} from 'material-ui/Card';
import {Grid, Row, Col} from 'react-bootstrap';
import RaisedButton from 'material-ui/RaisedButton';
import UIButton from '../../../framework/components/UiButton';
import UIBackButton from '../../../framework/components/UiBackButton';

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

export default class Dashboard extends Component {
    constructor(props) {
        super(props)
        this.state = {
            apiLoading: false,
            departments: [],
            showDepartmentView: true,
            showKPIQueryView: false
        }

        this.kpiLabel   = "KPIs";
        this.ulbLabel   = "ULB(s)";
        this.fyLabel    = "Financial Years";
        this.ulbRes     = null;
        this.fyRes      = null;
        this.kpiRes     = null;
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

    processSelectOnKPISelectField = (index, value, label) => {
        console.log(`processSelectOnKPISelectField ${index} ${value} ${label}`)
    }

    processOnClickViewButton = () => {

    }

    processOnClickBackButton = () => {
        this.setState({
            showKPIQueryView: false,
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
                departments.map((item, index) => <DashboardCard index={index} onClick={this.processOnClickOnCard} name={item.name} logo={require('../../../../images/headerLogo.png')} />)
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
                                    <KPISelectField label={this.kpiLabel} mandatory= {true} multiple={true} disabled={false} value={0} displayKey={"name"}
                                        items={parseDepartmentKPIsAsPerKPIType(this.state.departments, 'VALUE')}
                                        onItemsSelected={this.processSelectOnKPISelectField}
                                    />
                                </Col>
                                <Col xs={12} sm={4} md={4} >
                                    <KPISelectField label={this.ulbLabel} mandatory= {true} multiple={true} disabled={false} value={0} displayKey={"name"}
                                        items={parseULBResponse(this.ulbRes)}
                                        onItemsSelected={this.processSelectOnKPISelectField}
                                    />
                                </Col>
                                <Col xs={12} sm={4} md={4} >
                                    <KPISelectField label={this.fyLabel} mandatory= {true} multiple={true} disabled={false} value={0} displayKey={"name"}
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
                    <RaisedButton label="VIEW" style={style} primary={true} type="button" onClick={this.processOnClickViewButton} />
                </div>

            </div>
        )
    }
}