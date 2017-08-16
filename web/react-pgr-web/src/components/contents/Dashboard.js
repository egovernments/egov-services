import React, { Component } from 'react';
import {connect} from 'react-redux';
import PropTypes from 'prop-types';
import {Link, Route} from 'react-router-dom';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import RaisedButton from 'material-ui/RaisedButton';
import TextField from 'material-ui/TextField';
import DataTable from '../common/Table';
import {Tabs, Tab} from 'material-ui/Tabs';
import {translate} from '../common/common';
import SwipeableViews from 'react-swipeable-views';
import {ListItem} from 'material-ui/List';
import IconButton from 'material-ui/IconButton';
import FontIcon from 'material-ui/FontIcon';

//api import
import Api from "../../api/api";

const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');

const buttons = require('datatables.net-buttons-bs');
const constants = require('../common/constants');

require('datatables.net-buttons/js/buttons.colVis.js'); // Column visibility
require('datatables.net-buttons/js/buttons.html5.js'); // HTML 5 file export
require('datatables.net-buttons/js/buttons.flash.js'); // Flash file export
require('datatables.net-buttons/js/buttons.print.js'); // Print view button

const styles = {
  headline: {
    fontSize: 24,
    paddingTop: 16,
    marginBottom: 12,
    fontWeight: 400,
  },
  headerStyle : {
    fontSize : 19
  },
  slide: {
    padding: 10,
  },
  status:{
    fontSize:14,
    background:"#5f5c62",
    display:"inline-block",
    padding:"4px 8px",
    borderRadius:4,
    color:"#fff"

  }
};

class Dashboard extends Component {
  constructor(props) {
    super(props);
    this.state = {
      slideIndex: 0,
      serviceRequests: [],
      citizenServices:[],
      selectedServiceCode:"",
      selectedServiceName:constants.LABEL_SERVICES,
	    localArray:[],
      hasData:false,
      workflowResult: {}
    };
    this.onClickServiceGroup=this.onClickServiceGroup.bind(this);
  }

