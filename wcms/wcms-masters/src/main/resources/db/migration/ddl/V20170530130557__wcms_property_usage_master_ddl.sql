---Property Type - Usage Type Master
CREATE TABLE egwtr_property_usage_type
(
  id SERIAL,
  property_type bigint NOT NULL,
  usage_type bigint NOT NULL,
  description character varying(250),
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_property_usage_type PRIMARY KEY (id,tenantid),
  CONSTRAINT uc_property_usage_tenant UNIQUE (property_type, usage_type, tenantid)
);
CREATE SEQUENCE seq_egwtr_property_usage_type;