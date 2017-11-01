insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'LEGALCASE', 'Legal Case Management', true, 'Legal Case Management', 1, null, 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'LEGALCASEMASTERS', 'Legal Case Masters', true, 'Legal Case Masters', 1, (select id from service where code = 'LEGALCASE' and tenantId='default'), 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'LEGALCASETRANSACTIONS', 'Legal Case Transactions', true, 'Legal Case Transactions', 1, (select id from service where code = 'LEGALCASE' and tenantId='default'), 'default');

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'CASE', 'Case', true, 'Case', 1, (select id from service where code = 'LEGALCASETRANSACTIONS' and tenantId='default'), 'default');
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'OPINION', 'Opinion', true, 'Opinion', 1, (select id from service where code = 'LEGALCASETRANSACTIONS' and tenantId='default'), 'default');
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'ADVOCATEPAYMENT', 'Payment Request', true, 'Payment Request', 1, (select id from service where code = 'LEGALCASETRANSACTIONS' and tenantId='default'), 'default');
insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'ADVOCATE', 'Advocate', true, 'Advocate', 1, (select id from service where code = 'LEGALCASEMASTERS' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'New Case Create','/legalcase/summon/_create','CASE',null,(select id from service where code='CASE' and tenantid='default'),0,'Create New Case',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Legacy Case Create','/legalcase/case/_dataentry','CASE',null,(select id from service where code='CASE' and tenantid='default'),0,'Create Legacy Case',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Case Search','/legalcase/case/_search','CASE',null,(select id from service where code='CASE' and tenantid='default'),0,'Search Case',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'New Case Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Legacy Case Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Case Search' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Advocate Create','/legalcase/advocate/_create','ADVOCATE',null,(select id from service where code='ADVOCATE' and tenantid='default'),0,'Create Advocate',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Advocate Update','/legalcase/advocate/_update','ADVOCATE',null,(select id from service where code='ADVOCATE' and tenantid='default'),0,'Update Advocate',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Advocate Search','/legalcase/advocate/_search','ADVOCATE',null,(select id from service where code='ADVOCATE' and tenantid='default'),0,'Search Advocate',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Advocate Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Advocate Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Advocate Search' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Opinion Create','/legalcase/opinion/_create','OPINION',null,(select id from service where code='OPINION' and tenantid='default'),0,'Create Opinion',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Opinion Update','/legalcase/opinion/_update','OPINION',null,(select id from service where code='OPINION' and tenantid='default'),0,'Update Opinion',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Opinion Search','/legalcase/opinion/_search','OPINION',null,(select id from service where code='OPINION' and tenantid='default'),0,'Search Opinion',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Opinion Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Opinion Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Opinion Search' ),'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'AdvocatePayment Create','/legalcase/advocatepayment/_create','ADVOCATEPAYMENT',null,(select id from service where code='ADVOCATEPAYMENT' and tenantid='default'),0,'Create Advocate Payment',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'AdvocatePayment Update','/legalcase/advocatepayment/_update','ADVOCATEPAYMENT',null,(select id from service where code='ADVOCATEPAYMENT' and tenantid='default'),0,'Update Advocate Payment',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'AdvocatePayment Search','/legalcase/advocatepayment/_search','ADVOCATEPAYMENT',null,(select id from service where code='ADVOCATEPAYMENT' and tenantid='default'),0,'Search Advocate Payment',true,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'AdvocatePayment Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'AdvocatePayment Update' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'AdvocatePayment Search' ),'default');