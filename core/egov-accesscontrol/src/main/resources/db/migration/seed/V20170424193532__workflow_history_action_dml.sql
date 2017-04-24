insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'Workflow_MS','searchhistory',
'/history',null,(select id from service where name='Workflow_MS'),1,'searchhistory',false,1,now(),1,now(),'default');

insert into eg_roleaction  (rolecode,actionid,tenantid)
values('SUPERUSER',(Select id from eg_action where url='/history'),'default');
