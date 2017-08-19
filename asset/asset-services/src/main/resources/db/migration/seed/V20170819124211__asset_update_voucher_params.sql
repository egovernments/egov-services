update egasset_assetconfiguration  set keyname = 'VoucherParams',description='Asset Voucher Parameters' where keyname = 'DepreciationVoucherParams';

update egasset_assetconfigurationvalues set value = '{"Fund":"01","Function":"0600"}' where keyid in 
	(select id from egasset_assetconfiguration where keyname = 'VoucherParams');
	