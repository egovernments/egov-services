import React from 'react';
import {Switch,Route} from 'react-router-dom';

import Login from './components/contents/Login';
import Dashboard from './components/contents/Dashboard';
import ProfileEdit from './components/contents/settings/profileEdit';

//ADMINISTRATION
import searchUserRole from './components/contents/administration/userManagement/searchUserRole';
import updateUserRole from './components/contents/administration/userManagement/updateUserRole';

//CITIZEN SERVICES
import VisibleNewServiceRequest from './components/contents/citizenServices/VisibleNewServiceRequest';

//PGR
import grievanceCreate from './components/contents/pgr/grievanceCreate';
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

//WC
import CategoryTypeCreate from './components/contents/wc/master/categoryType/categoryTypeCreate';
import ViewEditCategoryType from './components/contents/wc/master/categoryType/viewEditCategoryType';
import ViewCategoryType from './components/contents/wc/master/categoryType/viewCategoryType';

import WaterSourceTypeCreate from './components/contents/wc/master/waterSourceType/waterSourceTypeCreate';
import ViewEditWaterSourceType from './components/contents/wc/master/waterSourceType/viewEditWaterSourceType';
import ViewWaterSourceType from './components/contents/wc/master/waterSourceType/viewWaterSourceType';

import SupplyTypeCreate from './components/contents/wc/master/supplyType/supplyTypeCreate';
import ViewEditSupplyType from './components/contents/wc/master/supplyType/viewEditSupplyType';
import ViewSupplyType from './components/contents/wc/master/supplyType/viewSupplyType';

import PipeSizeCreate from './components/contents/wc/master/pipeSize/pipeSizeCreate';
import ViewEditPipeSize from './components/contents/wc/master/pipeSize/viewEditPipeSize';
import ViewPipeSize from './components/contents/wc/master/pipeSize/viewPipeSize';

import DocumentTypeCreate from './components/contents/wc/master/documentType/documentTypeCreate';
import ViewEditDocumentType from './components/contents/wc/master/documentType/viewEditDocumentType';
import ViewDocumentType from './components/contents/wc/master/documentType/viewDocumentType';

import DocumentTypeApplicationTypeCreate from './components/contents/wc/master/documentTypeApplicationType/documentTypeApplicationTypeCreate';
import ViewEditDocumentTypeApplicationType from './components/contents/wc/master/documentTypeApplicationType/viewEditDocumentTypeApplicationType';
import ViewDocumentTypeApplicationType from './components/contents/wc/master/documentTypeApplicationType/viewDocumentTypeApplicationType';

import AddDemandWc from './components/contents/wc/master/addDemand';
import ViewLegacy from './components/non-framework/wc/viewLegacy';
import AddDemand from './components/contents/propertyTax/master/addDemand';

//Property tax
import PropertyTaxSearch from './components/contents/propertyTax/master/PropertyTaxSearch';
import Test from './components/contents/propertyTax/master/Test';
import FloorType from './components/contents/propertyTax/master/FloorType';
import RoofType from './components/contents/propertyTax/master/RoofType';
import WallType from './components/contents/propertyTax/master/WallType';
import WoodType from './components/contents/propertyTax/master/WoodType';
import UsageType from './components/contents/propertyTax/master/UsageType';
import PropertyType from './components/contents/propertyTax/master/PropertyType';
import EditDemands from './components/non-framework/wc/editDemands';

// import Occupancy from './components/contents/propertyTax/master/Occupancy';
import MutationReason from './components/contents/propertyTax/master/MutationReason';
import BuildingClassification from './components/contents/propertyTax/master/BuildingClassification';
import CreateProperty from './components/contents/propertyTax/master/CreateProperty';
import DataEntry from './components/contents/propertyTax/master/DataEntry';
import ViewProperty from './components/contents/propertyTax/master/viewProperty';
import Acknowledgement from './components/contents/propertyTax/master/Acknowledgement';
import DataEntryAcknowledgement from './components/contents/propertyTax/master/Acknowledgement_dataEntry';
import DemandAcknowledgement from './components/contents/propertyTax/master/Acknowledgement_demand';

import CreateVacantLand from'./components/contents/propertyTax/master/CreateVacantLand';
import Create from './components/framework/create';
// import CreateTwo from './components/framework/createTwo';

import View from './components/framework/view';
import Search from './components/framework/search';
import Transaction from './components/framework/transaction';
import Inbox from './components/framework/inbox';

import LegacyLicenseCreate from './components/non-framework/tl/transaction/LegacyLicenseCreate';


import ReceiptView from './components/non-framework/collection/master/receipt/view';
import Employee from './components/non-framework/employee/create';
import EmployeeSearch from './components/non-framework/employee/search';

const base = "";

