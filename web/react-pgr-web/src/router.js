import React from 'react';
import {Switch,Route} from 'react-router-dom';
import grievanceCreate from './components/contents/grievanceCreate';
import grievanceCreateSD from './components/contents/grievanceCreateSD';
import grievanceView from './components/contents/grievanceView';
import grievanceSearch from './components/contents/grievanceSearch';
import ReceivingCenterCreate from './components/contents/master/receivingCenter/receivingCenterCreate';
import ViewEditReceivingCenter from './components/contents/master/receivingCenter/viewEditReceivingCenter';
import ViewReceivingCenter from './components/contents/master/receivingCenter/viewReceivingCenter';
import receivingModeCreate from './components/contents/master/receivingMode/receivingModeCreate';
import viewOrUpdateReceivingMode from './components/contents/master/receivingMode/viewOrUpdateReceivingMode';
import ServiceGroupCreate from './components/contents/master/serviceGroup/serviceGroupCreate';
import ViewEditServiceGroup from './components/contents/master/serviceGroup/viewEditServiceGroup';
import viewReceivingMode from './components/contents/master/receivingMode/viewReceivingMode';
import createRouter from './components/contents/master/router/create';
import searchRouter from './components/contents/master/router/search';
import routerGeneration from './components/contents/master/router/routerGeneration';
import BulkEscalationGeneration from './components/contents/master/escalation/bulkEscalationGeneration';
import serviceTypeCreate from './components/contents/master/serviceType/serviceTypeCreate';
import viewOrUpdateServiceType from './components/contents/master/serviceType/viewOrUpdateServiceType';
import viewServiceType from './components/contents/master/serviceType/viewServiceType';
import ViewServiceGroup from './components/contents/master/serviceGroup/viewServiceGroup';
import ViewEscalation from './components/contents/master/escalation/viewEscalation';
import DefineEscalation from './components/contents/master/escalation/defineEscalation';

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/'/>
      <Route exact path='/createGrievance' component={grievanceCreate}/>
      <Route exact path='/viewGrievance/:srn' component={grievanceView}/>
      <Route exact path='/searchGrievance' component={grievanceSearch}/>
      <Route exact name="createReceivingCenter" path='/createReceivingCenter/:id?' component={ReceivingCenterCreate}/>
      <Route exact path='/createReceivingCenter' component={ReceivingCenterCreate}/>
      <Route exact path='/receivingCenter/view' component={ViewEditReceivingCenter}/>
      <Route exact path='/receivingCenter/edit' component={ViewEditReceivingCenter}/>
      <Route exact path='/viewReceivingCenter/:id' component={ViewReceivingCenter}/>
      <Route exact path='/createRouter/:type/:id' component={createRouter}/>
      <Route exact path='/createRouter' component={createRouter}/>
      <Route exact path='/routerGeneration' component={routerGeneration}/>
      <Route exact path='/searchRouter/:type' component={searchRouter}/>
      <Route exact path='/receivingModeCreate' component={receivingModeCreate}/>
      <Route exact name='receivingModeCreate' path='/receivingModeCreate/:type/:id' component={receivingModeCreate}/>
      <Route exact path='/viewOrUpdateReceivingMode/:type' component={viewOrUpdateReceivingMode}/>
      <Route exact path='/viewReceivingMode/:type/:id' component={viewReceivingMode}/>
      <Route exact name="createServiceGroup" path='/createServiceGroup/:id?' component={ServiceGroupCreate}/>
      <Route exact path='/createServiceGroup' component={ServiceGroupCreate}/>
      <Route exact path='/serviceGroup/view' component={ViewEditServiceGroup}/>
      <Route exact path='/serviceGroup/edit' component={ViewEditServiceGroup}/>
      <Route exact path='/bulkEscalationGeneration' component={BulkEscalationGeneration}/>
      <Route exact path='/serviceTypeCreate' component={serviceTypeCreate}/>
      <Route exact name="serviceTypeCreate" path='/serviceTypeCreate/:id' component={serviceTypeCreate}/>
      <Route exact path='/viewOrUpdateServiceType/view' component={viewOrUpdateServiceType}/>
      <Route exact path='/viewOrUpdateServiceType/edit' component={viewOrUpdateServiceType}/>
      <Route exact path='/viewServiceType/:id' component={viewServiceType}/>
      <Route exact path='/viewServiceGroup/:id' component={ViewServiceGroup}/>
      <Route exact path='/viewEscalation' component={ViewEscalation}/>
      <Route exact path='/defineEscalation' component={DefineEscalation}/>
    </Switch>
  </main>
)

export default(
  <Main/>
);
