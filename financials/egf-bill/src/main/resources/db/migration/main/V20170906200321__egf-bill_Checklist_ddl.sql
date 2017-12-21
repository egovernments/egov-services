
Create table egf_checklist( 
  code varchar(50),
  type varchar(50),
  subType varchar(50),
  key varchar(250),
  description varchar(250),
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  tenantId varchar(250),
  version bigint
);
alter table egf_checklist add constraint pk_egf_checklist primary key (code,tenantId);
create sequence seq_egf_checklist;
