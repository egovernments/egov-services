import React, { Component } from 'react';
import { connect } from 'react-redux';

import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import RaisedButton from 'material-ui/RaisedButton';
import { RadioButton, RadioButtonGroup } from 'material-ui/RadioButton';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';

import { Collapse, Grid, Row, Col, Table, DropdownButton, Button, OverlayTrigger, Popover, Glyphicon } from 'react-bootstrap';

import { Card, CardHeader, CardText } from 'material-ui/Card';

import _ from 'lodash';

import { translate } from '../../../common/common';
import Api from '../../../../api/api';
import UiButton from '../../../framework/components/UiButton';
import { fileUpload } from '../../../framework/utility/utility';

import { parseFinancialYearResponse } from '../apis/apis';

class kpivalues extends Component {
  constructor(props) {
    super(props);

    this.state = {
      showResult: false,
      data: [],
      header: [],
      KPIs: [],
      Department: [],
      FinantialYear: [],
      collapse: [],

      selectedDeptId: '',
      selectedFinYear: '',
      selectedKpiCode: '',

      viewFirst: true,
      viewSecond: false,
      viewThird: false,
      viewFour: false,
      next: true,
      prev: false,
      KPIResult: [],
      documents: [],

      uploadPane: [],
      anchorEl: [],
      response: [],
      filelist: [],
      allowedMax: 1,
      search: false,
      currentFileList: [],
      showAlert: false,
      headerList: [
        { 4: 'April' },
        { 5: 'May' },
        { 6: 'June' },
        { 7: 'July' },
        { 8: 'August' },
        { 9: 'September' },
        { 10: 'October' },
        { 11: 'November' },
        { 12: 'December' },
        { 1: 'January' },
        { 2: 'February' },
        { 3: 'March' },
      ],
      errorInput: 'No record found',
    };

    //this.handleChange = this.handleChange.bind(this);
    //this.handleSubmit = this.handleSubmit.bind(this);
    //this.searchKPIValues = this.searchKPIValues.bind(this);
    //this.clearSearch = this.clearSearch.bind(this);
    //this.nextSection = this.nextSection.bind(this);
    this.header = this.header.bind(this);

    this.handleSubmit = this.handleSubmit.bind(this);
    this.handleFile = this.handleFile.bind(this);

    this.prepareBodyobject = this.prepareBodyobject.bind(this);

    this.renderFileObject = this.renderFileObject.bind(this);
    this.handleChange = this.handleChange.bind(this);
    this.nextSection = this.nextSection.bind(this);
    this.prevSection = this.prevSection.bind(this);
    this.clearSearch = this.clearSearch.bind(this);
    this.uploadFile = this.uploadFile.bind(this);
  }

  componentDidMount() {
    var url = 'egov-mdms-service/v1/_get?moduleName=common-masters&masterName=Department';
    var query = [];
    let self = this;
    Api.commonApiPost(url, query, {}, false, false).then(
      function(res) {
        if (res.MdmsRes) {
          //res.MdmsRes.`common-masters
          self.setState({ Department: res.MdmsRes['common-masters'].Department });
          //self.state.Department = res.MdmsRes['common-masters'].Department;
        }
      },
      function(err) {}
    );

    url = 'egov-mdms-service/v1/_get?masterName=financialYears&moduleName=egf-master';
    query = [];
    Api.commonApiPost(url, query, {}, false, false).then(
      function(res) {
        if (res) {
          let fnYear = parseFinancialYearResponse(res);
          console.log(fnYear);
          //res.MdmsRes.`common-masters
          self.setState({ FinantialYear: fnYear });
          //self.state.Department = res.MdmsRes['common-masters'].Department;
        }
      },
      function(err) {}
    );
  }

  nextSection() {
    this.setState({ prev: true });
    if (this.state.viewFirst) {
      this.setState({ viewFirst: false, viewSecond: true });
    }
    if (this.state.viewSecond) {
      this.setState({ viewSecond: false, viewThird: true, next: false });
    }
  }
  prevSection() {
    this.setState({ next: true });
    if (this.state.viewThird) {
      this.setState({ viewThird: false, viewSecond: true });
    }
    if (this.state.viewSecond) {
      this.setState({ viewSecond: false, viewFirst: true, prev: false });
    }
  }

