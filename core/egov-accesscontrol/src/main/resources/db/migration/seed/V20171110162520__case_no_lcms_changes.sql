insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'CASENOSEARCH', 'Case No Search', false, 'Case No Search', 1, (select id from service where code = 'CASE' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Case No Search','/lcms-services/legalcase/caseno/_search','CASENOSEARCH',null,(select id from service where code='CASENOSEARCH' and tenantid='default'),1,'Case No Search',false,1,now(),1,now());
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Case No Search' ),'default');