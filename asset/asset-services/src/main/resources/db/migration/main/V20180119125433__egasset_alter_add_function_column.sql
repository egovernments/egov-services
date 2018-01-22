alter table egasset_asset add column function character varying(16);
update egasset_asset set function ='0201';
ALTER TABLE egasset_asset ALTER COLUMN function SET NOT NULL ;