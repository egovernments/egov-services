insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'COLLECTION-REPORTS','Collection-Reports',true,null,'Reports',3,
(select id from service where code='COLLECTION'),'default');

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'GetReportData','/collection-services/report/_get','COLLECTION-REPORTS',null,1,'Get Report Data',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'GetReportMetadata','/collection-services/report/metadata/_get','COLLECTION-REPORTS',null,2,'Get Report Metadata',false,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'ReloadReport','/collection-services/report/metadata/_reload','COLLECTION-REPORTS',null,3,'Reload Report',false,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='GetReportData'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='GetReportMetadata'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='ReloadReport'),'default');





