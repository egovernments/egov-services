import React from 'react';
import {Switch,Route} from 'react-router-dom';
import grievanceCreate from './components/contents/grievanceCreate';
import ReceivingCenterCreate from './components/contents/master/receivingCenter/receivingCenterCreate';
import grievanceCreateSD from './components/contents/grievanceCreateSD';
import grievanceView from './components/contents/grievanceView';

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/'/>
      <Route exact path='/createGrievance' component={grievanceCreate}/>
      <Route exact path='/createReceivingCenter' component={ReceivingCenterCreate}/>
      <Route exact path='/searchAll' component={ReceivingCenterCreate}/>
      <Route exact path='/createGrievanceSD' component={grievanceCreateSD}/>
      <Route path='/viewGrievance/:srn' component={grievanceView}/>
    </Switch>
  </main>
)

export default(
  <Main/>
);
