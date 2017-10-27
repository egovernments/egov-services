insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'Vehicle', 'Vehicle', true, 'Vehicle', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

update eg_action set servicecode = 'Vehicle',parentmodule = (select id from service where code='Vehicle' and tenantid='default') where name in ('Vehicle Create');

update eg_action set servicecode = 'Vehicle',parentmodule = (select id from service where code='Vehicle' and tenantid='default') where name in ('Vehicle Update');

update eg_action set servicecode = 'Vehicle',parentmodule = (select id from service where code='Vehicle' and tenantid='default') where name in ('Vehicle Search');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'Collection Point', 'Collection Point', true, 'Collection Point', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

update eg_action set servicecode = 'Collection Point',parentmodule = (select id from service where code='Collection Point' and tenantid='default'),url='/swm-services/collectionpoints/_create' where name in ('CollectionPoint Create');

update eg_action set servicecode = 'Collection Point',parentmodule = (select id from service where code='Collection Point' and tenantid='default'),url='/swm-services/collectionpoints/_update' where name in ('CollectionPoint Update');

update eg_action set servicecode = 'Collection Point',parentmodule = (select id from service where code='Collection Point' and tenantid='default'),url='/swm-services/collectionpoints/_search' where name in ('CollectionPoint Search');
