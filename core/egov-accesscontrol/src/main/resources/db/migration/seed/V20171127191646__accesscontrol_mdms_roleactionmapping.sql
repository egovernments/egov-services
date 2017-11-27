INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Get Actions from MDMS', '/access/v1/actions/mdms/_get', 'AccessControl',null,   (select id from service where name = 'Access Control' and tenantid = 'default'), 1,   'Get Actions from mdms', false, 1, now(), 1, now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Get Actions from MDMS'),'default');


