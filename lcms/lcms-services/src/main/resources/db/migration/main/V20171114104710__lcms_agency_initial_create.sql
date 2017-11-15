CREATE TABLE egov_lcms_agency(
	name character varying NOT NULL,
	code character varying NOT NULL,
	agencyAddress character varying NOT NULL,
	isIndividual boolean NOT NULL,
	dateOfEmpanelment bigint NOT NULL,
	standingCommitteeDecisionDate bigint NOT NULL,
	empanelmentFromDate bigint NOT NULL,
	newsPaperAdvertismentDate bigint NOT NULL,
	empanelmentToDate bigint NOT NULL,
	bankName character varying NOT NULL,
	bankBranch character varying NOT NULL,
	bankAccountNo character varying NOT NULL,
	ifscCode character varying NOT NULL,
	micr character varying NOT NULL,
	isActive boolean,
	isTerminate boolean,
	inActiveDate bigint,
	terminationDate bigint,
	reasonOfTermination character varying,
	tenantId character varying NOT NULL,
	createdby character varying,
	lastmodifiedby character varying,
	createdtime bigint,
	lastmodifiedtime bigint,
	CONSTRAINT pk_egov_lcms_agency PRIMARY KEY (code)
);


CREATE TABLE egov_lcms_persondetails(
	name character varying NOT NULL,
	code character varying NOT NULL,
	agencyName character varying NOT NULL,
	agencyCode character varying,
	title character varying,
	firstName character varying NOT NULL,
	secondName character varying NOT NULL,
	lastName character varying NOT NULL,
	address character varying NOT NULL,
	contactNo character varying NOT NULL,
	vatTinNo character varying NOT NULL,
	aadhar character varying NOT NULL,
	gender character varying NOT NULL,
	age character varying NOT NULL,
	dob bigint,
	mobileNumber character varying,
	emailId character varying,
	pan character varying,
	tenantId character varying NOT NULL,
	createdby character varying,
	lastmodifiedby character varying,
	createdtime bigint,
	lastmodifiedtime bigint,
	CONSTRAINT pk_egov_lcms_persondetails PRIMARY KEY (code)
);

ALTER TABLE egov_lcms_persondetails ADD CONSTRAINT fk_egov_lcms_agency FOREIGN KEY(agencyCode) REFERENCES egov_lcms_agency(code);


ALTER TABLE egov_lcms_advocate RENAME COLUMN organizationname TO agencyName;
ALTER TABLE egov_lcms_advocate ADD COLUMN agencycode character varying;
ALTER TABLE egov_lcms_advocate ADD CONSTRAINT fk_egov_lcms_agency FOREIGN KEY(agencycode) REFERENCES egov_lcms_agency(code);


