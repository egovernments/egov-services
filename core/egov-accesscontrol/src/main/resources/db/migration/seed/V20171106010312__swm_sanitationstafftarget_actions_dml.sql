insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'VehicleMaintenance', 'VehicleMaintenance', true, 'VehicleMaintenance', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleMaintenance Create','/swm-services/vehiclemaintenances/_create','VehicleMaintenance',null,(select id from service where code='VehicleMaintenance' and tenantid='default'),0,'Create Vehicle Maintenance',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleMaintenance Update','/swm-services/vehiclemaintenances/_update','VehicleMaintenance',null,(select id from service where code='VehicleMaintenance' and tenantid='default'),0,'Update Vehicle Maintenance',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleMaintenance Search','/swm-services/vehiclemaintenances/_search','VehicleMaintenance',null,(select id from service where code='VehicleMaintenance' and tenantid='default'),0,'View Vehicle Maintenance',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleMaintenance Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleMaintenance Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleMaintenance Search' ),'default');