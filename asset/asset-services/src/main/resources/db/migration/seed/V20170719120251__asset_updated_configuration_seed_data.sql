update egasset_assetconfiguration set description = 'Enable or Disable voucher generation for asset revaluation,disposal and depreciation'
	where keyname = 'EnableVoucherGeneration' and tenantid = 'default';
update egasset_assetconfiguration set description = 'Voucher Name for Asset Revaluation' where keyname = 'AssetRevaluationVoucherName' and 
	tenantid = 'default';
update egasset_assetconfiguration set description = 'Voucher Description for Asset Revaluation' where 
	keyname = 'AssetRevaluationVoucherDescription' and tenantid = 'default';
update egasset_assetconfiguration set description = 'Voucher Name for Asset Disposal or Sale' where keyname = 'AssetDisposalVoucherName' and 
	tenantid = 'default';
update egasset_assetconfiguration set description = 'Voucher Description for Asset Disposal or Sale' where 
	keyname = 'AssetDisposalVoucherDescription' and tenantid = 'default';

update egasset_assetconfigurationvalues set value = 'Asset Revaluation' where 
	keyid = (select id from egasset_assetconfiguration where keyname = 'AssetRevaluationVoucherName') and tenantid = 'default';
update egasset_assetconfigurationvalues set value = 'Asset Revaluation Journal Voucher' where 
	keyid = (select id from egasset_assetconfiguration where keyname = 'AssetRevaluationVoucherDescription') and tenantid = 'default';
update egasset_assetconfigurationvalues set value = 'Asset Disposal or Sale' where 
	keyid = (select id from egasset_assetconfiguration where keyname = 'AssetDisposalVoucherName') and tenantid = 'default';
update egasset_assetconfigurationvalues set value = 'Asset Disposal or Sale Journal Voucher' where 
	keyid = (select id from egasset_assetconfiguration where keyname = 'AssetDisposalVoucherDescription') and tenantid = 'default';


--rollback update egasset_assetconfigurationvalues set value = 'Asset Revaluation Voucher' where keyid = 
--rollback 	(select id from egasset_assetconfiguration where keyname = 'AssetRevaluationVoucherName') and tenantid = 'default';
--rollback update egasset_assetconfigurationvalues set value = 'Creating Voucher for Asset Revaluation' where keyid = 
--rollback 	(select id from egasset_assetconfiguration where keyname = 'AssetRevaluationVoucherDescription') and tenantid = 'default';
--rollback update egasset_assetconfigurationvalues set value = 'Asset Disposal Voucher' where keyid = 
--rollback 	(select id from egasset_assetconfiguration where keyname = 'AssetDisposalVoucherName') and tenantid = 'default';
--rollback update egasset_assetconfigurationvalues set value = 'Creating Voucher for Asset Disposal' where keyid = 
--rollback 	(select id from egasset_assetconfiguration where keyname = 'AssetDisposalVoucherDescription') and tenantid = 'default';

--rollback update egasset_assetconfiguration set description = 'Enable/Disable voucher generation for asset revaluation,disposal and depreciation' 
--rollback 	where keyname = 'EnableVoucherGeneration' and tenantid = 'default';
--rollback update egasset_assetconfiguration set description = 'Voucher for Asset Revaluation' where keyname = 'AssetRevaluationVoucherName' 
--rollback 	and tenantid = 'default';
--rollback update egasset_assetconfiguration set description = 'Creating Voucher for Asset Revaluation' where 
--rollback 	keyname = 'AssetRevaluationVoucherDescription' and tenantid = 'default';
--rollback update egasset_assetconfiguration set description = 'Voucher for Asset Disposal' where keyname = 'AssetDisposalVoucherName' and 
--rollback 	tenantid = 'default';
--rollback update egasset_assetconfiguration set description = 'Creating Voucher for Asset Disposal' where 
--rollback 	keyname = 'AssetDisposalVoucherDescription' and tenantid = 'default';