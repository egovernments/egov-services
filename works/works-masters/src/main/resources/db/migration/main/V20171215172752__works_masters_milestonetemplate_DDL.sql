create table egw_milestonetemplate
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  code character varying(100) NOT NULL,
  name character varying(100) NOT NULL,
  active boolean NOT NULL,
  description character varying(1024) NOT NULL,
  typeofwork  character varying(100),
  subtypeofwork  character varying(100),
  deleted boolean DEFAULT false,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_milestonetemplate PRIMARY  KEY(id,tenantId),
  constraint uk_egw_milestonetemplate_code_tenantId UNIQUE (tenantId, code)
  );

create table egw_milestonetemplateactivities
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  milestonetemplate character varying(100),
  stageordernumber numeric,
  stagedescription character varying(1024),
  percentage numeric,
  deleted boolean DEFAULT false,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_milestonetemplateactivities PRIMARY  KEY(id, tenantId)
  );