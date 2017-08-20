update eg_action set url='/collection-services/report/_reload' where url='/collection-services/report/metadata/_reload';

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CashCollection','/collection-services/report/_cash','COLLECTION-REPORTS',NULL, (select id from service where code ='COLLECTION-REPORTS' and tenantid='default'),1,'Cash Collection Report',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ChequeCollection','/collection-services/report/_cheque','COLLECTION-REPORTS',NULL, (select id from service where code ='COLLECTION-REPORTS' and tenantid='default'),3,'Cheque Collection Report',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ReceiptRegister','/collection-services/report/_receiptregister','COLLECTION-REPORTS',NULL, (select id from service where code ='COLLECTION-REPORTS' and tenantid='default'),2,'Receipt Register Report',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CollectionSummaryAccountHead','/collection-services/report/_receiptregister','COLLECTION-REPORTS',NULL, (select id from service where code ='COLLECTION-REPORTS'  and tenantid='default'),2,'Collection Summary Account Head Wise',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='CashCollection'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='ChequeCollection'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='ReceiptRegister'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='CollectionSummaryAccountHead'),'default');