ALTER TABLE egasset_asset RENAME assetrefrance TO assetreference;
ALTER TABLE egasset_asset add CONSTRAINT uk_egasset_asset_name UNIQUE (name);
ALTER TABLE egasset_assetcategory add CONSTRAINT uk_egasset_assetcategory_name UNIQUE (name);
