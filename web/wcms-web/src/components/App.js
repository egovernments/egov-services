import React, { Component } from 'react';
import {connect} from 'react-redux';

import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
// import {Switch,Route} from 'react-router-dom';

import Header from './common/Header';
import Footer from './common/Footer';
// import PropertyTaxSearch from "./contents/PropertyTaxSearch";

import router from "../router";
var axios = require('axios');


class App extends Component {

  componentDidMount(){

    var instance = axios.create({
      baseURL: window.location.origin,
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Authorization': 'Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0',
      }
    });

    var params = new URLSearchParams();
    params.append('username', '9999999999');
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

  }

  render() {
    var {toggleDailogAndSetText,isDialogOpen,msg}=this.props;
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
            modal={false}
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
