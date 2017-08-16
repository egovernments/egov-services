import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardText} from 'material-ui/Card';

import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "../../../../framework/showFields";

import {translate} from '../../../../common/common';
import Api from '../../../../../api/api';
import UiButton from '../../../../framework/components/UiButton';
import UiDynamicTable from '../../../../framework/components/UiDynamicTable';
import {fileUpload} from '../../../../framework/utility/utility';
import UiTable from '../../../../framework/components/UiTable';
import IconButton from 'material-ui/IconButton';
// import "jspdf";
// import "jspdf-autotable";
import jsPDF from 'jspdf';
import "jspdf-autotable";
import html2canvas from "html2canvas";




var specifications={};

let reqRequired = [];
class Report extends Component {
  state={
    pathname:""
  }
  constructor(props) {
    super(props);
    this.state = {
      showResult: false,
      resultList : {
        resultHeader: [],
        resultValues: []
      },
      values: []
    }
  }

  setLabelAndReturnRequired(configObject) {
    if(configObject && configObject.groups) {
      for(var i=0; i<configObject.groups.length; i++) {
        configObject.groups[i].label = translate(configObject.groups[i].label);
        for (var j = 0; j < configObject.groups[i].fields.length; j++) {
              configObject.groups[i].fields[j].label = translate(configObject.groups[i].fields[j].label);
              if (configObject.groups[i].fields[j].isRequired)
                  reqRequired.push(configObject.groups[i].fields[j].jsonPath);
        }

        if(configObject.groups[i].children && configObject.groups[i].children.length) {
          for(var k=0; k<configObject.groups[i].children.length; k++) {
            this.setLabelAndReturnRequired(configObject.groups[i].children[k]);
          }
        }
      }
    }
  }

  setDefaultValues (groups, dat) {
    for(var i=0; i<groups.length; i++) {
      for(var j=0; j<groups[i].fields.length; j++) {
        if(typeof groups[i].fields[j].defaultValue == 'string' || typeof groups[i].fields[j].defaultValue == 'number' || typeof groups[i].fields[j].defaultValue == 'boolean') {
          //console.log(groups[i].fields[j].name + "--" + groups[i].fields[j].defaultValue);
          _.set(dat, groups[i].fields[j].jsonPath, groups[i].fields[j].defaultValue);
        }

        if(groups[i].fields[j].children && groups[i].fields[j].children.length) {
          for(var k=0; k<groups[i].fields[j].children.length; k++) {
            this.setDefaultValues(groups[i].fields[j].children[k].groups);
          }
        }
      }
    }
  }

  getVal = (path) => {
    return typeof _.get(this.props.formData, path) != "undefined" ? _.get(this.props.formData, path) : "";
  }

  initData() {

    let self=this;
    let hashLocation = window.location.hash;
    specifications = require('../../../../framework/specs/collection/master/receipt').default;
    let { setMetaData, setModuleName, setActionName, initForm, setMockData, setFormData } = this.props;
    let obj = specifications[`collection.view`];

    Api.commonApiPost(obj.url, {transactionId:this.props.match.params.hasOwnProperty("id")?this.props.match.params.id:""}, {}, null, obj.useTimestamp).then(function(res){
        // console.log(res);
        self.handleChange({target:{value:res.Receipt}},"Receipt",false,"","");
        self.props.setLoadingStatus('hide');
        var resultList = {
          resultHeader: [{label: "#"}, ...obj.result.header],
          resultValues: []
        };
        var specsValuesList = obj.result.values;
        var values = _.get(res, obj.result.resultPath);
        if(values && values.length) {
          for(var i=0; i<values.length; i++) {
            var tmp = [i+1];
            for(var j=0; j<specsValuesList.length; j++) {
              tmp.push(_.get(values[i], specsValuesList[j]));
            }
            resultList.resultValues.push(tmp);
          }
        }
        self.setState({
          resultList,
          values,
          showResult: true
        });

        self.props.setFlag(1);
      }, function(err) {
        self.props.toggleSnackbarAndSetText(true, err.message, false, true);
        self.props.setLoadingStatus('hide');
      })
    // try {
    //   var hash = window.location.hash.split("/");
    //   if(hash.length == 4 && hashLocation.split("/")[1]!="transaction") {
    //     specifications = require(`./specs/${hash[2]}/${hash[2]}`).default;
    //   } else if(hashLocation.split("/")[1]!="transaction"){
    //     specifications = require(`./specs/${hash[2]}/master/${hash[3]}`).default;
    //   } else {
    //     specifications = require(`./specs/${hash[2]}/transaction/${hash[3]}`).default;
    //   }
    // } catch(e) {}

    reqRequired = [];
    this.setLabelAndReturnRequired(obj);
    initForm(reqRequired);
    setMetaData(specifications);
    setMockData(JSON.parse(JSON.stringify(specifications)));
    setModuleName("collection");
    setActionName("view");
    var formData = {};
    if(obj && obj.groups && obj.groups.length) this.setDefaultValues(obj.groups, formData);
    setFormData(formData);
    this.setState({
      pathname:this.props.history.location.pathname
    })
  }

