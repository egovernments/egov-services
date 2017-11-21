insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'VendorPaymentDetails', 'Vendor Payment Details', true, 'Vendor Payment Details', 1, (select id from service where code = 'SWM Transactions' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VendorPaymentDetails Create','/swm-services/vendorpaymentdetails/_create','VendorPaymentDetails',null,(select id from service where code='VendorPaymentDetails' and tenantid='default'),0,'Create Vendor Payment Details',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VendorPaymentDetails Update','/swm-services/vendorpaymentdetails/_update','VendorPaymentDetails',null,(select id from service where code='VendorPaymentDetails' and tenantid='default'),0,'Update Vendor Payment Details',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VendorPaymentDetails Search','/swm-services/vendorpaymentdetails/_search','VendorPaymentDetails',null,(select id from service where code='VendorPaymentDetails' and tenantid='default'),0,'View Vendor Payment Details',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VendorPaymentDetails Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VendorPaymentDetails Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VendorPaymentDetails Search' ),'default');