insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CollectionPoint Create','/swm-services/vehicles/_create','SWM Masters',null,(select id from service where code='SWM Masters' and tenantid='default'),0,'Create Collection Point',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CollectionPoint Update','/swm-services/vehicles/_update','SWM Masters',null,(select id from service where code='SWM Masters' and tenantid='default'),0,'Update Collection Point',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CollectionPoint Search','/swm-services/vehicles/_search','SWM Masters',null,(select id from service where code='SWM Masters' and tenantid='default'),0,'View Collection Point',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'CollectionPoint Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'CollectionPoint Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'CollectionPoint Search' ),'default');

update eg_action set displayname = 'Create Vehicle' where name = 'Vehicle Create';

update eg_action set displayname = 'Update Vehicle' where name = 'Vehicle Update';

update eg_action set displayname = 'View Vehicle' where name = 'Vehicle Search';
