---service wcms
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId)
values (nextval('SEQ_SERVICE'),'WCMS','Water Charge',true,'wcms' ,'Water Charge Management System',1 ,NULL ,'default');

---service wcms-masters

INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'WCMS MASTERS','WCMS Masters',true,'wcms','Masters',1,(select id from service where code='WCMS'),'default');

----UsageType
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES
 (nextval('SEQ_SERVICE'),'USAGETYPEMASTER','UsageType Master',true,'wcms','Usage Type Master',1,(select id from service where code='WCMS MASTERS'),'default');

---action
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'CreateUsageType','/app/create/create-usage-type.html','USAGETYPEMASTER',null,(select id from service where name='UsageType Master' and contextroot='wcms'),1,'Create Usage Type',true,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'Create UsageType','/usagetype/_create','USAGETYPEMASTER',null,(select id from service where name='UsageType Master' and contextroot='wcms'),2,'Create UsageType',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'ModifyUsageType','/app/common/show-usage-type.html','USAGETYPEMASTER',null,(select id from service where name='UsageType Master' and contextroot='wcms'),3,'Modify Usage Type',true,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'UsageTypeModify','/usagetype/_update/','USAGETYPEMASTER',null,(select id from service where name='UsageType Master' and contextroot='wcms'),4,'UsageTypeModify',false,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'ViewUsageType','/app/common/show-usage-type.html','USAGETYPEMASTER',null,(select id from service where name='UsageType Master' and contextroot='wcms'),5,'View Usage Type',true,1,now(),1,now(),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)
values(nextval('SEQ_EG_ACTION'),'SearchUsageType','/usagetype/_search','USAGETYPEMASTER',null,(select id from service where name='UsageType Master' and contextroot='wcms'),6,'SearchUsageType',false,1,now(),1,now(),'default');

---role action mapping
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateUsageType'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Create UsageType'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ModifyUsageType'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'UsageTypeModify'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ViewUsageType'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchUsageType'),'default');