  handleSearch(event) {
    let args = [];
    if (this.state.selectedDeptId) {
      args.push('departmentId=' + this.state.selectedDeptId);
    }
    if (this.state.selectedFinYear) {
      args.push('finYear=' + this.state.selectedFinYear);
      args['finYear'] = this.state.selectedFinYear;
    }
    if (this.state.selectedKpiCode) {
      args.push('kpiCodes=' + this.state.selectedKpiCode);
    }

    let self = this;

    /*var res = JSON.parse('{"responseInfo":{"apiId":"org.egov.pt","ver":"1.0","ts":1512034880099,"resMsgId":"uief87324","msgId":"654654","status":"200"},"kpiValues":[{"tenantId":"default","kpi":{"departmentId":2,"department":null,"id":"2","name":"MYKPITWO 2","code":"MKT","remoteSystemId":null,"periodicity":"MONTHLY","targetType":"OBJECTIVE","instructions":"SECOND INS Updated","financialYear":"2017-18","documentsReq":null,"auditDetails":{"createdBy":73,"lastModifiedBy":73,"createdTime":1511867483098,"lastModifiedTime":1511867571861},"kpiTarget":{"id":"10","kpiCode":"MKT","targetValue":"3","targetDescription":"In Progress","tenantId":null,"createdBy":null,"lastModifiedBy":null,"createdDate":null,"lastModifiedDate":null}},"kpiValue":{"id":"66","kpi":null,"kpiCode":"MKT","tenantId":"default","valueList":[{"id":null,"valueid":"66","period":"4","value":"1"},{"id":null,"valueid":"66","period":"5","value":"3"},{"id":null,"valueid":"66","period":"6","value":""},{"id":null,"valueid":"66","period":"7","value":"3"},{"id":null,"valueid":"66","period":"8","value":""},{"id":null,"valueid":"66","period":"9","value":""},{"id":null,"valueid":"66","period":"10","value":""},{"id":null,"valueid":"66","period":"11","value":""},{"id":null,"valueid":"66","period":"12","value":""},{"id":null,"valueid":"66","period":"1","value":""},{"id":null,"valueid":"66","period":"2","value":""},{"id":null,"valueid":"66","period":"3","value":"2"}],"documents":null,"auditDetails":{"createdBy":73,"lastModifiedBy":0,"createdTime":1511867905957,"lastModifiedTime":0}}},{"tenantId":"default","kpi":{"departmentId":2,"department":null,"id":"1","name":"MYKPIONE 1","code":"MKO","remoteSystemId":null,"periodicity":"MONTHLY","targetType":"VALUE","instructions":"ONE Ins Updated","financialYear":"2017-18","documentsReq":null,"auditDetails":{"createdBy":73,"lastModifiedBy":73,"createdTime":1511867483098,"lastModifiedTime":1512030906958},"kpiTarget":{"id":"9","kpiCode":"MKO","targetValue":"1000","targetDescription":"1000","tenantId":null,"createdBy":null,"lastModifiedBy":null,"createdDate":null,"lastModifiedDate":null}},"kpiValue":{"id":"57","kpi":null,"kpiCode":"MKO","tenantId":"default","valueList":[{"id":null,"valueid":"57","period":"4","value":"311"},{"id":null,"valueid":"57","period":"5","value":"200"},{"id":null,"valueid":"57","period":"6","value":"100"},{"id":null,"valueid":"57","period":"7","value":"120"},{"id":null,"valueid":"57","period":"8","value":"130"},{"id":null,"valueid":"57","period":"9","value":"140"},{"id":null,"valueid":"57","period":"10","value":"110"},{"id":null,"valueid":"57","period":"11","value":"120"},{"id":null,"valueid":"57","period":"12","value":"105"},{"id":null,"valueid":"57","period":"1","value":""},{"id":null,"valueid":"57","period":"2","value":""},{"id":null,"valueid":"57","period":"3","value":"10"}],"documents":null,"auditDetails":{"createdBy":73,"lastModifiedBy":0,"createdTime":1511867905957,"lastModifiedTime":0}}},{"tenantId":"default","kpi":{"departmentId":2,"department":null,"id":"3","name":"MYKPITHREE 3","code":"MKTH","remoteSystemId":null,"periodicity":"MONTHLY","targetType":"TEXT","instructions":"THIRD INS Updated","financialYear":"2017-18","documentsReq":null,"auditDetails":{"createdBy":73,"lastModifiedBy":73,"createdTime":1511867483098,"lastModifiedTime":1511867571861},"kpiTarget":{"id":"11","kpiCode":"MKTH","targetValue":"CONFIRMED","targetDescription":"CONFIRMED","tenantId":null,"createdBy":null,"lastModifiedBy":null,"createdDate":null,"lastModifiedDate":null}},"kpiValue":{"id":"75","kpi":null,"kpiCode":"MKTH","tenantId":"default","valueList":[{"id":null,"valueid":"75","period":"4","value":"100"},{"id":null,"valueid":"75","period":"5","value":"200"},{"id":null,"valueid":"75","period":"6","value":""},{"id":null,"valueid":"75","period":"7","value":""},{"id":null,"valueid":"75","period":"8","value":""},{"id":null,"valueid":"75","period":"9","value":""},{"id":null,"valueid":"75","period":"10","value":""},{"id":null,"valueid":"75","period":"11","value":""},{"id":null,"valueid":"75","period":"12","value":""},{"id":null,"valueid":"75","period":"1","value":""},{"id":null,"valueid":"75","period":"2","value":""},{"id":null,"valueid":"75","period":"3","value":"CONFIRMED"}],"documents":null,"auditDetails":{"createdBy":73,"lastModifiedBy":0,"createdTime":1511867905957,"lastModifiedTime":0}}}]}');
   var response = res.kpiValues;

   let header = self.header();
   //var row   = response;//self.prepareBodyobject(response);

   self.setState({data: response,header:header,showResult: true,KPIResult:response});*/

    //let url = 'http://localhost:3000/perfmanagement/v1/kpivalue/_search?departmentId=2&finYear=2017-18&kpiCodes=PFP&tenantId=default&pageSize=200';
    var url = 'perfmanagement/v1/kpivalue/_search?' + args.join('&');
    this.props.setLoadingStatus('loading');
    Api.commonApiPost(url, {}, {}, false, true).then(
      function(res) {
        self.props.setLoadingStatus('hide');
        var response = res.kpiValues;

        let header = self.header();
        //var row   = self.prepareBodyobject(response);

        let showResult = false;
        if (res.kpiValues.length) {
          showResult = true;
        }

        /*response.map(item => {
      item.kpiValue.valueList.map(periodItem =>{
        if(periodItem.documents) {
            periodItem.documents.map(files => {
              console.log(files);
              self.getFileDetails(files['fileStoreId'],self);
            }); 
          }
          
        });      
      });*/

        self.setState({ data: res.kpiValues, header: header, showResult: showResult, KPIResult: response });
      },
      function(err) {
        self.props.setLoadingStatus('hide');
      }
    );
  }

