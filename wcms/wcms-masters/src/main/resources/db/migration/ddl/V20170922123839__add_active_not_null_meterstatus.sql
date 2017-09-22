alter table egwtr_meterstatus drop column active;
alter table egwtr_meterstatus add column active boolean NOT NULL DEFAULT true;
alter table egwtr_meterstatus alter column active drop DEFAULT;