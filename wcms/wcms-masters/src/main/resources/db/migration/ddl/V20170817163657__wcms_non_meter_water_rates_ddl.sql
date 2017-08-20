CREATE TABLE egwtr_non_meter_water_rates
(
  id bigint NOT NULL,
  code character varying(20) NOT NULL,
  billingtype character varying(50) NOT NULL,
  connectiontype character varying(50) NOT NULL,
  usagetypeid character varying(50) NOT NULL,
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
  CONSTRAINT pk_nonmeter_water_rates PRIMARY KEY (id,tenantid),
  CONSTRAINT fk_nonmeterwaterrates_pipesize FOREIGN KEY (pipesizeid,tenantid) REFERENCES egwtr_pipesize (id,tenantid) ,
  CONSTRAINT fk_nonmeterwaterrates_sourcetype FOREIGN KEY (sourcetypeid,tenantid)  REFERENCES egwtr_water_source_type (id,tenantid) ,
  CONSTRAINT unique_nonmeter_water_rates UNIQUE (code,connectiontype,usagetypeid,sourcetypeid,pipesizeid,fromdate,tenantid),
  CONSTRAINT unique_nonmeter_water_rates_code UNIQUE (code,tenantid)
);

CREATE SEQUENCE seq_egwtr_non_meter_water_rates;