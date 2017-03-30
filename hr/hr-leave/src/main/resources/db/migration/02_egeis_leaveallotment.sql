CREATE TABLE egeis_leaveAllotment (
	id BIGINT NOT NULL,
	designationId BIGINT,
	leaveTypeId BIGINT NOT NULL,
	noOfDays FLOAT NOT NULL,
	createdBy BIGINT NOT NULL,
	createdDate TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate TIMESTAMP WITHOUT TIME ZONE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_leaveAllotment PRIMARY KEY (Id),
	CONSTRAINT fk_egeis_leaveAllotment_leaveTypeId FOREIGN KEY (leaveTypeId)
		REFERENCES egeis_leaveType(id)
);

CREATE SEQUENCE seq_egeis_leaveAllotment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;