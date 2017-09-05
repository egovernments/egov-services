Drop table if exists egwtr_donation;

CREATE TABLE egwtr_donation
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  usagetypeid bigint NOT NULL,
  subusagetypeid bigint NOT NULL, 
  categorytypeid bigint NOT NULL,
  maxpipesizeid bigint NOT NULL,
  minpipesizeid bigint NOT NULL,
  fromdate date,
  todate date,
  donationamount double precision NOT NULL,
  outsideulb boolean,
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_donation PRIMARY KEY (id,tenantid),
  CONSTRAINT fk_maxpipesizeid FOREIGN KEY (maxpipesizeid,tenantid) REFERENCES egwtr_pipesize (id,tenantid) ,
  CONSTRAINT fk_minpipesizeid FOREIGN KEY (minpipesizeid,tenantid) REFERENCES egwtr_pipesize (id,tenantid) ,
  CONSTRAINT fk_categorytypeid FOREIGN KEY (categorytypeid, tenantid) references egwtr_category (id, tenantid) ,
  CONSTRAINT fk_usagetypeid FOREIGN KEY (usagetypeid, tenantid) references egwtr_usage_type (id, tenantid) ,
  CONSTRAINT fk_subusagetypeid FOREIGN KEY (subusagetypeid, tenantid) references egwtr_usage_type (id, tenantid) ,
  CONSTRAINT unq_donation UNIQUE (code,categorytypeid,usagetypeid,subusagetypeid,maxpipesizeid,minpipesizeid,tenantid),
  CONSTRAINT unique_donation_code UNIQUE (code,tenantid)
  );
  
