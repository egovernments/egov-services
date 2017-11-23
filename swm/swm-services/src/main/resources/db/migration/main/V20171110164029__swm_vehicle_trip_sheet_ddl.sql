Create table egswm_vehicletripsheetdetails(
  tenantId varchar(128) NOT NULL,
  tripNo varchar(256) NOT NULL,
  vehicle varchar(256) NOT NULL,
  route varchar(256) NOT NULL,
  tripStartDate bigint NOT NULL,
  tripEndDate bigint NOT NULL,
  inTime bigint,
  outTime bigint,
  entryWeight NUMERIC(13,2),
  exitWeight NUMERIC(13,2),
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);


alter table egswm_vehicletripsheetdetails add constraint pk_egswm_vehicletripsheetdetails primary key (tripNo,tenantId);