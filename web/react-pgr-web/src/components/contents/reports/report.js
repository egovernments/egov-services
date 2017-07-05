import React, {Component} from 'react';
import {connect} from 'react-redux';

import Api from '../../../api/api';
import SearchForm from './searchForm';
import ReportResult from './reportResult';

var metaData=
{
    "requestInfo": null,
    "tenantId": null,
    "reportDetails": {
        "reportName": "GrievanceByType",
        "summary": "Grievance Type Wise Report",
        "reportHeader": [
            {
                "name": "name",
                "label": "reports.pgr.complainttype.name",
                "type": "string"
            },
            {
                "name": "registered",
                "label": "reports.pgr.seva.status.registered",
                "type": "string"
            },
            {
                "name": "inprocess",
                "label": "reports.pgr.seva.status.inprocess",
                "type": "string"
            },
            {
                "name": "completed",
                "label": "reports.pgr.seva.status.completed",
                "type": "string"
            },
            {
                "name": "rejected",
                "label": "reports.pgr.seva.status.rejected",
                "type": "string"
            },
            {
                "name": "reopened",
                "label": "reports.pgr.seva.status.reopened",
                "type": "string"
            },
            {
                "name": "withinSLA",
                "label": "reports.pgr.withinsla",
                "type": "string"
            },
            {
                "name": "outsideSLA",
                "label": "reports.pgr.ousidesla",
                "type": "string"
            },
            {
                "name": "total",
                "label": "reports.pgr.total",
                "type": "string"
            }
        ],
        "searchParams": [
            {
                "name": "fromDate",
                "label": "reports.pgr.datefrom",
                "type": "date",
                "isMandatory": true
            },
            {
                "name": "toDate",
                "label": "reports.pgr.dateto",
                "type": "date",
                "isMandatory": true
            },
            {
                "name": "complainttype",
                "label": "reports.pgr.complainttype",
                "type": "singlevaluelist",
                "pattern": "/pgr-master/service/v1/_search?tenantId=default|$.Service[*].serviceCode|$.Service[*].serviceName",
                "isMandatory": true
            }
        ],
        "query": ""
    },
    "ResponseInfo": null
}

class Report extends Component {

  componentWillMount()
  {
    let {setMetaData}=this.props;

    //Call api
    setMetaData(metaData);
    // let response=Api.commonApiPost("egf-masters", "functions", "_search").then(function(response)
    // {
    //   console.log(response)
    // },function(err) {
    //     console.log(err);
    // });
    //call boundary service fetch wards,location,zone data
  }


