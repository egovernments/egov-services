
Create table egswm_vehicletype( 
  id varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  name varchar(128) NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_vehicletype add constraint pk_egswm_vehicletype primary key (id);
create sequence seq_egswm_vehicletype;
