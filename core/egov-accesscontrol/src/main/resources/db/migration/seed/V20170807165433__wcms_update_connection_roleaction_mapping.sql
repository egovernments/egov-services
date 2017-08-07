
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateNewConnectionAPI','/wcms-connection/connection/{ackNumber}/_update',
'Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,
'Update Connection',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'UpdateNewConnectionAPI' ),'default');

