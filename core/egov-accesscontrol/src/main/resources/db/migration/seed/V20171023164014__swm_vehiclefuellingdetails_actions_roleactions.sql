insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) 
values (nextval('SEQ_SERVICE'), 'SWM', 'SWM SERVICE', false, 'Solid Waste Management', 1, null, 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleFuellingDetails Create','/swm-services/vehiclefuellingdetailses/_create','Vehicle Fuelling Details Create',null,(select id from service where code='SWM' and tenantid='default'),0,'Vehicle Fuelling Details Create',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleFuellingDetails Update','/swm-services/vehiclefuellingdetailses/_update','Vehicle Fuelling Details Update',null,(select id from service where code='SWM' and tenantid='default'),0,'Vehicle Fuelling Details Update',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleFuellingDetails Search','/swm-services/vehiclefuellingdetailses/_search','Vehicle Fuelling Details Search',null,(select id from service where code='SWM' and tenantid='default'),0,'Vehicle Fuelling Details Search',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleFuellingDetails Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleFuellingDetails Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleFuellingDetails Search' ),'default');
