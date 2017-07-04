import React, { Component } from 'react';
import {connect} from 'react-redux';
import PropTypes from 'prop-types';
import {Link, Route} from 'react-router-dom';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import RaisedButton from 'material-ui/RaisedButton';
import {Tabs, Tab} from 'material-ui/Tabs';
// From https://github.com/oliviertassinari/react-swipeable-views
import SwipeableViews from 'react-swipeable-views';
//api import
import Api from "../../api/api";
const styles = {
  headline: {
    fontSize: 24,
    paddingTop: 16,
    marginBottom: 12,
    fontWeight: 400,
  },
  slide: {
    padding: 10,
  },
  status:{
    fontSize:14,
    background:"rgb(53, 79, 87)",
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
      serviceRequests: []
    };
}
  componentWillMount() {

    let current = this;
    let {currentUser}=this.props;

    if(currentUser.type=="CITIZEN") {
      Api.commonApiPost("/pgr/seva/v1/_search",{userId:currentUser.id},{}).then(function(response){
          console.log(response);
          current.setState({
            serviceRequests: response.serviceRequests
          });
      }).catch((error)=>{
          console.log(error);
          current.setState({
            serviceRequests: []
          });
      })
    } else {
        Api.commonApiPost("/pgr/seva/v1/_search",{assignmentId:currentUser.id},{}).then(function(response){
            console.log(response);
            current.setState({
              serviceRequests: response.serviceRequests
            });
        }).catch((error)=>{
            console.log(error);
            current.setState({
              serviceRequests: []
            });
        })
    }

  };

  handleChange = (value) => {
    this.setState({
      slideIndex: value,
    });
  };


  render() {

    // console.log(this);
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
              <Tab label="My Request" value={0} />
              <Tab label="New Grievances" value={1} />
            </Tabs>
            <SwipeableViews
              index={this.state.slideIndex}
              onChangeIndex={this.handleChange}
            >
              <div>
                  <Grid>
                    <Row>
                      {this.state.serviceRequests && this.state.serviceRequests.map((e,i)=>{
						  						  
                        return(
                          <Col xs={12} md={4} sm={6} style={{paddingTop:15, paddingBottom:15}} key={i}>
                             <Card>
                                 <CardHeader titleStyle={{fontSize:18, fontWeight:700}} subtitleStyle={styles.status}
                                  title={e.serviceName}
                                  subtitle={e.attribValues && e.attribValues.map((item,index)=>{
                                      if(item.key =="status"){
                                        return(item.name)
                                      }
                                  })}
                                 />

                                 <CardHeader  titleStyle={{fontSize:18}}
                                   title={<Link to={`/pgr/viewGrievance/${e.serviceRequestId}`} target="_blank>{e.serviceRequestId}</Link>}
                                   subtitle={e.requestedDatetime}
                                 />
                                 <CardText>
                                    Complaint No. {e.serviceRequestId} regarding {e.serviceName} in {e.attribValues && e.attribValues.map((item,index)=>{
                                        if(item.key =="status"){
                                          return(item.name)
                                        }
                                    })} status.
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
                          <Link to={`/pgr/createGrievance`} target="_blank"><RaisedButton label="Create Grievance" secondary={true} /></Link>
                      </Col>
                    </Row>
                  </Grid>
              </div>
            </SwipeableViews>
          </div>:  <Card>
              <CardHeader title="Work list" />
				<CardText>
						 <Grid>
                    <Row>
                      {this.state.serviceRequests && this.state.serviceRequests.map((e,i)=>{
                        return(
                          <Col xs={12} md={4} sm={6} style={{paddingTop:15, paddingBottom:15}} key={i}>
                             <Card>
                                 <CardHeader titleStyle={{fontSize:18, fontWeight:700}} subtitleStyle={styles.status}
                                  title={e.serviceName}
                                  subtitle={e.attribValues && e.attribValues.map((item,index)=>{
                                      if(item.key =="status"){
                                        return(item.name)
                                      }
                                  })}
                                 />

                                 <CardHeader  titleStyle={{fontSize:18}}
                                   title={<Link to={`/pgr/viewGrievance/${e.serviceRequestId}`} target="">{e.serviceRequestId}</Link>}
                                   subtitle={e.requestedDatetime}
                                 />
                                 <CardText>
                                    Complaint No. {e.serviceRequestId} regarding {e.serviceName} in {e.attribValues && e.attribValues.map((item,index)=>{
                                        if(item.key =="status"){
                                          return(item.name)
                                        }
                                    })} status.
                                 </CardText>
                             </Card>
                          </Col>
                        )
                      }) }
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