CREATE SEQUENCE seq_egcl_configurationvalues;
CREATE SEQUENCE seq_egcl_configuration;

CREATE TABLE egcl_configuration (
	id BIGINT NOT NULL,
	keyName CHARACTER VARYING(50) NOT NULL,
	description CHARACTER VARYING(250),
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egcl_configuration PRIMARY KEY (id)
);

CREATE TABLE egcl_configurationvalues (
	id BIGINT NOT NULL,
	keyId BIGINT NOT NULL,
	value CHARACTER VARYING(1000) NOT NULL,
	effectiveFrom DATE NOT NULL,
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egcl_configurationvalues PRIMARY KEY (id)
);