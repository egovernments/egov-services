insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Create Bulk Router','/workflow/router/v1/_create','RUTR',null,(select id from service where code='RUTR' and tenantid ='default'),4,'Create Bulk Router',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid) values('SUPERUSER',(select id from eg_action where name='Create Bulk Router'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values('GA',(select id from eg_action where name='Create Bulk Router'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values('RO',(select id from eg_action where name='Create Bulk Router'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values('GRO',(select id from eg_action where name='Create Bulk Router'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid) values('GO',(select id from eg_action where name='Create Bulk Router'),'default');