  componentWillMount() {

	 $('#searchTable').DataTable({
         dom: 'lBfrtip',
         buttons: [],
          bDestroy: true,
          language: {
             "emptyTable": "No Records"
          }
    });

	  let { setLoadingStatus} = this.props;
     setLoadingStatus("loading");

    let current = this;
    let {currentUser}=this.props;

    if(currentUser.type === constants.ROLE_CITIZEN) {
      Promise.all([
          Api.commonApiPost("/pgr/seva/v1/_search",{userId:currentUser.id},{}),
          Api.commonApiPost("/pgr-master/serviceGroup/v1/_search",{keywords:constants.CITIZEN_SERVICES_KEYWORD},{})
      ])
      .then((responses)=>{
        //if any error occurs
        if(!responses || responses.length ===0 || !responses[0] || !responses[1]){
          current.setState({
            serviceRequests: [],
            citizenServices:[],
            localArray:[],
             hasData:false
          });
          return;
        }

        //inbox items
        let inboxResponse = responses[0];

        for(var i=0; i<inboxResponse.serviceRequests.length; i++) {
          var d1 = inboxResponse.serviceRequests[i].requestedDatetime.split(" ")[0].split("-");
          var d11 = inboxResponse.serviceRequests[i].requestedDatetime.split(" ")[1].split(":");
          inboxResponse.serviceRequests[i].clientTime = new Date(d1[2], d1[1]-1, d1[0], d11[0], d11[1], d11[2]).getTime();
        }

        inboxResponse.serviceRequests.sort(function(s1, s2) {
            var d1 = s1.requestedDatetime.split(" ")[0].split("-");
            var d11 = s1.requestedDatetime.split(" ")[1].split(":");
            var d2 = s2.requestedDatetime.split(" ")[0].split("-");
            var d22 = s2.requestedDatetime.split(" ")[1].split(":");
            if(new Date(d1[2], d1[1]-1, d1[0], d11[0], d11[1], d11[2]).getTime() < new Date(d2[2], d2[1]-1, d2[0], d22[0], d22[1], d22[2]).getTime()) {
              return 1;
            } else if(new Date(d1[2], d1[1]-1, d1[0], d11[0], d11[1], d11[2]).getTime() > new Date(d2[2], d2[1]-1, d2[0], d22[0], d22[1], d22[2]).getTime()) {
              return -1;
            }
            return 0;
        });

        //citizen services
        let citizenServices=responses[1].ServiceGroups;

        current.props.setLoadingStatus('hide');

        current.setState({
          serviceRequests: inboxResponse.serviceRequests,
          localArray: inboxResponse.serviceRequests,
          citizenServices,
          hasData:true
        });

      }).catch(function(err){
         console.log('error', err);
      });

    } else {
      Api.commonApiPost("/hr-employee/employees/_search", {id: currentUser.id}, {}).then(function(res) {

        if(res && res.Employee && res.Employee[0] && res.Employee[0].assignments && res.Employee[0].assignments[0] && res.Employee[0].assignments[0].position) {
          /*Api.commonApiPost("/pgr/seva/v1/_search",{positionId:res.Employee[0].assignments[0].position, status: "REGISTERED,FORWARDED,PROCESSING,NOTCOMPLETED,REOPENED"},{}).then(function(response){
                for(var i=0; i<response.serviceRequests.length; i++) {
                  var d1 = response.serviceRequests[i].requestedDatetime.split(" ")[0].split("-");
                  var d11 = response.serviceRequests[i].requestedDatetime.split(" ")[1].split(":");
                  response.serviceRequests[i].clientTime = new Date(d1[2], d1[1]-1, d1[0], d11[0], d11[1], d11[2]).getTime();
                }

                response.serviceRequests.sort(function(s1, s2) {
                  var d1 = s1.requestedDatetime.split(" ")[0].split("-");
                  var d11 = s1.requestedDatetime.split(" ")[1].split(":");
                  var d2 = s2.requestedDatetime.split(" ")[0].split("-");
                  var d22 = s2.requestedDatetime.split(" ")[1].split(":");
                  if(new Date(d1[2], d1[1]-1, d1[0], d11[0], d11[1], d11[2]).getTime() < new Date(d2[2], d2[1]-1, d2[0], d22[0], d22[1], d22[2]).getTime()) {
                    return 1;
                  } else if(new Date(d1[2], d1[1]-1, d1[0], d11[0], d11[1], d11[2]).getTime() > new Date(d2[2], d2[1]-1, d2[0], d22[0], d22[1], d22[2]).getTime()) {
                    return -1;
                  }
                  return 0;
                })

                current.setState({
                  serviceRequests: response.serviceRequests,
                  localArray:response.serviceRequests,
                   hasData:true
                });
            }).catch((error)=>{
                current.setState({
                  serviceRequests: [],
                  localArray:[],
                   hasData:false
                });
				        current.props.setLoadingStatus('hide');
            })*/

            var bodyReq = {
              tenantId: localStorage.getItem("tenantId") || "default",
              "reportName": "CommonInbox",
              "searchParams": [
                {
                  "name": "positionId",
                  "input": res.Employee[0].assignments[0].position
                }
              ]
            };
            Api.commonApiPost("/pgr-master/report/_get", {}, bodyReq).then(function(res) {
              current.setState({
                workflowResult: res,
                hasData: true
              });
            }, function(err) {
              current.props.setLoadingStatus('hide');
              current.setState({
                workflowResult: {},
                hasData: true
              });
            })
        } else {
			    current.props.setLoadingStatus('hide');
          current.props.toggleSnackbarAndSetText(true, "Something went wrong. Please try again later.");
        }
      })
    }
  };



 componentWillUnmount(){
     $('#searchTable')
     .DataTable()
     .destroy(true);
 };


   componentDidUpdate() {
    let self = this;
    if(this.state.hasData){
       $('#searchTable').DataTable({
        "initComplete": function(settings, json) {
            self.props.setLoadingStatus('hide');
         },
         dom: 'lBfrtip',
         buttons: [],
          bDestroy: true,
          language: {
             "emptyTable": "No Records"
          }
     });
    }
  }

  localHandleChange = (string) => {
	 var b = this.state.serviceRequests.filter(function(item, index, array){
		  if(JSON.stringify(item).toLowerCase().match(string.toLowerCase())){
			  return item;
		  }
	  })
	  this.setState({localArray:b});
  }

  filterCitizenServices = (name) =>{
     this.setState({servicesFilter:name});
  }

  handleChange = (value) => {
    this.setState({
      slideIndex: value,
    });
  };

  handleNavigation = (type, id) => {
      this.props.history.push(type+id);
  }

  handleRowClick = (row) => {
    this.props.setRoute(row[row.length-1].replace("_url?", ""));
  }

