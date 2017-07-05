
Drop table if exists egwtr_property_usage_type;

---Property Type - Usage Type Master
CREATE TABLE egwtr_property_usage_type
(
  id bigint NOT NULL,
  propertytypeid character varying(50) NOT NULL,
  usagetypeid character varying(50) NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_property_usage_type PRIMARY KEY (id,tenantid),
  CONSTRAINT uc_property_usage_tenant UNIQUE (propertytypeid, usagetypeid, tenantid)
);
