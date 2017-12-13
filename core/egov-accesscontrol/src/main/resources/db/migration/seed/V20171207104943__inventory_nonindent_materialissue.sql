insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'),
'NON INDENT ISSUES', 'Non Indent Issues', true, 'Non Indent Issues',13, (select id from service where code = 'INVENTORY TRANSACTIONS'
and tenantId = 'default') , 'default');

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Non Indent Issue Create','/inventory-services/materialissues-ni/_create','NON INDENT ISSUES',
null,1,'Create Non Indent Issue',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Non Indent Issue Update','/inventory-services/materialissues-ni/_update','NON INDENT ISSUES',
null,2,'Update Non Indent Issue',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,
lastmodifieddate) values (nextval('SEQ_EG_ACTION'),'Non Indent Issue Search','/inventory-services/materialissues-ni/_search','NON INDENT ISSUES',
null,3,'Search Non Indent Issue',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Non Indent Issue Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Non Indent Issue Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Non Indent Issue Search' ),'default');