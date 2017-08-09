-- Category---
update eg_action set url ='/tl-masters/v1/category/_create' where name ='CreateLicenseCategory' and servicecode = 'LICENSECATEGORY' and parentmodule =(select id from service where code='LICENSECATEGORY' and tenantid='default')::text;

update eg_action set url ='/tl-masters/v1/category/_search' where name ='ViewLicenseCategory' and servicecode = 'LICENSECATEGORY' and parentmodule =(select id from service where code='LICENSECATEGORY' and tenantid='default')::text;

update eg_action set url ='/tl-masters/v1/category/_search' where name ='ModifyLicenseCategory' and servicecode = 'LICENSECATEGORY' and parentmodule =(select id from service where code='LICENSECATEGORY' and tenantid='default')::text;

update eg_action set url ='/tl-masters/v1/category/_update' where name ='UpdateLicenseCategory' and servicecode = 'LICENSECATEGORY' and parentmodule =(select id from service where code='LICENSECATEGORY' and tenantid='default')::text;

--- Sub Category ----
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TLSUBCATEGORY', 'TL Sub Category', true, 'Sub Category', 3, (select id from service where name ='Trade License Masters' and code='TLMASTERS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateTLSUBCATEGORY','/tl-masters/v1/category/_create',
'TLSUBCATEGORY',null,(select id from service where code='TLSUBCATEGORY' and tenantid='default'),1,
'Create Sub Category',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'CreateTLSUBCATEGORY' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'CreateTLSUBCATEGORY' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ViewTLSUBCATEGORY','/tl-masters/v1/category/_search',
'TLSUBCATEGORY',null,(select id from service where code='TLSUBCATEGORY' and tenantid='default'),2,
'View Sub Category',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ViewTLSUBCATEGORY' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ViewTLSUBCATEGORY' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyTLSUBCATEGORY','/tl-masters/v1/category/_search',
'TLSUBCATEGORY',null,(select id from service where code='TLSUBCATEGORY' and tenantid='default'),3,
'Modify Sub Category',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ModifyTLSUBCATEGORY' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ModifyTLSUBCATEGORY' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateTLSUBCATEGORY','/tl-masters/v1/category/_update',
'TLSUBCATEGORY',null,(select id from service where code='TLSUBCATEGORY' and tenantid='default'),3,
'Update Sub Category',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'UpdateTLSUBCATEGORY' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'UpdateTLSUBCATEGORY' ),'default');


--- UOM ----
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TLUOM', 'TL Unit of Measurement', true, 'Unit of Measurement', 3, (select id from service where name ='Trade License Masters' and code='TLMASTERS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateTLUOM','/tl-masters/v1/uom/_create',
'TLUOM',null,(select id from service where code='TLUOM' and tenantid='default'),1,
'Create Unit of Measurement',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'CreateTLUOM' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'CreateTLUOM' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ViewTLUOM','/tl-masters/v1/uom/_search',
'TLUOM',null,(select id from service where code='TLUOM' and tenantid='default'),2,
'View Unit of Measurement',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ViewTLUOM' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ViewTLUOM' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyTLUOM','/tl-masters/v1/uom/_search',
'TLUOM',null,(select id from service where code='TLUOM' and tenantid='default'),3,
'Modify Unit of Measurement',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ModifyTLUOM' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ModifyTLUOM' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateTLUOM','/tl-masters/v1/uom/_update',
'TLUOM',null,(select id from service where code='TLUOM' and tenantid='default'),3,
'Update Unit of Measurement',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'UpdateTLUOM' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'UpdateTLUOM' ),'default');


