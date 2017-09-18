import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Card, CardHeader, CardTitle, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import Api from '../../../../api/api';
import {translate, epochToDate} from '../../../common/common';
import styles from '../../../../styles/material-ui';
import RaisedButton from 'material-ui/RaisedButton';
import jsPDF from 'jspdf';
import "jspdf-autotable";
import PDFObject from "pdfobject";
import html2canvas from "html2canvas";
const constants = require('../../../common/constants');

var self;

const customStyles = {
  labelStyle: {
    marginBottom: '5px'
  }
}

const CONFIG_DEPT_KEY = "default.citizen.workflow.initiator.department.name";
const CONFIG_MUNICIPAL_ACT_KEY = "default.pdf.municipal.act.section";
const DOCUMENT_NAME = "LICENSE_CERTIFICATE";

function dataURItoBlob(dataURI) {
  // convert base64/URLEncoded data component to raw binary data held in a string
  var byteString;
  if (dataURI.split(',')[0].indexOf('base64') >= 0)
    byteString = atob(dataURI.split(',')[1]);
  else
    byteString = unescape(dataURI.split(',')[1]);

  // separate out the mime component
  var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

  // write the bytes of the string to a typed array
  var ia = new Uint8Array(byteString.length);
  for (var i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }

  return new Blob([ia], {
    type: mimeString
  });
}

class PrintCertificate extends Component{
  constructor(props){
    super(props);
    this.state={
      pdfData : undefined
    };
  }
  componentDidMount(){
    window.scrollTo(0,0);
    this.doInitialStuffs();
  }
  componentWillReceiveProps(nextProps){
    if(this.props.match.params.id !== nextProps.match.params.id){
      this.initData(nextProps.match.params.id);
    }
  }

  handleError = (msg) => {
    let {toggleDailogAndSetText, setLoadingStatus}=this.props;
    setLoadingStatus('hide');
    toggleDailogAndSetText(true, msg);
  }

  requestAsync = (url) => {

      return new Promise(function(resolve, reject) {

          var image = new Image();
          image.setAttribute('crossOrigin', 'anonymous'); //getting images from external domain

          image.onload = function () {
              var canvas = document.createElement('canvas');
              canvas.width = this.naturalWidth;
              canvas.height = this.naturalHeight;
              //next three lines for white background in case png has a transparent background
              var ctx = canvas.getContext('2d');
              ctx.fillStyle = '#fff';  /// set white fill style
              ctx.fillRect(0, 0, canvas.width, canvas.height);

              canvas.getContext('2d').drawImage(this, 0, 0);
              var base64Img = canvas.toDataURL('image/png');
              resolve({image:base64Img});
          };

          image.onerror = function(){
            reject({message : `Oops! Something isn't right. Please try again later.`});
          }

          image.src = url;
      });
  }

  doInitialStuffs = ()=>{
    var ulbLogoPromise = this.requestAsync("./temp/images/headerLogo.png");
    var stateLogoPromise = this.requestAsync("./temp/images/AS.png");

    var _this=this;

    Promise.all([
      ulbLogoPromise,
      stateLogoPromise,
      Api.commonApiPost("/tl-services/configurations/v1/_search",{},{tenantId:this.getTenantId(), pageSize:"500"}, false, true)
    ]).then((response) => {
      console.log('response', response);
      _this.generatePdf(response[0].image, response[1].image, response[2].TLConfiguration);
    }).catch(function(err) {
       _this.props.errorCallback(err.message);
    });
  }

  getTenantId = ()=>{
    return localStorage.getItem("tenantId") || "default";
  }

