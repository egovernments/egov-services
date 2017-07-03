CREATE SEQUENCE seq_egcl_clconfigurationValues;
CREATE SEQUENCE seq_egcl_clconfiguration;

CREATE TABLE egcl_clconfiguration (
	id BIGINT NOT NULL,
	keyName CHARACTER VARYING(50) NOT NULL,
	description CHARACTER VARYING(250),
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egcl_clconfiguration PRIMARY KEY (id)
);

CREATE TABLE egcl_clconfigurationvalues (
	id BIGINT NOT NULL,
	keyId BIGINT NOT NULL,
	value CHARACTER VARYING(1000) NOT NULL,
	effectiveFrom DATE NOT NULL,
	createdBy BIGINT NOT NULL,
	createdDate DATE NOT NULL,
	lastModifiedBy BIGINT,
	lastModifiedDate DATE,
	tenantId CHARACTER VARYING(250) NOT NULL,

	CONSTRAINT pk_egcl_clconfigurationvalues PRIMARY KEY (id)
);