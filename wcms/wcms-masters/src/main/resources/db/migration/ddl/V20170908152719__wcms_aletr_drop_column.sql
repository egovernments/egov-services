--storage reservoir
alter table  egwtr_storage_reservoir drop column ward;
alter table  egwtr_storage_reservoir drop column zone;

ALTER TABLE egwtr_storage_reservoir
   ALTER COLUMN location TYPE character varying(256);
   
---treatment plant

alter table  egwtr_treatment_plant drop column ward;
alter table  egwtr_treatment_plant drop column zone;

ALTER TABLE egwtr_treatment_plant
   ALTER COLUMN location TYPE character varying(256);