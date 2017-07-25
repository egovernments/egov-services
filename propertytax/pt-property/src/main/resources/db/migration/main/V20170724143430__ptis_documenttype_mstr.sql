CREATE TABLE egpt_mstr_documenttype(
id bigint NOT NULL,
tenantId character varying(128) NOT NULL,
name character varying NOT NULL,
application character varying NOT NULL,
code character varying NOT NULL,
createdby character varying,
lastmodifiedby character varying,
createdtime bigint,
lastmodifiedtime bigint,
CONSTRAINT pk_egpt_mstr_documenttype PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_mstr_documenttype;

ALTER TABLE egpt_mstr_documenttype ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_documenttype');

ALTER TABLE egpt_mstr_documenttype ADD UNIQUE (tenantid, code, application);
