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

class ShowPropertyCategory extends Component {
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
                      'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true,

          });
      }
  }

  render() {
    let {
      showPropertyCategory,
      fieldErrors,
      isFormValid,
      isTableShow,
      handleChange,
      handleChangeNextOne,
      handleChangeNextTwo,
      buttonText
    } = this.props;
    let {search} = this;
    console.log(showPropertyCategory);
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


              <th>Sl No.</th>
              <th>Property Type</th>
              <th> Category Type </th>
              <th>Status</th>
              <th>Action </th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>1</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-1" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>
            <tr>
              <td>2</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
              <DropdownButton title="Action" id="dropdown-2" pullRight>
                  <MenuItem>View</MenuItem>
                  <MenuItem>Update</MenuItem>
              </DropdownButton>
              </td>
            </tr>

            <tr>
              <td>3</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>Table cell</td>
              <td>
                <DropdownButton title="Action" id="dropdown-3" pullRight>
                    <MenuItem>View</MenuItem>
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
      <div className="ShowPropertyCategory">
        <form onSubmit={(e) => {
          search(e)
        }}>
          <Card>
            <CardHeader title={< strong style = {{color:"#5a3e1b"}} > Modify Property Category  < /strong>}/>

            <CardText>
              <Card>
                <CardText>
                  <Grid>
                    <Row>
                    <Col xs={12} md={6}>
                      <TextField errorText={fieldErrors.name
                        ? fieldErrors.name
                        : ""} value={showPropertyCategory.name?showPropertyCategory.name:""} onChange={(e) => handleChange(e, "name", false, "")} hintText="Name" floatingLabelText="Name" />
                    </Col>

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

const mapStateToProps = state => ({showPropertyCategory: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid,isTableShow:state.form.showTable,buttonText:state.form.buttonText});

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
  }


});

export default connect(mapStateToProps, mapDispatchToProps)(ShowPropertyCategory);
