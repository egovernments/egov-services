import grievanceCreate from './grievanceCreate';
import grievanceView from './grievanceView';
import grievanceSearch from './grievanceSearch';
import ReceivingCenterCreate from './master/receivingCenter/receivingCenterCreate';
import ViewEditReceivingCenter from './master/receivingCenter/viewEditReceivingCenter';
import ViewReceivingCenter from './master/receivingCenter/viewReceivingCenter';
import receivingModeCreate from './master/receivingMode/receivingModeCreate';
import viewOrUpdateReceivingMode from './master/receivingMode/viewOrUpdateReceivingMode';
import ServiceGroupCreate from './master/serviceGroup/serviceGroupCreate';
import ViewEditServiceGroup from './master/serviceGroup/viewEditServiceGroup';
import viewReceivingMode from './master/receivingMode/viewReceivingMode';
import createRouter from './master/router/create';
import searchRouter from './master/router/search';
import routerGeneration from './master/router/routerGeneration';
import BulkEscalationGeneration from './master/escalation/bulkEscalationGeneration';
import serviceTypeCreate from './master/serviceType/serviceTypeCreate';
import viewOrUpdateServiceType from './master/serviceType/viewOrUpdateServiceType';
import viewServiceType from './master/serviceType/viewServiceType';
import ViewServiceGroup from './master/serviceGroup/viewServiceGroup';
import ViewEscalation from './master/escalation/viewEscalation';
import DefineEscalation from './master/escalation/defineEscalation';
import SearchEscalation from './master/escalationTime/searchEscalation';
import DefineEscalationTime from './master/escalationTime/defineEscalationTime';
import PGRDashboard from './dashboards/index';
import PgrAnalytics from './dashboards/PgrAnalytics';

var routes = [{
	component: grievanceCreate,
	route: '/pgr/createGrievance'
},{
	component: grievanceView,
	route: '/pgr/viewGrievance/:srn'
},{
	component: grievanceSearch,
	route: '/pgr/searchGrievance'
},{
	component: ReceivingCenterCreate,
	route: '/pgr/createReceivingCenter/:id?'
}, {
	component: ReceivingCenterCreate,
	route: '/pgr/createReceivingCenter'
}, {
	component: ViewEditReceivingCenter,
	route: '/pgr/receivingCenter/view'
}, {
	component: ViewEditReceivingCenter,
	route: '/pgr/receivingCenter/edit'
},{
	component: ViewReceivingCenter,
	route: '/pgr/viewReceivingCenter/:id'
},{
	component: receivingModeCreate,
	route: '/pgr/receivingModeCreate'
}, {
	component: receivingModeCreate,
	route: '/pgr/receivingModeCreate/:type/:id'
},{
	component: viewOrUpdateReceivingMode,
	route: '/pgr/viewOrUpdateReceivingMode/:type'
},{
	component: ServiceGroupCreate,
	route: '/pgr/updateServiceGroup/:id?'
},{
	component: ServiceGroupCreate,
	route: '/pgr/createServiceGroup'
},{
	component: ViewEditServiceGroup,
	route: '/pgr/serviceGroup/view'
},{
	component: ViewEditServiceGroup,
	route: '/pgr/serviceGroup/edit'
},{
	component: viewReceivingMode,
	route: '/pgr/viewReceivingMode/:type/:id'
},{
	component: createRouter,
	route: '/pgr/createRouter/:type/:id'
},{
	component: createRouter,
	route: '/pgr/createRouter/:type/:id'
},{
	component: searchRouter,
	route: '/pgr/createRouter'
},{
	component: routerGeneration,
	route: '/pgr/routerGeneration'
},{
	component: BulkEscalationGeneration,
	route: '/pgr/bulkEscalationGeneration'
},{
	component: serviceTypeCreate,
	route: '/pgr/serviceTypeCreate'
}, {
	component: serviceTypeCreate,
	route: '/pgr/serviceTypeCreate/:type/:id'
}, {
	component: serviceTypeCreate,
	route: '/pgr/createServiceType'
}, {
	component: viewOrUpdateServiceType,
	route: '/pgr/viewOrUpdateServiceType/:type'
}, {
	component: viewServiceType,
	route: '/pgr/viewServiceType/:type/:id'
}, {
	component: ViewServiceGroup,
	route: '/pgr/viewServiceGroup/:id'
}, {
	component: ViewEscalation,
	route: '/pgr/viewEscalation'
}, {
	component: DefineEscalation,
	route: '/pgr/defineEscalation'
}, {
	component: SearchEscalation,
	route: '/pgr/searchEscalationTime'
}, {
	component: DefineEscalationTime,
	route: '/pgr/defineEscalationTime'
}, {
	component: PGRDashboard,
	route: '/pgr/dashboard'
}, {
	component: PgrAnalytics,
	route: '/pgr/analytics'
}];

export {
	routes
};
