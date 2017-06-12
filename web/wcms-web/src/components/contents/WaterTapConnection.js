import React, {Component} from 'react';
import {connect} from 'react-redux';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import Checkbox from 'material-ui/Checkbox';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import RaisedButton from 'material-ui/RaisedButton';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton'
// import DataTable from '../common/Table';

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

class usageType extends Component {
  constructor(props) {
       super(props);
       this.state = {
         searchBtnText : 'Search'
       }
      //  this.search=this.search.bind(this);
   }

  componentWillMount()
  {
    //call boundary service fetch wards,location,zone data
  }

  componentDidMount()
  {
    let {initForm} = this.props;
    initForm();


  }

  // componentWillUnmount(){
  //    $('#propertyTaxTable')
  //    .DataTable()
  //    .destroy(true);
  // }
  //


  // search(e)
  // {
  //     let {showTable,changeButtonText}=this.props;
  //     e.preventDefault();
  //     console.log("Show Table");
  //     flag=1;
  //     changeButtonText("Search Again");
  //     // this.setState({searchBtnText:'Search Again'})
  //     showTable(true);
  // }

  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#propertyTaxTable').dataTable().fnDestroy();
    }
  }

  // componentDidUpdate(prevProps, prevState) {
  //     if (true) {
  //         $('#propertyTaxTable').DataTable({
  //           dom: 'lBfrtip',
  //           buttons: [
  //                     'excel', 'pdf', 'print'
  //            ],
  //            ordering: false,
  //            bDestroy: true,
  //
  //         });
  //     }
  // }

  render() {
    let {
      usageType,
      fieldErrors,
      isFormValid,
      handleChange,
      buttonText
    } = this.props;
    let {search} = this;
    console.log(usageType);
        return (
      <div className="usageType">
        <form >
          <Card>
            <CardHeader title={< strong style = {{color:"#5a3e1b"}} > New Water Tap Connection < /strong>}/>

            <CardText>
              <Card>
                <CardText>
                  <Grid>
                    <Row>

                    <Col xs={12} md={6}>
                        <TextField errorText={fieldErrors.aadharNo
                          ? fieldErrors.aadharNo
                          : ""} value={usageType.aadharNo?usageType.aadharNo:""} onChange={(e) => handleChange(e, "aadharNo", false, /^\d{3}$/g)} hintText="3 " floatingLabelText="Connection Time " />
                      </Col>

                    <Col xs={12} md={6}>
                    <SelectField errorText={fieldErrors.zone
                                ? fieldErrors.zone
                                : ""} value={usageType.zone?usageType.zone:""} onChange={(event, index, value) => {
                                  var e = {
                                    target: {
                                      value: value
                                    }
                                  };
                                  handleChange(e, "zone", false, "")}} floatingLabelText="No Of Taps " >
                                <MenuItem value={1} primaryText="1"/>
                                <MenuItem value={2} primaryText="2"/>
                                <MenuItem value={3} primaryText="3"/>
                                <MenuItem value={4} primaryText="4"/>
                                <MenuItem value={5} primaryText="5"/>
                              </SelectField>
                    </Col>
                    </Row>
                    <Row>
                    <Col xs={12} md={6}>
                    <RadioButtonGroup name="shipSpeed" defaultSelected="not_light">
                          <RadioButton
                           value="light"
                           label="Meter"
                           style={styles.radioButton}
                          />
                          <RadioButton
                           value="not_light"
                           label="Non Meter"
                           style={styles.radioButton}
                          />
                          </RadioButtonGroup>

                          </Col>
                          <Col xs={12} md={6}>
                          <SelectField errorText={fieldErrors.zone
                                      ? fieldErrors.zone
                                      : ""} value={usageType.zone?usageType.zone:""} onChange={(event, index, value) => {
                                        var e = {
                                          target: {
                                            value: value
                                          }
                                        };
                                        handleChange(e, "zone", false, "")}} floatingLabelText="Water Source Type " >
                                      <MenuItem value={1} primaryText="River"/>
                                      <MenuItem value={2} primaryText="Municipal"/>
                                      <MenuItem value={3} primaryText="Sea"/>
                                      <MenuItem value={4} primaryText="Pond"/>
                                      <MenuItem value={5} primaryText="Weekly"/>
                                    </SelectField>
                          </Col>
                        </Row>
                        <Row>
                        <Col xs={12} md={6}>
                        <SelectField errorText={fieldErrors.zone
                                    ? fieldErrors.zone
                                    : ""} value={usageType.zone?usageType.zone:""} onChange={(event, index, value) => {
                                      var e = {
                                        target: {
                                          value: value
                                        }
                                      };
                                      handleChange(e, "zone", false, "")}} floatingLabelText="Property Type " >
                                    <MenuItem value={1} primaryText="Own House"/>
                                    <MenuItem value={2} primaryText="Flat"/>
                                    <MenuItem value={3} primaryText="Hostel"/>
                                    <MenuItem value={4} primaryText="P.G"/>
                                    <MenuItem value={5} primaryText="Weekly"/>
                                  </SelectField>
                        </Col>
                        <Col xs={12} md={6}>
                        <SelectField errorText={fieldErrors.zone
                                    ? fieldErrors.zone
                                    : ""} value={usageType.zone?usageType.zone:""} onChange={(event, index, value) => {
                                      var e = {
                                        target: {
                                          value: value
                                        }
                                      };
                                      handleChange(e, "zone", false, "")}} floatingLabelText="Category  " >
                                    <MenuItem value={1} primaryText="Genarel"/>
                                    <MenuItem value={2} primaryText="BPL"/>
                                    <MenuItem value={3} primaryText="APl"/>
                                    <MenuItem value={4} primaryText="P.G"/>
                                    <MenuItem value={5} primaryText="Weekly"/>
                                  </SelectField>
                        </Col>
                        </Row>
                        <Row>
                        <Col xs={12} md={6}>
                        <SelectField errorText={fieldErrors.zone
                                    ? fieldErrors.zone
                                    : ""} value={usageType.zone?usageType.zone:""} onChange={(event, index, value) => {
                                      var e = {
                                        target: {
                                          value: value
                                        }
                                      };
                                      handleChange(e, "zone", false, "")}} floatingLabelText="Usage Type  " >
                                    <MenuItem value={1} primaryText="Genarel"/>
                                    <MenuItem value={2} primaryText="BPL"/>
                                    <MenuItem value={3} primaryText="APl"/>
                                    <MenuItem value={4} primaryText="P.G"/>
                                    <MenuItem value={5} primaryText="Weekly"/>
                                  </SelectField>
                        </Col>

                        <Col xs={12} md={6}>
                        <SelectField errorText={fieldErrors.zone
                                    ? fieldErrors.zone
                                    : ""} value={usageType.zone?usageType.zone:""} onChange={(event, index, value) => {
                                      var e = {
                                        target: {
                                          value: value
                                        }
                                      };
                                      handleChange(e, "zone", false, "")}} floatingLabelText="Connection Pipe Size  " >
                                    <MenuItem value={1} primaryText="Genarel"/>
                                    <MenuItem value={2} primaryText="BPL"/>
                                    <MenuItem value={3} primaryText="APl"/>
                                    <MenuItem value={4} primaryText="P.G"/>
                                    <MenuItem value={5} primaryText="Weekly"/>
                                  </SelectField>
                        </Col>
                        </Row>
                        </Grid>
                  </CardText>
              </Card>

              <Card>
              <CardHeader title={< strong style = {{color:"#5a3e1b"}} > If Appartment < /strong>} actAsExpander={true} showExpandableButton={true}/>

                  <CardText expandable={true}>
                    <Grid>
                      <Row>
                      <Col xs={12} md={6}>
                          <TextField errorText={fieldErrors.aadharNo
                            ? fieldErrors.aadharNo
                            : ""} value={usageType.aadharNo?usageType.aadharNo:""} onChange={(e) => handleChange(e, "aadharNo", false, /^\d{3}$/g)} hintText="3 " floatingLabelText="Residential Flat " />
                        </Col>
                        <Col xs={12} md={6}>
                            <TextField errorText={fieldErrors.aadharNo
                              ? fieldErrors.aadharNo
                              : ""} value={usageType.aadharNo?usageType.aadharNo:""} onChange={(e) => handleChange(e, "aadharNo", false, /^\d{3}$/g)} hintText="3 " floatingLabelText="Non Residential Flat " />
                          </Col>


                      </Row>
                      <Row>
                      <Col xs={12} md={6}>
                          <TextField errorText={fieldErrors.aadharNo
                            ? fieldErrors.aadharNo
                            : ""} value={usageType.aadharNo?usageType.aadharNo:""} onChange={(e) => handleChange(e, "aadharNo", false, /^\d{3}$/g)} hintText="3 " floatingLabelText="Sump Capacity " />
                        </Col>

                      </Row>
                      </Grid>
                      </CardText>
                      </Card>




              <div style={{
                float: "center"
              }}>
              <RaisedButton type="submit" label="Add" backgroundColor={brown500} labelColor={white}/>
              <RaisedButton label="Close"/>
              </div>
            </CardText>
          </Card>





        </form>

      </div>
    );
  }
}

const mapStateToProps = state => ({usageType: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,buttonText:state.form.buttonText});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["ownerName" ]
        },

      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  },
  // handleChangeNextOne: (e, property, propertyOne, isRequired, pattern) => {
  //   dispatch({
  //     type: "HANDLE_CHANGE_NEXT_ONE",
  //     property,
  //     propertyOne,
  //     value: e.target.value,
  //     isRequired,
  //     pattern
  //   })
  // },
  // handleChangeNextTwo: (e, property, propertyOne, propertyTwo, isRequired, pattern) => {
  //   dispatch({
  //     type: "HANDLE_CHANGE_NEXT_ONE",
  //     property,
  //     propertyOne,
  //     propertyTwo,
  //     value: e.target.value,
  //     isRequired,
  //     pattern
  //   })
  // },
  showTable:(state)=>
  {
    dispatch({type:"SHOW_TABLE",state});
  },
  changeButtonText:(text)=>
  {
    dispatch({type:"BUTTON_TEXT",text});
  }


});

export default connect(mapStateToProps, mapDispatchToProps)(usageType);
