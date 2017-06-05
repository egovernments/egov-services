
import React from 'react';
import {Switch,Route} from 'react-router-dom';
import PropertyTaxSearch from './components/contents/PropertyTaxSearch';


const Main = () => (
  <main>
    <Switch>
      <Route exact path='/' component={PropertyTaxSearch}/>    
    </Switch>
  </main>
)


export default(
  <Main/>
);
