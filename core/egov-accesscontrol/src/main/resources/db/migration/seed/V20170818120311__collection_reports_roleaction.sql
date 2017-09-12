insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ReportMetaData','/report/collection/metadata/_get','COLLECTION-REPORTS',NULL, (select id from service where code ='COLLECTION-REPORTS' and tenantid='default'),2,'ReportMetaData',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ReportGet','/report/collection/_get','COLLECTION-REPORTS',NULL, (select id from service where code ='COLLECTION-REPORTS'  and tenantid='default'),2,'ReportGet',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='ReportMetaData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='ReportGet'),'default');
