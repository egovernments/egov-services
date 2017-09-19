Drop table if exists egwtr_slab;
Drop table if exists egwtr_meter_water_rates;
Drop table if exists egwtr_non_meter_water_rates;

CREATE TABLE egwtr_meter_water_rates
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  billingtype character varying(50) NOT NULL,
  usagetypeid bigint NOT NULL,
  subusagetypeid bigint NOT NULL,
  sourcetypeid bigint NOT NULL,
  pipesizeid bigint NOT NULL,
  fromdate bigint,
  todate bigint,
  active boolean NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  outsideulb boolean,
  CONSTRAINT pk_meter_water_rates PRIMARY KEY (id,tenantid),
  CONSTRAINT fk_meterwaterrates_pipesize FOREIGN KEY (pipesizeid,tenantid) REFERENCES egwtr_pipesize (id,tenantid) ,
  CONSTRAINT fk_meterwaterrates_sourcetype FOREIGN KEY (sourcetypeid,tenantid)  REFERENCES egwtr_water_source_type (id,tenantid) ,
  CONSTRAINT unique_meter_water_rates UNIQUE (code,usagetypeid,subusagetypeid,sourcetypeid,pipesizeid,tenantid),
  CONSTRAINT unique_meter_water_rates_code UNIQUE (code,tenantid),
  CONSTRAINT fk_meterwaterrates_usagetype FOREIGN KEY (usagetypeid, tenantid) REFERENCES egwtr_usage_type (id, tenantid),
  CONSTRAINT fk_meterwaterrates_subusagetype FOREIGN KEY (subusagetypeid, tenantid) REFERENCES egwtr_usage_type (id, tenantid)
);


CREATE TABLE egwtr_slab
(
  id bigint NOT NULL,
  meterwaterratesid bigint NOT NULL,
  fromunit numeric,
  tounit numeric,
  unitrate double precision,
  tenantid CHARACTER VARYING(250) NOT NULL,
  CONSTRAINT pk_slab PRIMARY KEY (id,tenantid),
  CONSTRAINT fk_meterwaterratesid FOREIGN KEY (meterwaterratesid,tenantid) REFERENCES egwtr_meter_water_rates (id,tenantid) 
);



CREATE TABLE egwtr_non_meter_water_rates
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  billingtype character varying(50) NOT NULL,
  connectiontype character varying(50) NOT NULL,
  usagetypeid bigint NOT NULL,
  subusagetypeid bigint NOT NULL,
  sourcetypeid bigint NOT NULL,
  pipesizeid bigint NOT NULL,
  fromdate bigint NOT NULL,
  amount double precision NOT NULL,
  nooftaps bigint NOT NULL,
  active boolean NOT NULL,
  createddate bigint,
  lastmodifieddate bigint,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid character varying(250) NOT NULL,
  version numeric DEFAULT 0,  
  outsideulb boolean,
  CONSTRAINT pk_nonmeter_water_rates PRIMARY KEY (id,tenantid),
  CONSTRAINT fk_nonmeterwaterrates_pipesize FOREIGN KEY (pipesizeid,tenantid) REFERENCES egwtr_pipesize (id,tenantid) ,
  CONSTRAINT fk_nonmeterwaterrates_sourcetype FOREIGN KEY (sourcetypeid,tenantid)  REFERENCES egwtr_water_source_type (id,tenantid) ,
  CONSTRAINT unique_nonmeter_water_rates UNIQUE (code,connectiontype,usagetypeid,subusagetypeid,sourcetypeid,pipesizeid,fromdate,tenantid),
  CONSTRAINT unique_nonmeter_water_rates_code UNIQUE (code,tenantid),
  CONSTRAINT fk_nonmeterwaterrates_usagetype FOREIGN KEY (usagetypeid, tenantid) REFERENCES egwtr_usage_type (id, tenantid),
  CONSTRAINT fk_nonmeterwaterrates_subusagetype FOREIGN KEY (subusagetypeid, tenantid) REFERENCES egwtr_usage_type (id, tenantid)
);