  componentDidMount() {
      this.initData();

  }
  componentWillReceiveProps(nextProps)
  {
    if (this.state.pathname!=nextProps.history.location.pathname) {
      this.initData();
    }
  }

  // search = (e) => {
  //   e.preventDefault();
  //   let self = this;
  //   self.props.setLoadingStatus('loading');
  //   var formData = {...this.props.formData};
  //   for(var key in formData) {
  //     if(formData[key] !== "" && typeof formData[key] == "undefined")
  //       delete formData[key];
  //   }
  //
  //   Api.commonApiPost(self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].url, formData, {}, null, self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].useTimestamp).then(function(res){
  //     self.props.setLoadingStatus('hide');
  //     var resultList = {
  //       resultHeader: [{label: "#"}, ...self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.header],
  //       resultValues: []
  //     };
  //     var specsValuesList = self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.values;
  //     var values = _.get(res, self.props.metaData[`${self.props.moduleName}.${self.props.actionName}`].result.resultPath);
  //     if(values && values.length) {
  //       for(var i=0; i<values.length; i++) {
  //         var tmp = [i+1];
  //         for(var j=0; j<specsValuesList.length; j++) {
  //           tmp.push(_.get(values[i], specsValuesList[j]));
  //         }
  //         resultList.resultValues.push(tmp);
  //       }
  //     }
  //     self.setState({
  //       resultList,
  //       values,
  //       showResult: true
  //     });
  //
  //     self.props.setFlag(1);
  //   }, function(err) {
  //     self.props.toggleSnackbarAndSetText(true, err.message, false, true);
  //     self.props.setLoadingStatus('hide');
  //   })
  // }

  getVal = (path) => {
    return _.get(this.props.formData, path) || "";
  }

  handleChange=(e, property, isRequired, pattern, requiredErrMsg="Required",patternErrMsg="Pattern Missmatch") => {
      let {handleChange}=this.props;
      handleChange(e,property, isRequired, pattern, requiredErrMsg, patternErrMsg);
  }

  rowClickHandler = (index) => {
    var value = this.state.values[index];
    var _url = window.location.hash.split("/").indexOf("update") > -1 ? this.props.metaData[`${this.props.moduleName}.${this.props.actionName}`].result.rowClickUrlUpdate : this.props.metaData[`${this.props.moduleName}.${this.props.actionName}`].result.rowClickUrlView;
    var key = _url.split("{")[1].split("}")[0];
    _url = _url.replace("{" + key + "}", _.get(value, key));
    this.props.setRoute(_url);
  }

  getPurposeTotal=(purpose="",item=[])=>{
    let sum=0;
    _.forEach(_.filter(item, { 'purpose': purpose}),(value,key)=>{
      sum+=value.creditAmount;
    })
    return sum;
  }

  getTotal=(item=[])=>{
    let sum=0;
    _.forEach(item,(value,key)=>{
      sum+=value.creditAmount;
    })
    return sum;
  }

  getGrandTotal=(purpose="",item=[])=>{

    let sum=0;
    if (purpose) {
      _.forEach(item,(value,key)=>{
        _.forEach(_.filter(value.billAccountDetails, { 'purpose': purpose}),(value1,key1)=>{
          sum+=value1.creditAmount;
        })
      })
    }
    else {
      _.forEach(item,(value,key)=>{
        _.forEach(value.billAccountDetails,(value1,key1)=>{
          sum+=value1.creditAmount;
        })
      })
    }

    return sum;
  }

