import React from 'react';
import {Switch,Route} from 'react-router-dom';
import grievanceCreate from './components/contents/grievanceCreate';
import ReceivingCenterCreate from './components/contents/master/receivingCenter/receivingCenterCreate';
import ViewEditReceivingCenter from './components/contents/master/receivingCenter/viewEditReceivingCenter';
import ViewReceivingCenter from './components/contents/master/receivingCenter/viewReceivingCenter';
import grievanceCreateSD from './components/contents/grievanceCreateSD';
import grievanceView from './components/contents/grievanceView';

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/'/>
      <Route exact path='/createGrievance' component={grievanceCreate}/>
      <Route exact name="createReceivingCenter" path='/createReceivingCenter/:id?' component={ReceivingCenterCreate}/>
      <Route exact path='/receivingCenter/view' component={ViewEditReceivingCenter}/>
      <Route exact path='/receivingCenter/edit' component={ViewEditReceivingCenter}/>
      <Route exact path='/viewReceivingCenter/:id' component={ViewReceivingCenter}/>
    </Switch>
  </main>
)

export default(
  <Main/>
);
