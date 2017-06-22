import React from 'react';
import {Switch,Route} from 'react-router-dom';
import UsageType from './components/contents/masters/UsageType';
import CategoryType from './components/contents/masters/CategoryType';
import WaterSourceType from './components/contents/masters/WaterSourceType';
import WaterSupplyType from './components/contents/masters/WaterSupplyType';
import PipeSize from './components/contents/masters/PipeSize';
import PropertyPipeSize from './components/contents/masters/PropertyPipeSize';
import PropertyCategory from './components/contents/masters/PropertyCategory';
import PropertyUsage from './components/contents/masters/PropertyUsage';
import DocumentTypeApplicationType from './components/contents/masters/DocumentTypeApplicationType';
import WaterTapConnection from './components/contents/WaterTapConnection';
import ShowUsageType from './components/contents/ShowUsageType';
import ShowDocumentTypeApplicationType from './components/contents/ShowDocumentTypeApplicationType';
import ShowCategoryType from './components/contents/ShowCategoryType';
import ShowWaterSourceType from './components/contents/ShowWaterSourceType';
import ShowWaterSupplyType from './components/contents/ShowWaterSupplyType';
import ShowPipeSize from './components/contents/ShowPipeSize';
import ShowDocumentName from './components/contents/ShowDocumentName';
import ShowDocumentType from './components/contents/ShowDocumentType';
import ShowPropertyPipeSize from './components/contents/ShowPropertyPipeSize';
import ShowPropertyCategory from './components/contents/ShowPropertyCategory';
import ShowPropertyUsage from './components/contents/ShowPropertyUsage';
import AcknowledgementSlip from './components/contents/AcknowledgementSlip';
import DocumentType from './components/contents/masters/DocumentType';
import Donation from './components/contents/masters/Donation';







const Main = () => (
  <main>
    <Switch>
    <Route exact path='/' component={WaterSupplyType}/>
    <Route exact path='/masters/DocumentTypeApplicationType' component={DocumentTypeApplicationType}/>
    <Route exact path='/masters/Donation' component={Donation}/>
    <Route exact path='/masters/PropertyPipeSize' component={PropertyPipeSize}/>
    <Route exact path='/masters/CategoryType' component={CategoryType}/>
    <Route exact path='/ShowCategoryType' component={ShowCategoryType}/>
    <Route exact path='/ShowDocumentTypeApplicationType' component={ShowDocumentTypeApplicationType}/>
    <Route exact path='/PipeSize' component={PipeSize}/>
    <Route exact path='/ShowPropertyUsage' component={ShowPropertyUsage}/>
    <Route exact path='/ShowPropertyCategory' component={ShowPropertyCategory}/>
    <Route exact path='/ShowPropertyPipeSize' component={ShowPropertyPipeSize}/>
    <Route exact path='/ShowDocumentName' component={ShowDocumentName}/>
    <Route exact path='/ShowDocumentType' component={ShowDocumentType}/>
    <Route exact path='/DocumentType' component={DocumentType}/>
    <Route exact path='/masters/PropertyCategory' component={PropertyCategory}/>
    <Route exact path='/masters/PropertyUsage' component={PropertyUsage}/>
    <Route exact path='/masters/PropertyCategory' component={PropertyCategory}/>
      <Route exact path='/AcknowledgementSlip' component={AcknowledgementSlip}/>
      <Route exact path='/ShowUsageType' component={ShowUsageType}/>
      <Route exact path='/WaterTapConnection' component={WaterTapConnection}/>
      <Route exact path='/masters/UsageType' component={UsageType}/>
      <Route exact path='/masters/PipeSize' component={PipeSize}/>
      <Route exact path='/ShowPipeSize' component={ShowPipeSize}/>
      <Route exact path='/masters/WaterSourceType' component={WaterSourceType}/>
      <Route exact path='/masters/WaterSupplyType' component={WaterSupplyType}/>
      <Route exact path='/ShowWaterSupplyType' component={ShowWaterSupplyType}/>
      <Route exact path='/ShowWaterSourceType' component={ShowWaterSourceType}/>
      <Route exact path='/masters/PropertyPipeSize' component={PropertyPipeSize}/>
      </Switch>
  </main>
)


export default(
  <Main/>
);
