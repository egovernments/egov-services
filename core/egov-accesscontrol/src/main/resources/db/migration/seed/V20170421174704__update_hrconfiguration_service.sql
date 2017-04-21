insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantId)values(nextval('SEQ_EG_ACTION'),'HRConfigSearch','hrconfigurations/_search','EIS',null,(select id from service where name='HR Masters'),3,'HRConfigSearch',false,1,now(),1,now(),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'HRConfigSearch'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('EMPLOYEE ADMIN', (select id from eg_action where name = 'HRConfigSearch'),'default');