update egasset_assetconfigurationvalues set value = '{"Fund":"02"}' where keyid in 
	(select id from egasset_assetconfiguration where keyname = 'VoucherParams');