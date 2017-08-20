
Create table egf_voucher( 
  id varchar(256),
  type varchar(50) NOT NULL,
  name varchar(50) NOT NULL,
  description varchar(256),
  voucherNumber varchar(50),
  voucherDate timestamp without time zone NOT NULL,
  originalVoucherNumber varchar(50),
  refVoucherNumber varchar(50),
  moduleName varchar(50),
  billNumber varchar(50),
  statusId varchar(256),
  fundId varchar(256),
  functionId varchar(256),
  fundsourceId varchar(256),
  schemeId varchar(256),
  subSchemeId varchar(256),
  functionaryId varchar(256),
  divisionId varchar(256),
  departmentId varchar(256),
  sourcePath varchar(256),
  budgetCheckRequired boolean,
  budgetAppropriationNo varchar(50),
  createdby varchar(256),
  createddate timestamp without time zone,
  lastmodifiedby varchar(256),
  lastmodifieddate timestamp without time zone,
  tenantId varchar(256),
  version bigint
);
alter table egf_voucher add constraint pk_egf_voucher primary key (id);
create sequence seq_egf_voucher;
