insert into service (id, code, name, enabled, displayname, ordernumber, tenantId) 
values (nextval('SEQ_SERVICE'), 'CRPT', 'Common Inbox Report', true, 'Common Inbox Reports', 1, 'default');


INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Common Inbox Report MetaData', '/report/common/metadata/_get', 'CRPT',null,   (select id from service where name='Common Inbox Report' and tenantid='default'), 1,   'Get CommonInbox Report Metadata', false, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Common Inbox Report', '/report/common/_get', 'CRPT',null,   (select id from service where name='Common Inbox Report' and tenantid='default'), 1,   'Get Common Inbox Report', false, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Common Inbox Report Reload', '/report/common/_reload', 'CRPT',null,   (select id from service where name='Common Inbox Report' and tenantid='default'), 1,   'HR Report Reload', false, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Common Inbox Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Common Inbox Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Common Inbox Report Reload'),'default');
