
Create table egswm_collectionpoint( 
  id varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  name varchar(50) NOT NULL,
  ward varchar(256) NOT NULL,
  zoneCode varchar(256) NOT NULL,
  street varchar(256) NOT NULL,
  colony varchar(256),
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_collectionpoint add constraint pk_egswm_collectionpoint primary key (id);
create sequence seq_egswm_collectionpoint;