  header() {
    let header = [
      { 4: 'April' },
      { 5: 'May' },
      { 6: 'June' },
      { 7: 'July' },
      { 8: 'August' },
      { 9: 'September' },
      { 10: 'October' },
      { 11: 'November' },
      { 12: 'December' },
      { 1: 'January' },
      { 2: 'February' },
      { 3: 'March' },
    ];

    return header.map((headerItem, k) => {
      let index = Object.keys(headerItem);
      var className = '';
      // first section
      var className = this.panelVisiblity(k);
      return (
        <th className={className}>
          <div suppressContentEditableWarning="true">{headerItem[index[0]]}</div>
        </th>
      );
    });
  }

  panelVisiblity(k) {
    var className = '';
    if (k >= 0 && k <= 3 && !this.state.viewFirst) {
      className = 'hidden';
    }
    if (k >= 4 && k <= 7 && !this.state.viewSecond) {
      className = 'hidden';
    }
    if (k >= 8 && k <= 11 && !this.state.viewThird) {
      className = 'hidden';
    }
    return className;
  }

  renderFileObject = (item, i) => {
    let self = this;

    if (self.props.readonly) {
      return (
        <a
          href={
            window.location.origin +
            '/filestore/v1/files/id?tenantId=' +
            localStorage.tenantId +
            '&fileStoreId=' +
            self.props.getVal(item.period + '[' + i + '].fileStoreId')
          }
          target="_blank"
        >
          {'attachment ' + i /*translate("wc.craete.file.Download")*/}
        </a>
      );
    } else {
      return (
        <div>
          {this.state.documents[item.valueid] &&
            this.state.documents[item.valueid][item.period] && (
              <span>
                <label>{this.state.documents[item.valueid][item.period].name}</label>&nbsp;&nbsp;
                <UiButton
                  item={{ label: 'Upload', uiType: 'button', primary: true }}
                  ui="google"
                  handler={e => this.uploadFile(item.valueid, item.period)}
                />&nbsp;&nbsp;
                <br />
              </span>
            )}

          <label class="btn btn-primary" for={'file_' + item.valueid + item.period} style={{ color: 'orange' }}>
            <input
              id={'file_' + item.valueid + item.period}
              type="file"
              accept=".xls,.xlsx,.txt,.json,.doc,.docx"
              style={{ display: 'none' }}
              onChange={e => this.handleFile(e, item.valueid, item.period)}
            />
            <span className="glyphicon glyphicon-upload" aria-hidden="true" />&nbsp;
            <strong>{item.documents && item.documents.length ? 'Upload more' : 'Upload'}</strong>
          </label>
        </div>
      );
    }
  };

