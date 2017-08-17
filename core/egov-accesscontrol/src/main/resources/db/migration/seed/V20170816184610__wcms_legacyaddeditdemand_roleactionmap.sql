insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'LegacyAddOrEditDemand','/wcms-connection/connection/getLegacyDemandDetailBeanListByExecutionDate',
'Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,
'Legacy Add/Edit Demand',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'LegacyAddOrEditDemand' ),'default');



insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'LegacyAddOrEditDemandupdate','/wcms-connection/connection/_leacydemand',
'Water Charge',null,(select id from service where code='Water Charge' and tenantid='default'),1,
'LegacyAddOrEditDemandupdate',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'LegacyAddOrEditDemandupdate' ),'default');