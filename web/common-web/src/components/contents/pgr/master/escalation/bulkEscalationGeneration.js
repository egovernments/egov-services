import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, DropdownButton, Table ,ListGroup, ListGroupItem} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
import Checkbox from 'material-ui/Checkbox';
import DatePicker from 'material-ui/DatePicker';
import SelectField from 'material-ui/SelectField';
import MenuItem from 'material-ui/MenuItem';
import AutoComplete from 'material-ui/AutoComplete';
import Dialog from 'material-ui/Dialog';
import RaisedButton from 'material-ui/RaisedButton';
import FlatButton from 'material-ui/FlatButton';
import DataTable from '../../../../common/Table';
import Api from '../../../../../api/api';


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
  headerStyle : {
    color: 'rgb(90, 62, 27)',
    fontSize : 19
  },
  marginStyle:{
    margin: '15px'
  },
  paddingStyle:{
    padding: '15px'
  },
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
    marginTop: 37
  }
};

class BulkEscalationGeneration extends Component {
    constructor(props) {
      super(props)
      this.state = {
              positionSource: [],
              dataSourceConfig : {
                text: 'name',
                value: 'id',
              },
              grievanceType:[]
            };
    }

    componentWillMount() {
      let{initForm} = this.props;
      initForm()

      $('#searchTable').DataTable({
           dom: 'lBfrtip',
           buttons: [
                     'excel', 'pdf', 'print'
            ],
            ordering: false,
            bDestroy: true,
      });
    }

    componentDidMount() {
      let self = this;
      Api.commonApiPost("/hr-masters/positions/_search").then(function(response) {
          self.setState({
            positionSource: response.Position
          })
      }, function(err) {
        self.setState({
            positionSource: []
          })
      });


        Api.commonApiPost("/pgr/services/_search", {type: "all"}).then(function(response) {
            self.setState({
              grievanceType: response.complaintTypes
            })
        }, function(err) {
          self.setState({
              grievanceType: []
            })
        });
    }

    componentWillUpdate() {
      if(flag == 1) {
        flag = 0;
        $('#searchTable').dataTable().fnDestroy();
      }
    }

    componentWillUnmount(){
       $('#searchTable')
       .DataTable()
       .destroy(true);
    }

  submitForm = (e) => {
      e.preventDefault()
      flag = 1;
      this.setState({
        resultList: [],
        isSearchClicked: true
      })
  }

    render() {

      let {
        isFormValid,
        bulkEscalationGeneration,
        fieldErrors,
        handleChange,
        handleAutoCompleteKeyUp
      } = this.props;

      let {submitForm} = this;

      let {isSearchClicked, resultList} = this.state;

      console.log(bulkEscalationGeneration);

      const renderBody = function() {
      	  if(resultList && resultList.length)
      		return resultList.map(function(val, i) {
      			return (
      				<tr key={i}>
      					<td>{val.serviceName}</td>
      					<td>{val.boundaryType}</td>
      					<td>{val.boundary}</td>
      				</tr>
      			)
      		})
      }

      const viewTable = function() {
      	  if(isSearchClicked)
      		return (
   	        <Card>
   	          <CardHeader title={<strong style = {{color:"#5a3e1b"}} > Search Result </strong>}/>
   	          <CardText>
   		        <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
   		          <thead style={{backgroundColor:"#f2851f",color:"white"}}>
   		            <tr>
   		              <th>Grievance Type</th>
   		              <th>From Position</th>
   		              <th>To Position</th>
   		            </tr>
   		          </thead>
   		          <tbody>
   		          	{renderBody()}
   		          </tbody>
   		        </Table>
   		       </CardText>
   		    </Card>
   		)
      }


      return(<div className="bulkEscalationGeneration">
      <form autoComplete="off" onSubmit={(e) => {submitForm(e)}}>
          <Card  style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Bulk Escalation Generation < /div>} />
              <CardText>
                  <Card>
                      <CardText>
                          <Grid>
                              <Row>
                                  <Col xs={12} md={4}>
                                        <AutoComplete
                                          floatingLabelText="From Position"
                                          fullWidth={true}
                                          filter={function filter(searchText, key) {
                                                    return key.toLowerCase().includes(searchText.toLowerCase());
                                                 }}
                                          dataSource={this.state.positionSource}
                                          dataSourceConfig={this.state.dataSourceConfig}
                                          onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "fromPosition")}}
                                          value={bulkEscalationGeneration.fromPosition ? bulkEscalationGeneration.fromPosition : ""}
                                          onNewRequest={(chosenRequest, index) => {
                  	                        var e = {
                  	                          target: {
                  	                            value: chosenRequest.id
                  	                          }
                  	                        };
                  	                        handleChange(e, "fromPosition", true, "");
                  	                       }}
                                        />
                                  </Col>
                                  <Col xs={12} md={4}>
                                        <SelectField
                                           multiple={true}
                                           floatingLabelText="Grievance Type"
                                           fullWidth={true}
                                           value={bulkEscalationGeneration.grievanceType ? bulkEscalationGeneration.grievanceType : ""}
                                           onChange= {(e, index ,values) => {
                                             var e = {
                                               target: {
                                                 value: values
                                               }
                                             };
                                             handleChange(e, "grievanceType", true, "");
                                            }}
                                         >
                                         {this.state.grievanceType.map((item, index) => (
                                                   <MenuItem
                                                     value={item.serviceCode}
                                                     key={index}
                                                     insetChildren={true}
                                                     primaryText={item.serviceName}
                                                     checked={bulkEscalationGeneration.grievanceType && bulkEscalationGeneration.grievanceType.indexOf(item.serviceCode) > -1}
                                                   />
                                          ))}
                                          </SelectField>
                                  </Col>
                                  <Col xs={12} md={4}>
                                      <AutoComplete
                                          floatingLabelText="To Position"
                                          fullWidth={true}
                                          filter={function filter(searchText, key) {
                                                    return key.toLowerCase().includes(searchText.toLowerCase());
                                                 }}
                                          dataSource={this.state.positionSource}
                                          dataSourceConfig={this.state.dataSourceConfig}
                                          onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "toPosition")}}
                                          value={bulkEscalationGeneration.toPosition ? bulkEscalationGeneration.toPosition : ""}
                                          onNewRequest={(chosenRequest, index) => {
                                            var e = {
                                              target: {
                                                value: chosenRequest.id
                                              }
                                            };
                                            handleChange(e, "toPosition", true, "");
                                           }}
                                        />
                                  </Col>
                              </Row>
                          </Grid>
                      </CardText>
                  </Card>
                  <div style={{textAlign:'center'}}>
                      <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label="Search" backgroundColor={"#5a3e1b"} labelColor={white}/>
                      <RaisedButton style={{margin:'15px 5px'}} label="Close"/>
                  </div>
                  {viewTable()}
              </CardText>
          </Card>
          </form>
      </div>)
    }
}


const mapStateToProps = state => {
  return ({bulkEscalationGeneration : state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["fromPosition","grievanceType" ,"toPosition"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },

  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:true,
      fieldErrors: {},
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
    dispatch({
      type: "HANDLE_CHANGE",
      property,
      value: e.target.value,
      isRequired,
      pattern
    });
  },

  handleAutoCompleteKeyUp : (e, type) => {
    dispatch({
      type: "HANDLE_CHANGE",
      property: type,
      value: e.target.value,
      isRequired : true,
      pattern: ''
    });
  }
})

export default connect(mapStateToProps, mapDispatchToProps)(BulkEscalationGeneration);
