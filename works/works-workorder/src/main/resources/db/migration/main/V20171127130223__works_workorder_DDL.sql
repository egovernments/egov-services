create table egw_workorder
(
  id character varying(256) not null,
  tenantId character varying(256) not null,
  letterofacceptance character varying(100) not null,
  workOrderDate bigint,
  workordernumber character varying(100) not null,
  status character varying(100) not null,
  stateid character varying(256),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_workorder PRIMARY KEY(id, tenantId)
  );

create table egw_workorder_details
(
  id character varying(256) not null,
  tenantId character varying(256) not null,
  workorder character varying(256) not null,
  remarks character varying(1024) not null,
  editable boolean default true,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_workorderdetails PRIMARY KEY(id, tenantId)
  );