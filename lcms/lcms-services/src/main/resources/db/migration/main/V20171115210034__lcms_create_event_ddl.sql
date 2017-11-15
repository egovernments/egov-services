CREATE TABLE egov_lcms_event(
	code character varying,
	entity character varying,
	moduleName character varying,	
	entityCode character varying,
	caseNo character varying,
	departmentConcernPerson character varying,
	nextHearingTime character varying,
	nextHearingDate bigint,
	hearingDetailsCode character varying,
	tenantId character varying,
	createdby character varying,
    lastmodifiedby character varying,
	createdtime bigint,
	lastmodifiedtime bigint,
	CONSTRAINT pk_egov_lcms_event PRIMARY KEY (code)
);