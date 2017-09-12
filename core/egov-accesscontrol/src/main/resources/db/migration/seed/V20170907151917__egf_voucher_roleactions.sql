insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) 
values (nextval('SEQ_SERVICE'), 'VOUCHER', 'VOUCHER SERVICE', true, 'Financial Voucher Service', 1, null, 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VoucherCreate','/egf-voucher/vouchers/_create','VOUCHER',null,(select id from service where code='VOUCHER' and tenantid='default'),0,'Create Voucher',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VoucherReverse','/egf-voucher/vouchers/_reverse','VOUCHER',null,(select id from service where code='VOUCHER' and tenantid='default'),0,'Create Reverse Voucher',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VoucherUpdate','/egf-voucher/vouchers/_update','VOUCHER',null,(select id from service where code='VOUCHER' and tenantid='default'),0,'Update Voucher',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VoucherSearch','/egf-voucher/vouchers/_search','VOUCHER',null,(select id from service where code='VOUCHER' and tenantid='default'),0,'Search Voucher',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VoucherDelete','/egf-voucher/vouchers/_delete','VOUCHER',null,(select id from service where code='VOUCHER' and tenantid='default'),0,'Delete Voucher',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VoucherCreate' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VoucherReverse' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VoucherUpdate' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VoucherSearch' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VoucherDelete' ),'default');
