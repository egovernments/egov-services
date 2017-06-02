import React, {Component} from 'react';
import {connect} from 'react-redux';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white} from 'material-ui/styles/colors';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import Checkbox from 'material-ui/Checkbox';
import RaisedButton from 'material-ui/RaisedButton';
import DataTable from '../common/Table';

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
  },
  checkbox: {
    marginBottom: 16,
    marginTop:24
  },
};

//Create Class for Create and update property
class CruProperty extends Component {
  constructor(props) {
    super(props)
    this.state = {
        createProperty: {
            valueOwnership:1,
            valueProperyType:'',
            valueAppartmentName:'',
            valueDepartment:''
        },
        cAddressDiffPAddress: {
          checked: false
        }
    }

  }

  componentWillMount() {
    //call boundary service fetch wards,location,zone data
  }

  componentDidMount() {

  }

  componentWillUnmount() {

  }

  componentWillUpdate() {

  }

  componentDidUpdate(prevProps, prevState) {

  }

  search = (e) => {

  }

  handleCheckBoxChange = (prevState) => {
      this.setState((prevState) => {prevState.cAddressDiffPAddress.checked = !prevState.cAddressDiffPAddress.checked})
  }

  render() {

    let {
      cruProperty,
      fieldErrors,
      isFormValid,
      handleChange,
      handleChangeNextOne,
      handleChangeNextTwo
    } = this.props;

    let {search} = this;

      return(
          <div className="cruProperty">
              <form onSubmit={(e) => {search(e)}}>
                  <Card>
                      <CardHeader title={< strong style = {{color:brown500}} >Create New Property</strong>} />
                      <CardText>
                          <Card>
                              <CardText>
                                  <Grid>
                                      <Row>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Category of ownership"
                                                  errorText={fieldErrors.ownerShip ? fieldErrors.ownerShip: ""}
                                                  value={cruProperty.ownerShip ? cruProperty.ownerShip :""}
                                                  onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "ownerShip", false, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >

                                                 <MenuItem value={1} primaryText="Vacant Land" />
                                                 <MenuItem value={2} primaryText="Central Government 75%" />
                                                 <MenuItem value={3} primaryText="Private" />
                                                 <MenuItem value={4} primaryText="State Government" />
                                                 <MenuItem value={5} primaryText="Central Government 33.5%" />
                                                 <MenuItem value={6} primaryText="Central Government 50%" />
                                               </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                                {cruProperty.ownerShip === 1 &&
                                                  <SelectField
                                                  floatingLabelText="Property type"
                                                  errorText={fieldErrors.propertyType ? fieldErrors.propertyType: ""}
                                                  value={cruProperty.propertyType ? cruProperty.propertyType :""}
                                                  onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "propertyType", false, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  >
                                                    <MenuItem value={4} primaryText="Central Government Land" />
                                                    <MenuItem value={5} primaryText="State Government Land" />
                                                    <MenuItem value={6} primaryText="Private Land" />
                                              </SelectField>}
                                              {cruProperty.ownerShip === 3 &&
                                                <SelectField
                                                floatingLabelText="Property type"
                                                errorText={fieldErrors.propertyType ? fieldErrors.propertyType: ""}
                                                value={cruProperty.propertyType ? cruProperty.propertyType :""}
                                                onChange={(event, index, value) => {
                                                  var e = {
                                                    target: {
                                                      value: value
                                                    }
                                                  };
                                                  handleChange(e, "propertyType", false, "")}}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                                >
                                                  <MenuItem value={4} primaryText="Mixed" />
                                                  <MenuItem value={5} primaryText="Residential" />
                                                  <MenuItem value={6} primaryText="Non residential" />
                                            </SelectField>}
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Apartment/ Complex name"
                                                  errorText={fieldErrors.appartment ? fieldErrors.appartment: ""}
                                                  value={cruProperty.appartment ? cruProperty.appartment :""}
                                                  onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "appartment", false, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Never"/>
                                                    <MenuItem value={2} primaryText="Every Night"/>
                                                    <MenuItem value={3} primaryText="Weeknights"/>
                                                    <MenuItem value={4} primaryText="Weekends"/>
                                                    <MenuItem value={5} primaryText="Weekly"/>
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              {(cruProperty.ownerShip != 1 && cruProperty.ownerShip != 3) &&
                                                <SelectField
                                                    floatingLabelText="Department"
                                                    errorText={fieldErrors.department ? fieldErrors.department: ""}
                                                    value={cruProperty.department ? cruProperty.department :""}
                                                    onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "department", false, "")}}
                                                    floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                    underlineStyle={styles.underlineStyle}
                                                    underlineFocusStyle={styles.underlineFocusStyle}
                                                >
                                                </SelectField>

                                              }
                                          </Col>
                                      </Row>
                                  </Grid>
                              </CardText>
                          </Card>
                      </CardText>
                  </Card>
                  <Card>
                      <CardHeader title={<strong style={{color:brown500}}>Owner Details</strong>} />
                      <CardText>
                          <Card>
                              <CardText>
                              <Grid>
                                <Row>
                                    <Col xs={12} md={6}>
                                      <TextField
                                          hintText="eg- 434345456545"
                                          floatingLabelText="Aadhar No"
                                          errorText={fieldErrors.adharNo ? fieldErrors.adharNo: ""}
                                          value={cruProperty.adharNo ? cruProperty.adharNo:""}
                                          onChange={(e) => handleChange(e, "adharNo", true, /^\d{12}$/g)}
                                          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                          underlineStyle={styles.underlineStyle}
                                          underlineFocusStyle={styles.underlineFocusStyle}
                                        />
                                    </Col>
                                    <Col xs={12} md={6}>
                                      <TextField
                                          hintText="eg- 9999888877"
                                          floatingLabelText="Mobile No"
                                          errorText={fieldErrors.mobileNo ? fieldErrors.mobileNo: ""}
                                          value={cruProperty.mobileNo ? cruProperty.mobileNo:""}
                                          onChange={(e) => handleChange(e, "mobileNo", true, /^\d{12}$/g)}
                                          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                          underlineStyle={styles.underlineStyle}
                                          underlineFocusStyle={styles.underlineFocusStyle}
                                        />
                                    </Col>
                                    <Col xs={12} md={6}>
                                      <TextField
                                          hintText="eg- Joe Doe"
                                          floatingLabelText="Owner Name"
                                          errorText={fieldErrors.ownerName ? fieldErrors.ownerName: ""}
                                          value={cruProperty.ownerName ? cruProperty.ownerName:""}
                                          onChange={(e) => handleChange(e, "ownerName", false, '')}
                                          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                          underlineStyle={styles.underlineStyle}
                                          underlineFocusStyle={styles.underlineFocusStyle}
                                        />
                                    </Col>
                                    <Col xs={12} md={6}>
                                        <SelectField
                                            floatingLabelText="Gender"
                                            onChange={this.handleChange}
                                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                            underlineStyle={styles.underlineStyle}
                                            underlineFocusStyle={styles.underlineFocusStyle}
                                        >
                                            <MenuItem value={1} primaryText="Male"/>
                                            <MenuItem value={2} primaryText="Female"/>
                                        </SelectField>
                                    </Col>
                                    <Col xs={12} md={6}>
                                      <TextField
                                          hintText="eg- example@example.com"
                                          floatingLabelText="Email"
                                          onChange={this.handleChange}
                                          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                          underlineStyle={styles.underlineStyle}
                                          underlineFocusStyle={styles.underlineFocusStyle}
                                        />
                                    </Col>
                                    <Col xs={12} md={6}>
                                        <SelectField
                                            hintText="eg- Father"
                                            floatingLabelText="Guardian Relation"
                                            onChange={this.handleChange}
                                            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                            underlineStyle={styles.underlineStyle}
                                            underlineFocusStyle={styles.underlineFocusStyle}
                                        >
                                            <MenuItem value={1} primaryText="Father"/>
                                            <MenuItem value={2} primaryText="Husband"/>
                                            <MenuItem value={3} primaryText="Mother"/>
                                            <MenuItem value={4} primaryText="Others"/>
                                        </SelectField>
                                    </Col>
                                    <Col xs={12} md={6}>
                                      <TextField
                                          hintText="eg- Guardian name"
                                          floatingLabelText="Guardian"
                                          onChange={this.handleChange}
                                          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                          underlineStyle={styles.underlineStyle}
                                          underlineFocusStyle={styles.underlineFocusStyle}
                                        />
                                    </Col>
                                </Row>
                              </Grid>
                              </CardText>
                          </Card>
                          <div>
                            <RaisedButton type="submit" label="Add" backgroundColor={brown500} labelColor={white}/>
                            <RaisedButton type="button" label="Delete" backgroundColor={brown500} labelColor={white} />
                          </div>
                      </CardText>
                  </Card>
                  <Card>
                      <CardHeader title={<strong style={{color: brown500}}>Property Address</strong>} />
                      <CardText>
                          <Card>
                              <CardText>
                                  <Grid>
                                      <Row>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Locality"
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >

                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Zone No."
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >

                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Ward No."
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >

                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Block No."
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >

                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Street"
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >

                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Election ward"
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >

                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                  floatingLabelText="Door No."
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                  floatingLabelText="Pin"
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <Col xs={12} md={12}>
                                              <Checkbox
                                                label="Is correspondence address different from property address?"
                                                style={styles.checkbox}
                                                defaultChecked ={this.state.cAddressDiffPAddress.checked}
                                                onCheck = {this.handleCheckBoxChange}
                                              />
                                          </Col>
                                          {this.state.cAddressDiffPAddress.checked &&
                                            <div className="addMoreAddress">
                                                <Col xs={12} md={6}>
                                                    <TextField
                                                        floatingLabelText="Address 1"
                                                        onChange={this.handleChange}
                                                        multiLine={true}
                                                         rows={2}
                                                         rowsMax={4}
                                                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                        underlineStyle={styles.underlineStyle}
                                                        underlineFocusStyle={styles.underlineFocusStyle}
                                                    />
                                                </Col>
                                                <Col xs={12} md={6}>
                                                    <TextField
                                                        floatingLabelText="Address 1"
                                                        onChange={this.handleChange}
                                                        multiLine={true}
                                                         rows={2}
                                                         rowsMax={4}
                                                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                        underlineStyle={styles.underlineStyle}
                                                        underlineFocusStyle={styles.underlineFocusStyle}
                                                    />
                                                </Col>
                                                <Col xs={12} md={6}>
                                                    <TextField
                                                        floatingLabelText="Pin"
                                                        onChange={this.handleChange}
                                                        floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                        underlineStyle={styles.underlineStyle}
                                                        underlineFocusStyle={styles.underlineFocusStyle}
                                                    />
                                                </Col>
                                            </div>
                                          }
                                      </Row>
                                  </Grid>
                              </CardText>
                          </Card>
                      </CardText>
                  </Card>
                  <Card>
                      <CardHeader title={<strong style={{color:brown500}}>Assessment details</strong>} />
                      <CardText>
                          <Card>
                              <CardText>
                                  <Grid>
                                      <Row>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Reason for Creation"
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                  <MenuItem value={1} primaryText="New Property"/>
                                                  <MenuItem value={2} primaryText="Bifurcation"/>
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                  floatingLabelText="Parent UPIC No."
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <div className="clearfix"></div>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                  floatingLabelText="Extent of Site (Sq. Mtrs)"
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                  floatingLabelText="Occupancy Certificate Number"
                                                  onChange={this.handleChange}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                      </Row>
                                  </Grid>
                              </CardText>
                          </Card>
                      </CardText>
                  </Card>
                  <Card>
                      <CardHeader title={<strong style={{color:brown500}}>Amenities</strong>} />
                      <CardText>
                          <Card>
                              <CardText>
                                  <Grid>
                                      <Row>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="Lift"
                                                style={styles.checkbox}
                                                onCheck = {this.handleCheckBoxChange}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="Toilet"
                                                style={styles.checkbox}
                                                onCheck = {this.handleCheckBoxChange}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="water tap"
                                                style={styles.checkbox}
                                                onCheck = {this.handleCheckBoxChange}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="electricity"
                                                style={styles.checkbox}
                                                onCheck = {this.handleCheckBoxChange}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="Attached bathroom"
                                                style={styles.checkbox}
                                                onCheck = {this.handleCheckBoxChange}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="Cable connection"
                                                style={styles.checkbox}
                                                onCheck = {this.handleCheckBoxChange}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="Water harvesting"
                                                style={styles.checkbox}
                                                onCheck = {this.handleCheckBoxChange}
                                              />
                                          </Col>
                                      </Row>
                                  </Grid>
                              </CardText>
                          </Card>
                      </CardText>
                  </Card>
                  <Card>
                      <CardHeader title={<strong style={{color:brown500}}>Construction Types</strong>} />
                      <CardText>
                          <Card>
                              <CardText>
                                  <Grid>
                                      <Row>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Floor Type"
                                                value={this.state.value}
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Options" />
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Roof Type"
                                                value={this.state.value}
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Options" />
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Wall Type"
                                                value={this.state.value}
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Options" />
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Wood Type"
                                                value={this.state.value}
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Options" />
                                              </SelectField>
                                          </Col>
                                      </Row>
                                  </Grid>
                              </CardText>
                          </Card>
                      </CardText>
                  </Card>
                  <Card>
                      <CardHeader title={<strong style={{color:brown500}}>Floor Details</strong>} />
                      <CardText>
                          <Card>
                              <CardText>
                                  <Grid>
                                      <Row>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Floor Number"
                                                value={this.state.value}
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Ground floor" />
                                                    <MenuItem value={2} primaryText=" Basement-2" />
                                                    <MenuItem value={3} primaryText=" Basement-1" />
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Classification of Building"
                                                value={this.state.value}
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Country Tiles" />
                                                    <MenuItem value={2} primaryText="Huts" />
                                                    <MenuItem value={3} primaryText="Madras Terras" />
                                                    <MenuItem value={4} primaryText="Mangalore Tiles" />
                                                    <MenuItem value={5} primaryText="R C C Ordinary Building" />
                                                    <MenuItem value={6} primaryText="R C C Posh Building" />
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Nature of Usage"
                                                value={this.state.value}
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Cinema Halls" />
                                                    <MenuItem value={2} primaryText="Commercial" />
                                                    <MenuItem value={3} primaryText="Community Halls And Auditoriams Others" />
                                                    <MenuItem value={4} primaryText="Dourmetries, Tiffin Centres, Mess Etc" />
                                                    <MenuItem value={5} primaryText="Education Institutes" />
                                                    <MenuItem value={6} primaryText="Godowns And Business" />
                                                    <MenuItem value={7} primaryText="Hospitals And Nursing Homes" />
                                                    <MenuItem value={8} primaryText="Hotels Lodges And Restaurants" />
                                                    <MenuItem value={9} primaryText="Industries" />
                                                    <MenuItem value={10} primaryText="Institute" />
                                                    <MenuItem value={11} primaryText="Offices And Banks" />
                                                    <MenuItem value={12} primaryText="Private Schools - 1to 5 Classes" />
                                                    <MenuItem value={13} primaryText="Residence" />
                                                    <MenuItem value={14} primaryText="Shops" />
                                                    <MenuItem value={15} primaryText="Shops 100mt Above" />
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                floatingLabelText="Firm Name"
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <div className="clearfix"></div>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Occupancy"
                                                value={this.state.value}
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Owner" />
                                                    <MenuItem value={2} primaryText="Tenant" />
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                floatingLabelText="Occupant Name"
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <div className="clearfix"></div>
                                          <Col xs={12} md={6}>
                                              <DatePicker
                                                floatingLabelText="Construction Date"
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <DatePicker
                                                floatingLabelText="Effective From Date"
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Unstructured land"
                                                value={this.state.value}
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                    <MenuItem value={1} primaryText="Yes" />
                                                    <MenuItem value={2} primaryText="No" />
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                floatingLabelText="Length"
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <div className="clearfix"></div>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                floatingLabelText="Breadth"
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                floatingLabelText="Plinth Area"
                                                onChange={this.handleChange}
                                                floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                underlineStyle={styles.underlineStyle}
                                                underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                      </Row>
                                  </Grid>
                              </CardText>
                          </Card>
                          <div>
                            <RaisedButton type="submit" label="Add" backgroundColor={brown500} labelColor={white}/>
                            <RaisedButton type="button" label="Delete" backgroundColor={brown500} labelColor={white} />
                          </div>
                      </CardText>
                  </Card>
              </form>
          </div>
      )
  }


}

const mapStateToProps = state => ({
  cruProperty:state.form.form,
  fieldErrors: state.form.fieldErrors
});

const mapDispatchToProps = dispatch => ({
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(CruProperty);
