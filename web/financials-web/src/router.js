import React from 'react';
import {Switch, Route} from 'react-router-dom';
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

import CreateNewFunction from './components/contents/CreateNewFunction';
import ViewFunction from './components/contents/ViewFunction'

// import App from "./components/App";

const Main = () => (
  <main>
    <Switch>
      <Route exact path='/' component={CreateNewFunction}/>
      <Route exact path='/view-function' component={ViewFunction}/>
      <Route exact path='/PropertyTaxSearch' component={PropertyTaxSearch}/>
      <Route exact path='/test' component={Test}/>
      <Route exact path='/floor-type' component={FloorType}/>
      <Route exact path='/roof-type' component={RoofType}/>
      <Route exact path='/wall-type' component={WallType}/>
      <Route exact path='/wood-type' component={WoodType}/>
      <Route exact path='/usage-type' component={UsageType}/>
      <Route exact path='/property-type' component={PropertyType}/>
      <Route exact path='/mutation-reason' component={MutationReason}/>
      <Route exact path='/building-classification' component={BuildingClassification}/>

    </Switch>
  </main>
)

export default(<Main/>);
