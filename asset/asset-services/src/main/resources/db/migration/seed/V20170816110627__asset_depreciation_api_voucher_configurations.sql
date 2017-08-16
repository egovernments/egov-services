insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'DepreciationVoucherParams','Asset Depreciation Voucher Parameters',
	'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'DepreciationVoucherParams' and tenantid = 'default'),'{"Fund":"1","Function":"13"}',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');