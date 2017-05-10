ALTER TABLE egasset_asset DROP CONSTRAINT if exists uk_egasset_asset_name;
ALTER TABLE egasset_asset ADD CONSTRAINT uk_egasset_asset_code UNIQUE (code,tenantId);