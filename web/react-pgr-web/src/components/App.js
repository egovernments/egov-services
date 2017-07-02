import React, { Component } from 'react';
import {connect} from 'react-redux';
import { browserRouter } from 'react-router';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import Header from './common/Header';
import Footer from './common/Footer';
import router from "../router";
import Snackbar from 'material-ui/Snackbar';
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
    var {toggleDailogAndSetText,isDialogOpen,msg,history, toastMsg, isSnackBarOpen, toggleSnackbarAndSetText}=this.props;
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
              <div className="mainWrapper">
                <div className="pushFooter">
                  {router}
                </div>
              </div>
          <Footer/>
          <Dialog
            actions={actions}
            modal={true}
            open={isDialogOpen}
            onRequestClose={toggleDailogAndSetText(false,"")}
            >
            {msg}
          </Dialog>
          <Snackbar
              open={isSnackBarOpen}
              message={toastMsg}
              autoHideDuration={4000}
              onRequestClose={()=> {toggleSnackbarAndSetText(false, "")}}
            />
      </div>
    );
  }
}

const mapStateToProps = state => ({isDialogOpen: state.form.dialogOpen, isSnackBarOpen : state.form.snackbarOpen,msg: state.form.msg, toastMsg: state.form.toastMsg});

const mapDispatchToProps = dispatch => ({
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState,toastMsg});
  }
});



export default connect(mapStateToProps, mapDispatchToProps)(App);