  prepareUploadPanel(itemValue) {
    return (
      <div
        style={{
          ...this.props.style,
          position: 'absolute',
          boxShadow: '0 5px 10px rgba(0, 0, 0, 0.2)',
          border: '1px solid #CCC',
          marginLeft: 78,
          marginTop: 5,
          padding: 10,
          backgroundColor: 'white',
          width: 350,
          zIndex: 1,
        }}
      >
        <ul>
          {itemValue.documents &&
            itemValue.documents.map((fileStoreId, index) => {
              return (
                <li>
                  {this.state.filelist[fileStoreId['fileStoreId']] && (
                    <a
                      href={
                        window.location.origin +
                        '/filestore/v1/files/id?tenantId=' +
                        localStorage.tenantId +
                        '&fileStoreId=' +
                        fileStoreId['fileStoreId']
                      }
                      target="_blank"
                    >
                      {this.state.filelist[fileStoreId['fileStoreId']] /*translate("wc.craete.file.Download")*/}
                    </a>
                  )}
                  {!this.state.filelist[fileStoreId['fileStoreId']] &&
                    this.state.currentFileList[itemValue.valueid] &&
                    this.state.currentFileList[itemValue.valueid][itemValue.period] && (
                      <span>
                        <a
                          href={
                            window.location.origin +
                            '/filestore/v1/files/id?tenantId=' +
                            localStorage.tenantId +
                            '&fileStoreId=' +
                            fileStoreId['fileStoreId']
                          }
                          target="_blank"
                        >
                          {
                            this.state.currentFileList[itemValue.valueid][itemValue.period][
                              fileStoreId['fileStoreId']
                            ] /*translate("wc.craete.file.Download")*/
                          }
                        </a>
                        <br />
                        <div
                          className={`${this.state.showingAlert ? '' : 'hidden'}`}
                          style={{ backgroundColor: '#4CAF50', color: 'white', opacity: 2, width: 248, marginLeft: 24 }}
                        >
                          {'Please Submit KPI Values to save.'}
                        </div>
                      </span>
                    )}
                </li>
              );
            })}
        </ul>
        <br />
        {this.renderFileObject(itemValue)}
      </div>
    );
  }

