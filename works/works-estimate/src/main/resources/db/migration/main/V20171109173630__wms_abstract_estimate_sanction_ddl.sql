CREATE TABLE egw_abstractestimate_sanction_details
(
  id character varying(256),
  tenantId character varying(256) NOT NULL,
  abstractestimate character varying(256) NOT NULL,
  sanctiontype character varying(100) NOT NULL,
  sanctionauthority character varying(50),
  CONSTRAINT egw_abstractestimate_sanction_details_pkey PRIMARY KEY(id,tenantId)
);


CREATE TABLE egw_abstractestimate_asset_details
(
  id character varying(256),
  tenantid character varying(256) NOT NULL,
  abstractestimate character varying(256) NOT NULL,
  asset character varying(256),
  assetcondition character varying(256),
  assetremarks character varying(1024),
  landasset character varying(256),
  landassetcondition character varying(256),
  constructionarea double precision,
  createdby character varying(256) NOT NULL,
  createdtime bigint NOT NULL,
  lastmodifiedby character varying(256),
  lastmodifiedtime bigint,
  CONSTRAINT egw_abstractestimate_asset_details_pkey PRIMARY KEY(id,tenantId)
);