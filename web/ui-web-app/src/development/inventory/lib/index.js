import MaterialSearch from './master/material/MaterialSearch';
import MaterialStoreMappingSearch from './master/materialstoremapping/MaterialStoreMappingSearch';
import StoreSearch from './master/store/StoreSearch';
import SupplierSearch from './master/supplier/SupplierSearch';
import IndentSearch from './transaction/indent/IndentSearch';
import PriceListSearch from './transaction/pricelist/PriceListSearch';




var routes = [{
  Component: MaterialSearch,
  route: '/non-framework/inventory/master/material'
}, {
  Component: MaterialStoreMappingSearch,
  route: '/non-framework/inventory/master/materialstoremapping'
},
{
  Component: StoreSearch,
  route: '/non-framework/inventory/master/store'
},
{
  Component: SupplierSearch,
  route: '/non-framework/inventory/master/supplier'
},
{
  Component: IndentSearch,
  route: '/non-framework/inventory/indent'
},
{
  Component: PriceListSearch,
  route: '/non-framework/inventory/transaction/pricelist'
}];

export {
  routes
};
