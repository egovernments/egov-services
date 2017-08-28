CREATE SEQUENCE seq_egcl_onlinepayments;
CREATE TABLE egcl_onlinepayments
(
  id bigint NOT NULL,
  receiptheader  BIGINT,
  paymentgatewayname character varying,
  transactionnumber character varying,
  transactionamount double precision,
  transactiondate bigint,
  authorisation_statuscode character varying,
  status character varying(32),
  remarks character varying,
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  createddate bigint,
  lastmodifieddate bigint,
  tenantid character varying(256) NOT NULL,
  CONSTRAINT pk_egcl_onlinepayments PRIMARY KEY (ID,TENANTID),
  CONSTRAINT fk_onpay_collhead FOREIGN KEY (receiptheader, TENANTID) REFERENCES EGCL_RECEIPTHEADER (ID, TENANTID)
);