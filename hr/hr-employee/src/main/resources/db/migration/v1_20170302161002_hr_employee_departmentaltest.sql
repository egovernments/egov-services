CREATE TABLE egeis_departmentalTest (
	id BIGINT NOT NULL,
	test CHARACTER VARYING(250) NOT NULL,
	yearOfPassing INTEGER NOT NULL,
	remarks CHARACTER VARYING(250),
	documents CHARACTER VARYING(250),
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_departmentalTest PRIMARY KEY (Id)
);

CREATE SEQUENCE seq_egeis_departmentalTest
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;