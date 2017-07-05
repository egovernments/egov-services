import React, {Component} from 'react';
import {connect} from 'react-redux';

import MenuItem from 'material-ui/MenuItem';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';

import Api from '../../../api/api';

const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');


const buttons = require('datatables.net-buttons-bs');

require('datatables.net-buttons/js/buttons.colVis.js'); // Column visibility
require('datatables.net-buttons/js/buttons.html5.js'); // HTML 5 file export
require('datatables.net-buttons/js/buttons.flash.js'); // Flash file export
require('datatables.net-buttons/js/buttons.print.js'); // Print view button

var flag = 0;


class ShowField extends Component {
  constructor(props) {
       super(props);

   }


  componentWillUnmount()
  {
     $('#propertyTaxTable')
     .DataTable()
     .destroy(true);
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
      isTableShow
    } = this.props;

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
        {viewTabel()}
      </div>
    );
  }
}

const mapStateToProps = state => ({isTableShow:state.form.showTable});

const mapDispatchToProps = dispatch => ({

});

export default connect(mapStateToProps, mapDispatchToProps)(ShowField);
