
Create table egf_budgetdetail( 
  id varchar(50),
  budgetId varchar(50) NOT NULL,
  budgetGroupId varchar(50) NOT NULL,
  usingDepartmentId varchar(50),
  executingDepartmentId varchar(50),
  fundId varchar(50),
  functionId varchar(50),
  schemeId varchar(50),
  subSchemeId varchar(50),
  functionaryId varchar(50),
  boundaryId varchar(50),
  anticipatoryAmount numeric (13,2) NOT NULL,
  originalAmount numeric (13,2) NOT NULL,
  approvedAmount numeric (13,2) NOT NULL,
  planningPercent numeric (13,2) NOT NULL,
  budgetAvailable numeric (13,2),
  statusId varchar(50),
  documentNumber varchar(50),
  uniqueNo varchar(50),
  materializedPath varchar(50),
  createdby varchar(50),
  createddate timestamp without time zone,
  lastmodifiedby varchar(50),
  lastmodifieddate timestamp without time zone,
  tenantId varchar(250) NOT NULL,
  version bigint
);
alter table egf_budgetdetail add constraint pk_egf_budgetdetail primary key (id);
create sequence seq_egf_budgetdetail;