  checkIfDate = (val, i) => {
    if(this.state.workflowResult && this.state.workflowResult.reportHeader && this.state.workflowResult.reportHeader.length && this.state.workflowResult.reportHeader[i] && this.state.workflowResult.reportHeader[i].type == "epoch") {
      var _date = new Date(Number(val));
      return ('0' + _date.getDate()).slice(-2) + '/'
             + ('0' + (_date.getMonth()+1)).slice(-2) + '/'
             + _date.getFullYear();
    } else {
      return val;
    }
  }

  loadServiceTypes = (service) => {
    var _this=this;
    this.props.setLoadingStatus("loading");
    Api.commonApiPost("/pgr-master/service/v2/_search",{categoryId:service.id,
       keywords:constants.CITIZEN_SERVICES_KEYWORD},{}).then(function(response){
      _this.props.setLoadingStatus("hide");
      service['types'] = response;
      _this.setState([..._this.state.citizenServices, ...service]);
    });
  }

  onClickServiceGroup= ({code, name}) =>{
    this.setState({servicesFilter:"", selectedServiceCode:code, selectedServiceName:name});
    var service = this.state.citizenServices.find((service) => service.code === code);
    if(service && !service.hasOwnProperty("types")){
          this.loadServiceTypes(service);
    }
  }

  onBackFromServiceType(){
    this.setState({servicesFilter:"", selectedServiceCode:"", selectedServiceName:translate(constants.LABEL_SERVICES)});
  }

