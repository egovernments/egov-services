CREATE SEQUENCE seq_egasset_disposal;

CREATE TABLE egasset_disposal
(
  id bigint NOT NULL,
  tenantid character varying(250) NOT NULL,
  assetid bigint NOT NULL,
  buyername character varying(64) NOT NULL,
  buyeraddress character varying(64) NOT NULL,
  disposalreason character varying(500) NOT NULL,
  disposaldate bigint NOT NULL,
  pancardnumber character varying(32) NOT NULL,
  aadharcardnumber character varying(64) NOT NULL,
  assetcurrentvalue numeric(12,2),
  salevalue numeric(12,2),
  transactiontype character varying(64) NOT NULL,
  assetsaleaccount bigint,
  createdby character varying(64) NOT NULL,
  createddate bigint NOT NULL,
  lastmodifiedby character varying(64),
  lastmodifieddate bigint,
  CONSTRAINT pk_egasset_disposal PRIMARY KEY (id, tenantid)
);
