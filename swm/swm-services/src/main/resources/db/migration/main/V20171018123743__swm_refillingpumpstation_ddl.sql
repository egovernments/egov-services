
Create table egswm_refillingpumpstation( 
  id varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  ward varchar(50) NOT NULL,
  zoneId varchar(50) NOT NULL,
  street varchar(50) NOT NULL,
  colony varchar(50),
  name varchar(256) NOT NULL,
  type varchar(256) NOT NULL,
  remarks varchar(300),
  typeOfFuel varchar(256) NOT NULL,
  quantity bigint NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_refillingpumpstation add constraint pk_egswm_refillingpumpstation primary key (id);
create sequence seq_egswm_refillingpumpstation;
