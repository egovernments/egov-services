CREATE TABLE egpt_mstr_configuration (
	id bigint NOT NULL,
	tenantId character varying(128) NOT NULL,
	keyName character varying(256) NOT NULL,
	description character varying(256),
	createdby character varying,
	lastmodifiedby character varying,
	createdtime bigint,
	lastmodifiedtime bigint,	
	CONSTRAINT pk_egpt_mstr_configuration  PRIMARY KEY (Id)
);

CREATE SEQUENCE seq_egpt_mstr_configuration ;
ALTER TABLE egpt_mstr_configuration  ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_configuration ');



CREATE TABLE egpt_mstr_configurationvalues (
	id bigint NOT NULL,
	tenantId character varying(128) NOT NULL,
	keyId bigint NOT NULL,
	value character varying(1000) NOT NULL,
	effectiveFrom timestamp without time zone NOT NULL,
	createdby character varying,
	lastmodifiedby character varying,
	createdtime bigint,
	lastmodifiedtime bigint,	
	CONSTRAINT pk_egpt_mstr_configurationvalues PRIMARY KEY (Id)
);

CREATE SEQUENCE seq_egpt_mstr_configurationvalues;
ALTER TABLE egpt_mstr_configurationvalues ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_configurationvalues');


CREATE TABLE egpt_mstr_guidancevalueboundary (
	id bigint NOT NULL,
	tenantId character varying(128) NOT NULL,
	guidancevalueboundary1 character varying NOT NULL,
	guidancevalueboundary2 character varying,
	createdby character varying,
	lastmodifiedby character varying,
	createdtime bigint,
	lastmodifiedtime bigint,	
	CONSTRAINT pk_egpt_mstr_guidancevalueboundary PRIMARY KEY (Id)
);

CREATE SEQUENCE seq_egpt_mstr_guidancevalueboundary;
ALTER TABLE egpt_mstr_guidancevalueboundary ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_guidancevalueboundary');
