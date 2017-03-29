CREATE TABLE egeis_leaveOpeningBalance (
	id BIGINT NOT NULL,
	employeeId BIGINT NOT NULL,
	calendarYear INTEGER NOT NULL,
	leaveTypeId BIGINT NOT NULL,
	noOfDays FLOAT NOT NULL,
	createdBy CHARACTER VARYING(250) NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy CHARACTER VARYING(250),
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_leaveOpeningBalance PRIMARY KEY (Id),
	CONSTRAINT uk_egeis_leaveOpeningBalance_employeeId UNIQUE (employeeId),
	CONSTRAINT uk_egeis_leaveOpeningBalance_calendarYear UNIQUE (calendarYear),
	CONSTRAINT fk_egeis_leaveOpeningBalance_leaveTypeId FOREIGN KEY (leaveTypeId)
		REFERENCES egeis_leaveType(id)
);

CREATE SEQUENCE seq_egeis_leaveOpeningBalance
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;