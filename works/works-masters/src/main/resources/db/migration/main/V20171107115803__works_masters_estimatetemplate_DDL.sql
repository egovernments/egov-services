create table egw_estimatetemplate
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  code character varying(100) NOT NULL,
  name character varying(100) NOT NULL,
  active boolean NOT NULL,
  description character varying(1024) NOT NULL,
  typeofwork  character varying(100),
  subtypeofwork  character varying(100),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_estimatetemplate PRIMARY  KEY(id,tenantId),
  constraint uk_egw_estimatetemplate_code_tenantId UNIQUE (tenantId, code)
  );

create table egw_estimatetemplateactivities
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  estimatetemplate character varying(100),
  schedulerofrate character varying(100),
  uom character varying(30),
  nonsor  character varying(256),
  unitrate numeric,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_estimatetemplatetctivities PRIMARY  KEY(id, tenantId)
  );

create table egw_nonsor
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  description character varying(100),
  uom character varying(30),
  constraint pk_egw_nonsor PRIMARY  KEY(id,tenantId)
  );