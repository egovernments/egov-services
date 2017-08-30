
insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'LegacySearchReceipt','/collection-services/receipt/_search','COLLECTION-TRANSACTIONS',null,3,'Search Legacy  Receipt',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='LegacySearchReceipt'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('COLL_OPERATOR',(select id from eg_action where name='LegacySearchReceipt'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('BANK_COLL_OPERATOR',(select id from eg_action where name='LegacySearchReceipt'),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('CSC_COLL_OPERATOR',(select id from eg_action where name='LegacySearchReceipt'),'default');

