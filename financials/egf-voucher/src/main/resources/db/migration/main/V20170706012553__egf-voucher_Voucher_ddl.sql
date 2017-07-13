
Create table egf_voucher( 
  id varchar(50),
  type varchar(50) NOT NULL,
  name varchar(50) NOT NULL,
  description varchar(50),
  voucherNumber varchar(50),
  voucherDate date NOT NULL,
  fundId varchar(50),
  statusId varchar(50),
  originalVoucherNumber varchar(50),
  refVoucherNumber varchar(50),
  moduleName varchar(50),
    createdby varchar(50),
		createddate timestamp without time zone,
		lastmodifiedby varchar(50),
		lastmodifieddate timestamp without time zone,
		tenantId varchar(250),
		version bigint
);
alter table egf_voucher add constraint pk_egf_voucher primary key (id);
create sequence seq_egf_voucher;
