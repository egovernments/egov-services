import React, {Component} from 'react';
import {connect} from 'react-redux';
import FlatButton from 'material-ui/FlatButton';
import RaisedButton from 'material-ui/RaisedButton';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import MenuItem from 'material-ui/MenuItem';
import Dialog from 'material-ui/Dialog';
import {translate} from '../../../common/common';
import Api from '../../../../api/api';
import styles from '../../../../styles/material-ui';

const patterns = {
  date:/^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/g,
  ownerName:/^.[a-zA-Z. ]{2,99}$/,
  tradeTitle : /^[a-zA-Z0-9@:()/#,. -]*$/,
  mobileNumber : /^\d{10}$/g
}

class NoticeSearch extends Component {
  constructor(props) {
    super(props);
    this.state={
      open:false
    };
  }
  componentDidMount(){
    this.props.initForm();
  }
  handleOpen = () => {
    this.setState({open: true});
  };

  handleClose = () => {
    this.setState({open: false});
  };
  search = () => {
    var self = this;
    let {NoticeSearch, setLoadingStatus} = this.props;
    // console.log(request);
    setLoadingStatus('loading');
    Api.commonApiPost("tl-services/noticedocument/v1/_search",NoticeSearch, {}, false, true).then(function(response)
    {
      self.setState({
        noticeResponse : response.NoticeDocument,
        showTable : true
      },setLoadingStatus('hide'));
    },function(err) {
      setLoadingStatus('hide');
      self.handleError(err.message);
    });
  }
  renderTable = () => {
    return(
      <Card style={styles.marginStyle}>
        <Table id="requestTable">
          <thead>
            <tr>
              <th>Document Name</th>
              <th>License Id</th>
            </tr>
          </thead>
          <tbody>
            {this.state.noticeResponse.map((notice, index) => {
              return(
                <tr key={index} onClick={(e)=>this.showFile(notice.fileStoreId)}>
                  <td>{notice.documentName}</td>
                  <td>{notice.licenseId}</td>
                </tr>
              )
            })}
          </tbody>
        </Table>
      </Card>
    )
  }
  showFile = (fileStoreId) => {
    var self = this;
    var fileURL = '/filestore/v1/files/id?fileStoreId='+fileStoreId+'&tenantId='+localStorage.getItem('tenantId');
    var oReq = new XMLHttpRequest();
    oReq.open("GET", fileURL, true);
    oReq.responseType = "arraybuffer";
    // console.log(fileURL);
    oReq.onload = function(oEvent) {
      var blob = new Blob([oReq.response], {type: "application/pdf"});
      var url = URL.createObjectURL(blob);
      self.setState({
        iframe_src : url
      });
      self.handleOpen();
    };
    oReq.send();
  }
  handleError = (msg) => {
    let {toggleDailogAndSetText, setLoadingStatus}=this.props;
    setLoadingStatus('hide');
    toggleDailogAndSetText(true, msg);
  }
  render(){
    let {handleChange, NoticeSearch, fieldErrors} = this.props;
    const actions = [
      <FlatButton
        label="Cancel"
        primary={true}
        onClick={this.handleClose}
      />
    ];
    return(
      <div>
        <Card style={styles.marginStyle}>
          <CardHeader style={{paddingBottom:0}} title={< div style = {styles.headerStyle} > {translate('Search Notice')}< /div>}/>
          <CardText style={{paddingTop:0}}>
            <Grid>
              <Row>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.search.result.groups.applicationNumber')}
                  value={NoticeSearch.applicationNumber ? NoticeSearch.applicationNumber : ""}
                  errorText={fieldErrors.applicationNumber ? fieldErrors.applicationNumber : ''}
                  onChange={(e,newValue)=>{handleChange(newValue, 'applicationNumber', false, '','');}}
                  />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.search.groups.tradeOwnerName')}
                  value={NoticeSearch.ownerName ? NoticeSearch.ownerName : ""}
                  errorText={fieldErrors.ownerName ? fieldErrors.ownerName : ''}
                  onChange={(e,newValue)=>{handleChange(newValue, 'ownerName', false, patterns.ownerName, 'Enter Valid Trade Owner Name(Min:3, Max:100)');}}
                  />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.search.groups.mobileNumber')}
                  value={NoticeSearch.mobileNumber ? NoticeSearch.mobileNumber : ""}
                  errorText={fieldErrors.mobileNumber ? fieldErrors.mobileNumber : ''}
                  maxLength="10"
                  onChange={(e,newValue)=>{handleChange(newValue, 'mobileNumber', false, patterns.mobileNumber, translate('core.lbl.enter.mobilenumber'));}}
                  />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.search.groups.tradeTitle')}
                  value={NoticeSearch.tradeTitle ? NoticeSearch.tradeTitle : ""}
                  errorText={fieldErrors.tradeTitle ? fieldErrors.tradeTitle : ''}
                  onChange={(e,newValue)=>{handleChange(newValue, 'tradeTitle', false, patterns.tradeTitle,"Enter Valid Trade Title (Max: 250)");}}
                  />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.search.groups.licenseNumber')}
                  value={NoticeSearch.licenseNumber ? NoticeSearch.licenseNumber : ""}
                  errorText={fieldErrors.licenseNumber ? fieldErrors.licenseNumber : ''}
                  onChange={(e,newValue)=>{handleChange(newValue, 'licenseNumber', false, '','');}}
                  />
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <SelectField maxHeight={200} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.search.groups.applicationStatus')}
                  value={NoticeSearch.applicationStatus ? NoticeSearch.applicationStatus : ""}
                  errorText={fieldErrors.applicationStatus ? fieldErrors.applicationStatus : ''}
                  onChange={(event, key, payload)=>{handleChange(payload, 'applicationStatus', false, '','');}}
                  >
                    <MenuItem value="" primaryText="Select" />
                  </SelectField>
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <SelectField maxHeight={200} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.create.groups.licensedocumenttype.licenseapptype')}
                  value={NoticeSearch.applicationType ? NoticeSearch.applicationType : ""}
                  errorText={fieldErrors.applicationType ? fieldErrors.applicationType : ''}
                  onChange={(event, key, payload)=>{handleChange(payload, 'applicationType', false, '','');}}
                  >
                    <MenuItem value="" primaryText="Select" />
                    <MenuItem value="New" primaryText="New" />
                    <MenuItem value="Renew" primaryText="Renew" />
                    <MenuItem value="Title Transfer" primaryText="Title Transfer" />
                    <MenuItem value="Cancellation" primaryText="Cancellation" />
                    <MenuItem value="Business Name Change" primaryText="Business Name Change" />
                  </SelectField>
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <SelectField maxHeight={200} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.create.licenses.groups.TradeLocationDetails.Ward')}
                  value={NoticeSearch.ward ? NoticeSearch.ward : ""}
                  errorText={fieldErrors.ward ? fieldErrors.ward : ''}
                  onChange={(event, key, payload)=>{handleChange(payload, 'ward', false, '','');}}
                  >
                    <MenuItem value="" primaryText="Select" />
                  </SelectField>
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <SelectField maxHeight={200} floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.search.documenttype')}
                  value={NoticeSearch.documentName ? NoticeSearch.documentName : ""}
                  errorText={fieldErrors.documentName ? fieldErrors.documentName : ''}
                  onChange={(event, key, payload)=>{handleChange(payload, 'documentName', false, '','');}}
                  >
                    <MenuItem value="" primaryText="Select" />
                    <MenuItem value="ACKNOWLEDGEMENT" primaryText="Acknowledgement" />
                    <MenuItem value="Inspection Report" primaryText="Inspection Report" />
                    <MenuItem value="Fee Receipt" primaryText="Fee Receipt" />
                    <MenuItem value="Certificate" primaryText="Certificate" />
                    <MenuItem value="Rejection Letter" primaryText="Rejection Letter" />
                  </SelectField>
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.search.fromDate')}
                  value={NoticeSearch.fromDate ? NoticeSearch.fromDate : ""}
                  errorText={fieldErrors.fromDate ? fieldErrors.fromDate : ''}
                  onChange={(e,newValue)=>{handleChange(newValue, 'fromDate', false, patterns.date,'Enter in dd/mm/yyyy Format');}}/>
                </Col>
                <Col xs={12} sm={4} md={3} lg={3}>
                  <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                  floatingLabelText={translate('tl.search.toDate')}
                  value={NoticeSearch.toDate ? NoticeSearch.toDate : ""}
                  errorText={fieldErrors.toDate ? fieldErrors.toDate : ''}
                  onChange={(e,newValue)=>{handleChange(newValue, 'toDate', false, patterns.date,'Enter in dd/mm/yyyy Format');}}/>
                </Col>
              </Row>
            </Grid>
          </CardText>
        </Card>
        <div className="text-center">
          <RaisedButton disabled={Object.values(fieldErrors).filter(String).length === 0 ? Object.values(NoticeSearch).filter(String).length >= 1 ? false : true : true } style={{margin:'15px 5px'}} label={translate('core.lbl.search')} primary={true} onClick={(e)=>{this.search()}}/>
        </div>
        {this.state.showTable ? this.renderTable() : ''}
        <Dialog
          title="Document"
          actions={actions}
          modal={false}
          open={this.state.open}
          onRequestClose={this.handleClose}
        >
          <iframe src={this.state.iframe_src} frameBorder="0" allowFullScreen height="500" width="100%"></iframe>
        </Dialog>
      </div>
    )
  }
}

const mapStateToProps = state => {
  // console.log(state.form.form, state.form.isFormValid);
  return ({NoticeSearch: state.form.form, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: []
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  handleChange: (value, property, isRequired, pattern, errorMsg) => {
    dispatch({type: "HANDLE_CHANGE", property, value, isRequired, pattern, errorMsg});
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  }
});

export default connect(mapStateToProps, mapDispatchToProps)(NoticeSearch);
