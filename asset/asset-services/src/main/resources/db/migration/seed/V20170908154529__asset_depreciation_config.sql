insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'AssetBatchSize','Asset Batch Size',
	'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'AssetBatchSize' and tenantid = 'default'),'500',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'DepreciationCFactor','Depreciation Multiple Factor For Half Financial Year',
	'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'DepreciationCFactor' and tenantid = 'default'),'0.5',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');