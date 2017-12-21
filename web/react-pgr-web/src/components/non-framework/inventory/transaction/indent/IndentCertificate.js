import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Card, CardHeader, CardTitle, CardText} from 'material-ui/Card';
import {Grid, Row, Col, Table} from 'react-bootstrap';
import pdfMake from "pdfmake/build/pdfmake";
import pdfFonts from "pdfmake/build/vfs_fonts";
import Api from '../../../../../api/api';
import {translate, epochToDate, dataURItoBlob} from '../../../../common/common';
import {fonts, writeMultiLanguageText, getBase64FromImageUrl} from '../../../../common/pdf-generation/PdfConfig';
import PdfViewer from '../../../../common/pdf-generation/PdfViewer';
import styles from '../../../../../styles/material-ui';
import RaisedButton from 'material-ui/RaisedButton';
const constants = require('../../../../common/constants');

var self;

const CONFIG_DEPT_KEY = "default.citizen.workflow.initiator.department.name";
const CONFIG_MUNICIPAL_ACT_KEY = "default.pdf.municipal.act.section";
const DOCUMENT_NAME = "INDENT_CERTIFICATE";
const TL_BUSINESS_CODE = "INVENTORY";

class IndentCertificate extends Component{
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

  doInitialStuffs = ()=>{
    var ulbLogoPromise = getBase64FromImageUrl("./temp/images/headerLogo.png");
    var stateLogoPromise = getBase64FromImageUrl("./temp/images/AS.png");

    var _this=this;
    this.props.setLoadingStatus('loading');

    let indentNumber = this.props.match.params.id;

    Promise.all([
      ulbLogoPromise,
      stateLogoPromise,
      Api.commonApiGet("https://raw.githubusercontent.com/abhiegov/test/master/tenantDetails.json",{timestamp:new Date().getTime()},{}, false, true),
      Api.commonApiPost("/inventory-services/indents/_search",{indentNumber:indentNumber},{tenantId:this.getTenantId(), pageSize:"500"}, false, true)
    ]).then((response) => {
      var cityName = response[2]["details"][this.getTenantId()]['name'];
      _this.generatePdf(response[0].image, response[1].image,
      response[3].indents[0], cityName);
    }).catch(function(err) {
       //_this.props.errorCallback(err.message);
    });

  }

  getTenantId = ()=>{
    return localStorage.getItem("tenantId") || "default";
  }


