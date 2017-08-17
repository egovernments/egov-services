CREATE TABLE egwtr_workorder_audit_log (
	id bigint  NOT NULL,
	waterconnectionid bigint NOT NULL,
	tenantid varchar NOT NULL,
	workordernumber varchar,
	workorderdate varchar,
	watertapownername varchar,
	acknumber varchar,
	acknumberdate varchar,
	hscnumber varchar,
	hscnumberdate varchar,
	servicename varchar,
	plumbername varchar,
	createdby bigint,
	lastmodifiedby bigint ,
	createdtime bigint ,
	lastmodifiedtime bigint ,
 	version numeric DEFAULT 0,
	CONSTRAINT egwtr_workorder_audit_pk PRIMARY KEY (id)
);


create sequence seq_egwtr_workorder_audit_log;

