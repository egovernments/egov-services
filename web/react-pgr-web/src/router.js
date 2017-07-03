
import React from 'react';
import {Switch,Route} from 'react-router-dom';
import Login from './components/contents/Login';
import Dashboard from './components/contents/Dashboard';
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
import Report from './components/contents/reports/report';

const base = "/pgr-web/v2";

const Main = () => {
    return (
  <main style={{"marginBottom": "50px"}}>
    <Switch>
      <Route exact path= {base + '/'} component={Login}/>
      <Route exact path='/profileEdit' component={ProfileEdit}/>
      <Route exact path='/dashboard' component={Dashboard}/>

        {/*
        pgr
        */}
        <Route exact path={base + '/pgr/createGrievance'} component={grievanceCreate}/>
        <Route exact path={base + '/pgr/viewGrievance/:srn'} component={grievanceView}/>
        <Route exact path={base + '/pgr/searchGrievance'} component={grievanceSearch}/>
        <Route exact name="createReceivingCenter" path={base + '/pgr/createReceivingCenter/:id?'} component={ReceivingCenterCreate}/>
        <Route exact path={base + '/pgr/createReceivingCenter'} component={ReceivingCenterCreate}/>
        <Route exact path={base + '/pgr/receivingCenter/view'} component={ViewEditReceivingCenter}/>
        <Route exact path={base + '/pgr/receivingCenter/edit'} component={ViewEditReceivingCenter}/>
        <Route exact path={base + '/pgr/viewReceivingCenter/:id'} component={ViewReceivingCenter}/>
        <Route exact path={base + '/pgr/createRouter/:type/:id'} component={createRouter}/>
        <Route exact path={base + '/pgr/createRouter'} component={createRouter}/>
        <Route exact path={base + '/pgr/routerGeneration'} component={routerGeneration}/>
        <Route exact path={base + '/pgr/searchRouter/:type'} component={searchRouter}/>
        <Route exact path={base + '/pgr/receivingModeCreate'} component={receivingModeCreate}/>
        <Route exact name='receivingModeCreate' path={base + '/pgr/receivingModeCreate/:type/:id'} component={receivingModeCreate}/>
        <Route exact path={base + '/pgr/viewOrUpdateReceivingMode/:type'} component={viewOrUpdateReceivingMode}/>
        <Route exact path={base + '/pgr/viewReceivingMode/:type/:id'} component={viewReceivingMode}/>
        <Route exact name="createServiceGroup" path={base + '/pgr/createServiceGroup/:id?'} component={ServiceGroupCreate}/>
        <Route exact path={base + '/pgr/createServiceGroup'} component={ServiceGroupCreate}/>
        <Route exact path={base + '/pgr/serviceGroup/view'} component={ViewEditServiceGroup}/>
        <Route exact path={base + '/pgr/serviceGroup/edit'} component={ViewEditServiceGroup}/>
        <Route exact path={base + '/pgr/bulkEscalationGeneration'} component={BulkEscalationGeneration}/>
        <Route exact path={base + '/pgr/serviceTypeCreate'} component={serviceTypeCreate}/>
        <Route exact name="serviceTypeCreate" path={base + '/pgr/serviceTypeCreate/:type/:id'} component={serviceTypeCreate}/>
        <Route exact path={base + '/pgr/viewOrUpdateServiceType/:type'} component={viewOrUpdateServiceType}/>
        <Route exact path={base + '/pgr/viewServiceType/:type/:id'} component={viewServiceType}/>
        <Route exact path={base + '/pgr/viewServiceGroup/:id'} component={ViewServiceGroup}/>
        <Route exact path={base + '/pgr/viewEscalation'} component={ViewEscalation}/>
        <Route exact path={base + '/pgr/defineEscalation'} component={DefineEscalation}/>
        <Route exact path={base + '/pgr/searchEscalationTime'} component={SearchEscalation}/>
        <Route exact path={base + '/pgr/defineEscalationTime'} component={DefineEscalationTime}/>
        <Route exact path={base + '/pgr/createServiceType'} component={ServiceTypeCreate}/>
        <Route exact path={base + '/report/:reportName'} component={Report}/>


    </Switch>
  </main>
)}


export default(
  <Main/>
);
