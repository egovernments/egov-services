CREATE TABLE egov_lcms_reference_evidence(

	code character varying NOT NULL,
	tenantId character varying NOT NULL,
	referenceType character varying NOT NULL,
	caseNo character varying NOT NULL,
	caseCode character varying NOT NULL,
	referenceDate bigint NOT NULL,
	description character varying(150) NOT NULL,
	documents jsonb,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	CONSTRAINT pk_egov_lcms_reference_evidence PRIMARY KEY (code)
);

ALTER TABLE egov_lcms_reference_evidence ADD CONSTRAINT fk_egov_lcms_case FOREIGN KEY(casecode) REFERENCES egov_lcms_case(code);