DELETE FROM egasset_asset;

ALTER TABLE egasset_asset
   ADD COLUMN createdby character varying(64) NOT NULL;

ALTER TABLE egasset_asset
   ADD COLUMN createddate bigint NOT NULL;

ALTER TABLE egasset_asset
   ADD COLUMN lastmodifiedby character varying(64);

ALTER TABLE egasset_asset
   ADD COLUMN lastmodifieddate bigint;


ALTER TABLE egasset_disposal ADD COLUMN status character varying;
