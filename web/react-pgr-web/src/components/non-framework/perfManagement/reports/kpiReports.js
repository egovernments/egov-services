import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Card, CardActions, CardHeader, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import RaisedButton from 'material-ui/RaisedButton';
import DropDownMenu from 'material-ui/DropDownMenu';
import MenuItem from 'material-ui/MenuItem';
import SelectField from 'material-ui/SelectField';
import {EGSelectField} from '../hoc/kpiHOC'
import {Bar} from 'react-chartjs-2';

const style = {
    margin: 12
};

const departmentList = [
    {
        name: "Department-1",
        code: "Department-1"
    },
    {
        name: "Department-2",
        code: "Department-2"
    },
    {
        name: "Department-3",
        code: "Department-3"
    }
]
const data = {
    labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
    datasets: [
      {
        label: 'My First dataset',
        backgroundColor: 'rgba(255,99,132,0.2)',
        borderColor: 'rgba(255,99,132,1)',
        borderWidth: 1,
        hoverBackgroundColor: 'rgba(255,99,132,0.4)',
        hoverBorderColor: 'rgba(255,99,132,1)',
        data: [65, 59, 80, 81, 56, 55, 40]
      }
    ]
  };


class KPIReports extends Component {
    constructor(props) {
        super(props);
        this.state = { 
            departments: [],
            kpiTypes: [],
            kpis: [],
            ULBs: [],
            financialYears: [],
            startViewReport: false
        };
        this.onSelectDepartment     = this.onSelectDepartment.bind(this)
        this.onSelectKPIType        = this.onSelectKPIType.bind(this)
        this.onSelectKPI            = this.onSelectKPI.bind(this)
        this.onSelectULB            = this.onSelectULB.bind(this)
        this.onSelectFinancialYear  = this.onSelectFinancialYear.bind(this)
        this.onClickedViewReport    = this.onClickedViewReport.bind(this)
    }
    
    onSelectDepartment(event, index, value) {
    }
    onSelectKPIType(event, index, value) {
    }
    onSelectKPI(event, index, value) {
    }
    onSelectULB(event, index, value) {
    }
    onSelectFinancialYear(event, index, value) {
    }
    onClickedViewReport() {
        console.log("generate report")
        this.setState({startViewReport: true})
    }

    render() {
        return (
            <div>
                <Card className="uiCard">
                    <Grid fluid>
                        <CardText>
                            <Row>
                                <Col xs={12} sm={4} md={4} >
                                    {EGSelectField("Department", true, 2, this.state.departments, this.onSelectDepartment)}
                                </Col>
                                <Col  xs={12} sm={4} md={4}>
                                    {EGSelectField("KPI Type", true, 2, this.state.kpiTypes, this.onSelectKPIType)}
                                </Col>
                                <Col  xs={12} sm={4} md={4}>
                                    {EGSelectField("KPIs", true, 2, this.state.kpis, this.onSelectKPI)}
                                </Col>
                            </Row>
                        </CardText>
                    </Grid>
                </Card>
                <Card className="uiCard">
                    <Grid fluid>
                        <CardText>
                            <Row>
                                <Col xs={12} sm={6} md={6} >
                                    {EGSelectField("ULBs", true, 2, this.state.ULBs, this.onSelectULB)}
                                </Col>
                                <Col  xs={12} sm={6} md={6}>
                                    {EGSelectField("Financial Year", true, 2, this.state.financialYears, this.onSelectFinancialYear)}
                                </Col>
                            </Row>
                        </CardText>
                    </Grid>
                </Card>
                <div style={{"textAlign": "center"}}>
                    <br/>
                    <RaisedButton label="View" style={style} primary={true} type="button" onClick={this.onClickedViewReport} />
                </div>
                { this.state.startViewReport ? <Card className="uiCard">
                    <Bar data={data} />
                    </Card> : <div></div>
                }
            </div>
        )
    }
}

export default KPIReports;