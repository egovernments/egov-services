insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'Vendor Contract', 'Vendor Contract', true, 'Vendor Contract', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VendorContract Create','/swm-services/vendorcontract/_create','VendorContract',null,(select id from service where code='VendorContract' and tenantid='default'),0,'Create Vendor Contract',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VendorContract Update','/swm-services/vendorcontract/_update','VendorContract',null,(select id from service where code='VendorContract' and tenantid='default'),0,'Update Vendor Contract',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VendorContract Search','/swm-services/vendorcontract/_search','VendorContract',null,(select id from service where code='VendorContract' and tenantid='default'),0,'View Vendor Contract',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VendorContract Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VendorContract Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VendorContract Search' ),'default');