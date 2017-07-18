CREATE SEQUENCE seq_egasset_assetconfiguration;
CREATE TABLE egasset_assetconfiguration
(
  id bigint NOT NULL,
  keyname character varying(50) NOT NULL,
  description character varying(250),
  createdby character varying(20) NOT NULL,
  createddate bigint NOT NULL,
  lastmodifiedby character varying(20),
  lastmodifieddate bigint,
  tenantid character varying(250) NOT NULL,
  CONSTRAINT pk_egasset_assetconfiguration PRIMARY KEY (id, tenantid),
  CONSTRAINT uk_egasset_assetconfiguration UNIQUE (keyname, tenantid)
);

--rollback drop table egasset_assetconfiguration;