delete from eg_roleaction  where actionid in(select id from eg_action where parentmodule in (select id::text from service where name='Workflow_MS' and tenantid='default'));

delete from eg_action where parentmodule=(select id::text from service where name='Workflow_MS' and tenantid='default') and url like '/designations/_search';
delete from eg_action where parentmodule=(select id::text from service where name='Workflow_MS' and tenantid='default') and url like '/process/_start';
delete from eg_action where parentmodule=(select id::text from service where name='Workflow_MS' and tenantid='default') and url like '/process/_end';
delete from eg_action where parentmodule=(select id::text from service where name='Workflow_MS' and tenantid='default') and url like '/history';
delete from eg_action where parentmodule=(select id::text from service where name='Workflow_MS' and tenantid='default') and url like '/tasks';
delete from eg_action where parentmodule=(select id::text from service where name='Workflow_MS' and tenantid='default') and url like '/tasks/_search';
delete from eg_action where parentmodule=(select id::text from service where name='Workflow_MS' and tenantid='default') and url like '/process/_search';

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Workflow_MS' and tenantid='default'),'designationsMSSearch',
'/egov-common-workflows/designations/_search',null,(select id from service where name='Workflow_MS' and tenantid='default'),1,'designationsMSSearch',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Workflow_MS' and tenantid='default'),'processMSStart',
'/egov-common-workflows/process/_start',null,(select id from service where name='Workflow_MS' and tenantid='default'),1,'processMSStart',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Workflow_MS' and tenantid='default'),'processMSEnd',
'/egov-common-workflows/process/_end',null,(select id from service where name='Workflow_MS' and tenantid='default'),1,'processMSEnd',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Workflow_MS' and tenantid='default'),'historyMS',
'/egov-common-workflows/history',null,(select id from service where name='Workflow_MS' and tenantid='default'),1,'historyMS',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Workflow_MS' and tenantid='default'),'tasksMS',
'/egov-common-workflows/tasks',null,(select id from service where name='Workflow_MS' and tenantid='default'),1,'tasksMS',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Workflow_MS' and tenantid='default'),'tasksMSSearch',
'/egov-common-workflows/tasks/_search',null,(select id from service where name='Workflow_MS' and tenantid='default'),1,'tasksMSSearch',false,1,now(),1,now());

insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE) values (NEXTVAL('SEQ_EG_ACTION'),(select code from service where name='Workflow_MS' and tenantid='default'),'processMSSearch',
'/egov-common-workflows/process/_search',null,(select id from service where name='Workflow_MS' and tenantid='default'),1,'processMSSearch',false,1,now(),1,now());

insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'designationsMSSearch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'processMSStart'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'processMSEnd'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'historyMS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'tasksMS'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'tasksMSSearch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('Super User', (select id from eg_action where name = 'processMSSearch'),'default');

insert into eg_roleaction(rolecode, actionid,tenantid) values ('EMPLOYEE', (select id from eg_action where name = 'tasksMSSearch'),'default');
insert into eg_roleaction(rolecode, actionid,tenantid) values ('EMPLOYEE', (select id from eg_action where name = 'historyMS'),'default');