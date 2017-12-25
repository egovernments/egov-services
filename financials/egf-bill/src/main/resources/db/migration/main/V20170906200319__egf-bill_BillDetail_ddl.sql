
Create table egf_billdetail( 
  id varchar(256),
  orderId smallint,
  chartOfAccount varchar(256) NOT NULL,
  debitAmount numeric (13,2) NOT NULL,
  creditAmount numeric (13,2) NOT NULL,
  function varchar(256),
  bill varchar(256),
  createdby varchar(256),
  createdtime bigint,
  lastmodifiedby varchar(256),
  lastmodifiedtime bigint,
  tenantId varchar(256),
  version bigint
);
alter table egf_billdetail add constraint pk_egf_billdetail primary key (id,tenantId);
create sequence seq_egf_billdetail;
