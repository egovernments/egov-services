import React from 'react';
import ReactDOM from 'react-dom';
import {HashRouter} from 'react-router-dom';
import { Provider } from 'react-redux';
import store from './store';
import {brown500} from 'material-ui/styles/colors';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import injectTapEventPlugin from 'react-tap-event-plugin';
// import './index.css';
import './styles/application.css';
import './styles/vistyle.css';
import getMuiTheme from 'material-ui/styles/getMuiTheme';


// Needed for onTouchTap
// http://stackoverflow.com/a/34015469/988941
injectTapEventPlugin();

import App from "./components/App"
// import router from "./router";


const muiTheme = getMuiTheme({
  palette: {

    primary1Color: "#5a3e1b",
    primary2Color: "#5a3e1b",
    textColor:"#5a3e1b"
  }
});

ReactDOM.render(
<Provider store={store}>
  <MuiThemeProvider muiTheme={muiTheme}>
    <HashRouter>
      <App />
    </HashRouter>
  </MuiThemeProvider>
</Provider>, document.getElementById('root'));
