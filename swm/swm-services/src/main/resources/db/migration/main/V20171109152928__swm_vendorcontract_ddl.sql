Create table egswm_vendorcontract(
  tenantId varchar(128) NOT NULL,
  vendor varchar(256) NOT NULL,
  contractNo varchar(128) NOT NULL,
  contractDate bigint NOT NULL,
  contractPeriodFrom bigint NOT NULL,
  contractPeriodTo bigint NOT NULL,
  securityDeposit bigint NOT NULL,
  paymentAmount bigint NOT NULL,
  paymentTerms varchar(256) NOT NULL,
  remarks varchar(500),
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint
);


alter table egswm_vendorcontract add constraint pk_egswm_vendorcontract primary key (contractNo,tenantId);