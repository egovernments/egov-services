CREATE TABLE egwtr_usage_type
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(250),
  parent character varying(100),
  active boolean NOT NULL,
  createdby bigint NOT NULL,
  createddate bigint,
  lastModifiedby bigint NOT NULL,
  lastmodifieddate bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_usage_type PRIMARY KEY (id,tenantid),
  CONSTRAINT unq_usage_code UNIQUE (code,tenantid),
  CONSTRAINT unq_usage_name UNIQUE (name,tenantid),
   CONSTRAINT fk_parent FOREIGN KEY (parent,tenantid) REFERENCES egwtr_usage_type (code,tenantid) 
);
CREATE SEQUENCE seq_egwtr_usage_type;