  generatePdf = (ulbLogo, stateLogo, intentObj, ulbName) => {


  var _this = this;


  //assigning fonts
  pdfMake.fonts = fonts;

  let tableDetails = intentObj.indentDetails.map((item)=>{
    return [item.material.code, item.material.name, item.indentQuantity, item.uom.code, item.indentQuantity, item.indentQuantity * item.indentQuantity, item.remarks]
  });


  //document defintion
  var docDefinition = {
    pageSize: 'A4',
    pageOrientation: 'landscape',
    pageMargins: [ 30, 30, 30, 30 ],
    content: [
      {text: writeMultiLanguageText('नगरपरिषद'), alignment: 'center', bold:true},
      {text: writeMultiLanguageText('नमुना क्रमांक ७१'),alignment: 'center', bold:true },
      {text: writeMultiLanguageText('(नियम क्रमांक २५८ पहा)'),alignment: 'center', bold:true },
      ' ',
      ' ',
      {
        columns: [
          {
            // auto-sized columns have their widths based on their content
            width: '*',
            text: writeMultiLanguageText('आर्थिक वर्ष <2017-2018>')
          },
          {
            // star-sized columns fill the remaining space
            // if there's more than one star-column, available width is divided equally
            width: '*',
            text: writeMultiLanguageText('साहित्य मागणी नोंद')
          },
          {
            // fixed width
            width: 'auto',
            text: writeMultiLanguageText('भांडार विभाग प्रत')
          }
        ],
        // optional space between columns
        columnGap: 10
      },
      ' ',
      {
        columns: [
          {
            // auto-sized columns have their widths based on their content
            width: '*',
            text: writeMultiLanguageText('मागणी  नमुना क्रमांक:IndentNumber')
          },
          {
            // star-sized columns fill the remaining space
            // if there's more than one star-column, available width is divided equally
            width: '*',
            text: writeMultiLanguageText('दिनांक: 29/11/2017'),
            alignment: 'right'
          }
        ],
        // optional space between columns
        columnGap: 10
      },
      ' ',
      {text: writeMultiLanguageText('प्रती,')},
      {text: writeMultiLanguageText('Mr.XYZ,')},
      {text: writeMultiLanguageText('भांडार विभाग प्रमुख,')},
      {text: writeMultiLanguageText('नगरपरिषद,')},
      ' ',
      ' ',
      {
        columns: [
          {
            // auto-sized columns have their widths based on their content
            width: 'auto',
            text: writeMultiLanguageText('साहित्य ज्यास सुपूर्द करण्यात येईल त्या व्यक्तीचे नाव :')
          },
          {
            // star-sized columns fill the remaining space
            // if there's more than one star-column, available width is divided equally
            width: '*',
            text: intentObj.designation,
            alignment: 'center'
          }
        ],
        // optional space between columns
        columnGap: 10
      },
      ' ',
      ' ',
      {text: writeMultiLanguageText('कृपया पुढील वस्तूंचा पुरवठा करण्यात यावा.'), alignment:'center', bold:true},
      ' ',
      {
        table: {
          // headers are automatically repeated if the table spans over multiple pages
          // you can declare how many rows should be treated as headers
          headerRows: 1,
          widths: [ '*', '*', '*', '*', '*', '*', '*' ],

          body: [
            [ {text: writeMultiLanguageText('वस्तूचा कोड'), alignment:'center', bold:true}, {text: writeMultiLanguageText('वस्तूचे नाव'), alignment:'center', bold:true}, {text: writeMultiLanguageText('संख्या'), alignment:'center', bold:true}, {text: writeMultiLanguageText('युनिट'), alignment:'center', bold:true}, {text: writeMultiLanguageText('दर'), alignment:'center', bold:true}, {text: writeMultiLanguageText('रक्कम रु.'), alignment:'center', bold:true}, {text: writeMultiLanguageText('शेरा'), alignment:'center', bold:true}],
            [ {text: writeMultiLanguageText('१'), alignment:'center', bold:true}, {text: writeMultiLanguageText('२'), alignment:'center', bold:true}, {text: writeMultiLanguageText('३'), alignment:'center', bold:true}, {text: writeMultiLanguageText('४'), alignment:'center', bold:true}, {text: writeMultiLanguageText('५'), alignment:'center', bold:true}, {text: writeMultiLanguageText('६(३X५)'), alignment:'center', bold:true}, {text: writeMultiLanguageText('७'), alignment:'center', bold:true}],
            ...tableDetails
          ]
        }
      },
      ' ',
      {text: writeMultiLanguageText('प्रभारी अधिकारी'), alignment:'right'},
      {text: writeMultiLanguageText('विभाग'), alignment:'right'},
      ' ',
      {text: writeMultiLanguageText('-----------* व्यतिरिक्त केलेल्या मागणीवरून पुरवठा केलेल्या वस्तू पहा. साहित्य निर्गमन नोंद क्रमांक------------------------'), alignment:'left'},
      ' ',
      {text: writeMultiLanguageText('दिनांक_____________'), alignment:'left'},
      ' ',
      {text: writeMultiLanguageText('*--------------------- व्यतिरिक्त मागणी केलेल्या वस्तू अचूकपणे प्राप्त झाल्या.'), alignment:'leftright'},
      ' ',
      {text: writeMultiLanguageText('प्रभारी अधिकारी'), alignment:'right'},
      {text: writeMultiLanguageText('विभाग'), alignment:'right'},
  ],
    styles: {
      title: {
        fontSize: 15,
        bold:true,
        lineHeight:1.1
      },
      subTitle: {
        fontSize: 12,
        lineHeight: 1.1
      },
      subTitle2: {
        fontSize: 12
      },
      contentTitle:{
        fontSize: 12
      }
    },
    defaultStyle: {
      fontSize: 11
    }
  }

  const pdfDocGenerator = pdfMake.createPdf(docDefinition);

  pdfDocGenerator.getDataUrl((dataUrl) => {
    this.setState({
      pdfData: dataUrl
    });

    let {
        setLoadingStatus
    } = this.props;
    setLoadingStatus('hide');
 });

}

  render(){
    self = this;
    let {viewLicense} = this.props;
    console.log(viewLicense);
    return(
      <PdfViewer pdfData={this.state.pdfData} title="inventory.indent.certificate.title">
        <div className="text-center">
          <RaisedButton style={styles.marginStyle} href={this.state.pdfData} download label={translate('tl.download')} download primary={true}/>
        </div>
      </PdfViewer>
    )
  }
}

const mapStateToProps = state => {
  return ({});
};

const mapDispatchToProps = dispatch => ({
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  }
});

const ViewIndentCertificate = connect(
  mapStateToProps,
  mapDispatchToProps
)(IndentCertificate);

export default ViewIndentCertificate;
