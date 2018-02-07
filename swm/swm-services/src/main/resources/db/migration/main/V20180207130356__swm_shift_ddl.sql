Create table egswm_shift( 
  code varchar(256) NOT NULL,
  tenantId varchar(256) NOT NULL,
  shiftType varchar(256) NOT NULL,
  department varchar(256) NOT NULL,
  designation varchar(256) NOT NULL,
  shiftStartTime bigint NOT NULL,
  shiftEndTime bigint NOT NULL,
  lunchStartTime bigint NOT NULL,
  lunchEndTime bigint NOT NULL,
  graceTimeFrom bigint NOT NULL,
  graceTimeTo bigint NOT NULL,
  remarks varchar(300),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);

alter table egswm_shift add constraint pk_egswm_shift primary key (code,tenantId);