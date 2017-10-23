CREATE TABLE egpt_taxexemptionreason_master(
	id bigint NOT NULL,
	tenantid character varying NOT NULL,
	code character varying NOT NULL,
	name character varying NOT NULL,
	percentagerate numeric NOT NULL,
	active boolean NOT NULL,
	description character varying,
	data jsonb,
	createdby character varying,
	lastmodifiedby character varying,
	createdtime bigint,
	lastmodifiedtime bigint,
	CONSTRAINT pk_egpt_mutation PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_taxexemptionreason;

ALTER TABLE egpt_taxexemptionreason_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_taxexemptionreason');
