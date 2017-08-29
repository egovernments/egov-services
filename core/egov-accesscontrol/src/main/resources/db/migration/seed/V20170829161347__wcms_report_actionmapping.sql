insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'WCOutstandingReport','/wcms-connection/report/_outstanding','WCMS REPORTS',NULL, (select id from service where code ='WCMS REPORTS' and tenantid='default'),1,'Outstanding Report',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'WCDemandRegister','/wcms-connection/report/_demandregister','WCMS REPORTS',NULL, (select id from service where code ='WCMS REPORTS' and tenantid='default'),3,'Demand Register Report',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'WCDCBReport','/wcms-connection/report/_dcbreport','WCMS REPORTS',NULL, (select id from service where code ='WCMS REPORTS' and tenantid='default'),2,'DCB Report',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='WCOutstandingReport'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='WCDemandRegister'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='WCDCBReport'),'default');

