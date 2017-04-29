DELETE FROM egasset_assetcategory;

DELETE FROM egasset_asset;

ALTER TABLE egasset_assetcategory ADD column isassetallow boolean,ADD column version character varying(64);

ALTER TABLE egasset_asset ADD column assetRefrance bigint,ADD column version character varying(64);
