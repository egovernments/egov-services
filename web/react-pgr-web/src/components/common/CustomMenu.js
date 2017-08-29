import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import Menu from 'material-ui/Menu';
import MenuItem from 'material-ui/MenuItem';
import DropDownMenu from 'material-ui/DropDownMenu';
import {connect} from 'react-redux';
import TextField from 'material-ui/TextField';
import RaisedButton from 'material-ui/RaisedButton';
// import MenuItem from 'material-ui/MenuItem';
// import Paper from 'material-ui/Paper';

import Divider from 'material-ui/Divider';
import ArrowDropRight from 'material-ui/svg-icons/navigation-arrow-drop-right';
import FloatingActionButton from 'material-ui/FloatingActionButton';
import jp from "jsonpath";
import _ from "lodash";

// import {brown500} from 'material-ui/styles/colors';
// import { stack as Menu } from 'react-burger-menu'

// import '../../styles/jquery.multilevelpushmenu.min.css';
// import './jquery.multilevelpushmenu.min.js';
//
// import './custom-menu.js';

const menuConvention={
  "Grievance Redressal.Grievance.Create Complaint":"/pgr/createGrievance",
  "Grievance Redressal.Grievance.Search Complaint":"/pgr/searchGrievance",

  "Grievance Redressal.Masters.Receiving Mode.Create receivingmode Master":"/pgr/receivingModeCreate",
  "Grievance Redressal.Masters.Receiving Mode.Update receivingmode Master":"/pgr/viewOrUpdateReceivingMode/update",
  "Grievance Redressal.Masters.Receiving Mode.Get all receivingmodes":"/pgr/viewOrUpdateReceivingMode/view",

  "Grievance Redressal.Masters.Grievance type.Create a Service Type":"/pgr/serviceTypeCreate",
  "Grievance Redressal.Masters.Grievance type.Update a Service Type":"/pgr/viewOrUpdateServiceType/edit",
  "Grievance Redressal.Masters.Grievance type.Search a Service Type":"/pgr/viewOrUpdateServiceType/view",

  "Grievance Redressal.Masters.Receiving Center.Create receivingcenter Master":"/pgr/createReceivingCenter",
  "Grievance Redressal.Masters.Receiving Center.Update receivingcenter Master":"/pgr/receivingCenter/edit",
  "Grievance Redressal.Masters.Receiving Center.Get all receivingcenters":"/pgr/receivingCenter/view",

  "Grievance Redressal.Masters.Router.CREATE COMPLAINT ROUTER":"/pgr/createRouter",
  "Grievance Redressal.Masters.Router.UPDATE COMPLAINT ROUTER":"/pgr/searchRouter/edit",
  "Grievance Redressal.Masters.Router.SEARCH COMPLAINT ROUTER":"/pgr/searchRouter/view",
  "Grievance Redressal.Masters.Router.Create Bulk Router":"/pgr/routerGeneration",

  "Grievance Redressal.Masters.Grievance Category.Create Service Group": "/pgr/createServiceGroup",
  "Grievance Redressal.Masters.Grievance Category.Update Service Group":"/pgr/serviceGroup/edit",
  "Grievance Redressal.Masters.Grievance Category.Search a Service Group":"/pgr/serviceGroup/view",

  "Grievance Redressal.Reports.Ageing Report":"/report/pgr/AgeingByBoundary",
  "Grievance Redressal.Reports.Drill Down Report":"/report/pgr/DrillDownByBoundary",
  "Grievance Redressal.Reports.Grievance Type Wise Report":"/report/pgr/GrievanceByType",
  "Grievance Redressal.Reports.Functionary Wise Report":"/report/pgr/GrievanceByFunctionary",
  "Grievance Redressal.Reports.Router Escalation Report":"/report/pgr/RouterEscalation",
  "Grievance Redressal.Reports.Ageing By Department Report":"/report/pgr/AgeingByDepartment",
  "Grievance Redressal.Reports.Drill Down By Department Report":"/report/pgr/DrillDownByDepartment",

  "Collection.Collection Reports.CashCollection":"/report/collection/CashCollection",
  "Collection.Collection Reports.ReceiptRegister":"/report/collection/ReceiptRegister",
  "Collection.Collection Reports.CollectionSummaryAccountHead":"/report/collection/CollectionSummaryAccountHeadWise",
  "Collection.Collection Reports.ChequeCollection":"/report/collection/ChequeCollection",

  "Property Tax.PTIS Reports.Demand Register":"/report/property/DemandRegister",
  "Property Tax.PTIS Reports.Collection Register":"/report/property/CollectionRegister",
  "Property Tax.PTIS Reports.Balance Register":"/report/property/BalanceRegister",
  "Property Tax.PTIS Reports.Demand Balance CollectionReport":"/report/property/DemandBalanceCollectionReport",
  "Property Tax.PTIS Reports.Assessment Register":"/report/property/AssessmentRegister",



  "Grievance Redressal.Masters.Escalation Time.Create Escalation Time Type":"/pgr/defineEscalationTime",
  "Grievance Redressal.Masters.Escalation Time.Search Escalation Time":"/pgr/searchEscalationTime",

  "Grievance Redressal.Masters.Escalation.Create Escalation":"/pgr/defineEscalation",
  // "Grievance Redressal.Masters.Escalation.Update Escalation":"",
  "Grievance Redressal.Masters.Escalation.Search Escalation":"/pgr/bulkEscalationGeneration",

	"Water Charge.Water Transactions.CreateNewConnectionAPI":"/create/wc",
	"Water Charge.WCMS Masters.CategoryMasters.CreateCategoryMasterApi":"/create/wc/categoryType",
	"Water Charge.WCMS Masters.ConnectionSize Master.CreatePipeSizeMasterApi":"/create/wc/pipeSize",
	"Water Charge.WCMS Masters.Document Type Master.CreateDocumentTypeMasterApi":"/create/wc/documentType",
	"Water Charge.WCMS Masters.Security Deposit.CreatDonationApi":"/create/wc/donation",
	"Water Charge.WCMS Masters.TariffCategory.CreatPropertyCategoryApi":"/create/wc/propertyCategory",
	"Water Charge.WCMS Masters.TariffConnectionSize.CreatPropertyPipeSizeApi":"/create/wc/propertyPipeSize",
	"Water Charge.WCMS Masters.TariffUsage.CreatPropertyUsageApi":"/create/wc/propertyUsage",
	"Water Charge.WCMS Masters.StorageReservoir.CreatStorageReservoirApi":"/create/wc/storageReservoir",
	"Water Charge.WCMS Masters.TreatmentPlant.CreatTreatmentPlantApi":"/create/wc/treatmentPlants",
	"Water Charge.WCMS Masters.Supply Type Master.CreateSupplyTypeMasterApi":"/create/wc/supplyType",
	"Water Charge.WCMS Masters.Source Type Master.CreateSourceTypeMasterApi":"/create/wc/waterSourceType",
	"Property Tax.New Property.CreateNewProperty":"/propertyTax/create-property",
	"Property Tax.Existing Property.SearchProperty":"/propertyTax/search",
	"Property Tax.New Property.CreateDataEntryProperty":"/propertyTax/create-dataEntry",
  "Water Charge.Water Transactions.SearchWaterConnectionAPI":"/searchconnection/wc",
  "Water Charge.WCMS Masters.CategoryMasters.View Category Type":"/search/wc/categoryType/view",
  "Water Charge.WCMS Masters.CategoryMasters.Update Catgeory Type":"/search/wc/categoryType/update",
  "Water Charge.WCMS Masters.Document Type Master.SearchDocumentTypeMaster":"/search/wc/documentType/view",
  "Water Charge.WCMS Masters.Document Type Master.DocumentTypeModify":"/search/wc/documentType/update",
  "Water Charge.WCMS Masters.DocumentApplication.CreatDocumentApplicationApi":"/create/wc/documentTypeApplicationType",
  "Water Charge.WCMS Masters.DocumentApplication.ModifyDocumentApplicationApi":"/search/wc/documentTypeApplicationType/update",
  "Water Charge.WCMS Masters.DocumentApplication.SearchDocumentApplicationApi":"/search/wc/documentTypeApplicationType/view",
  "Water Charge.WCMS Masters.Security Deposit.ModifyDonationApi":"/search/wc/donation/update",
  "Water Charge.WCMS Masters.Security Deposit.SearchDonationApi":"/search/wc/donation/view",
  "Water Charge.WCMS Masters.Supply Type Master.ModifySupplyTypeMaster":"/search/wc/supplyType/update",
  "Water Charge.WCMS Masters.Supply Type Master.SearchWaterSupplyTypeMaster":"/search/wc/supplyType/view",
  "Water Charge.WCMS Masters.Source Type Master.ModifySourceTypeMaster":"/search/wc/waterSourceType/update",
  "Water Charge.WCMS Masters.Source Type Master.SearchWaterSourceTypeMaster":"/search/wc/waterSourceType/view",
  "Water Charge.WCMS Masters.TariffConnectionSize.ModifyPropertyPipeSizeApi":"/search/wc/propertyPipeSize/update",
  "Water Charge.WCMS Masters.TariffConnectionSize.SearchPropertyPipeSizeApi":"/search/wc/propertyPipeSize/view",
  "Grievance Redressal.Reports.Grievance Report":"/report/GrievanceReport",
  "Water Charge.WCMS Masters.Meter Cost Master.CreateMeterCostMaster":"/create/wc/meterWaterRates",
  "Water Charge.WCMS Masters.Meter Cost Master.UpdateMeterCostMaster":"/search/wc/meterWaterRates/update",
  "Water Charge.WCMS Masters.Meter Cost Master.SearchMeterCostMaster":"/search/wc/meterWaterRates/view",

  "Water Charge.WCMS Masters.ConnectionSize Master.ModifyPipeSizeMasterApi":"/search/wc/pipeSize/update",
  "Water Charge.WCMS Masters.ConnectionSize Master.SearchPipeSizeMaster":"/search/wc/pipeSize/view",
  "Water Charge.WCMS Masters.TariffCategory.ModifyPropertyCategoryApi":"/search/wc/propertyCategory/update",
  "Water Charge.WCMS Masters.TariffCategory.SearchPropertyCategoryApi":"/search/wc/propertyCategory/view",
  "Water Charge.WCMS Masters.TariffUsage.ModifyPropertyUsageApi":"/search/wc/propertyUsage/update",
  "Water Charge.WCMS Masters.TariffUsage.SearchPropertyUsageApi":"/search/wc/propertyUsage/view",
  "Water Charge.WCMS Masters.TreatmentPlant.ModifyTreatmentPlantApi":"/search/wc/treatmentPlants/update",
  "Water Charge.WCMS Masters.TreatmentPlant.SearchTreatmentPlantApi":"/search/wc/treatmentPlants/view",
  "Water Charge.WCMS Masters.StorageReservoir.ModifyStorageReservoirApi":"/search/wc/storageReservoir/update",
  "Water Charge.WCMS Masters.StorageReservoir.SearchStorageReservoirApi":"/search/wc/storageReservoir/view",
  "Water Charge.WCMS Masters.MeterWaterRates.SearchMeterWaterRatesApi":"/search/wc/meterWaterRates/view",
  "Water Charge.WCMS Masters.MeterWaterRates.ModifyMeterWaterRatesApi":"/search/wc/meterWaterRates/update",
  "Water Charge.WCMS Masters.MeterWaterRates.CreatMeterWaterRatesApi":"/create/wc/meterWaterRates",

  "Collection.Collection Masters.Business Detail.CreateBusinessDetailMaster":"/create/collection/businessDetails",
  "Collection.Collection Masters.Business Detail.ModifyBusinessDetailMaster":"/search/collection/businessDetails/update",
  "Collection.Collection Masters.Business Detail.ViewBusinessDetailMaster":"/search/collection/businessDetails/view",
  "Collection.Collection Masters.Business Category.CreateBusinessCategoryMaster":"/create/collection/businessCategory",
  "Collection.Collection Masters.Business Category.ModifyBusinessCategoryMaster":"/search/collection/businessCategory/update",
  "Collection.Collection Masters.Business Category.ViewBusinessCategoryMaster":"/search/collection/businessCategory/view",
  "Collection.Collection Transactions.CreateReceipt":"/transaction/collection/collection",
  "Collection.Collection Transactions.SearchReceipt":"/search/collection/receipt/view",

  "Trade License.License Masters.License Category.CreateLicenseCategory":"/create/tl/CreateLicenseCategory",
  "Trade License.License Masters.License Category.ViewLicenseCategory":"/search/tl/CreateLicenseCategory/view",
  "Trade License.License Masters.License Category.ModifyLicenseCategory": "/search/tl/CreateLicenseCategory/update",

  "Trade License.License Masters.License Sub Category.CreateTLSUBCATEGORY": "/create/tl/CreateLicenseSubCategory",
  "Trade License.License Masters.License Sub Category.ViewTLSUBCATEGORY": "/search/tl/CreateLicenseSubCategory/view",
  "Trade License.License Masters.License Sub Category.ModifyTLSUBCATEGORY": "/search/tl/CreateLicenseSubCategory/update",
  "Trade License.License Transactions.CreateLegacyLicense": "/non-framework/tl/transaction/LegacyLicenseCreate",
  "Trade License.License Search.SearchLicense": "/search/tl/CreateLegacyLicense/view",
  "Trade License.License Reports.License Register Report":"/report/tradelicense/licenseRegisterReport",

  //Trade License Error Messages
  //#---- Validation Error Messages----
  "error.license.licenses.notfound": "licenses is required, but not found",
  "error.license.licenses.size.not.empty": "at least one license should present for the creation",
  "error.license.applicationtype": "Application Type is required, Please enter valid Application Type",
  "error.license.oldLicenseNumber": "oldLicenseNumber is required, Please enter valid Old License Number with 20 characters",
  "error.license.applicationDate": "applicationDate is required, Please enter valid date as epoch",
  "error.license.aadhaarnumber": "Aadhaar Number is required , please enter 12 digits Aadhaar Number",
  "error.license.mobilenumber": "Mobile Number is required , please enter 10 digits Mobile Number",
  "error.license.ownername": "Owner Name is required , please enter Valid name length between 4-100 characters",
  "error.license.fatherspousename": "Father Spouse Name is required , please enter Valid name length between 4-100 characters",
  "error.license.fatherspousename.empty": "FatherSpouseName can not be empty, please enter valid fatherSpouseName with length between 4- 100",
  "error.license.emailid": "Emailid is required, please enter the valid emailid",
  "error.license.emailid.empty": "Emailid can not be empty, Please enter valid Emailid",
  "error.license.emailid.length": "Emailid more than 50 characters not allowed.",
  "error.license.owneraddress.emptyspaces": "Owner Address can not be empty , please enter Valid Address length between 4-250 characters",
  "error.license.owneraddress": "Owner Address is required , please enter Valid Address length between 4-250 characters",
  "error.license.propertyassesmentNo": "Property Assesment No is required ,  please enter Valid Property Assesment No  between 15-20 characters",
  "error.license.locality": "Locality is required , please enter valid Locality",
  "error.license.revenueward": "RevenueWard is required , please enter valid RevenueWard",
  "error.license.adminward": "Admin Ward is required , please enter valid Admin Ward",
  "error.license.tradeaddress": "Trade Address is required , please enter Valid Address length between 4-250 characters",
  "error.license.ownerShip": "OwnerShip Type is required, Please enter valid OwnerShip Type",
  "error.license.tradetitle": "Trade Title is required , please enter Trade Title",
  "error.license.tradetitle.emptyspaces": "Trade Title can not be empty, please enter Trade Title",
  "error.license.tradetype": "Trade Type is required , please enter Valid Trade Type",
  "error.license.ownershiptype": "OwnerShip Type is required , please enter Valid OwnerShip type",
  "error.license.tradetype.emptyspaces": "Trade Type can not be empty, please enter Trade Type",
  "error.license.category": "Category is required , please enter Valid Category",
  "error.license.subcategory": "Sub Category is required , please enter Valid Sub Category",
  "error.license.uom": "Uom is required , please enter Valid Uom",
  "error.license.uom.length": "Quantity should not have more than 2 decimals",
  "error.license.quantity": "Quantity is required , please enter Valid Quantity",
  "error.license.validityyears": "ValidityYears is required , please enter Valid number between 1-10",
  "error.license.tradecommencementdate": "tradeCommencementDate is required, Please enter valid date as epoch",
  "error.license.licensevalidfrom": "LicenseValidFromDate is required, Please enter valid date as epoch",
  "error.license.agreementdate": "AgreementDate is required, Please enter valid date as epoch",
  "error.license.agreementno.notfound": "agreement number is required, but not found",
  "error.license.agreementno": "Please enter valid agreementNo between 4-128",
  "error.financialYear.emptyspaces": "FinancialYear can not be empty, Please enter valid financialYear",
  "error.financialYear.empty": "FinancialYear is required, Please enter valid financialYear",
  "error.license.financialyear": "FinancialYear is required, Please enter valid financialYear",
  "error.license.amount": "Amount is required, Please enter valid Amount",
  "error.license.amount.decimal": "Amount should not have more than 2 decimals",
  "error.license.legacy": "isLegacy is required, Please enter valid value true/false",
  "error.license.propertyowner": "isPropertyOwner is required, Please enter valid value true/false",
  "error.license.ispropertyowner": "isPropertyOwner is required, Please provide value as true/false",
  "error.license.documenttype": "DocumentType is required, Please enter valid DocumentType",
  "error.license.filestore": "FileStoreId is required, Please enter valid FileStoreId ",
  "error.license.tenantId.emptyspaces": "TenantId can not be empty, Please enter valid tenant id with length between 4- 128",
  "error.license.tenantId.empty": "TenantID is required, please enter valid tenant id with length between 4- 128",
  "error.oldLicenseNumber.emptyspaces": "OldLicenseNumber can not be empty, Please enter valid oldLicenseNumber length between 4- 20",
  "error.oldLicenseNumber.empty": "OldLicenseNumber is required, Please enter valid Old License Number between  4-20 characters",
  "error.Assesment.legnth": "Property Assesment No is required , please enter Valid Property Assesment No  between 4-20 characters",
  "error.license.active": "Active is required, Please enter valid value true/false",
  "error.oldLicense.duplicate": "oldLicenseNumber alredy Exists, please provide unique oldLicenseNumber",
//  #--adapter custom messages
  "id.notFound.field": "id",
  "oldLicense.id.notFound.customMsg": "please provide valid id for updating trade license",
  "oldLicense.id.notValid.customMsg": "TradeLicense does not exists for given id",
  "supporting.document.id.notValid.customMsg": "Support document does not exists for given id",
  "supporting.document.id.notFound.customMsg": "please provide valid id for updating trade license support document",
  "feedetail.id.notFound.customMsg": "please provide valid id for updating trade license fee details",
  "feedetail.id.notValid.customMsg": "Fee Details does not exists for given id",
  "mandatory.document.notfound.customMsg": "Required support document(s) are not found, please provide all mandatory support documents",
  "nonlegacy.update.customMsg": " Non Legacy license update not supported",

  "legacy.trade.create.success.status.message": "Legacy tradeLicense(s) created successfully, with License Number(s)",
  "error.license.licenses.notfound": "licenses is required, but not found",
  "new.trade.create.success.status.message": "Your request for creating new tradeLicense(s) is accepted with Application Number(s)",
  "error.license.licenses.size.not.empty": "at least one license should present for the creation/updation",
  "error.license.legacy.feedetails.notfound": "feeDetails are required, please provide valid feeDetails",
  "error.license.applicationtype": "Application Type is required, Please enter valid Application Type",
  "error.license.oldLicenseNumber": "oldLicenseNumber is required, Please enter valid Old License Number with 20 characters",
  "error.oldLicense.duplicate": "oldLicenseNumber alredy Exists, please provide unique oldLicenseNumber",
  "error.license.applicationDate": "applicationDate is required, Please enter valid date as epoch",
  "error.license.aadhaarnumber": " Aadhaar Number is required , please enter 12 digits Aadhaar Number",
  "error.license.mobilenumber ": " Mobile Number is required , please enter 10 digits Mobile Number",
  "error.license.ownername": "Owner Name is required , please enter Valid name length between 4-100 characters",
  "error.license.fatherspousename ": " Father Spouse Name is required , please enter Valid name length between 4-100 characters",
  "error.license.owneraddress": "Owner Address is required , please enter Valid Address length between 4-250 characters",
  "error.license.emailid": "Emailid is required, please enter the valid emailid",
  "error.license.propertyAssesmentNo.notfound": "Property Assesment No  is not found ,  please enter Valid Property Assesment No  between 15-20 characters",
  "error.license.propertyAssesmentNo.notvalid": "Property Assesment No  is not valid ,  please enter Valid Property Assesment No  between 15-20 characters",
  "error.license.locality": "Locality is required , please enter valid Locality",
  "error.license.revenueward": "RevenueWard is required , please enter valid RevenueWard",
  "error.license.adminward": "Admin Ward is required , please enter valid Admin Ward",
  "error.license.tradeaddress": "Trade Address is required , please enter Valid Address length between 4-250 characters",
  "error.license.tradetitle": "Trade Title is required , please enter Trade Title",
  "error.license.tradetype": "Trade Type is required , please enter Valid Trade Type",
  "error.license.category": "Category is required , please enter Valid Category",
  "error.license.subcategory": "Sub Category is required , please enter Valid Sub Category",
  "error.license.uom": "Uom is required , please enter Valid Uom",
  "error.license.quantity": "Quantity is required , please enter Valid Quantity",
  "error.license.validityyears": "ValidityYears is required , please enter Valid number between 1-10",
  "error.license.validityyearsMatch": "ValidityYears is required ,  should match with subacategory validity years",
  "error.license.tradecommencementdate": "tradeCommencementDate is required, Please enter valid date as epoch",
  "error.license.licensevalidfrom": "LicenseValidFromDate is required, Please enter valid date as epoch",
  "error.license.agreementdate": "AgreementDate is required, Please enter valid date as epoch",
  "error.license.agreementno.notfound": "agreement number is required, but not found",
  "error.license.agreementno": "Please enter valid agreementNo between 4-128",
  "error.license.financialyear": "FinancialYear is required, Please enter valid financialYear",
  "error.license.amount": "Amount is required, Please enter valid Amount",
  "error.license.documenttype": "DocumentType is required, Please enter valid DocumentType",
  "error.license.filestore": "FileStoreId is required, Please enter valid FileStoreId",
  "error.license.feedetails": "provided fee details are not valid, please provide valid fee details",
  "error.license.categoryendpoint": "category end point error",
  "error.license.documentendpoint": " document end point error",
  "error.license.locationendpoint": " location end point error",
  "error.license.propertyendpoint ": " proeprty endpoint error",
  "error.license.feeDetailYearNotFound ": "Financial Does not exists for Current/Future Year",


  "Water Charge.Water Transactions.LegacyCreateNewConnectionAPI":"/create/wc/legacy",

  //employee Master,
  "Employee Management.Employee Masters.Position.CreatePosition":"/create/employee/createPosition",
  "Employee Management.Employee Masters.Position.UpdatePosition":"/search/employee/createPosition/update",
  "Employee Management.Employee Masters.Position.ViewPosition":"/search/employee/createPosition/view",
  "Employee Management.Employee Masters.Employee.CreateEmployee":"/employee/create",
  "Employee Management.Employee Masters.Employee.ViewEmployee":"/empsearch/view",
  "Employee Management.Employee Masters.Employee.UpdateEmployee":"/empsearch/update",
  "Employee Management.HR Report.HR Employee History Report" : "/report/hr/EmployeeSearch",
  "Employee Management.HR Report.HR Attendance Report": "/report/hr/AttendenceReport",
  "Employee Management.HR Report.HR Employees Leave Report" : "/report/hr/employeeLeaveReport",

  //Administration
  "Administration.UpdateUserWithoutValidation":"/administration/searchUserRole"




}

