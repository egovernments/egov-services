import React, { Component } from 'react';
import {connect} from 'react-redux';
import PropTypes from 'prop-types';
import {Card, CardActions, CardHeader, CardMedia, CardTitle, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
//api import
import api from "../../api/commonAPIS"


class Dashboard extends Component {

  render() {
    // console.log(this);
    var {toggleDailogAndSetText,isDialogOpen,msg}=this.props;

    return (
      <div className="Dashboard">
      <Card>
        <CardHeader
         title="Work list"
        />


        <CardText>
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
        </CardText>
      </Card>



      </div>
    );
  }
}

const mapStateToProps = state => ({
    // labels: state.labels,
    // appLoaded: state.common.appLoaded,
    // appName: state.common.appName,
    // currentUser: state.common.currentUser,
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
