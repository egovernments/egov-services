INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Pgr Report MetaData', '/report/pgr/metadata/_get', 'RPT',null,   (select id from service where name='Reports' and tenantid='default'), 1,   'Get Pgr Report Metadata', false, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Pgr Report', '/report/pgr/_get', 'RPT',null,   (select id from service where name='Reports' and tenantid='default'), 1,   'Get Pgr Report', false, 1, now(), 1, now());

insert into service (id, code, name, enabled, displayname, ordernumber, tenantId) 
values (nextval('SEQ_SERVICE'), 'CRPT', 'Common Report', true, 'Common Reports', 1, 'default');

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'Report Reload', '/report/_reload', 'CRPT',null,   (select id from service where name='Common Report' and tenantid='default'), 1,   'Report Reload', false, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Pgr Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Pgr Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Report Reload'),'default');