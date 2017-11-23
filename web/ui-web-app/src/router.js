import React from 'react';
import {Switch} from 'react-router-dom';
import Route from "./AuthRoute";

import Login from './components/contents/Login';
import Dashboard from './components/contents/Dashboard';
//import ProfileEdit from './components/contents/settings/profileEdit';

//ADMINISTRATION
import searchUserRole from './components/contents/administration/userManagement/searchUserRole';
import updateUserRole from './components/contents/administration/userManagement/updateUserRole';

//CITIZEN SERVICES
import VisibleNewServiceRequest from './components/contents/citizenServices/VisibleNewServiceRequest';
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
// import AddDemand from './components/contents/propertyTax/master/addDemand';
//
// //Property tax
// import PropertyTaxSearch from './components/contents/propertyTax/master/PropertyTaxSearch';
// import Test from './components/contents/propertyTax/master/Test';
// import FloorType from './components/contents/propertyTax/master/FloorType';
// import RoofType from './components/contents/propertyTax/master/RoofType';
// import WallType from './components/contents/propertyTax/master/WallType';
// import WoodType from './components/contents/propertyTax/master/WoodType';
// import UsageType from './components/contents/propertyTax/master/UsageType';
// import PropertyType from './components/contents/propertyTax/master/PropertyType';
import EditDemands from './components/non-framework/wc/editDemands';

// import Occupancy from './components/contents/propertyTax/master/Occupancy';
// import MutationReason from './components/contents/propertyTax/master/MutationReason';
// import BuildingClassification from './components/contents/propertyTax/master/BuildingClassification';
// import CreateProperty from './components/contents/propertyTax/master/CreateProperty';
// import DataEntry from './components/contents/propertyTax/master/DataEntry';
// import ViewProperty from './components/contents/propertyTax/master/viewProperty';
// import ViewDCB from './components/non-framework/pt/viewDCB';
// import Workflow from './components/contents/propertyTax/master/workflow';
// import Acknowledgement from './components/contents/propertyTax/master/Acknowledgement';
// import DataEntryAcknowledgement from './components/contents/propertyTax/master/Acknowledgement_dataEntry';
// import DemandAcknowledgement from './components/contents/propertyTax/master/Acknowledgement_demand';
// import InboxAcknowledgement from './components/contents/propertyTax/master/Acknowledgement_inbox';
//
//
// import CreateVacantLand from'./components/contents/propertyTax/master/CreateVacantLand';

import PayTaxCreate from './components/non-framework/collection/master/paytax/PayTaxCreate';
import Transaction from './components/framework/transaction';
//import Inbox from './components/framework/inbox';

import createPenaltyRates from './components/non-framework/tl/masters/create/createPenaltyRates';
import updatePenaltyRates from './components/non-framework/tl/masters/update/updatePenaltyRates';
import viewPenaltyRates from './components/non-framework/tl/masters/view/viewPenaltyRates';
import penaltyRatesSearch from './components/non-framework/tl/masters/search/penaltyRatesSearch';
import penaltyRatesUpdateSearch from './components/non-framework/tl/masters/search/penaltyRatesUpdateSearch';

import createFeeMatrix from './components/non-framework/tl/masters/createFeeMatrix';
import updateFeeMatrix from './components/non-framework/tl/masters/updateFeeMatrix';
import viewFeeMatrix from './components/non-framework/tl/masters/viewFeeMatrix';

import CreateLicenseDocumentType from './components/non-framework/tl/masters/CreateLicenseDocumentType';
import UpdateSubCategory from './components/non-framework/tl/masters/update/UpdateSubCategory';
import createSubCategory from './components/non-framework/tl/masters/create/createSubCategory';
import LegacyLicenseCreate from './components/non-framework/tl/transaction/LegacyLicenseCreate';
import viewLegacyLicense from './components/non-framework/tl/transaction/viewLegacyLicense';
import LegacyLicenseSearch from './components/non-framework/tl/transaction/LegacyLicenseSearch';
import viewLicense from './components/non-framework/tl/transaction/viewLicense';
import VisibleNewTradeLicense from './components/non-framework/tl/transaction/NewTradeLicense';
import NoticeSearchLicense from './components/non-framework/tl/search/NoticeSearch';

import ReceiptView from './components/non-framework/collection/master/receipt/view';
import Employee from './components/non-framework/employee/create';
import EmployeeSearch from './components/non-framework/employee/search';
import SearchLegacyWc from './components/non-framework/wc/search';
import updateConnection from './components/non-framework/wc/connection-workflow';
import NoDues from './components/non-framework/citizenServices/NoDues';
import PayTax from './components/non-framework/citizenServices/PayTax';

