CREATE SEQUENCE seq_egasset_revaluation;

CREATE TABLE egasset_revalution
(
  id bigint NOT NULL,
  tenantid character varying(250) NOT NULL,
  assetid bigint NOT NULL,
  currentcapitalizedvalue numeric(12,2),
  typeofchange character varying(64) NOT NULL,
  revaluationamount numeric(12,2) NOT NULL,
  valueafterrevaluation numeric(12,2),
  revaluationdate bigint NOT NULL,
  reevaluatedby character varying(64) NOT NULL,
  reasonforrevaluation character varying(1024),
  fixedassetswrittenoffaccount bigint,
  function bigint,
  fund bigint,
  scheme bigint,
  subscheme bigint,
  comments character varying(250),
  status character varying(250),
  createdby character varying(64) NOT NULL,
  createddate bigint NOT NULL,
  lastmodifiedby character varying(64),
  lastmodifieddate bigint,
  CONSTRAINT pk_egasset_revalution PRIMARY KEY (id, tenantid)
);
