create table egw_letterofacceptance
(
  id character varying(256),
  tenantId character varying(256) not null,
  code character varying(100),
  contractor character varying(100) not null,
  loadate bigint not null,
  loanumber character varying(50) not null,
  contractperiod int not null,
  emdamountdeposited numeric,
  engineerincharge character varying(100) not null,
  defectliabilityperiod int not null,
  loaamount numeric not null,
  status character varying(100) not null,
  tenderfinalizedpercentage double precision not null,
  approveddate bigint,
  filenumber character varying(100) not null,
  filedate bigint not null,
  parent character varying(256),
  stateid character varying(256),
  cancellationreason character varying(100),
  cancellationremarks character varying(512),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_letterofacceptance PRIMARY KEY(id, tenantId),
  constraint uk_egw_letterofacceptance_code_tenantId UNIQUE (tenantId, code)
  );

create table egw_letterofacceptanceestimate
(
  id character varying(256) not null,
  tenantId character varying(256) not null,
  letterofacceptance character varying(100) not null,
  detailedestimate character varying(100) not null,
  workcompletiondate bigint,
  estimateloaamount numeric,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_letterofacceptanceestimate PRIMARY KEY(id, tenantId)
  );

create table egw_securitydeposit
(
  id character varying(256) not null,
  tenantId character varying(256) not null,
  letterofacceptance character varying(100) not null,
  collectionmode character varying(50) not null,
  percentage double precision not null,
  amount numeric not null,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_securitydeposit PRIMARY KEY(id, tenantId)
  );

create table egw_assetsforloa
(
  id character varying(256) not null,
  tenantId character varying(256) not null,
  asset character varying(100) not null,
  letterofacceptanceestimate character varying(100) not null,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_assetsforloa PRIMARY KEY(id, tenantId)
  );

create table egw_loaactivity
(
  id character varying(256) not null,
  tenantId character varying(256) not null,
  asset character varying(100) not null,
  letterofacceptanceestimate character varying(100) not null,
  estimateactivity character varying(100) not null,
  parent character varying(100),
  approvedrate numeric,
  approvedquantity numeric,
  approvedamount numeric,
  remarks character varying(1024),
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_loaactivity PRIMARY KEY(id, tenantId)
  );

create table egw_loameasurementsheet
(
  id character varying(256) not null,
  tenantId character varying(256) not null,
  number numeric,
  length numeric,
  width numeric,
  depthorheight numeric,
  quantity numeric not null,
  loaactivity character varying(100) not null,
  estimatemeasurementsheet character varying(100) not null,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_loameasurementsheet PRIMARY KEY(id, tenantId)
  );
