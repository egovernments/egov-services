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

  "Grievance Redressal.Masters.Grievance Category.Create Service Group": "/pgr/createServiceGroup",
  "Grievance Redressal.Masters.Grievance Category.Update Service Group":"/pgr/serviceGroup/edit",
  "Grievance Redressal.Masters.Grievance Category.Search a Service Group":"/pgr/serviceGroup/view",

  "Grievance Redressal.Reports.Ageing Report":"/report/AgeingByBoundary",
  "Grievance Redressal.Reports.Drill Down Report":"/report/DrillDownByBoundary",
  "Grievance Redressal.Reports.Grievance Type Wise Report":"/report/GrievanceByType",
  "Grievance Redressal.Reports.Functionary Wise Report":"/report/GrievanceByFunctionary",
  "Grievance Redressal.Reports.Router Escalation Report":"/report/RouterEscalation",
  "Grievance Redressal.Reports.Ageing By Department Report":"/report/AgeingByDepartment",
  "Grievance Redressal.Reports.Drill Down By Department Report":"/report/DrillDownByDepartment",

  "Grievance Redressal.Masters.Escalation Time.Create Escalation Time Type":"/pgr/defineEscalationTime",
  "Grievance Redressal.Masters.Escalation Time.Search Escalation Time":"/pgr/searchEscalationTime",

  "Grievance Redressal.Masters.Escalation.Create Escalation":"/pgr/defineEscalation",
  // "Grievance Redressal.Masters.Escalation.Update Escalation":"",
  "Grievance Redressal.Masters.Escalation.Search Escalation":"/pgr/bulkEscalationGeneration",

    "Water Charge.Water Transactions.CreateNewConnectionAPI":"/create/wc",
    "Water Charge.WCMS Masters.CategoryMasters.CreateCategoryMasterApi":"/create/wc/categoryType",
    "Water Charge.WCMS Masters.PipeSize Master.CreatePipeSizeMasterApi":"/create/wc/pipeSize",
    "Water Charge.WCMS Masters.Document Type Master.CreateDocumentTypeMasterApi":"/create/wc/documentType",
    "Water Charge.WCMS Masters.Donation.CreatDonationApi":"/create/wc/donation",
    "Water Charge.WCMS Masters.PropertyCategory.CreatPropertyCategoryApi":"/create/wc/propertyCategory",
    "Water Charge.WCMS Masters.PropertyPipeSize.CreatPropertyPipeSizeApi":"/create/wc/propertyPipeSize",
    "Water Charge.WCMS Masters.PropertyUsage.CreatPropertyUsageApi":"/create/wc/propertyUsage",
    "Water Charge.WCMS Masters.StorageReservoir.CreatStorageReservoirApi":"/create/wc/storageReservoir",
    "Water Charge.WCMS Masters.TreatmentPlant.CreatTreatmentPlantApi":"/create/wc/treatmentPlants",
    "Water Charge.WCMS Masters.Supply Type Master.CreateSupplyTypeMasterApi":"/create/wc/supplyType",
    "Water Charge.WCMS Masters.Source Type Master.CreateSourceTypeMasterApi":"/create/wc/waterSourceType",
	"Property Tax.New Property.CreateNewProperty":"/propertyTax/create-property",
	"Property Tax.Existing Property.SearchProperty":"/propertyTax/search",
  "Water Charge.Water Transactions.SearchWaterConnectionAPI":"/search/wc/view",
  "Water Charge.WCMS Masters.CategoryMasters.View Category Type":"/search/wc/categoryType/view",
  "Water Charge.WCMS Masters.CategoryMasters.Update Catgeory Type":"/search/wc/categoryType/update",
  "Water Charge.WCMS Masters.Document Type Master.SearchDocumentTypeMaster":"/search/wc/documentType/view",
  "Water Charge.WCMS Masters.Document Type Master.DocumentTypeModify":"/search/wc/documentType/update",
  "Water Charge.WCMS Masters.DocumentApplication.CreatDocumentApplicationApi":"/create/wc/documentTypeApplicationType",
  "Water Charge.WCMS Masters.DocumentApplication.ModifyDocumentApplicationApi":"/search/wc/documentTypeApplicationType/update",
  "Water Charge.WCMS Masters.DocumentApplication.SearchDocumentApplicationApi":"/search/wc/documentTypeApplicationType/view",
  "Water Charge.WCMS Masters.Donation.ModifyDonationApi":"/search/wc/donation/update",
  "Water Charge.WCMS Masters.Donation.SearchDonationApi":"/search/wc/donation/view",
  "Water Charge.WCMS Masters.Supply Type Master.ModifySupplyTypeMaster":"/search/wc/supplyType/update",
  "Water Charge.WCMS Masters.Supply Type Master.SearchWaterSupplyTypeMaster":"/search/wc/supplyType/view",
  "Water Charge.WCMS Masters.Source Type Master.ModifySourceTypeMaster":"/search/wc/waterSourceType/update",
  "Water Charge.WCMS Masters.Source Type Master.SearchWaterSourceTypeMaster":"/search/wc/waterSourceType/view",
  "Water Charge.WCMS Masters.PropertyPipeSize.ModifyPropertyPipeSizeApi":"/search/wc/propertyPipeSize/update",
  "Water Charge.WCMS Masters.PropertyPipeSize.SearchPropertyPipeSizeApi":"/search/wc/propertyPipeSize/view",
  "Grievance Redressal.Reports.Grievance Report":"/report/GrievanceReport",

  "Water Charge.WCMS Masters.PipeSize Master.ModifyPipeSizeMasterApi":"/search/wc/pipeSize/update",
  "Water Charge.WCMS Masters.PipeSize Master.SearchPipeSizeMaster":"/search/wc/pipeSize/view",
  "Water Charge.WCMS Masters.PropertyCategory.ModifyPropertyCategoryApi":"/search/wc/propertyCategory/update",
  "Water Charge.WCMS Masters.PropertyCategory.SearchPropertyCategoryApi":"/search/wc/propertyCategory/view",

  "Collection.Collection-Transactions.CreateReceipt":"/transaction/collection/collection"

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
