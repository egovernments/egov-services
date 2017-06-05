---Document Type Master
CREATE TABLE egwtr_document_type
(
  id SERIAL,
  code character varying(20) NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(250),
  active boolean NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid character varying(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_document_type PRIMARY KEY (id, tenantid),
  CONSTRAINT unq_document_code UNIQUE (code, tenantid),
  CONSTRAINT un_document_name UNIQUE (name, tenantid)
);

CREATE SEQUENCE seq_egwtr_document_type;