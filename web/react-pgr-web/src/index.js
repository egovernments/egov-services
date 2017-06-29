import React from 'react';
import ReactDOM from 'react-dom';
import {HashRouter} from 'react-router-dom';
import { Provider } from 'react-redux';
import store from './store';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import injectTapEventPlugin from 'react-tap-event-plugin';
// import './index.css';
import registerServiceWorker from './registerServiceWorker';
import App from "./components/App";

import './styles/bootstrap.min.css';

// import router from "./router";

// Needed for onTouchTap
// http://stackoverflow.com/a/34015469/988941
injectTapEventPlugin();

const muiTheme = getMuiTheme({
  palette: {
    primary1Color: "#354f57",
    primary2Color: "#0277bd"
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
registerServiceWorker();
