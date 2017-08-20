update service set name='TariffCategory' where name='PropertyCategory' and tenantId='default';
update service set name='TariffUsage' where name='PropertyUsage' and tenantId='default';
update service set name='TariffConnectionSize' where name='PropertyConnectionSize' and tenantId='default';



update eg_action set displayname='Update Property ConnectionSize' where name='ModifyPropertyPipeSizeApi';
update eg_action set displayname='View Property ConnectionSize' where name='SearchPropertyPipeSizeApi';
update eg_action set displayname='Create Tariff Category' where name='CreatPropertyCategoryApi';
update eg_action set displayname='View Tariff Category' where name='SearchPropertyCategoryApi';
update eg_action set displayname='Update Tariff Category' where name='ModifyPropertyCategoryApi';
update eg_action set displayname='Create Tariff Usage' where name='CreatPropertyUsageApi';
update eg_action set displayname='View Tariff Usage' where name='SearchPropertyUsageApi';
update eg_action set displayname='Update Tariff Usage' where name='ModifyPropertyUsageApi';
update eg_action set displayname='Update Tariff ConnectionSize' where name='ModifyPropertyPipeSizeApi';
update eg_action set displayname='Create Tariff ConnectionSize' where name='CreatPropertyPipeSizeApi';
update eg_action set displayname='View Tariff ConnectionSize' where name='SearchPropertyPipeSizeApi';

