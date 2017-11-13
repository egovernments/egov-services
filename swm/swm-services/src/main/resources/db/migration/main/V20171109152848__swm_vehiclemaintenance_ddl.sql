Create table egswm_vehiclemaintenance(
  code varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  vehicle varchar(256) NOT NULL,
  maintenanceUom varchar(10) NOT NULL,
  maintenanceAfter bigint NOT NULL,
  downtimeforMaintenanceUom varchar(10),
  downtimeforMaintenance bigint NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);


alter table egswm_vehiclemaintenance add constraint pk_egswm_vehiclemaintenance primary key (code,tenantId);