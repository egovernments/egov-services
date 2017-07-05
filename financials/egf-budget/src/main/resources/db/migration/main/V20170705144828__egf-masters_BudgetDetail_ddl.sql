
Create table egfBudgetDetail( 
  id varchar(50),
  ,
  ,
  originalAmount numeric (13,2),
  approvedAmount numeric (13,2),
  budgetAvailable numeric (13,2),
  anticipatoryAmount numeric (13,2),
  ,
  ,
  ,
  ,
  ,
  ,
  ,
  ,
  materializedPath varchar(50),
  ,
  documentNumber bigint,
  uniqueNo varchar(50),
  planningPercent numeric (13,2),
  ,
    createdby bigint,
		createddate timestamp without time zone,
		lastmodifiedby bigint,
		lastmodifieddate timestamp without time zone,
		version bigint
);
alter table egfBudgetDetail add constraint pk_egfBudgetDetail primary key (id);
create sequence seq_egfBudgetDetail;
