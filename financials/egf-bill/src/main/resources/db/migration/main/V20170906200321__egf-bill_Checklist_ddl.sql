
Create table egf_checklist( 
  code varchar(256),
  type varchar(50),
  subType varchar(50),
  key varchar(256),
  description varchar(250),
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  tenantId varchar(256),
  version bigint
);
alter table egf_checklist add constraint pk_egf_checklist primary key (code,tenantId);
create sequence seq_egf_checklist;
