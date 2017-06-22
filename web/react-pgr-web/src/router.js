import React from 'react';
import {Switch,Route} from 'react-router-dom';
import grievanceCreate from './components/contents/grievanceCreate';
import ReceivingCenterCreate from './components/contents/master/receivingCenter/receivingCenterCreate';
import ViewEditReceivingCenter from './components/contents/master/receivingCenter/viewEditReceivingCenter';
import ViewReceivingCenter from './components/contents/master/receivingCenter/viewReceivingCenter';
import grievanceCreateSD from './components/contents/grievanceCreateSD';
import grievanceView from './components/contents/grievanceView';
import grievanceSearch from './components/contents/grievanceSearch';
import receivingModeCreate from './components/contents/master/receivingMode/receivingModeCreate';
import viewOrUpdateReceivingMode from './components/contents/master/receivingMode/viewOrUpdateReceivingMode';
import ServiceGroupCreate from './components/contents/master/serviceGroup/serviceGroupCreate';
import ViewEditServiceGroup from './components/contents/master/serviceGroup/viewEditServiceGroup';


const Main = () => (
  <main>
    <Switch>
      <Route exact path='/'/>
      <Route exact path='/createGrievance' component={grievanceCreate}/>
      <Route exact name="createReceivingCenter" path='/createReceivingCenter/:id?' component={ReceivingCenterCreate}/>
      <Route exact path='/receivingCenter/view' component={ViewEditReceivingCenter}/>
      <Route exact path='/receivingCenter/edit' component={ViewEditReceivingCenter}/>
      <Route exact path='/viewReceivingCenter/:id' component={ViewReceivingCenter}/>
      <Route exact path='/searchGrievance' component={grievanceSearch}/>
      <Route exact path='/receivingModeCreate' component={receivingModeCreate}/>
      <Route exact path='/viewOrUpdateReceivingMode' component={viewOrUpdateReceivingMode}/>
      <Route exact name="receivingModeCreate" path='/receivingModeCreate' component={receivingModeCreate}/>
      <Route exact name="receivingModeCreate" path='/receivingModeCreate/:id' component={receivingModeCreate}/>
      <Route exact name="receivingModeCreate" path='/receivingModeCreate/:id' component={receivingModeCreate}/>
      <Route exact path='/viewOrUpdateReceivingMode/view' component={viewOrUpdateReceivingMode}/>
      <Route exact path='/viewOrUpdateReceivingMode/edit' component={viewOrUpdateReceivingMode}/>
      <Route exact path='/createReceivingCenter' component={ReceivingCenterCreate}/>
      <Route exact name="createServiceGroup" path='/createServiceGroup/:code' component={ServiceGroupCreate}/>
      <Route exact path='/createServiceGroup' component={ServiceGroupCreate}/>
      <Route exact path='/serviceGroup/view' component={ViewEditServiceGroup}/>
      <Route exact path='/serviceGroup/edit' component={ViewEditServiceGroup}/>
    </Switch>
  </main>
)

export default(
  <Main/>
);
