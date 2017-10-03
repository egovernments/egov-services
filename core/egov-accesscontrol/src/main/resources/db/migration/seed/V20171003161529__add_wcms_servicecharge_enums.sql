insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'WCServiceType','/wcms/masters/master/_getservicetypes',
'SERVICECHARGEMASTER',NULL, (select id from service where code ='SERVICECHARGEMASTER' and tenantid='default'),1,
'Service Type',false,1,now(),1,now());
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'WCServiceChargeType','/wcms/masters/master/_getservicechargetypes',
'SERVICECHARGEMASTER',NULL, (select id from service where code ='SERVICECHARGEMASTER' and tenantid='default'),2,
'Service Charge Type',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='WCServiceType'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='WCServiceChargeType'),'default');