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
import RefreshIndicator from 'material-ui/RefreshIndicator';
import Api from '../../../../api/api'
var jp = require('jsonpath');

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

const kpiTypes = [
    {
        name: "VALUE",
        code: "VALUE"
    },
    {
        name: "TEXT",
        code: "TEXT"
    },
    {
        name: "OBJECTIVE",
        code: "OBJECTIVE"
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
            valueKPIs: [],
            textKPIs:[],
            objeciveKPIs: [],
            departments: [],
            kpiTypes: kpiTypes,
            kpis: [],
            ULBs: [],
            financialYears: [],
            startViewReport: false,
            isAPIInProgress: false,
            
            departmentIndexValue: 0,
            kpiTypeIndexValue: 0,
            kpiIndexValue: 0,
            ULBIndexValue: 0,
            financialYearIndexValue: 0
        };
        this.onSelectDepartment     = this.onSelectDepartment.bind(this)
        this.onSelectKPIType        = this.onSelectKPIType.bind(this)
        this.onSelectKPI            = this.onSelectKPI.bind(this)
        this.onSelectULB            = this.onSelectULB.bind(this)
        this.onSelectFinancialYear  = this.onSelectFinancialYear.bind(this)
        this.onClickedViewReport    = this.onClickedViewReport.bind(this)
    }
    
    onSelectDepartment(event, index, value) {
        let self = this;
        self.setState({
            departmentIndexValue: value
        })

        Api.commonApiPost("perfmanagement/v1/kpimaster/_search?departmentId=" + this.state.departments[index]['id'], [], {}, false, true).then(function(res) {
            console.log(res)
            self.setState({
                valueKPIs:jp.query(res, '$.KPIs[?(@.targetType=="VALUE")]'),
                textKPIs: jp.query(res, '$.KPIs[?(@.targetType=="TEXT")]'),
                objeciveKPIs: jp.query(res, '$.KPIs[?(@.targetType=="OBJECTIVE")]')
            })
        }, function(err) {
        });
    }
    onSelectKPIType(event, index, value) {
        this.setState({
            kpiTypeIndexValue: value
        })
        let kpis = []
        if (kpiTypes[index]['name'] == 'VALUE') {
            kpis = this.state.valueKPIs
        }
        if (kpiTypes[index]['name'] == 'TEXT') {
            kpis =  this.state.textKPIs
        }
        if (kpiTypes[index]['name'] == 'OBJECTIVE') {
            kpis =  this.state.objeciveKPIs
        }
        this.setState({
            kpis:kpis
        })
    }
    onSelectKPI(event, index, value) {
        this.setState({
            kpiIndexValue: value
        })
    }
    onSelectULB(event, index, value) {
        this.setState({
            ULBIndexValue: value
        })
    }
    onSelectFinancialYear(event, index, value) {
        this.setState({
            financialYearIndexValue: value
        })
    }
    onClickedViewReport() {
        this.setState({startViewReport: true})
    }

    componentWillMount() {
        let self = this;
        Api.commonApiPost("egov-mdms-service/v1/_get?moduleName=common-masters&masterName=Department", [], {}, false, false).then(function(res) {
            if (res.MdmsRes && res.MdmsRes['common-masters'].Department) {
                self.setState({departments:res.MdmsRes['common-masters'].Department});
            }
        }, function(err) {
        });

        Api.commonApiPost("egov-mdms-service/v1/_get?masterName=tenants&moduleName=tenant", [], {}, false, false).then(function(res) {
            if (res.MdmsRes && res.MdmsRes.tenant && res.MdmsRes.tenant.tenants) {
                self.setState({ULBs:res.MdmsRes['tenant'].tenants});
            }
        }, function(err) {
        });
   
        Api.commonApiPost("egf-master/financialyears/_search", [], {}, false, false).then(function(res) {
            if (res) {
                self.setState({financialYears:res.financialYears});
            }
        }, function(err) {
        });
    }

    render() {
        return (
            <div>
                <Card className="uiCard">
                    <Grid fluid>
                        <CardText>
                            <Row>
                                <Col xs={12} sm={4} md={4} >
                                    {EGSelectField("Department", true, this.state.departmentIndexValue, this.state.departments, "name", this.onSelectDepartment)}
                                </Col>
                                <Col  xs={12} sm={4} md={4}>
                                    {EGSelectField("KPI Type", true, this.state.kpiTypeIndexValue, this.state.kpiTypes, "name", this.onSelectKPIType)}
                                </Col>
                                <Col  xs={12} sm={4} md={4}>
                                    {EGSelectField("KPIs", true, this.state.kpiIndexValue, this.state.kpis, "name", this.onSelectKPI)}
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
                                    {EGSelectField("ULBs", true, this.state.ULBIndexValue, this.state.ULBs, "name", this.onSelectULB)}
                                </Col>
                                <Col  xs={12} sm={6} md={6}>
                                    {EGSelectField("Financial Year", true, this.state.financialYearIndexValue, this.state.financialYears, "finYearRange", this.onSelectFinancialYear)}
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

                <div>
                    <RefreshIndicator
                        size={40}
                        left={10}
                        top={0}
                        loadingColor="#FF9800"
                        status={this.state.isAPIInProgress ? "loading" : "hide"}
                        style={style.refresh}
                    />
                </div>
            </div>
        )
    }
}

export default KPIReports;