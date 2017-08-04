
insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'LegacyCreateNewConnectionAPI','/wcms-connection/connection/_create',
'Water Charge','legacy=true',(select id from service where code='Water Charge' and tenantid='default'),1,
'Create Legacy Connection',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'LegacyCreateNewConnectionAPI' ),'default');
