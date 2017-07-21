ALTER TABLE egasset_asset RENAME depriciationrate  TO depreciationrate;

ALTER TABLE egasset_asset RENAME enableyearwisedepriciation  TO enableyearwisedepreciation;

ALTER TABLE egasset_yearwisedepreciation   
ADD CONSTRAINT uc_egasset_yearwisedepreciation UNIQUE(financialyear);