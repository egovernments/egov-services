import abstractEstimate from './transaction/abstractEstimate';
import viewAbstractEstimate from './transaction/viewAbstractEstimate';


var routes = [{
  Component: abstractEstimate
  route: './components/non-framework/works/transaction/abstractEstimate'
}, {
  Component: viewAbstractEstimate
  route: './components/non-framework/works/transaction/viewAbstractEstimate'
}];

module.exports = {
  routes
};
