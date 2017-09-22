alter table egwtr_meterstatus add column active boolean;
update egwtr_meterstatus set active = true;
ALTER TABLE egwtr_meterstatus ALTER COLUMN active SET NOT NULL ;