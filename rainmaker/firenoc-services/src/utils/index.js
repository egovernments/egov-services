import uniqBy from "lodash/uniqBy";
import uniq from "lodash/uniq";
import get from "lodash/get";
import { httpRequest } from "./api";

const uuidv1 = () => {
  return require("uuid/v4")();
};

var rn = require("random-number");
var options = {
  min: 100,
  integer: true
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
    endPoint: "/user/_search",
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

export const mergeSearchResults = async response => {
  const responseArray = []; //Array holding filtered table data
  const ownerIdArray = []; //holds all rows owners data
  response.forEach(element => {
    const buildings = [];
    const owners = [];

    // check if the firenoc id is already present in the array .If doesnt exists then perform further operation
    const isItemPresent =
      responseArray.findIndex(item => item.fid === element.fid) === -1;
    if (isItemPresent) {
      //filter array with unique firenoc id
      const fireNocByIDArray = response.filter(obj => obj.fid === element.fid);
      fireNocByIDArray.forEach((eachItem, index) => {
        buildings.push({
          id: eachItem.buildingid,
          tenantId: eachItem.buildingtenantid,
          name: eachItem.buildingname,
          usageType: eachItem.usagetype,
          noOfFloors: eachItem.nooffloors,
          noOfBasements: eachItem.noofbasements,
          plotsize: eachItem.plotsize,
          builtupArea: eachItem.builtuparea,
          heightOfBuilding: eachItem.heightOfBuilding,
          applicationDocuments: []
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
              applicationNumber: element.applicationNumber,
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
                ownerShipType: element.ownerType,
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

export const addUUIDAndAuditDetails = request => {
  request.FireNOCs = request.FireNOCs.map(fireNOC => {
    fireNOC.id = uuidv1();
    fireNOC.fireNOCDetails.id = uuidv1();
    //id gen service should be integrated here
    fireNOC.fireNOCDetails.applicationNumber = `PB-NOC-${rn(options)}`;
    fireNOC.fireNOCDetails.buildings = fireNOC.fireNOCDetails.buildings.map(
      building => {
        building.id = uuidv1();
        building.applicationDocuments = building.applicationDocuments.map(
          applicationDocument => {
            applicationDocument.id = uuidv1();
          }
        );
        return building;
      }
    );
    fireNOC.fireNOCDetails.propertyDetails.address.id = uuidv1();
    fireNOC.fireNOCDetails.applicantDetails.additionalDetail.id = uuidv1();
    fireNOC.fireNOCDetails.applicantDetails.owners = fireNOC.fireNOCDetails.applicantDetails.owners.map(
      owner => {
        owner.id = owner.id ? owner.id : uuidv1();
        return owner;
      }
    );
    fireNOC.auditDetails = {
      createdBy: "Murali M",
      lastModifiedBy: "",
      createdTime: new Date().getTime(),
      lastModifiedTime: 0
    };
    return fireNOC;
  });
  return request;
};

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
