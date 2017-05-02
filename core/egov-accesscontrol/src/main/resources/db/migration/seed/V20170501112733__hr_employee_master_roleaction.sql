insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'BloodMasterSearch','/bloodgroups/_search','EIS',null,(select id from service where name='HR Employee'),0,'BloodMasterSearch',false,1,now(),1,now(),'default');
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'MaritalStatusSearch','/maritalstatuses/_search','EIS',null,(select id from service where name='HR Employee'),0,'MaritalStatusSearch',false,1,now(),1,now(),'default');


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'BloodMasterSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'MaritalStatusSearch'),'default');


insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'BloodMasterSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'MaritalStatusSearch'),'default');