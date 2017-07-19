
Create table egf_budgetreappropriation( 
  id varchar(50),
  budgetDetailId varchar(50) NOT NULL,
  additionAmount numeric (13,2),
  deductionAmount numeric (13,2),
  originalAdditionAmount numeric (13,2),
  originalDeductionAmount numeric (13,2),
  anticipatoryAmount numeric (13,2),
  statusId varchar(50),
  asOnDate date,
  createdby varchar(50),
  createddate timestamp without time zone,
  lastmodifiedby varchar(50),
  lastmodifieddate timestamp without time zone,
  tenantId varchar(250) NOT NULL,
  version bigint
);
alter table egf_budgetreappropriation add constraint pk_egf_budgetreappropriation primary key (id);
create sequence seq_egf_budgetreappropriation;
