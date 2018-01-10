ALTER TABLE egasset_asset
ALTER COLUMN grossvalue TYPE double precision;

ALTER TABLE egasset_asset
ALTER COLUMN accumulateddepreciation TYPE double precision;  

ALTER TABLE egasset_asset
ALTER COLUMN depreciationrate TYPE double precision; 

ALTER TABLE egasset_assetcategory
ALTER COLUMN depreciationrate TYPE double precision; 

ALTER TABLE egasset_current_value
ALTER COLUMN currentamount TYPE double precision; 

ALTER TABLE egasset_depreciation
ALTER COLUMN depreciationrate TYPE double precision; 

ALTER TABLE egasset_depreciation
ALTER COLUMN valuebeforedepreciation TYPE double precision; 

ALTER TABLE egasset_depreciation
ALTER COLUMN depreciationvalue TYPE double precision; 

ALTER TABLE egasset_depreciation
ALTER COLUMN valueafterdepreciation TYPE double precision; 