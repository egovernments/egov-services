
Create table egf_Budget( 
  id varchar(50),
  name varchar(50),
  financialYear varchar(50),
  parent varchar(50),
  referenceBudget varchar(50),
  status varchar(50),
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
  version bigint
);
alter table egf_Budget add constraint pk_egf_Budget primary key (id);
create sequence seq_egf_Budget;
