
-- eg_action table data--
insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'CreateLocalization','/localization/messages/v1/_create','','','',1,'Create Localization',true,1,now(),1,now());

insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'SearchLocalization','/localization/messages/v1/_search','','','',1,'Search Localization',true,1,now(),1,now());


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'UpdateLocalization','/localization/messages/v1/_update','','','',1,'Update Localization',true,1,now(),1,now());


insert into eg_action(id,name,url,servicecode,queryparams,parentmodule,ordernumber,displayname,enabled,createdby,createddate,lastmodifiedby,lastmodifieddate)
values(nextval('SEQ_EG_ACTION'),'DeleteLocalization','/localization/messages/v1/_delete','','','',1,'Delete Localization',true,1,now(),1,now());




-- eg_roleaction table data--

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='CreateLocalization'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='SearchLocalization'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='UpdateLocalization'),'default');

insert into eg_roleaction(roleCode,actionid,tenantId)values('SUPERUSER',(select id from eg_action where name='DeleteLocalization'),'default');

