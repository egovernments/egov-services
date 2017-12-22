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
          console.log(this.props.data, 'here');
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
                <input
                  id={'file_' + this.props.cell.valueid + this.props.cell.period}
                  type="file"
                  accept=".xls,.xlsx,.txt,.json,.doc,.docx"
                  onChange={e => this.handleFile(e, this.props.cell.valueid, this.props.cell.period, item.name, i, this.props.data)}
                />
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
