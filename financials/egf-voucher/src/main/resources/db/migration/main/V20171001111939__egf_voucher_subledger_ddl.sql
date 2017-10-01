drop table egf_ledgerdetail;

drop sequence seq_egf_ledgerdetail;

create table egf_subledger( 
  id varchar(50),
  accountDetailTypeId varchar(50),
  accountDetailKeyId varchar(50) NOT NULL,
  amount numeric (13,2) NOT NULL,
  ledgerId varchar(50),
  createdby varchar(50),
  createddate timestamp without time zone,
  lastmodifiedby varchar(50),
  lastmodifieddate timestamp without time zone,
  tenantId varchar(250),
  version bigint
);

alter table egf_subledger add constraint pk_egf_subledger primary key (id);

create sequence seq_egf_subledger;

