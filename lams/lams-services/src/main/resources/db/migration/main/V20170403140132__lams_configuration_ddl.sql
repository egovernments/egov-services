CREATE SEQUENCE seq_eglams_lamsConfigurationValues;
CREATE SEQUENCE seq_eglams_lamsConfiguration;

CREATE TABLE eglams_lamsConfiguration (
	id BIGINT NOT NULL,
	keyName CHARACTER VARYING(50) NOT NULL,
	description CHARACTER VARYING(250),
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_eglams_lamsConfiguration PRIMARY KEY (Id)
);

CREATE TABLE eglams_lamsconfigurationvalues (
	id BIGINT NOT NULL,
	keyId BIGINT NOT NULL,
	value CHARACTER VARYING(1000) NOT NULL,
	effectiveFrom DATE NOT NULL,
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_eglams_lamsconfigurationvalues PRIMARY KEY (Id)
);
