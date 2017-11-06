ALTER TABLE egswm_collectionpoint DROP COLUMN latitude RESTRICT;

ALTER TABLE egswm_collectionpoint DROP COLUMN longitude RESTRICT;

ALTER TABLE egswm_bindetails ADD COLUMN latitude NUMERIC;

ALTER TABLE egswm_bindetails ADD COLUMN longitude NUMERIC;