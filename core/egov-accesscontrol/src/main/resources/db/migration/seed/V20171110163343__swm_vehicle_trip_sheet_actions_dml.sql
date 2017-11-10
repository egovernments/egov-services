insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'VehicleTripSheetDetails', 'Vehicle Trip Sheet Details', true, 'Vehicle Trip Sheet Details', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleTripSheetDetails Create','/swm-services/vehicletripsheetdetails/_create','VehicleTripSheetDetails',null,(select id from service where code='VehicleTripSheetDetails' and tenantid='default'),0,'Create Vehicle Trip Sheet Details',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleTripSheetDetails Update','/swm-services/vehicletripsheetdetails/_update','VehicleTripSheetDetails',null,(select id from service where code='VehicleTripSheetDetails' and tenantid='default'),0,'Update Vehicle Trip Sheet Details',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VehicleTripSheetDetails Search','/swm-services/vehicletripsheetdetails/_search','VehicleTripSheetDetails',null,(select id from service where code='VehicleTripSheetDetails' and tenantid='default'),0,'View Vehicle Trip Sheet Details',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleTripSheetDetails Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleTripSheetDetails Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VehicleTripSheetDetails Search' ),'default');