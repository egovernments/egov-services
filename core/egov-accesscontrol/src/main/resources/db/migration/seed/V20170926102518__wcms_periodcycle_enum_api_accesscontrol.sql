insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'WCGetPeriodCycle','/wcms-connection/connection/_getallperiodcycles',
'Water Charge',NULL, (select id from service where code ='Water Charge' and tenantid='default'),1,
'Get All Period Cycles',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='WCGetPeriodCycle'),'default');