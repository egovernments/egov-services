import uniqBy from "lodash/uniqBy";
const uuidv1 =()=>{
  return require('uuid/v4')()
}


var rn = require('random-number');
var options = {
  min:100,
  integer: true
}


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

// const getUserResponse = async uuids => {
//   let requestBody = {
//     uuid: uuids
//   };
//   const response = await httpRequest(
//     "post",
//     "/user/_search",
//     [],
//     requestBody,
//     []
//   );

//   return response;
// };

export const mergeSearchResults = (response, queryParams) => {
  const responseArray = [];
  response.forEach(element => {
    const buildings = [];
    const owners = [];
    const userIdArray = [];
    const isItemPresent =
      responseArray.findIndex(item => item.fid === element.fid) === -1;
    if (isItemPresent) {
      const fireNocByIDArray = response.filter(obj => obj.fid === element.fid);
      fireNocByIDArray.forEach(async (eachItem, index) => {
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
          name: "Shreya",
          active: eachItem.active,
          ownerType: eachItem.ownertype,
          relationship: eachItem.relationship,
          tenantId: eachItem.tenantId,
          fatherOrHusbandName: ""
        });

        if (index === fireNocByIDArray.length - 1) {
          responseArray.push({
            ...element,
            applicationdate: JSON.parse(BigInt(element.applicationdate)),
            fireNOCDetails: {
              id: element.firenocdetailsid,
              applicationNumberresponseArray: element.applicationNumber,
              applicationStatus: element.action,
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
  return responseArray;
};

export const addUUIDAndAuditDetails=(request)=>{
  request.FireNOCs=request.FireNOCs.map((fireNOC)=>{
    fireNOC.id=uuidv1();
    fireNOC.fireNOCDetails.id=uuidv1();
    fireNOC.fireNOCDetails.applicationNumber=`PB-NOC-${rn(options)}`;
    fireNOC.fireNOCDetails.buildings=fireNOC.fireNOCDetails.buildings.map((building)=>{
      building.id=uuidv1();
      building.applicationDocuments=building.applicationDocuments.map((applicationDocument)=>{
        applicationDocument.id=uuidv1();
      })
      return building;
    });
    fireNOC.fireNOCDetails.propertyDetails.address.id=uuidv1();
    fireNOC.fireNOCDetails.applicantDetails.additionalDetail.id=uuidv1();
    fireNOC.fireNOCDetails.applicantDetails.owners=fireNOC.fireNOCDetails.applicantDetails.owners.map((owner)=>{
      owner.id=owner.id?owner.id:uuidv1();
      return owner;
    });
    fireNOC.auditDetails={
        	createdBy:"Murali M",
        	lastModifiedBy:"",
        	createdTime:new Date().getTime(),
        	lastModifiedTime:0
    };
    return fireNOC;
  })
  return request;
}
