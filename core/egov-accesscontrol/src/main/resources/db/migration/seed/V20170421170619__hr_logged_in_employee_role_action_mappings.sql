insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'LoggedInEmployeeDetails','/employees/_loggedinemployee','EIS',NULL,(select id from service where name='HR Employee'),0,'LoggedInEmployeeDetails',false,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'LoggedInEmployeeDetails'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'LoggedInEmployeeDetails'),'default');

--rollback scripts
--rollback delete from eg_roleaction where rolecode in ('EMPLOYEE ADMIN', 'SUPERUSER') and actionid in (select id from eg_action where name = 'LoggedInEmployeeDetails');
--rollback delete from eg_action where name = 'LoggedInEmployeeDetails';