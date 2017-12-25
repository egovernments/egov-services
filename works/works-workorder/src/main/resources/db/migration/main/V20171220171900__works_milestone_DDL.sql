create table egw_milestone
(
  id character varying(256) not null,
  tenantid character varying(256) not null,
  letterofacceptanceestimate character varying(100) not null,
  status character varying(100) not null,
  cancellationreason character varying(100),
  cancellationremarks character varying(512),
  deleted boolean default false,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_milestone PRIMARY KEY(id, tenantid)
  );

create table egw_milestoneactivity
(
  id character varying(256) not null,
  tenantid character varying(256) not null,
  milestone character varying(100) not null,
  schedulestartdate bigint,
  scheduleenddate bigint,
  percentage numeric not null,
  description character varying(512) not null,
  stageordernumber character varying(100) not null,
  deleted boolean default false,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_milestoneactivity PRIMARY KEY(id, tenantid)
  );

create table egw_trackmilestone
(
  id character varying(256) not null,
  tenantid character varying(256) not null,
  milestone character varying(100) not null,
  status character varying(100) not null,
  totalPercentage numeric,
  projectCompleted boolean,
  deleted boolean default false,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_trackmilestone PRIMARY KEY(id, tenantid)
  );

create table egw_trackmilestoneactivity
(
  id character varying(256) not null,
  tenantid character varying(256) not null,
  trackmilestone character varying(100) not null,
  milestoneactivity character varying(100) not null,
  remarks character varying(1024),
  percentage numeric not null,
  actualstartdate bigint not null,
  actualenddate bigint,
  status character varying(100) not null,
  deleted boolean default false,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_trackmilestoneactivity PRIMARY KEY(id, tenantid)
  );