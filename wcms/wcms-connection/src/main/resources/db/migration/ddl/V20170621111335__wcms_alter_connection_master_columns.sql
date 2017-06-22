ALTER TABLE egwtr_waterconnection DROP COLUMN supplytype ;
ALTER TABLE egwtr_waterconnection ADD COLUMN supplytype character varying(100);
UPDATE egwtr_waterconnection SET supplytype='1';
ALTER TABLE egwtr_waterconnection ALTER COLUMN supplytype SET NOT NULL;

ALTER TABLE egwtr_waterconnection DROP COLUMN categorytype ;
ALTER TABLE egwtr_waterconnection ADD COLUMN categorytype character varying(100);
UPDATE egwtr_waterconnection SET categorytype='1';
ALTER TABLE egwtr_waterconnection ALTER COLUMN categorytype SET NOT NULL;


ALTER TABLE egwtr_waterconnection DROP COLUMN hscpipesizetype ;
ALTER TABLE egwtr_waterconnection ADD COLUMN hscpipesizetype character varying(100);
UPDATE egwtr_waterconnection SET hscpipesizetype='1';
ALTER TABLE egwtr_waterconnection ALTER COLUMN hscpipesizetype SET NOT NULL;

ALTER TABLE egwtr_waterconnection DROP COLUMN sourcetype ;
ALTER TABLE egwtr_waterconnection ADD COLUMN sourcetype character varying(100);
UPDATE egwtr_waterconnection SET sourcetype='1';
ALTER TABLE egwtr_waterconnection ALTER COLUMN sourcetype SET NOT NULL;