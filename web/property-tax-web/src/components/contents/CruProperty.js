import React, {Component} from 'react';
import {connect} from 'react-redux';

import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import FontIcon from 'material-ui/FontIcon';
import {RadioButton, RadioButtonGroup} from 'material-ui/RadioButton';
import Upload from 'material-ui-upload/Upload';
import FlatButton from 'material-ui/FlatButton';
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
  uploadButton: {
   verticalAlign: 'middle',
 },
 uploadInput: {
   cursor: 'pointer',
   position: 'absolute',
   top: 0,
   bottom: 0,
   right: 0,
   left: 0,
   width: '100%',
   opacity: 0,
 },
 floatButtonMargin: {
   marginLeft: 20,
   fontSize:12
 },
 iconFont: {
   fontSize:17
 },
 radioButton: {
    marginBottom: 16,
  },
actionWidth: {
  width:160
}
};

//Create Class for Create and update property
class CruProperty extends Component {
  constructor(props) {
    super(props)
    this.state = {
      addOwner: false,
      addFloor: false
    }
  }

  componentWillMount() {
    //call boundary service fetch wards,location,zone data
  }

  componentDidMount() {
      let {initForm}=this.props;
      initForm();
  }

  componentWillUnmount() {

  }

  onFileLoad = (e) => console.log(e.target.result)

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
      owners,
      cruProperty,
      fieldErrors,
      isFormValid,
      handleChange,
      handleChangeNextOne,
      handleChangeNextTwo,
      deleteObject,
      editObject,
      editIndex,
      isEditIndex
    } = this.props;

    let {search} = this;

    console.log(cruProperty);

    const ownerForm = () => (
      <Row>
      <Col xs={12} md={6}>
      <TextField
          hintText="eg- 434345456545"
          floatingLabelText="Aadhar No"
          errorText={fieldErrors.ownerDetail ? fieldErrors.ownerDetail.adharNo: ""}
          value={cruProperty.ownerDetail ? cruProperty.ownerDetail.adharNo:""}
          onChange={(e) => handleChangeNextOne(e,"ownerDetail", "adharNo", true, /^\d{12}$/g)}
          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
          underlineStyle={styles.underlineStyle}
          underlineFocusStyle={styles.underlineFocusStyle}
        />
    </Col>
    <Col xs={12} md={6}>
      <TextField
          hintText="eg- 9999888877"
          floatingLabelText="Mobile No"
          errorText={fieldErrors.ownerDetail ? fieldErrors.ownerDetail.mobileNo: ""}
          value={cruProperty.ownerDetail ? cruProperty.ownerDetail.mobileNo:""}
          onChange={(e) => handleChangeNextOne(e, "ownerDetail","mobileNo", true, /^\d{10}$/g)}
          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
          underlineStyle={styles.underlineStyle}
          underlineFocusStyle={styles.underlineFocusStyle}
        />
    </Col>
    <Col xs={12} md={6}>
      <TextField
          hintText="eg- Joe Doe"
          floatingLabelText="Owner Name"
          errorText={fieldErrors.ownerDetail ? fieldErrors.ownerDetail.ownerName: ""}
          value={cruProperty.ownerDetail ? cruProperty.ownerDetail.ownerName:""}
          onChange={(e) => handleChangeNextOne(e,"ownerDetail" ,"ownerName", true, "")}
          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
          underlineStyle={styles.underlineStyle}
          underlineFocusStyle={styles.underlineFocusStyle}
        />
    </Col>
    <Col xs={12} md={6}>
        <SelectField
            floatingLabelText="Gender"
            errorText={fieldErrors.ownerDetail ? fieldErrors.ownerDetail.gender: ""}
            value={cruProperty.ownerDetail ? cruProperty.ownerDetail.gender:""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e, "ownerDetail" ,"gender", true, "")}
            }
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
        >
            <MenuItem value={1} primaryText="Male"/>
            <MenuItem value={2} primaryText="Female"/>
            <MenuItem value={3} primaryText="Others"/>
        </SelectField>
    </Col>
    <Col xs={12} md={6}>
      <TextField
          hintText="eg- example@example.com"
          floatingLabelText="Email"
          errorText={fieldErrors.ownerDetail ? fieldErrors.ownerDetail.email: ""}
          value={cruProperty.ownerDetail ? cruProperty.ownerDetail.email:""}
          onChange={(e) => handleChangeNextOne(e, "ownerDetail", "email", false, /^\w+@[a-zA-Z_]+?\.[a-zA-Z]{2,3}$/)}
          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
          underlineStyle={styles.underlineStyle}
          underlineFocusStyle={styles.underlineFocusStyle}
        />
    </Col>
    <Col xs={12} md={6}>
        <SelectField
            hintText="eg- Father"
            floatingLabelText="Guardian Relation"
            errorText={fieldErrors.ownerDetail ? fieldErrors.ownerDetail.gaurdianRelation: ""}
            value={cruProperty.ownerDetail ? cruProperty.ownerDetail.gaurdianRelation:""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e, "ownerDetail", "gaurdianRelation", true, "")}
            }
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
          errorText={fieldErrors.ownerDetail ? fieldErrors.ownerDetail.gaurdian: ""}
          value={cruProperty.ownerDetail ? cruProperty.ownerDetail.gaurdian:""}
          onChange={(e) => handleChangeNextOne(e,  "ownerDetail",  "gaurdian", false, "")}
          floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
          underlineStyle={styles.underlineStyle}
          underlineFocusStyle={styles.underlineFocusStyle}
        />
    </Col>

    <Col xs={12} md={6}>
      <br/><br/>
      <RadioButtonGroup name="ownerDetails" defaultSelected="not_light">
       <RadioButton
          value={cruProperty.ownerDetail ? cruProperty.ownerDetail.primaryOwner:"primaryOwner"}
          onChange={(e, v) =>{
            var e = {
              target: {
                value: v
              }
            }
               handleChangeNextOne(e,"ownerDetail", "primaryOwner", true,'')
          }}
         label="Primary owner"
         style={styles.radioButton}
       />
       <RadioButton
          value={cruProperty.ownerDetail ? cruProperty.ownerDetail.secondaryOwner:"secondaryOwner"}
          onChange={(e, v) =>{
            var e = {
              target: {
                value: v
              }
            }
               handleChangeNextOne(e,"ownerDetail", "secondaryOwner", true,'')
          }}
         label="Secondary owner"
         style={styles.radioButton}
       />
     </RadioButtonGroup>
    </Col>

    <Col xs={12} md={6}>
        <SelectField
            floatingLabelText="Owner type"
            errorText={fieldErrors.ownerDetail ? fieldErrors.ownerDetail.ownerType: ""}
            value={cruProperty.ownerDetail ? cruProperty.ownerDetail.ownerType:""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e, "ownerDetail" ,"ownerType", false, "")}
            }
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
        >
            <MenuItem value={1} primaryText="Ex-Service man"/>
            <MenuItem value={2} primaryText="Freedom Fighter"/>
            <MenuItem value={3} primaryText="Freedom figher's wife"/>
        </SelectField>
    </Col>

    <Col xs={12} md={6}>
        <TextField
            hintText="eg- 100"
            floatingLabelText="Percentage of Ownership"
            errorText={fieldErrors.ownerDetail ? fieldErrors.ownerDetail.percentageOwnership: ""}
            value={cruProperty.ownerDetail ? cruProperty.ownerDetail.percentageOwnership:""}
            onChange={(e) => handleChangeNextOne(e,"ownerDetail", "percentageOwnership", false, /^\d{3}$/g)}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
    </Col>

    <Col xs={12}>
    <br/>
    { (editIndex == -1) &&
      <RaisedButton type="button" label="Add"  backgroundColor={brown500} labelColor={white} onClick={()=> {
            this.props.addNestedFormData("owners","ownerDetail");
            this.props.resetObject("ownerDetail");
          }
      }/>
    }
      { (editIndex != -1) &&
        <RaisedButton type="button" label="Save"  backgroundColor={brown500} labelColor={white} onClick={()=> {
              this.props.updateObject("owners","ownerDetail",  editIndex);
              this.props.resetObject("ownerDetail");
              isEditIndex(-1);
            }
        }/>
      }


    </Col>
    </Row>)


    const floorForm = () => (
      <Row>
      <Col xs={12} md={6}>
          <SelectField
            floatingLabelText="Floor Number"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.floorNo : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.floorNo : ""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e, "floorDetail","floorNo", true, "")}
            }
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
          <TextField
            floatingLabelText="Unit Number"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.unitNumber : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.unitNumber : ""}
            onChange={(e) => {handleChangeNextOne(e,"floorDetail" ,"unitNumber", true, /^\d{3}$/g)}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <div className="clearfix"></div>
      <Col xs={12} md={6}>
          <SelectField
            floatingLabelText="Room Type"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.roomType : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.roomType : ""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e,"floorDetail" ,"roomType", true, "")}
            }
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          >
                <MenuItem value={1} primaryText="Options" />
          </SelectField>
      </Col>
      <Col xs={12} md={6}>
          <SelectField
            floatingLabelText="Construction type"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.constructionType : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.constructionType : ""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e,"floorDetail" ,"constructionType", true, "")}
            }
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          >
                <MenuItem value={1} primaryText="Country Tiles" />
                <MenuItem value={1} primaryText="Huts" />
                <MenuItem value={1} primaryText="Madras Terras" />
                <MenuItem value={1} primaryText="Mangalore Tiles" />
                <MenuItem value={1} primaryText="R C C Ordinary Building" />
                <MenuItem value={1} primaryText="R C C Posh Building" />
          </SelectField>
      </Col>
      <Col xs={12} md={6}>
          <SelectField
            floatingLabelText="Usage type"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.usageType : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.usageType : ""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e,"floorDetail" ,"usageType", true, "")}
            }
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          >
                <MenuItem value={1} primaryText="Residential" />
                <MenuItem value={1} primaryText="Non-Residential" />
                <MenuItem value={1} primaryText="Residential Slum" />
                <MenuItem value={1} primaryText="Non-Residential Slum" />
                <MenuItem value={1} primaryText="Mixed" />
                <MenuItem value={1} primaryText="Religious" />
                <MenuItem value={1} primaryText="Residential Open Land" />
                <MenuItem value={1} primaryText="Non-Residential Open Land" />
                <MenuItem value={1} primaryText="Industrial" />
                <MenuItem value={1} primaryText="Others" />

          </SelectField>
      </Col>
      <Col xs={12} md={6}>
          <SelectField
            floatingLabelText="Usage sub type"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.usageSubType : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.usageSubType : ""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e,"floorDetail" ,"usageSubType", true, "")}
            }
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
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.firmName : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.firmName : ""}
            onChange={(e) => {handleChangeNextOne(e,"floorDetail" ,"firmName", false, "")}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <SelectField
            floatingLabelText="Occupancy"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.occupancy : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.occupancy : ""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e,"floorDetail" ,"occupancy", true, "")}
            }
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          >
                <MenuItem value={1} primaryText="Owner" />
                <MenuItem value={2} primaryText="Tenant" />
          </SelectField>
      </Col>
        <div className="clearfix"></div>
      <Col xs={12} md={6}>
          <TextField
            floatingLabelText="Occupant Name"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.occupantName : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.occupantName : ""}
            onChange={(e) => {handleChangeNextOne(e,"floorDetail" , "occupantName", false, "")}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <TextField
            floatingLabelText="Annual Rent"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.annualRent : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.annualRent : ""}
            onChange={(e) => {handleChangeNextOne(e,"floorDetail" , "annualRent", false, /^\d{9}$/g)}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <TextField
            floatingLabelText="Manual ARV"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.manualArv : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.manualArv : ""}
            onChange={(e) => {handleChangeNextOne(e,"floorDetail" , "manualArv", false, /^\d{9}$/g)}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <DatePicker
            floatingLabelText="Construction Date"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.constructionDate : ""}
            defaultDate={cruProperty.floorDetail ? new Date(cruProperty.floorDetail.constructionDate) : new Date()}
            onChange={(event,date) => {
                var e = {
                  target:{
                      value: date
                  }
                }
              handleChangeNextOne(e,"floorDetail" ,"constructionDate", true, "")}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <DatePicker
            floatingLabelText="Effective From Date"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.effectiveDate : ""}
            defaultDate={cruProperty.floorDetail ? new Date(cruProperty.floorDetail.effectiveDate) : new Date()}
            onChange={(event,date) => {
                var e = {
                  target:{
                      value: date
                  }
                }
                handleChangeNextOne(e,"floorDetail" ,"effectiveDate", true, "")}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <SelectField
            floatingLabelText="Unstructured land"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.unstructuredLand : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.unstructuredLand : ""}
            onChange={(event, index, value) => {
                var e = {
                  target: {
                    value: value
                  }
                };
                handleChangeNextOne(e, "floorDetail" ,"unstructuredLand", true, "")}
            }
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
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.length : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.length : ""}
            onChange={(e) => {handleChangeNextOne(e,"floorDetail" ,"length", false, "")}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <TextField
            floatingLabelText="Breadth"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.breadth : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.breadth : ""}
            onChange={(e) => {handleChangeNextOne(e,"floorDetail" ,"breadth", false, "")}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <TextField
            floatingLabelText="Plinth Area"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.plinthArea : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.plinthArea : ""}
            onChange={(e) => {handleChangeNextOne(e, "floorDetail","plinthArea", true, "")}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <TextField
            floatingLabelText="Occupancy Certificate Number"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.occupancyCertiNumber : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.occupancyCertiNumber : ""}
            onChange={(e) => {handleChangeNextOne(e,"floorDetail" ,"occupancyCertiNumber", false, /^\d[a-zA-Z0-9]{9}$/g)}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <TextField
            floatingLabelText="Building Permission number"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.buildingPermissionNo : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.buildingPermissionNo : ""}
            onChange={(e) => {handleChangeNextOne(e,"floorDetail" ,"buildingPermissionNo", false, /^\d[a-zA-Z0-9]{14}$/g)}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <DatePicker
            floatingLabelText="Building Permission Date"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.buildingPermissionDate : ""}
            defaultDate={cruProperty.floorDetail ? new Date(cruProperty.floorDetail.buildingPermissionDate) : new Date()}
            onChange={(event,date) => {
                var e = {
                  target:{
                      value: date
                  }
                }
                handleChangeNextOne(e,"floorDetail" ,"buildingPermissionDate", false, "")}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12} md={6}>
          <TextField
            floatingLabelText="Plinth area in Building plan"
            errorText={fieldErrors.floorDetail ? fieldErrors.floorDetail.plinthAreaBuildingPlan : ""}
            value={cruProperty.floorDetail ? cruProperty.floorDetail.plinthAreaBuildingPlan : ""}
            onChange={(e) => {handleChangeNextOne(e, "floorDetail","plinthAreaBuildingPlan", false,  /^\d{6}$/g)}}
            floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
            underlineStyle={styles.underlineStyle}
            underlineFocusStyle={styles.underlineFocusStyle}
          />
      </Col>
      <Col xs={12}>
          <br/>
          { (editIndex == -1) &&
            <RaisedButton type="button" label="Add"  backgroundColor={brown500} labelColor={white} onClick={()=> {
                  this.props.addNestedFormData("floorDetails","floorDetail");
                  this.props.resetObject("floorDetail");
                }
            }/>
          }
            { (editIndex != -1) &&
              <RaisedButton type="button" label="Save"  backgroundColor={brown500} labelColor={white} onClick={()=> {
                    this.props.updateObject("floorDetails","floorDetail",  editIndex);
                    this.props.resetObject("floorDetail");
                    isEditIndex(-1);
                  }
              }/>
            }

      </Col>
      </Row>
    )

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
                                <Col xs={12}>
                                  <FloatingActionButton mini={true} className="pull-right" onClick={() => {
                                        this.setState((prevState, props) => ({
                                            addOwner: !prevState.addOwner
                                          }));
                                        this.props.resetObject("ownerDetail");
                                        this.props.resetObject("floorDetail");
                                        isEditIndex(-1);
                                      }
                                  }>
                                      <i className="material-icons" style={styles.iconFont}>{ !this.state.addOwner ? "add_box": "remove_circle"}</i>
                                  </FloatingActionButton>
                                  <div className="clearfix" />
                                  <br/>

                                </Col>
                              </Row>
                                <Row>
                                    <Col xs={12} md={12}>
                                        <Table id="cruPropertyTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
                                        <thead style={{backgroundColor:"#f2851f",color:"white"}}>
                                          <tr>
                                            <th>#</th>
                                            <th>Adhar Number</th>
                                            <th>Mobile Number</th>
                                            <th>Owner Name</th>
                                            <th>Gender</th>
                                            <th>Email</th>
                                            <th>Gaurdian Relation</th>
                                            <th>Gaurdian</th>
                                            <th>Gaurdian</th>
                                            <th>Owner Type</th>
                                            <th>Percentage of Ownership</th>
                                            <th  style={styles.actionWidth}>Action</th>
                                          </tr>
                                        </thead>
                                        <tbody>


                                            {cruProperty.owners && cruProperty.owners.map(function(i, index){
                                              return (<tr key={index}>
                                                  <td>{index}</td>
                                                  <td>{i.adharNo}</td>
                                                  <td>{i.mobileNo}</td>
                                                  <td>{i.ownerName}</td>
                                                  <td>{i.gender}</td>
                                                  <td>{i.email}</td>
                                                  <td>{i.gaurdianRelation}</td>
                                                  <td>{i.gaurdian}</td>
                                                  <td>{i.gaurdian}</td>
                                                  <td>{i.ownerType}</td>
                                                  <td>{i.percentageOwnership}</td>
                                                  <td style={styles.actionWidth}>
                                                     <FloatingActionButton mini={true} style={styles.floatButtonMargin} onClick={ () => {

                                                       editObject("ownerDetail",i);
                                                       isEditIndex(index);

                                                     }}>
                                                        <i className="material-icons" style={styles.iconFont}>mode_edit</i>
                                                    </FloatingActionButton>
                                                    <FloatingActionButton mini={true} style={styles.floatButtonMargin} onClick={ () => {deleteObject("owners", index);}}>
                                                        <i className="material-icons" style={styles.iconFont}>delete</i>
                                                    </FloatingActionButton>

                                                  </td>
                                                </tr> )
                                            })}

                                        </tbody>
                                        </Table>
                                    </Col>
                                    {this.state.addOwner && ownerForm()}
                                </Row>
                              </Grid>
                              </CardText>
                          </Card>

                      </CardText>
                  </Card>
                  <Card>
                    <CardHeader title={<strong style={{color: brown500}}>Header</strong>} />
                    <CardText>
                      <Card>
                          <CardText>
                              <Grid>
                                  <Row>
                                    <Col xs={12} md={6}>
                                        <TextField
                                            floatingLabelText="Reference property number"
                                            errorText={fieldErrors.doorNo ? fieldErrors.refPropertyNumber : ""}
                                            value={cruProperty.doorNo ? cruProperty.refPropertyNumber : ""}
                                            onChange={(e) => handleChange(e, "refPropertyNumber", false, /^\d{15}$/g)}
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
                      <CardHeader title={<strong style={{color: brown500}}>Property Address</strong>} />
                      <CardText>
                          <Card>
                              <CardText>
                                  <Grid>
                                      <Row>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Locality"
                                                  errorText={fieldErrors.locality ? fieldErrors.locality: ""}
                                                  value={cruProperty.locality ? cruProperty.locality:""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "locality", true, "")}
                                                  }
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
                                              <SelectField
                                                  floatingLabelText="Appartment/Complex name"
                                                  errorText={fieldErrors.locality ? fieldErrors.appComplexName: ""}
                                                  value={cruProperty.locality ? cruProperty.appComplexName:""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "appComplexName", true, "")}
                                                  }
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
                                              <SelectField
                                                  floatingLabelText="Zone No."
                                                  errorText={fieldErrors.zoneNo ? fieldErrors.zoneNo: ""}
                                                  value={cruProperty.zoneNo ? cruProperty.zoneNo:""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "zoneNo", true, "")}
                                                  }
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
                                              <SelectField
                                                  floatingLabelText="Ward No."
                                                  errorText={fieldErrors.wardNo ? fieldErrors.wardNo : ""}
                                                  value={cruProperty.wardNo ? cruProperty.wardNo : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "wardNo", true, "")}
                                                  }
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
                                              <SelectField
                                                  floatingLabelText="Block No."
                                                  errorText={fieldErrors.blockNo ? fieldErrors.blockNo : ""}
                                                  value={cruProperty.blockNo ? cruProperty.blockNo : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "blockNo", false, "")}
                                                  }
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
                                              <SelectField
                                                  floatingLabelText="Street"
                                                  errorText={fieldErrors.street ? fieldErrors.street : ""}
                                                  value={cruProperty.street ? cruProperty.street : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "street", false, "")}
                                                  }
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
                                              <SelectField
                                                  floatingLabelText="Revenue circle"
                                                  errorText={fieldErrors.revenueCircle ? fieldErrors.revenueCircle : ""}
                                                  value={cruProperty.revenueCircle ? cruProperty.revenueCircle : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "revenueCircle", false, "")}
                                                  }
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
                                              <SelectField
                                                  floatingLabelText="Election ward"
                                                  errorText={fieldErrors.electionCard ? fieldErrors.electionCard : ""}
                                                  value={cruProperty.electionCard ? cruProperty.electionCard : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "electionCard", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >

                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                  floatingLabelText="Door No."
                                                  errorText={fieldErrors.doorNo ? fieldErrors.doorNo : ""}
                                                  value={cruProperty.doorNo ? cruProperty.doorNo : ""}
                                                  onChange={(e) => handleChange(e, "doorNo", true, '')}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                  floatingLabelText="Pin"
                                                  errorText={fieldErrors.pin ? fieldErrors.pin : ""}
                                                  value={cruProperty.pin ? cruProperty.pin : ""}
                                                  onChange={(e) => handleChange(e, "pin", false, '')}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <Col xs={12} md={12}>
                                              <Checkbox
                                                label="Is correspondence address different from property address?"
                                                style={styles.checkbox}
                                                defaultChecked ={cruProperty.cAddressDiffPAddress}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "cAddressDiffPAddress", false, '')
                                                }}

                                              />
                                          </Col>
                                          {cruProperty.cAddressDiffPAddress &&
                                            <div className="addMoreAddress">
                                                <Col xs={12} md={6}>
                                                    <TextField
                                                        floatingLabelText="Door No."
                                                        errorText={fieldErrors.cDoorno ? fieldErrors.cDoorno : ""}
                                                        value={cruProperty.cDoorno ? cruProperty.cDoorno : ""}
                                                        onChange={(e) => handleChange(e, "cDoorno", true, '')}
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
                                                        errorText={fieldErrors.addressTwo ? fieldErrors.addressTwo : ""}
                                                        value={cruProperty.addressTwo ? cruProperty.addressTwo : ""}
                                                        onChange={(e) => handleChange(e, "addressTwo", true, '')}
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
                                                        errorText={fieldErrors.pinTwo ? fieldErrors.pinTwo : ""}
                                                        value={cruProperty.pinTwo ? cruProperty.pinTwo : ""}
                                                        onChange={(e) => handleChange(e, "pinTwo", true, '')}
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
                                                defaultChecked ={cruProperty.lift}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "lift", false, '')
                                                }}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="Toilet"
                                                style={styles.checkbox}
                                                defaultChecked ={cruProperty.toilet}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "toilet", false, '')
                                                }}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="water tap"
                                                style={styles.checkbox}
                                                defaultChecked ={cruProperty.waterTap}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "waterTap", false, '')
                                                }}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="electricity"
                                                style={styles.checkbox}
                                                defaultChecked ={cruProperty.electricity}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "electricity", false, '')
                                                }}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="Attached bathroom"
                                                style={styles.checkbox}
                                                defaultChecked ={cruProperty.attachedBathroom}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "attachedBathroom", false, '')
                                                }}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="Cable connection"
                                                style={styles.checkbox}
                                                defaultChecked ={cruProperty.cableConnection}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "cableConnection", false, '')
                                                }}
                                              />
                                          </Col>
                                          <Col xs={12} md={3}>
                                              <Checkbox
                                                label="Water harvesting"
                                                style={styles.checkbox}
                                                defaultChecked ={cruProperty.waterHarvesting}
                                                onCheck = {(e, i, v) => {
                                                  var e = {
                                                    target: {
                                                      value:i
                                                    }
                                                  }
                                                  handleChange(e, "waterHarvesting", false, '')
                                                }}
                                              />
                                          </Col>
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
                                                  errorText={fieldErrors.reasonForCreation ? fieldErrors.reasonForCreation : ""}
                                                  value={cruProperty.reasonForCreation ? cruProperty.reasonForCreation : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "reasonForCreation", true, "")}
                                                  }
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
                                                  errorText={fieldErrors.parentUpicNo ? fieldErrors.parentUpicNo : ""}
                                                  value={cruProperty.parentUpicNo ? cruProperty.parentUpicNo : ""}
                                                  onChange={(e) => {handleChange(e, "parentUpicNo", true, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              />
                                          </Col>
                                          <div className="clearfix"></div>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Property Type"
                                                  errorText={fieldErrors.assessmentPropertyType ? fieldErrors.assessmentPropertyType : ""}
                                                  value={cruProperty.assessmentPropertyType ? cruProperty.assessmentPropertyType : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "assessmentPropertyType", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                  <MenuItem value={1} primaryText="Building"/>
                                                  <MenuItem value={2} primaryText="Open Land"/>
                                                  <MenuItem value={2} primaryText="Religious"/>
                                                  <MenuItem value={2} primaryText="Others"/>
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Property Sub-type"
                                                  errorText={fieldErrors.assessmentPropertySubType ? fieldErrors.assessmentPropertySubType : ""}
                                                  value={cruProperty.assessmentPropertySubType ? cruProperty.assessmentPropertySubType : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "assessmentPropertySubType", true, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                  <MenuItem value={1} primaryText="Options"/>
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="assessmentDepartment"
                                                  errorText={fieldErrors.assessmentDepartment ? fieldErrors.assessmentDepartment : ""}
                                                  value={cruProperty.assessmentDepartment ? cruProperty.assessmentDepartment : ""}
                                                  onChange={(event, index, value) => {
                                                      var e = {
                                                        target: {
                                                          value: value
                                                        }
                                                      };
                                                      handleChange(e, "assessmentDepartment", false, "")}
                                                  }
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                              >
                                                  <MenuItem value={1} primaryText="Options"/>
                                              </SelectField>
                                          </Col>
                                          <Col xs={12} md={6}>
                                              <TextField
                                                  floatingLabelText="Extent of Site (Sq. Mtrs)"
                                                  errorText={fieldErrors.extentOfSite ? fieldErrors.extentOfSite : ""}
                                                  value={cruProperty.extentOfSite ? cruProperty.extentOfSite : ""}
                                                  onChange={(e) => {handleChange(e, "extentOfSite", true, "")}}
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
                      <CardHeader title={<strong style={{color:brown500}}>Construction Types</strong>} />
                      <CardText>
                          <Card>
                              <CardText>
                                  <Grid>
                                      <Row>
                                          <Col xs={12} md={6}>
                                              <SelectField
                                                floatingLabelText="Floor Type"
                                                errorText={fieldErrors.floorType ? fieldErrors.floorType : ""}
                                                value={cruProperty.floorType ? cruProperty.floorType : ""}
                                                onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "floorType", true, "")}
                                                }
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
                                                errorText={fieldErrors.roofType ? fieldErrors.roofType : ""}
                                                value={cruProperty.roofType ? cruProperty.roofType : ""}
                                                onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "roofType", true, "")}
                                                }
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
                                                errorText={fieldErrors.wallType ? fieldErrors.wallType : ""}
                                                value={cruProperty.wallType ? cruProperty.wallType : ""}
                                                onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "wallType", false, "")}
                                                }
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
                                                errorText={fieldErrors.woodType ? fieldErrors.woodType : ""}
                                                value={cruProperty.woodType ? cruProperty.woodType : ""}
                                                onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "woodType", false, "")}
                                                }
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
                                        <Col xs={12}>
                                          <FloatingActionButton mini={true} className="pull-right" onClick={() => {
                                                this.setState((prevState, props) => ({
                                                    addFloor: !prevState.addFloor
                                                  }));
                                                this.props.resetObject("floorDetail");
                                                this.props.resetObject("ownerDetail");
                                                isEditIndex(-1);
                                              }
                                          }>
                                              <i className="material-icons" style={styles.iconFont}>{ !this.state.addFloor ? "add_box": "remove_circle"}</i>
                                          </FloatingActionButton>
                                          <div className="clearfix" />
                                          <br/>

                                        </Col>
                                      </Row>
                                      <Row>
                                      <Col xs={12} md={12}>
                                          <Table id="cruPropertyTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
                                          <thead style={{backgroundColor:"#f2851f",color:"white"}}>
                                            <tr>
                                              <th>#</th>
                                              <th>Floor Number</th>
                                              <th>Unit Number</th>
                                              <th>Room Type</th>
                                              <th>Construction Type</th>
                                              <th>Usage Type</th>
                                              <th>Usage Sub Type</th>
                                              <th>Firm Name</th>
                                              <th>Occupancy</th>
                                              <th>Occupant Name</th>
                                              <th>Annual Rent</th>
                                              <th>Manual ARV</th>
                                              <th>Construction Date</th>
                                              <th>Effective From Date</th>
                                              <th>Unstructured land</th>
                                              <th>Length</th>
                                              <th>Breadth</th>
                                              <th>Plinth Area</th>
                                              <th>Occupancy Certificate Number</th>
                                              <th>Building Permission Number</th>
                                              <th>Building Permission Date</th>
                                              <th>Plinth Area In Building Plan</th>
                                              <th>Action</th>
                                            </tr>
                                          </thead>
                                          <tbody>
                                            {cruProperty.floorDetails && cruProperty.floorDetails.map(function(i, index){
                                              return (<tr key={index}>
                                                  <td>{index}</td>
                                                  <td>{i.floorNo}</td>
                                                  <td>{i.unitNumber}</td>
                                                  <td>{i.roomType}</td>
                                                  <td>{i.constructionType}</td>
                                                  <td>{i.usageType}</td>
                                                  <td>{i.usageSubType}</td>
                                                  <td>{i.firmName}</td>
                                                  <td>{i.occupancy}</td>
                                                  <td>{i.occupantName}</td>
                                                  <td>{i.annualRent}</td>
                                                  <td>{i.manualArv}</td>
                                                  <td>{i.constructionDate}</td>
                                                  <td>{i.effectiveDate}</td>
                                                  <td>{i.unstructuredLand}</td>
                                                  <td>{i.length}</td>
                                                  <td>{i.breadth}</td>
                                                  <td>{i.plinthArea}</td>
                                                  <td>{i.occupancyCertiNumber}</td>
                                                  <td>{i.buildingPermissionNo}</td>
                                                  <td>{i.buildingPermissionDate}</td>
                                                  <td>{i.plinthAreaBuildingPlan}</td>
                                                  <td>
                                                    <FloatingActionButton mini={true} style={styles.floatButtonMargin} onClick={ () => {
                                                      editObject("floorDetail",i);
                                                      isEditIndex(index);
                                                    }}>
                                                       <i className="material-icons">mode_edit</i>
                                                   </FloatingActionButton>
                                                   <FloatingActionButton mini={true} style={styles.floatButtonMargin} onClick={ () => {deleteObject("floorDetails", index);}}>
                                                       <i className="material-icons">delete</i>
                                                   </FloatingActionButton>
                                                  </td>
                                                </tr> )
                                            })}
                                          </tbody>
                                          </Table>
                                      </Col>
                                      </Row>
                                      {this.state.addFloor && floorForm()}
                                  </Grid>
                              </CardText>
                          </Card>

                      </CardText>
                  </Card>
                  <Card>
                    <CardHeader title={< strong style = {{color:brown500}} >Document Upload</strong>} />
                    <CardText>
                        <Card>
                            <CardText>
                                <Grid>
                                    <Row>
                                        <Col xs={12} md={12}>

                                          <FlatButton   label="Choose Document"
                                              labelPosition="before"
                                              style={styles.uploadButton}
                                              containerElement="label">
                                                  <Upload onFileLoad={this.onFileLoad} style={styles.uploadInput}/>
                                          </FlatButton>

                                        </Col>
                                    </Row>
                                </Grid>
                            </CardText>
                        </Card>
                    </CardText>
                  </Card>
                  <Card>
                    <CardHeader title={< strong style = {{color:brown500}} >Workflow</strong>} />
                    <CardText>
                        <Card>
                            <CardText>
                                <Grid>
                                    <Row>
                                        <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Department Name"
                                                  errorText={fieldErrors.workflowDepartment ? fieldErrors.workflowDepartment: ""}
                                                  value={cruProperty.workflowDepartment ? cruProperty.workflowDepartment :""}
                                                  onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "workflowDepartment", false, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  >
                                                    <MenuItem value={4} primaryText="Options" />
                                              </SelectField>
                                        </Col>
                                        <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Designation Name"
                                                  errorText={fieldErrors.workflowDesignation ? fieldErrors.workflowDesignation: ""}
                                                  value={cruProperty.workflowDesignation ? cruProperty.workflowDesignation :""}
                                                  onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "workflowDesignation", false, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  >
                                                    <MenuItem value={4} primaryText="Options" />
                                              </SelectField>
                                        </Col>
                                        <Col xs={12} md={6}>
                                              <SelectField
                                                  floatingLabelText="Approver Name"
                                                  errorText={fieldErrors.approverName ? fieldErrors.approverName: ""}
                                                  value={cruProperty.approverName ? cruProperty.approverName :""}
                                                  onChange={(event, index, value) => {
                                                    var e = {
                                                      target: {
                                                        value: value
                                                      }
                                                    };
                                                    handleChange(e, "approverName", false, "")}}
                                                  floatingLabelFocusStyle={styles.floatingLabelFocusStyle}
                                                  underlineStyle={styles.underlineStyle}
                                                  underlineFocusStyle={styles.underlineFocusStyle}
                                                  >
                                                    <MenuItem value={4} primaryText="Options" />
                                              </SelectField>
                                        </Col>
                                    </Row>
                                </Grid>
                            </CardText>
                        </Card>
                    </CardText>
                  </Card>
              </form>
          </div>
      )
  }
}

const mapStateToProps = state => ({
  cruProperty:state.form.form,
  fieldErrors: state.form.fieldErrors,
  editIndex: state.form.editIndex

});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: []
        },
        pattern: {
          current: [],
          required: []
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
  addNestedFormData: (formArray, formData) => {
    dispatch({
      type: "PUSH_ONE",
      formArray,
      formData
    })
  },

  deleteObject: (property, index) => {
    dispatch({
      type: "DELETE_OBJECT",
      property,
      index
    })
  },

  editObject: (objectName, object, isEditable) => {
    dispatch({
      type: "EDIT_OBJECT",
      objectName,
      object,
      isEditable
    })
  },

  resetObject: (object) => {
    dispatch({
      type: "RESET_OBJECT",
      object
    })
  },

  updateObject: (objectName, object) => {
    dispatch({
      type: "UPDATE_OBJECT",
      objectName,
      object
    })
  },

  isEditIndex: (index) => {
    dispatch({
      type: "EDIT_INDEX",
      index
    })
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(CruProperty);
