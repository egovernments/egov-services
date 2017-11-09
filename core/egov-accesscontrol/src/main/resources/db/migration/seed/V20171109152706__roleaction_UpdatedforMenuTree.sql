-- updating display names of asset moveable

update service set name='Immovable Asset',code='Immovable Asset',displayname='Immovable Asset',parentmodule=(select id from service where name='Asset Masters' and tenantid='default') where name='Asset Immovable' and tenantid='default'; 

update service set name='Movable Asset',code='Movable Asset',displayname='Movable Asset',parentmodule=(select id from service where name='Asset Masters' and tenantid='default') where name='Asset Movable' and tenantid='default';


--updating hierarchy by removing asset
update service set enabled='f'  where name ='Asset' and tenantid='default';


update eg_action set servicecode=(select code from service where name='Movable Asset' and tenantid='default') where servicecode='Asset Movable';

update eg_action set servicecode=(select code from service where name='Immovable Asset' and tenantid='default') where servicecode='Asset Immovable';







