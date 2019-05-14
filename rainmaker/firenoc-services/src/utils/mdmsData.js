import { httpRequest } from "./api";

export default (callback, requestInfo={}) => {
  console.log(requestInfo);
  var requestBody = {
    RequestInfo: {
      apiId: "Rainmaker",
      ver: ".01",
      action: "_search",
      did: "1",
      key: "",
      msgId: "20170310130900|en_IN",
      requesterId: "",
      authToken: "f452cc70-1262-4d18-b784-1e7c1a6f76ea"
    },
    MdmsCriteria: {
      tenantId: "pb.amritsar",
      moduleDetails: [
        {
          moduleName: "common-masters",
          masterDetails: [{ name: "OwnerType" }, { name: "OwnerShipCategory" }]
        },
        {
          moduleName: "firenoc",
          masterDetails: [{ name: "BuildingType" }, { name: "FireStations" }]
        },
        {
          moduleName: "egov-location",
          masterDetails: [{ name: "TenantBoundary" }]
        },
        { moduleName: "tenant", masterDetails: [{ name: "tenants" }] }
      ]
    }
  };
  httpRequest({ endPoint: "/egov-mdms-service/v1/_search", requestBody }).then(
    mdmsResponse => {
      console.log("success");
      callback(mdmsResponse);
    },
    error => {
      console.log("error");
      console.log(error);
      callback({});
    }
  );
};
