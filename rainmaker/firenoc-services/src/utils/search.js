import get from "lodash/get";
import findIndex from "lodash/findIndex";
import isEmpty from "lodash/isEmpty";
import { httpRequest } from "./api";
import envVariables from "../envVariables";


const status = {
  INITIATE: "INITIATED",
  APPROVE: "APPROVED"
};

const fireNOCRowMapper = (row, mapper = {}) => {
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
  let fireNOCDetails = {
    id: row.firenocdetailsid,
    applicationNumber: row.applicationnumber,
    applicationStatus: status[row.action],
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
    propertyDetails: {},
    applicantDetails: {
      ownerShipType: row.ownertype,
      owners: get(fireNoc, "fireNOCDetails.applicantDetails.owners", []),
      additionalDetail: {}
    },
    additionalDetail: row.additionalDetail,
    auditDetails
  };
  //building tranformation , it should refactor in future

  //ownertranformation
  fireNoc.fireNOCDetails = fireNOCDetails;
  fireNoc.auditDetails = auditDetails;
  return fireNoc;
};

const fireNocBuildingsRowMapper = (row, mapper = []) => {
  let buildingIndex = findIndex(mapper, { id: row.buildingid });
  let buildingObject = {
    id: row.buildingid,
    tenantId: row.tenantid,
    name: row.buildingname,
    usageType: row.usagetype,
    uoms: fireNocUomsRowMapper(row, get(buildingObject, "uoms", [])),
    applicationDocuments: fireNocApplicationDocumentsRowMapper(
      row,
      get(buildingObject, "applicationDocuments", [])
    )
  };
  if (buildingIndex != -1) {
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
    documentUid: row.documentuid,
    auditDetails: {
      createdby: row.documentCreatedBy,
      lastmodifiedby: row.documentLastModifiedBy,
      createdtime: row.documentCreatedTime,
      lastmodifiedtime: row.documentLastModifiedTime
    }
  };
  if (applicationDocumentIndex != -1) {
    mapper[applicationDocumentIndex] = applicationDocumentObject;
  } else {
    if (applicationDocumentObject.id) {
      mapper.push(applicationDocumentObject);
    }
  }
  return mapper;
};

export const mergeSearchResults = async response => {
  // const responseArray = []; //Array holding filtered table data
  // const ownerIdArray = []; //holds all rows owners data
  // response.forEach(element => {
  //   const buildings = [];
  //   const owners = [];
  //   const buildingsUoms = [];
  //   const applicationDocs = [];
  //
  //   // check if the firenoc id is already present in the array .If doesnt exists then perform further operation
  //   const isItemPresent =responseArray.findIndex(item => item.fid === element.fid) === -1;
  //   if (isItemPresent) {
  //     //filter array with unique firenoc id
  //     const fireNocByIDArray = response.filter(obj => obj.fid === element.fid);
  //     fireNocByIDArray.forEach((eachItem, index) => {
  //       buildings.push({
  //         id: eachItem.buildingid,
  //         tenantId: eachItem.tenantid,
  //         name: eachItem.buildingname,
  //         usageType: eachItem.usagetype,
  //         uoms: getBuidingUoms(eachItem, buildingsUoms),
  //         applicationDocuments: getApplicationDocuments(
  //           eachItem,
  //           applicationDocs
  //         )
  //       });
  //
  //       owners.push({
  //         id: eachItem.ownerid,
  //         userName: eachItem.username,
  //         useruuid: eachItem.useruuid,
  //         active: eachItem.active,
  //         ownerType: eachItem.ownertype,
  //         relationship: eachItem.relationship,
  //         tenantId: eachItem.tenantId,
  //         fatherOrHusbandName: ""
  //       });
  //
  //       // check if the current index is last item of the subarray, if true then push into final array
  //       if (isItemPresent && index === fireNocByIDArray.length - 1) {
  //         ownerIdArray.push({
  //           id: eachItem.ownerid,
  //           useruuid: eachItem.useruuid
  //         });
  //         responseArray.push({
  //           ...element,
  //           status: status[element.action],
  //           applicationdate: JSON.parse(element.applicationdate?BigInt(element.applicationdate):element.applicationdate),
  //           fireNOCDetails: {
  //             id: element.firenocdetailsid,
  //             applicationNumber: element.applicationnumber,
  //             applicationStatus: status[element.action],
  //             fireNOCType: element.firenoctype,
  //             firestationId: "",
  //             applicationDate: element.applicationdate,
  //             financialYear: element.financialyear,
  //             issuedDate: element.issueddate,
  //             validFrom: element.validfrom,
  //             validTo: element.validto,
  //             action: element.action,
  //             channel: element.channel,
  //             noOfBuildings: "",
  //             buildings: buildings && uniqBy(buildings, "id"),
  //             propertyDetails: {},
  //             applicantDetails: {
  //               ownerShipType: element.ownertype,
  //               owners: owners && uniqBy(owners, "id"),
  //               additionalDetail: {}
  //             },
  //             additionalDetail: {},
  //             auditDetails: {}
  //           }
  //         });
  //       }
  //     });
  //   }
  // });
  // const result = await getFinalResult(ownerIdArray, responseArray);

  // return result;

  // let result={}
  // set(result,`${response[i].fid}.id`,response[i].fid);
  // set(result,`${response[i].fid}.tenantId`,response[i].tenantid);
  // set(result,`${response[i].fid}.fireNOCNumber`,response[i].fireNOCNumber);
  // set(result,`${response[i].fid}.provisionFireNOCNumber`,response[i].provisionFireNOCNumber);
  // set(result,`${response[i].fid}.oldFireNOCNumber`,response[i].oldFireNOCNumber);
  // set(result,`${response[i].fid}.dateOfApplied`,response[i].dateOfApplied);
  let result = [];
  for (var i = 0; i < response.length; i++) {
    let fireNoc = {};
    let index = findIndex(result, { id: response[i].fid });
    if (index != -1) {
      fireNoc = fireNOCRowMapper(response[i], result[index]);
      result[index] = fireNoc;
    } else {
      fireNoc = fireNOCRowMapper(response[i]);
      result.push(fireNoc);
    }
  }

  return result;
};
