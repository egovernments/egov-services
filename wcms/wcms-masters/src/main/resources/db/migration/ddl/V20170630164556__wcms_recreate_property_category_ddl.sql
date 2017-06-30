drop table if exists egwtr_property_category_type;

CREATE TABLE egwtr_property_category_type
(
  id bigint NOT NULL,
  propertytypeid character varying(50) NOT NULL,
  categorytypeid bigint NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid character varying(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_prop_cat_type PRIMARY KEY (id,tenantid),
  CONSTRAINT unq_property_category UNIQUE (categorytypeid,propertytypeid,tenantid),
  FOREIGN KEY (categorytypeid, tenantid) references egwtr_category (id, tenantid) 
);