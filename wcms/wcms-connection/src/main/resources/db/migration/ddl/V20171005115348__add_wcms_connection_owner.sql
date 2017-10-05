CREATE SEQUENCE seq_egwtr_connection_owners;

CREATE TABLE egwtr_connection_owners (
	id bigint NOT NULL,
	waterconnectionid bigint NOT NULL,
	ownerid bigint NOT NULL,
	primaryowner boolean default true,
	ordernumber numeric DEFAULT 1, 
	tenantid varchar NOT NULL,
	createdby bigint,
	lastmodifiedby bigint ,
	createdtime bigint ,
	lastmodifiedtime bigint ,
 	version numeric DEFAULT 0,
	CONSTRAINT egwtr_connection_owners_pk PRIMARY KEY (id),
	CONSTRAINT unq_connection_owner UNIQUE (waterconnectionid,ownerid,tenantid),
	CONSTRAINT fk_connection_tenant FOREIGN KEY (waterconnectionid,tenantid) REFERENCES egwtr_waterconnection (id,tenantid)
);

alter table egwtr_waterconnection drop column isprimaryowner;


