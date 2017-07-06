
Create table egf_BudgetDetail( 
  id varchar(50),
  budgetGroupId varchar(50),
  budgetId varchar(50),
  usingDepartmentId varchar(50),
  executingDepartmentId varchar(50),
  functionId varchar(50),
  schemeId varchar(50),
  fundId varchar(50),
  subSchemeId varchar(50),
  functionaryId varchar(50),
  boundaryId varchar(50),
  statusId varchar(50),
  originalAmount numeric (13,2),
  approvedAmount numeric (13,2),
  budgetAvailable numeric (13,2),
  anticipatoryAmount numeric (13,2),
  materializedPath varchar(50),
  documentNumber bigint,
  uniqueNo varchar(50),
  planningPercent numeric (13,2),
  createdby varchar(50),
  createddate timestamp without time zone,
  lastmodifiedby varchar(50),
  lastmodifieddate timestamp without time zone,
  tenantId varchar(250),
  version bigint
);
alter table egf_BudgetDetail add constraint pk_egf_BudgetDetail primary key (id);
create sequence seq_egf_BudgetDetail;
