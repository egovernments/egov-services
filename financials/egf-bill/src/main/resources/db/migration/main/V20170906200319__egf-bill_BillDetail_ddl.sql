
Create table egf_billdetail( 
  id varchar(50),
  orderId smallint,
  chartOfAccount varchar(50) NOT NULL,
  debitAmount numeric (13,2) NOT NULL,
  creditAmount numeric (13,2) NOT NULL,
  function varchar(50),
  bill varchar(50),
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  tenantId varchar(250),
  version bigint
);
alter table egf_billdetail add constraint pk_egf_billdetail primary key (id,tenantId);
create sequence seq_egf_billdetail;
