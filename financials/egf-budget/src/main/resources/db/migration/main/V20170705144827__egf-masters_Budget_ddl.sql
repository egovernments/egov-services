
Create table egf_Budget( 
  id varchar(50),
  name varchar(50),
  financialYearId varchar(50),
  parentId varchar(50),
  referenceBudgetId varchar(50),
  statusId varchar(50),
  estimationType varchar(50),
  description varchar(50),
  isActiveBudget boolean,
  isPrimaryBudget boolean,
  materializedPath varchar(50),
  documentNumber bigint,
  createdby varchar(50),
  createddate timestamp without time zone,
  lastmodifiedby varchar(50),
  lastmodifieddate timestamp without time zone,
  tenantId varchar(250),
  version bigint
);
alter table egf_Budget add constraint pk_egf_Budget primary key (id);
create sequence seq_egf_Budget;
