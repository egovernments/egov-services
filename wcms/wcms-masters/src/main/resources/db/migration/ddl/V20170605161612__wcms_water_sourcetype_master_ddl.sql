CREATE TABLE egwtr_water_source_type
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  name character varying(100) NOT NULL,
  description character varying(250) ,
  active boolean ,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint ,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_watersource_type PRIMARY KEY (id,tenantid),
  CONSTRAINT unq_watersource_type_code UNIQUE (code,tenantid),
  CONSTRAINT unq_watersource_type_name UNIQUE (name,tenantid)
);
CREATE SEQUENCE seq_egwtr_water_source_type;