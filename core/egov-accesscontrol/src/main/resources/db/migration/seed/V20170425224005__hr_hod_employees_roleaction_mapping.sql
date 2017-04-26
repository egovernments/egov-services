insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'HOD Employees','/hod/employees/_search','EIS','',(select id from service where name='HR Employee'),0,'HOD Employees',false,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'HOD Employees'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'HOD Employees'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'HOD Employees'),'default');
