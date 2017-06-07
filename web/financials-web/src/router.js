import React from 'react';
import {Switch, Route} from 'react-router-dom';

import CreateNewFunction from './components/contents/CreateNewFunction';
import ViewFunction from './components/contents/ViewFunction';
import EditFunction from './components/contents/EditFunction';
import ViewDetailedCode from './components/contents/masters/chartOfAccount/detailedCode/viewDetailedCode';
import CreateDetailedCode from './components/contents/masters/chartOfAccount/detailedCode/createDetailedCode';

// import App from "./components/App";

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/' component={CreateNewFunction}/>
      <Route exact path='/view-function' component={ViewFunction}/>
      <Route exact path='/edit-function' component={EditFunction}/>
      <Route exact path='/chartOfAccounts-viewDetailedCode' component={ViewDetailedCode}/>
      <Route exact path='/chartOfAccounts-createDetailedCode' component={CreateDetailedCode}/>
    </Switch>
  </main>
)

export default(<Main/>);
