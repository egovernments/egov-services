update service set ordernumber=2 where code = 'OPINION' and tenantId='default';
update service set ordernumber=3 where code = 'ADVOCATEPAYMENT' and tenantId='default';
update service set ordernumber=4 where code = 'LEGALCASENOTICE' and tenantId='default';

update eg_action set enabled=false  where name = 'Advocate Update';
update eg_action set enabled=false  where name = 'Opinion Update';
update eg_action set enabled=false  where name = 'AdvocatePayment Update';
update eg_action set enabled=false  where name = 'Stamp/Register Update';

insert into service (id, code, name, enabled, displayname, ordernumber, parentmodule, tenantId) values (nextval('SEQ_SERVICE'), 'REFERENCEEVIDENCE', 'Reference Evidence', false, 'Reference Evidence', 1, (select id from service where code = 'CASE' and tenantId='default'), 'default');

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Reference Evidence Create','/lcms-services/legalcase/referenceevidence/_create','REFERENCEEVIDENCE',null,(select id from service where code='REFERENCEEVIDENCE' and tenantid='default'),1,'Create Reference Evidence',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'Reference Evidence Update','/lcms-services/legalcase/referenceevidence/_update','REFERENCEEVIDENCE',null,(select id from service where code='REFERENCEEVIDENCE' and tenantid='default'),2,'Update Reference Evidence',false,1,now(),1,now());

insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Reference Evidence Create' ),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name = 'Reference Evidence Update' ),'default');