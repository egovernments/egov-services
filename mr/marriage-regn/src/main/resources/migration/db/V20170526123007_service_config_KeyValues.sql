CREATE SEQUENCE seq_egmr_serviceConfigurationValues;
CREATE SEQUENCE seq_egmr_serviceConfiguration;

CREATE TABLE egmr_serviceConfiguration (
	id BIGINT NOT NULL,
	keyName CHARACTER VARYING(50),
	description CHARACTER VARYING(250),
	createdBy CHARACTER VARYING(250) NOT NULL,
	createdTime BIGINT NOT NULL,
	lastModifiedBy CHARACTER VARYING(250),
	lastModifiedTime BIGINT,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egmr_serviceConfiguration PRIMARY KEY (Id,tenantId)
);

CREATE TABLE egmr_serviceConfigurationValues (
	id BIGINT NOT NULL,
	keyId BIGINT NOT NULL,
	value CHARACTER VARYING(1000) NOT NULL,
	createdBy CHARACTER VARYING(250) NOT NULL,
	createdTime BIGINT NOT NULL,
	lastModifiedBy CHARACTER VARYING(250),
	lastModifiedTime BIGINT,
	tenantId CHARACTER VARYING(250) NOT NULL,
	effectivefrom BIGINT NOT NULL,

	CONSTRAINT pk_egmr_serviceConfigurationValues PRIMARY KEY (Id,tenantId)
);
