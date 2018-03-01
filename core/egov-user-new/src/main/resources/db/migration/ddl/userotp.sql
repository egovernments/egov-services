CREATE TABLE eg_user_otp (
    userid character(36) PRIMARY KEY,
    tenantid character varying(256) NOT NULL,
    otpnumber character varying(128) NOT NULL,
    otpidentity character varying(100) NOT NULL,
    validated CHARACTER(1) NOT NULL DEFAULT 'N',
    createdtime bigint NOT NULL,
    lastmodifiedtime bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);