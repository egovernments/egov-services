ALTER TABLE egpt_propertydetails ADD COLUMN bpaNo character varying(16) DEFAULT NULL;
ALTER TABLE egpt_propertydetails ADD COLUMN bpaDate timestamp without time zone;

ALTER TABLE egpt_unit ALTER COLUMN occupiername TYPE character varying(4000);
ALTER TABLE egpt_propertydetails ALTER COLUMN sitalarea DROP NOT NULL;

ALTER TABLE egpt_unit_history ALTER COLUMN occupiername TYPE character varying(4000);
ALTER TABLE egpt_propertydetails_history ALTER COLUMN sitalarea DROP NOT NULL;


