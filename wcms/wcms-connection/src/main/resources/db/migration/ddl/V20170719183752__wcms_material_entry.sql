CREATE TABLE egwtr_material (
	id bigint  NOT NULL,
	estimationChargeId bigint NOT NULL,
	name varchar NOT NULL,
	quantity double precision ,
	size double precision,
	amountDetails double precision,
	tenantId varchar NOT NULL,
	createdby varchar,
	lastmodifiedby varchar ,
	createdtime TIMESTAMP ,
	lastmodifiedtime TIMESTAMP ,
 	version numeric DEFAULT 0,
	CONSTRAINT egwtr_material_pk PRIMARY KEY (id)
,CONSTRAINT fk_conn_material FOREIGN KEY (estimationChargeId)
      REFERENCES egwtr_estimationcharge (id)
);


create sequence seq_egwtr_material;

alter table egwtr_estimationcharge drop column material;
