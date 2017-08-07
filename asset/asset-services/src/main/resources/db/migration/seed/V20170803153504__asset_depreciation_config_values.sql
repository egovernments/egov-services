insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'AssetDefaultCapitalizedValue','Asset Default Capitalized Value',
	'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'AssetMinimumValue','Asset Minimum Value',
	'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfiguration(id,keyname,description,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) values(
	(select nextval('seq_egasset_assetconfiguration')),'DepreciationSeparationDate','Asset Depreciation Separation Date',
	'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'AssetDefaultCapitalizedValue' and tenantid = 'default'),'1',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'AssetMinimumValue' and tenantid = 'default'),'5000',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');

insert into egasset_assetconfigurationvalues(id,keyid,value,effectivefrom,createdby,createddate,lastmodifiedby,lastmodifieddate,tenantid) 
	values((select nextval('seq_egasset_assetconfigurationvalues')),(select id from egasset_assetconfiguration where 
		keyname = 'DepreciationSeparationDate' and tenantid = 'default'),'9/30/23/59/59',(extract(epoch from now()) * 1000),
		'1',(extract(epoch from now()) * 1000),'1',(extract(epoch from now()) * 1000),'default');
