CREATE TABLE egwtr_estimationnotice_audit_log (
	id bigint  NOT NULL,
	waterconnectionid bigint NOT NULL,
	tenantid varchar NOT NULL,
	dateofletter varchar,
	letternumber varchar,
	letterto varchar,
	letterintimationsubject varchar,
	applicationnumber varchar,
	applicationdate varchar,
	applicantname varchar,
	servicename varchar,
	waternumber varchar,
	sladays bigint, 
	penaltychargedescription1 varchar,
	penaltychargedescription2 varchar,
	ulblogo varchar,
	tenantlogo varchar,
	createdby varchar,
	lastmodifiedby varchar ,
	createdtime TIMESTAMP ,
	lastmodifiedtime TIMESTAMP ,
 	version numeric DEFAULT 0,
	CONSTRAINT egwtr_estimationnotice_pk PRIMARY KEY (id)
);


create sequence seq_egwtr_estimationnotice_audit_log;