const style = {
  display: 'inline-block',
  margin: '14px 32px 16px 0',

};

class CustomMenu extends Component {
  constructor(props) {
    super(props);
    this.state={
      searchText:"",
      menu:[],
      filterMenu:[],
      level:0,
      parentLevel:0,
      modules:[],
      items:[],
      path:"",
      menuItems:[]
    }
    this.handleClickOutside = this.handleClickOutside.bind(this);
    this.setWrapperRef = this.setWrapperRef.bind(this);
  }

  setWrapperRef(node) {
    this.wrapperRef = node;
  }

  componentWillReceiveProps()
  {
    //this.resetMenu();
    console.log("HERE");
  }

  componentDidMount() {

    document.addEventListener('mousedown', this.handleClickOutside);
    // console.log(actionList);
    // duplicteMenuItems=jp.query(actionList,'$...path');
    // console.log(duplicteMenuItems);

    this.resetMenu();

  }

  resetMenu=()=>{
    let {actionList}=this.props;
    let menuItems=[];
    for (var i = 0; i < actionList.length; i++) {
      if (actionList[i].path!="") {
        let splitArray=actionList[i].path.split(".");
        if (splitArray.length>1) {
            if (!_.some(menuItems,{ 'name':splitArray[0]} )) {
              menuItems.push({path:"",name:splitArray[0],url:"",queryParams:actionList[i].queryParams,orderNumber:actionList[i].orderNumber});
            }
        } else{
          menuItems.push({path:"",name:actionList.displayName,url:actionList.url,queryParams:actionList[i].queryParams,orderNumber:actionList[i].orderNumber});
        }
      }
    }


    // console.log(_.orderBy(menuItems, ['orderNumber'], ['asc']));
    this.setState({
      menuItems,
      path:""
    })
  }

