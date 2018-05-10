update egasset_assetconfigurationvalues  set value='10/1/0/0/0' where keyid in(select id from egasset_assetconfiguration where 
		keyname = 'DepreciationSeparationDate');