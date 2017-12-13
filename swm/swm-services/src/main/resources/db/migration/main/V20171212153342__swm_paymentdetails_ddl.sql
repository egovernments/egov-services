Create table egswm_paymentdetails(
  code varchar(256) NOT NULL,
  paymentNo varchar(256) NOT NULL,
  tenantId varchar(128) NOT NULL,
  voucherNumber varchar(256) NOT NULL,
  voucherDate bigint NOT NULL,
  instrumentType varchar(256) NOT NULL,
  instrumentNumber varchar(256) NOT NULL,
  instrumentDate bigint NOT NULL,
  amount numeric NOT NULL,
  bankName varchar(256),
  branchName varchar(256),
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint,
  constraint uk_paymentdetails_code_tenant unique (code,tenantid)
);
