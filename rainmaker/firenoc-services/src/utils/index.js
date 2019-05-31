import uniqBy from "lodash/uniqBy";
import uniq from "lodash/uniq";
import get from "lodash/get";
import { httpRequest } from "./api";
import envVariables from '../envVariables'

const uuidv1 = () => {
  return require("uuid/v4")();
};

export const requestInfoToResponseInfo = (requestinfo, success) => {
  let ResponseInfo = {
    apiId: "",
    ver: "",
    ts: 0,
    resMsgId: "",
    msgId: "",
    status: ""
  };
  ResponseInfo.apiId =
    requestinfo && requestinfo.apiId ? requestinfo.apiId : "";
  ResponseInfo.ver = requestinfo && requestinfo.ver ? requestinfo.ver : "";
  ResponseInfo.ts = requestinfo && requestinfo.ts ? requestinfo.ts : null;
  ResponseInfo.resMsgId = "uief87324";
  ResponseInfo.msgId =
    requestinfo && requestinfo.msgId ? requestinfo.msgId : "";
  ResponseInfo.status = success ? "successful" : "failed";

  return ResponseInfo;
};

const transformById = (payload, id) => {
  return (
    payload &&
    payload.reduce((result, item) => {
      result[item[id]] = {
        ...item
      };

      return result;
    }, {})
  );
};

const getOwnerResponse = async uuids => {
  let requestBody = {
    uuid: uuids
  };
  const response = await httpRequest({
    hostURL:envVariables.EGOV_USER_HOST,
    endPoint: `${envVariables.EGOV_USER_CONTEXT_PATH}${envVariables.EGOV_USER_SEARCH_ENDPOINT}`,
    queryObject: [],
    requestBody,
    headers: []
  });

  return transformById(response.user, "uuid");
};

const status = {
  INITIATE: "INITIATED",
  APPROVE: "APPROVED"
};

const getFinalResult = async (ownerIdArray, responseArray) => {
  let userIdArray = [];
  ownerIdArray &&
    ownerIdArray.length > 0 &&
    ownerIdArray.forEach((item, index) => {
      userIdArray.push(item.useruuid);
    });
  //get owner details
  let userResponse = await getOwnerResponse(uniq(userIdArray));

  //set owner/user details in the Owner Array
  return (
    responseArray &&
    responseArray.length > 0 &&
    responseArray.map(obj => {
      const owners = get(obj, "fireNOCDetails.applicantDetails.owners", []);
      const ownerDetails = owners.map((item, index) => {
        if (
          userResponse &&
          Object.values(userResponse).some(elem => elem.uuid === item.useruuid)
        ) {
          return {
            ...item,
            password: "",
            salutation: "",
            name: userResponse[item.useruuid].name,
            gender: userResponse[item.useruuid].gender,
            mobileNumber: userResponse[item.useruuid].mobileNumber,
            emailId: userResponse[item.useruuid].emailId,
            altContactNumber: "",
            pan: "",
            dob: userResponse[item.useruuid].dob,
            aadhaarNumber: userResponse[item.useruuid].aadhaarNumber,
            permanentAddress: userResponse[item.useruuid].permanentAddress,
            permanentCity: userResponse[item.useruuid].permanentCity,
            permanentPincode: userResponse[item.useruuid].permanentPinCode,
            correspondenceCity: userResponse[item.useruuid].correspondenceCity,
            correspondencePincode:
              userResponse[item.useruuid].correspondencePincode,
            correspondenceAddress:
              userResponse[item.useruuid].correspondenceAddress
          };
        }
      });
      return {
        ...obj,
        fireNOCDetails: {
          ...obj.fireNOCDetails,
          applicantDetails: {
            ...obj.fireNOCDetails.applicantDetails,
            owners: ownerDetails
          }
        }
      };
    })
  );
};

const getBuidingUoms = (rowData, uoms) => {
  if (uoms.findIndex(item => item.id === rowData.uomuuid) === -1) {
    uoms.push({
      id: rowData.uomuuid,
      code: rowData.code,
      value: rowData.value,
      isActiveUom: rowData.activeuom,
      active: rowData.active
    });
  }

  return uoms;
};

const getApplicationDocuments = (rowData, applicationDocs) => {
  if (
    applicationDocs.findIndex(item => item.id === rowData.documentuuid) === -1
  ) {
    applicationDocs.push({
      id: rowData.documentuuid,
      tenantId: rowData.tenantid,
      documentType: rowData.documenttype,
      fileStoreId: rowData.fileStoreid,
      documentUid: rowData.documentuid,
      auditDetails: {
        createdby: rowData.documentCreatedBy,
        lastmodifiedby: rowData.documentLastModifiedBy,
        createdtime: rowData.documentCreatedTime,
        lastmodifiedtime: rowData.documentLastModifiedTime
      }
    });
  }

  return applicationDocs;
};