  render() {
    //filter citizen services
    let servicesMenus=[];
    let serviceTypeMenus=[];

    if(!this.state.selectedServiceCode)
      servicesMenus = this.state.citizenServices.filter(
        (service)=>  !this.state.servicesFilter ||
         service.name.toLowerCase().indexOf(this.state.servicesFilter.toLowerCase()) > -1);
    else
    {
        var service=this.state.citizenServices.find((service)=>service.code === this.state.selectedServiceCode);
        if(service){
          var types = [];
          if(service.hasOwnProperty("types"))
             types=service.types.filter((type)=> !this.state.servicesFilter || type.serviceName.toLowerCase().indexOf(this.state.servicesFilter.toLowerCase()) > -1);
          serviceTypeMenus=[...types];
        }
    }

    let {workflowResult} = this.state;
    let {handleRowClick, checkIfDate} = this;
	  const renderBody=()=> {
		 return this.state.localArray.map((e,i)=> {
				var triColor = "#fff";
				e.attribValues.map((item,index)=>{
				  if(item.key =="PRIORITY"){
            switch(item.name) {
              case 'PRIORITY-1':
                triColor = "#ff0000";
                break;
              case 'PRIORITY-2':
                triColor = "#00ff00";
                break;
              case 'PRIORITY-3':
                triColor = "#ffff00";
                break;
            }
				  }
				})


		  return(
								<tr key={i} style={{ cursor:'pointer'}} onClick={()=>{
									 this.handleNavigation("/pgr/viewGrievance/", e.serviceRequestId);
								}}>
									<td>{i+1}</td>
									<td  style={{minWidth:120}}><span style={{width:6, height:6, borderRadius:50, backgroundColor:triColor, display:"inline-block", marginRight:5}}></span>{e.serviceRequestId}</td>
									<td><span style={{"display": "none"}}>{e.clientTime}</span>{e.requestedDatetime}</td>
    									<td>{e.firstName}</td>
									<td>
                    {e.attribValues && e.attribValues.map((item, index)=>{
                      if(item['key'] == 'keyword')
                        return (item['name'] && item['name'].toLowerCase() == 'complaint' ? 'Grievance' : 'Service');
                      })
                    }
                  </td>
									<td>{e.attribValues && e.attribValues.map((item,index)=>{
                                      if(item.key =="systemStatus"){
                                        return(item.name)
                                      }
									})}</td>
									<td  style={{maxWidth:300}}> Complaint No. {e.serviceRequestId} regarding {e.serviceName} in {e.attribValues && e.attribValues.map((item,index)=>{
                                        if(item.key =="systemStatus"){
                                          return(item.name)
                                        }
                                    })} </td>

								</tr>
		  )	}
							)
	  }

    var {currentUser}=this.props;

    return (
      <div className="Dashboard">

      {
            currentUser && currentUser.type==constants.ROLE_CITIZEN?<div>
          <Tabs
              onChange={this.handleChange}
              value={this.state.slideIndex}
            >
              <Tab label={translate("csv.lbl.myrequest")} value={0}/>
              <Tab label={translate(constants.LABEL_SERVICES)} value={1} />
              <Tab label={translate("pgr.title.create.grievence")} value={2} onClick={()=>{
                  this.props.history.push("/pgr/createGrievance")
                }} />
            </Tabs>
            <SwipeableViews
              index={this.state.slideIndex}
              onChangeIndex={this.handleChange}
            >
              <div>
                  <Grid>
                    <Row>
						<Col xs={12} md={12}>
							<TextField
								hintText={translate("core.lbl.search")}
								floatingLabelText={translate("core.lbl.search")}
								fullWidth={true}
								onChange={(e, value) =>this.localHandleChange(value)}
							/>
						</Col>
                      {this.state.localArray && this.state.localArray.map((e,i)=>{

							var priority;
							var triColor = "#fff";
							e.attribValues.map((item,index)=>{
							  if(item.key =="PRIORITY"){
								triColor = item.name
							  }
							})

                        return(
                          <Col xs={12} md={4} sm={6} style={{paddingTop:15, paddingBottom:15}} key={i}>
                             <Card style={{minHeight:320}}>
                                 <CardHeader subtitleStyle={styles.status}
                                  title={e.serviceName}
                                  subtitle={e.attribValues && e.attribValues.map((item,index)=>{
                                      if(item.key =="systemStatus"){
                                        return(item.name)
                                      }
                                  })}
                                 />

                                 <CardHeader  titleStyle={{fontSize:18}}
                                   title={<Link to={`/pgr/viewGrievance/${e.serviceRequestId}`} target=""><span style={{width:6, height:6, borderRadius:50, backgroundColor:triColor, display:"inline-block", marginRight:5}}></span>{e.serviceRequestId}</Link>}
                                   subtitle={e.requestedDatetime}
                                 />
                                 <CardText>
                                    Complaint No. {e.serviceRequestId} regarding {e.serviceName} in {e.attribValues && e.attribValues.map((item,index)=>{
                                        if(item.key =="systemStatus"){
                                          return(item.name)
                                        }
                                    })}
                                 </CardText>
                             </Card>
                          </Col>
                        )
                      }) }
                    </Row>
                  </Grid>
              </div>
              <div style={styles.slide}>
                  <Grid>
                    <Row>
                      <TextField
        								floatingLabelText={translate("core.lbl.search")}
        								fullWidth={true}
                        value={this.state.servicesFilter||""}
                        onChange={(e, value) => this.filterCitizenServices(value)}
        							/>
                    </Row>
                    <Row>
                        <Card style={{width:'100%'}}>
                          <CardTitle style={{padding:'16px 16px 0px 16px'}}>
                            {this.state.selectedServiceCode ? (
                              <IconButton onTouchTap={()=>{
                                  this.onBackFromServiceType();
                                }}>
                                <FontIcon className="material-icons">arrow_back</FontIcon>
                              </IconButton>
                            ):null}
                            <span className="custom-card-title disable-selection">{translate(this.state.selectedServiceName)}</span>
                          </CardTitle>

                           <CardText style={{padding:'0px 16px 16px 16px'}}>
                              <Row>
                                {!this.state.selectedServiceCode && servicesMenus.map((service, index)=>{
                                     return (<ServiceMenu key={index} service={service} onClick={this.onClickServiceGroup} />);
                                  })}
                                {serviceTypeMenus.map((serviceType, index)=>{
                                  return (<ServiceTypeItem key={index} serviceType={serviceType} onClick={()=>{
                                      this.props.setRoute(`/services/apply/${serviceType.serviceCode}/${serviceType.serviceName.replace(/\//g, "~")}`);
                                    }}></ServiceTypeItem>)
                                })}

                                {serviceTypeMenus.length === 0 && servicesMenus.length === 0? (
                                  <div className="col-xs-12 empty-info">
                                    <FontIcon className="material-icons icon">inbox</FontIcon>
                                    <span className="msg">{translate(constants.LABEL_NO_SERVICS)}</span>
                                  </div>
                                ) : null}

                              </Row>
                           </CardText>
                        </Card>
                    </Row>
                  </Grid>
              </div>
            </SwipeableViews>
          </div>:  <Card className="uiCard">
              <CardHeader title={< div style = {styles.headerStyle} >{translate("deshboard.title")}< /div>} />
				<CardText>
						 <Grid style={{"paddingTop":"0"}}>
                    <Row>
                      <div className="col-md-12">
                          <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive className="table-striped">
                            <thead>
                              <tr>
                                {
                                  workflowResult.hasOwnProperty("reportHeader") && workflowResult.reportHeader.map((item, i) => {
                                      if(item.name != "url")
                                        return (
                                          <th key={i}>{translate(item.label)}</th>
                                        )
                                  })
                                }
                              </tr>
                            </thead>
                            <tbody>
                              {
                                workflowResult.hasOwnProperty("reportData") && workflowResult.reportData.map((item, i) => {
                                  return (
                                    <tr key={i} onClick={() => {handleRowClick(item)}}>
                                      {item.map((item1, i2)=>{
                                        if(!/_url\?/.test(item1))
                                          return (
                                            <td key={i2}>{checkIfDate(item1, i2)}</td>
                                          )
                                      })}
                                    </tr>
                                    )
                                })
                              }
                            </tbody>
                          </Table>
                      </div>
				{/*<div  className="tableLayout">
            <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive className="table-striped">
						 <thead>
							<tr>
							  <th>#</th>
							  <th>Application No.</th>
							  <th>Date</th>
							  <th>Sender</th>
							  <th>Nature of Work</th>
							  <th>Status</th>
							  <th>Comments</th>
							</tr>

						  </thead>
						  <tbody>
						  {renderBody()}
						  </tbody>
					</Table>
          </div>*/}
          {/*<div className="cardLayout">

         {(this.state.localArray.length>0) && this.state.localArray.map((e,i)=>{

			 	var priority;
							var triColor = "#fff";
							e.attribValues.map((item,index)=>{
							  if(item.key =="PRIORITY"){
								triColor = item.name
							  }
							})

                        return(
                          <Col xs={12} md={4} sm={6} style={{paddingTop:15, paddingBottom:15}} key={i}>
                             <Card style={{minHeight:320}}>
                                 <CardHeader titleStyle={{fontSize:18, fontWeight:700}} subtitleStyle={styles.status}
                                  title={e.serviceName}
                                  subtitle={e.attribValues && e.attribValues.map((item,index)=>{
                                      if(item.key =="systemStatus"){
                                        return(item.name)
                                      }
                                  })}
                                 />

                                 <CardHeader  titleStyle={{fontSize:18}}
                                   title={<Link to={`/pgr/viewGrievance/${e.serviceRequestId}`} target=""><span style={{width:6, height:6, borderRadius:50, backgroundColor:triColor, display:"inline-block", marginRight:5}}></span>{e.serviceRequestId}</Link>}
                                   subtitle={e.requestedDatetime}
                                 />
                                 <CardText>
                                    Complaint No. {e.serviceRequestId} regarding {e.serviceName} in {e.attribValues && e.attribValues.map((item,index)=>{
                                        if(item.key =="systemStatus"){
                                          return(item.name)
                                        }
                                    })}
                                 </CardText>
                             </Card>
                          </Col>
                        )
                      }) }
					</div>*/}


                    </Row>
                  </Grid>
				</CardText>
            </Card>
      }


      </div>
    );
  }
}

