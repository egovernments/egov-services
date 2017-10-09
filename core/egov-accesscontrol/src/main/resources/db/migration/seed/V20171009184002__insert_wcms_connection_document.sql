insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ConnectionDocumentDataCreate','/wcms-connection/documents/_create',
'Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,
'ConnectionDocumentDataCreate',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ConnectionDocumentDataSearch','/wcms-connection/documents/_search',
'Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),2,
'ConnectionDocumentDataSearch',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,
lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'WCDocumentType','/wcms-connection/connection/_getDocumentTypes',
'Water Charge',NULL, (select id from service where code ='Water Charge' and tenantid='default'),1,
'WCDocumentType',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='ConnectionDocumentDataCreate'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='ConnectionDocumentDataSearch'),'default');
insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='WCDocumentType'),'default');
