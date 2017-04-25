INSERT INTO service (id, code, name, enabled, contextroot, parentmodule, displayname, ordernumber,tenantid) VALUES (nextval('seq_service'), 'Filestore_MS', 'Filestore_MS', false, 'filestore', NULL, 'Filestore', 20,'default');


insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'uploadfiles','uploadfiles',
'/v1/files',null,(select id from service where name='Filestore_MS'),1,'uploadfiles',false,1,now(),1,now(),'default');


insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'filesearch','filesearch',
'/v1/files/id',null,(select id from service where name='Filestore_MS'),1,'filesearch',false,1,now(),1,now(),'default');


insert into eg_action (ID,servicecode,NAME,URL,QUERYPARAMS,PARENTMODULE,ORDERNUMBER,DISPLAYNAME,ENABLED,CREATEDBY,CREATEDDATE,LASTMODIFIEDBY,LASTMODIFIEDDATE,tenantid) values (NEXTVAL('SEQ_EG_ACTION'),'filesearchbytag','filesearchbytag',
'/v1/files/tag',null,(select id from service where name='Filestore_MS'),1,'filesearchbytag',false,1,now(),1,now(),'default');



insert into eg_roleaction  (rolecode,actionid,tenantid)
values('SUPERUSER',(Select id from eg_action where url='/v1/files' and name='uploadfiles'),'default');

insert into eg_roleaction  (rolecode,actionid,tenantid)
values('SUPERUSER',(Select id from eg_action where url='/v1/files/id'),'default');

insert into eg_roleaction  (rolecode,actionid,tenantid)
values('SUPERUSER',(Select id from eg_action where url='/v1/files/tag'),'default');
