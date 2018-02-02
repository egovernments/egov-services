ALTER TABLE egasset_asset
    ALTER COLUMN electionward TYPE character varying USING electionward::character varying;

