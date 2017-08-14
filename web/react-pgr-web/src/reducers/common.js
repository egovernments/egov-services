let menuConvention={
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

  "Grievance Redressal.Reports.Ageing Report":"/report/pgr-master/AgeingByBoundary",
  "Grievance Redressal.Reports.Drill Down Report":"/report/pgr-master/DrillDownByBoundary",
  "Grievance Redressal.Reports.Grievance Type Wise Report":"/report/pgr-master/GrievanceByType",
  "Grievance Redressal.Reports.Functionary Wise Report":"/report/pgr-master/GrievanceByFunctionary",
  "Grievance Redressal.Reports.Router Escalation Report":"/report/pgr-master/RouterEscalation",
  "Grievance Redressal.Reports.Ageing By Department Report":"/report/pgr-master/AgeingByDepartment",
  "Grievance Redressal.Reports.Drill Down By Department Report":"/report/pgr-master/DrillDownByDepartment",

  "Collection.Collection Reports.CashCollection":"/report/collection-services/CashCollection",
  "Collection.Collection Reports.ReceiptRegister":"/report/collection-services/ReceiptRegister",
  "Collection.Collection Reports.CollectionSummaryAccountHead":"/report/collection-services/CollectionSummaryAccountHeadWise",
  "Collection.Collection Reports.ChequeCollection":"/report/collection-services/ChequeCollection",

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
  "Water Charge.Water Transactions.SearchWaterConnectionAPI":"/search/wc/view",
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
  "Trade License.License Transactions.CreateLegacyLicense": "/create/tl/CreateLegacyLicense",


  "Water Charge.Water Transactions.LegacyCreateNewConnectionAPI":"/create/wc/legacy",

  //employee Master,
  "Employee Management.Employee Masters.Position.CreatePosition":"/create/employee/createPosition",
  "Employee Management.Employee Masters.Position.UpdatePosition":"/search/employee/createPosition/update",
  "Employee Management.Employee Masters.Position.ViewPosition":"/search/employee/createPosition/view",
  "Employee Management.Employee Masters.Employee.CreateEmployee":"/create/employee/searchEmployee",
  "Employee Management.Employee Masters.Employee.ViewEmployee":"/search/employee/searchEmployee/view",
  "Employee Management.Employee Masters.Employee.UpdateEmployee":"/search/employee/searchEmployee/update"




}

const defaultState = {
  appName: 'common',
  token: null,
  viewChangeCounter: 0,
  route: '',
  complaintsLength: 0,
  pleaseWait: false,
  showMenu: false,
  actionList:JSON.parse(localStorage.getItem("actions")) || [],
  showHome: false,
  menuConvention:menuConvention,
  tenantInfo:[]
};

export default (state = defaultState, action) => {
  switch (action.type) {
    case 'APP_LOAD':
      return {
        ...state,
        token: action.token || null,
        appLoaded: true,
        currentUser: action.payload ? action.payload.UserRequest : null
      };

    case "SET_TENANT_INFO":
      return {
        ...state,
        tenantInfo:action.tenantInfo
      }

      break;

    case 'REDIRECT':
      return { ...state, redirectTo: null };
    case 'LOGOUT':
      return { ...state, redirectTo: '/'+action.tenantId, token: null, currentUser: null, showMenu: false };
    case 'SETTINGS_SAVED':
      return {
        ...state,
        redirectTo: action.error ? null : '/prd/dashboard',
        currentUser: action.error ? null : action.payload.UserRequest
      };
    case 'LOGIN':
    case 'REGISTER':
      return {
        ...state,
        redirectTo: action.error ? null : '/prd/dashboard',
        token: action.error ? null : action.payload.access_token,
        currentUser: action.error ? null : action.payload.UserRequest
      };
    case 'HOME_PAGE_UNLOADED':
    case 'PROFILE_PAGE_UNLOADED':
    case 'LOGIN_PAGE_UNLOADED':
    case 'REGISTER_PAGE_UNLOADED':
      return { ...state, viewChangeCounter: state.viewChangeCounter + 1 };
    case 'SET_ROUTE':
      return {
        ...state,
        route: action.route,
        redirectTo: action.route,

      }
    case 'SET_HOME':
      return {
        ...state,
        showHome: action.showHome
      }
    case 'GET_LENGTH':
      return {
        ...state,
        complaintsLength: action.payload && action.payload.service_requests && action.payload.service_requests.length ? action.payload.service_requests.length : 10789
      }
    case 'PLEASE_WAIT':
      return {
        ...state,
        pleaseWait: action.pleaseWait
      };
    case 'MENU_TOGGLE':
      return {
        ...state,
        showMenu: action.showMenu
      }
    case 'SET_ACTION_LIST':
        return {
          ...state,
          actionList:action.actionList
        }
      break;
    default:
        return state
  }

  return state;
};
