CREATE TABLE egpgr_otp_config (
    tenantid character varying(256) NOT NULL,
    enabled character varying(1) NOT NULL,
    createdby bigint,
    createddate timestamp without time zone,
 	lastmodifiedby bigint,
 	lastmodifieddate timestamp without time zone,
 	CONSTRAINT egpgr_otp_config_pkey PRIMARY KEY (tenantid),
 	CONSTRAINT egpgr_otp_config_un UNIQUE (tenantid, enabled)
);