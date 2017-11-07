ALTER TABLE egswm_vehiclemaintenance DROP COLUMN maintenanceAfterDays RESTRICT;

ALTER TABLE egswm_vehiclemaintenance DROP COLUMN maintenanceAfterKm RESTRICT;

ALTER TABLE egswm_vehiclemaintenance DROP COLUMN downtimeforMaintenance RESTRICT;

ALTER TABLE egswm_vehiclemaintenance ADD COLUMN maintenanceUom varchar(10);

ALTER TABLE egswm_vehiclemaintenance ADD COLUMN downtimeforMaintenanceUom varchar(10);

ALTER TABLE egswm_vehiclemaintenance ADD COLUMN maintenanceAfter bigint;

ALTER TABLE egswm_vehiclemaintenance ADD COLUMN downtimeforMaintenance bigint;