const ServiceMenu = ({service, onClick}) => {
  return(
    <Col md={4} sm={4} lg={4} xs={12}>
      <div className="service-menu-item disable-selection">
        <ListItem
          primaryText={<div className="ellipsis">{service.name}</div>}
          secondaryText={service.description}
          onTouchTap={()=> {
            onClick(service);
          }}
        />
      </div>
    </Col>
  )
};

const ServiceTypeItem = ({serviceType, onClick}) =>{
    return(
      <Col md={4} sm={4} lg={4} xs={12}>
        <div className="service-menu-item disable-selection">
          <ListItem
            primaryText={serviceType.serviceName}
            secondaryText={serviceType.description}
            onClick={onClick}
          />
          {/*<RaisedButton fullWidth={true} label={service.serviceName} primary={true} onClick={onClick} /> */}
        </div>
      </Col>
    )
};


const mapStateToProps = state => ({
    currentUser: state.common.currentUser
});

// this.props.appLoaded

const mapDispatchToProps = dispatch => ({
    toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
      dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
    },
        setLoadingStatus: (loadingStatus) => {
      dispatch({type: "SET_LOADING_STATUS", loadingStatus});
    },
    setRoute: (route) => dispatch({type: "SET_ROUTE", route})
});

export default connect(mapStateToProps, mapDispatchToProps)(Dashboard);
