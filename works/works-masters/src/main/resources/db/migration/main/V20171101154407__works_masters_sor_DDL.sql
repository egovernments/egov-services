create table egw_scheduleofrate
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  code character varying(100),
  description character varying(4000) NOT NULL,
  schedulecategory character varying(100),
  uom character varying(256),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_scheduleofrate PRIMARY  KEY(id,tenantId),
  constraint uk_egw_scheduleofrate_code_tenantId_category UNIQUE (tenantId, code, schedulecategory)
  );

create sequence seq_egw_scheduleofrate;

create table egw_sorrate
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  schedulerofrate character varying(100),
  fromdate bigint NOT NULL,
  todate bigint,
  rate numeric,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_sorrate PRIMARY  KEY(id,tenantId),
  constraint uk_egw_sorrate_code_tenantId_fromdate_todate UNIQUE (tenantId, schedulerofrate, fromdate, todate)
  );

create sequence seq_egw_sorrate;

create table egw_marketrate
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  schedulerofrate character varying(100),
  fromdate bigint NOT NULL,
  todate bigint,
  rate numeric,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_marketrate PRIMARY  KEY(id,tenantId),
  constraint uk_egw_marketrate_code_tenantId_fromdate_todate UNIQUE (tenantId, schedulerofrate, fromdate, todate)
  );

create sequence seq_egw_marketrate;