  handleClickOutside(event) {
      if (this.wrapperRef && !this.wrapperRef.contains(event.target) && event.target.innerHTML != "menu") {
          this.props.handleToggle(false);
      }
  }


  changeModulesActions(modules,items)
  {
    this.setState({
      modules,
      items
    })
  }

  handleChange=(e)=>
  {
      this.setState({
        searchText:e.target.value
      })
  }

  menuChange=(nextLevel, parentLevel) => {
    this.setState({
      level:nextLevel,
      parentLevel
    });
  }

  menuChangeTwo=(path) => {
    // let tempPath=path;
    let {actionList}=this.props;
    let menuItems=[];
    for (var i = 0; i < actionList.length; i++) {
      // actionList[i].path.startsWith(path)
      if (actionList[i].path!="" && actionList[i].path.startsWith(path+".")) {
        let splitArray=actionList[i].path.split(path+".")[1].split(".");
        if (splitArray.length>1) {
            if (!_.some(menuItems,{ 'name':splitArray[0]} )) {
              menuItems.push({path:path+"."+splitArray[0],name:splitArray[0],url:"",queryParams:actionList[i].queryParams,orderNumber:actionList[i].orderNumber});
            }
            // tempPath=path+"."+splitArray[1];
        } else{
          menuItems.push({path:path+"."+splitArray[0],name:actionList[i].displayName,url:actionList[i].url,queryParams:actionList[i].queryParams,orderNumber:actionList[i].orderNumber});
        }
      }
    }


    // console.log(_.orderBy(menuItems, ['orderNumber'], ['asc']));
    menuItems=_.orderBy(menuItems, ['orderNumber'], ['asc']);
    this.setState({
      menuItems,
      path
    })
  }

