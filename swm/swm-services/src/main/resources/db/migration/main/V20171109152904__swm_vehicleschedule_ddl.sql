Create table egswm_vehicleschedule(
  transactionNo varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  scheduledFrom bigint NOT NULL,
  scheduledTo bigint NOT NULL,
  route varchar(256) NOT NULL,
  vehicle varchar(256) NOT NULL,
  targetedGarbage numeric (13,2) NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);


alter table egswm_vehicleschedule add constraint pk_egswm_vehicleschedule primary key (transactionNo,tenantId);
