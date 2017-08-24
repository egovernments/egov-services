insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) 
values (nextval('SEQ_SERVICE'), 'LICENSEREPORTS', 'License Reports', true, 'License Reports', 4, (select id from service where code = 'TRADELICENSE' and tenantid ='default'), 'default');

insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'License Register Report','/tradelicense/report/licenseRegisterReport','LICENSEREPORTS',null,(select id from service where code='LICENSEREPORTS' and tenantid ='default'),1,'License Register Report',true,1,now(),1,now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
VALUES (nextval('SEQ_EG_ACTION'), 'TL Report MetaData', '/report/tradelicense/metadata/_get', 'LICENSEREPORTS',null, (select id from service where name='Reports' and tenantid='default'), 1, 'Get TL Report Metadata', false, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
VALUES (nextval('SEQ_EG_ACTION'), 'TL Report', '/report/tradelicense/_get', 'LICENSEREPORTS',null, (select id from service where name='Reports' and tenantid='default'), 1, 'Get TL Report', false, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
VALUES (nextval('SEQ_EG_ACTION'), 'TL Report Reload', '/report/tradelicense/_reload', 'LICENSEREPORTS',null, (select id from service where name='TL Report' and tenantid='default'), 1, 'TL Report Reload', false, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='TL Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='TL Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='TL Report Reload'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='License Register Report'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='TL Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='TL Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='TL Report Reload'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='License Register Report'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='TL Report MetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='TL Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='TL Report Reload'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='License Register Report'),'default');