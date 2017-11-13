Create table egswm_vendorpaymentdetails(
  paymentno varchar(256) NOT NULL,
  tenantid varchar(128) NOT NULL,
  vendorcontract varchar(256) NOT NULL,
  invoiceno varchar(128) NOT NULL,
  fromdate bigint NOT NULL,
  todate bigint NOT NULL,
  vendorinvoiceamount numeric NOT NULL,
  employee varchar(256),
  createdby varchar(50),
  createdtime bigint,
  lastmodifiedby varchar(50),
  lastmodifiedtime bigint,
  version bigint,
  constraint uk_vendorpaymentdetails_paymentno_tenant unique (paymentno,tenantid)
);
