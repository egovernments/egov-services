alter table egwtr_waterConnection add column estimationNumber character varying(200);

alter table egwtr_waterConnection add column workOrderNumber character varying(200);

alter table egwtr_waterConnection drop column demandid ;

CREATE TABLE egwtr_demand_connection
(
  id bigint NOT NULL,
  demandid character varying(64) NOT NULL,
  connectionid bigint NOT NULL,
  createdby character varying NOT NULL,
  lastmodifiedby character varying NOT NULL,
  createdtime timestamp without time zone NOT NULL,
  lastmodifiedtime timestamp without time zone NOT NULL,
  tenantid character varying NOT NULL,
  	CONSTRAINT pk_demand_connection_pkey PRIMARY KEY (id),
	CONSTRAINT fk_demand_connection FOREIGN KEY (connectionid,tenantid)
      REFERENCES egwtr_waterconnection (id,tenantid) );


create sequence seq_egwtr_demand_connection;