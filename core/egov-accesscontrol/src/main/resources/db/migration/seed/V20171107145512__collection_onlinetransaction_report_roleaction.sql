insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'OnlineTransaction','/collection-services/report/_online','COLLECTION-REPORTS',NULL, (select id from service where code ='COLLECTION-REPORTS' and tenantid='default'),1,'Online Transaction Report',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='OnlineTransaction'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('COLL_REP_VIEW',(select id from eg_action where name='OnlineTransaction'),'default');

