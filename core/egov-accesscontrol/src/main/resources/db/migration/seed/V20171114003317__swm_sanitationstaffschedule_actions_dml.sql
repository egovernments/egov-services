insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'SanitationStaffSchedule', 'Sanitation Staff Schedule', true, 'Sanitation Staff Schedule', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SanitationStaffSchedule Create','/swm-services/sanitationstaffschedules/_create','SanitationStaffSchedule',null,(select id from service where code='SanitationStaffSchedule' and tenantid='default'),0,'Create Sanitation Staff Schedule',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SanitationStaffSchedule Update','/swm-services/sanitationstaffschedules/_update','SanitationStaffSchedule',null,(select id from service where code='SanitationStaffSchedule' and tenantid='default'),0,'Update Sanitation Staff Schedule',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SanitationStaffSchedule Search','/swm-services/sanitationstaffschedules/_search','SanitationStaffSchedule',null,(select id from service where code='SanitationStaffSchedule' and tenantid='default'),0,'View Sanitation Staff Schedule',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'SanitationStaffSchedule Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'SanitationStaffSchedule Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'SanitationStaffSchedule Search' ),'default');