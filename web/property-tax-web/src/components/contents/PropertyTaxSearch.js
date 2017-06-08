import React, {Component} from 'react';
import {connect} from 'react-redux';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import DataTable from '../common/Table';
import Api from '../../api/pTAPIS';

const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');


const buttons = require('datatables.net-buttons-bs');

require('datatables.net-buttons/js/buttons.colVis.js'); // Column visibility
require('datatables.net-buttons/js/buttons.html5.js'); // HTML 5 file export
require('datatables.net-buttons/js/buttons.flash.js'); // Flash file export
require('datatables.net-buttons/js/buttons.print.js'); // Print view button

var flag = 0;
const styles = {
  errorStyle: {
    color: red500
  },
  underlineStyle: {
    borderColor: brown500
  },
  underlineFocusStyle: {
    borderColor: brown500
  },
  floatingLabelStyle: {
    color: brown500
  },
  floatingLabelFocusStyle: {
    color: brown500
  },
  customWidth: {
    width:100
  }
};

class PropertyTaxSearch extends Component {
  constructor(props) {
       super(props);
       this.state = {
         searchBtnText : 'Search'
       }
       this.search=this.search.bind(this);
   }

  componentWillMount()
  {

    //call boundary service fetch wards,location,zone data
  }

  componentDidMount()
  {
    let {initForm} = this.props;
    initForm();
    let {toggleDailogAndSetText}=this.props;
    // let response=Api.commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "ADMINISTRATION" }).then(function(response)
    // {
    //
    // },function(err) {
    //     toggleDailogAndSetText(true,err)
    // });

  }

  componentWillUnmount(){
     $('#propertyTaxTable')
     .DataTable()
     .destroy(true);
  }



  search(e)
  {
      let {showTable,changeButtonText}=this.props;
      e.preventDefault();
      console.log("Show Table");
      flag=1;
      changeButtonText("Search Again");
      // this.setState({searchBtnText:'Search Again'})
      showTable(true);
  }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#propertyTaxTable').dataTable().fnDestroy();
    }
  }

  componentDidUpdate(prevProps, prevState) {
      if (true) {
          $('#propertyTaxTable').DataTable({
            dom: 'lBfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true,

          });
      }
  }

  render() {
    let {
      propertyTaxSearch,
      fieldErrors,
      isFormValid,
      isTableShow,
      handleChange,
      handleChangeNextOne,
      handleChangeNextTwo,
      buttonText
    } = this.props;
    let {search} = this;
    console.log(propertyTaxSearch);
    console.log(isTableShow);
    const viewTabel=()=>
    {
      return (
        <Card>
          <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Result < /strong>}/>
          <CardText>
        <Table id="propertyTaxTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
          <thead style={{backgroundColor:"#f2851f",color:"white"}}>
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
                <RaisedButton type="submit" disabled={!isFormValid} label={buttonText} backgroundColor={"#5a3e1b"} labelColor={white}/>
                <RaisedButton label="Close"/>
              </div>
            </CardText>
          </Card>


                  {isTableShow?viewTabel():""}



        </form>

      </div>
    );
  }
}

const mapStateToProps = state => ({propertyTaxSearch: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: [ ]
        },
        pattern: {
          current: [],
          required: ["assessmentNo", "oldAssessmentNo", "mobileNo", "aadharNo", "doorNo"]
        }
      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  },
  handleChangeNextOne: (e, property, propertyOne, isRequired, pattern) => {
    dispatch({
      type: "HANDLE_CHANGE_NEXT_ONE",
      property,
      propertyOne,
      value: e.target.value,
      isRequired,
      pattern
    })
  },
  handleChangeNextTwo: (e, property, propertyOne, propertyTwo, isRequired, pattern) => {
    dispatch({
      type: "HANDLE_CHANGE_NEXT_ONE",
      property,
      propertyOne,
      propertyTwo,
      value: e.target.value,
      isRequired,
      pattern
    })
  },
  showTable:(state)=>
  {
    dispatch({type:"SHOW_TABLE",state});
  },
  changeButtonText:(text)=>
  {
    dispatch({type:"BUTTON_TEXT",text});
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(PropertyTaxSearch);
