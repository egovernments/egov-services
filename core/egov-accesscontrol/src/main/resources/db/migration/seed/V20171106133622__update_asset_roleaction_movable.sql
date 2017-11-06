update service set name='Asset Movable',code='Asset Movable',displayname='Asset Movable',parentmodule=(select id from service where name='Asset' and tenantId='default')
where name='Asset movable' AND displayname='Asset movable' ANd tenantid='default';

update eg_action set servicecode='Asset Movable',parentmodule=(select id from service where name='Asset Movable' AND tenantId='default')
where name='CreateAssetMovable' AND displayname='Create Asset movable';


update eg_action set servicecode='Asset Movable',parentmodule=(select id from service where name='Asset Movable' AND tenantId='default')
where name='ModifyAssetMovable' AND displayname='Modify Asset Movable';


update eg_action set servicecode='Asset Movable',parentmodule=(select id from service where name='Asset Movable' AND tenantId='default')
where name='SearchAssetMovable' AND displayname='Search Asset Movable';
