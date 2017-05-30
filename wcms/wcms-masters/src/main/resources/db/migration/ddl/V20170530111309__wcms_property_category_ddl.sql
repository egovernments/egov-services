---Property Type - Category Type Master
CREATE TABLE egwtr_property_category_type
(
  id SERIAL,
  property_type_Id bigint NOT NULL,
  category_type_Id bigint NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_prop_cat_type PRIMARY KEY (id,tenantid),
  FOREIGN KEY (category_type_Id, tenantid) references egwtr_category (id, tenantid) ON DELETE CASCADE ON UPDATE CASCADE

  
);
CREATE SEQUENCE seq_egwtr_property_category_type;