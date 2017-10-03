insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) 
values (nextval('SEQ_SERVICE'), 'VOUCHERSUBTYPE', 'VOUCHERSUBTYPE SERVICE', false, 'Financial VoucherSubType Service', 1, null, 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VoucherSubTypeCreate','/egf-voucher/vouchersubtype/_create','VOUCHERSUBTYPE',null,(select id from service where code='VOUCHERSUBTYPE' and tenantid='default'),0,'Create VoucherSubType',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VoucherSubTypeUpdate','/egf-voucher/vouchersubtype/_update','VOUCHERSUBTYPE',null,(select id from service where code='VOUCHERSUBTYPE' and tenantid='default'),0,'Update VoucherSubType',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'VoucherSubTypeSearch','/egf-voucher/vouchersubtype/_search','VOUCHERSUBTYPE',null,(select id from service where code='VOUCHERSUBTYPE' and tenantid='default'),0,'Search VoucherSubType',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VoucherSubTypeCreate' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VoucherSubTypeUpdate' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'VoucherSubTypeSearch' ),'default');
