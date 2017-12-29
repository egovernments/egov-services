CREATE SEQUENCE seq_egasset_asset_landetails;

ALTER TABLE egasset_asset_landdetails DROP CONSTRAINT pk_egasset_asset_landdetails;

ALTER TABLE egasset_asset_landdetails ADD COLUMN id bigint;
ALTER TABLE egasset_asset_landdetails ALTER COLUMN id SET NOT NULL;

ALTER TABLE egasset_asset_landdetails
  ADD CONSTRAINT egasset_asset_landdetails_pkey PRIMARY KEY(id, tenantid);

ALTER TABLE egasset_asset_landdetails ADD CONSTRAINT uk_egasset_asset_landdetails UNIQUE(id, assetid, tenantid);