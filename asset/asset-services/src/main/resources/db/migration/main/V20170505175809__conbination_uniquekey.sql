ALTER TABLE egasset_assetcategory DROP CONSTRAINT if exists uk_egasset_assetcategory_code;
ALTER TABLE egasset_assetcategory ADD CONSTRAINT uk_egasset_assetcategory_code UNIQUE (code,tenantid);

ALTER TABLE egasset_assetcategory DROP CONSTRAINT if exists uk_egasset_assetcategory_name;
ALTER TABLE egasset_assetcategory ADD CONSTRAINT uk_egasset_assetcategory_name UNIQUE (name,tenantid);


ALTER TABLE egasset_asset DROP CONSTRAINT if exists uk_egasset_asset_name;
ALTER TABLE egasset_asset ADD CONSTRAINT uk_egasset_asset_name UNIQUE (name,tenantid);
