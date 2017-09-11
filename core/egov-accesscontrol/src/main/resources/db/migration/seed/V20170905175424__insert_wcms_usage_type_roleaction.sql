insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'USAGETYPEMASTER','UsageType Master',true,null,'UsageType Master',24,
(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');
insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'SUBUSAGETYPEMASTER','SubUsageType Master',true,null,'SubUsageType Master',25,
(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');



insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateUsageTypeMaster','/wcms/masters/usagetype/_create','USAGETYPEMASTER',null,1,'Create UsageType',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'UpdateUsageTypeMaster','/wcms/masters/usagetype/_update','USAGETYPEMASTER',null,2,'Update UsageType',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchUsageTypeMaster','/wcms/masters/usagetype/_search','USAGETYPEMASTER',null,3,'View UsageType',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateSubUsageTypeMaster','/wcms/masters/usagetype/_create','SUBUSAGETYPEMASTER','subusagetype=true',1,'Create SubUsageType',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'UpdateSubUsageTypeMaster','/wcms/masters/usagetype/_update','SUBUSAGETYPEMASTER','subusagetype=true',2,'Update SubUsageType',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchSubUsageTypeMaster','/wcms/masters/usagetype/_search','SUBUSAGETYPEMASTER','subusagetype=true',3,'View SubUsageType',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='CreateUsageTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='UpdateUsageTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchUsageTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='CreateSubUsageTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='UpdateSubUsageTypeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchSubUsageTypeMaster'),'default');
