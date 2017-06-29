import React, { Component } from 'react';
import {connect} from 'react-redux';
import { browserRouter } from 'react-router';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import Header from './common/Header';
import Footer from './common/Footer';

import router from "../router";
var axios = require('axios');

class App extends Component {

  componentWillReceiveProps(nextProps) {

  }
  componentDidMount(){

    var instance = axios.create({
      baseURL: window.location.origin,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': 'Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0',
      }
    });

    var params = new URLSearchParams();
    params.append('username', 'narasappa');
    params.append('password', 'demo');
    params.append('grant_type', 'password');
    params.append('scope', 'read');
    params.append('tenantId', 'default');

    instance.post('/user/oauth/token', params).then(function(response) {
      localStorage.setItem("auth", response.data.access_token);
			localStorage.setItem("type", response.data.UserRequest.type);
			localStorage.setItem("id", response.data.UserRequest.id);
			localStorage.setItem("tenantId", response.data.UserRequest.tenantId);
    }).catch(function(response) {
      throw new Error(response.data.message);
    });

    instance.get('/localization/messages?tenantId=default&locale=en_IN').then(function(response) {
      localStorage.setItem("lang_response", JSON.stringify(response.data.messages));
    }).catch(function(response) {
      throw new Error(response.data.message);
    });

  }

  render() {
    var {toggleDailogAndSetText,isDialogOpen,msg,history}=this.props;
    const actions = [
      <FlatButton
        label="Ok"
        primary={true}
        onTouchTap={this.handleClose}
      />,
    ];
    return (
      <div className="App">
          <Header/>
              {router}
          <Footer/>
          <Dialog
            actions={actions}
            modal={true}
            open={isDialogOpen}
            onRequestClose={toggleDailogAndSetText(false,"")}
            >
            {msg}
          </Dialog>
      </div>
    );
  }
}

const mapStateToProps = state => ({isDialogOpen: state.form.dialogOpen, msg: state.form.msg});

const mapDispatchToProps = dispatch => ({
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  }
});



export default connect(mapStateToProps, mapDispatchToProps)(App);
