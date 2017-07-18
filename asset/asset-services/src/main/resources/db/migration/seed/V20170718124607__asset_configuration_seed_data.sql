insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'EnableVoucherGeneration','Enable/Disable voucher generation for asset revaluation,disposal and depreciation',
	'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'AssetRevaluationVoucherName','Voucher for Asset Revaluation','1',
		(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'AssetRevaluationVoucherDescription','Creating Voucher for Asset Revaluation','1',
		(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'AssetDisposalVoucherName','Voucher for Asset Disposal','1',
		(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
		
insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'AssetDisposalVoucherDescription','Creating Voucher for Asset Disposal','1',
		(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'EnableVoucherGeneration' and tenantid = 'default'),'false',(extract(epoch from now()) * 1000),'1',
		(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'AssetRevaluationVoucherName' and tenantid = 'default'),'Asset Revaluation Voucher',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'AssetRevaluationVoucherDescription' and tenantid = 'default'),'Creating Voucher for Asset Revaluation',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'AssetDisposalVoucherName' and tenantid = 'default'),'Asset Disposal Voucher',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
		
insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'AssetDisposalVoucherDescription' and tenantid = 'default'),'Creating Voucher for Asset Disposal',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

--rollback delete from egasset_assetconfigurationvalues;
--rollback delete from egasset_assetconfiguration;