import { httpRequest } from "../utils/api";
import envVariables from "../envVariables";
import { constants } from "../config/constants";

export default async (requestInfo = {}, tenantId) => {
  var requestBody = {
    RequestInfo: requestInfo,
    MdmsCriteria: {
      tenantId,
      moduleDetails: [
        {
          moduleName: constants.MDMS_MODULENAME_FIRENOC,
          masterDetails: [
            { name: constants.MDMS_MASTERNAME_BUILDINGTYPE },
            { name: constants.MDMS_MASTERNAME_FIRENOCULBCONST },
            { name: constants.MDMS_MASTERNAME_FIRENOCSTATECONST },
            { name: constants.MDMS_MASTERNAME_UOMS }
          ]
        },
        {
          moduleName: constants.MDMS_MODULENAME_TENANT,
          masterDetails: [{ name: constants.MDMS_MASTERNAME_TENANTS }]
        }
      ]
    }
  };
  var mdmsResponse = await httpRequest({
    hostURL: envVariables.EGOV_MDMS_HOST,
    endPoint: `${envVariables.EGOV_MDMS_SEARCH_ENDPOINT}`,
    requestBody
  });
  return mdmsResponse;
};
