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

const getNameByServiceCode = function(object, serviceCode, property = "") {
  if (serviceCode == "" || serviceCode == null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property == "") {
            if (object[i].serviceCode == serviceCode) {
                return object[i].serviceName;
            }
        } else {
            if (object[i].hasOwnProperty(property)) {
                if (object[i].serviceCode == serviceCode) {
                    return object[i][property];
                }
            } else {
                return "";
            }
        }
    }
    return "";
}

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
              },
			  editIndex: -1,
			  grievanceType:[],
			  designations:[],
			  departments:[],
			  toPosition: []
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
	  
	  
		Api.commonApiPost("/pgr/services/v1/_search", {type: "all"}).then(function(response) {
			self.setState({
			  grievanceType: response.complaintTypes
			})
		}, function(err) {
		  self.setState({
			  grievanceType: []
			})
		});
		
		
		Api.commonApiPost("/egov-common-masters/departments/_search", {}).then(function(response) {
			self.setState({
			  departments: response.Department
			})
        }, function(err) {
          self.setState({
              departments: []
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
        id:this.props.defineEscalation.fromPosition
      }
	  
	  console.log(query);

      Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_search",query,{}).then(function(response){
          console.log(response);

          if (response.escalationHierarchies[0] != null) {
              flag = 1;
              current.setState({
                resultList: response.escalationHierarchies,
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
  
	handleDepartment = (e) => {
		
		  var currentThis = this;
	    currentThis.setState({designation : []});
		currentThis.setState({toPosition : []});
		
		let query = {
			id:e.target.value
		}
	
		  Api.commonApiPost("/hr-masters/designations/_search",query).then(function(response)
		  {console.log(response);
			currentThis.setState({designations : response.Designation});
		  },function(err) {
			console.log(err);
		  });	
  }
  
  	handleDesignation = (e) => {
		
		var currentThis = this;
		currentThis.setState({toPosition : []});
		
		let query = {
			departmentId:this.props.defineEscalation.department, 
			designationId:e.target.value
		}
	
		  Api.commonApiPost("/hr-masters/positions/_search",query).then(function(response)
		  {console.log(response);
			currentThis.setState({toPosition : response.Position});
		  },function(err) {
			console.log(err);
		  });	
  }
  
  
  updateEscalation = () => {
		 var current = this
    var body = {
      escalationHierarchy: [ {
				serviceCode : this.props.defineEscalation.grievanceType,
				tenantId : "default",
				fromPosition : this.props.defineEscalation.fromPosition,
				toPosition : this.props.defineEscalation.toPosition
			  }]
    }
	
	//var idd = this.props.defineEscalation.fromPosition;

    Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_update/",{},body).then(function(response){
        console.log(response);
        current.setState({
          resultList:[
            ...current.state.resultList,
            current.props.defineEscalation
          ]
      })
    }).catch((error)=>{
        console.log(error);
    })
  }

  addEscalation = () => {
    var current = this
    var body = {
      escalationHierarchy: [ {
				serviceCode : this.props.defineEscalation.grievanceType,
				tenantId : "default",
				fromPosition : this.props.defineEscalation.fromPosition,
				toPosition : this.props.defineEscalation.toPosition
			  }]
    }

    Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_create",{},body).then(function(response){
        console.log(response);
        current.setState({
          resultList:[
            ...current.state.resultList,
            current.props.defineEscalation
          ]
      })
    }).catch((error)=>{
        console.log(error);
    })
  }
  
  editObject = (index) => {
      this.props.setForm(this.state.resultList[index])
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


    render() {

      var current = this;
	  
	  console.log(this.state.toPosition);

      let {
        isFormValid,
        defineEscalation,
        fieldErrors,
        handleChange,
        handleAutoCompleteKeyUp
      } = this.props;

      let {submitForm, localHandleChange, addEscalation, updateEscalation, editObject} = this;

      console.log(this.state.resultList);

      let {isSearchClicked, resultList, escalationForm,  editIndex} = this.state;

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
                <td><RaisedButton style={{margin:'15px 5px'}} label={translate("pgr.lbl.update")} onClick={() => {
					editObject(i);
                    current.setState({editIndex:i})
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
                            value={defineEscalation.fromPosition ? getNameById(current.state.positionSource, defineEscalation.fromPosition) : ""}
                            id="name"
                            disabled={true}
                        />
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText="Grievance Type"
                           fullWidth={true}
                           value={defineEscalation.grievanceType ? defineEscalation.grievanceType : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             handleChange(e, "grievanceType", true, "");
                            }}
                          >
						  {current.state.grievanceType && current.state.grievanceType.map((item, index)=>{
							  return(
								<MenuItem value={item.serviceCode} key={index} primaryText={item.serviceName} />
							  )
						  })}
                        </SelectField>
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText="Department"
                           fullWidth={true}
                           value={defineEscalation.department ? defineEscalation.department : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
							 current.handleDepartment(e);
                             handleChange(e, "department", true, "");
                            }}
                         >
							 {current.state.departments && current.state.departments.map((item, index)=>{
								 return(
									<MenuItem value={item.id} key={index} primaryText={item.name} />
								 )
							})}
                           
                        </SelectField>
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText="Designation"
                           fullWidth={true}
                           value={defineEscalation.designation ? defineEscalation.designation : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
							 current.handleDesignation(e);
                             handleChange(e, "designation", true, "");
                            }}
                         >
                           {current.state.designations && current.state.designations.map((item, index)=>{
								 return(
									<MenuItem value={item.id} key={index} primaryText={item.name} />
								 )
							})}
                        </SelectField>
                    </Col>
                    <Col xs={12} md={3} sm={6}>
                        <SelectField
                           floatingLabelText="To Position"
                           fullWidth={true}
                           value={defineEscalation.toPosition ?  defineEscalation.toPosition  : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             handleChange(e, "toPosition", true, "");
                            }}
                         >
                            {current.state.toPosition && current.state.toPosition.map((item, index)=>{
								 return(
									<MenuItem value={item.id} key={index} primaryText={item.name ?  item.name: getNameById(current.state.toPosition, defineEscalation.toPosition)} />
								 )
							})}
                        </SelectField>
                    </Col>
                    <div className="clearfix"></div>
					<Col xs={12} md={12} style={{textAlign:"center"}}>
                        {editIndex<0 && <RaisedButton style={{margin:'15px 5px'}} disabled={!isFormValid} label={translate("pgr.lbl.add")} primary={true} onClick={() => {
                          addEscalation();
                        }}/>}
                        {editIndex>=0 && <RaisedButton style={{margin:'15px 5px'}} disabled={!isFormValid} label={translate("pgr.lbl.update")} primary={true} onClick={() => {

                          updateEscalation();
                        }}/>}
                    </Col>
                  </Row>
              </CardText>
   	          <CardText>
   		        <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
   		          <thead >
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
                                          value={defineEscalation.fromPosition ? defineEscalation.fromPosition : ""}
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
                              </Row>
                          </Grid>
                      </CardText>
                  </Card>
                  <div style={{textAlign:'center'}}>

                      <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={defineEscalation.fromPosition ? false: true} label="Search" backgroundColor={"#5a3e1b"} labelColor={white}/>

                  </div>
                  {this.state.noData &&
                    <Card style = {{textAlign:"center"}}>
                      <CardHeader title={<strong style = {{color:"#5a3e1b", paddingLeft:90}} > There is no escalation details available for the selected position. </strong>}/>
                      <CardText>
                          <RaisedButton style={{margin:'10px 0'}} label="Add Escalation Details" labelColor={white} onClick={() => {
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
          required: ["fromPosition", "grievanceType", "department", "designation", "toPosition"]
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
  },
     setLoadingStatus: (loadingStatus) => {
      dispatch({type: "SET_LOADING_STATUS", loadingStatus});
    },

    toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
      dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
    },
})

export default connect(mapStateToProps, mapDispatchToProps)(DefineEscalation);