--- DocumentType---
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TLDOCUMENTTYPE', 'TL Document Type', true, 'Document Type', 4, (select id from service where name ='Trade License Masters' and code='TLMASTERS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateTLDOCUMENTTYPE','/tl-masters/v1/documenttype/_create',
'TLDOCUMENTTYPE',null,(select id from service where code='TLDOCUMENTTYPE' and tenantid='default'),1,
'Create Document Type',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'CreateTLDOCUMENTTYPE' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'CreateTLDOCUMENTTYPE' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ViewTLDOCUMENTTYPE','/tl-masters/v1/documenttype/_search',
'TLDOCUMENTTYPE',null,(select id from service where code='TLDOCUMENTTYPE' and tenantid='default'),2,
'View Document Type',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ViewTLDOCUMENTTYPE' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ViewTLDOCUMENTTYPE' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyTLDOCUMENTTYPE','/tl-masters/v1/documenttype/_search',
'TLDOCUMENTTYPE',null,(select id from service where code='TLDOCUMENTTYPE' and tenantid='default'),3,
'Modify Document Type',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ModifyTLDOCUMENTTYPE' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ModifyTLDOCUMENTTYPE' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateTLDOCUMENTTYPE','/tl-masters/v1/documenttype/_update',
'TLDOCUMENTTYPE',null,(select id from service where code='TLDOCUMENTTYPE' and tenantid='default'),3,
'Update Document Type',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'UpdateTLDOCUMENTTYPE' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'UpdateTLDOCUMENTTYPE' ),'default');


 --- PenaltyRate---
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TLPENALTYRATE', 'TL Penalty Rate', true, 'Penalty Rate', 5, (select id from service where name ='Trade License Masters' and code='TLMASTERS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateTLPENALTYRATE','/tl-masters/v1/penaltyrate/_create',
'TLPENALTYRATE',null,(select id from service where code='TLPENALTYRATE' and tenantid='default'),1,
'Create Penalty Rate',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'CreateTLPENALTYRATE' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'CreateTLPENALTYRATE' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ViewTLPENALTYRATE','/tl-masters/v1/penaltyrate/_search',
'TLPENALTYRATE',null,(select id from service where code='TLPENALTYRATE' and tenantid='default'),2,
'View Penalty Rate',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ViewTLPENALTYRATE' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ViewTLPENALTYRATE' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyTLPENALTYRATE','/tl-masters/v1/penaltyrate/_search',
'TLPENALTYRATE',null,(select id from service where code='TLPENALTYRATE' and tenantid='default'),3,
'Modify Penalty Rate',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ModifyTLPENALTYRATE' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ModifyTLPENALTYRATE' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateTLPENALTYRATE','/tl-masters/v1/penaltyrate/_update',
'TLPENALTYRATE',null,(select id from service where code='TLPENALTYRATE' and tenantid='default'),3,
'Update Penalty Rate',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'UpdateTLPENALTYRATE' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'UpdateTLPENALTYRATE' ),'default');

 --- FeeMatrix---
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TLFEEMATRIX', 'TL Fee Matrix', true, 'Fee Matrix', 6, (select id from service where name ='Trade License Masters' and code='TLMASTERS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateTLFEEMATRIX','/tl-masters/v1/feematrix/_create',
'TLFEEMATRIX',null,(select id from service where code='TLFEEMATRIX' and tenantid='default'),1,
'Create Fee Matrix',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'CreateTLFEEMATRIX' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'CreateTLFEEMATRIX' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ViewTLFEEMATRIX','/tl-masters/v1/feematrix/_search',
'TLFEEMATRIX',null,(select id from service where code='TLFEEMATRIX' and tenantid='default'),2,
'View Fee Matrix',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ViewTLFEEMATRIX' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ViewTLFEEMATRIX' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'ModifyTLFEEMATRIX','/tl-masters/v1/feematrix/_search',
'TLFEEMATRIX',null,(select id from service where code='TLFEEMATRIX' and tenantid='default'),3,
'Modify Fee Matrix',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'ModifyTLFEEMATRIX' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'ModifyTLFEEMATRIX' ),'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'UpdateTLFEEMATRIX','/tl-masters/v1/feematrix/_update',
'TLFEEMATRIX',null,(select id from service where code='TLFEEMATRIX' and tenantid='default'),3,
'Update Fee Matrix',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'UpdateTLFEEMATRIX' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'UpdateTLFEEMATRIX' ),'default');

