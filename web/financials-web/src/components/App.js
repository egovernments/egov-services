import React, { Component } from 'react';
import {connect} from 'react-redux';

import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
// import {Switch,Route} from 'react-router-dom';

import Header from './common/Header';
import Footer from './common/Footer';

import router from "../router";


class App extends Component {
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