  changeLevel=(path)=>{
    let {searchText}=this.state;
    let {setRoute}=this.props;




    if (!path) {
      this.resetMenu();
      // console.log("level 0");
      setRoute("/prd/dashboard");
    }
    else {
      let splitArray=_.split(path, '.');
      var x = splitArray.slice(0, splitArray.length - 1).join(".") ;
      if (x!="" && splitArray.length>1) {
            this.menuChangeTwo(x);
      } else {
            this.resetMenu();
      }

    }
  }

  changeRoute=(route)=>{
      let {setRoute}=this.props;

      // setRoute("/");
      setRoute(route);
  }




  render() {
    // console.log(this.state.searchText);
    let {handleToggle,actionList}=this.props;
    let {searchText,filterMenu,level,parentLevel,modules,items,changeModulesActions,path,menuItems}=this.state;
    let {menuChange,changeLevel,menuChangeTwo,changeRoute}=this;

    const checkUrl = function(item) {
      if(item.url == '/pgr/createReceivingCenter' && window.location.href.indexOf("/pgr/createReceivingCenter")>-1) {
          window.urlCheck = true;
      }

      if(item.url == '/pgr/receivingModeCreate' && window.location.href.indexOf("/pgr/receivingModeCreate/update")>-1) {
          window.urlCheck = true;
      }

      if(item.url == '/pgr/createServiceType' && window.location.href.indexOf("/pgr/serviceTypeCreate/edit")>-1) {
          window.urlCheck = true;
      }

      if(item.url == '/pgr/createServiceGroup' && window.location.href.indexOf("/pgr/updateServiceGroup")>-1) {
          window.urlCheck = true;
      }
    }



    const showMenuTwo=()=>{
      if (!_.isEmpty(menuConvention)) {
        if(searchText.length==0)
        {

          return menuItems.map((item,index)=>{
              if (!item.url) {
                return (
                          <MenuItem
                               style={{whiteSpace: "initial"}}
                               key={index}
                               leftIcon={<i className="material-icons marginLeft">view_module</i>}
                               primaryText={<div className="menuStyle" style={{width: "127px", textOverflow: "ellipsis", whiteSpace: "nowrap", overflow: "hidden"}}><span className="onHoverText hidden-sm hidden-xs">{item.name}</span><span>{item.name}</span></div>}
                               rightIcon={<i className="material-icons">keyboard_arrow_right</i>}
                               onTouchTap={()=>{menuChangeTwo(!item.path?item.name:item.path)}}
                            />
                        )

              }
              else {
                if (menuConvention && menuConvention.hasOwnProperty(item.path)) {
                  // {/*<Link  key={index} to={menuConvention[item.path]} >*/}
                    // {/*</Link>*/}
                  return(

                          <MenuItem
                               style={{whiteSpace: "initial"}}
                               key={index}
                               onTouchTap={()=>{checkUrl(item); document.title=item.name; handleToggle(false); changeRoute(menuConvention[item.path])}}
                               leftIcon={<i className="material-icons marginLeft">view_module</i>}
                               primaryText={<div className="menuStyle" style={{width: "127px", textOverflow: "ellipsis", whiteSpace: "nowrap", overflow: "hidden"}}><span className="onHoverText hidden-sm hidden-xs">{item.name}</span><span>{item.name}</span></div>}
                            />

                      )
                } else {
                  let base="";
                  if (item.path.search("Employee Management.")>-1 || item.path.search("ess.")>-1) {
                    base=window.location.origin+"/hr-web";
                    // console.log(base);
                  }
                  else if (item.path.search("Leases And Agreements.")>-1) {
                    base=window.location.origin+"/lams-web";

                  }
                  else if (item.path.search("Asset Management.")>-1) {
                      base=window.location.origin+"/asset-web";
                  }
                  return (
                           <a key={index} href={base+item.url+((item.queryParams!="" && item.queryParams)?"?"+item.queryParams:"")} target="_blank">
                             <MenuItem
                                  style={{whiteSpace: "initial"}}
                                  leftIcon={<i style={{top: "12px", margin: "0px", left: "24px"}} className="material-icons marginLeft">view_module</i>}
                                  primaryText={<div className="menuStyle" style={{width: "127px", textOverflow: "ellipsis", whiteSpace: "nowrap", overflow: "hidden"}}><span className="onHoverText hidden-sm hidden-xs">{item.name}</span><span>{item.name}</span></div>}
                               />
                            </a>
                          )
                }

              }

          })

        }
        else {

            return actionList.map((item,index)=>{
                if (item.path && item.url && item.displayName.toLowerCase().indexOf(searchText.toLowerCase()) > -1) {

              if (menuConvention.hasOwnProperty(item.path)) {
                return(
                      <Link  key={index} to={menuConvention[item.path]}>
                        <MenuItem
                            style={{whiteSpace: "initial"}}
                             onTouchTap={()=>{checkUrl(item); document.title=item.displayName; handleToggle(false)}}
                             leftIcon={<i className="material-icons marginLeft">view_module</i>}
                             primaryText={<div className="menuStyle" style={{width: "127px", textOverflow: "ellipsis", whiteSpace: "nowrap", overflow: "hidden"}}><span className="onHoverText hidden-sm hidden-xs">{item.displayName}</span><span>{item.displayName}</span></div>}
                          />
                      </Link>
                    )
              } else {
                let base="";
                if (item.path.search("EIS.")>-1 || item.path.search("ess.")>-1) {
                  base=window.location.origin+"/hr-web";
                  // console.log(base);
                }
                else if (item.path.search("Leases And Agreements.")>-1) {
                  base=window.location.origin+"/lams-web";

                }
                else if (item.path.search("Asset Management.")>-1) {
                    base=window.location.origin+"/asset-web";
                }
                return (
                         <a key={index} href={base+item.url} target="_blank">
                           <MenuItem
                                style={{whiteSpace: "initial"}}
                                leftIcon={<i className="material-icons marginLeft">view_module</i>}
                                primaryText={<div className="menuStyle" style={{width: "127px", textOverflow: "ellipsis", whiteSpace: "nowrap", overflow: "hidden"}}><span className="onHoverText hidden-sm hidden-xs">{item.displayName}</span><span>{item.displayName}</span></div>}
                             />
                          </a>
                        )
              }

            }

          })


        }
      }

    }

      return (
      <div className="custom-menu" style={style}  ref={this.setWrapperRef}>
          {
            <TextField
               hintText = "&nbsp;&nbsp;Search"
               onChange={this.handleChange}
               value={searchText}
			   className="searchMargin"
             />
          }






        <Menu desktop={true}>


		{(path|| searchText) &&  <div className="pull-left" style={{marginLeft:12, marginBottom:10, cursor:'pointer'}}  onTouchTap={()=>{changeLevel(path)}}><i className="material-icons" style={{"color": "#757575"}}>arrow_back</i></div>}
        { path &&  <div className="pull-right" style={{marginRight:12,marginBottom:10,cursor:'pointer'}} onTouchTap={()=>{handleToggle(false); changeLevel("")}} ><i className="material-icons" style={{"color": "#757575"}}>home</i></div>}

		<div className="clearfix"></div>

            {showMenuTwo()}

          </Menu>


      </div>
    );
  }
}


