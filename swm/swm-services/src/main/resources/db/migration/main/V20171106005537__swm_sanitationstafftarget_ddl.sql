Create table egswm_sanitationstafftarget(
  targetNo varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  swmProcess varchar(256) NOT NULL,
  route varchar(256) NOT NULL,
  employee varchar(256) NOT NULL,
  targetFrom bigint,
  targetTo bigint,
  targetedGarbage numeric (13,2) NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);

Create table egswm_sst_collectionpoints(
  tenantId varchar(128) NOT NULL,
  sanitationstafftarget varchar(256) NOT NULL,
  collectionpoint varchar(256) NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);


alter table egswm_sanitationstafftarget add constraint pk_egswm_sanitationstafftarget primary key (code);
create sequence seq_egswm_sanitationstafftarget;
create sequence seq_egswm_sst_collectionpoints;
