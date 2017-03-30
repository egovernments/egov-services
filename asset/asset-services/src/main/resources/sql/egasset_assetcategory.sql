CREATE TABLE public.egasset_assetcategory
(
  id bigint NOT NULL,
  name character varying(250) NOT NULL,
  code character varying(250) NOT NULL,
  parentid bigint,
  assetcategorytype character varying(250) NOT NULL,
  depreciationmethod character varying(250),
  depreciationrate bigint,
  assetaccount bigint,
  accumulateddepreciationaccount bigint,
  revaluationreserveaccount bigint,
  depreciationexpenseaccount bigint,
  unitofmeasurement bigint,
  customfields character varying(10000),
  tenantid character varying(250) NOT NULL,
  createdby character varying(64) NOT NULL,
  createddate timestamp without time zone NOT NULL,
  lastmodifiedby character varying(64),
  lastmodifieddate timestamp without time zone,
  CONSTRAINT pk_egasset_assetcategory PRIMARY KEY (id),
  CONSTRAINT uk_egasset_assetcategory_code UNIQUE (code)
)

CREATE SEQUENCE SEQ_EGASSET_ASSETCATEGORY INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1;