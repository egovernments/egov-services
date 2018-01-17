UPDATE egasset_asset SET dateofcreation= 1504549800000 where dateofcreation IS NULL;
ALTER TABLE egasset_asset ALTER COLUMN dateofcreation SET NOT NULL;