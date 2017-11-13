insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'SWMVMD',  'Vehicle Maintenance Details', true, 'Vehicle Maintenance Details', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Vehicle Maintenance Details Create','/swm-services/vehiclemaintenancedetails/_create','Vehicle Maintenance Details',null,(select id from service where code='SWMVMD' and tenantid='default'),0,'Create Vehicle Maintenance Details',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Vehicle Maintenance Details Update','/swm-services/vehiclemaintenancedetails/_update','Vehicle Maintenance Details',null,(select id from service where code='SWMVMD' and tenantid='default'),0,'Update Vehicle Maintenance Details',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Vehicle Maintenance Details Search','/swm-services/vehiclemaintenancedetails/_search','Vehicle Maintenance Details',null,(select id from service where code='SWMVMD' and tenantid='default'),0,'View Vehicle Maintenance Details',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Get Next Scheduled Maintenance Date','/swm-services/vehiclemaintenancedetails/_getnextscheduleddate','Get Next Scheduled Maintenance Date',null,(select id from service where code='SWMVMD' and tenantid='default'),0,'Get Next Scheduled Maintenance Date',true,1,now(),1,now());



insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Vehicle Maintenance Details Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Vehicle Maintenance Details Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Vehicle Maintenance Details Search' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Get Next Scheduled Maintenance Date' ),'default');
