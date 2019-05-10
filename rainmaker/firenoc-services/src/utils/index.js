import uniqBy from "lodash/uniqBy";

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

export const mergeSearchResults = (response, queryParams) => {
  const responseArray = [];
  response.forEach(element => {
    const buildings = [];
    const owners = [];
    if (responseArray.findIndex(item => item.id === element.FID) === -1) {
      const fireNocByIDArray = response.filter(obj => obj.id === element.FID);
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
          password: "",
          salutation: "",
          name: "",
          gender: "",
          mobileNumber: "",
          emailId: "",
          altContactNumber: "",
          pan: "",
          aadhaarNumber: "",
          permanentAddress: "",
          permanentCity: "",
          permanentPincode: "",
          correspondenceCity: "",
          correspondencePincode: "",
          correspondenceAddress: "",
          active: eachItem.active,
          dob: eachItem.dob,
          ownerType: eachItem.ownertype,
          relationship: eachItem.relationship,
          tenantId: eachItem.tenantId,
          fatherOrHusbandName: ""
        });

        if (index === fireNocByIDArray.length - 1) {
          responseArray.push({
            ...element,
            fireNOCDetails: {
              id: element.firenocdetailsid,
              applicationNumberresponseArray: element.applicationNumber,
              applicationStatus: queryParams.action,
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
