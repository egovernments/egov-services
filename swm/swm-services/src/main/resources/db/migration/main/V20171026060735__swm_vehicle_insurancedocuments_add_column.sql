ALTER TABLE egswm_vehicle ADD COLUMN insuranceDocuments varchar(256);
ALTER TABLE egswm_vehicle RENAME COLUMN vendorName TO vendor;