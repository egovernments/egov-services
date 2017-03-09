CREATE TABLE egeis_probation (
	id BIGINT NOT NULL,
    designationId BIGINT NOT NULL,
	declaredOn DATE NOT NULL,
	orderNo CHARACTER VARYING(250),
	orderDate DATE,
	remarks CHARACTER VARYING(250),
	documents CHARACTER VARYING(250),
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_probation PRIMARY KEY (Id)
);

CREATE SEQUENCE seq_egeis_probation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;