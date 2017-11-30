import React, {Component} from 'react';
import {Card, CardText} from 'material-ui/Card';
import {Grid, Row, Col} from 'react-bootstrap';
import RaisedButton from 'material-ui/RaisedButton';
import {EGSelectField} from '../hoc/kpiHOC'
import RefreshIndicator from 'material-ui/RefreshIndicator';
import Api from '../../../../api/api'
import EGBarChart from '../charts/kpiBarchart'

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
            kpiIndexValues: [0],
            ULBIndexValues: [],
            financialYearIndexValues: [],
            kpiReportResponse: {}
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
        if (kpiTypes[index]['name'] === 'VALUE') {
            kpis = this.state.valueKPIs
        }
        if (kpiTypes[index]['name'] === 'TEXT') {
            kpis =  this.state.textKPIs
        }
        if (kpiTypes[index]['name'] === 'OBJECTIVE') {
            kpis =  this.state.objeciveKPIs
        }
        this.setState({
            kpis:kpis
        })
    }
    onSelectKPI(event, index, values) {
        this.setState({
            kpiIndexValues: values
        })
    }
    onSelectULB(event, index, values) {
        this.setState({
            ULBIndexValues: values
        })
    }
    onSelectFinancialYear(event, index, values) {
        this.setState({
            financialYearIndexValues: values
        })
    }

    onClickedViewReport() {
        let self = this;
        console.log("Selected KPIs = %s", JSON.stringify(this.state.kpiIndexValues))
        console.log("Selected ULBs = %s", JSON.stringify(this.state.ULBIndexValues))
        console.log("Selected FY = %s", JSON.stringify(this.state.financialYearIndexValues))

        let finYears    = this.state.financialYearIndexValues.map((item, index) => self.state.financialYears[item]['finYearRange']).join(',')
        let ulbs        = this.state.ULBIndexValues.map((item, index) => self.state.ULBs[item]['code']).join(',')
        let kpis        = this.state.kpiIndexValues.map((item, index)=> self.state.kpis[item]['code']).join(',')
        let url         = `perfmanagement/v1/kpivalue/_search?departmentId=2&finYear=${finYears}&kpiCodes=${kpis}&ulbs=${ulbs}`
        console.log(`querying URL ${url}`)
        url              = "perfmanagement/v1/kpivalue/_search?departmentId=2&finYear=2017-18&kpiCodes=MKO,MKT"
        Api.commonApiPost(url, [], {}, false, true).then(function(res) {
            if (res && res.kpiValues) {
                self.setState({
                    kpiReportResponse: res,
                    startViewReport: true
                })
            }
        }, function(err) {
        });
    }

    componentWillMount() {
        let self = this;
        Api.commonApiPost("egov-mdms-service/v1/_get?moduleName=common-masters&masterName=Department", [], {}, false, false).then(function(res) {
            if (res.MdmsRes && res.MdmsRes['common-masters'].Department) {
                self.setState({departments:res.MdmsRes['common-masters'].Department});
                if (self.state.departments[0]['id']) {
                    Api.commonApiPost("perfmanagement/v1/kpimaster/_search?departmentId=" + self.state.departments[0]['id'], [], {}, false, true).then(function(res) {
                        if (res && res.KPIs) {
                            self.setState({
                                valueKPIs:jp.query(res, '$.KPIs[?(@.targetType=="VALUE")]'),
                                textKPIs: jp.query(res, '$.KPIs[?(@.targetType=="TEXT")]'),
                                objeciveKPIs: jp.query(res, '$.KPIs[?(@.targetType=="OBJECTIVE")]')
                            })
                        }
                    }, function(err) {
                    });
                }
            }
        }, function(err) {
        });

        Api.commonApiPost("egov-mdms-service/v1/_get?masterName=tenants&moduleName=tenant", [], {}, false, false).then(function(res) {
            if (res.MdmsRes && res.MdmsRes['tenant'] && res.MdmsRes['tenant'].tenants) {
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
                                    {EGSelectField("Department", true, false, this.state.departmentIndexValue, this.state.departments, "name", this.onSelectDepartment)}
                                </Col>
                                <Col  xs={12} sm={4} md={4}>
                                    {EGSelectField("KPI Type", true, false, this.state.kpiTypeIndexValue, this.state.kpiTypes, "name", this.onSelectKPIType)}
                                </Col>
                                <Col  xs={12} sm={4} md={4}>
                                    {EGSelectField("KPIs", true, true, this.state.kpiIndexValues, this.state.kpis, "name", this.onSelectKPI)}
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
                                    {EGSelectField("ULBs", true, true, this.state.ULBIndexValues, this.state.ULBs, "name", this.onSelectULB)}
                                </Col>
                                <Col  xs={12} sm={6} md={6}>
                                    {EGSelectField("Financial Year", true, true, this.state.financialYearIndexValues, this.state.financialYears, "finYearRange", this.onSelectFinancialYear)}
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
                    <EGBarChart data = {this.state.kpiReportResponse} FY={this.state.financialYearIndexValues} ULB={this.state.ULBIndexValues} KPI={this.state.kpiIndexValues} />
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