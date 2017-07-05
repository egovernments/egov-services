
Create table egf_BudgetDetail( 
  id varchar(50),
  budgetGroup varchar(50),
  budget varchar(50),
  usingDepartment varchar(50),
  executingDepartment varchar(50),
  function varchar(50),
  scheme varchar(50),
  fund varchar(50),
  subScheme varchar(50),
  functionary varchar(50),
  boundary varchar(50),
  status varchar(50),
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
  version bigint
);
alter table egf_BudgetDetail add constraint pk_egf_BudgetDetail primary key (id);
create sequence seq_egf_BudgetDetail;
