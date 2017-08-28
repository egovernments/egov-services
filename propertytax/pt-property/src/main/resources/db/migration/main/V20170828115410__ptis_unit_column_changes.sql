ALTER TABLE egpt_unit ADD COLUMN subUsage character varying(128);
ALTER TABLE egpt_unit_history ADD COLUMN subUsage character varying(128);

ALTER TABLE egpt_unit ALTER COLUMN usage  TYPE character varying(128);
ALTER TABLE egpt_unit_history ALTER COLUMN usage  TYPE character varying(128);

