const envVariables={
    MAX_NUMBER_PAGES: process.env.MAX_NUMBER_PAGES || 80,
    EGOV_LOCALISATION_HOST: process.env.EGOV_LOCALISATION_HOST || "http://egov-localization:8080",
    EGOV_FILESTORE_SERVICE_HOST: process.env.EGOV_FILESTORE_SERVICE_HOST || "http://egov-filestore:8080",
    SERVER_PORT: process.env.SERVER_PORT || 8080,

}
export default envVariables;