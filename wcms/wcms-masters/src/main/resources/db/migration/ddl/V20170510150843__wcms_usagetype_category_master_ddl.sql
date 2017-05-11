---Usage Type Master
CREATE TABLE egwtr_usage_type
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(250),
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_usage_type PRIMARY KEY (id,tenantid),
  CONSTRAINT unq_usage_code UNIQUE (code,tenantid),
  CONSTRAINT unq_usage_name UNIQUE (name,tenantid)
);
CREATE SEQUENCE seq_egwtr_usage_type;

---Category Master

CREATE TABLE egwtr_category
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(250),
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint ,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_category PRIMARY KEY (id,tenantid),
  CONSTRAINT unq_category_code UNIQUE (code,tenantid),
  CONSTRAINT unq_category_name UNIQUE (name,tenantid)
);
CREATE SEQUENCE seq_egwtr_category;