  int_to_words=(int)=> {
  if (int === 0) return 'zero';

  var ONES  = ['','one','two','three','four','five','six','seven','eight','nine','ten','eleven','twelve','thirteen','fourteen','fifteen','sixteen','seventeen','eighteen','nineteen'];
  var TENS  = ['','','twenty','thirty','fourty','fifty','sixty','seventy','eighty','ninety'];
  var SCALE = ['','thousand','million','billion','trillion','quadrillion','quintillion','sextillion','septillion','octillion','nonillion'];

  // Return string of first three digits, padded with zeros if needed
  function get_first(str) {
    return ('000' + str).substr(-3);
  }

  // Return string of digits with first three digits chopped off
  function get_rest(str) {
    return str.substr(0, str.length - 3);
  }

  // Return string of triplet convereted to words
  function triplet_to_words(_3rd, _2nd, _1st) {
    return (_3rd == '0' ? '' : ONES[_3rd] + ' hundred ') + (_1st == '0' ? TENS[_2nd] : TENS[_2nd] && TENS[_2nd] + '-' || '') + (ONES[_2nd + _1st] || ONES[_1st]);
  }

  // Add to words, triplet words with scale word
  function add_to_words(words, triplet_words, scale_word) {
    return triplet_words ? triplet_words + (scale_word && ' ' + scale_word || '') + ' ' + words : words;
  }

  function iter(words, i, first, rest) {
    if (first == '000' && rest.length === 0) return words;
    return iter(add_to_words(words, triplet_to_words(first[0], first[1], first[2]), SCALE[i]), ++i, get_first(rest), get_rest(rest));
  }

  return iter('', 0, get_first(String(int)), get_rest(String(int)));
}

  print=()=>{
    window.print();
  }

