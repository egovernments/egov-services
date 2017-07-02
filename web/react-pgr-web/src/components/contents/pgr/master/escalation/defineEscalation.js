import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, DropdownButton, Table, ListGroupItem} from 'react-bootstrap';
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

class DefineEscalation extends Component {
    constructor(props) {
      super(props)
      this.state = {
              positionSource:[],
              dataSourceConfig : {
                text: 'name',
                value: 'id',
              },
              isSearchClicked: false,
              resultList: [],
              noData:false,
              escalationForm:{
              }
            };
    }

    componentWillMount() {
      let{initForm} = this.props;
      initForm();

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
      e.preventDefault();
      let current = this;

      let query = {
        id:this.props.defineEscalation.position.id
      }

      Api.commonApiPost("/pgr-master/escalation/_search",query,{}).then(function(response){
          console.log(response);

          if (response.EscalationTimeType[0] != null) {
              flag = 1;
              current.setState({
                resultList: response.EscalationTimeType,
                isSearchClicked: true
              })
          } else {
            current.setState({
              noData: true,
            })
          }
      }).catch((error)=>{
          console.log(error);
      })

  }

  localHandleChange = (e, property, isRequired, pattern) => {
    if(this.state.escalationForm.hasOwnProperty('fromPosition')){
      this.setState({
          escalationForm: {
            ...this.state.escalationForm,
              [property]: e.target.value
          }
      })
    } else {
      this.setState({
          escalationForm: {
            ...this.state.escalationForm,
            fromPosition: this.props.defineEscalation.position.id,
              [property]: e.target.value
          }
      })
    }
  }

  addEscalation = () => {
        this.setState({
          resultList:[
            ...this.state.resultList,
            this.state.escalationForm
          ]
      })
  }

    render() {

      var current = this;

      let {
        isFormValid,
        defineEscalation,
        fieldErrors,
        handleChange,
        handleAutoCompleteKeyUp
      } = this.props;

      let {submitForm, localHandleChange, addEscalation} = this;

      console.log(this.state.escalationForm, this.state.resultList);

      let {isSearchClicked, resultList, escalationForm} = this.state;

      const renderBody = function() {
      	  if(resultList && resultList.length)
      		return resultList.map(function(val, i) {
      			return (
      				<tr key={i}>
                <td>{val.fromPosition}</td>
      					<td>{val.grievanceType}</td>
      					<td>{val.department}</td>
      					<td>{val.designation}</td>
                <td>{val.toPosition}</td>
                <td><RaisedButton style={{margin:'15px 5px'}} label="Delete" backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
                    }}/></td>
      				</tr>
      			)
      		})
      }

      const viewTable = function() {
      	  if(isSearchClicked)
      		return (
   	        <Card>
              <CardText>
                  <Row>
                    <Col xs={12} md={3} sm={6}>
                        <TextField
                            fullWidth={true}
                            floatingLabelText="From Position"
                            value={defineEscalation.position ? defineEscalation.position.name : ""}
                            id="name"
                            disabled={true}
                        />
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText="Grievance Type"
                           fullWidth={true}
                           value={escalationForm.grievanceType ? escalationForm.grievanceType : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             localHandleChange(e, "grievanceType", true, "");
                            }}
                          >
                           <MenuItem value={1} primaryText="Options" />
                        </SelectField>
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText="Department"
                           fullWidth={true}
                           value={escalationForm.department ? escalationForm.department : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             localHandleChange(e, "department", true, "");
                            }}
                         >
                           <MenuItem value={2} primaryText="Options" />
                        </SelectField>
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText="Designation"
                           fullWidth={true}
                           value={escalationForm.designation ? escalationForm.designation : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             localHandleChange(e, "designation", true, "");
                            }}
                         >
                           <MenuItem value={1} primaryText="Options" />
                        </SelectField>
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText="To Position"
                           fullWidth={true}
                           value={escalationForm.toPosition ? escalationForm.toPosition : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             localHandleChange(e, "toPosition", true, "");
                            }}
                         >
                           <MenuItem value={1} primaryText="Options" />
                        </SelectField>
                    </Col>
                    <div className="clearfix"></div>
                    <Col xs={12} md={12} style={{textAlign:"center"}}>
                        <RaisedButton style={{margin:'0 3px'}} label="Update" backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
                          addEscalation();
                        }}/>
                    </Col>
                  </Row>
              </CardText>
   	          <CardText>
   		        <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
   		          <thead style={{backgroundColor:"#f2851f",color:"white"}}>
   		            <tr>
   		              <th>From Position</th>
   		              <th>Grievance Type</th>
   		              <th>Department</th>
                    <th>Designation</th>
                    <th>To Position</th>
                    <th></th>
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

      return(<div className="defineEscalation">
      <form autoComplete="off" onSubmit={(e) => {submitForm(e)}}>
          <Card  style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Create/Update Escalation < /div>} />
              <CardText>
                  <Card>
                      <CardText>
                          <Grid>
                              <Row>
                                  <Col xs={12} md={4} mdPush={4}>
                                        <AutoComplete
                                          floatingLabelText="Position"
                                          fullWidth={true}
                                          filter={function filter(searchText, key) {
                                                    return key.toLowerCase().includes(searchText.toLowerCase());
                                                 }}
                                          dataSource={this.state.positionSource}
                                          dataSourceConfig={this.state.dataSourceConfig}
                                          onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "position")}}
                                          value={defineEscalation.position ? defineEscalation.position : ""}
                                          onNewRequest={(chosenRequest, index) => {
                  	                        var e = {
                  	                          target: {
                  	                            value: chosenRequest
                  	                          }
                  	                        };
                  	                        handleChange(e, "position", true, "");
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
                  {this.state.noData &&
                    <Card style = {{textAlign:"center"}}>
                      <CardHeader title={<strong style = {{color:"#5a3e1b", paddingLeft:90}} > There is no escalation details available for the selected position. </strong>}/>
                      <CardText>
                          <RaisedButton style={{margin:'10px 0'}} label="Add Escalation Details" backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
                            this.setState({
                              isSearchClicked: true,
                              noData:false
                            })
                          }}/>
                     </CardText>
                  </Card>
                  }
                  {viewTable()}
              </CardText>
          </Card>
          </form>
      </div>)
    }
}


const mapStateToProps = state => {
  return ({defineEscalation : state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["position"]
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
    console.log("handlechange"+e+property+isRequired+pattern);
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

export default connect(mapStateToProps, mapDispatchToProps)(DefineEscalation);
