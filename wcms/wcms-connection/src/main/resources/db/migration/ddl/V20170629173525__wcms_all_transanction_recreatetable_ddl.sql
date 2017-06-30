drop table if exists egwtr_meterreading;

drop table if exists egwtr_meter;

drop table if exists egwtr_estimationcharge;

drop table if exists egwtr_workorder;


drop table if exists egwtr_timeline;

drop table if exists egwtr_documentowner;


drop table if exists egwtr_waterconnection;

drop sequence if exists seq_egwtr_waterconnection;




CREATE TABLE egwtr_waterconnection
(
  id bigint  NOT NULL ,
  tenantid character varying NOT NULL,
  connectiontype character varying NOT NULL,--TEMPORARY,PERMANANT
  billingtype character varying NOT NULL,--METERED,NON_METERED
  connectionstatus character varying(100),--INPROGRESS,ACTIVE
  applicationtype character varying(100) NOT NULL,--NEWCONNECTION,ADDITIONALCONNECTION,CHANGEOFUSAGE......
  sumpcapacity bigint,
  donationcharge bigint NOT NULL DEFAULT 0,
  numberofftaps bigint ,
  supplytype character varying(100) NOT NULL,
  categorytype character varying(100) NOT NULL,
  hscpipesizetype character varying(100) NOT NULL,
  sourcetype character varying(100) NOT NULL,
  numberofpersons bigint ,
  parentconnectionid bigint,
  waterTreatmentId character varying(100) NOT NULL,
  legacyconsumernumber character varying(250),
  islegacy boolean not null,
  status bigint NOT NULL,
  stateId bigint NOT NULL,
  acknowledgmentnumber character varying (250) NOT NULL,
  consumernumber character varying(250),
  propertyidentifier character varying(100) ,
  usagetype character varying(100) NOT NULL,
  propertytype character varying(100) NOT NULL,
  address character varying(250) NOT NULL,
  assetidentifier character varying(100),
  createdby character varying NOT NULL,
  lastmodifiedby character varying NOT NULL,
  createdtime timestamp without time zone NOT NULL,
  lastmodifiedtime timestamp without time zone NOT NULL,
  CONSTRAINT egwtr_waterconnection_pk PRIMARY KEY (id, tenantid)
);


create sequence seq_egwtr_waterconnection;





CREATE TABLE egwtr_workorder (
	id bigint  NOT NULL,
	connectionid bigint NOT NULL,
	remarks TEXT NOT NULL,
	tenantid varchar NOT NULL,
	createdby varchar NOT NULL,
	lastmodifiedby varchar NOT NULL,
	createdtime TIMESTAMP NOT NULL,
	lastmodifiedtime TIMESTAMP NOT NULL,
	CONSTRAINT egwtr_workorder_pk PRIMARY KEY (id),
CONSTRAINT fk_conn_workorder FOREIGN KEY (connectionid,tenantid)
      REFERENCES egwtr_waterconnection (id,tenantid));

drop sequence if exists seq_egwtr_workorder;
create sequence seq_egwtr_workorder;

CREATE TABLE egwtr_meter (
	id bigint  NOT NULL,
	metermake varchar NOT NULL,
	initialMeterReading varchar NOT NULL,
	tenantid varchar NOT NULL,
	connectionid bigint NOT NULL,
	createdby varchar NOT NULL,
	lastmodifiedby varchar NOT NULL,
	createdtime TIMESTAMP NOT NULL,
	lastmodifiedtime TIMESTAMP NOT NULL,
	CONSTRAINT egwtr_meter_pk PRIMARY KEY (id),
CONSTRAINT fk_conmeter_meter FOREIGN KEY (connectionid,tenantid)
      REFERENCES egwtr_waterconnection (id,tenantid)

);

drop sequence if exists seq_egwtr_meter;
create sequence seq_egwtr_meter;




CREATE TABLE egwtr_meterreading (
	id bigint  NOT NULL,
	meterId bigint NOT NULL,
	reading bigint NOT NULL,
	tenantid varchar NOT NULL,
	createdby varchar NOT NULL,
	lastmodifiedby varchar NOT NULL,
	createdtime TIMESTAMP NOT NULL,
	lastmodifiedtime TIMESTAMP NOT NULL,
	CONSTRAINT egwtr_meterreading_pk PRIMARY KEY (id)
,CONSTRAINT fk_meterread_meter FOREIGN KEY (meterId)
      REFERENCES egwtr_meter (id));



drop sequence if exists seq_egwtr_meterreading;
create sequence seq_egwtr_meterreading;

CREATE TABLE egwtr_timeline (
	id bigint  NOT NULL,
	connectionid bigint NOT NULL,
	remarks TEXT NOT NULL,
	assigner bigint NOT NULL,
	assignee bigint NOT NULL,
	tenantid varchar NOT NULL,
	createdby varchar NOT NULL,
	lastmodifiedby varchar NOT NULL,
	createdtime TIMESTAMP NOT NULL,
	lastmodifiedtime TIMESTAMP NOT NULL,
	department bigint NOT NULL,
	designation bigint NOT NULL,
	approver bigint NOT NULL,
	comments TEXT NOT NULL,
	status TEXT NOT NULL,
	CONSTRAINT egwtr_timeline_pk PRIMARY KEY (id)
,CONSTRAINT fk_conn_timeline FOREIGN KEY (connectionid,tenantid)
      REFERENCES egwtr_waterconnection (id,tenantid)
);


drop sequence if exists seq_egwtr_timeline;
create sequence seq_egwtr_timeline;



CREATE TABLE egwtr_documentowner (
    id bigint  NOT NULL,
    document bigint NOT NULL,
	name varchar NOT NULL,
	fileStoreId varchar NOT NULL,
	connectionId bigint  NOT NULL,
	tenantid varchar NOT NULL,
	CONSTRAINT egwtr_documentowner_pk PRIMARY KEY (id),
CONSTRAINT fk_conn_documentowner FOREIGN KEY (connectionid,tenantid)
      REFERENCES egwtr_waterconnection (id,tenantid)
);


drop sequence if exists seq_egwtr_documentowner;

create sequence seq_egwtr_documentowner;


CREATE TABLE egwtr_estimationcharge (
	id bigint  NOT NULL,
	connectionid bigint NOT NULL,
	material bigint NOT NULL,
	existingdistributionpipeline varchar NOT NULL,
	pipelinetohomedistance bigint NOT NULL,
	estimationcharges bigint NOT NULL,
	supervisioncharges bigint NOT NULL,
	materialcharges bigint NOT NULL,
	tenantId varchar NOT NULL,
	createdby varchar NOT NULL,
	lastmodifiedby varchar NOT NULL,
	createdtime TIMESTAMP NOT NULL,
	lastmodifiedtime TIMESTAMP NOT NULL,
	CONSTRAINT egwtr_estimationcharge_pk PRIMARY KEY (id)
,CONSTRAINT fk_conn_estimation FOREIGN KEY (connectionid,tenantid)
      REFERENCES egwtr_waterconnection (id,tenantid)
);

drop sequence if exists seq_egwtr_estimationcharge;

create sequence seq_egwtr_estimationcharge;

