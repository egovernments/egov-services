import React from 'react';
import {Switch,Route} from 'react-router-dom';
import grievanceCreate from './components/contents/grievanceCreate';
import ReceivingCenterCreate from './components/contents/master/receivingCenter/receivingCenterCreate';

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/'/>
      <Route exact path='/createGrievance' component={grievanceCreate}/>
      <Route exact path='/createReceivingCenter' component={ReceivingCenterCreate}/>
      <Route exact path='/searchAll' component={ReceivingCenterCreate}/>
    </Switch>
  </main>
)

export default(
  <Main/>
);
