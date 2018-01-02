import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Row, Col } from 'react-bootstrap';
import Dialog from 'material-ui/Dialog';
import FlatButton from 'material-ui/FlatButton';
import RaisedButton from 'material-ui/RaisedButton';

import { fileUpload } from '../../../framework/utility/utility';

class KPIDocumentField extends Component {
  constructor(props) {
    super(props);

    this.state = {
      documents: [],
      open: false,
      cell: '',
      fileAttrList: [],
      currentFileName: [],
      currentFileStoreID: [],
    };
  }

  docUpload = () => {
    let { actionName, moduleName } = this.props;
    this.state.cell;
    console.log(this.state.cell);
    console.log(this.state.documents[this.state.cell.valueid]);
    this.state.documents[this.state.cell.valueid][this.state.cell.period].map((fileInfo, id) => {
      let valueid = this.state.cell.valueid;
      let period = this.state.cell.period;
      let code = fileInfo.code;

      let result = new Promise(function(resolve, reject) {
        fileUpload(fileInfo.file, moduleName, function(err, res) {
          if (err) {
            console.log('unable to upload');
          } else {
            resolve({ fileStoreId: res.files[0].fileStoreId, name: fileInfo.name, valueid: valueid, period: period, docCode: code });
          }
        });
      });

      result.then(file => {
        this.props.setUploadedFiles(file);
      });
    });

    this.props.switchDialog(false);
  };

  handleClose = () => {
    this.props.switchDialog(false);
  };

  handleFile(event, valueid, period, doc, index, docList, docCode) {
    let files = this.state.documents.slice();
    if (!files[valueid]) {
      files[valueid] = [];
    }
    if (!files[valueid][period]) {
      files[valueid][period] = [];
    }

    files[valueid][period][index] = { file: event.target.files[0], name: doc, code: docCode };
    this.setState({ documents: files });
    this.setState({ cell: { valueid: valueid, period: period } });

    docList.map((files, i) => {
      if (files.active) {
      }
    });
    //console.log(docList);
  }

  getFileDetails(filestoreID, self, valueid, period, code) {
    let result = new Promise(function(resolve, reject) {
      let url = '/filestore/v1/files/id?tenantId=' + localStorage.getItem('tenantId') + '&fileStoreId=' + filestoreID;

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
            //self.setState({ currentFileName: filename, currentFileStoreID: filestoreID });

            resolve({ currentFileName: filename, currentFileStoreID: filestoreID });
          }
        }
      };
      oReq.send();
    });

    result.then(filename => {
      let fileDetails = self.state.currentFileName.slice();

      if (!fileDetails[valueid]) {
        fileDetails[valueid] = [];
      }
      if (!fileDetails[valueid][period]) {
        fileDetails[valueid][period] = [];
      }
      fileDetails[valueid][period][code] = filename.currentFileName;
      console.log(fileDetails, 'setting values');
      self.setState({ currentFileName: fileDetails, currentFileStoreID: filename.currentFileStoreID });
    });
  }

  renderFilePanel(self, item, i, code) {
    let displayFile = false;
    let fileNameArr = [];
    let storeID;
    //console.log(item);
    self.props.kpiresult.map(kpi => {
      kpi.kpiValue.valueList.map(kpiValue => {
        //console.log(kpi.kpiValue.valueList,' Value list');

        kpiValue.documents.map(kpidoc => {
          console.log(kpidoc, 'elements :');

          let valueid = kpiValue.valueid;
          let period = kpiValue.period;

          if (kpidoc.fileStoreId) {
            if (!fileNameArr[valueid]) {
              fileNameArr[valueid] = [];
            }

            if (!fileNameArr[valueid][period]) {
              fileNameArr[valueid][period] = [];
            }

            if (kpidoc.code) {
              fileNameArr[valueid][period][kpidoc.code] = kpidoc.fileStoreId;
            }
          }

          // console.log(fileNameArr,'file Name Arr');
          // console.log(storeID,'store ID');
        });

        //fileNameArr.currentFileName
        // let kpidoc = kpiValue.documents[0];
        // console.log(kpiValue.documents);
        // console.log(kpidoc);
        // if (kpidoc) {

        //       self.getFileDetails(kpidoc.fileStoreId,self);

        // }
      });
    });
    //console.log(code,'--code--',fileNameArr[self.props.cell.valueid][self.props.cell.period][code]['code']);
    //console.log(fileNameArr[self.props.cell.valueid]);
    if (
      fileNameArr[self.props.cell.valueid] &&
      fileNameArr[self.props.cell.valueid][self.props.cell.period] &&
      fileNameArr[self.props.cell.valueid][self.props.cell.period][code] &&
      this.state.currentFileStoreID !== fileNameArr[self.props.cell.valueid][self.props.cell.period][code]
    ) {
      self.getFileDetails(
        fileNameArr[self.props.cell.valueid][self.props.cell.period][code],
        self,
        self.props.cell.valueid,
        self.props.cell.period,
        code
      );
    }

    let display = '';
    if (
      self.state.currentFileName[self.props.cell.valueid] &&
      self.state.currentFileName[self.props.cell.valueid][self.props.cell.period] &&
      self.state.currentFileName[self.props.cell.valueid][self.props.cell.period][code]
    ) {
      display = self.state.currentFileName[self.props.cell.valueid][self.props.cell.period][code];
    }
    console.log(self.state.currentFileName, 'current file details ---');
    console.log(self.props.cell.period, 'period ---');
    console.log(code, 'code ---');
    console.log(display, 'display');
    return (
      <div>
        {display && (
          <span>
            <a
              href={
                window.location.origin + '/filestore/v1/files/id?tenantId=' + localStorage.tenantId + '&fileStoreId=' + this.state.currentFileStoreID
              }
              target="_blank"
            >
              {display}{' '}
            </a>
          </span>
        )}
        {!display && (
          <input
            id={'file_' + self.props.cell.valueid + self.props.cell.period}
            type="file"
            accept=".xls,.xlsx,.txt,.json,.doc,.docx"
            onChange={e => self.handleFile(e, self.props.cell.valueid, self.props.cell.period, item.name, i, self.props.data, code)}
          />
        )}
      </div>
    );
  }

  render() {
    const actions = [
      <FlatButton label="Cancel" primary={true} onClick={this.handleClose.bind(this)} />,
      <RaisedButton label="Upload" primary={true} keyboardFocused={true} onClick={this.docUpload.bind(this)} />,
    ];
    //this.setState({fileAttrList:this.props.data});
    return (
      <Dialog title="KPIs Documents" actions={actions} modal={true} open={this.props.open}>
        {this.props.data.map((item, i) => {
          console.log(item, 'render panel');
          return [
            <Row>
              <Col xs={6} md={6}>
                <strong>{item.name}</strong>
                {item.active && (
                  <span
                    style={{
                      color: '#FF0000',
                    }}
                  >
                    *
                  </span>
                )}
              </Col>
              <Col xs={6} md={6}>
                {this.renderFilePanel(this, item, i, item.code)}
              </Col>
            </Row>,
            <br />,
          ];
        })}
      </Dialog>
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

export default connect(mapStateToProps)(KPIDocumentField);
