insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'REGISTER', 'Stamp/Register', true, 'Stamp/Register', 1, (select id from service where code = 'LEGALCASEMASTERS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Stamp/Register Create','/lcms-services/legalcase/register/_create','REGISTER',null,(select id from service where code='REGISTER' and tenantid='default'),0,'Create Stamp/Register',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Stamp/Register Update','/lcms-services/legalcase/register/_update','REGISTER',null,(select id from service where code='REGISTER' and tenantid='default'),0,'Update Stamp/Register',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Stamp/Register Search','/lcms-services/legalcase/register/_search','REGISTER',null,(select id from service where code='REGISTER' and tenantid='default'),0,'Search Stamp/Register',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Stamp/Register Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Stamp/Register Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Stamp/Register Search' ),'default');


insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'PARAWISECOMMENT', 'Parawise Comment', false, 'Parawise Comment', 1, (select id from service where code = 'CASE' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Parawise Comment Create','/lcms-services/legalcase/parawisecomment/_create','PARAWISECOMMENT',null,(select id from service where code='PARAWISECOMMENT' and tenantid='default'),0,'Create Parawise Comment',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Parawise Comment Update','/lcms-services/legalcase/parawisecomment/_update','PARAWISECOMMENT',null,(select id from service where code='PARAWISECOMMENT' and tenantid='default'),0,'Update Parawise Comment',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Parawise Comment Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Parawise Comment Update' ),'default');


insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'CASEREGISTRATION', 'Case Registration', false, 'Case Registration', 1, (select id from service where code = 'CASE' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Case Registration Create','/lcms-services/legalcase/case/_registration','CASEREGISTRATION',null,(select id from service where code='CASEREGISTRATION' and tenantid='default'),0,'Create Case Registration',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Case Registration Create' ),'default');


insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'VAKALATNAMA', 'Vakalatnama Generation', false, 'Vakalatnama Generation', 1, (select id from service where code = 'CASE' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Vakalatnama Generation','/lcms-services/legalcase/case/_vakalatnamageneration','VAKALATNAMA',null,(select id from service where code='VAKALATNAMA' and tenantid='default'),0,'Vakalatnama Generation',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Vakalatnama Generation' ),'default');


insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'HEARINGDETAILS', 'Hearing Details', false, 'Hearing Details', 1, (select id from service where code = 'CASE' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Hearing Details Create','/lcms-services/legalcase/hearingdetails/_create','HEARINGDETAILS',null,(select id from service where code='HEARINGDETAILS' and tenantid='default'),0,'Create Hearing Details',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Hearing Details Update','/lcms-services/legalcase/hearingdetails/_update','HEARINGDETAILS',null,(select id from service where code='HEARINGDETAILS' and tenantid='default'),0,'Update Hearing Details',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Hearing Details Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Hearing Details Update' ),'default');


insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'SUMMON', 'Summon', false, 'Summon', 1, (select id from service where code = 'CASE' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Assign Advocate','/lcms-services/legalcase/summon/_assignadvocate','SUMMON',null,(select id from service where code='SUMMON' and tenantid='default'),0,'Assign Advocate',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Assign Advocate' ),'default');


insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'LEGALCASENOTICE', 'Legal Case Notice', false, 'Legal Case Notice', 1, (select id from service where code = 'LEGALCASETRANSACTIONS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Legal Case Notice Create','/lcms-services/legalcase/notice/_create','LEGALCASENOTICE',null,(select id from service where code='LEGALCASENOTICE' and tenantid='default'),0,'Create Legal Case Notice',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Legal Case Notice Update','/lcms-services/legalcase/notice/_update','LEGALCASENOTICE',null,(select id from service where code='LEGALCASENOTICE' and tenantid='default'),0,'Update Legal Case Notice',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Legal Case Notice Search','/lcms-services/legalcase/notice/_search','LEGALCASENOTICE',null,(select id from service where code='LEGALCASENOTICE' and tenantid='default'),0,'Search Legal Case Notice',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Legal Case Notice Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Legal Case Notice Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Legal Case Notice Search' ),'default');