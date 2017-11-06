insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'SanitationStaffTarget', 'Sanitation Staff Target', true, 'Sanitation Staff Target', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SanitationStaffTarget Create','/swm-services/sanitationstafftargets/_create','SanitationStaffTarget',null,(select id from service where code='SanitationStaffTarget' and tenantid='default'),0,'Create Sanitation Staff Target',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SanitationStaffTarget Update','/swm-services/sanitationstafftargets/_update','SanitationStaffTarget',null,(select id from service where code='SanitationStaffTarget' and tenantid='default'),0,'Update Sanitation Staff Target',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SanitationStaffTarget Search','/swm-services/sanitationstafftargets/_search','SanitationStaffTarget',null,(select id from service where code='SanitationStaffTarget' and tenantid='default'),0,'View Sanitation Staff Target',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'SanitationStaffTarget Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'SanitationStaffTarget Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'SanitationStaffTarget Search' ),'default');