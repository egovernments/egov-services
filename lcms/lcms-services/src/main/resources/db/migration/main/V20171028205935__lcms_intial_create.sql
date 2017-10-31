--Case
Create table egov_lcms_case(
	code character varying,
	isSummon boolean NOT NULL,
	summonReferenceNo character varying,
	caseNo character varying NOT NULL,
	summonDate bigint,
	year character varying NOT NULL,
	caseType jsonb,
	plantiffName character varying NOT NULL,
	defendant character varying NOT NULL,
	caseCategory jsonb,
	courtName jsonb,
	departmentName jsonb NOT NULL,
	sectionApplied character varying,
	hearingDate bigint NOT NULL,
	hearingTime bigint,
	ward character varying NOT NULL,
	bench jsonb,
	side jsonb,
	stamp jsonb,
	caseDetails character varying NOT NULL,
	plantiffAddress jsonb,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	tenantId character varying NOT NULL,
	stateId character varying,
	documents jsonb,
	summon jsonb,
	oldCaseNo character varying,
	suitNo character varying,
	judgeDetails character varying,
	caseRefernceNo character varying,
	departmentPerson character varying,
	caseRegistrationDate bigint,
	vakalatnamaGenerationDate bigint,
	isVakalatnamaGenerated boolean,
	witness jsonb,
	coName character varying,
	age character varying,
	days bigint,
	address jsonb,
	pleaderEngagementDetails character varying,
    receiptDate bigint,
    reslovtion character varying,
    reslovtionDate bigint,
    advocateInfoDate bigint,
    remarks character varying,
    isUlbinitiated boolean,
	CONSTRAINT pk_egov_lcms_case PRIMARY KEY ( code )
);
	

--AdvocatePayment
CREATE TABLE eglcms_advocate_payment(
	code character varying,
	advocate jsonb,
	demandDate bigint NOT NULL,
	year character varying,
	caseType jsonb,
	caseNo character varying NOT NULL,
	caseStatus jsonb,
	amountClaimed NUMERIC,
	amountRecived NUMERIC,
	allowance NUMERIC,
	isPartialPayment boolean NOT NULL,
	invoiceDoucment jsonb NOT NULL,
	tenantId character varying(128) NOT NULL,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	resolutionDate bigint,
	resolutionNo character varying,
	resolutionRemarks character varying,
	advocateCharges jsonb,
	modeOfPayment character varying,
	instrumentNumber character varying,
	instrumentDate bigint,
	stateId character varying,
    pleaderEngagementDetails character varying,
    receiptDate bigint,
    reslovtion character varying,
    reslovtionDate bigint,
    advocateInfoDate bigint,
    remarks character varying
);


-- Advocate

CREATE TABLE egov_lcms_advocate(
	code character varying NOT NULL,
	name character varying(100) NOT NULL,
	organizationName character varying(100) NOT NULL,
	isIndividual boolean,
	title character varying NOT NULL,
	firstName character varying NOT NULL,
	secondName character varying NOT NULL,
	lastName character varying NOT NULL,
	address character varying NOT NULL,
	contactNo character varying NOT NULL,
	dateOfEmpanelment bigint NOT NULL,
	standingCommitteeDecisionDate bigint NOT NULL,
	empanelmentFromDate bigint NOT NULL,
	aadhar character varying(12) NOT NULL,
	gender character varying NOT NULL,
	age character varying NOT NULL,
	dob bigint NOT NULL,
	mobileNumber character varying(10) NOT NULL,
	emailId character varying NOT NULL,
	pan character varying(10) NOT NULL,
	vatTinNo character varying NOT NULL,
	newsPaperAdvertismentDate bigint NOT NULL,
	empanelmentToDate bigint NOT NULL,
	bankName character varying NOT NULL,
	bankBranch character varying NOT NULL,
	bankAccountNo character varying NOT NULL,
	isfcCode character varying NOT NULL,
	micr character varying NOT NULL,
	isActive boolean NOT NULL,
	isTerminate boolean NOT NULL,
	inActiveDate bigint, 
	terminationDate bigint,
	reasonOfTermination character varying,
	tenantId character varying,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	CONSTRAINT pk_egov_lcms_advocate PRIMARY KEY (code)
);


-- Advocate Relation Table

