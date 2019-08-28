const envVariables={
    MAX_NUMBER_PAGES: process.env.MAX_NUMBER_PAGES || 80,
    EGOV_LOCALISATION_HOST: process.env.EGOV_LOCALISATION_HOST || "http://egov-localization:8080",
    EGOV_FILESTORE_SERVICE_HOST: process.env.EGOV_FILESTORE_SERVICE_HOST || "http://egov-filestore:8080",
    SERVER_PORT: process.env.SERVER_PORT || 8080,

    KAFKA_BROKER_HOST: process.KAFKA_BROKER_HOST || "localhost:9092",
    KAFKA_CREATE_JOB_TOPIC: process.KAFKA_CREATE_JOB_TOPIC || "PDF_GEN_CREATE",

    DB_USERNAME: process.env.DB_USERNAME || "postgres",
    DB_PASSWORD: process.env.DB_PASSWORD || "postgres",
    DB_HOST: process.env.DB_HOST || "localhost",
    DB_NAME: process.env.DB_NAME || "PdfGen",
    DB_PORT: process.env.DB_PORT || 5432,
    DATA_CONFIG_URLS: process.env.DATA_CONFIG_URLS || "https://raw.githubusercontent.com/egovernments/egov-services/gopesh_pdf_work/rainmaker/pdf-service/src/config/data-config/pt-receipt.json",
    FORMAT_CONFIG_URLS: process.env.FORMAT_CONFIG_URLS || "https://raw.githubusercontent.com/egovernments/egov-services/gopesh_pdf_work/rainmaker/pdf-service/src/config/format-config/pt-receipt.json"
}
export default envVariables;