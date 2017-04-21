---Creating new module
insert into service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) values (nextval('SEQ_SERVICE'),'ESS','ess',true,'hr-web' ,'Employee Self Service',NULL ,NULL ,'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'View ESS Employee','/app/hr/employee/view.html','ESS',NULL,(select id from service where name='ess'),0,'My Details',true,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'ESS Leave Application','/app/hr/leavemaster/apply-leave.html','ESS',NULL,(select id from service where name='ess'),1,'Apply Leave',true,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'View ESS Employee'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ESS Leave Application'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'View ESS Employee'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'ESS Leave Application'),'default');

--rollback scripts
--rollback delete from eg_roleaction where rolecode in ('EMPLOYEE ADMIN', 'SUPERUSER') and actionid in (select id from eg_action where name in ('ESS Leave Application','View ESS Employee'));
--rollback delete from eg_action where name in ('ESS Leave Application', 'View ESS Employee');
--rollback delete from service where name = 'ess';
