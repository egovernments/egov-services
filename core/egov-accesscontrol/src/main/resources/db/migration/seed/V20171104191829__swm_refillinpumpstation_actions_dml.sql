insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'Refillin Pump Station', 'Refillin Pump Station', true, 'Refillin Pump Station', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'RefillinPumpStation Create','/swm-services/refillinpumpstation/_create','RefillinPumpStation',null,(select id from service where code='RefillinPumpStation' and tenantid='default'),0,'Create Refillin Pump Station',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'RefillinPumpStation Update','/swm-services/refillinpumpstation/_update','RefillinPumpStation',null,(select id from service where code='RefillinPumpStation' and tenantid='default'),0,'Update Refillin Pump Station',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'RefillinPumpStation Search','/swm-services/refillinpumpstation/_search','RefillinPumpStation',null,(select id from service where code='RefillinPumpStation' and tenantid='default'),0,'View Refillin Pump Station',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'RefillinPumpStation Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'RefillinPumpStation Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'RefillinPumpStation Search' ),'default');