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

class BulkEscalationGeneration extends Component {
    constructor(props) {
      super(props)
      this.state = {
              positionSource: [],
              dataSourceConfig : {
                text: 'name',
                value: 'id',
              },
              serviceCode:[],
              searchResult:[]
            };
    }

    componentWillMount() {
      let{initForm} = this.props;
      initForm()

       $('#searchTable').DataTable({
         dom: 'lBfrtip',
         buttons: [],
          bDestroy: true,
          language: {
             "emptyTable": "No Records"
          }
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
              serviceCode: response.complaintTypes
            })
        }, function(err) {
          self.setState({
              serviceCode: []
            })
        });
    }

    componentWillUnmount(){
       $('#searchTable')
       .DataTable()
       .destroy(true);
    }

	componentWillUpdate() {
	  $('#searchTable').dataTable().fnDestroy();
	}
	
  componentDidUpdate() {
       $('#searchTable').DataTable({
         dom: 'lBfrtip',
         buttons: [],
          bDestroy: true,
          language: {
             "emptyTable": "No Records"
          }
     });
  }
  
  updateToPosition = () => {
	  
	let {setLoadingStatus, toggleSnackbarAndSetText, toggleDailogAndSetText, bulkEscalationGeneration} = this.props;  
	
	var current = this;
    
	var body = {
      escalationHierarchy: []
    }
	
			setLoadingStatus("loading");
	  
	  if(bulkEscalationGeneration.serviceCode) {
		  
		for(let i = 0;i<bulkEscalationGeneration.serviceCode.length;i++) {
			var Data = {
				serviceCode : bulkEscalationGeneration.serviceCode[i],
				tenantId : "default",
				fromPosition : bulkEscalationGeneration.fromPosition,
				toPosition : bulkEscalationGeneration.toPosition,
			}
			body.escalationHierarchy.push(Data);
		}
	  }

    Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_update/",{},body).then(function(response){
		setLoadingStatus("hide");
		toggleDailogAndSetText(true, "To Position Updated Successfully");
          let query = {
			fromPosition:bulkEscalationGeneration.fromPosition,
			serviceCode : bulkEscalationGeneration.serviceCode
		}
    
                Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_search",query,{}).then(function(response){
                    setLoadingStatus('hide');
                    if (response.escalationHierarchies[0] != null) {
                        flag = 1;
                        current.setState({
                          searchResult: response.escalationHierarchies,
                          isSearchClicked: true
                        })

                    } else {
                      current.setState({
                        noData: true,
                      })
                    }
                }).catch((error)=>{
					 setLoadingStatus('hide');
					toggleSnackbarAndSetText(true, error.message);
                })

              current.setState((prevState)=>{
                  prevState.editIndex=-1
                })
    }).catch((error)=>{
		setLoadingStatus('hide');
		toggleSnackbarAndSetText(true, error.message);
    })
  }

  submitForm = (e) => {
      e.preventDefault()

      let self = this;

      let {setLoadingStatus, toggleSnackbarAndSetText} = this.props;

      setLoadingStatus("loading");

      let searchSet = {
		  fromPosition : this.props.bulkEscalationGeneration.fromPosition,
		  serviceCode: this.props.bulkEscalationGeneration.serviceCode
	  }
      
        Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_search", searchSet).then(function(response) {
            setLoadingStatus("hide");
           flag = 1;
          self.setState({
            searchResult: response.escalationHierarchies,
            isSearchClicked: true
          }); 
        }, function(err) {
            setLoadingStatus('hide');
			toggleSnackbarAndSetText(true, err.message)
        }); 
  }

    render() {

      let {
        isFormValid,
        bulkEscalationGeneration,
        fieldErrors,
        handleChange,
        handleAutoCompleteKeyUp
      } = this.props;

      let self = this;

      let {submitForm} = this;

      let {isSearchClicked, searchResult} = this.state;

      console.log(bulkEscalationGeneration);

      const renderBody = function() {
      	  if(searchResult && searchResult.length)
      		return searchResult.map(function(val, i) {
      			return (
      				<tr key={i}>
      					<td>{getNameByServiceCode(self.state.serviceCode, val.serviceCode)}</td>
      					<td>{getNameById(self.state.positionSource, val.fromPosition)}</td>
      					<td>{getNameById(self.state.positionSource, val.toPosition)}</td>
      				</tr>
      			)
      		})
      }

      const viewTable = function() {
      	  if(isSearchClicked)
      		return (
   	        <Card>
   	          <CardHeader title={<strong style = {{color:"#5a3e1b"}} >{translate('pgr.lbl.result')}</strong>}/>
   	          <CardText>
   		        <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
   		          <thead style={{backgroundColor:"#f2851f",color:"white"}}>
   		            <tr>
   		              <th>{translate('pgr.lbl.grievance.type')}</th>
   		              <th>{translate('pgr.lbl.fromposition')}</th>
   		              <th>{translate('pgr.lbl.toposition')}</th>
   		            </tr>
   		          </thead>
   		          <tbody>
   		          	{renderBody()}
   		          </tbody>
   		        </Table>
   		       </CardText>
			   <div style={{textAlign:'center'}}>
				  <RaisedButton style={{margin:'15px 5px'}} type="button" label={translate('core.lbl.save')} primary={true} onClick={()=>{
					  self.updateToPosition();
				  }}/>
			   </div>
   		    </Card>
   		)
      }


      return(<div className="bulkEscalationGeneration">
      <form autoComplete="off" onSubmit={(e) => {submitForm(e)}}>
          <Card  style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {translate('pgr.lbl.bueg')} < /div>} />
              <CardText>
                  <Card>
                      <CardText>
                          <Grid>
                              <Row>
                                  <Col xs={12} md={4}>
                                        <AutoComplete
                                          floatingLabelText={translate('pgr.lbl.fromposition')+" *"}
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
                                           floatingLabelText={translate('pgr.lbl.grievance.type')+" *"}
                                           fullWidth={true}
                                           value={bulkEscalationGeneration.serviceCode ? bulkEscalationGeneration.serviceCode : ""}
                                           onChange= {(e, index ,values) => {
                                             var e = {
                                               target: {
                                                 value: values
                                               }
                                             };
                                             handleChange(e, "serviceCode", true, "");
                                            }}
                                         >
                                         {this.state.serviceCode && this.state.serviceCode.map((item, index) => (
                                                   <MenuItem
                                                     value={item.serviceCode}
                                                     key={index}
                                                     insetChildren={true}
                                                     primaryText={item.serviceName}
                                                     checked={bulkEscalationGeneration.serviceCode && bulkEscalationGeneration.serviceCode.indexOf(item.serviceCode) > -1}
                                                   />
                                          ))}
                                          </SelectField>
                                  </Col>
                                  <Col xs={12} md={4}>
                                      <AutoComplete
                                          floatingLabelText={translate('pgr.lbl.toposition')+" *"}
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
                      <RaisedButton style={{margin:'15px 5px'}} type="submit" disabled={!isFormValid} label={translate('core.lbl.search')} primary={true}/>
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
          required: ["fromPosition","grievanceType", "toPosition"]
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
  },

   setLoadingStatus: (loadingStatus) => {
      dispatch({type: "SET_LOADING_STATUS", loadingStatus});
    },
	 toggleDailogAndSetText: (dailogState,msg) => {
      dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState, msg});
    },
    toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
      dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
    }, 
})

export default connect(mapStateToProps, mapDispatchToProps)(BulkEscalationGeneration);
