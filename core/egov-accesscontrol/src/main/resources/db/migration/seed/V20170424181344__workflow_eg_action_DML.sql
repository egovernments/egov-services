INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Workflow_MS', 'Workflow_MS', false, 'egov-common-workflows', NULL, 'Workflow', 20,'default');


insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'loaddesignations','loaddesignations',
'/designations/_search',null,(select id from service where name='Workflow_MS'),1,'loaddesignations',false,1,now(),1,now(),'default');


insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'processsearch','processsearch',
'/process/_search',null,(select id from service where name='Workflow_MS'),1,'processsearch',false,1,now(),1,now(),'default');


insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'tasksearch','tasksearch',
'/tasks/_search',null,(select id from service where name='Workflow_MS'),1,'tasksearch',false,1,now(),1,now(),'default');



insert into eg_roleaction  (rolecode,actionid,tenantid)
values('SUPERUSER',(Select id from eg_action where url='/designations/_search' and name='loaddesignations'),'default');

insert into eg_roleaction  (rolecode,actionid,tenantid)
values('SUPERUSER',(Select id from eg_action where url='/process/_search'),'default');

insert into eg_roleaction  (rolecode,actionid,tenantid)
values('SUPERUSER',(Select id from eg_action where url='/tasks/_search'),'default');