  generatePdf=()=>{
    // const input = document.getElementById('receipt');
    // html2canvas(input)
    //   .then((canvas) => {
    //     const imgData = canvas.toDataURL('image/jpeg');
    //     const pdf = new jsPDF();
    //     pdf.addImage(imgData, 'JPEG', 0, 0, 210,130);
    //     pdf.save("receipt.pdf");
    //   });
    let {tenantInfo}=this.props;
    let {getVal}=this;
    //
    //
    //
    // var doc = new jsPDF()

    var columns = [
	{ title: "A", dataKey: "A" },
	{ title: "B", dataKey: "B" },
	{ title: "C", dataKey: "C" }
];

var rows = [
	{ A: "A", B: "B", C: "C" },
	{ A: "A", B: "B", C: "C" }
];

var doc = new jsPDF('p', 'pt');
  doc.setFontSize(20);
  doc.setTextColor(40);
  doc.setFontStyle('normal');
  doc.text("YOLO", 10, 20);
  doc.text("YOLO2", 10, 50);

  doc.autoTable(columns, rows, {
    startY: doc.autoTableEndPosY() + 70,
    margin: { horizontal: 10 },
    styles: { overflow: 'linebreak' },
    bodyStyles: { valign: 'top' },
    columnStyles: { email: { columnWidth: 'wrap' } },
    theme: "grid"
  });

  // doc.autoTable(columns, rows);
  // doc.save('repro.pdf');


// let x=5,y=5,w=200,h=90,rectGap=10,originalX=5,originalY=5,originalX=5,originalY=5,dublicateX=5,dublicateY=5,triplicateX=5,triplicateY=5;
// doc.rect(x, y, w, h)
// doc.rect(x, (h*1)+rectGap, w, h)
// doc.rect(x, (h*2)+rectGap+5, w, h)
// doc.setFontSize(14);
// doc.setFontType("bold");
// doc.text(originalX+100, y+5, "Receipt"+" Original" , 'center');
// doc.text(originalX+100, y+10,"kurnool", 'center');
// doc.setFontType("bold");
// doc.text(originalX+10, y+20,"Payee Name:");
// doc.setFontType("normal");
// doc.text(originalX+41, y+20,"name");
// doc.setFontType("bold");
// doc.text(originalX+60, y+20,"Receipt Date:");
// doc.setFontType("normal");
// doc.text(originalX+92, y+20,"Date");
// doc.setFontType("bold");
// doc.text(originalX+110, y+20,"Address:");
// doc.setFontType("normal");
// doc.text(originalX+132, y+20,"Bangalore");
//







        // doc.setLineWidth(0.5);
      //   doc.line(15, 38, 195, 38);
      //   doc.text(15, 47, 'Lease details: ');
      //   doc.text(110, 47, 'Agreement No: ' + noticeData.agreementNumber);
      //   doc.text(15, 57, 'Lease Name: ' + noticeData.allotteeName);
      //   doc.text(110, 57, 'Asset No: ' + noticeData.assetNo);
      //   doc.text(15, 67, (noticeData.allotteeMobileNumber ? noticeData.allotteeMobileNumber + ", " : "") + (noticeData.doorNo ? noticeData.doorNo + ", " : "") + (noticeData.allotteeAddress ? noticeData.allotteeAddress + ", " : "") + tenantId.split(".")[1] + ".");
       //
      //
      //   doc.setFontType("normal");
      //   doc.text(15, 77, doc.splitTextToSize('1.    The period of lease shall be ' ));
      //   doc.setFontType("bold");
      //   doc.text(85, 77, doc.splitTextToSize(' ' + noticeData.agreementPeriod * 12 + ' '));
      //   doc.setFontType("normal");
      //   doc.text(93, 77, doc.splitTextToSize('months commencing from'));
      //   doc.setFontType("bold");
      //   doc.text(15, 83, doc.splitTextToSize(' ' + commencementDate + ' '));
      //   doc.setFontType("normal");
      //   doc.text(42, 83, doc.splitTextToSize('(dd/mm/yyyy) to' ));
      //   doc.setFontType("bold");
      //   doc.text(77, 83, doc.splitTextToSize(' ' + expiryDate + ' '));
      //   doc.setFontType("normal");
      //   doc.text(104, 83, doc.splitTextToSize('(dd/mm/yyyy).', (210 - 15 - 15)));
      //   doc.text(15, 91, doc.splitTextToSize('2.    The property leased is shop No'));
      //   doc.setFontType("bold");
      //   doc.text(93, 91, doc.splitTextToSize(' ' + noticeData.assetNo + ' '));
      //   doc.setFontType("normal");
      //   doc.text(101, 91, doc.splitTextToSize('and shall be leased for a sum of '));
      //   doc.setFontType("bold");
      //   doc.text(15, 97, doc.splitTextToSize('Rs.' + noticeData.rent + '/- (' + noticeData.rentInWord + ')'));
      //   doc.setFontType("normal");
      //   doc.text(111, 97, doc.splitTextToSize('per month exclusive of the payment'));
      //   doc.text(15, 103, doc.splitTextToSize('of electricity and other charges.', (210 - 15 - 15)));
      //   doc.text(15, 112, doc.splitTextToSize('3.   The lessee has paid a sum of '));
      //   doc.setFontType("bold");
      //   doc.text(90, 112, doc.splitTextToSize('Rs.' + noticeData.securityDeposit + '/- (' + noticeData.securityDepositInWord + ')'));
      //   doc.setFontType("normal");
      //   doc.text(15, 118, doc.splitTextToSize('as security deposit for the tenancy and the said sum is repayable or adjusted only at the end of the tenancy on the lease delivery vacant possession of the shop let out, subject to deductions, if any, lawfully and legally payable by the lessee under the terms of this lease deed and in law.', (210 - 15 - 15)));
      //   doc.text(15, 143, doc.splitTextToSize('4.   The rent for every month shall be payable on or before'));
      //   doc.setFontType("bold");
      //   doc.text(143, 143, doc.splitTextToSize(' ' + rentPayableDate + ' '));
      //   doc.setFontType("normal");
      //   doc.text(169, 143, doc.splitTextToSize('of the'));
      //   doc.text(15, 149, doc.splitTextToSize('succeeding month.', (210 - 15 - 15)));
      //   doc.text(15, 158, doc.splitTextToSize('5.   The lessee shall pay electricity charges to the Electricity Board every month without fail.', (210 - 15 - 15)));
      //   doc.text(15, 172, doc.splitTextToSize('6.   The lessor or his agent shall have a right to inspect the shop at any hour during the day time.', (210 - 15 - 15)));
      //   doc.text(15, 187, doc.splitTextToSize('7.   The Lessee shall use the shop let out duly for the business of General Merchandise and not use the same for any other purpose.  (The lessee shall not enter into partnership) and conduct the business in the premises in the name of the firm.  The lessee can only use the premises for his own business.', (210 - 15 - 15)));
      //   doc.text(15, 214, doc.splitTextToSize('8.    The lessee shall not have any right to assign, sub-let, re-let, under-let or transfer the tenancy or any portion thereof.', (210 - 15 - 15)));
      //   doc.text(15, 229, doc.splitTextToSize('9.    The lessee shall not carry out any addition or alteration to the shop without the previous consent and approval in writing of the lessor.', (210 - 15 - 15)));
      //   doc.text(15, 244, doc.splitTextToSize('10.   The lessee on the expiry of the lease period of'));
      //   doc.setFontType("bold");
      //   doc.text(128, 244, doc.splitTextToSize(' ' + expiryDate + ' '));
      //   doc.setFontType("normal");
      //   doc.text(156, 244, doc.splitTextToSize('months'));
      //   doc.text(15, 250, doc.splitTextToSize('shall hand over vacant possession of the ceased shop peacefully or the lease agreement can be renewed for a further period on mutually agreed terms.', (210 - 15 - 15)));
      //  doc.text(15, 266, noticeData.commissionerName?noticeData.commissionerName:"");
      //  doc.text(160, 266, 'LESSEE');
      //  doc.text(15, 274, 'Signature:   ');
      //   doc.text(160, 274, 'Signature:  ');
      //   doc.setFontType("bold");
      //   doc.text(15, 282, tenantId.split(".")[1]);
      //
      //
      //
      //
      //
       //
      //
      //
      //
      //
        doc.save('Receipt-' + getVal("Receipt[0].transactionId") + '.pdf');
       //

  }

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors, isFormValid,tenantInfo} = this.props;
    let {search, handleChange, getVal, addNewCard, removeCard, rowClickHandler,getPurposeTotal,getTotal,getGrandTotal,int_to_words,print,generatePdf} = this;
    let {showResult, resultList} = this.state;
    console.log(tenantInfo);
    // console.log(formData);
    return (
      <div className="SearchResult" >


      {
        formData.hasOwnProperty("Receipt") && <div>  <Card className="uiCard" id="receipt">
              <CardHeader title={""}/>
              <CardText >
              <Grid>

                    <Row><Col style={{textAlign:"center"}} xs={12} md={12}><h3><strong> {translate("Receipt")} </strong></h3></Col> </Row>
                    <Row><Col style={{textAlign:"center"}} xs={12} md={12}><h4><strong> {translate(tenantInfo[0].city.name)} </strong></h4></Col> </Row>

                    <br/>

                    <Row className="show-grid">
                      <Col xs={12} md={3}><strong>Payee Name - </strong>{getVal("Receipt[0].Bill[0].payeeName")} </Col>
                      <Col xs={12} md={3}><strong>Receipt Date - </strong>{getVal("Receipt[0].instrument") && getVal("Receipt[0].instrument.transactionDate").split("-")[2]+"-"+getVal("Receipt[0].instrument.transactionDate").split("-")[1]+"-"+getVal("Receipt[0].instrument.transactionDate").split("-")[0]} </Col>
                      <Col xs={12} md={3}><strong>Address - </strong>{getVal("Receipt[0].Bill[0].payeeAddress")} </Col>
                      <Col xs={12} md={3}><strong>Transaction Id - </strong>{getVal("Receipt[0].transactionId")} </Col>

                    </Row>
                    <br/>


                    <Row >
                    <Col className="text-center" xs={12} md={12}>
                    {showResult && <Table bordered condensed responsive className="table-striped">
                    <thead>
                      <tr>
                        <th>{translate("collection.create.serviceType")}</th>
                        <th>{translate("collection.create.receiptNumber")}</th>
                        <th>{translate("collection.create.consumerCode")}</th>
                        <th>{translate("collection.search.period")}</th>
                        {getGrandTotal("ARREAR_AMOUNT",formData.Receipt[0].Bill[0].billDetails)>0 && <th>{translate("collection.search.arrears")}</th>}
                        {getGrandTotal("CURRENT_AMOUNT",formData.Receipt[0].Bill[0].billDetails)>0 &&<th>{translate("collection.search.current")}</th>}
                        {getGrandTotal("OTHERS",formData.Receipt[0].Bill[0].billDetails)>0 &&<th>{translate("collection.search.interest")}</th>}
                        {getGrandTotal("REBATE",formData.Receipt[0].Bill[0].billDetails)>0 &&<th>{translate("collection.search.rebate")}</th>}
                        {getGrandTotal("ADVANCE_AMOUNT",formData.Receipt[0].Bill[0].billDetails)>0 &&<th>{translate("collection.create.advance")}</th>}
                        {getGrandTotal("ARREAR_LATEPAYMENT_CHARGES",formData.Receipt[0].Bill[0].billDetails)>0 &&<th>{translate("collection.create.arrearLatePayment")}</th>}
                        {getGrandTotal("CURRENT_LATEPAYMENT_CHARGES",formData.Receipt[0].Bill[0].billDetails)>0 &&<th>{translate("collection.create.currentLatePayment")}</th>}
                        {getGrandTotal("CHEQUE_BOUNCE_PENALTY",formData.Receipt[0].Bill[0].billDetails)>0 &&<th>{translate("collection.create.checkLatePayment")}</th>}
                        <th>{translate("collection.create.total")}</th>



                        {/*resultList.resultHeader && resultList.resultHeader.length && resultList.resultHeader.map((item, i) => {
                          return (
                            <th  key={i}>{translate(item.label)}</th>
                          )
                        })*/}

                      </tr>
                    </thead>
                    <tbody>
                          {formData.Receipt[0].Bill[0].billDetails.map((item,index)=>{
                              return (
                                <tr key={index}>
                                    <td>{item.businessService} </td>
                                    <td>{item.receiptNumber} </td>
                                    <td>{item.consumerCode} </td>
                                    <td>{item.period} </td>
                                    {getGrandTotal("ARREAR_AMOUNT",formData.Receipt[0].Bill[0].billDetails)>0 &&<td>{getPurposeTotal("ARREAR_AMOUNT",item.billAccountDetails)}</td>}
                                    {getGrandTotal("CURRENT_AMOUNT",formData.Receipt[0].Bill[0].billDetails)>0 &&<td>{getPurposeTotal("CURRENT_AMOUNT",item.billAccountDetails)}</td>}
                                    {getGrandTotal("OTHERS",formData.Receipt[0].Bill[0].billDetails)>0 &&<td>{getPurposeTotal("OTHERS",item.billAccountDetails)}</td>}
                                    {getGrandTotal("REBATE",formData.Receipt[0].Bill[0].billDetails)>0 &&<td>{getPurposeTotal("REBATE",item.billAccountDetails)}</td>}
                                    {getGrandTotal("ADVANCE_AMOUNT",formData.Receipt[0].Bill[0].billDetails)>0 &&<td>{getPurposeTotal("ADVANCE_AMOUNT",item.billAccountDetails)}</td>}
                                    {getGrandTotal("ARREAR_LATEPAYMENT_CHARGES",formData.Receipt[0].Bill[0].billDetails)>0 &&<td>{getPurposeTotal("ARREAR_LATEPAYMENT_CHARGES",item.billAccountDetails)}</td>}
                                    {getGrandTotal("CURRENT_LATEPAYMENT_CHARGES",formData.Receipt[0].Bill[0].billDetails)>0 &&<td>{getPurposeTotal("CURRENT_LATEPAYMENT_CHARGES",item.billAccountDetails)}</td>}
                                    {getGrandTotal("CHEQUE_BOUNCE_PENALTY",formData.Receipt[0].Bill[0].billDetails)>0 &&<td>{getPurposeTotal("CHEQUE_BOUNCE_PENALTY",item.billAccountDetails)}</td>}
                                    <td>{getTotal(item.billAccountDetails)}</td>


                                </tr>
                              )
                          })}
                          <tr>
                              <td colSpan={5}></td>
                              <td><strong>{getGrandTotal("",formData.Receipt[0].Bill[0].billDetails)}</strong></td>
                          </tr>
                          <tr>
                              <td colSpan={3}>Amount in words</td>
                              <td colSpan={3}><strong>{int_to_words(getGrandTotal("",formData.Receipt[0].Bill[0].billDetails)).toUpperCase()+" ONLY"}</strong></td>
                          </tr>

                          {formData.Receipt[0].instrument && formData.Receipt[0].instrument.instrumentType.name!="Cash" && <tr>
                              <td colSpan={6}>Cheque/DD No <strong>{formData.Receipt[0].instrument.transactionNumber}</strong> drawn on <strong>{formData.Receipt[0].instrument.bank.name}</strong>, <strong>{formData.Receipt[0].instrument.branchName}</strong> Dated <strong>{formData.Receipt[0].instrument.transactionDate}</strong><br/>
                                Cheque/DD payments are subject to realisation</td>
                          </tr>}
                          {/*resultList.hasOwnProperty("resultValues") && resultList.resultValues.map((item, i) => {
                            return (
                              <tr key={i} onClick={() => {rowClickHandler(i)}}>
                                {
                                  item.map((item2, i2)=>{
                                    return (
                                      <td  key={i2}>{item2?item2:""}</td>
                                    )
                                })}
                              </tr>
                              )

                          })*/}


                    </tbody>
                  </Table>}
                  </Col>
                    </Row>



                    </Grid>




               </CardText>
          </Card>
          <Grid>
            <Row>
                <Col className="text-center" xs={12} md={12} ><IconButton onClick={e=>print()}><i className="material-icons">print</i></IconButton><IconButton onClick={e=>generatePdf()}><i className="material-icons">receipt</i></IconButton></Col>
            </Row>
          </Grid>
          </div>
      }



      </div>
    );
  }
}

