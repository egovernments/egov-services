const envVariables={
    MAX_NUMBER_PAGES: process.env.MAX_NUMBER_PAGES || 80,
    EGOV_HOST: process.env.EGOV_HOST || "https://egov-micro-dev.egovernments.org",
    EGOV_LOCALISATION_SERVICE_ENDPOINT: process.env.EGOV_LOCALISATION_SERVICE_ENDPOINT || "/localization/messages/v1/_search",
    EGOV_FILESTORE_SERVICE_ENDPOINT: process.env.EGOV_FILESTORE_SERVICE_ENDPOINT || "/filestore/v1/files",

}
export default envVariables;