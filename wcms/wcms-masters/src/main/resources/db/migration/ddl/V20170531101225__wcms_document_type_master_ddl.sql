---Document Type Master
CREATE TABLE egwtr_document_type
(
  id SERIAL,
  name character varying(250) NOT NULL,
  code character varying(250) NOT NULL,
  description character varying(250),
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_document_type PRIMARY KEY (id,tenantid),
  CONSTRAINT uc_documentcode_tenant UNIQUE (code, tenantid)
);
CREATE SEQUENCE seq_egwtr_document_type;