const mapStateToProps = state => ({menuConvention:state.common.menuConvention});
const mapDispatchToProps = dispatch => ({
  handleToggle: (showMenu) => dispatch({type: 'MENU_TOGGLE', showMenu}),
  setRoute:(route)=>dispatch({type:'SET_ROUTE',route})
})
export default connect(mapStateToProps,mapDispatchToProps)(CustomMenu);

/*showMenu()*/

// const showMenu=()=>{
//
//   if(searchText.length==0)
//   {
//
//     return menuItems.map((item,index)=>{
//         if (item.level==level) {
//           if (item.url) {
//             return(
//               <Link  key={index} to={item.url} >
//                 <MenuItem
//                     style={{whiteSpace: "initial"}}
//                      onTouchTap={()=>{checkUrl(item); document.title=item.name; handleToggle(false)}}
//                      leftIcon={<i className="material-icons">{item.leftIcon}</i>}
//                      primaryText={item.name}
//                   />
//               </Link>
//
//
//             )
//
//           } else {
//             return (
//                   <MenuItem
//
//                        key={index}
//                        leftIcon={<i className="material-icons">{item.leftIcon}</i>}
//                        primaryText={item.name}
//                        rightIcon={<i className="material-icons">{item.rightIcon}</i>}
//                        onTouchTap={()=>{menuChange(item.nextLevel, item.level)}}
//                     />
//                 )
//           }
//
//         }
//     })
//     return(
//       <div>
//         <MenuItem
//
//              leftIcon={<i className="material-icons">view_module</i>}
//              primaryText={menuItems.length>0?menuItems[0].title:""}
//              rightIcon={<ArrowDropRight />}
//               />
//
//         </div>
//     )
//   }
//   else {
//
//       return menuItems.map((item,index)=>{
//             if (item.url && item.name.toLowerCase().indexOf(searchText.toLowerCase()) > -1) {
//               return(
//                 <Link   key={index} to={item.url} >
//                   <MenuItem
//                       style={{whiteSpace: "initial"}}
//                        onTouchTap={()=>{handleToggle(false)}}
//                        leftIcon={<i className="material-icons">{item.leftIcon}</i>}
//                        primaryText={item.name}
//                     />
//                 </Link>
//               )
//             }
//
//       })
//
//
//   }
// }

