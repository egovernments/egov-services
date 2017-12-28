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
      currentFileName: '',
      currentFileStoreID: '',
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

      let result = new Promise(function(resolve, reject) {
        fileUpload(fileInfo.file, moduleName, function(err, res) {
          if (err) {
            console.log('unable to upload');
          } else {
            resolve({ fileStoreId: res.files[0].fileStoreId, name: fileInfo.name, valueid: valueid, period: period });
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

  handleFile(event, valueid, period, doc, index, docList) {
    let files = this.state.documents.slice();

    if (!files[valueid]) {
      files[valueid] = [];
    }
    if (!files[valueid][period]) {
      files[valueid][period] = [];
    }

    files[valueid][period][index] = { file: event.target.files[0], name: doc };
    this.setState({ documents: files });
    this.setState({ cell: { valueid: valueid, period: period } });

    docList.map((files, i) => {
      if (files.active) {
      }
    });
    //console.log(docList);
  }

  getFileDetails(filestoreID, self) {
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

          console.log('lalu chettan', filename);
          self.setState({ currentFileName: filename, currentFileStoreID: filestoreID });
        }
      }
    };
    oReq.send();
  }

  renderFilePanel(self, item, i) {
    let displayFile = false;
    let fileName;
    self.props.kpiresult.map(kpi => {
      kpi.kpiValue.valueList.map(kpiValue => {
        console.log('item :', kpiValue.documents);
        kpiValue.documents.map(kpidoc => {
          //console.log(kpidoc);
          if (kpidoc.code == item.code) {
            self.getFileDetails(kpidoc.fileStoreId, self);
          }
        });

        // let kpidoc = kpiValue.documents[0];
        // console.log(kpiValue.documents);
        // console.log(kpidoc);
        // if (kpidoc) {

        //       self.getFileDetails(kpidoc.fileStoreId,self);

        // }
      });
    });


    return (
      <div>
        {self.state.currentFileName && (
          <span>
            <a
              href={
                window.location.origin + '/filestore/v1/files/id?tenantId=' + localStorage.tenantId + '&fileStoreId=' + self.state.currentFileStoreID
              }
              target="_blank"
            >
              {self.state.currentFileName}{' '}
            </a>
          </span>
        )}
        {!self.state.currentFileName && (
          <input
            id={'file_' + self.props.cell.valueid + self.props.cell.period}
            type="file"
            accept=".xls,.xlsx,.txt,.json,.doc,.docx"
            onChange={e => self.handleFile(e, self.props.cell.valueid, self.props.cell.period, item.name, i, self.props.data)}
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
          console.log(item, 'here');
          console.log(this.props.kpiresult, 'here');
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
                {this.renderFilePanel(this, item, i)}
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
