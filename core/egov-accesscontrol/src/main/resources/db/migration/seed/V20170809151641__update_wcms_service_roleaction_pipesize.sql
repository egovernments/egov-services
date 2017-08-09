update service set displayname='Connection Size Master' where name='PipeSize Master' and tenantId='default';
update service set displayname='Property ConnectionSize Master' where name='PropertyPipeSize' and tenantId='default';


update eg_action set displayname='Create Connection Size' where name='CreatePipeSizeMasterApi';
update eg_action set displayname='View Connection Size' where name='SearchPipeSizeMaster';
update eg_action set displayname='Update Connection Size' where name='ModifyPipeSizeMasterApi';
update eg_action set displayname='Create Property ConnectionSize' where name='CreatPropertyPipeSizeApi';
update eg_action set displayname='View Property ConnectionSize' where name='ModifyPropertyPipeSizeApi';
update eg_action set displayname='Update Property ConnectionSize' where name='SearchPropertyPipeSizeApi';

