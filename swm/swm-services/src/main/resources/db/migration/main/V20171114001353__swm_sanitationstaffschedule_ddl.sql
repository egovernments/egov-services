Create table egswm_sanitationstaffschedule(
  tenantId varchar(128) NOT NULL,
  transactionNo varchar(256) NOT NULL,
  sanitationStaffTarget varchar(256) NOT NULL,
  shift varchar(256) NOT NULL,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);


alter table egswm_sanitationstaffschedule add constraint pk_egswm_sanitationstaffschedule primary key (transactionNo,tenantId);
