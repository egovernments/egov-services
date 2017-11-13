insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'VehicleSchedule', 'Vehicle Schedule', true, 'Vehicle Schedule', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleSchedule Create','/swm-services/vehicleschedules/_create','VehicleSchedule',null,(select id from service where code='VehicleSchedule' and tenantid='default'),0,'Create Vehicle Schedule',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleSchedule Update','/swm-services/vehicleschedules/_update','VehicleSchedule',null,(select id from service where code='VehicleSchedule' and tenantid='default'),0,'Update Vehicle Schedule',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleSchedule Search','/swm-services/vehicleschedules/_search','VehicleSchedule',null,(select id from service where code='VehicleSchedule' and tenantid='default'),0,'View Vehicle Schedule',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleSchedule Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleSchedule Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleSchedule Search' ),'default');