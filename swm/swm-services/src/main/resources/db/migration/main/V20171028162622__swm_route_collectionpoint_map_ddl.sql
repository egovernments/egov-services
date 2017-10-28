
Create table egswm_routecollectionpointmap( 
  id varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  route varchar(256) NOT NULL,
  collectionpoint varchar(256) NOT NULL,
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_routecollectionpointmap add constraint pk_egswm_routecollectionpointmap primary key (id);
create sequence seq_egswm_routecollectionpointmap;
