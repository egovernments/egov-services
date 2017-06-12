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
            <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Acknowledgement  < /strong>}/>

            <CardText>
              <Card>
                <CardText>
                  <Grid>
                    <Row>
                    <Col xs={12} md={6}>
                    <label><h5> Acknowledgement No</h5>  </label>
                    <label>:  123</label>
                    </Col>

                    <Col xs={12} md={6}>
                    <label><h5>Service Type </h5>  </label>
                    <label>: Rental </label>
                    </Col>
                    </Row>
                    <Row>

                    <Col xs={12} md={6}>
                    <label><h5> Name </h5> </label>
                    <label> :  Kumar </label>
                    </Col>
                    <Col xs={12} md={6}>
                    <label><h5> Address </h5> </label>
                    <label> :  Kolkata </label>
                    </Col>
                    </Row>
                    <Row>
                    <Col xs={12} md={6}>
                    <label><h5> Election Ward </h5> </label>
                    <label> :  110 </label>
                    </Col>

                    <Col xs={12} md={6}>
                    <label><h5> Date </h5> </label>
                    <label> :  10/05/2017 </label>
                    </Col>
                    </Row>
                    <Row>
                    <Col xs={12} md={6}>
                    <label><h5> Due Date </h5> </label>
                    <label> :  10/04/2017 </label>
                    </Col>
                    </Row>

                    </Grid>
                  </CardText>
              </Card>





              <div style={{
                float: "center"
              }}>
              <RaisedButton label="Print" primary={true}/>
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
          required: [ ]
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
