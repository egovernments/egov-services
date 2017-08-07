insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'UpdateReceipt','/collection-services/receipts/_update','COLLECTION-TRANSACTIONS',null,3,'Update Receipt',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='UpdateReceipt'),'default');


update eg_action set url='/collection-services/receipts/_create' where name='CreateReceipt';
update eg_action set url='/collection-services/receipts/_cancel' where name='CancelReceipt';
update eg_action set url='/collection-services/receipts/_search' where name='SearchReceipt';
