
Create table egf_ledger( 
  id varchar(50),
  orderId smallint,
  chartOfAccountId varchar(50) NOT NULL,
  glcode varchar(50) NOT NULL,
  debitAmount numeric (13,2) NOT NULL,
  creditAmount numeric (13,2) NOT NULL,
  functionId varchar(50),
    VoucherId varchar(50),
		createdby varchar(50),
		createddate timestamp without time zone,
		lastmodifiedby varchar(50),
		lastmodifieddate timestamp without time zone,
		tenantId varchar(250),
		version bigint
);
alter table egf_ledger add constraint pk_egf_ledger primary key (id);
create sequence seq_egf_ledger;
