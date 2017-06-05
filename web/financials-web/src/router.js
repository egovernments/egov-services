import React from 'react';
import {Switch, Route} from 'react-router-dom';



import CreateNewFunction from './components/contents/CreateNewFunction';
import ViewFunction from './components/contents/ViewFunction';
import EditFunction from './components/contents/EditFunction';

// import App from "./components/App";

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/' component={CreateNewFunction}/>
      <Route exact path='/view-function' component={ViewFunction}/>
      <Route exact path='/edit-function' component={EditFunction}/>


    </Switch>
  </main>
)

export default(<Main/>);
