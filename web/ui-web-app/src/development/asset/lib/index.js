import assetImmovableCreate from './master/assetImmovableCreate';
import assetImmovableView from './master/assetImmovableView';
import assetMovableCreate from './master/assetMovableCreate';
import assetMovableView from './master/assetMovableView';
import acknowledgeDepreciation from './acknowledgeDepreciation';
import transactionGeneral from './transactionGeneral';
import transactionRevaluation from './transactionRevaluation';
import transactionTransfer from './transactionTransfer';

var routes = [{
	component: assetImmovableCreate,
	route: '/non-framework/asset/master/assetImmovableCreate/:id?'
}, {
	component: assetImmovableCreate,
	route: '/non-framework/asset/master/assetImmovableCreate'
}, {
	component: assetImmovableView,
	route: '/non-framework/asset/master/assetImmovableView/:id'
}, {
	component: assetMovableCreate,
	route: '/non-framework/asset/master/assetMovableCreate/:id?'
}, {
	component: assetMovableCreate,
	route: '/non-framework/asset/master/assetMovableCreate'
}, {
	component: assetMovableView,
	route: '/non-framework/asset/master/assetMovableView/:id'
}, {
	component: acknowledgeDepreciation,
	route: '/non-framework/asset/acknowledgeDepreciation/:id'
}, {
	component: transactionGeneral,
	route: '/transactionTransfer/asset/generalAsset'
}, {
	component: transactionRevaluation,
	route: '/transactionRevaluation/asset/revaluationAsset'
}, {
	component: transactionTransfer,
	route: '/transactionTransfer/asset/translateAsset'
}];

export {
	routes
};
