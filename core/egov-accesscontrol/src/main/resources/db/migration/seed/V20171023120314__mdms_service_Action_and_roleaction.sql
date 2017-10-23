insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) 
values (nextval('SEQ_SERVICE'), 'MDMS', 'MDMS SERVICE', false, 'MDMS Service', 1, null, 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'MDMS Search','/egov-mdms-services/v1/_search','MDMS Search',null,(select id from service where code='MDMS' and tenantid='default'),0,'MDMS Search',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'MDMS Search' ),'default');
