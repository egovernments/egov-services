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
import {translate} from '../../../../common/common';

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

const getNameById = function(object, id, property = "") {
  if (id == "" || id == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property == "") {
            if (object[i].id == id) {
                return object[i].name;
            }
        } else {
            if (object[i].hasOwnProperty(property)) {
                if (object[i].id == id) {
                    return object[i][property];
                }
            } else {
                return "";
            }
        }
    }
    return "";
}

class DefineEscalationTime extends Component {
    constructor(props) {
      super(props)
      this.state = {
              serviceTypeSource:[],
              designation:[],
              dataSourceConfig : {
                text: 'serviceName',
                value: 'id',
              },
              isSearchClicked: false,
              resultList: [],
              noData:false,
              escalationForm:{
              },
              editIndex: -1
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
      Api.commonApiPost("/pgr/services/v1/_search", {type: "all"}).then(function(response) {
          console.log(response);
          self.setState({
            serviceTypeSource: response.complaintTypes
          })
      }, function(err) {
        self.setState({
            serviceTypeSource: []
          })
      });

      Api.commonApiPost("/hr-masters/designations/_search").then(function(response) {
          self.setState({
            designation: response.Designation
          })
      }, function(err) {
        self.setState({
            designation: []
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
        id:this.props.defineEscalationTime.serviceType.id
      }

      Api.commonApiPost("workflow/escalation-hours/v1/_search",query,{}).then(function(response){
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

  addEscalation = () => {
    var current = this
    var body = {
      EscalationTimeType:{
        grievancetype:{
          id: this.props.defineEscalationTime.serviceType.id
        },
        noOfHours:this.props.defineEscalationTime.noOfHours,
        designation:this.props.defineEscalationTime.designation.id,
        tenantId :localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : 'default'

      }
    }

    Api.commonApiPost("workflow/escalation-hours/v1/_create",{},body).then(function(response){
        console.log(response);
        current.setState({
          resultList:[
            ...current.state.resultList,
            current.props.defineEscalationTime
          ]
      })
    }).catch((error)=>{
        console.log(error);
    })

  /*  current.setState({
      resultList:[
        ...this.state.resultList,
        this.props.defineEscalationTime
      ]
    })*/
  }

  updateEscalation = () => {

    var current = this
    var body = {
      EscalationTimeType:{
        grievancetype:{
          id: this.props.defineEscalationTime.serviceType.id
        },
        noOfHours:this.props.defineEscalationTime.noOfHours,
        designation:this.props.defineEscalationTime.designation.id,
        tenantId :localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : 'default'
      }
    }

    var idd = this.props.defineEscalationTime.serviceType.id

    Api.commonApiPost("workflow/escalation-hours/v1/_update/"+idd,{},body).then(function(response){
        console.log(response);
        current.setState((prevState)=>{
          resultList:[
            ...current.state.resultList,
            prevState.resultList[prevState.editIndex] = current.props.defineEscalationTime
          ],
          prevState.editIndex=-1
        })
    }).catch((error)=>{
        console.log(error);
    })

  }

  editObject = (index) => {
      this.props.setForm(this.state.resultList[index])
  }

  deleteObject = (index) => {
    this.setState({
        resultList:[
          ...this.state.resultList.slice(0, index),
          ...this.state.resultList.slice(index+1)
        ]
    })
  }

    render() {

      var current = this;

      let {
        isFormValid,
        defineEscalationTime,
        fieldErrors,
        handleChange,
        handleAutoCompleteKeyUp
      } = this.props;

      let {submitForm, localHandleChange, addEscalation, deleteObject, editObject, updateEscalation} = this;

      console.log(this.state.escalationForm, this.state.resultList);

      let {isSearchClicked, resultList, escalationForm, designation, editIndex} = this.state;

      console.log(designation);

      const renderBody = function() {
      	  if(resultList && resultList.length)
      		return resultList.map(function(val, i) {
      			return (
      				<tr key={i}>
                <td>{i+1}</td>
      					<td>{val.designation.name}</td>
                <td>{val.noOfHours}</td>
                <td>
                <RaisedButton style={{margin:'0 3px'}} label={translate("pgr.lbl.update")} backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
                    editObject(i);
                    current.setState({editIndex:i})
                }}/>
                {false && <RaisedButton style={{margin:'0 3px'}} label={translate("pgr.lbl.delete")} disabled={editIndex<0?false:true} backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
                    deleteObject(i);
                }}/>}</td>
      				</tr>
      			)
      		})
      }

      const viewTable = function() {
      	  if(!isSearchClicked)
      		return (
   	        <Card>
              <CardText>
                  <Row>
                    <Col xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText={translate("pgr.lbl.designation")}
                           fullWidth={true}
                           value={defineEscalationTime.designation ? defineEscalationTime.designation : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             handleChange(e, "designation", true, "");
                            }}
                          >
                              {current.state.designation && current.state.designation.map((e,i)=>{
                                  return(<MenuItem key={i} value={e} primaryText={e.name} />)
                              })}
                        </SelectField>
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <TextField
                            fullWidth={true}
                            floatingLabelText={translate("pgr.noof.hours")}
                            value={defineEscalationTime.noOfHours ? defineEscalationTime.noOfHours : ""}
                            errorText={fieldErrors.noOfHours ? fieldErrors.noOfHours : ""}
                            onChange={(e) => {
                              handleChange(e, "noOfHours", true, /^\d+$/)
                            }}
                            id="noOfHours"
                        />
                    </Col>
                    <div className="clearfix"></div>
                    <Col xs={12} md={12} style={{textAlign:"center"}}>
                        {editIndex<0 && <RaisedButton style={{margin:'15px 5px'}} disabled={!isFormValid} label={translate("pgr.lbl.add")} backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
                          addEscalation();
                        }}/>}
                        {editIndex>=0 && <RaisedButton style={{margin:'15px 5px'}} disabled={!isFormValid} label={translate("pgr.lbl.update")} backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
                          updateEscalation();
                        }}/>}
                    </Col>
                  </Row>
              </CardText>
   	          <CardText>
   		        <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
   		          <thead style={{backgroundColor:"#f2851f",color:"white"}}>
   		            <tr>
   		              <th>No.</th>
                    <th>{translate("pgr.lbl.designation")}</th>
                    <th>{translate("pgr.noof.hours")}</th>
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

      console.log(defineEscalationTime);

      return(<div className="defineEscalationTime">
      <form autoComplete="off" onSubmit={(e) => {submitForm(e)}}>
          <Card  style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > Search Escalation Time < /div>} />
              <CardText>
                  <Card>
                      <CardText>
                          <Grid>
                              <Row>
                                  <Col xs={12} md={4} mdPush={4}>
                                        <AutoComplete
                                          floatingLabelText={translate("pgr.lbl.grievance.type")}
                                          fullWidth={true}
                                          filter={function filter(searchText, key) {
                                                    return key.toLowerCase().includes(searchText.toLowerCase());
                                                 }}
                                          dataSource={this.state.serviceTypeSource}
                                          dataSourceConfig={this.state.dataSourceConfig}
                                          onKeyUp={handleAutoCompleteKeyUp}
                                          errorText={fieldErrors.serviceType ? fieldErrors.serviceType : "" }
                                          value={defineEscalationTime.serviceType ? defineEscalationTime.serviceType : ""}
                                          onNewRequest={(chosenRequest, index) => {
                  	                        var e = {
                  	                          target: {
                  	                            value: chosenRequest
                  	                          }
                  	                        };
                  	                        handleChange(e, "serviceType", false, "");
                  	                       }}
                                        />
                                  </Col>
                              </Row>
                          </Grid>
                      </CardText>
                  </Card>
                  <div style={{textAlign:'center'}}>
                      <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={defineEscalationTime.serviceType ? false : true} label={translate("core.lbl.search")} backgroundColor={"#5a3e1b"} labelColor={white}/>
                      <RaisedButton style={{margin:'15px 5px'}} label={translate("core.lbl.close")}/>
                  </div>
                  {this.state.noData &&
                    <Card style = {{textAlign:"center"}}>
                      <CardHeader title={<strong style = {{color:"#5a3e1b", paddingLeft:90}} >{translate("pgr.lbl.escdetail")}</strong>}/>
                      <CardText>
                          <RaisedButton style={{margin:'10px 0'}} label={translate("pgr.lbl.addesc")} backgroundColor={"#5a3e1b"} labelColor={white} onClick={() => {
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
  return ({defineEscalationTime : state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ["designation", "noOfHours"]
        },
        pattern: {
          current: [],
          required: [ "noOfHours"]
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
          current: ["designation", "noOfHours"],
          required: ["designation", "noOfHours"]
        },
        pattern: {
          current: ["noOfHours"],
          required: [ "noOfHours"]
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

  handleAutoCompleteKeyUp : (e) => {
    dispatch({type: "HANDLE_CHANGE", property: 'addressId', value: '', isRequired : true, pattern: ''});
  },
})

export default connect(mapStateToProps, mapDispatchToProps)(DefineEscalationTime);
