CREATE TABLE egeis_technicalQualification (
	id BIGINT NOT NULL,
	skill CHARACTER VARYING(250) NOT NULL,
	grade CHARACTER VARYING(250),
	yearOfPassing INTEGER,
	remarks CHARACTER VARYING(250),
	documents CHARACTER VARYING(250),
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_technicalQualification PRIMARY KEY (Id)
);

CREATE SEQUENCE seq_egeis_technicalQualification
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;