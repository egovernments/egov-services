CREATE TABLE egwtr_meter_water_rates
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  billingtype character varying(50) NOT NULL,
  usagetypeid character varying(50) NOT NULL,
  sourcetypeid bigint NOT NULL,
  pipesizeid bigint NOT NULL,
  fromdate date NOT NULL,
  todate date,
  active boolean NOT NULL,
  createddate timestamp without time zone,
  lastmodifieddate timestamp without time zone,
  createdby bigint,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_meter_water_rates PRIMARY KEY (id,tenantid),
  CONSTRAINT fk_meterwaterrates_pipesize FOREIGN KEY (pipesizeid,tenantid) REFERENCES egwtr_pipesize (id,tenantid) ,
  CONSTRAINT fk_meterwaterrates_sourcetype FOREIGN KEY (sourcetypeid,tenantid)  REFERENCES egwtr_water_source_type (id,tenantid) ,
  CONSTRAINT unique_meter_water_rates UNIQUE (code,usagetypeid,sourcetypeid,pipesizeid,tenantid),
  CONSTRAINT unique_meter_water_rates_code UNIQUE (code,tenantid)
);

CREATE SEQUENCE seq_egwtr_meter_water_rates;
-------------------END-------------------

------------------START------------------
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
CREATE SEQUENCE seq_egwtr_slab;
