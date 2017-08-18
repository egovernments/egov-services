CREATE TABLE egcl_bankaccountservicemapping
(
  id bigint NOT NULL,
  businessdetails bigint NOT NULL,
  bankaccount bigint NOT NULL,
  active boolean,
  version bigint NOT NULL DEFAULT 1,
  createdby bigint NOT NULL,
  lastmodifiedby bigint NOT NULL,
  createddate bigint,
  lastmodifieddate bigint,
  tenantid character varying(252) NOT NULL,
  CONSTRAINT pk_egcl_bankaccountservicemapping PRIMARY KEY (id)
  );
