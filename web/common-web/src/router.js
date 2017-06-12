
import React from 'react';
import {Switch,Route} from 'react-router-dom';
import PropertyTaxSearch from './components/contents/PropertyTaxSearch';
import Login from './components/contents/Login';


const Main = () => (
  <main>
    <Switch>
      <Route exact path='/PropertyTaxSearch' component={PropertyTaxSearch}/>
      <Route exact path='/' component={Login}/>
    </Switch>
  </main>
)


export default(
  <Main/>
);
