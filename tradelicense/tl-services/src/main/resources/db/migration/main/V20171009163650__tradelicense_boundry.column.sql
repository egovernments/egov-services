ALTER TABLE egtl_license
RENAME adminwardid TO adminward;

ALTER TABLE egtl_license
ALTER COLUMN adminward TYPE character varying(128);

ALTER TABLE egtl_license
RENAME localityid TO locality;

ALTER TABLE egtl_license
ALTER COLUMN locality TYPE character varying(128);

ALTER TABLE egtl_license
RENAME revenuewardid TO revenueward;

ALTER TABLE egtl_license
ALTER COLUMN revenueward TYPE character varying(128);