  generatePdf = (ulbLogo, stateLogo, certificateConfigDetails) => {

  //getDataUri('./temp/images/headerLogo.png');

  let license = this.props.license;

  var _this = this;

  var departmentName = certificateConfigDetails[CONFIG_DEPT_KEY];
  var municipalActDetails = certificateConfigDetails[CONFIG_MUNICIPAL_ACT_KEY];

  var doc = new jsPDF('p', 'pt', 'a4')
  var docWidth = 594,
    docMargin = 20,
    headerHeight = 72,
    docTitleTop = 40,
    docSubTitle1Top = 60,
    docSubTitle2Top = 80,
    contentMargin = 10;
  var docContentWidth = docWidth - docMargin * 2;

  let lastYOffset = 0;

  //Header start
  var centeredText = function(text, y, isSameLine = false, offset = -1) {
    var textWidth = doc.getStringUnitWidth(text) * doc.internal.getFontSize() / doc.internal.scaleFactor;
    if (!isSameLine) {
      var textHeight = doc.getTextDimensions(text);
      lastYOffset = y + textHeight.h;
    }
    var textOffset = offset > -1 ? (offset - textWidth) / 2 + offset : (doc.internal.pageSize.width - textWidth) / 2;
    doc.text(textOffset, y, text);
  }

  var rightText = function(text, y, isSameLine = false, offset = -1) {
    var textWidth = doc.getStringUnitWidth(text) * doc.internal.getFontSize() / doc.internal.scaleFactor;
    if (!isSameLine) {
      var textHeight = doc.getTextDimensions(text);
      lastYOffset = y + textHeight.h;
    }
    var textOffset = offset > -1 ? offset : (doc.internal.pageSize.width - docMargin) - textWidth;
    doc.text(textOffset, y, text);
  }

  var leftText = function(text, y, isSameLine = false) {
    var textWidth = doc.getStringUnitWidth(text) * doc.internal.getFontSize() / doc.internal.scaleFactor;
    if (!isSameLine) {
      var textHeight = doc.getTextDimensions(text);
      lastYOffset = y + textHeight.h;
    }
    var textOffset = docMargin;
    doc.text(textOffset, y, text);
  }

  doc.setFontSize(16);
  doc.rect(docMargin, docMargin, docContentWidth, headerHeight);
  centeredText("Roha Municipal", docTitleTop);

  doc.setFontSize(12);

  centeredText(departmentName && departmentName.length > 0 ? departmentName[0] : "ERROR FIND DEPARTMENT", lastYOffset + 5);
  centeredText(license.tradeType + " License", lastYOffset + 5);
  doc.addImage(ulbLogo, 'png', 30, docMargin + 5, 60, 60);
  doc.addImage(stateLogo, 'png', docWidth - 90, docMargin + 5, 60, 60);

  //Header end

  doc.setFontSize(11);
  leftText(municipalActDetails && municipalActDetails.length > 0 ? municipalActDetails[0] : 'As per the Municipal act', lastYOffset + 20);

  leftText('License Holder Name : ' + license.ownerName, lastYOffset + 10, true);
  rightText('License No : ' + license.licenseNumber, lastYOffset + 10);

  leftText('Business Name : ' + license.tradeTitle, lastYOffset + 5, true);
  rightText('Date : ' + epochToDate(license.applicationDate), lastYOffset + 5);

  leftText('Business Address : ' + license.tradeAddress, lastYOffset + 5);

  //Table columns
  var columns = [{
      title: "License Type / Sub-Type",
      dataKey: "licenseType"
    },
    {
      title: "Measuring Parameters",
      dataKey: "measuringParams"
    },
    {
      title: "Input Values",
      dataKey: "measurementValue"
    },
    {
      title: "Units",
      dataKey: "measurementUnit"
    },
    {
      title: "Fee Type",
      dataKey: "feeType"
    },
    {
      title: "Fee Amount",
      dataKey: "feeAmount"
    }
  ];

  var applicationFee = license.applications.find((application) => application.applicationType === "NEW");

  //Table Data
  var data = [{
      licenseType: license.category + " / " + license.subCategory,
      measuringParams: 'Fee Factor1',
      measurementValue: license.quantity,
      measurementUnit: license.uom,
      feeType: 'License Fee',
      feeAmount: applicationFee && applicationFee.licenseFee ? applicationFee.licenseFee.toFixed(2) : "-"
    },
    {
      licenseType: "",
      measuringParams: '',
      measurementValue: '',
      measurementUnit: '',
      feeType: 'Total Amount',
      feeAmount: applicationFee && applicationFee.licenseFee ? applicationFee.licenseFee.toFixed(2) : "-"
    }
  ];

  //Rowspan Table Header
  var headerColumns = [{
      title: "",
      dataKey: "licenseType"
    },
    {
      title: "Measurements",
      dataKey: "measuringParams"
    },
    {
      title: "Fee Details",
      dataKey: "measurementValue"
    }
  ]

  //metrics
  var columnWidth = (docWidth - docMargin * 2) / columns.length;
  var tableMargin = docMargin;

  var tableStyle = {
    theme: 'plain',
    tableLineColor: [255, 255, 255],
    tableLineWidth: 0,
    styles: {
      // font: 'courier',
      lineColor: [0, 0, 0],
      overflow: 'linebreak',
      lineWidth: 0.1
    },
    headerStyles: {
      fillColor: [255, 255, 255],
      fontSize: 10,
      textColor: 0
    },
    bodyStyles: {
      fillColor: [255, 255, 255],
      textColor: 0
    }
  };

  //header table
  doc.autoTable(headerColumns, [], {
    ...tableStyle,
    startY: lastYOffset,
    columnStyles: {
      licenseType: {
        columnWidth: columnWidth,
        fillColor: 148
      },
      measuringParams: {
        columnWidth: columnWidth * 2
      },
      measurementValue: {
        columnWidth: columnWidth * 3
      }
    },
    margin: {
      horizontal: tableMargin
    },
    addPageContent: function(data) {}
  });

  //table data
  doc.autoTable(columns, data, {
    ...tableStyle,
    startY: doc.autoTable.previous.finalY,
    columnStyles: {
      licenseType: {
        columnWidth: columnWidth
      },
      measuringParams: {
        columnWidth: columnWidth
      },
      measurementValue: {
        columnWidth: columnWidth
      },
      measurementUnit: {
        columnWidth: columnWidth
      },
      feeType: {
        columnWidth: columnWidth
      },
      feeAmount: {
        columnWidth: columnWidth
      }
    },
    margin: {
      horizontal: tableMargin
    },
    addPageContent: function(data) {}
  });

  lastYOffset = doc.autoTable.previous.finalY;

  //TODO addpage condition check y here


  //footer
  leftText('Total License Fee : ' + applicationFee.licenseFee.toFixed(2), lastYOffset + 20);
  leftText('Receipt No : ', lastYOffset + 5, true);
  rightText('Receipt Date : ', lastYOffset + 5, false, docContentWidth / 2);
  leftText('License Valid From : ' + epochToDate(license.licenseValidFromDate), lastYOffset + 5, true);
  rightText('Expiry Date : ' + epochToDate(license.expiryDate), lastYOffset + 5, false, docContentWidth / 2);

  centeredText('Signing Authority', lastYOffset + 80, false, docContentWidth / 2);
  centeredText('Roha Municipal', lastYOffset + 5, false, docContentWidth / 2);

  const pdfData = doc.output('datauristring');
  this.setState({
    pdfData: pdfData
  });

  // if(license.licenseNumber){
  //
  // }
  let formData = new FormData();
  var blob = dataURItoBlob(pdfData);
  formData.append("file", blob, `TL_${license.licenseNumber || '0'} + .pdf`);
  formData.append("tenantId", localStorage.getItem('tenantId'));
  formData.append("module", constants.TRADE_LICENSE_FILE_TAG);

  let {
    setLoadingStatus
  } = this.props;

  setLoadingStatus('loading');

  var errorFunction = function(err) {
    _this.props.errorCallback(err.message);
  };

  Api.commonApiPost("/filestore/v1/files", {}, formData).then(function(response) {
    if (response.files && response.files.length > 0) {
      //response.files[0].fileStoreId
      var noticeDocument = [{
        licenseId: license.id,
        tenantId: _this.getTenantId(),
        documentName: DOCUMENT_NAME,
        fileStoreId: response.files[0].fileStoreId
      }]
      Api.commonApiPost("tl-services/noticedocument/v1/_create", {}, {
        NoticeDocument: noticeDocument
      }, false, true).then(function(response) {
        _this.props.successCallback();
        //setLoadingStatus('hide');
      }, errorFunction);
    } else
      setLoadingStatus('hide');

  }, errorFunction);

}

  render(){
    self = this;
    let {viewLicense} = this.props;
    console.log(viewLicense);
    return(
      <Grid style={styles.fullWidth}>
        <Card style={styles.marginStyle}>
          <CardTitle title="Trade License Certificate" />
          <CardText>
            <iframe type="application/pdf" style={{width:'100%'}} height="800" src={this.state.pdfData}></iframe>
          </CardText>
        </Card>
      </Grid>
    )
  }
}

const mapStateToProps = state => {
  return ({viewLicense : state.form.form});
};

const mapDispatchToProps = dispatch => ({
  setForm: (data) => {
    dispatch({
      type: "SET_FORM",
      data,
      isFormValid:false,
      fieldErrors: {},
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
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  }
});

const ViewPrintCertificate = connect(
  mapStateToProps,
  mapDispatchToProps
)(PrintCertificate);

export default ViewPrintCertificate;
