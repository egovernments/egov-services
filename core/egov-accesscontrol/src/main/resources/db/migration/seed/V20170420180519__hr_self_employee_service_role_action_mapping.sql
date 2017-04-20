---Creating new module
insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) values (nextval('SEQ_SERVICE'),'ESS','ess',true,NULL ,'Employee Self Service',NULL ,NULL ,'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'View ESS Employee','/hr-web/app/hr/common/employee-search.html','EIS','type=view&tenantId=',(select id from service where name='ess'),5,'View Employee',true,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ESS Leave Application','/hr-web/app/hr/leavemaster/leave-application.html','ESS','tenantId=',(select id from service where name='ess'),1,'Apply Leave',true,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'View ESS Employee'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ESS Leave Application'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'View ESS Employee'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'ESS Leave Application'),'default');

--rollback scripts
--rollback delete from eg_roleaction where rolecode in ('EMPLOYEE ADMIN', 'SUPERUSER') and actionid in (select id from eg_action where name in ('ESS Leave Application','View ESS Employee'));
--rollback delete from eg_action where name in ('ESS Leave Application', 'View ESS Employee');
--rollback delete from service where name = 'ess';