import React from 'react';
import {Switch,Route} from 'react-router-dom';
import grievanceCreate from './components/contents/grievanceCreate';

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/'/>
      <Route exact path='/createGrievance' component={grievanceCreate}/>
    </Switch>
  </main>
)

export default(
  <Main/>
);
