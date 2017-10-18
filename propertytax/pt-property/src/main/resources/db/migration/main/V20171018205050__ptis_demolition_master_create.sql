CREATE TABLE egpt_mstr_demolition
(
 id bigserial NOT NULL,
 tenantid character varying(128) NOT NULL,
 name character varying NOT NULL,
 code character varying NOT NULL,
 description character varying,
 createdby character varying,
 createdtime bigint,
 lastmodifiedby character varying,
 lastmodifiedtime bigint,
 CONSTRAINT pk_egpt_mstr_demolition PRIMARY KEY (id)
);

CREATE SEQUENCE seq_egpt_mstr_demolition;
ALTER TABLE egpt_mstr_demolition ALTER COLUMN id SET DEFAULT nextval('seq_egpt_mstr_demolition');

