
import React from 'react';
import {Switch,Route} from 'react-router-dom';
import PropertyTaxSearch from './components/contents/PropertyTaxSearch';
import Test from './components/contents/Test';
import FloorType from './components/contents/FloorType';
import RoofType from './components/contents/RoofType';
import WallType from './components/contents/WallType';
import WoodType from './components/contents/WoodType';
import UsageType from './components/contents/UsageType';
import PropertyType from './components/contents/PropertyType';
import Occupancy from './components/contents/Occupancy';
import MutationReason from './components/contents/MutationReason';
import BuildingClassification from './components/contents/BuildingClassification';

import CreateProperty from './components/contents/CreateProperty';

import CreateVacantLand from'./components/contents/CreateVacantLand'

// import App from "./components/App";

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/propertyTax/CreateVacantLand' component={CreateVacantLand}/>
      <Route exact path='/propertyTax' component={PropertyTaxSearch}/>
      <Route exact path='/propertyTax/test' component={Test}/>
      <Route exact path='/propertyTax/floor-type' component={FloorType}/>
      <Route exact path='/propertyTax/roof-type' component={RoofType}/>
      <Route exact path='/propertyTax/wall-type' component={WallType}/>
      <Route exact path='/propertyTax/wood-type' component={WoodType}/>
      <Route exact path='/propertyTax/usage-type' component={UsageType}/>
      <Route exact path='/propertyTax/property-type' component={PropertyType}/>
      <Route exact path='/propertyTax/mutation-reason' component={MutationReason}/>
      <Route exact path='/propertyTax/building-classification' component={BuildingClassification}/>
      <Route exact path='/propertyTax/create-property' component={CreateProperty}/>
    </Switch>
  </main>
)


export default(
  <Main/>
);
