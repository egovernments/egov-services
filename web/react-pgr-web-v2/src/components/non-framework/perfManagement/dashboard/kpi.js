import React, { Component } from 'react';
// import {Card, CardText, CardMedia, CardHeader, CardTitle} from 'material-ui/Card';
// import {Grid, Row, Col} from 'react-bootstrap';
// import RaisedButton from 'material-ui/RaisedButton';
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
import DashboardCard from './dashboardcard'


export default class Dashboard extends Component {
    constructor(props) {
        super(props)
        this.state = {
            apiLoading: false,
            departments: []
        }
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

    }

    busyUI = (status) => {
        this.setState({
            apiLoading:status
        })
    }

    processOnClickOnCard = (e) => {
        console.log(`processOnClickOnCard`)
        console.log(e)
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
     * present card as per department
     */
    renderDepartments = () => {
        let departments = parseDepartmentResponse(this.state.departments)
        if (departments.length > 0) {
            return (
                departments.map((item, index) => <DashboardCard key={index} onClick={this.processOnClickOnCard} name={item.name} logo={require('../../../../images/headerLogo.png')} />)
            )
        }
        return (
            <div></div>
        )
    }
}