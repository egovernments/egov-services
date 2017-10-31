CREATE TABLE egov_lcms_register(
	code character varying NOT NULL,
	register character varying NOT NULL,
	isActive boolean NOT NULL,
	tenantId character varying NOT NULL,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	CONSTRAINT pk_egov_lcms_register PRIMARY KEY (code)
);