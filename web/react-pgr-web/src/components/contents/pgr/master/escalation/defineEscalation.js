import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, DropdownButton, Table, ListGroupItem} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
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

import $ from 'jquery';
import 'datatables.net-buttons/js/buttons.html5.js';// HTML 5 file export
import 'datatables.net-buttons/js/buttons.flash.js';// Flash file export
import jszip from 'jszip/dist/jszip';
import pdfMake from "pdfmake/build/pdfmake";
import pdfFonts from "pdfmake/build/vfs_fonts";
pdfMake.vfs = pdfFonts.pdfMake.vfs;

var flag = 0;
const styles = {
  headerStyle : {
    color: 'rgb(90, 62, 27)',
    fontSize : 19
  },
  marginStyle:{
    margin: '15px'
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
			  toPosition: [],
        localDesignation:[]
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
            "columnDefs": [
              { "orderable": false, "targets": 5 }
            ],
            //ordering: false,
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

    Api.commonApiPost("/hr-masters/designations/_search",{}).then(function(response) {
        self.setState({localDesignation : response.Designation});
        },function(err) {

    });
}

componentWillUpdate() {
	  $('#searchTable').dataTable().fnDestroy();
 }


    componentDidUpdate() {

         $('#searchTable').DataTable({
          dom:'<"col-md-4"l><"col-md-4"B><"col-md-4"f>rtip',
          buttons: ['excel', 'pdf'],
          bDestroy: true,
          "columnDefs": [
          { "orderable": false, "targets": 5 }
          ]
        });

    }


    componentWillUnmount(){
       $('#searchTable')
       .DataTable()
       .destroy(true);
    }

  submitForm = (e) => {

     let {setLoadingStatus, toggleSnackbarAndSetText} = this.props;
    setLoadingStatus('loading');

      e.preventDefault();
      let current = this;

      let query = {
        fromPosition:this.props.defineEscalation.fromPosition
      }

      Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_search",query,{}).then(function(response){
           setLoadingStatus('hide');

          if (response.escalationHierarchies[0] != null) {
              flag = 1;
              current.setState({
                resultList: response.escalationHierarchies,
                isSearchClicked: true,
                noData : false
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
  }


	handleDepartment = (data) => {
		 let {toggleSnackbarAndSetText, emptyProperty} = this.props;

		 var currentThis = this;
	    currentThis.setState({designations : []});
		  currentThis.setState({toPosition : []});

		let query = {
			id : data.department ? data.department :  data.target.value
		}

		  Api.commonApiPost("/hr-masters/designations/_search",query).then(function(response)
		  {
			currentThis.setState({designations : response.Designation});
		  },function(err) {
			toggleSnackbarAndSetText(true, err);
		  });
  }

  handleDesignation = (data) => {
  let {setLoadingStatus, toggleSnackbarAndSetText} = this.props;

		var current = this;
		this.setState({toPosition : []});

		let query = {
			departmentId:data.department ? data.department : this.props.defineEscalation.department,
			designationId:data.designation ? data.designation : data.target.value
		}

		  Api.commonApiPost("/hr-masters/positions/_search",query).then(function(response) {
			setLoadingStatus('hide');
			current.setState({toPosition : response.Position});
		  },function(err) {
			  toggleSnackbarAndSetText(true, err.message);
			  setLoadingStatus('hide');
		  });
  }


  updateEscalation = () => {

    let {setLoadingStatus, toggleSnackbarAndSetText, toggleDailogAndSetText, emptyProperty} = this.props;
    setLoadingStatus('loading');

		 var current = this
    var body = {
      escalationHierarchy: [ {
			serviceCode : this.props.defineEscalation.serviceCode,
			tenantId : localStorage.getItem("tenantId"),
			fromPosition : this.props.defineEscalation.fromPosition,
			toPosition : this.props.defineEscalation.toPosition,
			department :this.props.defineEscalation.department,
			designation :this.props.defineEscalation.designation
			  }]
    }

    Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_update/",{},body).then(function(response){

        let msg = `${translate('pgr.lbl.escalations')} ${translate('core.lbl.updatedsuccessful')}`
		    toggleDailogAndSetText(true, msg);
        let query = {
        fromPosition:current.props.defineEscalation.fromPosition
        }
		emptyProperty("serviceCode");
		emptyProperty("department");
		emptyProperty("designation");
		emptyProperty("toPosition");
                Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_search",query,{}).then(function(response){
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
                    setLoadingStatus('hide');
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

  addEscalation = () => {

    let {setLoadingStatus, toggleDailogAndSetText, toggleSnackbarAndSetText, emptyProperty} = this.props;
    setLoadingStatus('loading');

    var current = this
    var body = {
      escalationHierarchy: [ {
				serviceCode : this.props.defineEscalation.serviceCode,
				tenantId : "default",
				fromPosition : this.props.defineEscalation.fromPosition,
				toPosition : this.props.defineEscalation.toPosition,
        department :this.props.defineEscalation.department,
        designation :this.props.defineEscalation.designation
			  }]
    }

    Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_create",{},body).then(function(response){
      let msg = `${translate('pgr.lbl.escalations')} ${translate('core.lbl.createdsuccessful')}`
    toggleDailogAndSetText(true, msg);
        let query = {
        fromPosition:current.props.defineEscalation.fromPosition
		}

		emptyProperty("serviceCode");
		emptyProperty("department");
		emptyProperty("designation");
		emptyProperty("toPosition");

                Api.commonApiPost("/pgr-master/escalation-hierarchy/v1/_search",query,{}).then(function(response){
                    if (response.escalationHierarchies[0] != null) {
                        flag = 1;
                        current.setState({
                          resultList: response.escalationHierarchies,
                          isSearchClicked: true,
                          noData: false
                        })

                    } else {
                      current.setState({
                        noData: true,
                      })
                    }
                    setLoadingStatus('hide');
                }).catch((error)=>{
                     setLoadingStatus('hide');
						toggleSnackbarAndSetText(true, error.message);
                })

    }).catch((error)=>{
		  setLoadingStatus('hide');
         toggleSnackbarAndSetText(true, error.message);
    })
  }

  editObject = (index) => {
      let {setLoadingStatus} = this.props;
      setLoadingStatus('loading');
      this.props.setForm(this.state.resultList[index])

      this.handleDepartment(this.state.resultList[index]);

      this.handleDesignation(this.state.resultList[index]);
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

      let {
        isFormValid,
        defineEscalation,
        fieldErrors,
        handleChange,
        handleAutoCompleteKeyUp
      } = this.props;

      let {submitForm, localHandleChange, addEscalation, updateEscalation, editObject} = this;


      let {isSearchClicked, resultList, escalationForm,  editIndex} = this.state;

      const renderBody = function() {
      	  if(resultList && resultList.length)
      		return resultList.map(function(val, i) {
      			return (
      				<tr key={i}>
                <td>{getNameById(current.state.positionSource,val.fromPosition)}</td>
      					<td>{val.serviceCode ? getNameByServiceCode(current.state.grievanceType ,val.serviceCode) : ""}</td>
      					<td>{getNameById(current.state.departments,val.department)}</td>
      					<td>{getNameById(current.state.localDesignation,val.designation)}</td>
                <td>{getNameById(current.state.positionSource,val.toPosition)}</td>
                <td><RaisedButton primary={true} style={{margin:'15px 5px'}} label={translate("pgr.lbl.edit")} onClick={() => {
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
   	        <Card style={styles.marginStyle}>
              <CardText>
                  <Row>
                    <Col xs={12} sm={4} md={3} lg={3}>
                        <TextField
                            fullWidth={true}
                            floatingLabelText={translate('pgr.lbl.fromposition')+" *"}
                            value={defineEscalation.fromPosition ? getNameById(current.state.positionSource, defineEscalation.fromPosition) : ""}
                            id="name"
                            disabled={true}
                        />
                    </Col>
                    <Col xs={12} sm={4} md={3} lg={3}>
                        <SelectField
                           floatingLabelText={translate('pgr.lbl.grievance.type')+ " *"}
                           fullWidth={true}
                           value={defineEscalation.serviceCode ? defineEscalation.serviceCode : ""}
						   disabled={editIndex<0 ? false : true}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             handleChange(e, "serviceCode", true, "");
                            }}
                          >
						  {current.state.grievanceType && current.state.grievanceType.map((item, index)=>{
							  return(
								<MenuItem value={item.serviceCode} key={index} primaryText={item.serviceName} />
							  )
						  })}
                        </SelectField>
                    </Col>
                    <Col xs={12} sm={4} md={3} lg={3}>
                        <SelectField
                           floatingLabelText={translate('core.lbl.department') + ' *'}
                           fullWidth={true}
                           value={defineEscalation.department ? defineEscalation.department : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             handleChange(e, "department", true, "");
                             current.handleDepartment(e);
                            }}
                         >
							 {current.state.departments && current.state.departments.map((item, index)=>{
								 return(
									<MenuItem value={item.id} key={index} primaryText={item.name} />
								 )
							})}

                        </SelectField>
                    </Col>
                    <Col xs={12} sm={4} md={3} lg={3}>
                        <SelectField
                           floatingLabelText={translate('pgr.lbl.designation')+" *"}
                           fullWidth={true}
                           value={defineEscalation.designation ? defineEscalation.designation : ""}
                           onChange= {(e, index ,value) => {
                             var e = {
                               target: {
                                 value: value
                               }
                             };
                             handleChange(e, "designation", true, "");
                             current.handleDesignation(e);
                            }}
                         >
                           {current.state.designations && current.state.designations.map((item, index)=>{
								 return(
									<MenuItem value={item.id} key={index} primaryText={item.name} />
								 )
							})}
                        </SelectField>
                    </Col>
                    <Col xs={12} sm={4} md={3} lg={3}>
                        <SelectField
                           floatingLabelText={translate('core.position.to')+" *"}
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
                        {editIndex<0 && <RaisedButton primary={true} style={{margin:'15px 5px'}} disabled={!isFormValid} label={translate("pgr.lbl.add")} primary={true} onClick={() => {
                          addEscalation();
                        }}/>}
                        {editIndex>=0 && <RaisedButton primary={true} style={{margin:'15px 5px'}} disabled={!isFormValid} label={translate("pgr.lbl.update")} primary={true} onClick={() => {

                          updateEscalation();
                        }}/>}
                    </Col>
                  </Row>
              </CardText>
   	          <CardText>
   		        <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive className="table-striped">
   		          <thead >
   		            <tr>
						<th>{translate('pgr.lbl.fromposition')}</th>
						<th>{translate('pgr.lbl.grievance.type')}</th>
						<th>{translate('core.lbl.department')}</th>
						<th>{translate('pgr.lbl.designation')}</th>
						<th>{translate('core.position.to')}</th>
						<th>{translate('pgr.lbl.actions')}</th>
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
          <Card style={styles.marginStyle}>
              <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {translate('pgr.lbl.escalation')} < /div>} />
              <CardText>
                  <Grid>
                      <Row>
                          <Col xs={12} sm={4} md={3} lg={3}>
                                <AutoComplete
                                  floatingLabelText={translate('pgr.lbl.position')+" *"}
                                  fullWidth={true}
                                  listStyle={{ maxHeight: 200, overflow: 'auto' }}
                                  filter={function filter(searchText, key) {
                                            return key.toLowerCase().includes(searchText.toLowerCase());
                                         }}
                                  dataSource={this.state.positionSource}
                                  dataSourceConfig={this.state.dataSourceConfig}
                                  value={defineEscalation.fromPosition ? defineEscalation.fromPosition : ""}
                                  ref="fromPosition"
                                  onNewRequest={(chosenRequest, index) => {
                                    if(index === -1){
                                      this.refs['fromPosition'].setState({searchText:''});
                                    }else{
                                      var e = {
                                        target: {
                                          value: chosenRequest.id
                                        }
                                      };
                                      handleChange(e, "fromPosition", true, "");
                                    }
          	                       }}
                                />
                          </Col>
                      </Row>
                  </Grid>
              </CardText>
          </Card>
          <div style={{textAlign:'center'}}>
              <RaisedButton primary={true} style={{margin:'15px 5px'}} type="submit" disabled={defineEscalation.fromPosition ? false : true} label={translate('core.lbl.search')} />
          </div>
          {this.state.noData &&
            <Card className="text-center" style={styles.marginStyle}>
              <CardHeader title={<strong style = {{color:"#5a3e1b", paddingLeft:90}} > {translate('pgr.lbl.escalationmessage')} </strong>}/>
              <CardText>
                  <RaisedButton primary={true} style={{margin:'10px 0'}} label={translate('pgr.lbl.addesc')} onClick={() => {
                    this.setState({
                      isSearchClicked: true,
                      noData:false
                    })
                  }}/>
             </CardText>
          </Card>
          }
          {this.state.noData ? '' : viewTable()}
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
          required: ["fromPosition", "serviceCode", "department", "designation", "toPosition"]
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
          current: ["fromPosition", "serviceCode", "department", "designation", "toPosition"] ,
          required:["fromPosition", "serviceCode", "department", "designation", "toPosition"]
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  emptyProperty: (property) => {
	  dispatch({
		  type: "EMPTY_PROPERTY",
		  property,
		  isFormValid:false,
		  validationData: {
        required: {
          current: ["fromPosition", ],
          required: ["fromPosition", "serviceCode", "department", "designation", "toPosition"]
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

export default connect(mapStateToProps, mapDispatchToProps)(DefineEscalation);
