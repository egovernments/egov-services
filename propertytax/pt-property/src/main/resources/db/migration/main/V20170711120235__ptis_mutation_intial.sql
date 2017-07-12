CREATE TABLE egpt_mutation_master(
id bigint NOT NULL,
tenantId character varying NOT NULL,
code character varying NOT NULL,
name character varying NOT NULL,
data jsonb,
createdby character varying,
lastmodifiedby character varying,
createdtime bigint,
lastmodifiedtime bigint,
CONSTRAINT pk_egpt_mutation PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_mutation;

ALTER TABLE egpt_mutation_master ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mutation');
