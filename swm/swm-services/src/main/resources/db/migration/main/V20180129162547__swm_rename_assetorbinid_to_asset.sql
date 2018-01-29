ALTER TABLE egswm_bindetails RENAME COLUMN assetOrBinId TO asset;

ALTER TABLE egswm_bindetails DROP COLUMN latitude RESTRICT;

ALTER TABLE egswm_bindetails DROP COLUMN longitude RESTRICT;