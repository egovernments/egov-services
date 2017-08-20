update egasset_assetconfigurationvalues set value = 'false' where keyid in (select id from egasset_assetconfiguration where keyname = 'EnableVoucherGeneration');

--rollback update egasset_assetconfigurationvalues set value = 'true' where keyid in (select id from egasset_assetconfiguration where keyname = 'EnableVoucherGeneration');