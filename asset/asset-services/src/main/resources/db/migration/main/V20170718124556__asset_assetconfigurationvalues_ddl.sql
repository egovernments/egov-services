CREATE SEQUENCE seq_egasset_assetconfigurationvalues;
CREATE TABLE egasset_assetconfigurationvalues
(
  id bigint NOT NULL,
  keyid bigint NOT NULL,
  value character varying(1000) NOT NULL,
  effectivefrom bigint NOT NULL,
  createdby character varying(20) NOT NULL,
  createddate bigint NOT NULL,
  lastmodifiedby character varying(20),
  lastmodifieddate bigint,
  tenantid character varying(250) NOT NULL,
  CONSTRAINT pk_egasset_assetconfigurationvalues PRIMARY KEY (id, tenantid)
);
--rollback drop table egasset_assetconfigurationvalues;
