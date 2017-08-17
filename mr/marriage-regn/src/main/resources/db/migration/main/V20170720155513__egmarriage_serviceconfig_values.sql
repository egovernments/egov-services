CREATE SEQUENCE seq_egmr_serviceconfigurationvalues;
CREATE SEQUENCE seq_egmr_serviceconfiguration;

CREATE TABLE egmr_serviceconfiguration (
	id BIGINT NOT NULL,
	keyName CHARACTER VARYING(50),
	description CHARACTER VARYING(250),
	createdBy CHARACTER VARYING(250) NOT NULL,
	createdTime BIGINT NOT NULL,
	lastModifiedBy CHARACTER VARYING(250),
	lastModifiedTime BIGINT,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egmr_serviceConfiguration_Id PRIMARY KEY (Id,tenantId),
	CONSTRAINT uk_egmr_serviceConfiguration_keyName UNIQUE (keyName,tenantId)
);

CREATE TABLE egmr_serviceconfigurationvalues (
	id BIGINT NOT NULL,
	keyId BIGINT NOT NULL,
	value CHARACTER VARYING(1000) NOT NULL,
	createdBy CHARACTER VARYING(250) NOT NULL,
	createdTime BIGINT NOT NULL,
	lastModifiedBy CHARACTER VARYING(250),
	lastModifiedTime BIGINT,
	tenantId CHARACTER VARYING(250) NOT NULL,
	effectivefrom BIGINT NOT NULL,

	CONSTRAINT pk_egmr_serviceConfigurationValues PRIMARY KEY (Id,tenantId),
	CONSTRAINT uk_egmr_serviceConfigurationValues UNIQUE (keyId,value,effectivefrom,tenantId)
);

