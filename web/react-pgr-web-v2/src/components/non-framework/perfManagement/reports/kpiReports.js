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

const kpiTypeEnums = [
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
            departments: [],
            departmentKPIs: [],
            kpiTypes: [],
            ULBs: [],
            financialYears: [],
            
            selectedDepartmentIndex: 0,
            selectedKPITypeIndex: 0,
            selectedKPIIndices: [0],
            selectedULBIndices: [],
            selectedfinancialYearIndices: [],

            compareSearchRes: {},
            showChart: false,

            multiSelectKPIs: true,
            multiSelectULBs: true,
            multiSelectFYs: true
        };
        this.onSelectDepartment     = this.onSelectDepartment.bind(this)
        this.onSelectKPIType        = this.onSelectKPIType.bind(this)
        this.onSelectKPI            = this.onSelectKPI.bind(this)
        this.onSelectULB            = this.onSelectULB.bind(this)
        this.onSelectFinancialYear  = this.onSelectFinancialYear.bind(this)
        this.onClickedViewReport    = this.onClickedViewReport.bind(this)
    }

    /**
     * render the view.
     */
    render() {
        return (
            <div>
                <Card className="uiCard">
                    <Grid fluid>
                        <CardText>
                            <Row>
                                <Col xs={12} sm={4} md={4} >
                                    {EGSelectField("Department", true, false, false, this.state.selectedDepartmentIndex, this.parseDepartmentResponse(), "name", this.onSelectDepartment)}
                                </Col>
                                <Col  xs={12} sm={4} md={4}>
                                    {EGSelectField("KPI Type", true, false, false, this.state.selectedKPITypeIndex, this.state.kpiTypes, "name", this.onSelectKPIType)}
                                </Col>
                                <Col  xs={12} sm={4} md={4}>
                                    {EGSelectField("KPIs", true, this.state.multiSelectKPIs, this.state.disableKPISelection, this.state.selectedKPIIndices, 
                                                    this.parseDepartmentKPIsAsPerKPIType(this.state.selectedKPITypeIndex), "name", this.onSelectKPI)}
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
                                    {EGSelectField("ULBs", true, this.state.multiSelectULBs, this.state.disableULBSelection, 
                                                    this.state.selectedULBIndices, this.parseULBResponse(), "name", this.onSelectULB)}
                                </Col>
                                <Col  xs={12} sm={6} md={6}>
                                    {EGSelectField("Financial Year", true, this.state.multiSelectFYs, this.state.disableFinancialYearSelection, 
                                                    this.state.selectedfinancialYearIndices, this.parseFinancialYearResponse(), "name", this.onSelectFinancialYear)}
                                </Col>
                            </Row>
                        </CardText>
                    </Grid>
                </Card>

                <div style={{"textAlign": "center"}}>
                    <br/>
                    <RaisedButton label="View" style={style} primary={true} type="button" onClick={this.onClickedViewReport} />
                </div>

                {
                    this.state.showChart ? 
                    <Card className="uiCard">
                        <EGBarChart data = {this.state.compareSearchRes} />
                    </Card> : <div></div>
                }
            </div>
        )
    }
    
    onSelectDepartment(event, index, value) {
        this.showProgressIndicator(true)
        this.fetchDepartmentKPIs(this.parseDepartmentResponse()[index]['id'], (err, res) => {
            if (err) {
                this.showProgressIndicator(false)
                this.showAPIError(err)
            } else {
                this.showProgressIndicator(false)
                this.setState({
                    selectedDepartmentIndex: index,
                    departmentKPIs: res
                })
                if ((this.state.selectedKPITypeIndex !== undefined) && this.parseDepartmentKPIsAsPerKPIType(this.state.selectedKPITypeIndex).length > 0) {
                    this.setState({
                        disableULBSelection: false
                    })
                }
            }
        })
    }

    onSelectKPIType(event, index, value) {
        this.setState({
            selectedKPITypeIndex: index
        })
    }
    onSelectKPI(event, index, values) {
        if (values.length > 1) {
            if (this.state.selectedULBIndices.length > 1 || this.state.selectedfinancialYearIndices.length > 1) {
                this.showQueryError('You have already selected multiple ULBs or Financial Years values')
            } else {
                this.setState({
                    selectedKPIIndices: values
                })
            }
        } else {
            this.setState({
                selectedKPIIndices: values
            })
        }
    }
    onSelectULB(event, index, values) {
        if (values.length > 1) {
            if (this.state.selectedKPIIndices.length > 1 || this.state.selectedfinancialYearIndices.length > 1) {
                this.showQueryError('You have already selected multiple KPIs or Financial Years values')
            } else {
                this.setState({
                    selectedULBIndices: values
                })
            }
        } else {
            this.setState({
                selectedULBIndices: values
            })
        }
    }

    onSelectFinancialYear(event, index, values) {
        if (values.length > 1) {
            if (this.state.selectedKPIIndices.length > 1 || this.state.selectedULBIndices.length > 1) {
                this.showQueryError('You have already selected multiple KPIs or ULBs values')
            } else {
                this.setState({
                    selectedfinancialYearIndices: values
                })
            }
        } else {
            this.setState({
                selectedfinancialYearIndices: values
            })
        }
    }

    onClickedViewReport() {
        let finYears    = this.state.selectedfinancialYearIndices.map((item, index) => jp.query(this.state.financialYears, `$.financialYears[${item}].finYearRange`)).join(',')
        let ulbs        = this.state.selectedULBIndices.map((item, index) => jp.query(this.state.ULBs, `$.MdmsRes.tenant.tenants[${item}].code`)).join(',')
        let kpis        = this.state.selectedKPIIndices.map((item, index)=> jp.query(this.state.departmentKPIs, `$.KPIs[${item}].code`)).join(',')
        
        finYears        = '2017-18'
        ulbs            = 'default,mh.roha,panavel'
        kpis            = 'PFP'
        this.fetchCompareSearch(finYears, kpis, ulbs, (err, res) => {
            if (err || !res) {
                this.showAPIError(err)
            } else {
                this.setState({
                    compareSearchRes: res,
                    showChart: true
                })
            }
        })
    }

    /**
     * popup api error
     */
    showAPIError(err) {
        console.log(err)
    }

    showQueryError(msg) {
        console.log(msg)
    }

    /**
     * show/hide progress indication
     */
    showProgressIndicator(status) {
        (status) ? console.log(`loading in progress`) : console.log(`loading completed`)
    }

    /**
     * fetch data for screen
     */
    componentDidMount() {

        let rspDepartments      = {}
        let rspDepartmentKPIs   = {}
        let rspFinancialYears   = {}
        let rspULBs             = {}
        
        this.showProgressIndicator(true)
        this.fetchDepartment((err, res) => {
            if (err || !res) {
                this.showAPIError(err)
                this.showProgressIndicator(false)
            } else {
                rspDepartments  = res
                this.fetchDepartmentKPIs(res.MdmsRes['common-masters'].Department[0]['id'], (err, res) => {
                    if (err || !res) {
                        this.showAPIError(err)
                        this.showProgressIndicator(false)
                    } else {
                        rspDepartmentKPIs   = res
                        this.fetchFinancialYears((err, res) => {
                            if (err || !res) {
                                this.showAPIError(err)
                                this.showProgressIndicator(false)
                            } else {
                                rspFinancialYears   = res
                                this.fetchULBs((err, res) => {
                                    if (err || !res) {
                                        this.showAPIError(err)
                                        this.showProgressIndicator(false)
                                    } else {
                                        rspULBs   = res
                                        this.showProgressIndicator(false)

                                        /**
                                         * feed data to screen so that it can render
                                         */
                                        this.setState({
                                            departments: rspDepartments,
                                            kpiTypes: kpiTypeEnums,
                                            departmentKPIs: rspDepartmentKPIs,
                                            ULBs: rspULBs,
                                            financialYears: rspFinancialYears
                                        })

                                        if ((this.state.selectedKPITypeIndex !== undefined) && this.parseDepartmentKPIsAsPerKPIType(this.state.selectedKPITypeIndex).length > 0) {
                                            this.setState({
                                                disableULBSelection: false
                                            })
                                        }
                                    }
                                })
                            }
                        })
                    }
                })
            }
        })
    }

    /**
     * API required here. 
     */
    fetchDepartment(cb) {
        Api.commonApiPost("egov-mdms-service/v1/_get?moduleName=common-masters&masterName=Department", [], {}, false, true).then(function(res) {
            if (res && res.MdmsRes && res.MdmsRes['common-masters'] && res.MdmsRes['common-masters'].Department && res.MdmsRes['common-masters'].Department[0]) {
                cb (null, res)
            } else {
                cb (null, null)
            }
        }, function(err) {
            cb (err, null)
        });
    }
    fetchDepartmentKPIs(departmentId, cb) {
        Api.commonApiPost(`perfmanagement/v1/kpimaster/_search?departmentId=${departmentId}`, [], {}, false, true).then(function(res) {
            if (res && res.KPIs) {
                cb (null, res)
            } else {
                cb (null, null)
            }
        }, function(err) {
            cb (err, null)
        });
    }
    fetchULBs(cb) {
        Api.commonApiPost("egov-mdms-service/v1/_get?masterName=tenants&moduleName=tenant", [], {}, false, false).then(function(res) {
            if (res.MdmsRes && res.MdmsRes['tenant'] && res.MdmsRes['tenant'].tenants) {
                cb (null, res)
            } else {
                cb (null, null)
            }
        }, function(err) {
            cb (err, null)
        });
    }
    fetchFinancialYears(cb) {
        Api.commonApiPost("egf-master/financialyears/_search", [], {}, false, false).then(function(res) {
            if (res.financialYears) {
                cb (null, res)
            } else {
                cb (null, null)
            }
        }, function(err) {
            cb (err, null)
        });
    }
    fetchCompareSearch(finYears, kpis, ulbs, cb) {
        Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?finYear=2017-18,2018-19&ulbs=default&kpiCodes=PFP`, [], {}, false, true).then(function(res) {
        // Api.commonApiPost(`perfmanagement/v1/kpivalue/_comparesearch?finYear=${finYears}&kpiCodes=${kpis}&ulbs=${ulbs}`, [], {}, false, true).then(function(res) {
            if (res && res.ulbs) {
                cb (null, res)
            } else {
                cb (null, null)
            }
        }, function(err) {
            cb (err, null)
        });
    }

    /**
     * functions to format api response that will be used during rendering
     */
    parseDepartmentResponse() {
        return jp.query(this.state.departments, '$.MdmsRes["common-masters"].Department[*]').map((department, index) => {
            return {
                code: department.code,
                name: department.name,
                id: department.id
            }
        });
    }

    parseULBResponse() {
        return jp.query(this.state.ULBs, '$.MdmsRes["tenant"].tenants[*]').map((tenant, index) => {
            return {
                code: tenant.code,
                name: tenant.name
            }
        });
    }

    parseFinancialYearResponse() {
        return jp.query(this.state.financialYears, '$.financialYears[*]').map((finYear, index) => {
            return {
                code: finYear.finYearRange,
                name: finYear.finYearRange
            }
        });
    }

    parseDepartmentKPIsAsPerKPIType(type) {
        return jp.query(this.state.departmentKPIs, `$.KPIs[?(@.targetType==\"${kpiTypeEnums[type].name}\")]`).map((kpi, index) => {
            return {
                code: kpi.code,
                name: kpi.name,
                type: kpi.targetType
            }
        });
    }
}

export default KPIReports;