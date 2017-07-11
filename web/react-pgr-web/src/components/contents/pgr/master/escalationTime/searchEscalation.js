import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, DropdownButton, Table ,ListGroup, ListGroupItem} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import {brown500, red500,white,orange800} from 'material-ui/styles/colors';
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
              if(object[i].serviceName) {
                return object[i].serviceName;
              } else {
                 return object[i].name;
              }
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


class SearchEscalation extends Component {
    constructor(props) {
      super(props)
      this.state = {
              designationSource:[],
              grievanceTypeSource:[],
              designationDataSourceConfig : {
                text: 'name',
                value: 'id',
              },
              serviceDataSourceConfig: {
                text: 'serviceName',
                value: 'id',
              },
              isSearchClicked: false,
              resultList: [],
            };
    }

    componentWillMount() {

        let{initForm, setLoadingStatus} = this.props;

      setLoadingStatus('loading');

      $('#searchTable').DataTable({
         dom: 'lBfrtip',
         buttons: [],
          bDestroy: true,
          language: {
             "emptyTable": "No Records"
          }
    });

      initForm()

    }

    componentDidMount() {
      let self = this;

      let{setLoadingStatus} = this.props;

      Api.commonApiPost("/pgr/services/v1/_search", {type: "all"}).then(function(response) {
        setLoadingStatus('hide');
          console.log(response);
          self.setState({
            grievanceTypeSource: response.complaintTypes
          })
      }, function(err) {
        self.setState({
            grievanceTypeSource: []
          })
      });

      Api.commonApiPost("/hr-masters/designations/_search").then(function(response) {
          console.log(response);
          self.setState({
            designationSource: response.Designation
          })
      }, function(err) {
        self.setState({
            designationSource: []
          })
      });

    }


 componentWillUnmount(){
     $('#searchTable')
     .DataTable()
     .destroy(true);
 };
 
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

  submitForm = (e) => {
       let{setLoadingStatus} = this.props;
       setLoadingStatus('loading');
      e.preventDefault();

      let current = this;

      let {searchEscalation} = this.props;

      var query = {}

      if(searchEscalation.designation && searchEscalation.serviceType){

        query = {
          designation:searchEscalation.designation.id,
          serviceId:searchEscalation.serviceType.id
         }

      } else if(searchEscalation.serviceType){
         
           query = {
          serviceId:searchEscalation.serviceType.id
           }
      } else if(searchEscalation.designation) {
      
        let query = {
          designation:searchEscalation.designation.id
        }
      } else {}

      console.log(query)

      Api.commonApiPost("/workflow/escalation-hours/v1/_search",query,{}).then(function(response){
          setLoadingStatus('hide');
          console.log(response);
          flag = 1;
          current.setState({
            resultList: response.EscalationTimeType,
            isSearchClicked: true
          })
      }).catch((error)=>{
          console.log(error);
      })

  }

    render() {
		
      var current = this;

      let {
        isFormValid,
        searchEscalation,
        fieldErrors,
        handleChange,
        handleAutoCompleteKeyUp
      } = this.props;

      let {submitForm} = this;

      let {isSearchClicked, resultList} = this.state;

      const renderBody = function() {
      	  if(resultList && resultList.length)
      		return resultList.map(function(val, i) {
      			return (
      				<tr key={i}>
      					<td>{getNameById(current.state.grievanceTypeSource,val.id)}</td>
      					<td>{getNameById(current.state.designationSource,val.designation)}</td>
      					<td>{val.noOfHours}</td>
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
   		         <thead>
   		            <tr>
                    <th>{translate("pgr.lbl.grievance.type")}</th>
                    <th>{translate("pgr.lbl.designation")}</th>
                    <th>{translate("pgr.noof.hours")}</th>
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

      return(<div className="searchEscalation">
      <form autoComplete="off" onSubmit={(e) => {submitForm(e)}}>
          <Card  style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} >{translate('pgr.lbl.escalationtime')}< /div>} />
              <CardText>
                  <Card>
                      <CardText>
                          <Grid>
                              <Row>
                                  <Col xs={12} md={6}>
                                        <AutoComplete
                                          floatingLabelText={translate("pgr.lbl.grievance.type")}
                                          fullWidth={true}
                                          filter={function filter(searchText, key) {
                                                    return key.toLowerCase().includes(searchText.toLowerCase());
                                                 }}
                                          dataSource={this.state.grievanceTypeSource}
                                          dataSourceConfig={this.state.serviceDataSourceConfig}
                                          onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "grievanceType")}}
                                          value={searchEscalation.serviceType ? searchEscalation.serviceType : ""}
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
                                  <Col xs={12} md={6}>
                                        <AutoComplete
                                          floatingLabelText={translate("pgr.lbl.designation")}
                                          fullWidth={true}
                                          filter={function filter(searchText, key) {
                                                    return key.toLowerCase().includes(searchText.toLowerCase());
                                                 }}
                                          dataSource={this.state.designationSource}
                                          dataSourceConfig={this.state.designationDataSourceConfig}
                                          onKeyUp={(e) => {handleAutoCompleteKeyUp(e, "designation")}}
                                          value={searchEscalation.designation ? searchEscalation.designation : ""}
                                          onNewRequest={(chosenRequest, index) => {
                  	                        var e = {
                  	                          target: {
                  	                            value: chosenRequest
                  	                          }
                  	                        };
                  	                        handleChange(e, "designation", false, "");
                  	                       }}
                                        />
                                  </Col>
                              </Row>
                          </Grid>
                      </CardText>
                  </Card>
                  <div style={{textAlign:'center'}}>

                      <RaisedButton style={{margin:'15px 5px'}} type="submit"  label={translate("core.lbl.search")} primary={true}/>

                  </div>
                  {viewTable()}
              </CardText>
          </Card>
          </form>
      </div>)
    }
}


const mapStateToProps = state => {
  return ({searchEscalation : state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

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
    }
})

export default connect(mapStateToProps, mapDispatchToProps)(SearchEscalation);
