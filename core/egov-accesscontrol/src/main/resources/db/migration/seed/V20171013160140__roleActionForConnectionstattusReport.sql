
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Connection Status Report','/wcms-connection/report/_connectionstatusreport','WCMS REPORTS',NULL, (select id from service where code ='WCMS REPORTS' and tenantid='default'),1,'Connection Status Report',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='Connection Status Report'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('ULB Operator',(select id from eg_action where name='Connection Status Report'),'default');