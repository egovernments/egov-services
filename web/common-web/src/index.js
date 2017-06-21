import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter} from 'react-router-dom';
import { Provider } from 'react-redux';
import store from './store';
// import {blue500} from 'material-ui/styles/colors';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';
import injectTapEventPlugin from 'react-tap-event-plugin';
// import './index.css';
import registerServiceWorker from './registerServiceWorker';
import getMuiTheme from 'material-ui/styles/getMuiTheme';
import App from "./components/App"

import './styles/index.css';
import './styles/application.css';
import './styles/vistyle.css';
import './styles/bootstrap.min.css';
import './styles/bootstrap-theme.min.css';
import './styles/jquery.dataTables.min.css';
import './styles/buttons.dataTables.min.css';

// import router from "./router";

// Needed for onTouchTap
// http://stackoverflow.com/a/34015469/988941
injectTapEventPlugin();




const muiTheme = getMuiTheme({
  palette: {

    primary1Color: "#354f57",
    primary2Color: "#354f57",
    textColor:"#354f57"
  }
});

ReactDOM.render(
<Provider store={store}>
  <MuiThemeProvider muiTheme={muiTheme}>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </MuiThemeProvider>
</Provider>, document.getElementById('root'));
registerServiceWorker();
