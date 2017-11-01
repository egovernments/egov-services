
Create table egswm_routecollectionpointmap( 
  tenantId varchar(128) NOT NULL,
  route varchar(256) NOT NULL,
  collectionpoint varchar(256) NOT NULL,
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  version bigint
);
create sequence seq_egswm_routecollectionpointmap;
