--- Status---
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TLSTATUS', 'TL Status', true, 'License Status', 1, (select id from service where name ='Trade License Masters' and code='TLMASTERS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateLicenseStatus','/tl-masters/v1/status/_create',
'TLSTATUS',null,(select id from service where code='TLSTATUS' and tenantid='default'),1,
'Create License Status',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'CreateLicenseStatus' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchLicenseStatus','/tl-masters/v1/status/_search',
'TLSTATUS',null,(select id from service where code='TLSTATUS' and tenantid='default'),1,
'Search License  Status',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'SearchLicenseStatus' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateLicenseStatus','/tl-masters/v1/status/_update',
'TLSTATUS',null,(select id from service where code='TLSTATUS' and tenantid='default'),1,
'Update License  Status',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'UpdateLicenseStatus' ),'default');