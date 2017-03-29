CREATE TABLE egeis_leaveType (
	id BIGINT NOT NULL,
	name CHARACTER VARYING(50) NOT NULL,
	description CHARACTER VARYING(250),
	halfdayAllowed BOOLEAN NOT NULL,
	payEligible BOOLEAN NOT NULL,
	accumulative BOOLEAN NOT NULL,
	encashable BOOLEAN NOT NULL,
	active BOOLEAN DEFAULT true,
	createdBy CHARACTER VARYING(250) NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy CHARACTER VARYING(250),
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_leaveType PRIMARY KEY (Id),
	CONSTRAINT uk_egeis_leaveType_name UNIQUE (name)
);

CREATE SEQUENCE seq_egeis_leaveType
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;