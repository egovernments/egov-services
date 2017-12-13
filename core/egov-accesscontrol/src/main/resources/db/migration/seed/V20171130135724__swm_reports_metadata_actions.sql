INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'SWM Report MetaData', '/report/swm/metadata/_get', 'SWMRPT',null,   (select id from service where name = 'Solid Waste Management' and tenantid = 'default'), 1, 'Get SWM Report Metadata', false, 1, now(), 1, now());

INSERT INTO eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate)  
VALUES (nextval('SEQ_EG_ACTION'), 'SWM Report', '/report/swm/_get', 'SWMRPT',null,   (select id from service where name = 'Solid Waste Management' and tenantid = 'default'), 1, 'Get SWM Report', false, 1, now(), 1, now());


insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='SWM Report MetaData'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='SWM Report'),'default');