const Main = () => {
    return (
    <main style={{"marginBottom": "50px"}}>
    <Switch>
        <Route exact path= {base + '/:tenantId?'} component={Login}/>
	     <Route exact path= {base + '/view/:moduleName/:master?/:id'} component={View}/>
        <Route exact path= {base + '/search/:moduleName/:master?/:action'} component={Search}/>
        <Route exact path={base + '/employee/:action/:id?'} component={Employee}/>
        <Route exact path={base + '/prd/profileEdit'} component={ProfileEdit}/>
        <Route exact path={base+'/prd/dashboard'} component={Dashboard}/>
        <Route exact path={base+'/administration/searchUserRole'} component={searchUserRole}/>
        <Route exact path={base+'/administration/updateUserRole/:userId'} component={updateUserRole}/>
        <Route exact path={base+'/services/apply/:serviceCode/:serviceName'} component={VisibleNewServiceRequest}/>
        <Route exact path={base+'/pgr/createGrievance'} component={grievanceCreate}/>
        <Route exact path={base+'/pgr/viewGrievance/:srn'} component={grievanceView}/>
        <Route exact path={base+'/pgr/searchGrievance'} component={grievanceSearch}/>
        <Route exact name="createReceivingCenter" path={base+'/pgr/createReceivingCenter/:id?'} component={ReceivingCenterCreate}/>
        <Route exact path={base+'/pgr/createReceivingCenter'} component={ReceivingCenterCreate}/>
        <Route exact path={base+'/pgr/receivingCenter/view'} component={ViewEditReceivingCenter}/>
        <Route exact path={base+'/pgr/receivingCenter/edit'} component={ViewEditReceivingCenter}/>
        <Route exact path={base+'/pgr/viewReceivingCenter/:id'} component={ViewReceivingCenter}/>
        <Route exact path={base+'/pgr/createRouter/:type/:id'} component={createRouter}/>
        <Route exact path={base+'/pgr/createRouter'} component={createRouter}/>
        <Route exact path={base+'/pgr/routerGeneration'} component={routerGeneration}/>
        <Route exact path={base+'/pgr/searchRouter/:type'} component={searchRouter}/>
        <Route exact path={base+'/pgr/receivingModeCreate'} component={receivingModeCreate}/>
        <Route exact name='receivingModeCreate' path={base+'/pgr/receivingModeCreate/:type/:id'} component={receivingModeCreate}/>
        <Route exact path={base+'/pgr/viewOrUpdateReceivingMode/:type'} component={viewOrUpdateReceivingMode}/>
        <Route exact path={base+'/pgr/viewReceivingMode/:type/:id'} component={viewReceivingMode}/>
        <Route exact name="createServiceGroup" path={base+'/pgr/updateServiceGroup/:id?'} component={ServiceGroupCreate}/>
        <Route exact path={base+'/pgr/createServiceGroup'} component={ServiceGroupCreate}/>
        <Route exact path={base+'/pgr/serviceGroup/view'} component={ViewEditServiceGroup}/>
        <Route exact path={base+'/pgr/serviceGroup/edit'} component={ViewEditServiceGroup}/>
        <Route exact path={base+'/pgr/bulkEscalationGeneration'} component={BulkEscalationGeneration}/>
        <Route exact path={base+'/pgr/serviceTypeCreate'} component={serviceTypeCreate}/>
        <Route exact name="serviceTypeCreate" path={base+'/pgr/serviceTypeCreate/:type/:id'} component={serviceTypeCreate}/>
        <Route exact path={base+'/pgr/viewOrUpdateServiceType/:type'} component={viewOrUpdateServiceType}/>
        <Route exact path={base+'/pgr/viewServiceType/:type/:id'} component={viewServiceType}/>
        <Route exact path={base+'/pgr/viewServiceGroup/:id'} component={ViewServiceGroup}/>
        <Route exact path={base+'/pgr/viewEscalation'} component={ViewEscalation}/>
        <Route exact path={base+'/pgr/defineEscalation'} component={DefineEscalation}/>
        <Route exact path={base+'/pgr/searchEscalationTime'} component={SearchEscalation}/>
        <Route exact path={base+'/pgr/defineEscalationTime'} component={DefineEscalationTime}/>
        <Route exact path={base+'/pgr/createServiceType'} component={ServiceTypeCreate}/>
        <Route exact path={base+'/report/:moduleName/:reportName'} component={Report}/>



        <Route exact path={base+'/wc/createCategoryType'} component={CategoryTypeCreate}/>
        <Route exact name="createCategoryType" path={base+'/wc/createCategoryType/:id?'} component={CategoryTypeCreate}/>
        <Route exact path={base+'/wc/categoryType/view'} component={ViewEditCategoryType}/>
        <Route exact path={base+'/wc/categoryType/edit'} component={ViewEditCategoryType}/>
        <Route exact path={base+'/wc/viewCategoryType/:id'} component={ViewCategoryType}/>


        <Route exact path={base+'/wc/createWaterSourceType'} component={WaterSourceTypeCreate}/>
        <Route exact name="createWaterSourceType" path={base+'/wc/createWaterSourceType/:id?'} component={WaterSourceTypeCreate}/>
        <Route exact path={base+'/wc/waterSourceType/view'} component={ViewEditWaterSourceType}/>
        <Route exact path={base+'/wc/waterSourceType/edit'} component={ViewEditWaterSourceType}/>
        <Route exact path={base+'/wc/viewWaterSourceType/:id'} component={ViewWaterSourceType}/>

        <Route exact path={base+'/wc/createSupplyType'} component={SupplyTypeCreate}/>
        <Route exact name="createSupplyType" path={base+'/wc/createSupplyType/:id?'} component={SupplyTypeCreate}/>
        <Route exact path={base+'/wc/supplyType/view'} component={ViewEditSupplyType}/>
        <Route exact path={base+'/wc/supplyType/edit'} component={ViewEditSupplyType}/>
        <Route exact path={base+'/wc/viewSupplyType/:id'} component={ViewSupplyType}/>

        <Route exact path={base+'/wc/createPipeSize'} component={PipeSizeCreate}/>
        <Route exact name="createPipeSize" path={base+'/wc/createPipeSize/:id?'} component={PipeSizeCreate}/>
        <Route exact path={base+'/wc/pipeSize/view'} component={ViewEditPipeSize}/>
        <Route exact path={base+'/wc/pipeSize/edit'} component={ViewEditPipeSize}/>
        <Route exact path={base+'/wc/viewPipeSize/:id'} component={ViewPipeSize}/>

        <Route exact path={base+'/wc/createDocumentType'} component={DocumentTypeCreate}/>
        <Route exact name="createDocumentType" path={base+'/wc/createDocumentType/:id?'} component={DocumentTypeCreate}/>
        <Route exact path={base+'/wc/documentType/view'} component={ViewEditDocumentType}/>
        <Route exact path={base+'/wc/documentType/edit'} component={ViewEditDocumentType}/>
        <Route exact path={base+'/wc/documentType/:id'} component={ViewDocumentType}/>

        <Route exact path={base+'/wc/addDemand'} component={AddDemandWc}/>

        <Route exact path={base+'/wc/createDocumentTypeApplicationType'} component={DocumentTypeApplicationTypeCreate}/>
       <Route exact name="createDocumentTypeApplicationType" path={base+'/wc/createDocumentTypeApplicationType/:id?'} component={DocumentTypeApplicationTypeCreate}/>
       <Route exact path={base+'/wc/documentTypeApplicationType/view'} component={ViewEditDocumentTypeApplicationType}/>
       <Route exact path={base+'/wc/documentTypeApplicationType/edit'} component={ViewEditDocumentTypeApplicationType}/>
       <Route exact path={base+'/wc/documentTypeApplicationType/:id'} component={ViewDocumentTypeApplicationType}/>


          <Route exact path={base+'/propertyTax/CreateVacantLand'} component={CreateVacantLand}/>
          <Route exact path={base+'/propertyTax/search'} component={PropertyTaxSearch}/>
          <Route exact path={base+'/propertyTax/test'} component={Test}/>
          <Route exact path={base+'/propertyTax/floor-type'} component={FloorType}/>
          <Route exact path={base+'/propertyTax/roof-type'} component={RoofType}/>
          <Route exact path={base+'/propertyTax/wall-type'} component={WallType}/>
          <Route exact path={base+'/propertyTax/wood-type'} component={WoodType}/>
          <Route exact path={base+'/propertyTax/usage-type'} component={UsageType}/>
          <Route exact path={base+'/propertyTax/property-type'} component={PropertyType}/>
          <Route exact path={base+'/propertyTax/mutation-reason'} component={MutationReason}/>
          <Route exact path={base+'/propertyTax/building-classification'} component={BuildingClassification}/>
          <Route exact path={base+'/propertyTax/create-property'} component={CreateProperty}/>
		  <Route exact path={base+'/propertyTax/addDemand/:upicNumber'} component={AddDemand}/>
		  <Route exact path={base+'/propertyTax/create-dataEntry'} component={DataEntry}/>
		  <Route exact path={base+'/propertyTax/view-property/:searchParam/:type?'} component={ViewProperty}/>
		  <Route exact path={base+'/propertyTax/acknowledgement'} component={Acknowledgement}/>
		  <Route exact path={base+'/propertyTax/dataEntry-acknowledgement'} component={DataEntryAcknowledgement}/>
		  <Route exact path={base+'/propertyTax/demand-acknowledgement'} component={DemandAcknowledgement}/>
		  <Route exact path= {base + '/create/:moduleName/:master?/:id?'} component={Create}/>
        {/*<Route exact path= {base + '/create/:moduleName/:master?/:id?'} component={Create}/>*/}
          <Route exact path= {base + '/update/:moduleName/:master?/:id?'} component={Create}/>
          <Route exact path= {base + '/transaction/:moduleName/:page'} component={Transaction}/>
		  <Route exact path= {base + '/views/:moduleName/:master?/:id'} component={Inbox}/>

      <Route exact path= {base + '/non-framework/tl/transaction/LegacyLicenseCreate/:id?'} component={LegacyLicenseCreate}/>

      <Route exact path= {base + '/non-framework/collection/receipt/view/:id'} component={ReceiptView}/>
      <Route exact path={base + '/empsearch/:actionName'} component={EmployeeSearch}/>
      <Route exact path={base+'/legacy/view/:id'} component={ViewLegacy}/>
      <Route exact path={base+'/wc/addDemand/:upicNumber'} component={EditDemands}/>


    </Switch>
  </main>
)}


export default(
  <Main/>
);
