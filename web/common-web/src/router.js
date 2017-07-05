
import React from 'react';
import {Switch,Route} from 'react-router-dom';
import PropertyTaxSearch from './components/contents/PropertyTaxSearch';
import Login from './components/contents/Login';
import Dashboard from './components/contents/Dashboard';

import PropertyTaxCreate from './components/contents/CruProperty';
import ProfileEdit from './components/contents/settings/profileEdit';

//PGR
import grievanceCreate from './components/contents/pgr/grievanceCreate';
import grievanceCreateSD from './components/contents/pgr/grievanceCreateSD';
import grievanceView from './components/contents/pgr/grievanceView';
import grievanceSearch from './components/contents/pgr/grievanceSearch';
import ReceivingCenterCreate from './components/contents/pgr/master/receivingCenter/receivingCenterCreate';
import ViewEditReceivingCenter from './components/contents/pgr/master/receivingCenter/viewEditReceivingCenter';
import ViewReceivingCenter from './components/contents/pgr/master/receivingCenter/viewReceivingCenter';
import receivingModeCreate from './components/contents/pgr/master/receivingMode/receivingModeCreate';
import viewOrUpdateReceivingMode from './components/contents/pgr/master/receivingMode/viewOrUpdateReceivingMode';
import ServiceGroupCreate from './components/contents/pgr/master/serviceGroup/serviceGroupCreate';
import ViewEditServiceGroup from './components/contents/pgr/master/serviceGroup/viewEditServiceGroup';
import viewReceivingMode from './components/contents/pgr/master/receivingMode/viewReceivingMode';
import createRouter from './components/contents/pgr/master/router/create';
import searchRouter from './components/contents/pgr/master/router/search';
import routerGeneration from './components/contents/pgr/master/router/routerGeneration';
import BulkEscalationGeneration from './components/contents/pgr/master/escalation/bulkEscalationGeneration';
import serviceTypeCreate from './components/contents/pgr/master/serviceType/serviceTypeCreate';
import viewOrUpdateServiceType from './components/contents/pgr/master/serviceType/viewOrUpdateServiceType';
import viewServiceType from './components/contents/pgr/master/serviceType/viewServiceType';
import ViewServiceGroup from './components/contents/pgr/master/serviceGroup/viewServiceGroup';
import ViewEscalation from './components/contents/pgr/master/escalation/viewEscalation';
import DefineEscalation from './components/contents/pgr/master/escalation/defineEscalation';
import SearchEscalation from './components/contents/pgr/master/escalationTime/searchEscalation';
import DefineEscalationTime from './components/contents/pgr/master/escalationTime/defineEscalationTime';
import ServiceTypeCreate from './components/contents/pgr/master/serviceType/serviceTypeCreate';





const Main = () => (
  <main style={{"marginBottom": "50px"}}>
    <Switch>
      <Route path='/propertyTaxSearch' component={PropertyTaxSearch}/>
      <Route exact path='/' component={Login}/>
      <Route exact path='/profileEdit' component={ProfileEdit}/>
      <Route exact path='/dashboard' component={Dashboard}/>

      {/*
        pgr
        */}
        <Route exact path='/pgr/createGrievance' component={grievanceCreate}/>
        <Route exact path='/pgr/viewGrievance/:srn' component={grievanceView}/>
        <Route exact path='/pgr/searchGrievance' component={grievanceSearch}/>
        <Route exact name="createReceivingCenter" path='/pgr/createReceivingCenter/:id?' component={ReceivingCenterCreate}/>
        <Route exact path='/pgr/createReceivingCenter' component={ReceivingCenterCreate}/>
        <Route exact path='/pgr/receivingCenter/view' component={ViewEditReceivingCenter}/>
        <Route exact path='/pgr/receivingCenter/edit' component={ViewEditReceivingCenter}/>
        <Route exact path='/pgr/viewReceivingCenter/:id' component={ViewReceivingCenter}/>
        <Route exact path='/pgr/createRouter/:type/:id' component={createRouter}/>
        <Route exact path='/pgr/createRouter' component={createRouter}/>
        <Route exact path='/pgr/routerGeneration' component={routerGeneration}/>
        <Route exact path='/pgr/searchRouter/:type' component={searchRouter}/>
        <Route exact path='/pgr/receivingModeCreate' component={receivingModeCreate}/>
        <Route exact name='receivingModeCreate' path='/pgr/receivingModeCreate/:type/:id' component={receivingModeCreate}/>
        <Route exact path='/pgr/viewOrUpdateReceivingMode/:type' component={viewOrUpdateReceivingMode}/>
        <Route exact path='/pgr/viewReceivingMode/:type/:id' component={viewReceivingMode}/>
        <Route exact name="createServiceGroup" path='/pgr/createServiceGroup/:id?' component={ServiceGroupCreate}/>
        <Route exact path='/pgr/createServiceGroup' component={ServiceGroupCreate}/>
        <Route exact path='/pgr/serviceGroup/view' component={ViewEditServiceGroup}/>
        <Route exact path='/pgr/serviceGroup/edit' component={ViewEditServiceGroup}/>
        <Route exact path='/pgr/bulkEscalationGeneration' component={BulkEscalationGeneration}/>
        <Route exact path='/pgr/serviceTypeCreate' component={serviceTypeCreate}/>
        <Route exact name="serviceTypeCreate" path='/pgr/serviceTypeCreate/:type/:id' component={serviceTypeCreate}/>
        <Route exact path='/pgr/viewOrUpdateServiceType/:type' component={viewOrUpdateServiceType}/>
        <Route exact path='/pgr/viewServiceType/:type/:id' component={viewServiceType}/>
        <Route exact path='/pgr/viewServiceGroup/:id' component={ViewServiceGroup}/>
        <Route exact path='/pgr/viewEscalation' component={ViewEscalation}/>
        <Route exact path='/pgr/defineEscalation' component={DefineEscalation}/>
        <Route exact path='/pgr/searchEscalationTime' component={SearchEscalation}/>
        <Route exact path='/pgr/defineEscalationTime' component={DefineEscalationTime}/>
        <Route exact path='/pgr/createServiceType' component={ServiceTypeCreate}/>

    </Switch>
  </main>
)


export default(
  <Main/>
);
