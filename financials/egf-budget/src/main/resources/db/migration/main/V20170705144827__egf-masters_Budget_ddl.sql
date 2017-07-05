
Create table egfBudget( 
  id varchar(50),
  name varchar(50),
  ,
  estimationType varchar(50),
  ,
  description varchar(50),
  isActiveBudget boolean,
  isPrimaryBudget boolean,
  materializedPath varchar(50),
  ,
  documentNumber bigint,
  ,
    createdby bigint,
		createddate timestamp without time zone,
		lastmodifiedby bigint,
		lastmodifieddate timestamp without time zone,
		version bigint
);
alter table egfBudget add constraint pk_egfBudget primary key (id);
create sequence seq_egfBudget;
