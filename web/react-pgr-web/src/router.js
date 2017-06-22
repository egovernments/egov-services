import React from 'react';
import {Switch,Route} from 'react-router-dom';
import grievanceCreate from './components/contents/grievanceCreate';
import ReceivingCenterCreate from './components/contents/master/receivingCenter/receivingCenterCreate';
import ViewEditReceivingCenter from './components/contents/master/receivingCenter/viewEditReceivingCenter';
import grievanceCreateSD from './components/contents/grievanceCreateSD';
import grievanceView from './components/contents/grievanceView';
import grievanceSearch from './components/contents/grievanceSearch';

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/'/>
      <Route exact path='/createGrievance' component={grievanceCreate}/>
      <Route exact path='/searchGrievance' component={grievanceSearch}/>
      <Route exact name="createReceivingCenter" path='/createReceivingCenter/:code' component={ReceivingCenterCreate}/>
      <Route exact path='/receivingCenter/view' component={ViewEditReceivingCenter}/>
      <Route exact path='/receivingCenter/edit' component={ViewEditReceivingCenter}/>
    </Switch>
  </main>
)

export default(
  <Main/>
);
