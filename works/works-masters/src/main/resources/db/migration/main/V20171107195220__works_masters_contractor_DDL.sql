DROP TABLE egw_contractor;

CREATE TABLE egw_contractor
(
  id character varying(256),
  tenantId character varying(128) NOT NULL,
  name character varying(100) NOT NULL,
  code character varying(100) NOT NULL,
  correspondenceaddress character varying(512) NOT NULL,
  paymentaddress character varying(512) NOT NULL,
  contactperson character varying(100) NOT NULL,
  email character varying(100) NOT NULL,
  narration character varying(1024),
  mobilenumber character varying(10) NOT NULL,
  pannumber character varying(10) NOT NULL,
  tinnumber character varying(12) NOT NULL,
  bank character varying(100) NOT NULL,
  bankaccountnumber character varying(20) NOT NULL,
  pwdapprovalcode character varying(20) NOT NULL,
  exemptedfrom character varying(20),
  pwdapprovalvalidtill bigint NOT NULL,
  epfRegistrationNumber character varying(50) NOT NULL,
  accountcode character varying(100) NOT NULL,
  ifsccode character varying(20) NOT NULL,
  contractorclass character varying(100) NOT NULL,
  pmc boolean DEFAULT false,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  constraint pk_egw_contractor PRIMARY  KEY(id,tenantId),
  constraint uk_egw_contractor_code_tenantId UNIQUE (tenantId, code)
);
