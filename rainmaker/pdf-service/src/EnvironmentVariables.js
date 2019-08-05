const envVariables={
    MAX_NUMBER_PAGES: process.env.MAX_NUMBER_PAGES || 80,
    EGOV_LOCALISATION_HOST: process.env.EGOV_LOCALISATION_HOST || "https://egov-micro-dev.egovernments.org",
    EGOV_LOCALISATION_SERVICE_ENDPOINT: process.env.EGOV_LOCALISATION_SERVICE_ENDPOINT || "/localization/messages/v1/_search",
    EGOV_FILESTORE_SERVICE_HOST: process.env.EGOV_FILESTORE_SERVICE_HOST || "https://egov-micro-dev.egovernments.org",
    EGOV_FILESTORE_SERVICE_ENDPOINT: process.env.EGOV_FILESTORE_SERVICE_ENDPOINT || "/filestore/v1/files",
    SERVER_PORT: process.env.SERVER_PORT || 8080,

}
export default envVariables;