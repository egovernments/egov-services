insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateBusinessCategoryMaster','/egov-common-masters/businessCategory/_create','BUSINESSCATEGORY',null,1,'Create Business Category',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ModifyBusinessCategoryMaster','/egov-common-masters/businessCategory/_update','BUSINESSCATEGORY',null,2,'Modify Business Category',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ViewBusinessCategoryMaster','/egov-common-masters/businessCategory/_search','BUSINESSCATEGORY',null,3,'View Business Category',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateBusinessDetailMaster','/egov-common-masters/businessDetails/_create','BUSINESSDETAIL',null,1,'Create Business Detail',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ModifyBusinessDetailMaster','/egov-common-masters/businessDetails/_update','BUSINESSDETAIL',null,2,'Modify Business Detail',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ViewBusinessDetailMaster','/egov-common-masters/businessDetails/_search','BUSINESSDETAIL',null,3,'View Business Detail',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateReceipt','/collection-services/receipt/_create','COLLECTION-TRANSACTIONS',null,1,'Pay Taxes',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CancelReceipt','/collection-services/receipt/_cancel','COLLECTION-TRANSACTIONS',null,2,'Cancel Receipt',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchReceipt','/collection-services/receipt/_search','COLLECTION-TRANSACTIONS',null,3,'Search Receipt',true,1,now(),1,now());



insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='CreateBusinessCategoryMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='ModifyBusinessCategoryMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='ViewBusinessCategoryMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='CreateBusinessDetailMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='ModifyBusinessDetailMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='ViewBusinessDetailMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='CreateReceipt'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='CancelReceipt'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchReceipt'),'default');



