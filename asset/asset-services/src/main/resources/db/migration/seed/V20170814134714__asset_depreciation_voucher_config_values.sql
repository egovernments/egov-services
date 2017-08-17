insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'AssetDepreciationVoucherName','Voucher Name for Asset Depreciation',
	'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'AssetDepreciationVoucherDescription','Voucher Description for Asset Depreciation',
	'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'AssetDepreciationVoucherName' and tenantid = 'default'),'Asset Depreciation',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'AssetDepreciationVoucherDescription' and tenantid = 'default'),'Asset Depreciation Journal Voucher',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

--rollback delete from egasset_assetconfigurationvalues where keyid in ((select id from egasset_assetconfiguration where 
--rollback 		keyname = 'AssetDepreciationVoucherName' and tenantid = 'default'),(select id from egasset_assetconfiguration where 
--rollback 		keyname = 'AssetDepreciationVoucherDescription' and tenantid = 'default')) and tenantid = 'default';

--rollback delete from egasset_assetconfiguration where name in ('AssetDepreciationVoucherName','AssetDepreciationVoucherDescription') and tenantid = 'default';