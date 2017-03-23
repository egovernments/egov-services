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

CREATE SEQUENCE seq_eglams_lamsConfigurationValues;