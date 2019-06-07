import {addIDGenId,uuidv1} from './index';
import envVariables from "../envVariables";

export const addUUIDAndAuditDetails = async request => {
  let { FireNOCs, RequestInfo } = request;
  //for loop should be replaced new alternative
  for (var i = 0; i < FireNOCs.length; i++) {
    FireNOCs[i].id = uuidv1();
    FireNOCs[i].fireNOCDetails.id = uuidv1();
    FireNOCs[i].fireNOCDetails.applicationNumber = await addIDGenId(
      RequestInfo,
      [
        {
          tenantId: FireNOCs[i].tenantId,
          format: envVariables.EGOV_APPLICATION_FORMATE
        }
      ]
    );
    FireNOCs[i].fireNOCDetails.buildings = FireNOCs[
      i
    ].fireNOCDetails.buildings.map(building => {
      building.id = uuidv1();
      building.applicationDocuments = building.applicationDocuments.map(
        applicationDocument => {
          applicationDocument.id = uuidv1();
          return applicationDocument;
        }
      );
      building.uoms = building.uoms.map(uom => {
        uom.id = uuidv1();
        return uom;
      });
      return building;
    });
    FireNOCs[i].fireNOCDetails.propertyDetails.address.id = uuidv1();
    FireNOCs[i].fireNOCDetails.applicantDetails.additionalDetail.id = uuidv1();
    FireNOCs[i].fireNOCDetails.applicantDetails.owners = FireNOCs[
      i
    ].fireNOCDetails.applicantDetails.owners.map(owner => {
      //user integration create, search and update is pending
      owner.id = uuidv1();
      return owner;
    });
    FireNOCs[i].auditDetails = {
      createdBy: get(RequestInfo, "userInfo.uuid", ""),
      lastModifiedBy: "",
      createdTime: new Date().getTime(),
      lastModifiedTime: 0
    };
  }
  request.FireNOCs = FireNOCs;
  return request;
};
