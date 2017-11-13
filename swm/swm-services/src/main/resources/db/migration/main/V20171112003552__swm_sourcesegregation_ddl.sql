Create table egswm_sourcesegregation(
  tenantId varchar(128) NOT NULL,
  code varchar(256) NOT NULL,
  dumpingGround varchar(256) NOT NULL,
  sourceSegregationDate bigint NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);


alter table egswm_sourcesegregation add constraint pk_egswm_sourcesegregation primary key (code,tenantId);

Create table egswm_collectiondetails(
  tenantId varchar(128) NOT NULL,
  id varchar(256) NOT NULL,
  sourcesegregation varchar(256) NOT NULL,
  collectionType varchar(256) NOT NULL,
  wetWasteCollected NUMERIC(13,2) NOT NULL,
  dryWasteCollected NUMERIC(13,2) NOT NULL,
  version bigint
);