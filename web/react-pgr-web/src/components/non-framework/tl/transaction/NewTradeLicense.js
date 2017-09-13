import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';
import {Grid, Row, Col, Table, DropdownButton} from 'react-bootstrap';
import {Card, CardHeader, CardTitle, CardText} from 'material-ui/Card';
import Checkbox from 'material-ui/Checkbox';
import TextField from 'material-ui/TextField';
import SelectField from 'material-ui/SelectField';
import AutoComplete from 'material-ui/AutoComplete';
import Dialog from 'material-ui/Dialog';
import MenuItem from 'material-ui/MenuItem';
import FlatButton from 'material-ui/FlatButton';
import _ from "lodash";
import {translate, validate_fileupload, dateToEpoch} from '../../../common/common';
import Api from '../../../../api/api';
import styles from '../../../../styles/material-ui';
const constants = require('../../../common/constants');

const patterns = {
  date:/^(((0[1-9]|[12]\d|3[01])\/(0[13578]|1[02])\/((19|[2-9]\d)\d{2}))|((0[1-9]|[12]\d|30)\/(0[13456789]|1[012])\/((19|[2-9]\d)\d{2}))|((0[1-9]|1\d|2[0-8])\/02\/((19|[2-9]\d)\d{2}))|(29\/02\/((1[6-9]|[2-9]\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))))$/g,
  ownerName:/^.[a-zA-Z. ]{2,99}$/,
  address:/^[a-zA-Z0-9:@&*_+#()/,. -]*$/,
  assessmentNumber : /^[a-z0-9\/\-]+$/,
  tradeTitle : /^[a-zA-Z0-9@:()/#,. -]*$/,
  remarks:/^[a-zA-Z0-9:@&*_+#()/,. -]*$/,
  agreementNo : /^[a-zA-Z0-9&/()-]*$/
}

//titles
const labels={
  applyNewTradeLicense : 'Apply New Trade License',
  tradeOwnerDetails : 'tl.create.licenses.groups.TradeOwnerDetails',
  tradeLocationDetails : 'tl.create.licenses.groups.TradeLocationDetails',
  tradeDetails : 'tl.create.licenses.groups.TradeDetails',
  agreementDetailsSection : 'tl.create.licenses.groups.agreementDetails',
  supportingDocuments : 'Supporting Documents'
}

const tradeOwnerDetailsCardFields = [
  {label : "tl.create.licenses.groups.TradeOwnerDetails.AadharNumber", type:"text", code:"adhaarNumber", isMandatory:false, maxLength:12, pattern:/^\d{12}$/g, errorMsg:"Enter Valid Aadhar Number (12 Digit Number)"},
  {label : "tl.create.licenses.groups.TradeOwnerDetails.Mobile Number", type:"text", code:"mobileNumber", isMandatory:true, maxLength:10, pattern:/^\d{10}$/g, errorMsg:"Enter Valid Mobile Number (10 Digit Number)"},
  {label : "tl.create.licenses.groups.TradeOwnerDetails.TradeOwnerName", type:"text", code:"ownerName", isMandatory:true, maxLength:100, pattern:patterns.ownerName, errorMsg:"Enter Valid Trade Owner Name(Min:3, Max:100)"},
  {label : "tl.create.licenses.groups.TradeOwnerDetails.FatherSpouseName", type:"text", code:"fatherSpouseName", isMandatory:true, maxLength:100, pattern:patterns.ownerName, errorMsg:"Enter Valid Father/Spouse Name(Min:3, Max:100)"},
  {label : "tl.create.licenses.TradeOwnerDetails.groups.EmailID", code:"emailId", type:"text", isMandatory:true, maxLength:50, pattern:/^(?=.{6,64}$)(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/, errorMsg:"Enter Valid Email ID (Max:50)"},
  {label : "tl.create.licenses.groups.TradeOwnerDetails.TradeOwnerAddress", type:"textarea", code:"ownerAddress", isMandatory:true, maxLength:250, pattern:patterns.address, errorMsg:"Enter Valid Trade Owner Address (Max:250)"}
]

const tradeLocationDetails = [
  {label : "tl.create.licenses.groups.TradeLocationDetails.PropertyAssessmentNo", type:"text", code:"propertyAssesmentNo", isMandatory:false, maxLength:20, pattern:patterns.assessmentNumber, errorMsg:"Enter Valid Property Assessment Number (Max:20)"},
  {label : "tl.create.licenses.groups.TradeLocationDetails.Locality", type:"dropdown", code:"localityId", codeName:"localityName", isMandatory:true, maxLength:50, pattern:""},
  {label : "tl.create.licenses.groups.TradeLocationDetails.adminWardId", type:"dropdown", code:"adminWardId", codeName:"adminWardName", isMandatory:true, maxLength:50, pattern:""},
  {label : "tl.create.licenses.groups.TradeLocationDetails.revenueWardId", type:"dropdown", code:"revenueWardId", codeName:"revenueWardName", isMandatory:true, maxLength:100, pattern:""},
  {label : "tl.create.licenses.groups.TradeLocationDetails.OwnershipType", code:"ownerShipType", type:"dropdown", isMandatory:true, maxLength:50, pattern:""},
  {label : "tl.create.licenses.groups.TradeLocationDetails.TradeAddress", type:"textarea", code:"tradeAddress", isMandatory:true, maxLength:250, pattern:patterns.address, errorMsg:"Enter Valid Trade Address (Max:250)"}
]

const tradeDetails = [
  {label : "tl.create.licenses.groups.TradeDetails.TradeTitle", type:"text", code:"tradeTitle", isMandatory:true, maxLength:100, pattern:patterns.tradeTitle, errorMsg:"Enter Valid Trade Title (Max: 250)"},
  {label : "tl.create.licenses.groups.TradeDetails.TradeType", type:"dropdown", code:"tradeType", isMandatory:true, maxLength:50, pattern:""},
  {label : "tl.create.licenses.groups.TradeDetails.TradeCategory", type:"dropdown", code:"categoryId", codeName:'category', isMandatory:true, maxLength:50, pattern:""},
  {label : "tl.create.licenses.groups.TradeDetails.TradeSubCategory", type:"dropdown", code:"subCategoryId", codeName:"subCategory", isMandatory:true, maxLength:100, pattern:""},
  {label : "tl.create.licenses.groups.TradeDetails.UOM", code:"uomId", codeName:"uom", type:"text", isMandatory:true, maxLength:50, pattern:"", isDisabled:true},
  {label : "tl.create.licenses.groups.TradeDetails.tradeValueForUOM", type:"text", code:"quantity", isMandatory:true, maxLength:50, pattern:/^[+-]?\d+(\.\d{2})?$/, errorMsg:"Enter Valid Trade Value for the UOM (Upto two decimal points)"},
  {label : "tl.create.licenses.groups.validity", type:"text", code:"validityYears", isMandatory:true, maxLength:50, pattern:"", isDisabled:true},
  {label : "tl.create.licenses.groups.TradeDetails.Remarks", type:"textarea", code:"remarks", isMandatory:false, maxLength:1000, pattern:patterns.remarks, errorMsg:"Please avoid sepcial characters except :@&*_+#()/,.-"},
  {label : "tl.create.licenses.groups.TradeDetails.TradeCommencementDate", type:"date", code:"tradeCommencementDate", isMandatory:true, maxLength:1000, pattern:patterns.date, errorMsg:"Enter in dd/mm/yyyy Format"},
  {label : "tl.create.licenses.groups.TradeDetails.TraderOwnerProperty", type:"checkbox", code:"isPropertyOwner", isMandatory:false}
]

const agreementDetailsSection = [
  {label : "tl.create.licenses.groups.agreementDetails.agreementDate", type:"date", code:"agreementDate", isMandatory:true, maxLength:10, pattern:patterns.date,  errorMsg:"Enter in dd/mm/yyyy Format"},
  {label : "tl.create.licenses.groups.agreementDetails.agreementNo", type:"text", code:"agreementNo", isMandatory:true, maxLength:30, pattern:patterns.agreementNo, errorMsg:"Enter Valid Agreement No (Max:30, Alpha/Numeric)"}
]

const customStyles={
  cardTitle:{
    padding: '16px 16px 0'
  },
  th:{
    padding:'15px 10px !important'
  },
  fileInput: {
    cursor: 'pointer',
     position: 'absolute',
     top: 0,
     bottom: 0,
     right: 0,
     left: 0,
     width: '100%',
     opacity: 0
  }
}

function compareStrings(a, b) {
  // Assuming you want case-insensitive comparison
  a = a.toLowerCase();
  b = b.toLowerCase();

  return (a < b) ? -1 : (a > b) ? 1 : 0;
}

function sortArrayByAlphabetically(arry, keyToSort){
  return arry.sort(function(a, b) {
    return compareStrings(a[keyToSort], b[keyToSort]);
  });
}



class NewTradeLicense extends Component {

  constructor(){
    super();
    this.customHandleChange = this.customHandleChange.bind(this);
    this.handleDocumentsClearCancel = this.handleDocumentsClearCancel.bind(this);
    this.handleDocumentsClearConfirm = this.handleDocumentsClearConfirm.bind(this);

    this.state={
      isPropertyOwner:true,
      open: false,
      openDocClearDialog:false,
      documentTypes:[], //supporting documents
      autocompleteDataSource:{
        localityId:[], //assigning datasource by field code
        localityIdConfig: { //autocomplete config
         text: 'name',
         value: 'id'
        }
      },
      confirmCategoryField:{}, //category or subcategory field confirm temporary store
      confirmCategoryFieldValue:"",
      supportDocClearDialogMsg : "Are you sure want to change the category? If you change support documents will be cleared",
      dropdownDataSource:{
        adminWardId:[],
        adminWardIdConfig: {
          text: 'name',
          value: 'id'
        },
        revenueWardId:[],
        revenueWardIdConfig: {
          text: 'name',
          value: 'id'
        },
        categoryId:[],
        categoryIdConfig: {
          text: 'name',
          value: 'id'
        },
        subCategoryId:[],
        subCategoryIdConfig:{
          text: 'name',
          value: 'id'
        },
        tradeType:[{
          id:"PERMANENT",
          name:"PERMANENT"
        },
        {
          id:"TEMPORARY",
          name:"TEMPORARY"
        }],
        tradeTypeConfig: {
          text: 'name',
          value: 'id'
        },
        ownerShipType:[{
          id:"STATE_GOVERNMENT",
          name:"STATE GOVERNMENT"
        },
        {
          id:"OWNED",
          name:"OWNED"
        },
        {
          id:"RENTED",
          name:"RENTED"
        },
        {
          id:"CENTRAL_GOVERNMENT",
          name:"CENTRAL GOVERNMENT"
        },
        {
          id:"ULB",
          name:"ULB"
        }],
        ownerShipTypeConfig: {
          text: 'name',
          value: 'id'
        },
        localityId:[],
        localityIdConfig:{
          text: 'name',
          value: 'id'
        }
      }
    }
  }

  handleOpen = () => {
    this.setState({open: true});
  };

  componentDidMount(){
    let requiredFields = [];

    tradeOwnerDetailsCardFields.filter(function(obj){
      obj.isMandatory ? requiredFields.push(obj.code) : '';
    });

    tradeLocationDetails.filter(function(obj){
      obj.isMandatory ? requiredFields.push(obj.code) : '';
    });

    tradeDetails.filter(function(obj){
      obj.isMandatory ? requiredFields.push(obj.code) : '';
    });

    this.props.initForm(requiredFields);

    var tenantId = localStorage.getItem("tenantId") || "default";

    this.props.setLoadingStatus('loading');

    Promise.all([
      Api.commonApiPost("/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName",{boundaryTypeName:"WARD", hierarchyTypeName:"REVENUE"},{tenantId:tenantId}),
      Api.commonApiPost("/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName",{boundaryTypeName:"Ward", hierarchyTypeName:"ADMINISTRATION"},{tenantId:tenantId}),
      Api.commonApiPost("/tl-masters/category/v1/_search",{type:"category"},{tenantId:tenantId, pageSize:"500"}, false, true),
      Api.commonApiPost("/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName",{boundaryTypeName:"LOCALITY", hierarchyTypeName:"LOCATION"},{tenantId:tenantId})
      // Api.commonApiPost("/tl-masters/documenttype/v2/_search",{applicationType:"NEW",enabled : true},{tenantId:tenantId})
    ])
    .then((responses)=>{
      //if any error occurs
      // if(!responses || responses.length ===0 || !responses[0] || !responses[1]){
      //   return;
      // }
      this.props.setLoadingStatus('hide');
      try{
        let revenueWardId = responses[0].Boundary.sort(function(a, b) {
            return parseFloat(a.name) - parseFloat(b.price);
        });
        let adminWardId = sortArrayByAlphabetically(responses[1].Boundary, "name");
        let categoryId = sortArrayByAlphabetically(responses[2].categories, "name");
        let localityId = sortArrayByAlphabetically(responses[3].Boundary, "name");
        let dropdownDataSource = {...this.state.dropdownDataSource};
        dropdownDataSource = {...dropdownDataSource, revenueWardId, adminWardId, categoryId, localityId};
        this.setState({dropdownDataSource});
      }
      catch(e){
        console.log('error', e);
      }

    });

  }

  fileSectionChange = (comments, doc) => {
    this.props.handleChange(comments, doc.id, doc.mandatory, "")
  }

  handleDocumentsClearConfirm(){
    //console.log('confirm clicked', field);
    if(this.state.confirmCategoryField.code === 'categoryId'){
      this.tradeCategoryChangeAndResetFields(this.state.confirmCategoryField, this.state.confirmCategoryFieldValue);
    }
    else if(this.state.confirmCategoryField.code === 'subCategoryId'){
      this.tradeSubCategoryChangeAndResetFields(this.state.confirmCategoryField, this.state.confirmCategoryFieldValue);
    }
  }

  handleDocumentsClearCancel(){
    this.setState({openDocClearDialog:false});
  }

  clearSupportDocuments(){
    var supportDocuments= this.state.documentTypes ? [...this.state.documentTypes] : [];
    supportDocuments.map((doc)=>{
      //doc.id
      if(doc.mandatory){
        this.props.REMOVE_MANDATORY_LATEST('',doc.id,doc.mandatory,"", "");
      }

      //remove file
      var supportDocument= this.props.files ? [...this.props.files].find((file)=> file.code === doc.id) : [];

      if(supportDocument){
        if(supportDocument.files && supportDocument.files.length > 0)
          this.props.removeFile({isRequired:doc.mandatory, code:doc.id, name:supportDocument.files[0].name});
      }
    });
    this.setState({documentTypes : []});
  }

  tradeCategoryChangeAndResetFields = (field, value) =>{
    var tenantId = this.getTenantId();
    var _this=this;

    var values=value.split("~");
    var id = value.indexOf("~") > -1 ? values[0] : value;
    this.props.handleChange(id, field.code, field.isMandatory, "", "");
    if(values.length > 1){
      if(field.hasOwnProperty("codeName")){
        this.props.handleChange(values[1], field.codeName, false, "", "");
      }
    }

    const dropdownDataSource = {...this.state.dropdownDataSource, subCategoryId:[]}
    this.setState({dropdownDataSource, openDocClearDialog:false});
    this.props.handleChange("", "subCategoryId", field.isMandatory, "", "");
    this.props.handleChange("", "validityYears", field.isMandatory, "", "");
    this.props.handleChange("", "uomId", field.isMandatory, "", "");
    this.clearSupportDocuments();
    Api.commonApiPost("tl-masters/category/v1/_search",{type:"subcategory", categoryId:id},{tenantId:tenantId}, false, true).then(function(response){
      const dropdownDataSource = {..._this.state.dropdownDataSource, subCategoryId:sortArrayByAlphabetically(response.categories, "name")};
      _this.setState({dropdownDataSource});
    }, function(err) {
        console.log(err);
    });
  }

  tradeSubCategoryChangeAndResetFields = (field, value) =>{
    var tenantId = this.getTenantId();
    var _this=this;

    var values=value.split("~");
    var id = value.indexOf("~") > -1 ? values[0] : value;
    this.props.handleChange(id, field.code, field.isMandatory, "", "");
    if(values.length > 1){
      if(field.hasOwnProperty("codeName")){
        this.props.handleChange(values[1], field.codeName, false, "", "");
      }
    }

    this.setState({openDocClearDialog:false});
    // /tl-masters/category/v1/_search
    this.props.handleChange("", "validityYears", field.isMandatory, "", "");
    this.props.handleChange("", "uomId", field.isMandatory, "", "");
    this.clearSupportDocuments();

    Api.commonApiPost("tl-masters/category/v1/_search",{type:"subcategory", ids:id},{tenantId:tenantId}, false, true).then(function(response){
      var category=response.categories[0];
      _this.props.handleChange(category.validityYears, "validityYears", field.isMandatory, "", "");
      _this.props.handleChange(category.details[0].uomId, "uomId", field.isMandatory, "", "");
      _this.props.handleChange(category.details[0].uomName, "uom", false, "", "");
      _this.getDocuments();
    }, function(err) {
        console.log(err);
    });

  }

  getTenantId = ()=>{
    return localStorage.getItem("tenantId") || "default";
  }

  customHandleChange = (value, field) => {
    var tenantId = this.getTenantId();
    var _this=this;

    if(field.type === "dropdown"){

      if(field.code === "categoryId" || field.code === "subCategoryId"){

        // this.state.documentTypes.length > 0 && this.state.documentTypes.map((obj) => {
        //   obj.mandatory ? _this.props.REMOVE_MANDATORY_LATEST('', obj.id, obj.mandatory, "", "") : '';
        // });

        var files = this.props.files ? this.props.files.filter((field)=> field.files.length > 0) : undefined;
        if(files && files.length > 0){
          this.setState({confirmCategoryField:field, confirmCategoryFieldValue:value, openDocClearDialog:true});
        }
        else{
          if(field.code==='categoryId')
            this.tradeCategoryChangeAndResetFields(field, value);
          else
            this.tradeSubCategoryChangeAndResetFields(field, value);
        }
      }
      else{

        var values=value.split("~");
        var id = value.indexOf("~") > -1 ? values[0] : value;
        this.props.handleChange(id, field.code, field.isMandatory, "", "");
        if(values.length > 1){
          if(field.hasOwnProperty("codeName")){
            this.props.handleChange(values[1], field.codeName, false, "", "");
          }
        }

      }

    }
    else{

      //date field slash append functionality
      if(field.type == 'date'){
        var oldValue = this.props.form[field.code] || "";
        if((value.length === 2 || value.length === 5) && value.length > oldValue.length)
          value=value+"/";
      }

      this.props.handleChange(value, field.code, field.isMandatory || false, field.pattern || "", field.errorMsg || "");
      if(field.code === 'isPropertyOwner'){
        this.setState({isPropertyOwner:!value});
        var agreementdate = agreementDetailsSection.find(agreement => agreement.code == 'agreementDate');
        var agreementno = agreementDetailsSection.find(agreement => agreement.code == 'agreementNo');
        if(value){
          _this.props.ADD_MANDATORY_LATEST('','agreementDate',agreementdate.isMandatory,agreementdate.pattern,agreementdate.errorMsg);
          _this.props.ADD_MANDATORY_LATEST('','agreementNo',agreementno.isMandatory,agreementno.pattern);
        }else{
          _this.props.REMOVE_MANDATORY_LATEST('','agreementDate',agreementdate.isMandatory,agreementdate.pattern,agreementdate.errorMsg);
          _this.props.REMOVE_MANDATORY_LATEST('','agreementNo',agreementno.isMandatory,agreementno.pattern);
        }
      }
    }

  }

  getDocuments = () => {
    var _this = this;
    let {form} = this.props;
    Api.commonApiPost("tl-masters/documenttype/v2/_search",{applicationType:"NEW", enabled:true, categoryId:form.categoryId, subCategoryId : form.subCategoryId},{}, false, true).then(function(response){
      _this.setState({documentTypes : sortArrayByAlphabetically(response.documentTypes,"name")},
        _this.addMandatoryDocuments(response.documentTypes)
      );
    }, function(err) {
        console.log(err);
    });
  }

  addMandatoryDocuments = (docTypes) => {
    var _this = this;
    docTypes.filter(function(obj){
      obj.mandatory ? _this.props.ADD_MANDATORY_LATEST('', obj.id, obj.mandatory, "", "") : '';
    })
  }

  customAutoCompleteKeyUpEvent = (e, field) =>{

    var _this=this;
    //reset autocomplete value
    this.props.handleChange("", field.code, field.isMandatory, field.pattern, field.errorMsg || "");

    if(e.target.value && field.code === 'localityId'){
      Api.commonApiGet("/egov-location/boundarys/getLocationByLocationName", {locationName : e.target.value}).then(function(response)
      {
        const autocompleteDataSource  = _this.state.autocompleteDataSource;
        autocompleteDataSource[field.code]=response;
        _this.setState({autocompleteDataSource});
      },function(err) {
        _this.handleError(err.message);
      });
    }

  }

  submit = (e) => {
    var _this=this;
    let {form, files, setLoadingStatus} = this.props;
    setLoadingStatus('loading');

    var licenseObj = {}, licenseArray = [];
    licenseObj = {...form};

    //adding optional fields value as undefined
    licenseObj['adhaarNumber'] = licenseObj['adhaarNumber'] || null;
    licenseObj['propertyAssesmentNo'] = licenseObj['propertyAssesmentNo'] || null;

    licenseObj['tenantId'] = localStorage.getItem('tenantId');
    licenseObj['applicationType'] = 'NEW';
    licenseObj['tradeCommencementDate'] = dateToEpoch(licenseObj.tradeCommencementDate);
    licenseObj['licenseValidFromDate'] = licenseObj.tradeCommencementDate;
    //isnotpropertyowner
    licenseObj['isPropertyOwner'] = licenseObj['isPropertyOwner'] ? licenseObj['isPropertyOwner'] : false;
    licenseObj['agreementDate'] = licenseObj.agreementDate ? dateToEpoch(licenseObj.agreementDate) : '';
    licenseObj['agreementNo'] = licenseObj.agreementNo ? licenseObj.agreementNo : '';
    licenseObj['isLegacy'] = false;
    licenseObj['active'] = true;
    licenseObj['application'] = {};
    licenseObj['application']['tenantId'] = localStorage.getItem('tenantId');
    licenseObj['application']['applicationType'] = 'NEW';
    licenseObj['application']['status'] = 4;
    licenseObj['application']['applicationDate'] = '';
    licenseObj['application']['licenseId'] = 0;
    licenseObj['application']['licenseFee'] = 0;
    licenseObj['application']['fieldInspectionReport'] = '';
    licenseObj['application']['statusName'] = 'Acknowledged';
    licenseObj['application']['workFlowDetails'] = {};
    licenseObj['application']['workFlowDetails']['department'] = null;
    licenseObj['application']['workFlowDetails']['designation'] = null;
    licenseObj['application']['workFlowDetails']['assignee'] = 1;
    licenseObj['application']['workFlowDetails']['action'] = 'create';
    licenseObj['application']['workFlowDetails']['status  '] = "Pending For Application processing";
    licenseObj['application']['workFlowDetails']['comments'] = '';
    licenseObj['application']['workFlowDetails']['senderName'] = '';
    licenseObj['application']['workFlowDetails']['details'] = '';
    licenseObj['application']['workFlowDetails']['stateId'] = null;
    licenseObj['supportDocuments'] = [];

    var supportDocuments = [];

    //filter which file field has files
    var supportingDocuments = files.filter((field) => field.files.length > 0);

    if(supportingDocuments && supportingDocuments.length > 0){
      supportingDocuments.map((field, index) => {
          let docs = {};
          let formData = new FormData();
          formData.append("tenantId", localStorage.getItem('tenantId'));
          formData.append("module", constants.TRADE_LICENSE_FILE_TAG);
          field.files.map((file)=>{
            formData.append("file", file);
          });
          // formData.append("file",file.files);
          Api.commonApiPost("/filestore/v1/files",{},formData).then(function(response)
          {
            // console.log('Comments:',field.code, form[field.code]);
            let doc = _this.state.documentTypes.find(doc => doc.id == field.code);
            // console.log('Docs:',doc);

            docs['documentTypeId']=field.code;
            docs['fileStoreId']=response.files[0].fileStoreId;
            docs['comments']=form[field.code];
            docs['auditDetails']=doc.auditDetails;
            docs['documentTypeName']=doc.name;
            supportDocuments.push(docs);

            if(files.length == index+1){
              //last file uploaded, create TL
              licenseObj['supportDocuments'] = supportDocuments;
              licenseArray.push(licenseObj);
              _this.createTL(licenseArray);
            }

          },function(err) {
            setLoadingStatus('hide');
            _this.handleError(err.message);
          });
      });
    }else{
      licenseArray.push(licenseObj);
      _this.createTL(licenseArray);
    }
  }

  createTL = (licenseArray) => {
    var _this = this;
    let {setLoadingStatus} = this.props;
    console.log(JSON.stringify(licenseArray));
    Api.commonApiPost("tl-services/license/v1/_create",{},{licenses:licenseArray}, false, true).then(function(response){

      _this.setState({
        licenseId : response.licenses[0].id,
        title : 'Create License Added Successfully',
        acknowledgement : response.responseInfo.status
      });
      setLoadingStatus('hide');
      _this.handleOpen();
      // _this.handleError('Added successfully');
      //response.responseInfo.status
    }, function(err) {
        setLoadingStatus('hide');
        console.log('Error Message', err.message);
        _this.handleError(err.message);
    });
  }

  handleError = (msg) => {
    let {toggleDailogAndSetText, toggleSnackbarAndSetText}=this.props;
    toggleDailogAndSetText(true, msg);
  }

  viewLicense = () => {
      let {setRoute} = this.props;
      this.setState({open: false});
      setRoute("/non-framework/tl/transaction/viewLicense/"+this.state.licenseId);
  }

  render(){

    const actions = [
      <FlatButton
        label="View License"
        primary={true}
        keyboardFocused={true}
        onClick={this.viewLicense}
      />,
    ];


    const supportDocClearActions = [
      <FlatButton
        label="Confirm"
        primary={true}
        keyboardFocused={true}
        onClick={this.handleDocumentsClearConfirm}
      />,
      <FlatButton
        label="Cancel"
        primary={true}
        keyboardFocused={true}
        onClick={this.handleDocumentsClearCancel}
      />
    ];

    var agreementCard=null;
    var brElement=null;

    let {isFormValid} = this.props;

    if(!this.state["isPropertyOwner"]){
      // console.log('coming inside');
      agreementCard=<CustomCard title={labels.agreementDetailsSection} form={this.props.form}
        fields={agreementDetailsSection}
        fieldErrors = {this.props.fieldErrors}
        handleChange={this.customHandleChange}></CustomCard>;
      brElement=<br/>;
    }

    return(
      <Grid fluid={true}>
        <h2 className="application-title">{translate(labels.applyNewTradeLicense)}</h2>
        <CustomCard title={labels.tradeOwnerDetails} form={this.props.form}
          fields={tradeOwnerDetailsCardFields}
          fieldErrors = {this.props.fieldErrors}
          handleChange={this.customHandleChange}></CustomCard>
        <br/>
        <CustomCard title={labels.tradeLocationDetails} form={this.props.form}
            fields={tradeLocationDetails}
            fieldErrors = {this.props.fieldErrors}
            autocompleteDataSource={this.state.autocompleteDataSource}
            autoCompleteKeyUp = {this.customAutoCompleteKeyUpEvent}
            dropdownDataSource={this.state.dropdownDataSource}
            handleChange={this.customHandleChange}></CustomCard>
        <br/>
        <CustomCard title={labels.tradeDetails} form={this.props.form}
            fields={tradeDetails}
            fieldErrors = {this.props.fieldErrors}
            dropdownDataSource={this.state.dropdownDataSource}
            handleChange={this.customHandleChange}></CustomCard>
        <br/>

        {agreementCard}
        {brElement}

        {this.state.documentTypes && this.state.documentTypes.length > 0 ?
        <SupportingDocuments files={this.props.files} dialogOpener={this.props.toggleDailogAndSetText}
           title={labels.supportingDocuments} docs={this.state.documentTypes}
           addFile={this.props.addFile} removeFile={this.props.removeFile}
           fileSectionChange={this.fileSectionChange}>
        </SupportingDocuments> : ''}
        <br/>
        <div style={{textAlign: 'center'}}>
          {/* <RaisedButton style={{margin:'15px 5px'}} label="Reset"/> */}
          <RaisedButton style={{margin:'15px 5px'}} disabled={!isFormValid} label="Submit" primary={true} onClick={(e)=>this.submit()}/>
        </div>

        <Dialog
          title={this.state.title}
          actions={actions}
          modal={true}
          open={this.state.open}
        >
        {this.state.acknowledgement}
        </Dialog>

        <Dialog
          actions={supportDocClearActions}
          modal={true}
          open={this.state.openDocClearDialog || false}>
            {this.state.supportDocClearDialogMsg}
        </Dialog>

     </Grid>
    )
  }

}

class CustomCard extends Component {

  constructor(){
    super()
  }

  render(){

    const renderedFields = this.props.fields.map((field, index)=>{
      if(field.type === "autocomplete")
        return <CustomField key={index} field={field} error={this.props.fieldErrors[field.code] || ""}
           autocompleteDataSource = {this.props.autocompleteDataSource[field.code] || []}
           autocompleteDataSourceConfig = {this.props.autocompleteDataSource[field.code+"Config"]}
           autocompleteKeyUp = {this.props.autoCompleteKeyUp}
           value={this.props.form[field.code] || ""} handleChange={this.props.handleChange}></CustomField>
      else if(field.type === "dropdown")
         return <CustomField key={index} field={field} error={this.props.fieldErrors[field.code] || ""}
                dropdownDataSource = {this.props.dropdownDataSource[field.code] || []}
                nameValue = {field.codeName ? this.props.form[field.codeName] || "" : ""}
                dropdownDataSourceConfig = {this.props.dropdownDataSource[field.code+"Config"]}
                value={this.props.form[field.code] || ""} handleChange={this.props.handleChange}></CustomField>
      else
         return <CustomField key={index} field={field} error={this.props.fieldErrors[field.code] || ""}
                value={this.props.form[field.code] || ""} handleChange={this.props.handleChange}></CustomField>

    });

    return(
      <Card>
        <CardTitle style={customStyles.cardTitle} title={translate(this.props.title)}></CardTitle>
        <CardText>
          <Grid fluid={true}>
            <Row>
              {renderedFields}
            </Row>
          </Grid>
        </CardText>
      </Card>
    )
  }
}

class CustomField extends Component {

  constructor(){
    super()
  }

  shouldComponentUpdate(nextProps, nextState){
    //console.log('customField should update', !(_.isEqual(this.props, nextProps) && _.isEqual(this.state, nextState)));
    return !(_.isEqual(this.props, nextProps) && _.isEqual(this.state, nextState));
  }

  renderCustomField = ({field, handleChange, error, value, nameValue, autocompleteDataSource, autocompleteKeyUp, autocompleteDataSourceConfig, dropdownDataSourceConfig, dropdownDataSource})=>{

    switch(field.type)
    {
      case "text":
        return(
          <Col xs={12} sm={4} md={4} lg={4}>
            <TextField floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
              floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")}
              fullWidth={true}
              maxLength={field.maxLength}
              value={value || ""}
              errorText={field.isDisabled ? "" : error ||  ""}
              disabled={field.isDisabled || false}
              onChange={(event, value) => handleChange(value, field)} />
          </Col>
        )
       case "textarea":
        return(
            <Col xs={12} sm={4} md={4} lg={4}>
              <TextField fullWidth={true}
                floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")} multiLine={true}
                value={value || ""}
                disabled={field.isDisabled || false}
                errorText={error ||  ""}
                maxLength={field.maxLength}
                onChange={(event, value) => handleChange(value, field)}/>
           </Col>
        )
        case "date":
         return(
             <Col xs={12} sm={4} md={4} lg={4}>
               <TextField fullWidth={true}
                 fullWidth={true}
                 hintText="DD/MM/YYYY"
                 disabled={field.isDisabled || false}
                 floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
                 floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")} multiLine={true}
                 value={value || ""}
                 errorText={error ||  ""}
                 maxLength={field.maxLength}
                 onChange={(event, value) => handleChange(value, field)}/>
            </Col>
         )
        case "autocomplete":
         return(
           <Col xs={12} sm={4} md={4} lg={4}>
             <AutoComplete
              fullWidth={true}
              disabled={field.isDisabled || false}
              floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
              dataSourceConfig={autocompleteDataSourceConfig}
              dataSource={autocompleteDataSource}
              onKeyUp={(e)=> autocompleteKeyUp(e, field)}
              floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")}/>
           </Col>
         )
        case "dropdown":
         value = value + (nameValue ? "~" + nameValue : "");
         //console.log('value for ', field.code, value);
         return(
           <Col xs={12} sm={4} md={4} lg={4}>
             <SelectField
               fullWidth={true}
               disabled={field.isDisabled || false}
               floatingLabelStyle={styles.floatingLabelStyle} floatingLabelFixed={true}
               floatingLabelText={translate(field.label) + (field.isMandatory ? " *":"")}
               value={value || ""}
               onChange={(event, key, value) => {
                 handleChange(value, field)
               }}>
               {dropdownDataSource && dropdownDataSource.map((item, index) =>{
                 if(field.codeName){
                   return (<MenuItem value={`${item[dropdownDataSourceConfig.value]}~${translate(item[dropdownDataSourceConfig.text])}`} key={index} primaryText={translate(item[dropdownDataSourceConfig.text])} />)
                 }
                 else{
                   return (<MenuItem value={item[dropdownDataSourceConfig.value]} key={index} primaryText={translate(item[dropdownDataSourceConfig.text])} />)
                 }

               })
              }
            </SelectField>
           </Col>
         )
         case "checkbox":
          return(
            <Col xs={12} sm={4} md={4} lg={4}>
              <Checkbox label={translate(field.label) + (field.isMandatory ? " *":"")}
                checked={value || false}
                onCheck={(e, isChecked)=>{
                handleChange(isChecked? true : false, field);
              }} />
            </Col>
          )
    }

    return null;

  }

  render(){
    return this.renderCustomField(this.props);
  }

}




class SupportingDocuments extends Component {

  constructor(){
    super()
    this.fileInputOnChange = this.fileInputOnChange.bind(this);
  }

  fileInputOnChange(e, doc){
    e.preventDefault();
    var files;
    if (e.dataTransfer) {
      files = e.dataTransfer.files;
    } else if (e.target) {
      files = e.target.files;
    }

    // console.log(e.target.files);
    // console.log(doc);

    if(!files)
       return;

    //validate file input
    let validationResult = validate_fileupload(files, constants.TRADE_LICENSE_FILE_FORMATS_ALLOWED);

    // console.log('validationResult', validationResult);

    if(typeof validationResult === "string" || !validationResult){
        if(this.props.dialogOpener)
          this.props.dialogOpener(true, validationResult);
        return;
    }
    // this.populateFilePreviews([...files]);
    var existingFile = this.props.files ? this.props.files.find((file) => file.code == doc.id) : undefined;
    if(existingFile && existingFile.files && existingFile.files.length > 0){
      this.props.removeFile({isRequired:doc.mandatory, code:doc.id, name:existingFile.files[0].name});
    }
    this.props.addFile({isRequired:doc.mandatory, code:doc.id, files:[...files]});
  }

  render(){

    const props=this.props;
    // console.log('files', this.props.docs);

    return(
      <Card>
        <CardTitle style={customStyles.cardTitle} title={translate(props.title)}></CardTitle>
        <CardText>
          <Grid fluid={true}>
            <Row>
              <Col xs={12} lg={12} sm={12} md={12}>
                <Table style={{width:'100%'}} responsive>
                 <thead>
                   <tr>
                     <th style={customStyles.th}>#</th>
                     <th style={customStyles.th}>Document Name</th>
                     <th style={customStyles.th}>Attach Document</th>
                     <th style={customStyles.th}>Comments</th>
                   </tr>
                 </thead>
                 <tbody>
                   {props.docs && props.docs.map((doc, index)=>{

                     if(doc.enabled){
                       var file = this.props.files && this.props.files.find((file)=>file.code === doc.id);

                       return <tr key={index}>
                         <td>{index+1}</td>
                         <td>{`${doc.name} ${doc.mandatory ? " *":""}`}</td>
                         <td>
                           <FileInput doc={doc} key={`file${index}`} file={file || null}
                             fileInputOnChange={this.fileInputOnChange} />
                         </td>
                         <td>
                           <TextField
                              hintText="Comments"
                              multiLine={true}
                              fullWidth={true}
                              onChange={(e,newValue)=>{this.props.fileSectionChange(newValue, doc)}}
                            />
                         </td>
                       </tr>
                     }

                   })}
                 </tbody>
               </Table>
              </Col>
            </Row>
          </Grid>
        </CardText>
      </Card>
    )
  }
}

const FileInput = (props)=>{

  let fileName = props.file ? (props.file.files && props.file.files.length > 0 ? props.file.files[0].name :'') : '';

  // console.log('fileName', props.file ? props.file.code : 'empty', props.file ? props.file.files[0].name : 'empty');

  return(
    <div>
      <RaisedButton
        label="Browse"
        labelPosition="before">
        <input type="file" style={customStyles.fileInput} onChange={(e)=>{
          props.fileInputOnChange(e, props.doc);
        }} />
      </RaisedButton>
      &nbsp;&nbsp;&nbsp;&nbsp;
      <label>
        {fileName}
      </label>
    </div>
  )

}


// class CustomSelectField extends Component{
//
//     constructor(){
//       super();
//       this.state={
//         attribValues:[],
//         loadingText:"",
//         isLoading:true
//       }
//     }
//
//     componentWillMount(){
//       this.renderAttributes(this.props.obj);
//       this.setState({loadingText:translate("csv.lbl.loading")});
//     }
//
//     shouldComponentUpdate(nextProps, nextState){
//       //console.log('nextState', nextState, this.state);
//       //console.log('nextProps', nextProps, this.props);
//       //console.log('ItShouldUpdate', !(_.isEqual(this.props, nextProps) && _.isEqual(this.state, nextState)));
//       return !(_.isEqual(this.props, nextProps) && _.isEqual(this.state, nextState));
//     }
//
//     renderAttributes = (obj) => {
//       var urlsPart=obj.url.split("?");
//       var queryParams={};
//       var url="";
//       let _this=this;
//       if(urlsPart.length === 2){
//         queryParams = JSON.parse('{"' + urlsPart[1].replace(/&/g, '","').replace(/=/g,'":"') + '"}',
//                    function(key, value) { return key===""?value:decodeURIComponent(value) });
//         url = urlsPart[0];
//       }
//       else
//         url=obj.url;
//       Api.commonApiPost(url, queryParams, {}).then(function(response){
//         var jsonArryKey = Object.keys(response).length > 1 ?  Object.keys(response)[1]:"";
//         if(jsonArryKey){
//           const attribValues=[];
//           response[jsonArryKey].map((item)=>{
//             attribValues.push({key:item.id, name:item.name});
//           });
//           _this.setState({attribValues, isLoading:false, loadingText:""});
//         }
//         }, function(err){
//       });
//     }
//
//     renderMenuItems =(props)=>{
//       return this.state.attribValues.map((dd, index) =>{
//          if(this.props.isMultiple)
//           return (<MenuItem value={translate(dd.key)} insetChildren={true}
//                checked={this.props.value && this.props.value.indexOf(dd.key) > -1} key={index} primaryText={translate(dd.name)} />)
//         else
//           return (<MenuItem value={translate(dd.key)} key={index} primaryText={translate(dd.name)} />)
//         });
//     }
//
//     render(){
//       const obj=this.props.obj;
//       const description=this.props.description;
//       const menuItems=this.renderMenuItems(this.props.isMultiple) || null;
//       return (
//         <Col xs={12} sm={4} md={3} lg={3}>
//           <SelectField fullWidth={true} ref={obj.code} multiple={this.props.isMultiple}
//             floatingLabelText={description} floatingLabelFixed={this.state.isLoading}
//             hintText={this.state.loadingText} value={this.props.value} onChange={(event, key, value) => {
//             this.props.handler(value, obj.code, obj.required, "")
//           }}>
//             {menuItems}
//           </SelectField>
//         </Col>
//       )
//     }
//
//   }



const mapStateToProps = state => {
  // console.log(state.form.form);
  return ({form: state.form.form, files: state.form.files, fieldErrors: state.form.fieldErrors, isFormValid: state.form.isFormValid});
}

const mapDispatchToProps = dispatch => ({
  initForm: (requiredArray) => {
    console.log(requiredArray);
    if(!requiredArray)
        requiredArray=[];

    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: requiredArray
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  ADD_MANDATORY_LATEST : (value, property, isRequired, pattern, errorMsg) => {
     dispatch({type: "ADD_MANDATORY_LATEST", property, value, isRequired, pattern});
  },
  REMOVE_MANDATORY_LATEST : (value, property, isRequired, pattern, errorMsg) => {
     dispatch({type: "REMOVE_MANDATORY_LATEST", property, value, isRequired, pattern});
  },
  toggleDailogAndSetText: (dailogState,msg) => {
    dispatch({type: "TOGGLE_DAILOG_AND_SET_TEXT", dailogState,msg});
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  handleChange: (value, property, isRequired, pattern, errorMsg) => {
    dispatch({type: "HANDLE_CHANGE", property, value, isRequired, pattern, errorMsg});
  },
  addFile: (action) => {
    dispatch({type: 'FILE_UPLOAD_BY_CODE', isRequired:action.isRequired, code: action.code, files : action.files});
  },
  removeFile: (action) => {
    dispatch({type: 'FILE_REMOVE_BY_CODE', isRequired:action.isRequired, code: action.code, name : action.name});
  },
  setRoute:(route)=>dispatch({type:'SET_ROUTE',route})
});

const VisibleNewTradeLicense = connect(
  mapStateToProps,
  mapDispatchToProps
)(NewTradeLicense)

export default VisibleNewTradeLicense;
