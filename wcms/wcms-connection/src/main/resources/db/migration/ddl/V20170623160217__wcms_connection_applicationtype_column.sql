ALTER TABLE egwtr_waterconnection ADD COLUMN applicationType character varying(100);
UPDATE egwtr_waterconnection SET applicationType='NEWCONNECTION';
ALTER TABLE egwtr_waterconnection ALTER COLUMN applicationType SET NOT NULL;