---Meter Cost Master
CREATE TABLE egwtr_metercost
(
  id bigint NOT NULL,
  pipesize bigint NOT NULL,
  metermake character varying(50) NOT NULL,
  amount double precision NOT NULL,
  active boolean NOT NULL,
  createddate timestamp without time zone ,
  lastmodifieddate timestamp without time zone,
  createdby bigint NOT NULL,
  lastmodifiedby bigint,
  tenantid CHARACTER VARYING(250) NOT NULL,
  version numeric DEFAULT 0,
  CONSTRAINT pk_metercost PRIMARY KEY (id,tenantid),
  CONSTRAINT fk_metercost_pipesize FOREIGN KEY (pipesize,tenantid)
      REFERENCES egwtr_pipesize);

CREATE SEQUENCE seq_egwtr_meter_cost;