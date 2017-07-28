INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (
nextval('seq_service'), 'AccessControl', 'Access Control', false, '/access', null, 'Access Control', 1,'default'); 


insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Get All Actions','/access/v1/actions/_get','AccessControl',null,1,'Get All Actions',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE', (select id from eg_action where name = 'Get All Actions'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'Get All Actions'),'default');