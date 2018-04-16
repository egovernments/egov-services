CREATE TABLE egeis_hrConfiguration (
	id BIGINT NOT NULL,
	keyName CHARACTER VARYING(50) NOT NULL,
	description CHARACTER VARYING(250),
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_hrConfiguration PRIMARY KEY (Id)
);

CREATE SEQUENCE seq_egeis_hrConfiguration
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;