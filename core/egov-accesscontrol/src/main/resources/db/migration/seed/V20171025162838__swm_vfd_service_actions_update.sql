update service set enabled = true,name='Solid Waste Management' where code = 'SWM' and tenantId='default';

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'VehicleFuelling', 'Vehicle Fuelling', true, 'Vehicle Fuelling', 1, (select id from service where code = 'SWM' and tenantId='default'), 'default');

update eg_action set servicecode = 'VehicleFuelling', displayname = 'Enter Vehicle Fuelling Details',parentmodule = (select id from service where code='VehicleFuelling' and tenantid='default') where name in ('VehicleFuellingDetails Create');

update eg_action set servicecode = 'VehicleFuelling', displayname = 'Update Vehicle Fuelling Details',parentmodule = (select id from service where code='VehicleFuelling' and tenantid='default')  where name in ('VehicleFuellingDetails Update');

update eg_action set servicecode = 'VehicleFuelling', displayname = 'View Vehicle Fuelling Details',parentmodule = (select id from service where code='VehicleFuelling' and tenantid='default')  where name in ('VehicleFuellingDetails Search');


