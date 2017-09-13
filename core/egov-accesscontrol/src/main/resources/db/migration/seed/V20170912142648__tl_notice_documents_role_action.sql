insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) 
values (nextval('SEQ_SERVICE'), 'NOTICEDOCUMENTS', 'Notice Documents', true, 'Notice Documents', 5, (select id from service where code = 'TRADELICENSE' and tenantid ='default'), 'default');

insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateNoticeDocuments','/noticedocument/v1/_create','NOTICEDOCUMENTS',null,(select id from service where code='NOTICEDOCUMENTS' and tenantid ='default'),0,'Create Notice Document',false,1,now(),1,now());

insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateNoticeDocuments','/noticedocument/v1/_update','NOTICEDOCUMENTS',null,(select id from service where code='NOTICEDOCUMENTS' and tenantid ='default'),1,'Update Notice Document',false,1,now(),1,now());

insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchNoticeDocuments','/noticedocument/v1/_search','NOTICEDOCUMENTS',null,(select id from service where code='NOTICEDOCUMENTS' and tenantid ='default'),2,'Search Notice Document',false,1,now(),1,now());

insert into eg_action (id, name, url, servicecode, queryparams, parentmodule, ordernumber, displayname, enabled, createdby, createddate, lastmodifiedby, lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchNoticeUI','/noticedocument/v1/_search','NOTICEDOCUMENTS',null,(select id from service where code='NOTICEDOCUMENTS' and tenantid ='default'),3,'Search Notice Documents',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='CreateNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='UpdateNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='SearchNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='SearchNoticeUI'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='CreateNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='UpdateNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='SearchNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_ADMIN',(select id from eg_action where name='SearchNoticeUI'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='CreateNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='UpdateNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='SearchNoticeDocuments'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('TL_CREATOR',(select id from eg_action where name='SearchNoticeUI'),'default');