// console.log(actionList);
// console.log(menuItems.length>0?menuItems[0].title:"");
// const constructMenu=(items)=>{
//   // console.log(items);
//   let menu=[];
//   if (items) {
//     for (var i=0;i<items.length;i++) {
//       if (items[i].hasOwnProperty("items")) {
//         // console.log("if :");
//         // console.log(items[i]);
//         menu.push(<MenuItem
//           primaryText={items[i].name}
//           rightIcon={<ArrowDropRight />}
//           menuItems={constructMenu(items[i].items.length>0?items[i].items[0].items:[])} />)
//       }
//       else {
//         // console.log("else :");
//         // console.log(items[i]);
//         menu.push(<MenuItem primaryText={items[i].name} />)
//       }
//     }
//   }
//
//
//   return menu;
// }
// console.log(menuItems);
// console.log(parentLevel);


// componentDidUpdate()
// {
//
// }

// menuLeaves=(items)=>{
//   // console.log(items);
//   let menu=[];
//   if (items) {
//     for (var i=0;i<items.length;i++) {
//       if (items[i].hasOwnProperty("items")) {
//         // console.log("if :");
//         // console.log(items[i]);
//         this.menuLeaves(items[i].items.length>0?items[i].items[0].items:[]);
//       }
//       else {
//         // console.log("else :");
//         // console.log(items[i]);
//          menu.push(items[i]);
//
//       }
//     }
//   }
//
//   return menu;
// }