const mapStateToProps = state => ({
  metaData:state.framework.metaData,
  mockData: state.framework.mockData,
  moduleName:state.framework.moduleName,
  actionName:state.framework.actionName,
  formData:state.frameworkForm.form,
  fieldErrors: state.frameworkForm.fieldErrors,
  flag: state.report.flag,
  isFormValid: state.frameworkForm.isFormValid,
  tenantInfo: state.common.tenantInfo
});

const mapDispatchToProps = dispatch => ({
  initForm: (requiredFields) => {
    dispatch({
      type: "SET_REQUIRED_FIELDS",
      requiredFields
    });
  },
  setMetaData: (metaData) => {
    dispatch({type:"SET_META_DATA", metaData})
  },
  setMockData: (mockData) => {
    dispatch({type: "SET_MOCK_DATA", mockData});
  },
  setModuleName: (moduleName) => {
    dispatch({type:"SET_MODULE_NAME", moduleName})
  },
  setActionName: (actionName) => {
    dispatch({type:"SET_ACTION_NAME", actionName})
  },
  handleChange: (e, property, isRequired, pattern, requiredErrMsg, patternErrMsg)=>{
    dispatch({type:"HANDLE_CHANGE_FRAMEWORK", property,value: e.target.value, isRequired, pattern, requiredErrMsg, patternErrMsg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  },
  setRoute: (route) => dispatch({type: "SET_ROUTE", route}),
  setFlag: (flag) => {
    dispatch({type:"SET_FLAG", flag})
  },
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Report);


// <form onSubmit={(e) => {
//   search(e)
// }}>
// {!_.isEmpty(mockData) && moduleName && actionName && <ShowFields groups={mockData[`${moduleName}.${actionName}`].groups} noCols={mockData[`${moduleName}.${actionName}`].numCols} ui="google" handler={handleChange} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false} addNewCard={""} removeCard={""}/>}
//   <div style={{"textAlign": "center"}}>
//     <br/>
//     <UiButton item={{"label": "Search", "uiType":"submit", "isDisabled": isFormValid ? false : true}} ui="google"/>
//     <br/>
//     {showResult && <UiTable resultList={resultList} rowClickHandler={rowClickHandler}/>}
//   </div>
// </form>
