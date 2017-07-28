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
// From https://github.com/oliviertassinari/react-swipeable-views
import SwipeableViews from 'react-swipeable-views';
//api import
import Api from "../../api/api";

const $ = require('jquery');
$.DataTable = require('datatables.net');
const dt = require('datatables.net-bs');

const buttons = require('datatables.net-buttons-bs');

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
	     localArray:[],
       hasData:false
    };
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

    if(currentUser.type=="CITIZEN") {
      Api.commonApiPost("/pgr/seva/v1/_search",{userId:currentUser.id},{}).then(function(response){
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
			      localArray: response.serviceRequests,
            hasData:true
          });
		   current.props.setLoadingStatus('hide');
      }).catch((error)=>{
          current.setState({
            serviceRequests: [],
			      localArray:[],
            hasData:true
          });
		  current.props.setLoadingStatus('hide');
      })
    } else {
      Api.commonApiPost("/hr-employee/employees/_search", {id: currentUser.id}, {}).then(function(res) {

        if(res && res.Employee && res.Employee[0] && res.Employee[0].assignments && res.Employee[0].assignments[0] && res.Employee[0].assignments[0].position) {
          Api.commonApiPost("/pgr/seva/v1/_search",{positionId:res.Employee[0].assignments[0].position, status: "REGISTERED,FORWARDED,PROCESSING,NOTCOMPLETED,REOPENED"},{}).then(function(response){
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

  handleChange = (value) => {
    this.setState({
      slideIndex: value,
    });
  };

   handleNavigation = (type, id) => {
      this.props.history.push(type+id);
    }


  render() {

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

    //console.log(this.state.localArray);
    var {currentUser}=this.props;
    // console.log(currentUser);
    return (
      <div className="Dashboard">

      {
            currentUser && currentUser.type=="CITIZEN"?<div>
          <Tabs
              onChange={this.handleChange}
              value={this.state.slideIndex}
            >
              <Tab label="My Request" value={0}/>
              <Tab label="New Grievances" value={1}  onClick={()=>{this.props.history.push("/pgr/createGrievance")}}/>
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
								hintText="Search"
								floatingLabelText="Search"
								fullWidth="true"
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
                    </Row>
                  </Grid>
              </div>
              <div style={styles.slide}>
                  <Grid>
                    <Row>
                      <Col>
                          <Link to={`/pgr/createGrievance`} target=""><RaisedButton label="Create Grievance" secondary={true} /></Link>
                      </Col>
                    </Row>
                  </Grid>
              </div>
            </SwipeableViews>
          </div>:  <Card>
              <CardHeader title={< div style = {styles.headerStyle} >Work List< /div>} />
				<CardText>
						 <Grid style={{"paddingTop":"0"}}>
                    <Row>

				<div  className="tableLayout">
            <Table id="searchTable" style={{color:"black",fontWeight: "normal"}} bordered responsive>
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
          </div>
          <div className="cardLayout">

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
					</div>


                    </Row>
                  </Grid>
				</CardText>

              {/*<CardText>
                            <Grid>
                              <Row>

                                <Col xs={12} md={3}>
                                 <Card>
                                     <CardHeader
                                      title="URL Avatar"
                                      subtitle="Subtitle"
                                      avatar="images/jsa-128.jpg"
                                     />
                                     <CardMedia
                                      overlay={<CardTitle title="Overlay title" subtitle="Overlay subtitle" />}
                                     >
                                      <img src="images/nature-600-337.jpg" alt="" />
                                     </CardMedia>
                                     <CardTitle title="Card title" subtitle="Card subtitle" />
                                     <CardText>
                                      Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                                      Donec mattis pretium massa. Aliquam erat volutpat. Nulla facilisi.
                                      Donec vulputate interdum sollicitudin. Nunc lacinia auctor quam sed pellentesque.
                                      Aliquam dui mauris, mattis quis lacus id, pellentesque lobortis odio.
                                     </CardText>

                                     </Card>
                                 </Col>

                                 <Col xs={12} md={3}>
                                  <Card>
                                      <CardHeader
                                       title="URL Avatar"
                                       subtitle="Subtitle"
                                       avatar="images/jsa-128.jpg"
                                      />
                                      <CardMedia
                                       overlay={<CardTitle title="Overlay title" subtitle="Overlay subtitle" />}
                                      >
                                       <img src="images/nature-600-337.jpg" alt="" />
                                      </CardMedia>
                                      <CardTitle title="Card title" subtitle="Card subtitle" />
                                      <CardText>
                                       Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                                       Donec mattis pretium massa. Aliquam erat volutpat. Nulla facilisi.
                                       Donec vulputate interdum sollicitudin. Nunc lacinia auctor quam sed pellentesque.
                                       Aliquam dui mauris, mattis quis lacus id, pellentesque lobortis odio.
                                      </CardText>

                                      </Card>
                                  </Col>

                                  <Col xs={12} md={3}>
                                   <Card>
                                       <CardHeader
                                        title="URL Avatar"
                                        subtitle="Subtitle"
                                        avatar="images/jsa-128.jpg"
                                       />
                                       <CardMedia
                                        overlay={<CardTitle title="Overlay title" subtitle="Overlay subtitle" />}
                                       >
                                        <img src="images/nature-600-337.jpg" alt="" />
                                       </CardMedia>
                                       <CardTitle title="Card title" subtitle="Card subtitle" />
                                       <CardText>
                                        Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                                        Donec mattis pretium massa. Aliquam erat volutpat. Nulla facilisi.
                                        Donec vulputate interdum sollicitudin. Nunc lacinia auctor quam sed pellentesque.
                                        Aliquam dui mauris, mattis quis lacus id, pellentesque lobortis odio.
                                       </CardText>

                                       </Card>
                                   </Col>

                                   <Col xs={12} md={3}>
                                    <Card>
                                        <CardHeader
                                         title="URL Avatar"
                                         subtitle="Subtitle"
                                         avatar="images/jsa-128.jpg"
                                        />
                                        <CardMedia
                                         overlay={<CardTitle title="Overlay title" subtitle="Overlay subtitle" />}
                                        >
                                         <img src="images/nature-600-337.jpg" alt="" />
                                        </CardMedia>
                                        <CardTitle title="Card title" subtitle="Card subtitle" />
                                        <CardText>
                                         Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                                         Donec mattis pretium massa. Aliquam erat volutpat. Nulla facilisi.
                                         Donec vulputate interdum sollicitudin. Nunc lacinia auctor quam sed pellentesque.
                                         Aliquam dui mauris, mattis quis lacus id, pellentesque lobortis odio.
                                        </CardText>

                                        </Card>
                                    </Col>


                              </Row>
                              </Grid>
                            </CardText>*/}
            </Card>
      }







      </div>
    );
  }
}

const mapStateToProps = state => ({
    // labels: state.labels,
    // appLoaded: state.common.appLoaded,
    // appName: state.common.appName,
    currentUser: state.common.currentUser,
    // redirectTo: state.common.redirectTo,
    // auth:state.common.token,
    // pleaseWait: state.common.pleaseWait,
    // isDialogOpen: state.form.dialogOpen,
    // msg: state.form.msg
});

// this.props.appLoaded

const mapDispatchToProps = dispatch => ({
    toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
      dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg});
    },
        setLoadingStatus: (loadingStatus) => {
      dispatch({type: "SET_LOADING_STATUS", loadingStatus});
    }
    // onLoad: (payload, token) => dispatch({type: 'APP_LOAD', payload, token, skipTracking: true}),
    // onRedirect: () => dispatch({type: 'REDIRECT'}),
    // setLabels: payload => dispatch({type: 'LABELS', payload}),
    // onUpdateAuth: (value, key) => dispatch({type: 'UPDATE_FIELD_AUTH', key, value}),
    // onLogin: (username, password) => {
    //     dispatch({
    //         type: 'LOGIN',
    //         payload: []//agent.Auth.login(username, password)
    //     })
    // },
    // updateError: (error) =>
    //     dispatch({
    //         type: 'UPDATE_ERROR',
    //         error
    //     }),
    // setPleaseWait: (pleaseWait) =>
    //     dispatch({
    //         type: 'PLEASE_WAIT',
    //         pleaseWait
    //     }),
  //  toggleDailogAndSetText: (dailogState,msg) => {
  //         dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  //       }
});


// App.contextTypes = {
//     router: React.PropTypes.object.isRequired
// };




export default connect(mapStateToProps, mapDispatchToProps)(Dashboard);
