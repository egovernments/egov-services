import { addIDGenId, uuidv1 } from "../utils";
import envVariables from "../envVariables";
import get from "lodash/get";
import userService from "../services/userService";
import isEmpty from "lodash/isEmpty";
import { status } from "./search";

export const addUUIDAndAuditDetails = async request => {
  let { FireNOCs, RequestInfo } = request;
  //for loop should be replaced new alternative
  for (var i = 0; i < FireNOCs.length; i++) {
    let id = get(FireNOCs[i], "id");
    FireNOCs[i].id = id ? id : uuidv1();
    let fireNOCDetailID = get(FireNOCs[i], "fireNOCDetails.id");
    FireNOCs[i].fireNOCDetails.id = fireNOCDetailID
      ? fireNOCDetailID
      : uuidv1();
    let fireNOCApplication = get(
      FireNOCs[i],
      "fireNOCDetails.applicationNumber"
    );
    FireNOCs[i].fireNOCDetails.applicationNumber = fireNOCApplication
      ? fireNOCApplication
      : await addIDGenId(RequestInfo, [
          {
            tenantId: FireNOCs[i].tenantId,
            format: envVariables.EGOV_APPLICATION_FORMATE
          }
        ]);
    FireNOCs[i].fireNOCDetails.buildings = FireNOCs[
      i
    ].fireNOCDetails.buildings.map(building => {
      let buildingId = building.id;
      building.id = buildingId ? buildingId : uuidv1();
      building.applicationDocuments = building.applicationDocuments.map(
        applicationDocument => {
          let applicationId = applicationDocument.id;
          applicationDocument.id = applicationId ? applicationId : uuidv1();
          return applicationDocument;
        }
      );
      building.uoms = building.uoms.map(uom => {
        let uomId = uom.id;
        uom.id = uomId ? uomId : uuidv1();
        return uom;
      });
      return building;
    });
    let addressId = get(
      FireNOCs[i],
      "fireNOCDetails.propertyDetails.address.id"
    );
    FireNOCs[i].fireNOCDetails.propertyDetails.address.id = addressId
      ? addressId
      : uuidv1();
    let applicationDetailsId = get(
      FireNOCs[i],
      "fireNOCDetails.applicantDetails.additionalDetail.id"
    );
    FireNOCs[
      i
    ].fireNOCDetails.applicantDetails.additionalDetail.id = applicationDetailsId
      ? applicationDetailsId
      : uuidv1();
    let createdBy = get(FireNOCs[i], "auditDetails.createdBy");
    let createdTime = get(FireNOCs[i], "auditDetails.createdTime");
    FireNOCs[i].auditDetails = {
      createdBy: createdBy ? createdBy : get(RequestInfo, "userInfo.uuid", ""),
      lastModifiedBy: createdBy ? get(RequestInfo, "userInfo.uuid", "") : "",
      createdTime: createdTime ? createdTime : new Date().getTime(),
      lastModifiedTime: createdTime ? new Date().getTime() : 0
    };
    if (
      FireNOCs[i].fireNOCDetails.applicantDetails.owners &&
      !isEmpty(FireNOCs[i].fireNOCDetails.applicantDetails.owners)
    ) {
      let owners = FireNOCs[i].fireNOCDetails.applicantDetails.owners;
      for (var owneriter = 0; owneriter < owners.length; owneriter++) {
        let userCreateResponse = await createUser(
          RequestInfo,
          owners[owneriter],
          FireNOCs[i].tenantId
        );
        let ownerUUID = get(owners[owneriter], "ownerUUID");
        owners[owneriter] = {
          ...owners[owneriter],
          ...get(userCreateResponse, "user.0", []),
          ownerUUID: ownerUUID ? ownerUUID : uuidv1()
        };
      }
    }
    FireNOCs[i].dateOfApplied = FireNOCs[i].dateOfApplied
      ? FireNOCs[i].dateOfApplied
      : new Date().getTime();
    FireNOCs[i].fireNOCDetails.applicationDate = FireNOCs[i].fireNOCDetails
      .applicationDate
      ? FireNOCs[i].fireNOCDetails.applicationDate
      : new Date().getTime();
    FireNOCs[i].fireNOCDetails.additionalDetail = {
      ...FireNOCs[i].fireNOCDetails.additionalDetail,
      ownerAuditionalDetail:
        FireNOCs[i].fireNOCDetails.applicantDetails.additionalDetail
    };
    FireNOCs[i].fireNOCDetails.status =
      status[FireNOCs[i].fireNOCDetails.action];
  }
  request.FireNOCs = FireNOCs;
  return request;
};

const createUser = async (requestInfo, owner, tenantId) => {
  let userSearchReqCriteria = {};
  let userSearchResponse = {};
  let userCreateResponse = {};
  if (!owner.uuid) {
    //uuid of user not present
    userSearchReqCriteria.userType = "CITIZEN";
    userSearchReqCriteria.tenantId = tenantId;
    userSearchReqCriteria.mobileNumber = owner.mobileNumber;
    userSearchResponse = await userService.searchUser(
      requestInfo,
      userSearchReqCriteria
    );
    if (get(userSearchResponse, "user", []).length > 0) {
      //assign to user

      userCreateResponse = await userService.updateUser(requestInfo, {
        ...userSearchResponse.user[0],
        ...owner
      });
    } else {
      // console.log("user not found");

      owner = addDefaultUserDetails(tenantId, owner);
      // console.log("userSearchResponse.user[0]", userSearchResponse.user[0]);
      // console.log("owner", owner);
      userCreateResponse = await userService.createUser(requestInfo, {
        ...userSearchResponse.user[0],
        ...owner
      });
      // console.log("Create passed");
    }
  } else {
    //uuid present
    userSearchReqCriteria.uuid = [owner.uuid];
    userSearchResponse = await userService.searchUser(
      requestInfo,
      userSearchReqCriteria
    );
    if (get(userSearchResponse, "user", []).length > 0) {
      userCreateResponse = await userService.updateUser(requestInfo, {
        ...userSearchResponse.user[0],
        ...owner
      });
      // console.log("Update passed");
    }
  }
  return userCreateResponse;
};



const addDefaultUserDetails = (tenantId, owner) => {
  if (!owner.userName || isEmpty(owner.userName))
    owner.userName = owner.mobileNumber;
  owner.active = true;
  owner.tenantId = tenantId.split(".")[0];
  owner.type = "CITIZEN";
  owner.roles = [
    { code: "CITIZEN", name: "Citizen", tenantId: tenantId.split(".")[0] }
  ];
  return owner;
};