export const mergeSearchResults = async response => {
  const responseArray = []; //Array holding filtered table data
  const ownerIdArray = []; //holds all rows owners data
  response.forEach(element => {
    const buildings = [];
    const owners = [];
    const buildingsUoms = [];
    const applicationDocs = [];

    // check if the firenoc id is already present in the array .If doesnt exists then perform further operation
    const isItemPresent =
      responseArray.findIndex(item => item.fid === element.fid) === -1;
    if (isItemPresent) {
      //filter array with unique firenoc id
      const fireNocByIDArray = response.filter(obj => obj.fid === element.fid);
      fireNocByIDArray.forEach((eachItem, index) => {
        buildings.push({
          id: eachItem.buildingid,
          tenantId: eachItem.tenantid,
          name: eachItem.buildingname,
          usageType: eachItem.usagetype,
          uoms: getBuidingUoms(eachItem, buildingsUoms),
          applicationDocuments: getApplicationDocuments(
            eachItem,
            applicationDocs
          )
        });

        owners.push({
          id: eachItem.ownerid,
          userName: eachItem.username,
          useruuid: eachItem.useruuid,
          active: eachItem.active,
          ownerType: eachItem.ownertype,
          relationship: eachItem.relationship,
          tenantId: eachItem.tenantId,
          fatherOrHusbandName: ""
        });

        // check if the current index is last item of the subarray, if true then push into final array
        if (isItemPresent && index === fireNocByIDArray.length - 1) {
          ownerIdArray.push({
            id: eachItem.ownerid,
            useruuid: eachItem.useruuid
          });
          responseArray.push({
            ...element,
            status: status[element.action],
            applicationdate: JSON.parse(BigInt(element.applicationdate)),
            fireNOCDetails: {
              id: element.firenocdetailsid,
              applicationNumber: element.applicationnumber,
              applicationStatus: status[element.action],
              fireNOCType: element.firenoctype,
              firestationId: "",
              applicationDate: element.applicationdate,
              financialYear: element.financialyear,
              issuedDate: element.issueddate,
              validFrom: element.validfrom,
              validTo: element.validto,
              action: element.action,
              channel: element.channel,
              noOfBuildings: "",
              buildings: buildings && uniqBy(buildings, "id"),
              propertyDetails: {},
              applicantDetails: {
                ownerShipType: element.ownertype,
                owners: owners && uniqBy(owners, "id"),
                additionalDetail: {}
              },
              additionalDetail: {},
              auditDetails: {}
            }
          });
        }
      });
    }
  });
  const result = await getFinalResult(ownerIdArray, responseArray);
  return result;
};

export const addIDGenId=async(requestInfo,idRequests)=>{
  let requestBody={
    RequestInfo:requestInfo,
    idRequests
  }
  // console.log(requestBody);
  let idGenResponse=await httpRequest({
    hostURL:envVariables.EGOV_IDGEN_HOST,
    endPoint: `${envVariables.EGOV_IDGEN_CONTEXT_PATH}${envVariables.EGOV_IDGEN_GENERATE_ENPOINT}`,
    requestBody
  });
  // console.log("idgenresponse",idGenResponse);
  return get(idGenResponse,"idResponses[0].id")
}

export const addUUIDAndAuditDetails =async (request) => {
  let {FireNOCs,RequestInfo}=request;
  //for loop should be replaced new alternative
  for (var i = 0; i < FireNOCs.length; i++) {
    FireNOCs[i].id = uuidv1();
    FireNOCs[i].fireNOCDetails.id = uuidv1();
    FireNOCs[i].fireNOCDetails.applicationNumber=await addIDGenId(RequestInfo,[{tenantId:FireNOCs[i].tenantId,format:envVariables.EGOV_APPLICATION_FORMATE}]);
    FireNOCs[i].fireNOCDetails.buildings = FireNOCs[i].fireNOCDetails.buildings.map(
      building => {
        building.id = uuidv1();
        building.applicationDocuments = building.applicationDocuments.map(
          applicationDocument => {
            applicationDocument.id = uuidv1();
            return applicationDocument;
          }
        );
        building.uoms = building.uoms.map(
          uom => {
            uom.id = uuidv1();
            return uom;
          }
        );
        return building;
      }
    );
    FireNOCs[i].fireNOCDetails.propertyDetails.address.id = uuidv1();
    FireNOCs[i].fireNOCDetails.applicantDetails.additionalDetail.id = uuidv1();
    FireNOCs[i].fireNOCDetails.applicantDetails.owners = FireNOCs[i].fireNOCDetails.applicantDetails.owners.map(
      owner => {
        //user integration create, search and update is pending
        owner.id = uuidv1();
        return owner;
      }
    );
    FireNOCs[i].auditDetails = {
      createdBy: get(RequestInfo,"userInfo.uuid",''),
      lastModifiedBy: "",
      createdTime: new Date().getTime(),
      lastModifiedTime: 0
    };
  }
  request.FireNOCs=FireNOCs;
  return request;
};

export const createWorkFlow=async(body)=>{
  let processInstances=body.FireNOCs.map((fireNOC)=>{
    return {
            tenantId: fireNOC.tenantId,
            businessService: envVariables.BUSINESS_SERVICE,
            businessId: fireNOC.fireNOCDetails.applicationNumber,
            action: fireNOC.fireNOCDetails.action,
            comment: fireNOC.fireNOCDetails.comment,
            assignee: fireNOC.fireNOCDetails.assignee,
            sla: 0,
            previousStatus: null,
            moduleName: envVariables.BUSINESS_SERVICE
        }
  })
  let requestBody={
    RequestInfo:body.RequestInfo,
    ProcessInstances:processInstances
  }
  let workflowResponse=await httpRequest({
      hostURL:envVariables.EGOV_WORKFLOW_HOST,
      endPoint: envVariables.EGOV_WORKFLOW_TRANSITION_ENDPOINT,
      requestBody
  });
  return workflowResponse;

}

export const addQueryArg = (url, queries = []) => {
  if (url && url.includes("?")) {
    const urlParts = url.split("?");
    const path = urlParts[0];
    let queryParts = urlParts.length > 1 ? urlParts[1].split("&") : [];
    queries.forEach(query => {
      const key = query.key;
      const value = query.value;
      const newQuery = `${key}=${value}`;
      queryParts.push(newQuery);
    });
    const newUrl = path + "?" + queryParts.join("&");
    return newUrl;
  } else {
    return url;
  }
};
