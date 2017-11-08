insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values (nextval('SEQ_EG_ACTION'),'MovableRegisterMetadata','/report/asset/metadata/_get','Asset Register Report',null,(select id from service where name='Asset Register Report'and tenantId='default'),1,'Movable Asset Register ',false,1,now(),1,now());


update eg_action set displayname='Immovable Asset Register' where name='AssetImmovableRegister' AND displayname='Asset Immovable Register';


update eg_action set displayname='Asset Immovable Create',name='AssetImmovableCreate' where name='CreateAssetImmovable' AND displayname='Create Asset Immovable';

update eg_action set displayname='Create Immovable Asset',name='CreateImmovableAsset' where name='AssetImmovableCreate' AND displayname='Asset Immovable Create';


update eg_action set displayname='Modify Immovable Asset',name='ModifyImmovableAsset' where name='ModifyAssetImmovable' AND displayname='Modify Asset Immovable';

update eg_action set displayname='View Immovable Asset',name='ViewImmovableAsset' where name='ViewAssetImmovable' AND displayname='View Asset Immovable';


update eg_action set displayname='Create Movable Asset',name='CreateMovableAsset' where name='CreateAssetMovable' AND displayname='Create Asset movable';

update eg_action set displayname='Modify Movable Asset',name='ModifyMovableAsset' where name='ModifyAssetMovable' AND displayname='Modify Asset Movable';

update eg_action set displayname='View Movable Asset',name='ViewMovableAsset' where name='SearchAssetMovable' AND displayname='Search Asset Movable';




