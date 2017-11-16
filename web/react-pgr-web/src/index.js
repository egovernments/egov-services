// import 'materialize-css/dist/css/materialize.min.css';
// import 'materialize-css/dist/js/materialize.min.js';

import React from 'react';
import ReactDOM from 'react-dom';
import {HashRouter} from 'react-router-dom';
import { Provider } from 'react-redux';
import store from './store';
import {grey300} from 'material-ui/styles/colors';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import injectTapEventPlugin from 'react-tap-event-plugin';
// import './index.css';
import registerServiceWorker from './registerServiceWorker';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import App from "./components/App";
// import $ from "jquery";
// import {firebaseDBRef} from './firebase/';

import './styles/index.css';
import './styles/application.css';
import './styles/vistyle.css';
import './styles/bootstrap.min.css';
import './styles/jquery.dataTables.min.css';
import './styles/buttons.dataTables.min.css';
import './styles/bootstrap.datepicker.css';
import './styles/react.datetimepicker.css';
import './styles/react-big-calendar.css';
// import router from "./router";

// Needed for onTouchTap
// http://stackoverflow.com/a/34015469/988941
injectTapEventPlugin();

document.title="eGov";

let themeObject={
  fontFamily:"Lato, sans",
  textColor:"rgba(0, 0, 0, 0.68)",
  backgroundcolor:'#F7F7F7',
  palette: {
    primary1Color: "#009688",
    primary2Color: "#f58720",
    textColor:"#5f5c62",
    canvasColor: '#F7F7F7',
    borderColor: grey300,
  },
  raisedButton: {
    primaryColor: "#607D8B"
  },
  floatingActionButton: {
    color: "#f58720"
  }
};

// var hash = window.location.hash.split("/");
//
//
// if(localStorage.getItem("token") && localStorage.getItem("userRequest"))
// {
//     getTheme(localStorage.getItem("tenantId")?localStorage.getItem("tenantId"):'default');
// }
// else {
//     getTheme(hash[1]?hash[1]:"default");
// }
//
//
//
// function getTheme(tenantId)
// {
//   $.ajax({
//   url: "https://raw.githubusercontent.com/abhiegov/test/master/tenantTheme.json?timestamp="+new Date().getTime(),
//   // dataType: 'application/javascript',
//   success: function(results)
//   {
//       var content = JSON.parse(results);
//       // console.log(content[(hash[1]?hash[1]:"default")]);
//       renderUI(content["theme"][tenantId]);
//   },
//   error: function (results) {
//     renderUI(themeObject);
//   }})
// }
//
//
//
// function renderUI(themeObject)
// {
  const muiTheme = getMuiTheme(themeObject);

  ReactDOM.render(
  <Provider store={store}>
    <MuiThemeProvider muiTheme={muiTheme}>
      <HashRouter>
        <App />
      </HashRouter>
    </MuiThemeProvider>
  </Provider>, document.getElementById('root'));
  registerServiceWorker();
// }






  // console.log(hash[1]?hash[1]:"default");
  // firebaseDBRef.child("theme/"+(hash[1]?hash[1]:"default")).once('value').then(function(snapshot) {
  //         console.log(snapshot);
  //         renderUI(snapshot.val());
  //
  //   });