  setFileName(item, self, e) {
    if (item.documents) {
      let filelistClone = self.state.filelist.slice();
      console.log(item.documents);
      item.documents.map(filedetails => {
        console.log('file api call');
        let result = new Promise(function(resolve, reject) {
          let { setLoadingStatus } = self.props;
          let url = '/filestore/v1/files/id?tenantId=' + localStorage.getItem('tenantId') + '&fileStoreId=' + filedetails.fileStoreId;

          var oReq = new XMLHttpRequest();
          oReq.open('GET', url, true);
          oReq.responseType = 'arraybuffer';
          oReq.onload = function(oEvent) {
            var blob = new Blob([oReq.response], { type: oReq.getResponseHeader('content-type') });
            var url = URL.createObjectURL(blob);

            let disposition = oReq.getResponseHeader('content-disposition');
            let filename = '';
            if (disposition && disposition.indexOf('attachment') !== -1) {
              var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
              var matches = filenameRegex.exec(disposition);
              if (matches != null && matches[1]) {
                filename = matches[1].replace(/['"]/g, '');

                resolve(filename);

                //console.log(filelistClone);
                //console.log(self.state.filelist);
              }
            }
          };
          oReq.send();
        });

        result.then(filename => {
          filelistClone[filedetails.fileStoreId] = filename;
          console.log(filelistClone);
          self.setState({ filelist: filelistClone });
        });

        //console.log(filestoreId);
      });
    }
  }

  prepareKPIdesc(kpi) {
    return (
      <Popover id="popover-positioned-bottom">
        <span>{kpi.instructions}</span>
      </Popover>
    );
  }

  prepareBodyobject(response) {
    response.map(item => {
      let result = [];
      item.kpiValue.valueList.map(periodItem => {
        this.state.headerList.map((headerItem, index) => {
          let key = Object.keys(headerItem);
          if (periodItem.period == key[0]) {
            result[index] = periodItem;
          }
        });
      });
      item.kpiValue.valueList = result;
    });

    return response.map(item => {
      //this.state.uploadPane[item.kpiValue.id] = [];
      return (
        <tr>
          <td>
            <h6>
              <strong>{item.kpi.name}</strong>
            </h6>
            <label>{translate('perfManagement.create.KPIs.groups.type')}</label>:&nbsp;
            <span>{item.kpi.targetType}</span>
            <br />
            <label>{translate('perfManagement.create.KPIs.groups.kpiTarget')}</label>:&nbsp;
            <span>{item.kpi.kpiTargets[0].targetDescription}</span>
            <br />
            <label>{translate('perfManagement.view.KPIs.groups.viewkpiTargetFinancialYear')}</label>:&nbsp;
            <span>{item.kpi.kpiTargets[0].finYear}</span>
            <br />
            <OverlayTrigger trigger="click" overlay={this.prepareKPIdesc(item.kpi)} placement="right" rootClose>
              <span style={{ color: 'orange' }}>
                <span className="glyphicon glyphicon-info-sign" aria-hidden="true" />&nbsp;
                <u>{translate('perfManagement.create.KPIs.groups.kpiInfo')}</u>
              </span>
            </OverlayTrigger>
          </td>
          <td />
          {item.kpiValue.valueList.map((itemValue, k) => {
            var className = this.panelVisiblity(k);

            return (
              <td className={className}>
                {item.kpi.targetType == 'TEXT' && (
                  <TextField
                    floatingLabelStyle={{ color: '#696969', fontSize: '20px', 'white-space': 'nowrap' }}
                    inputStyle={{ color: '#5F5C57', textAlign: 'left' }}
                    style={{ display: 'inline-block', width: 153 }}
                    errorStyle={{ float: 'left' }}
                    value={itemValue.value}
                    onChange={e => {
                      this.handleChange(itemValue.valueid, itemValue.period, e, 'TXT');
                    }}
                  />
                )}

                {item.kpi.targetType == 'VALUE' && (
                  <TextField
                    floatingLabelStyle={{ color: '#696969', fontSize: '20px', 'white-space': 'nowrap' }}
                    inputStyle={{ color: '#5F5C57', textAlign: 'left' }}
                    style={{ display: 'inline-block', width: 153 }}
                    errorStyle={{ float: 'left' }}
                    value={itemValue.value}
                    onChange={e => {
                      this.handleChange(itemValue.valueid, itemValue.period, e, 'VAL');
                    }}
                  />
                )}

                {item.kpi.targetType == 'OBJECTIVE' && (
                  <SelectField
                    value={itemValue.value}
                    dropDownMenuProps={{ animated: false, targetOrigin: { horizontal: 'left', vertical: 'bottom' } }}
                    style={{ display: 'inline-block', width: 153 }}
                    errorStyle={{ float: 'left' }}
                    fullWidth={true}
                    hintText="Please Select"
                    onChange={e => {
                      this.handleChange(itemValue.valueid, itemValue.period, e, 'DD');
                    }}
                  >
                    <MenuItem value="1" primaryText="YES" />
                    <MenuItem value="2" primaryText="NO" />
                    <MenuItem value="3" primaryText="WIP" />
                  </SelectField>
                )}

                <div>
                  <OverlayTrigger trigger="click" overlay={this.prepareUploadPanel(itemValue)} placement="bottom" rootClose>
                    <span style={{ color: 'orange' }}>
                      <span className="glyphicon glyphicon-upload" aria-hidden="true" /> &nbsp;&nbsp;
                      <span id={itemValue.valueid + '' + itemValue.period} onClick={e => this.setFileName(itemValue, this, e)}>
                        <strong>{translate('perfManagement.create.KPIs.groups.Uploads')}</strong>
                      </span>
                    </span>
                  </OverlayTrigger>
                  <br />
                </div>
              </td>
            );
          })}
          <td />
        </tr>
      );
    });
  }

  handleChange(kpiValueID, period, event, type) {
    let KPIsClone = this.state.KPIResult.slice();

    /*if (KPIsClone[kpiId]) {
        KPIsClone[kpiId][kpiValueId] = event.target.value;
      }
      else
      {
        KPIsClone[kpiId] = [];
        KPIsClone[kpiId][kpiValueId] = event.target.value;
      }
      console.log(this.state.KPIResult);
      console.log(this.state.KPIResult);*/

    if (type == 'DD') {
      if (event.target.innerHTML == 'NO') {
        event.target.value = '2';
      }
      if (event.target.innerHTML == 'YES') {
        event.target.value = '1';
      }
      if (event.target.innerHTML == 'WIP') {
        event.target.value = '3';
      }
    }
    var pattern = /^[0-9]*$/;
    if (type == 'VAL' && !pattern.test(event.target.value)) {
      return false;
    }

    for (var i = KPIsClone.length - 1; i >= 0; i--) {
      KPIsClone[i]['kpiValue']['valueList'].map(p => {
        if (p['valueid'] == kpiValueID && p['period'] == period) {
          p['value'] = event.target.value;
        }
      });
    }

    this.setState({ KPIResult: KPIsClone });

    //console.log(this.state.KPIs);
  }

  uploadFile(valueid, period, e) {
    let { actionName, moduleName } = this.props;
    let fileList = this.state.documents;
    let self = this;

    console.log(fileList, 'file list');
    console.log(valueid, period, 'field values :');

    console.log(fileList[valueid][period]);

    fileUpload(fileList[valueid][period], moduleName, function(err, res) {
      if (err) {
        self.props.setLoadingStatus('hide');
        self.props.toggleSnackbarAndSetText(true, err, false, true);
      } else {
        let resultClone = self.state.KPIResult.slice();
        resultClone.map(kpi => {
          kpi.kpiValue.valueList.map(kpiValue => {
            if (kpiValue.valueid == valueid && kpiValue.period == period) {
              if (!kpiValue.documents) {
                kpiValue.documents = [];
              }
              kpiValue.documents.push({ fileStoreId: res.files[0].fileStoreId });

              self.getFileDetails(res.files[0].fileStoreId, valueid, period, self);
            }
          });
        });

        //this.state.documents[valueid][period]

        let uploadedFiles = self.state.currentFileList.slice();

        //uploadedFiles[valueid] = undefined;

        self.setState({ documents: uploadedFiles });

        self.setState({ KPIResult: resultClone });
      }
    });
  }

  getFileDetails(filestoreID, valueid, period, self) {
    // let {setLoadingStatus} = self.props;
    let url = '/filestore/v1/files/id?tenantId=' + localStorage.getItem('tenantId') + '&fileStoreId=' + filestoreID;

    let filelistClone = self.state.currentFileList.slice();
    console.log(filelistClone);
    console.log(valueid, period);

    var oReq = new XMLHttpRequest();
    oReq.open('GET', url, true);
    oReq.responseType = 'arraybuffer';
    oReq.onload = function(oEvent) {
      var blob = new Blob([oReq.response], { type: oReq.getResponseHeader('content-type') });
      var url = URL.createObjectURL(blob);

      let disposition = oReq.getResponseHeader('content-disposition');
      let filename = '';
      if (disposition && disposition.indexOf('attachment') !== -1) {
        var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
        var matches = filenameRegex.exec(disposition);
        if (matches != null && matches[1]) {
          filename = matches[1].replace(/['"]/g, '');
          //filelistClone[filestoreID] = filename;

          if (filelistClone[valueid]) {
            if (filelistClone[valueid][period]) {
              filelistClone[valueid][period][filestoreID] = filename;
            } else {
              filelistClone[valueid][period] = [];
              filelistClone[valueid][period][filestoreID] = filename;
            }
          } else {
            filelistClone[valueid] = [];
            filelistClone[valueid][period] = [];
            filelistClone[valueid][period][filestoreID] = filename;
          }
          self.setState({ currentFileList: filelistClone });

          self.setState({
            showingAlert: true,
          });
          setTimeout(() => {
            self.setState({
              showingAlert: false,
            });
          }, 2000);
          //console.log(filelistClone);
          console.log(self.state.currentFileList);
        }
      }
    };
    oReq.send();
  }

  handleFile(event, valueid, period) {
    let files = this.state.documents.slice();

    if (!files[valueid]) {
      files[valueid] = [];
    }

    files[valueid][period] = event.target.files[0];
    console.log(files);
    this.setState({ documents: files });
  }

  handleSubmit(event) {
    let { actionName, moduleName } = this.props;
    let fileList = this.state.documents;
    let counter = Object.keys(fileList).length;
    let url = 'perfmanagement/v1/kpivalue/_create';
    let fileStorId = [];
    let self = this;
    console.log(fileList, 'fileslist');
    console.log(fileList.length, 'counter');
    let breakOut = 0;
    //this.props.setLoadingStatus('loading');

    //self.props.setLoadingStatus('hide');
    let body = { kpiValues: this.state.KPIResult };
    self.makeAjaxCall(body, url);

    /* if (counter) {

         for(let key in fileList) {
        let kpiFiles = fileList[key];

       var previous = '';

        for(let index in kpiFiles) {
            let reference = '';
           fileUpload(kpiFiles[index], moduleName, function(err, res) {
            if(breakOut == 1) return;
            if(err) {
              breakOut = 1;
              self.props.setLoadingStatus('hide');
              self.props.toggleSnackbarAndSetText(true, err, false, true);
            } else {


              let resultClone = self.state.KPIResult.slice();
              resultClone.map(kpi => {
                kpi.kpiValue.valueList.map(kpiValue => {

                  if (kpiValue.valueid == key && kpiValue.period == index) {
                    kpiValue.documents.push({"fileStoreId":res.files[0].fileStoreId})
                    console.log(res.files[0].fileStoreId,'file store id'); 
                    console.log(kpiValue.period,'result clone'); 
                  }

                });
              });
                           

              self.setState({KPIResult:resultClone});
              

              if(counter == 0 && breakOut == 0)
                {
                  console.log(self.state.KPIResult,'final result');
                 self.props.setLoadingStatus('hide');
                  let body = {'kpiValues' : self.state.KPIResult};
                  self.makeAjaxCall(body,url);
                }
              //
              //console.log(res.files[0].fileStoreId);
              //_.set(formData, key, res.files[0].fileStoreId);
              
                
                //self.makeAjaxCall(formData, _url);
            }
        });
          
        }
        counter--;

       }
     }
       else
       {
          self.props.setLoadingStatus('hide');
          let body = {'kpiValues' : this.state.KPIResult};
          self.makeAjaxCall(body,url);
       }*/
  }

  makeAjaxCall = (data, url) => {
    let self = this;

    let query = [];

    Api.commonApiPost(url, query, data, false, true).then(
      function(res) {
        self.props.setLoadingStatus('hide');
        if (res) {
          self.props.toggleSnackbarAndSetText(true, translate('perfManagement.update.KPIs.groups.updatekpivalue'), true);
        }
      },
      function(err) {
        self.props.setLoadingStatus('hide');
        self.props.toggleSnackbarAndSetText(true, err.message);
      }
    );
  };

  clearSearch() {
    this.setState({ selectedDeptId: '', selectedFinYear: '', selectedKpiCode: '', showResult: false, search: false });
  }

  searchKPIValues(event, key, value, type) {
    switch (type) {
      case 'DEPT':
        this.setState({ selectedDeptId: value, search: true });
        break;
      case 'FINYEAR':
        this.setState({ selectedFinYear: event.target.innerHTML });
        break;
      case 'KPI':
        this.setState({ selectedKpiCode: value });
        break;
      default:
    }

    let args = [];
    if (type == 'DEPT') {
      args.push('departmentId=' + value);
    } else if (this.state.selectedDeptId) {
      args.push('departmentId=' + this.state.selectedDeptId);
    }
    if (type == 'FINYEAR') {
      args.push('finYear=' + event.target.innerHTML);
    } else if (this.state.selectedFinYear) {
      args.push('finYear=' + this.state.selectedFinYear);
      //args['finYear'] = this.state.selectedFinYear;
    }

    let self = this;
    let url = 'perfmanagement/v1/kpimaster/_search?' + args.join('&');

    Api.commonApiPost(url, {}, {}, false, true).then(
      function(res) {
        //console.log(res);
        self.setState({ KPIs: res.KPIs });
      },
      function(err) {}
    );
  }

  render() {
    let { mockData, moduleName, actionName, formData, fieldErrors, isFormValid } = this.props;
    let { create, handleChange, getVal, addNewCard, removeCard, autoComHandler, initiateWF } = this;

    let tableStyle = {
      align: 'center',
    };

    let body = this.prepareBodyobject(this.state.data);
    let header = this.header();

    //console.log(list);
    return (
      <div className="SearchResult">
        <Card className="uiCard">
          <CardHeader
            style={{ paddingTop: 4, paddingBottom: 0 }}
            title={<div style={{ color: '#354f57', fontSize: 18, margin: '8px 0' }}>{translate('perfManagement.search.KPIs.groups.kpiSearch')}</div>}
            actAsExpander={true}
          />

          <CardText>
            <Row className="show-grid">
              <Col xs={4} md={4}>
                <SelectField
                  value={this.state.selectedDeptId}
                  className="custom-form-control-for-select"
                  floatingLabelStyle={{ color: '#696969', fontSize: '20px', 'white-space': 'nowrap' }}
                  floatingLabelFixed={true}
                  dropDownMenuProps={{ animated: false, targetOrigin: { horizontal: 'left', vertical: 'bottom' } }}
                  style={{ display: 'inline-block' }}
                  errorStyle={{ float: 'left' }}
                  fullWidth={true}
                  hintText="Please Select"
                  labelStyle={{ color: '#5F5C57' }}
                  floatingLabelText={
                    <span>
                      Department{' '}
                      <span style={{ color: '#FF0000' }}>
                        <i>*</i>
                      </span>
                    </span>
                  }
                  onChange={(event, key, value) => this.searchKPIValues(event, key, value, 'DEPT')}
                >
                  {this.state.Department &&
                    this.state.Department.map((dd, index) => <MenuItem value={dd.id && dd.id.toString()} key={index} primaryText={dd.name} />)}
                </SelectField>
              </Col>

              <Col xs={4} md={4}>
                <SelectField
                  value={this.state.selectedFinYear}
                  className="custom-form-control-for-select"
                  floatingLabelStyle={{ color: '#696969', fontSize: '20px', 'white-space': 'nowrap' }}
                  floatingLabelFixed={true}
                  dropDownMenuProps={{ animated: false, targetOrigin: { horizontal: 'left', vertical: 'bottom' } }}
                  style={{ display: 'inline-block' }}
                  errorStyle={{ float: 'left' }}
                  fullWidth={true}
                  hintText="Please Select"
                  labelStyle={{ color: '#5F5C57' }}
                  floatingLabelText={<span>Financial Year</span>}
                  onChange={(event, key, value) => this.searchKPIValues(event, key, value, 'FINYEAR')}
                >
                  {this.state.FinantialYear &&
                    this.state.FinantialYear.map((dd, index) => (
                      <MenuItem value={dd.finYearRange && dd.finYearRange.toString()} key={index} primaryText={dd.finYearRange} />
                    ))}
                </SelectField>
              </Col>

              <Col xs={4} md={4}>
                <SelectField
                  value={this.state.selectedKpiCode}
                  className="custom-form-control-for-select"
                  floatingLabelStyle={{ color: '#696969', fontSize: '20px', 'white-space': 'nowrap' }}
                  floatingLabelFixed={true}
                  dropDownMenuProps={{ animated: false, targetOrigin: { horizontal: 'left', vertical: 'bottom' } }}
                  style={{ display: 'inline-block' }}
                  errorStyle={{ float: 'left' }}
                  fullWidth={true}
                  hintText="Please Select"
                  labelStyle={{ color: '#5F5C57' }}
                  floatingLabelText={<span>KPI Code</span>}
                  onChange={(event, key, value) => this.searchKPIValues(event, key, value, 'KPI')}
                >
                  {this.state.KPIs &&
                    this.state.KPIs.map((dd, index) => <MenuItem value={dd.code && dd.code.toString()} key={index} primaryText={dd.code} />)}
                </SelectField>
              </Col>
            </Row>
          </CardText>
        </Card>

        <Row>
          <Col xs={6} md={6}>
            <h3 style={{ paddingLeft: 15, marginBottom: '0' }}>
              {!_.isEmpty(mockData) &&
              moduleName &&
              actionName &&
              mockData[`${moduleName}.${actionName}`] &&
              mockData[`${moduleName}.${actionName}`].title
                ? translate(mockData[`${moduleName}.${actionName}`].title)
                : ''}
            </h3>
          </Col>
          <Col xs={6} md={6}>
            <div style={{ textAlign: 'right', color: '#FF0000', marginTop: '15px', marginRight: '15px', paddingTop: '8px' }}>
              <i>( * ) {translate('framework.required.note')}</i>
            </div>
          </Col>
        </Row>

        <Row className="show-grid">
          <Col xs={6} xsOffset={5}>
            <UiButton
              item={{ label: 'Search', uiType: 'button', primary: true, isDisabled: !this.state.search }}
              ui="google"
              handler={e => this.handleSearch(e)}
            />&nbsp;&nbsp;
            <RaisedButton
              icon={
                <i style={{ color: 'black' }} className="material-icons">
                  backspace
                </i>
              }
              label="Reset"
              primary={false}
              onClick={this.clearSearch}
            />
            &nbsp;&nbsp;
          </Col>
        </Row>

        {this.state.showResult && (
          <Card className="uiCard">
            <CardHeader titleStyle={{ marginLeft: 638 }} title={<strong> {translate('ui.table.title')} </strong>} />

            <CardText>
              <div className="cntdatatable">
                <div className="cntdatatable1">
                  <Table
                    id="searchTable"
                    className="table table-striped table-bordered table dataTable no-footer"
                    cellspacing="0"
                    width="100%"
                    style={tableStyle}
                    responsive
                  >
                    <thead>
                      <th>KPI</th>

                      <th>
                        {this.state.prev && (
                          <span
                            className="glyphicon glyphicon-menu-left"
                            aria-hidden="true"
                            style={{ cursor: 'pointer' }}
                            title="Prev"
                            onClick={e => this.prevSection(e)}
                          />
                        )}
                      </th>
                      {header}
                      <th>
                        {this.state.next && (
                          <span
                            className="glyphicon glyphicon-menu-right"
                            aria-hidden="true"
                            style={{ cursor: 'pointer' }}
                            title="Next"
                            onClick={e => this.nextSection(e)}
                          />
                        )}
                      </th>
                    </thead>
                    <tbody>{body}</tbody>
                  </Table>
                </div>
              </div>

              <br />
              <Row>
                <Col xs={2} xsOffset={10}>
                  <UiButton item={{ label: 'Save', uiType: 'button', primary: true }} ui="google" handler={e => this.handleSubmit(e)} />&nbsp;&nbsp;
                </Col>
              </Row>
            </CardText>
          </Card>
        )}
      </div>
    );
  }
}

const mapStateToProps = state => ({
  metaData: state.framework.metaData,
  mockData: state.framework.mockData,
  moduleName: state.framework.moduleName,
  actionName: state.framework.actionName,
  formData: state.frameworkForm.form,
  fieldErrors: state.frameworkForm.fieldErrors,
  isFormValid: state.frameworkForm.isFormValid,
  requiredFields: state.frameworkForm.requiredFields,
  dropDownData: state.framework.dropDownData,
});

const mapDispatchToProps = dispatch => ({
  initForm: requiredFields => {
    dispatch({
      type: 'SET_REQUIRED_FIELDS',
      requiredFields,
    });
  },
  setMetaData: metaData => {
    dispatch({ type: 'SET_META_DATA', metaData });
  },
  setMockData: mockData => {
    dispatch({ type: 'SET_MOCK_DATA', mockData });
  },
  setFormData: data => {
    dispatch({ type: 'SET_FORM_DATA', data });
  },
  setModuleName: moduleName => {
    dispatch({ type: 'SET_MODULE_NAME', moduleName });
  },
  setActionName: actionName => {
    dispatch({ type: 'SET_ACTION_NAME', actionName });
  },
  handleChange: (e, property, isRequired, pattern, requiredErrMsg, patternErrMsg) => {
    dispatch({ type: 'HANDLE_CHANGE_FRAMEWORK', property, value: e.target.value, isRequired, pattern, requiredErrMsg, patternErrMsg });
  },
  setLoadingStatus: loadingStatus => {
    dispatch({ type: 'SET_LOADING_STATUS', loadingStatus });
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({ type: 'TOGGLE_SNACKBAR_AND_SET_TEXT', snackbarState, toastMsg, isSuccess, isError });
  },
  setDropDownData: (fieldName, dropDownData) => {
    dispatch({ type: 'SET_DROPDWON_DATA', fieldName, dropDownData });
  },
  setRoute: route => dispatch({ type: 'SET_ROUTE', route }),
  delRequiredFields: requiredFields => {
    dispatch({ type: 'DEL_REQUIRED_FIELDS', requiredFields });
  },
  addRequiredFields: requiredFields => {
    dispatch({ type: 'ADD_REQUIRED_FIELDS', requiredFields });
  },
  removeFieldErrors: key => {
    dispatch({ type: 'REMOVE_FROM_FIELD_ERRORS', key });
  },
});

export default connect(mapStateToProps, mapDispatchToProps)(kpivalues);
