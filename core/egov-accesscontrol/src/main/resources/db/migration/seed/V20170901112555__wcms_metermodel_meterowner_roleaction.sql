insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'MeterOwnerTypesAPI','/wcms-connection/connection/_getmeterownertypes',
'Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,
'Get Meter Owner Types',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'MeterOwnerTypesAPI' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'MeterModelTypesAPI','/wcms-connection/connection/_getmetermodeltypes',
'Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,
'Get Meter Model Types',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'MeterModelTypesAPI' ),'default');