
Create table egf_BudgetReAppropriation( 
  id varchar(50),
  budgetDetail varchar(50),
  status varchar(50),
  additionAmount numeric (13,2),
  deductionAmount numeric (13,2),
  originalAdditionAmount numeric (13,2),
  originalDeductionAmount numeric (13,2),
  anticipatoryAmount numeric (13,2),
  asOnDate date,
  createdby varchar(50),
  createddate timestamp without time zone,
  lastmodifiedby varchar(50),
  lastmodifieddate timestamp without time zone,
  version bigint
);
alter table egf_BudgetReAppropriation add constraint pk_egf_BudgetReAppropriation primary key (id);
create sequence seq_egf_BudgetReAppropriation;
