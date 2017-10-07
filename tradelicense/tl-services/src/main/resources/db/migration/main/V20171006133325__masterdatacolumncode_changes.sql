ALTER TABLE egtl_license
RENAME categoryid TO category;
 
ALTER TABLE egtl_license
ALTER COLUMN category TYPE character varying(100);

ALTER TABLE egtl_license
RENAME subcategoryid TO subcategory;
 
ALTER TABLE egtl_license
ALTER COLUMN subcategory TYPE character varying(100);

ALTER TABLE egtl_license
RENAME uomid TO uom;
 
ALTER TABLE egtl_license
ALTER COLUMN uom TYPE character varying(100);

ALTER TABLE egtl_license
ALTER COLUMN status TYPE character varying(100);

ALTER TABLE egtl_license_application
ALTER COLUMN status TYPE character varying(100);