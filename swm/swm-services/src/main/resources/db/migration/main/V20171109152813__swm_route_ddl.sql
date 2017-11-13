
Create table egswm_route( 
  code varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  name varchar(128) NOT NULL,
  collectionType varchar(256) NOT NULL,
  startingCollectionPoint varchar(256) NOT NULL,
  endingCollectionPoint varchar(256) ,
  endingDumpingGroundPoint varchar(256) ,
  distance numeric NOT NULL,
  garbageEstimate numeric NOT NULL,
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_route add constraint pk_egswm_route primary key (code,tenantId);


Create table egswm_routecollectionpointmap( 
  tenantId varchar(128) NOT NULL,
  route varchar(256) NOT NULL,
  collectionpoint varchar(256) NOT NULL,
  version bigint
);