import ComingSoon from './components/non-framework/citizenServices/ComingSoon.js';
import CS_WaterConnection from './components/non-framework/citizenServices/wc/create.js';
import CS_VIEW_WaterConnection from './components/non-framework/citizenServices/wc/view.js';
import ViewWc from './components/non-framework/wc/viewWc';
import ServiceRequests from './components/non-framework/citizenServices/ServiceRequestSearch.js';
import CS_FireNoc from './components/non-framework/citizenServices/buildingPlan/create.js';
import CS_VIEW_FireNoc from './components/non-framework/citizenServices/buildingPlan/view.js';
import Payment from './components/non-framework/citizenServices/payment';
import ReceiptDownload from './components/non-framework/citizenServices/ReceiptDownload.js';
import CS_TradeLicense from './components/non-framework/citizenServices/tl/create.js';
import CS_VIEW_TradeLicense from './components/non-framework/citizenServices/tl/view.js';
import CertificateView from './components/non-framework/citizenServices/SRNView.js';
import createLegacy from './components/non-framework/wc/createLegacy';

import createServiceCharge from './components/non-framework/wc/masters/serviceCharge/create';
import createWc from './components/non-framework/wc/createWc';
import createVoucher from './components/non-framework/egf/transaction/createVoucher';

import acknowledgementWc from './components/non-framework/wc/acknowledgement';

import NoMatch from './components/common/NoMatch';

const base = "";
var fwRoutes;
var assets;
var works;
var perfManagement;
var legal;
var pgr;
var swm;
var inventory;
var LegalTemplateParser = (process.env.NODE_ENV === "production") ? require('egov-legal/specifications/templateParser/legalTemplateParser') : require('./development/legal/lib/specifications/templateParser/legalTemplateParser');

if(process.env.NODE_ENV === "production") {
    fwRoutes = require('egov-ui-react-framework');
    assets = require('egov-asset');
    works = require('egov-works');
    perfManagement = require('egov-perfManagement');
    pgr = require('egov-pgr');
    inventory = require('egov-inventory');
} else {
    fwRoutes = require('./development/ui-react-framework/lib/index');
    assets = require('./development/asset/lib/index');
    works = require('./development/works/lib/index');
    perfManagement = require('./development/perfManagement/lib/index');
    pgr = require('./development/pgr/lib/index');
    inventory = require('./development/inventory/lib/index');
}

