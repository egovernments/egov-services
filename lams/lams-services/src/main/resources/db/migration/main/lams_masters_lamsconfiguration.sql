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

CREATE SEQUENCE seq_eglams_lamsConfiguration;
