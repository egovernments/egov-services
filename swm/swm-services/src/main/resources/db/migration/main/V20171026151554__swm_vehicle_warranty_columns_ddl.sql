ALTER TABLE egswm_vehicle ADD COLUMN fuelType varchar(256) NOT NULL;
ALTER TABLE egswm_vehicle ADD COLUMN isUnderWarranty boolean;
ALTER TABLE egswm_vehicle ADD COLUMN kilometers bigint;
ALTER TABLE egswm_vehicle ADD COLUMN endOfWarranty bigint;
