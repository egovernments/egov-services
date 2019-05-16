const envVariables = {
    DB_USER: process.env.DB_USER || 'thpqnqhvqfbvqw',
    DB_PASSWORD: process.env.DB_PASSWORD || '46ad4cd2bd2d8d2f3a6cb567482b6c703473dc0eaf150baea5a92909131302b5',
    DB_HOST: process.env.DB_HOST || 'ec2-54-225-68-133.compute-1.amazonaws.com',
    DB_NAME: process.env.DB_NAME || 'd9jf2v9doprlot',
    DB_SSL: process.env.DB_SSL || true,
    DB_PORT: process.env.DB_PORT || 5432,
    KAFKA_BROKER_HOST: process.env.KAFKA_BROKER_HOST || 'localhost:9092',
    KAFKA_TOPICS_FIRENOC_CREATE: process.env.KAFKA_TOPICS_FIRENOC_CREATE || 'save-fn-firenoc',
    KAFKA_TOPICS_FIRENOC_UPDATE: process.env.KAFKA_TOPICS_FIRENOC_UPDATE || 'update-fn-firenoc',
    TRACER_ENABLE_REQUEST_LOGGING: process.env.TRACER_ENABLE_REQUEST_LOGGING || false,
    HOST_URL: process.env.HOST_URL || 'https://egov-micro-dev.egovernments.org',
    DB_MAX_POOL_SIZE: process.env.DB_MAX_POOL_SIZE || '5',
    HTTP_CLIENT_DETAILED_LOGGING_ENABLED: process.env.HTTP_CLIENT_DETAILED_LOGGING_ENABLED || false,
    SERVER_PORT: process.env.SERVER_PORT || '8080',
}
module.exports = envVariables;
