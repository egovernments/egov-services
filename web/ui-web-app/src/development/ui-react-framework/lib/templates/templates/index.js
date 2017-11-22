import MyExampleTemplate from './MyExampleTemplate';
var assetImmovableReport;
var assetMovableReport;
var VakalatnamaTemplate;
var landRegisterReport;
if(process.env.NODE_ENV == "production") {
  assetImmovableReport = require('asset/templates/assetImmovableReport');
  assetMovableReport = require('asset/templates/assetMovableReport');
  landRegisterReport = require('asset/templates/landRegisterReport');
} else {
  assetImmovableReport = require('../../../../asset/lib/specifications/templates/assetImmovableReport');
  assetMovableReport = require('../../../../asset/lib/specifications/templates/assetMovableReport');
  landRegisterReport = require('../../../../asset/lib/specifications/templates/landRegisterReport');
}

export { MyExampleTemplate, landRegisterReport, assetImmovableReport, assetMovableReport};
