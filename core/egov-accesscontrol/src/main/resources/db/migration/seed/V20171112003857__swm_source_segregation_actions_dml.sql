insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'SourceSegregation', 'Source Segregation', true, 'Source Segregation', 1, (select id from service where code = 'SWM Masters' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SourceSegregation Create','/swm-services/sourcesegregations/_create','SourceSegregation',null,(select id from service where code='SourceSegregation' and tenantid='default'),0,'Create Source Segregation',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SourceSegregation Update','/swm-services/sourcesegregations/_update','SourceSegregation',null,(select id from service where code='SourceSegregation' and tenantid='default'),0,'Update Source Segregation',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SourceSegregation Search','/swm-services/sourcesegregations/_search','SourceSegregation',null,(select id from service where code='SourceSegregation' and tenantid='default'),0,'View Source Segregation',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'SourceSegregation Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'SourceSegregation Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'SourceSegregation Search' ),'default');