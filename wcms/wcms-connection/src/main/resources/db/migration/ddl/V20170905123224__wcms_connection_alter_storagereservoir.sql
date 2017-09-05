ALTER TABLE egwtr_waterconnection
  DROP COLUMN propertytype;
ALTER TABLE egwtr_waterconnection
  ADD COLUMN storagereservoir character varying(100);
