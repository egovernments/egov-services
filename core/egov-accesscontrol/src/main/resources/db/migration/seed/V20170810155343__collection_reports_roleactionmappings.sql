insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'GetDistinctCollectedBy','/collection-services/receipts/_getDistinctCollectedBy','COLLECTION-TRANSACTIONS',null,1,'Distinct collected By',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Collection status','/collection-services/receipts/_status','COLLECTION-TRANSACTIONS',null,1,'Collection status',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Business Details','/collection-services/receipts/_getDistinctBusinessDetails','COLLECTION-TRANSACTIONS',null,1,'Business Details',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Chart Of Accounts','/collection-services/receipts/_getChartOfAccounts','COLLECTION-TRANSACTIONS',null,1,'Chart Of Accounts',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='GetDistinctCollectedBy'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='Collection status'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='Business Details'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='Chart Of Accounts'),'default');