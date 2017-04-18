CREATE TABLE egeis_hrConfigurationValues (
	id BIGINT NOT NULL,
	keyId BIGINT NOT NULL,
	value CHARACTER VARYING(1000) NOT NULL,
	effectiveFrom DATE NOT NULL,
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egeis_hrConfigurationValues PRIMARY KEY (Id)
	--CONSTRAINT uk_egeis_hrConfigurationValues_keyId UNIQUE (keyId)
);

CREATE SEQUENCE seq_egeis_hrConfigurationValues
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;