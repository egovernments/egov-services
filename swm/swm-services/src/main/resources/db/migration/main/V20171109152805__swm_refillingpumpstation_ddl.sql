Create table egswm_refillingpumpstation(
  code varchar(256) NOT NULL,
  tenantid varchar(128) NOT NULL,
  location varchar(50) NOT NULL,
  name varchar(256) NOT NULL,
  typeofpump varchar(256) NOT NULL,
  remarks varchar(300),
  typeoffuel varchar(256) NOT NULL,
  quantity bigint,
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);
alter table egswm_refillingpumpstation add constraint pk_egswm_refillingpumpstation primary key (code,tenantid);