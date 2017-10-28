
Create table egswm_collectionpointdetails( 
  id varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  collectionType varchar(256) NOT NULL,
  collectionPoint varchar(256) NOT NULL,
  garbageEstimate numeric NOT NULL,
  description varchar(300),
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_collectionpointdetails add constraint pk_egswm_collectionpointdetails primary key (id);
create sequence seq_egswm_collectionpointdetails;
