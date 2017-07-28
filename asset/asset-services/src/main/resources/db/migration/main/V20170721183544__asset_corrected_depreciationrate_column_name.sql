ALTER TABLE egasset_asset RENAME COLUMN enableyearwisedepriciation TO enableyearwisedepreciation;

ALTER TABLE egasset_asset RENAME COLUMN depriciationrate TO depreciationrate;

--rollback ALTER TABLE egasset_asset RENAME COLUMN enableyearwisedepreciation TO enableyearwisedepriciation;

--rollback ALTER TABLE egasset_asset RENAME COLUMN depreciationrate TO depriciationrate;
