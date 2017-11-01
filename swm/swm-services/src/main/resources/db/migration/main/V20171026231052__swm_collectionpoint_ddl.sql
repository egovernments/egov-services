
Create table egswm_collectionpoint( 
  code varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  name varchar(50) NOT NULL,
  location varchar(256),
  latitude NUMERIC,
  longitude NUMERIC,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);

alter table egswm_collectionpoint add constraint pk_egswm_collectionpoint primary key (code);
create sequence seq_egswm_collectionpoint;


Create table egswm_bindetails( 
  id varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  collectionPoint varchar(50) NOT NULL,
  assetOrBinId varchar(256),
  rfidAssigned boolean,
  rfid varchar(256),
  version bigint
);

alter table egswm_bindetails add constraint pk_egswm_bindetails primary key (id);
create sequence seq_egswm_bindetails;

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