const Main = () => {
    return (
    <main style={{"marginBottom": "50px"}}>
    <Switch>
        {
            fwRoutes && fwRoutes.routes && fwRoutes.routes.length && fwRoutes.routes.map(function(r) {
                return (<Route exact path= {r.route} component={r.component}/>)
            })
        }

        {
            assets && assets.routes && assets.routes.length && assets.routes.map(function(r) {
                return (<Route exact path= {r.route} component={r.component}/>)
            })
        }

        {
            perfManagement && perfManagement.routes && perfManagement.routes.length && perfManagement.routes.map(function(r) {
                return (<Route exact path= {r.route} component={r.component}/>)
            })
        }

        {
            pgr && pgr.routes && pgr.routes.length && pgr.routes.map(function(r) {
                return (<Route exact path= {r.route} component={r.component}/>)
            })
        }

        <Route exact path= {base + '/:tenantId?'} component={Login}/>
        <Route exact path={base + '/service/request/search'} component={ServiceRequests}/>
        <Route exact path={base + '/coming/soon'} component={ComingSoon}/>
        <Route exact path={base + '/employee/:action/:id?'} component={Employee}/>
        {/*<Route exact path={base + '/prd/profileEdit'} component={ProfileEdit}/>*/}
        <Route exact path={base+'/prd/dashboard'} component={Dashboard}/>
        <Route exact path={base+'/administration/searchUserRole'} component={searchUserRole}/>
        <Route exact path={base+'/administration/updateUserRole/:userId'} component={updateUserRole}/>
        <Route exact path={base+'/services/apply/:serviceCode/:serviceName'} component={VisibleNewServiceRequest}/>
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


          {/*<Route exact path={base+'/propertyTax/CreateVacantLand'} component={CreateVacantLand}/>
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
		  <Route exact path={base+'/propertyTax/view-dcb/:searchParam/:type?'} component={ViewDCB}/>
          <Route exact path={base+'/propertyTax/workflow/:searchParam/:type?'} component={Workflow}/>
		  <Route exact path={base+'/propertyTax/acknowledgement'} component={Acknowledgement}/>
		  <Route exact path={base+'/propertyTax/dataEntry-acknowledgement'} component={DataEntryAcknowledgement}/>
		  <Route exact path={base+'/propertyTax/demand-acknowledgement'} component={DemandAcknowledgement}/>
		  <Route exact path={base+'/propertyTax/inbox-acknowledgement'} component={InboxAcknowledgement}/>*/}
          <Route exact path= {base + '/transaction/:moduleName/:page/:businessService?/:consumerCode?'} component={Transaction}/>
		  {/*<Route exact path= {base + '/views/:moduleName/:master?/:id'} component={Inbox}/>*/}

      <Route exact path= {base + '/non-framework/tl/masters/create/createPenaltyRates'} component={createPenaltyRates}/>
      <Route exact path= {base + '/non-framework/tl/masters/update/updatePenaltyRates/:id'} component={updatePenaltyRates}/>
      <Route exact path= {base + '/non-framework/tl/masters/view/viewPenaltyRates/:id'} component={viewPenaltyRates}/>
      <Route exact path= {base + '/non-framework/tl/masters/search/penaltyRatesSearch'} component={penaltyRatesSearch}/>
      <Route exact path= {base + '/non-framework/tl/masters/search/penaltyRatesUpdateSearch'} component={penaltyRatesUpdateSearch}/>

      <Route exact path= {base + '/non-framework/tl/masters/createFeeMatrix'} component={createFeeMatrix}/>
      <Route exact path= {base + '/non-framework/tl/masters/updateFeeMatrix/:id'} component={updateFeeMatrix}/>
      <Route exact path= {base + '/non-framework/tl/masters/viewFeeMatrix/:id'} component={viewFeeMatrix}/>

      <Route exact path= {base + '/non-framework/tl/masters/CreateLicenseDocumentType'} component={CreateLicenseDocumentType}/>
      <Route exact path= {base + '/non-framework/tl/masters/update/UpdateSubCategory/:id'} component={UpdateSubCategory}/>
      <Route exact path= {base + '/non-framework/tl/masters/create/createSubCategory'} component={createSubCategory}/>
      <Route exact path= {base + '/non-framework/tl/transaction/LegacyLicenseCreate'} component={LegacyLicenseCreate}/>
      <Route exact path= {base + '/non-framework/tl/transaction/ApplyNewTradeLicense'} component={VisibleNewTradeLicense}/>
      <Route exact path= {base + '/non-framework/tl/transaction/LegacyLicenseSearch'} component={LegacyLicenseSearch}/>
      <Route exact path= {base + '/non-framework/tl/transaction/viewLegacyLicense/:licenseNumber'} component={viewLegacyLicense}/>
      <Route exact path= {base + '/non-framework/tl/transaction/:inbox/viewLicense/:id'} component={viewLicense}/>
      <Route exact path= {base + '/non-framework/tl/transaction/viewLicense/:id'} component={viewLicense}/>
      <Route exact path= {base + '/non-framework/tl/search/NoticeSearch'} component={NoticeSearchLicense}/>

      <Route exact path= {base + '/non-framework/collection/master/paytax/PayTaxCreate'} component={PayTaxCreate}/>
      <Route exact path= {base + '/non-framework/collection/receipt/view/:id'} component={ReceiptView}/>
      <Route exact path= {base + '/non-framework-cs/citizenServices/paytax/:status/:id/:paymentGateWayRes?'} component={PayTax}/>
      <Route exact path= {base + '/non-framework-cs/citizenServices/:moduleName/:status/:id/:paymentGateWayRes?'} component={NoDues}/>


      <Route exact path= {base + '/empsearch/:actionName'} component={EmployeeSearch}/>
      <Route exact path= {base+'/legacy/view/:id'} component={ViewLegacy}/>
      <Route exact path= {base+'/wc/addDemand/:upicNumber'} component={EditDemands}/>
      <Route exact path= {base+'/searchconnection/wc'} component={SearchLegacyWc}/>
      <Route exact path= {base+'/wc/application/update/:stateId'} component={updateConnection}/>
	    <Route exact path= {base+'/waterConnection/view/:id'} component={ViewWc}/>
      <Route exact path= {base + '/non-framework/citizenServices/create/:status/:id/:paymentGateWayRes?'} component={CS_WaterConnection}/>
      <Route exact path= {base + '/non-framework/citizenServices/view/:status/:id/:ackNo/:paymentGateWayRes?'} component={CS_VIEW_WaterConnection}/>
      <Route exact path= {base + '/non-framework/citizenServices/fireNoc/:status/:id/:paymentGateWayRes?'} component={CS_FireNoc}/>
      <Route exact path= {base + '/non-framework/citizenServices/fireNoc/:status/:id/:ackNo/:paymentGateWayRes?'} component={CS_VIEW_FireNoc}/>
      <Route exact path= {base + '/payment/response/redirect/:msg'} component={Payment}/>
      <Route exact path= {base + '/receipt/:page/:type/:cc/:sid'} component={ReceiptDownload}/>
      <Route exact path= {base + '/non-framework/citizenServices/tl/:status/:id/:paymentGateWayRes?'} component={CS_TradeLicense}/>
      <Route exact path= {base + '/non-framework/citizenServices/tl/:status/:id/:ackNo/:paymentGateWayRes?'} component={CS_VIEW_TradeLicense}/>
      <Route exact path= {base + '/service/request/view/:srn/:isCertificate'} component={CertificateView}/>
      <Route exact path= {base + '/createLegacy/wc/legacy'} component={createLegacy}/>
      <Route exact path= {base + '/non-framework/wc/masters/serviceCharge/create'} component={createServiceCharge}/>
      <Route exact path= {base + '/non-framework/wc/masters/serviceCharge/update/:id'} component={createServiceCharge}/>
      <Route exact path= {base + '/createWc/wc'} component={createWc}/>
      <Route exact path= {base + '/non-framework/egf/transaction/createVoucher'} component={createVoucher}/>
      <Route exact path= {base + '/wc/acknowledgement/:id/:status'} component={acknowledgementWc}/>
      <Route exact path= {base + '/print/notice/:legalTemplatePath'} component={LegalTemplateParser}/>
      <Route component={NoMatch}/>

    </Switch>
  </main>
)}


export default(
  <Main/>
);
