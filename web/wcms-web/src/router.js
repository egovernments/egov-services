import React from 'react';
import {Switch,Route} from 'react-router-dom';
import UsageType from './components/contents/masters/UsageType';
import CategoryType from './components/contents/masters/CategoryType';
import PipeSize from './components/contents/masters/PipeSize';
import PropertyPipeSize from './components/contents/masters/PropertyPipeSize';
import PropertyCategory from './components/contents/masters/PropertyCategory';
import PropertyUsage from './components/contents/masters/PropertyUsage';
import DocumentName from './components/contents/masters/DocumentName';
import WaterTapConnection from './components/contents/WaterTapConnection';
import ShowUsageType from './components/contents/ShowUsageType';
import ShowCategoryType from './components/contents/ShowCategoryType';
import ShowPipeSize from './components/contents/ShowPipeSize';
import ShowDocumentName from './components/contents/ShowDocumentName';
import ShowPropertyPipeSize from './components/contents/ShowPropertyPipeSize';
import ShowPropertyCategory from './components/contents/ShowPropertyCategory';
import ShowPropertyUsage from './components/contents/ShowPropertyUsage';
import AcknowledgementSlip from './components/contents/AcknowledgementSlip';







const Main = () => (
  <main>
    <Switch>
    <Route exact path='/' component={PropertyPipeSize}/>
    <Route exact path='/masters/PipeSize' component={PipeSize}/>

    <Route exact path='/ShowCategoryType' component={ShowCategoryType}/>

    <Route exact path='/CategoryType' component={CategoryType}/>
    <Route exact path='/ShowPropertyUsage' component={ShowPropertyUsage}/>
    <Route exact path='/ShowPropertyCategory' component={ShowPropertyCategory}/>
    <Route exact path='/ShowPropertyPipeSize' component={ShowPropertyPipeSize}/>
    <Route exact path='/ShowDocumentName' component={ShowDocumentName}/>
    <Route exact path='/DocumentName' component={DocumentName}/>
    <Route exact path='/masters/PropertyCategory' component={PropertyCategory}/>
    <Route exact path='/masters/PropertyUsage' component={PropertyUsage}/>
    <Route exact path='/masters/PropertyCategory' component={PropertyCategory}/>
      <Route exact path='/AcknowledgementSlip' component={AcknowledgementSlip}/>
      <Route exact path='/ShowUsageType' component={ShowUsageType}/>
      <Route exact path='/WaterTapConnection' component={WaterTapConnection}/>
      <Route exact path='/masters/UsageType' component={UsageType}/>
      <Route exact path='/masters/PipeSize' component={PipeSize}/>
      <Route exact path='/ShowPipeSize' component={ShowPipeSize}/>
      <Route exact path='/masters/CategoryType' component={CategoryType}/>
      </Switch>
  </main>
)


export default(
  <Main/>
);
