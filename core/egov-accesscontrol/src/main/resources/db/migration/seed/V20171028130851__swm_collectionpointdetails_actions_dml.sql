insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'Collection Point Details', 'Collection Point Details', true, 'Collection Point Details', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CollectionPointDetails Create','/swm-services/collectionpointdetails/_create','Collection Point Details',null,(select id from service where code='Collection Point Details' and tenantid='default'),0,'Create Collection Point Details',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CollectionPointDetails Update','/swm-services/collectionpointdetails/_update','Collection Point Details',null,(select id from service where code='Collection Point Details' and tenantid='default'),0,'Update Collection Point Details',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CollectionPointDetails Search','/swm-services/collectionpointdetails/_search','Collection Point Details',null,(select id from service where code='Collection Point Details' and tenantid='default'),0,'View Collection Point Details',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'CollectionPointDetails Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'CollectionPointDetails Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'CollectionPointDetails Search' ),'default');