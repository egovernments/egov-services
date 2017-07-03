--Journal Voucher Module
INSERT INTO service (id,code,name,enabled,contextroot,displayname,ordernumber,parentmodule,tenantId) VALUES (nextval('SEQ_SERVICE'),
	'JournalVouchers', 'Journal Vouchers', true, '/EGF', 'Journal Vouchers', null,null, 'default');

--Create Voucher API
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
	lastmodifieddate)values(nextval('SEQ_EG_ACTION'),'Create Voucher Rest',
		'/vouchers/_create','CreateVoucherRest',null,(select id from service where name='Journal Vouchers' and  tenantId='default'),
			1,'Create Journal Vouchers',true,1,now(),1,now());
			
-- Role Action Mapping of Voucher API with SUPERUSER
insert into eg_roleaction(roleCode,actionid,tenantid) values ('SUPERUSER',(select id from eg_action where name = 'Create Voucher Rest'),'default');

-- Role Action Mapping of Voucher API with Asset Administrator
insert into eg_roleaction(roleCode,actionid,tenantid) values ('Asset Administrator',(select id from eg_action where name = 'Create Voucher Rest'),'default');

--rollback delete from eg_roleaction where roleCode in ('SUPERUSER','Asset Administrator') and actionid = (select id from eg_action where name = 'Create Voucher Rest');

--rollback delete from eg_action where name = 'Create Voucher Rest';

--rollback delete from service where name = 'Journal Vouchers';