  render() {
    const viewTabel=()=>
    {
      return (
<<<<<<< 1f1486cd1d79c5fe1e143b14180c563a8019b8ed
          <ReportResult />
        )
    }
    return (
      <div className="Report">
        <SearchForm />
=======
        <Card>
          <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Result < /strong>}/>
          <CardText>
        <Table id="propertyTaxTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
         <thead>
            <tr>
              <th>#</th>
              <th>Assessment Number</th>
              <th>Owner Name</th>
              <th>Address</th>
              <th>Current Demand</th>
              <th>Arrears Demand</th>
              <th>Property usage</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>1</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-1" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>Create</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>

            <tr>
              <td>3</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
                <DropdownButton title="Action" id="dropdown-3" pullRight>
                    <MenuItem>Create</MenuItem>
                    <MenuItem>Update</MenuItem>
                </DropdownButton>
              </td>
            </tr>
          </tbody>
        </Table>
      </CardText>
      </Card>
      )
    }
    return (
      <div className="PropertyTaxSearch">
        <form onSubmit={(e) => {
          search(e)
        }}>
          <Card>
            <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Search Property < /strong>}/>

            <CardText>
              <Card>
                <CardText>
                  <Grid>
                    <Row>
                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.doorNo
                          ? fieldErrors.doorNo
                          : ""} id="doorNo" value={propertyTaxSearch.doorNo?propertyTaxSearch.doorNo:""} onChange={(e) => handleChange(e, "doorNo", false, /^([\d,/.\-]){6,10}$/g)} hintText="eg:-3233312323" floatingLabelText="Door number" />
                      </Col>

                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.assessmentNo
                          ? fieldErrors.assessmentNo
                          : ""} value={propertyTaxSearch.assessmentNo?propertyTaxSearch.assessmentNo:""} onChange={(e) => handleChange(e, "assessmentNo", false, /^\d{3,15}$/g)} hintText="eg:-123456789123456" floatingLabelText="Assessment number"/>
                      </Col>
                    </Row>

                    <Row>
                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.mobileNo
                          ? fieldErrors.mobileNo
                          : ""} value={propertyTaxSearch.mobileNo?propertyTaxSearch.mobileNo:""} onChange={(e) => handleChange(e, "mobileNo", false, /^\d{10}$/g)} hintText="Mobile number" floatingLabelText="Mobile number" />
                      </Col>

                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.aadharNo
                          ? fieldErrors.aadharNo
                          : ""} value={propertyTaxSearch.aadharNo?propertyTaxSearch.aadharNo:""} onChange={(e) => handleChange(e, "aadharNo", false, /^\d{12}$/g)} hintText="Aadhar number " floatingLabelText="Aadhar number " />
                      </Col>
                    </Row>
                  </Grid>

                </CardText>
              </Card>

              <Card>
                <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Advance Search < /strong>} actAsExpander={true} showExpandableButton={true}/>

                <CardText expandable={true}>
                  <Grid>
                    <Row>
                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.ownerName
                          ? fieldErrors.ownerName
                          : ""} value={propertyTaxSearch.ownerName?propertyTaxSearch.ownerName:""} onChange={(e) => handleChange(e, "ownerName", false, "")} hintText="Owner Name" floatingLabelText="Owner Name" />
                      </Col>

                      <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.oldAssessmentNo
                          ? fieldErrors.oldAssessmentNo
                          : ""} value={propertyTaxSearch.oldAssessmentNo?propertyTaxSearch.oldAssessmentNo:""} onChange={(e) => handleChange(e, "oldAssessmentNo", false, /^\d{3,15}$/g)} hintText="Old Assessment Number" floatingLabelText="Old Assessment Number" />
                      </Col>
                    </Row>

                    <Row>

                      <Card>
                        <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Boundary < /strong>}/>

                        <CardText>
                          <Grid>
                            <Row>
                              <Col xs={12} md={6}>

                                <SelectField errorText={fieldErrors.zone
                                  ? fieldErrors.zone
                                  : ""} value={propertyTaxSearch.zone?propertyTaxSearch.zone:""} onChange={(event, index, value) => {
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "zone", false, "")}} floatingLabelText="Zone	Drop " >
                                  <MenuItem value={1} primaryText="Never"/>
                                  <MenuItem value={2} primaryText="Every Night"/>
                                  <MenuItem value={3} primaryText="Weeknights"/>
                                  <MenuItem value={4} primaryText="Weekends"/>
                                  <MenuItem value={5} primaryText="Weekly"/>
                                </SelectField>

                              </Col>

                              <Col xs={12} md={6}>
                                <SelectField errorText={fieldErrors.ward
                                  ? fieldErrors.ward
                                  : ""} value={propertyTaxSearch.ward?propertyTaxSearch.ward:""} onChange={(event, index, value) =>{
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "ward", false, "")}
                                  } floatingLabelText="Ward" >
                                  <MenuItem value={1} primaryText="Never"/>
                                  <MenuItem value={2} primaryText="Every Night"/>
                                  <MenuItem value={3} primaryText="Weeknights"/>
                                  <MenuItem value={4} primaryText="Weekends"/>
                                  <MenuItem value={5} primaryText="Weekly"/>
                                </SelectField>
                              </Col>
                            </Row>

                            <Row>
                              <Col xs={12} md={6}>
                                <SelectField errorText={fieldErrors.location
                                  ? fieldErrors.location
                                  : ""} value={propertyTaxSearch.location?propertyTaxSearch.location:""} onChange={(event, index, value) => {
                                    var e = {
                                      target: {
                                        value: value
                                      }
                                    };
                                    handleChange(e, "location", false, "")}} floatingLabelText="Location" >
                                  <MenuItem value={1} primaryText="Never"/>
                                  <MenuItem value={2} primaryText="Every Night"/>
                                  <MenuItem value={3} primaryText="Weeknights"/>
                                  <MenuItem value={4} primaryText="Weekends"/>
                                  <MenuItem value={5} primaryText="Weekly"/>
                                </SelectField>
                              </Col>

                            </Row>
                          </Grid>

                        </CardText>
                      </Card>

                    </Row>

                    <Row>
                      <Card>
                        <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Search Property by Demand < /strong>}/>
                        <CardText>
                          <Grid>
                            <Row>
                              <Col xs={12} md={6}>
                              <TextField errorText={fieldErrors.demandFrom
                                ? fieldErrors.demandFrom
                                : ""} value={propertyTaxSearch.demandFrom?propertyTaxSearch.demandFrom:""} onChange={(e) => handleChange(e, "demandFrom", false, /^\d$/g)} hintText="Demand From" floatingLabelText="Demand From" />


                              </Col>

                              <Col xs={12} md={6}>
                              <TextField errorText={fieldErrors.demandTo
                                ? fieldErrors.demandTo
                                : ""} value={propertyTaxSearch.demandTo?propertyTaxSearch.demandTo:""} onChange={(e) => handleChange(e, "demandTo", false, /^\d$/g)} hintText="Demand To" floatingLabelText="Demand To" />


                              </Col>
                            </Row>
                          </Grid>
                        </CardText>
                      </Card>
                    </Row>

                  </Grid>
                </CardText>
              </Card>
              <div style={{
                float: "center"
              }}>
                <RaisedButton type="submit" disabled={!isFormValid} label={buttonText} labelColor={white}/>
                <RaisedButton label="Close"/>
              </div>
            </CardText>
          </Card>


                  {isTableShow?viewTabel():""}



        </form>


        <ReportResult />
      </div>
    );
  }
}

const mapStateToProps = state => ({});

const mapDispatchToProps = dispatch => ({
  setMetaData:(metaData)=>{
    dispatch({type:"SET_META_DATA",metaData})
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(Report);
