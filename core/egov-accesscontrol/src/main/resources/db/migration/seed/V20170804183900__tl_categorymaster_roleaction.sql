insert into eg_ms_role (name, code, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) values ('TL Admin', 'TL_ADMIN', 'Who has a access to Trade License Masters', now(), 1, 1, now(), 0);

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TRADELICENSE', 'Trade License', true, 'Trade License', 1, NULL, 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TLMASTERS', 'Trade License Masters', true, 'Masters', 1, (select id from service where name ='Trade License' and code='TRADELICENSE' and tenantId='default'), 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'LICENSECATEGORY', 'License Category', true, 'License Category', 1, (select id from service where name ='Trade License Masters' and code='TLMASTERS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateLicenseCategory','/tl-masters/category/_create',
'LICENSECATEGORY',null,(select id from service where code='LICENSECATEGORY' and tenantid='default'),1,
'Create License Category',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'CreateLicenseCategory' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'CreateLicenseCategory' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ViewLicenseCategory','/tl-masters/category/_search',
'LICENSECATEGORY',null,(select id from service where code='LICENSECATEGORY' and tenantid='default'),1,
'View License Category',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ViewLicenseCategory' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ViewLicenseCategory' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyLicenseCategory','/tl-masters/category/_search',
'LICENSECATEGORY',null,(select id from service where code='LICENSECATEGORY' and tenantid='default'),1,
'Modify License Category',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ModifyLicenseCategory' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ModifyLicenseCategory' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateLicenseCategory','/tl-masters/category/_update',
'LICENSECATEGORY',null,(select id from service where code='LICENSECATEGORY' and tenantid='default'),1,
'Update License Category',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'UpdateLicenseCategory' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'UpdateLicenseCategory' ),'default');
