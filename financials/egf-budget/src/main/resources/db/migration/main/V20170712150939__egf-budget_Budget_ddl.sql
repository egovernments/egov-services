
Create table egf_budget( 
  id varchar(50),
  name varchar(250) NOT NULL,
  financialYearId varchar(50) NOT NULL,
  estimationType varchar(4) NOT NULL,
  parentId varchar(50),
  active boolean,
  primaryBudget boolean NOT NULL,
  referenceBudgetId varchar(50),
  statusId varchar(50),
  documentNumber varchar(50),
  description varchar(250),
  materializedPath varchar(25),
  createdby varchar(50),
  createddate timestamp without time zone,
  lastmodifiedby varchar(50),
  lastmodifieddate timestamp without time zone,
  tenantId varchar(250) NOT NULL,
  version bigint
);
alter table egf_budget add constraint pk_egf_budget primary key (id);
create sequence seq_egf_budget;
