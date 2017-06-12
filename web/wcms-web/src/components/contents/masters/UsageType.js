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
            <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Create Usage Type < /strong>}/>

            <CardText>
              <Card>
                <CardText>
                  <Grid>
                    <Row>
                    <Col xs={12} md={6}>
                      <TextField errorText={fieldErrors.ownerName
                        ? fieldErrors.ownerName
                        : ""} value={usageType.ownerName?usageType.ownerName:""} onChange={(e) => handleChange(e, "ownerName", false, "")} hintText="Name" floatingLabelText="Name" />
                    </Col>

                    <Col xs={12} md={6}>
                      <TextField errorText={fieldErrors.Description
                        ? fieldErrors.Description
                        : ""} value={usageType.Description?usageType.Description:""} multiLine={true} onChange={(e) => handleChange(e, "Description", false, "")} hintText="Description" floatingLabelText="Description" />
                    </Col>
                    </Row>
                    <Row>
                    <Col xs={12} md={6}>
                                        <Checkbox
                                         label="Active"
                                         errorText={fieldErrors.Active
                                           ? fieldErrors.Active
                                           : ""}
                                         value={usageType.Active?usageType.Active:""}
                                         onCheck={(event,isInputChecked) => {
                                           var e={
                                             "target":{
                                               "value":isInputChecked
                                             }
                                           }
                                           handleChange(e, "Active", false, "")}
                                         }
                                         style={styles.checkbox}
                                         style={styles.topGap}
                                        />
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
