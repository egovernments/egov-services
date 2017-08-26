ALTER TABLE egtl_license
ALTER COLUMN licenseNumber DROP NOT NULL;

ALTER TABLE egtl_license
DROP CONSTRAINT unq_tl_licenseno;