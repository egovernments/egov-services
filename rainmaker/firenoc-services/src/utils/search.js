import get from "lodash/get";
import findIndex from "lodash/findIndex";
import isEmpty from "lodash/isEmpty";
import { httpRequest } from "./api";
import envVariables from "../envVariables";
import userService from "../services/userService";

let requestInfo = {};
export const status = {
  "INITIATE":"INITIATED",
  "APPROVE":"APPROVED",
  "APPLY":"PENDINGPAYMENT",
  "ADHOC":"PENDINGPAYMENT",
  "PAY":"DOCUMENTVERIFY",
  "FORWARD":"FIELDINSPECTION",
  "REJECT":"REJECTED",
  "APPROVE":"APPROVED",
  "CANCEL":"CANCELED"
};


export const actions = {
  INITIATED: "INITIATE",
  APPROVED: "APPROVE",
  PENDINGPAYMENT: "APPLY",
  DOCUMENTVERIFY: "PAY",
  FIELDINSPECTION: "FORWARD",
  REJECTED: "REJECT",
  APPROVED: "APPROVE",
  CANCELED: "CANCEL"
};

const fireNOCRowMapper =async (row, mapper = {}) => {
  let fireNoc = isEmpty(mapper) ? {} : mapper;
  fireNoc.id = row.fid;
  fireNoc.tenantId = row.tenantid;
  fireNoc.fireNOCNumber = row.firenocnumber;
  fireNoc.provisionFireNOCNumber = row.provisionfirenocnumber;
  fireNoc.oldFireNOCNumber = row.oldfirenocnumber;
  fireNoc.dateOfApplied = row.dateofapplied;
  let auditDetails = {
    createdBy: row.createdby,
    lastModifiedBy: row.lastmodifiedby,
    createdTime: row.createdtime,
    lastModifiedTime: row.lastmodifiedtime
  };
  let owners=await fireNocOwnersRowMapper(
    row,
    get(fireNoc, "fireNOCDetails.applicantDetails.owners", [])
  );
  let fireNOCDetails = {
    id: row.firenocdetailsid,
    applicationNumber: row.applicationnumber,
    status: status[row.action],
    fireNOCType: row.firenoctype,
    firestationId: row.firestationId,
    applicationDate: row.applicationdate,
    financialYear: row.financialyear,
    issuedDate: row.issueddate,
    validFrom: row.validfrom,
    validTo: row.validto,
    action: row.action,
    channel: row.channel,
    noOfBuildings: row.noofbuildings,
    buildings: fireNocBuildingsRowMapper(
      row,
      get(fireNoc, "fireNOCDetails.buildings", [])
    ),
    propertyDetails: {
      id: row.puuid,
      propertyId: row.propertyid,
      address: {
        tenantId: row.tenantid,
        doorNo: row.pdoorno,
        latitude: row.platitude,
        longitude: row.plongitude,
        addressNumber: row.paddressNumber,
        buildingName: row.pbuildingname,
        city: row.pcity,
        locality: {
          code: row.plocality
        },
        pincode: row.ppincode,
        street: row.pstreet
      }
    },
    applicantDetails: {
      ownerShipType: row.ownertype,
      owners,
      additionalDetail: get(row,'additionaldetail.ownerAuditionalDetail',{})
    },
    additionalDetail: row.additionaldetail,
    auditDetails
  };
  //building tranformation , it should refactor in future

  //ownertranformation
  fireNoc.fireNOCDetails = fireNOCDetails;
  fireNoc.auditDetails = auditDetails;
  return fireNoc;
};

const fireNocOwnersRowMapper = async (row, mapper = []) => {
  let ownerIndex = findIndex(mapper, { id: row.ownerid });
  let ownerObject = {
    id: row.ownerid,
    userName: row.username,
    useruuid: row.useruuid,
    active: row.active,
    ownerType: row.ownertype,
    relationship: row.relationship,
    tenantId: row.tenantId,
    fatherOrHusbandName: ""
  };
  if (ownerIndex != -1) {
    mapper[ownerIndex] = ownerObject;
  } else {
    let user = {};
    if (row.useruuid) user =await searchUser(requestInfo, row.useruuid);
    console.info("user",user);
    mapper.push({ ...ownerObject, ...user });
  }
  return mapper;
};

const fireNocBuildingsRowMapper = (row, mapper = []) => {
  let buildingIndex = findIndex(mapper, { id: row.buildingid });
  let buildingObject = {
    id: row.buildingid,
    tenantId: row.tenantid,
    name: row.buildingname,
    usageType: row.usagetype,
    uoms: fireNocUomsRowMapper(row),
    applicationDocuments: fireNocApplicationDocumentsRowMapper(row)
  };
  if (buildingIndex != -1) {
    buildingObject.uoms = fireNocUomsRowMapper(
      row,
      get(mapper[buildingIndex], "uoms", [])
    );
    buildingObject.applicationDocuments = fireNocApplicationDocumentsRowMapper(
      row,
      get(mapper[buildingIndex], "applicationDocuments", [])
    );
    mapper[buildingIndex] = buildingObject;
  } else {
    mapper.push(buildingObject);
  }
  return mapper;
};

const fireNocUomsRowMapper = (row, mapper = []) => {
  let uomIndex = findIndex(mapper, { id: row.uomuuid });
  let uomObject = {
    id: row.uomuuid,
    code: row.code,
    value: row.value,
    isActiveUom: row.activeuom,
    active: row.active
  };
  if (uomIndex != -1) {
    mapper[uomIndex] = uomObject;
  } else {
    mapper.push(uomObject);
  }
  return mapper;
};

const fireNocApplicationDocumentsRowMapper = (row, mapper = []) => {
  let applicationDocumentIndex = findIndex(mapper, { id: row.documentuuid });
  let applicationDocumentObject = {
    id: row.documentuuid,
    tenantId: row.tenantid,
    documentType: row.documenttype,
    fileStoreId: row.fileStoreid,
    documentUid: row.documentuid
  };
  if (applicationDocumentIndex != -1) {
    mapper[applicationDocumentIndex] = applicationDocumentObject;
  } else {
    if (row.documentuuid) {
      mapper.push(applicationDocumentObject);
    }
  }
  return mapper;
};

export const mergeSearchResults = async (response, query = {}, reqInfo) => {
  requestInfo = reqInfo;
  let result = [];
  for (var i = 0; i < response.length; i++) {
    let fireNoc = {};
    let index = findIndex(result, { id: response[i].fid });
    if (index != -1) {
      fireNoc =await fireNOCRowMapper(response[i], result[index]);
      result[index] = fireNoc;
    } else {
      fireNoc =await fireNOCRowMapper(response[i]);
      result.push(fireNoc);
    }
  }
  return result;
};

const searchUser = async (requestInfo, uuid) => {
  let userSearchReqCriteria = {};
  let userSearchResponse = {};
  userSearchReqCriteria.uuid = [uuid];
  userSearchResponse = await userService.searchUser(
    requestInfo,
    userSearchReqCriteria
  );
  let users=get(userSearchResponse,"user",[]);
  return users.length?users[0]:{};
};
