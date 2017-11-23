import abstractEstimate from './transaction/abstractEstimate';
import viewAbstractEstimate from './transaction/viewAbstractEstimate';


var routes = [{
  Component: abstractEstimate,
  route: '/non-framework/works/transaction/abstractEstimate'
}, {
  Component: viewAbstractEstimate,
  route: '/non-framework/works/transaction/viewAbstractEstimate/:id'
}];

export {
  routes
};
