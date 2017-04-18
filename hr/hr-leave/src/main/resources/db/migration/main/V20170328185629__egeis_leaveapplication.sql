CREATE TABLE egeis_leaveApplication (
	id BIGINT NOT NULL,
	applicationNumber CHARACTER VARYING(100),
	employeeId BIGINT NOT NULL,
	leaveTypeId BIGINT NOT NULL,
	fromDate DATE NOT NULL,
	toDate DATE NOT NULL,
	compensatoryForDate DATE,
	leaveDays FLOAT,
	availableDays FLOAT,
	halfdays INTEGER,
	firstHalfleave BOOLEAN,
	reason CHARACTER VARYING(500),
	status CHARACTER VARYING(10) NOT NULL,
	stateId BIGINT,
	createdBy BIGINT NOT NULL,
	createdDate TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate TIMESTAMP WITHOUT TIME ZONE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_leaveApplication PRIMARY KEY (Id),
	CONSTRAINT uk_egeis_leaveApplication_applicationNumber UNIQUE (applicationNumber),
	CONSTRAINT fk_egeis_leaveApplication_leaveTypeId FOREIGN KEY (leaveTypeId)
		REFERENCES egeis_leaveType(id)
);

CREATE SEQUENCE seq_egeis_leaveApplication
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;