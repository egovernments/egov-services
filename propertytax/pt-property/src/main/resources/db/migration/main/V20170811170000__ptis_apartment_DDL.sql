CREATE TABLE egpt_mstr_apartment(
id bigint NOT NULL,
tenantId character varying(128) NOT NULL,
name character varying(256) NOT NULL,
code character varying(64) NOT NULL,
data jsonb,
createdby character varying,
lastmodifiedby character varying,
createdtime bigint,
lastmodifiedtime bigint,
CONSTRAINT pk_egpt_mstr_apartment PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_mstr_apartment;

ALTER TABLE egpt_mstr_apartment ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_apartment');
