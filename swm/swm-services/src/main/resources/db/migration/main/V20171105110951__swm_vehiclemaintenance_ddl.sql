Create table egswm_vehiclemaintenance(
  code varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  vehicle varchar(256) NOT NULL,
  maintenanceAfterDays bigint,
  maintenanceAfterKm bigint,
  downtimeforMaintenance numeric (13,2) NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);


alter table egswm_vehiclemaintenance add constraint pk_egswm_vehiclemaintenance primary key (code);
create sequence seq_egswm_vehiclemaintenance;
