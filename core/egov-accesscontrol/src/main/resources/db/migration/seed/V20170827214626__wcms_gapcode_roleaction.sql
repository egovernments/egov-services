insert into service (id, code, name, enabled, contextroot, displayname, ordernumber, parentmodule, tenantId)
VALUES(nextval('SEQ_SERVICE'),'GAPCODEMASTER','Gapcode Master',true,null,'Gapcode Master',22,
(select id from service where code='WCMS MASTERS' and tenantid='default'),'default');



insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateGapcodeMaster','/wcms/masters/gapcode/_create','GAPCODEMASTER',null,1,'Create Gapcode',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'UpdateGapcodeMaster','/wcms/masters/gapcode/_update','GAPCODEMASTER',null,2,'Update Gapcode',true,1,now(),1,now());

insert into eg_action(id, name,url,servicecode,queryparams,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchGapcodeMaster','/wcms/masters/gapcode/_search','GAPCODEMASTER',null,3,'View Gapcode',true,1,now(),1,now());


insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='CreateGapcodeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='UpdateGapcodeMaster'),'default');
insert into eg_roleaction(roleCode,actionid,tenantid)values('SUPERUSER',(select id from eg_action where name='SearchGapcodeMaster'),'default');
