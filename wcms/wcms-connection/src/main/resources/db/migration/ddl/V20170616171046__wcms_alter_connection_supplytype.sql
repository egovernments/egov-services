ALTER TABLE egwtr_waterconnection DROP COLUMN supplytype ;
ALTER TABLE egwtr_waterconnection ADD COLUMN supplytype bigint;
UPDATE egwtr_waterconnection SET supplytype=1 ;
ALTER TABLE egwtr_waterconnection ALTER COLUMN supplytype SET NOT NULL;
ALTER TABLE egwtr_waterconnection ADD CONSTRAINT egwtr_connection_supplytype_fk0 FOREIGN KEY (supplytype, tenantid)
      REFERENCES egwtr_supply_type (id, tenantid);
ALTER TABLE egwtr_waterconnection ADD CONSTRAINT egwtr_connection_sourcetype_fk0 FOREIGN KEY (sourcetype, tenantid)
      REFERENCES egwtr_water_source_type (id, tenantid);