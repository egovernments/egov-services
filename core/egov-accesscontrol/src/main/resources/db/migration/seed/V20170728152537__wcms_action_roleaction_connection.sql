update service set contextroot=null where name='Water Charge Connection' and code='Water Charge' and tenantid='default' and contextroot='wcms';


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateNewConnectionAPI','/wcms-connection/connection/_create','Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,'Create New Connection',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchWaterConnectionAPI','/wcms-connection/connection/_search','Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,'Search Connection',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'CreateNewConnectionAPI' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'SearchWaterConnectionAPI' ),'default');



insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ConnectionBillingTypeAPI','/wcms-connection/connection/_getconnectiontypes','Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,'Search BillingType',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ConnectionConnectionType','/wcms-connection/connection/_getbillingtypes','Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,'Search ConnectionType',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ConnectionBillingTypeAPI' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER', (select id from eg_action where name = 'ConnectionConnectionType' ),'default');


