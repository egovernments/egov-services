insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'SWM Transactions', 'SWM Transactions', true, 'SWM Transactions', 2, (select id from service where code = 'SWM' and tenantId='default'), 'default');

update service set parentmodule = (select id from service where code = 'SWM Transactions' and tenantId='default') where code in ('VehicleSchedule','VehicleTripSheetDetails','SourceSegregation','SanitationStaffSchedule','Vehicle Maintenance Details','VehicleFuelling');

update service set name = 'Vehicle Maintenance' where code = 'VehicleMaintenance';