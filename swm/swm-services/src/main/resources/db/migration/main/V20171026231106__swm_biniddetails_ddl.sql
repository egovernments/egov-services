
Create table egswm_biniddetails( 
  id varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  collectionPointId varchar(50) NOT NULL,
  assetOrBinId varchar(256),
  rfidAssigned boolean,
  rfid varchar(256),
  latitude NUMERIC,
  longitude NUMERIC,
  version bigint
);
alter table egswm_biniddetails add constraint pk_egswm_biniddetails primary key (id);
create sequence seq_egswm_biniddetails;
