insert into eg_ms_role (name, code, description, createddate, createdby, lastmodifiedby, lastmodifieddate, version) values ('TL Creator', 'TL_CREATOR', 'Who has a access to Trade License Services', now(), 1, 1, now(), 0);

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TLTRANSACTIONS', 'Trade License Transactions', true, 'Transactions', 1, (select id from service where name ='Trade License' and code='TRADELICENSE' and tenantId='default'), 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'TLSEARCH', 'Trade License Search', true, 'Search', 1, (select id from service where name ='Trade License' and code='TRADELICENSE' and tenantId='default'), 'default');


insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'CreateLegacyLicense','/tl-services/license/v1/_create',
'TLTRANSACTIONS',null,(select id from service where code='TLTRANSACTIONS' and tenantid='default'),1,
'Create Legacy License',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_CREATOR',
 (select id from eg_action where name = 'CreateLegacyLicense' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'CreateLegacyLicense' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'CreateLegacyLicense' ),'default');





 insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,
displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate) 
values(nextval('SEQ_EG_ACTION'),'SearchLicense','/tl-services/license/v1/_search',
'TLSEARCH',null,(select id from service where code='TLSEARCH' and tenantid='default'),1,
'Search License ',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_CREATOR',
 (select id from eg_action where name = 'SearchLicense' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',
 (select id from eg_action where name = 'SearchLicense' ),'default');

insert into eg_roleaction(roleCode,actionid,tenantid)values('TL_ADMIN',
 (select id from eg_action where name = 'SearchLicense' ),'default');