CREATE TABLE egov_lcms_case_advocate(
	code character varying NOT NULL,
	casecode character varying,
	advocate jsonb,
	assigndate bigint,
	fee NUMERIC,
	CONSTRAINT pk_egov_lcms_case_advocate PRIMARY KEY (code)
);


ALTER TABLE egov_lcms_case_advocate ADD CONSTRAINT fk_egov_lcms_case FOREIGN KEY(casecode) REFERENCES egov_lcms_case(code);


-- Para wise comment
CREATE TABLE egov_lcms_parawise_comments(
	code character varying NOT NULL,
	parawiseCommentsAskedDate bigint NOT NULL,
	parawiseCommentsReceivedDate bigint NOT NULL,
	hodProvidedDate bigint NOT NULL,
	resolutionDate bigint NOT NULL,
	paraWiseComments character varying NOT NULL,
	tenantId character varying NOT NULL,
	documents jsonb,
	casecode character varying NOT NULL,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	CONSTRAINT pk_egov_lcms_parawise_comments PRIMARY KEY (code)
);

ALTER TABLE egov_lcms_parawise_comments ADD CONSTRAINT fk_egov_lcms_case FOREIGN KEY(casecode) REFERENCES egov_lcms_case(code);


-- Hearing details

CREATE TABLE egov_lcms_hearing_details(
	code character varying NOT NUll,
	caseJudgeMent character varying,
	caseFinalDecision character varying,
	caseStatus jsonb,
	nextHearingDate bigint,
	attendees jsonb,
	judges jsonb,
	tenantId character varying(128),
	casecode character varying NOT NULL,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	judgeMentDate bigint,
    advocateOpinion character varying,
    furtherProcesssDetails character varying,
    darkhasthDueDate bigint,
	CONSTRAINT pk_egov_lcms_hearing_details PRIMARY KEY (code)
);

ALTER TABLE egov_lcms_hearing_details ADD CONSTRAINT fk_egov_lcms_case FOREIGN KEY(casecode) REFERENCES egov_lcms_case(code);

-- Attendee Details
CREATE TABLE egov_lcms_attendee_details(
	code character varying NOT NULL,
	name character varying,
	mobileNumber character varying,
	gender character varying,
	address character varying,
	tenantId character varying,
	hearingcode character varying NOT NULL,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	CONSTRAINT pk_egov_lcms_attendee_details PRIMARY KEY (code)
);

ALTER TABLE egov_lcms_attendee_details ADD CONSTRAINT fk_egov_lcms_hearing_details FOREIGN KEY(hearingcode) REFERENCES egov_lcms_hearing_details(code);


-- Judge Details


CREATE TABLE egov_lcms_judge_details(
	code character varying NOT NULL,
	name character varying,
	mobileNumber character varying,
	gender character varying,
	address character varying,
	tenantId character varying,
	hearingcode character varying NOT NULL,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	CONSTRAINT pk_egov_lcms_judge_details PRIMARY KEY (code)
);

ALTER TABLE egov_lcms_judge_details ADD CONSTRAINT fk_egov_lcms_hearing_details FOREIGN KEY(hearingcode) REFERENCES egov_lcms_hearing_details(code);



-- Opinion
CREATE TABLE egov_lcms_opinion(
	code character varying NOT NULL,
	opinionRequestDate bigint NOT NULL,
	departmentName character varying NOT NULL,
	opinionOn character varying NOT NULL,
	documents jsonb,
	opinionDescription character varying,
	additionalAdvocate character varying,
	opinionsBy jsonb,
	inWardDate bigint,
	tenantId character varying,
	stateId character varying,
	createdBy character varying,
	lastModifiedBy character varying,
	createdTime bigint,
	lastModifiedTime bigint,
	CONSTRAINT pk_egov_lcms_opinion PRIMARY KEY (code)
);

CREATE TABLE egov_lcms_case_voucher(
    code character varying NOT NULL,
    casecode character varying NOT NULL,
    vocherType character varying,
    vocherDate bigint,
    details character varying,
    verificationRemarks character varying,
    officerSignature character varying,
    CONSTRAINT pk_egov_lcms_case_voucher PRIMARY KEY (code)
);
ALTER TABLE egov_lcms_case_voucher ADD CONSTRAINT fk_egov_lcms_case_voucher FOREIGN KEY(casecode) REFERENCES egov_lcms_case(code);
