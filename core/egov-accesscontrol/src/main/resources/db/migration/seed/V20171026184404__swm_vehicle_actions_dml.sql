insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'SWM Masters', 'SWM Masters', true, 'Masters', 1, (select id from service where code = 'SWM' and tenantId='default'), 'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Vehicle Create','/swm-services/vehicles/_create','SWM Masters',null,(select id from service where code='SWM Masters' and tenantid='default'),0,'Vehicle Create',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Vehicle Update','/swm-services/vehicles/_update','SWM Masters',null,(select id from service where code='SWM Masters' and tenantid='default'),0,'Vehicle Update',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Vehicle Search','/swm-services/vehicles/_search','SWM Masters',null,(select id from service where code='SWM Masters' and tenantid='default'),0,'Vehicle Search',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Vehicle Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Vehicle Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Vehicle Search' ),'default');

