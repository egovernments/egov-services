ALTER TABLE egasset_asset DROP COLUMN usage,DROP COLUMN cubiccontents,DROP COLUMN height,DROP COLUMN length,DROP COLUMN width,DROP COLUMN floors,DROP COLUMN plintharea;
ALTER TABLE egasset_asset ADD COLUMN assetcategorytype character varying(60);
