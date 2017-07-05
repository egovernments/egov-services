
Create table egfBudgetReAppropriation( 
  id varchar(50),
  ,
  additionAmount numeric (13,2),
  deductionAmount numeric (13,2),
  originalAdditionAmount numeric (13,2),
  originalDeductionAmount numeric (13,2),
  anticipatoryAmount numeric (13,2),
  ,
  asOnDate date,
    createdby bigint,
		createddate timestamp without time zone,
		lastmodifiedby bigint,
		lastmodifieddate timestamp without time zone,
		version bigint
);
alter table egfBudgetReAppropriation add constraint pk_egfBudgetReAppropriation primary key (id);
create sequence seq_egfBudgetReAppropriation;
